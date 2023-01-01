package gay.nyako.vanityslots;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RegisterItems {

    public static final ArmorMaterial vanityArmorMaterial = new VanityArmorMaterial();
    public static final Item FAMILIAR_WIG = new ArmorItem(vanityArmorMaterial, EquipmentSlot.HEAD, new Item.Settings().group(ItemGroup.COMBAT));
    public static final Item FAMILIAR_SHIRT = new ArmorItem(vanityArmorMaterial, EquipmentSlot.CHEST, new Item.Settings().group(ItemGroup.COMBAT));
    public static final Item FAMILIAR_PANTS = new ArmorItem(vanityArmorMaterial, EquipmentSlot.LEGS, new Item.Settings().group(ItemGroup.COMBAT));
    public static final Item FAMILIAR_SNEAKERS = new ArmorItem(vanityArmorMaterial, EquipmentSlot.FEET, new Item.Settings().group(ItemGroup.COMBAT));

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier("vanityslots", "familiar_wig"), FAMILIAR_WIG);
        Registry.register(Registry.ITEM, new Identifier("vanityslots", "familiar_shirt"), FAMILIAR_SHIRT);
        Registry.register(Registry.ITEM, new Identifier("vanityslots", "familiar_pants"), FAMILIAR_PANTS);
        Registry.register(Registry.ITEM, new Identifier("vanityslots", "familiar_sneakers"), FAMILIAR_SNEAKERS);
    }
}