package gay.nyako.vanityslots.mixin;

import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(PiglinBrain.class)
public class MixinPiglinBrain {

    @Redirect(method = "wearsGoldArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getArmorItems()Ljava/lang/Iterable;"))
    private static Iterable<ItemStack> vanityslots$wearsGoldArmor(LivingEntity entity) {
        List<ItemStack> visibleArmor = new ArrayList<>((List<ItemStack>) entity.getArmorItems());

        if (entity instanceof PlayerEntity) {
            Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(entity);
            if (component.isPresent()) {
                TrinketComponent component2 = component.get();
                for (var equipped : component2.getAllEquipped()) {
                    SlotType slotType = equipped.getLeft().inventory().getSlotType();
                    ItemStack itemStack = equipped.getRight();
                    if (!slotType.getName().equals("vanity")) {
                        continue;
                    }
                    if (slotType.getGroup().equals("feet")) {
                        if (!itemStack.isEmpty()) visibleArmor.set(0, itemStack);
                    }
                    if (slotType.getGroup().equals("legs")) {
                        if (!itemStack.isEmpty()) visibleArmor.set(1, itemStack);
                    }
                    if (slotType.getGroup().equals("chest")) {
                        if (!itemStack.isEmpty()) visibleArmor.set(2, itemStack);
                    }
                    if (slotType.getGroup().equals("head")) {
                        if (!itemStack.isEmpty()) visibleArmor.set(3, itemStack);
                    }
                }
            }
        }

        return visibleArmor;
    }
}