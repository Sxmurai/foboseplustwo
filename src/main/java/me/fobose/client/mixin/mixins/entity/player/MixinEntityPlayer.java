package me.fobose.client.mixin.mixins.entity.player;

import com.mojang.authlib.GameProfile;
import me.fobose.client.Fobose;
import me.fobose.client.features.modules.misc.PortalModifier;
import me.fobose.client.features.modules.movement.Phase;
import me.fobose.client.features.modules.movement.PacketFly;
import me.fobose.client.features.modules.player.TPSSync;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase {
    public MixinEntityPlayer(World worldIn, GameProfile gameProfileIn) {
        super(worldIn);
    }

    @Inject(method={"getCooldownPeriod"}, at={@At(value="HEAD")}, cancellable=true)
    private void getCooldownPeriodHook(CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (TPSSync.INSTANCE.isOn() && TPSSync.INSTANCE.attack.getValue()) {
            callbackInfoReturnable.setReturnValue((float) (1.0 / EntityPlayer.class.cast(this).getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getAttributeValue() * 20.0 * (double) Fobose.serverManager.getTpsFactor()));
        }
    }

    @ModifyConstant(method={"getPortalCooldown"}, constant={@Constant(intValue=10)})
    private int getPortalCooldownHook(int cooldown) {
        int time = cooldown;
        if (PortalModifier.getInstance().isOn() && PortalModifier.getInstance().fastPortal.getValue()) {
            time = PortalModifier.getInstance().cooldown.getValue();
        }
        return time;
    }

    @Inject(method={"isEntityInsideOpaqueBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void isEntityInsideOpaqueBlockHook(CallbackInfoReturnable<Boolean> info) {
        if (Phase.getInstance().isOn() && Phase.getInstance().type.getValue() != Phase.PacketFlyMode.NONE) {
            info.setReturnValue(false);
        } else if (PacketFly.getInstance().isOn()) {
            info.setReturnValue(false);
        }
    }
}

