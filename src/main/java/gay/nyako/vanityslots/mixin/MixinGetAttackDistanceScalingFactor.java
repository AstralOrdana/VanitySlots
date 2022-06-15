package gay.nyako.vanityslots.mixin;

import dev.emi.trinkets.api.SlotGroup;
import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;

@Mixin(LivingEntity.class)
public class MixinGetAttackDistanceScalingFactor {

    @Redirect(method = "getAttackDistanceScalingFactor", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getArmorVisibility()F"))
    private float injected(LivingEntity entity) {
        Iterable<ItemStack> iterable = entity.getArmorItems();

        List<ItemStack> visible_armor = new ArrayList<ItemStack>();
        int i = 0;
        for(Iterator var4 = iterable.iterator(); var4.hasNext(); ++i) {
            ItemStack itemStack = (ItemStack)var4.next();
            visible_armor.add(itemStack);
        }

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
                        if (!itemStack.isEmpty()) visible_armor.set(0, itemStack);
                    }
                    if (slotType.getGroup().equals("legs")) {
                        if (!itemStack.isEmpty()) visible_armor.set(1, itemStack);
                    }
                    if (slotType.getGroup().equals("chest")) {
                        if (!itemStack.isEmpty()) visible_armor.set(2, itemStack);
                    }
                    if (slotType.getGroup().equals("head")) {
                        if (!itemStack.isEmpty()) visible_armor.set(3, itemStack);
                    }
                }
            }
        }

        int j = 0;
        for (ItemStack item : visible_armor) {
            if (!item.isEmpty()) {
                if (!item.getTranslationKey().startsWith("item.vanityslots.familiar_")) {
                    j++;
                }
            }
        }

        return i > 0 ? (float)j / (float)i : 0.0F;
    }
}