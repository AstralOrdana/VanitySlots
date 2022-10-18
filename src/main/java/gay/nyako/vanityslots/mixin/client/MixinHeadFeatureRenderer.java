package gay.nyako.vanityslots.mixin.client;

import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(HeadFeatureRenderer.class)
public class MixinHeadFeatureRenderer<T extends LivingEntity> {

    @Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
    public ItemStack vanityslots$getOverlayArmor(T entity, EquipmentSlot slot) {
        if (!(entity instanceof PlayerEntity))
            return entity.getEquippedStack(slot);

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

                if ((slot == EquipmentSlot.HEAD) && (slotType.getGroup().equals("head"))) {
                    return itemStack;
                }
            }
        }

        return entity.getEquippedStack(slot);
    }
}