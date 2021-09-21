package me.fobose.client.mixin.mixins.network.packets.s2c;

import net.minecraft.network.play.server.SPacketSetSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={SPacketSetSlot.class})
public interface ISPacketSetSlot {
    @Accessor("windowId")
    int getId();

    @Accessor("windowId")
    void setWindowId(int var1);
}

