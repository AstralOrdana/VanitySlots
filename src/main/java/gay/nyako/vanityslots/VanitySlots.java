/*
    VanitySlots
    Copyright (C) 2020  Alexandra Tildea

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package gay.nyako.vanityslots;

import dev.emi.trinkets.api.TrinketsApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class VanitySlots implements ModInitializer {
	public static VanitySlotsConfig CONFIG;

	@Override
	public void onInitialize() {
		RegisterItems.register();

		registerPredicate("head", EquipmentSlot.HEAD);
		registerPredicate("chest", EquipmentSlot.CHEST);
		registerPredicate("legs", EquipmentSlot.LEGS);
		registerPredicate("feet", EquipmentSlot.FEET);

		AutoConfig.register(VanitySlotsConfig.class, GsonConfigSerializer::new);

		CONFIG = AutoConfig.getConfigHolder(VanitySlotsConfig.class).getConfig();
	}

	public boolean isInBlacklist(Item item) {
		String itemID = Registries.ITEM.getId(item).toString();
		return CONFIG.itemBlacklist.contains(itemID);
	}

	public void registerPredicate(String identifier, EquipmentSlot slot) {
		TrinketsApi.registerTrinketPredicate(new Identifier("vanityslots", identifier), (stack, ref, entity) -> {
			if (isInBlacklist(stack.getItem())) return TriState.FALSE;
			if (stack.getItem() instanceof ArmorItem) {
				if (((ArmorItem) stack.getItem()).getSlotType() == slot) {
					return TriState.TRUE;
				}
			}
			if ((slot == EquipmentSlot.HEAD) && stack.isIn(TagKey.of(RegistryKeys.ITEM, new Identifier("vanityslots", "wearable_on_head")))) {
				return TriState.TRUE;
			}
			return TriState.DEFAULT;
		});

		TrinketsApi.registerTrinketPredicate(new Identifier("vanityslots", "quick_" + identifier), (stack, ref, entity) -> {
			// If the vanilla slot is empty...
			if (entity.getEquippedStack(slot).isEmpty()) {
				// We don't want to shift click into our custom one.
				return TriState.FALSE;
			}
			// There's something in the vanilla slot, so shift
			// clicking into the vanity slot should be allowed.
			return TriState.TRUE;
		});
	}
}
