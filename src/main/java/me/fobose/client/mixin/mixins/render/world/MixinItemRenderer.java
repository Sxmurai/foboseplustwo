package me.fobose.client.mixin.mixins.render.world;

import me.fobose.client.features.modules.render.NoRender;
import me.fobose.client.features.modules.render.ViewModel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
    @Inject(method = "renderFireInFirstPerson", at = @At(value="HEAD"), cancellable = true)
    public void renderFireInFirstPersonHook(CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().fire.getValue()) {
            info.cancel();
        }
    }

    @Inject(method = "renderSuffocationOverlay", at = @At(value="HEAD"), cancellable = true)
    public void renderSuffocationOverlay(CallbackInfo ci) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().blocks.getValue()) {
            ci.cancel();
        }
    }

    @Inject(method = "transformFirstPerson", at = @At("HEAD"))
    public void onTransformFirstPerson(EnumHandSide handSide, float p_187459_2_, CallbackInfo info) {
        if (ViewModel.INSTANCE.isOn()) {
            Vec3d offset = ViewModel.getOffsets(handSide);
            GlStateManager.translate(offset.x, offset.y, offset.z);
        }
    }

    @Inject(method = "transformSideFirstPerson", at = @At("HEAD"))
    public void onTransformSideFirstPerson(EnumHandSide handSide, float p_187459_2_, CallbackInfo info) {
        if (ViewModel.INSTANCE.isOn()) {
            Vec3d offset = ViewModel.getOffsets(handSide);
            GlStateManager.translate(offset.x, offset.y, offset.z);
        }
    }
}

