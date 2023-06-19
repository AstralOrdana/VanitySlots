package gay.nyako.vanityslots.mixin.client;

import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import gay.nyako.vanityslots.VanitySlots;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(InGameHud.class)
public class MixinInGameHud<T extends LivingEntity> {

    @Redirect(method = "render(Lnet/minecraft/client/gui/DrawContext;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;getArmorStack(I)Lnet/minecraft/item/ItemStack;"))
    public ItemStack vanityslots$render(PlayerInventory instance, int slot) {

        EquipmentSlot slotType = null;
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            if (equipmentSlot.getEntitySlotId() == slot) {
                // Get slot type from equipment slot
                slotType = equipmentSlot;
                break;
            }
        }

        if (slotType == null) {
            return instance.getArmorStack(slot);
        }

        ItemStack vanity = VanitySlots.getVanityStack(instance.player, slotType);
        if (!vanity.isEmpty()) {
            return vanity;
        }

        return instance.getArmorStack(slot);
    }
}