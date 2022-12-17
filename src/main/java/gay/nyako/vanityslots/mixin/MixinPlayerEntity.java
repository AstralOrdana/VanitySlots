package gay.nyako.vanityslots.mixin;

import gay.nyako.vanityslots.VanitySlots;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {

    @Final
    @Shadow
    private PlayerInventory inventory;

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private void injected(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> cir) {
        if (slot.getType() == EquipmentSlot.Type.ARMOR) {
            if (VanitySlots.USE_VANITY) {
                ItemStack vanity = VanitySlots.getVanityStack(this, slot);
                if (!vanity.isEmpty()) {
                    cir.setReturnValue(vanity);
                    return;
                }
            }
            cir.setReturnValue(this.inventory.armor.get(slot.getEntitySlotId()));
        }
    }
}
