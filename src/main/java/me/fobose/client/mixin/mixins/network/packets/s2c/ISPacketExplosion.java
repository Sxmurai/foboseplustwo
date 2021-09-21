package me.fobose.client.mixin.mixins.network.packets.s2c;

import net.minecraft.network.play.server.SPacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPacketExplosion.class)
public interface ISPacketExplosion {
    @Accessor("motionX")
    void setMotionX(float motionX);

    @Accessor("motionX")
    float getMotionX();

    @Accessor("motionY")
    void setMotionY(float motionY);

    @Accessor("motionY")
    float getMotionY();

    @Accessor("motionZ")
    void setMotionZ(float motionZ);

    @Accessor("motionZ")
    float getMotionZ();
}
