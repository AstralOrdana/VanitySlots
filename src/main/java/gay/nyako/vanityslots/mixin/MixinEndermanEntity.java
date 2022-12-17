package gay.nyako.vanityslots.mixin;

import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import gay.nyako.vanityslots.VanitySlots;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndermanEntity.class)
public class MixinEndermanEntity {

    @Redirect(method = "isPlayerStaring", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;get(I)Ljava/lang/Object;"))
    public Object vanityslots$isPlayerStaring(DefaultedList<ItemStack> instance, int index, PlayerEntity player) {

        ItemStack vanity = VanitySlots.getVanityStack(player, EquipmentSlot.HEAD);
        if (!vanity.isEmpty()) {
            return vanity;
        }

        return instance.get(index);


    }
}

