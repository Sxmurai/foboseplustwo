package me.fobose.client.mixin.mixins.network.packets.c2s;

import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketPlayer.class)
public interface ICPacketPlayer {
    @Accessor("yaw")
    void setYaw(float yaw);

    @Accessor("pitch")
    void setPitch(float pitch);

    @Accessor("onGround")
    void setOnGround(boolean onGround);

    @Accessor("x")
    double getX();

    @Accessor("y")
    double getY();

    @Accessor("y")
    void setY(double y);

    @Accessor("z")
    double getZ();
}
