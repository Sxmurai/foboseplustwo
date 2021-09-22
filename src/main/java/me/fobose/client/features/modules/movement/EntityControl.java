
package me.fobose.client.features.modules.movement;

import me.fobose.client.event.events.PacketEvent;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityControl extends Module {
    public static EntityControl INSTANCE;

    public final Setting<Boolean> mountBypass = this.register(new Setting<>("MountBypass", false));

    public EntityControl() {
        super("EntityControl", "Control entities with the force or some shit", Module.Category.MOVEMENT, true, false, false);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (!Module.fullNullCheck() && event.getPacket() instanceof CPacketUseEntity && mountBypass.getValue()) {
            CPacketUseEntity packet = event.getPacket();
            if (packet.getAction() == CPacketUseEntity.Action.INTERACT_AT && packet.getEntityFromWorld(mc.world) instanceof AbstractHorse) {
                event.setCanceled(true);
            }
        }
    }
}

