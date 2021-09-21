package me.fobose.client.mixin.mixins.network.packets.s2c;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPacketEntityVelocity.class)
public interface ISPacketEntityVelocity {
    @Accessor("motionX")
    void setMotionX(int motionX);

    @Accessor("motionX")
    int getMotionX();

    @Accessor("motionY")
    int getMotionY();

    @Accessor("motionY")
    void setMotionY(int motionY);

    @Accessor("motionZ")
    int getMotionZ();

    @Accessor("motionZ")
    void setMotionZ(int motionZ);
}
