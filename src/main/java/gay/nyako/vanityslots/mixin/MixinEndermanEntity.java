package gay.nyako.vanityslots.mixin;

import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(EndermanEntity.class)
public class MixinEndermanEntity {

    @Redirect(method = "isPlayerStaring", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;get(I)Ljava/lang/Object;"))
    public Object vanityslots$isPlayerStaring(DefaultedList<ItemStack> instance, int index, PlayerEntity player) {
        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
        if (component.isPresent()) {
            TrinketComponent component2 = component.get();
            for (var equipped : component2.getAllEquipped()) {
                SlotType slotType = equipped.getLeft().inventory().getSlotType();
                ItemStack itemStack = equipped.getRight();
                if (!slotType.getName().equals("vanity")) {
                    continue;
                }
                if (itemStack.isEmpty()) continue;

                if (slotType.getGroup().equals("head")) {
                    return itemStack;
                }
            }
        }

        return instance.get(index);


    }
}

