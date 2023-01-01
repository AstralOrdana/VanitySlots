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

import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.Optional;

public class VanitySlots implements ModInitializer {
	public static VanitySlotsConfig CONFIG;

	public static boolean USE_VANITY;

	@Override
	public void onInitialize() {
		RegisterItems.register();

		registerPredicate("head", EquipmentSlot.HEAD);
		registerPredicate("chest", EquipmentSlot.CHEST);
		registerPredicate("legs", EquipmentSlot.LEGS);
		registerPredicate("feet", EquipmentSlot.FEET);

		AutoConfig.register(VanitySlotsConfig.class, GsonConfigSerializer::new);

		CONFIG = AutoConfig.getConfigHolder(VanitySlotsConfig.class).getConfig();
		USE_VANITY = false;
	}

	public static ItemStack getVanityStack(LivingEntity entity, EquipmentSlot slot) {
		if (!(entity instanceof PlayerEntity)) return ItemStack.EMPTY;

		Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(entity);
		if (component.isPresent()) {
			TrinketComponent component2 = component.get();
			for (var equipped : component2.getAllEquipped()) {
				SlotType slotType = equipped.getLeft().inventory().getSlotType();
				ItemStack itemStack = equipped.getRight();
				if (!slotType.getName().equals("vanity")) {
					continue;
				}
				if (itemStack.isEmpty()) continue;

				if ((slot == EquipmentSlot.FEET) && (slotType.getGroup().equals("feet"))) {
					return itemStack;
				}
				if ((slot == EquipmentSlot.LEGS) && (slotType.getGroup().equals("legs"))) {
					return itemStack;
				}
				if ((slot == EquipmentSlot.CHEST) && (slotType.getGroup().equals("chest"))) {
					return itemStack;
				}
				if ((slot == EquipmentSlot.HEAD) && (slotType.getGroup().equals("head"))) {
					return itemStack;
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public static ItemStack getEquippedStack(LivingEntity entity, EquipmentSlot slot) {
		if (!(entity instanceof PlayerEntity))
			return entity.getEquippedStack(slot);

		ItemStack vanity = getVanityStack(entity, slot);
		if (!vanity.isEmpty())
		{
			return vanity;
		}

		return entity.getEquippedStack(slot);
	}

	public boolean isInBlacklist(Item item) {
		String itemID = Registry.ITEM.getId(item).toString();
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
			if ((slot == EquipmentSlot.HEAD) && stack.isIn(TagKey.of(Registry.ITEM_KEY, new Identifier("vanityslots", "wearable_on_head")))) {
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
