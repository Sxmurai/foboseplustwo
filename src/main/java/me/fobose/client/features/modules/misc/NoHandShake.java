
package me.fobose.client.features.modules.misc;

import io.netty.buffer.Unpooled;
import me.fobose.client.event.events.PacketEvent;
import me.fobose.client.features.modules.Module;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import java.io.IOException;

public class NoHandShake
extends Module {
    public NoHandShake() {
        super("NoHandshake", "Doesnt send your modlist to the server.", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketCustomPayload packet;
        if (event.getPacket() instanceof FMLProxyPacket && !mc.isSingleplayer()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCustomPayload && (packet = (CPacketCustomPayload)event.getPacket()).getChannelName().equals("MC|Brand")) {
            try {
                packet.writePacketData(new PacketBuffer(Unpooled.buffer()).writeString("vanilla"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

