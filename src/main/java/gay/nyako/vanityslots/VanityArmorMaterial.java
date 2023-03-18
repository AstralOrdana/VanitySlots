package gay.nyako.vanityslots;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class VanityArmorMaterial implements ArmorMaterial {

    @Override
    public int getDurability(ArmorItem.Type type) {
        return switch(type) {
            case HELMET -> 13;
            case CHESTPLATE -> 15;
            case LEGGINGS -> 16;
            case BOOTS -> 11;
        };
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return switch(type) {
            case HELMET, BOOTS -> 1;
            case CHESTPLATE -> 2;
            case LEGGINGS -> 3;
        };
    }

    @Override
    public int getEnchantability() {
        return 1;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.LEATHER);
    }

    @Override
    public String getName() {
        return "familiar";
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}