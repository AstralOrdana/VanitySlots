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
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;

public class VanitySlots implements ModInitializer {

	public static final String MODID = "vanityslots";

	//public static final Item GLOWING_LEATHER = new Item(new FabricItemSettings().group(ItemGroup.MISC));

	@Override
	public void onInitialize() {
		RegisterItems.register();

		registerPredicate("head", EquipmentSlot.HEAD);
		registerPredicate("chest", EquipmentSlot.CHEST);
		registerPredicate("legs", EquipmentSlot.LEGS);
		registerPredicate("feet", EquipmentSlot.FEET);
	}

	public void registerPredicate(String identifier, EquipmentSlot slot) {
		TrinketsApi.registerTrinketPredicate(new Identifier("vanityslots", identifier), (stack, ref, entity) -> {
			if (stack.getItem() instanceof ArmorItem) {
				if (((ArmorItem) stack.getItem()).getSlotType() == slot) {
					return TriState.TRUE;
				}
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
