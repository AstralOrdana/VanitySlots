package gay.nyako.vanityslots.mixin.client;

import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
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

    @Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;getArmorStack(I)Lnet/minecraft/item/ItemStack;"))
    public ItemStack vanityslots$render(PlayerInventory instance, int slot) {
        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(instance.player);
        if (component.isPresent()) {
            TrinketComponent component2 = component.get();
            for (var equipped : component2.getAllEquipped()) {
                SlotType slotType = equipped.getLeft().inventory().getSlotType();
                ItemStack itemStack = equipped.getRight();
                if (!slotType.getName().equals("vanity")) {
                    continue;
                }
                if (itemStack.isEmpty()) continue;

                if ((slot == EquipmentSlot.HEAD.getEntitySlotId()) && (slotType.getGroup().equals("head"))) {
                    return itemStack;
                }
            }
        }

        return instance.getArmorStack(slot);
    }
}