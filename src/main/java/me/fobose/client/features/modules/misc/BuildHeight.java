
package me.fobose.client.features.modules.misc;

import me.fobose.client.event.events.PacketEvent;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.mixin.mixins.network.packets.c2s.ICPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BuildHeight
extends Module {
    private Setting<Integer> height = this.register(new Setting<Integer>("Height", 255, 0, 255));

    public BuildHeight() {
        super("BuildHeight", "Allows you to place at build height", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketPlayerTryUseItemOnBlock packet;
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && (packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket()).getPos().getY() >= this.height.getValue() && packet.getDirection() == EnumFacing.UP) {
            ((ICPacketPlayerTryUseItemOnBlock) packet).setPlacedBlockDirection(EnumFacing.DOWN);
        }
    }
}

