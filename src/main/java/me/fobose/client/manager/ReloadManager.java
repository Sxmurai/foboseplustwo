package me.fobose.client.manager;

import me.fobose.client.Fobose;
import me.fobose.client.event.events.PacketEvent;
import me.fobose.client.features.Feature;
import me.fobose.client.features.command.Command;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReloadManager extends Feature {
    public String prefix;

    public void init(String prefix) {
        this.prefix = prefix;
        MinecraftForge.EVENT_BUS.register(this);
        if (!ReloadManager.fullNullCheck()) {
            Command.sendMessage("\u00a7cFobose+2 has been unloaded. Type " + prefix + "reload to reload.");
        }
    }

    public void unload() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = event.getPacket();
            if (packet.getMessage().equalsIgnoreCase(prefix + "reload")) {
                Fobose.reload();
                event.setCanceled(true);
            }
        }
    }
}

