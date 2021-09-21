package me.fobose.client.mixin.mixins.entity.ridden;

import me.fobose.client.features.modules.movement.EntityControl;
import net.minecraft.entity.passive.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AbstractHorse.class})
public class MixinAbstractHorse {
    @Inject(method={"isHorseSaddled"}, at={@At(value="HEAD")}, cancellable=true)
    public void isHorseSaddled(CallbackInfoReturnable<Boolean> cir) {
        if (EntityControl.INSTANCE.isOn()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canBeSteered", at = @At("HEAD"), cancellable = true)
    public void hookCanBeSteered(CallbackInfoReturnable<Boolean> info) {
        if (EntityControl.INSTANCE.isOn()) {
            info.setReturnValue(true);
        }
    }
}

