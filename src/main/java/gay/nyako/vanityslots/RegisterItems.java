package gay.nyako.vanityslots;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RegisterItems {

    public static final ArmorMaterial vanityArmorMaterial = new VanityArmorMaterial();
    public static final Item FAMILIAR_WIG = new ArmorItem(vanityArmorMaterial, ArmorItem.Type.HELMET, new Item.Settings());
    public static final Item FAMILIAR_SHIRT = new ArmorItem(vanityArmorMaterial, ArmorItem.Type.CHESTPLATE, new Item.Settings());
    public static final Item FAMILIAR_PANTS = new ArmorItem(vanityArmorMaterial, ArmorItem.Type.LEGGINGS, new Item.Settings());
    public static final Item FAMILIAR_SNEAKERS = new ArmorItem(vanityArmorMaterial, ArmorItem.Type.BOOTS, new Item.Settings());

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier("vanityslots", "familiar_wig"), FAMILIAR_WIG);
        Registry.register(Registries.ITEM, new Identifier("vanityslots", "familiar_shirt"), FAMILIAR_SHIRT);
        Registry.register(Registries.ITEM, new Identifier("vanityslots", "familiar_pants"), FAMILIAR_PANTS);
        Registry.register(Registries.ITEM, new Identifier("vanityslots", "familiar_sneakers"), FAMILIAR_SNEAKERS);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(FAMILIAR_WIG));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(FAMILIAR_SHIRT));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(FAMILIAR_PANTS));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(FAMILIAR_SNEAKERS));


    }
}