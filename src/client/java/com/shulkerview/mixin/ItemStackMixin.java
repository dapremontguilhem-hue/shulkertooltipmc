package com.shulkerview.mixin;

import com.shulkerview.ShulkerTooltipData;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    /**
     * On ne touche pas aux lignes de texte (tooltip vanilla conservé) :
     * on ajoute seulement des "données de tooltip" pour les shulker boxes.
     */
    @Inject(method = "getTooltipData", at = @At("RETURN"), cancellable = true)
    private void shulkerview$addShulkerPreview(CallbackInfoReturnable<Optional<TooltipData>> cir) {
        if (cir.getReturnValue().isPresent()) return;

        ShulkerTooltipData data = ShulkerTooltipData.from((ItemStack) (Object) this);
        if (data != null) {
            cir.setReturnValue(Optional.of(data));
        }
    }
}
