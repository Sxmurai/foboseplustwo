
package me.fobose.client.features.modules.misc;

import me.fobose.client.event.events.PacketEvent;
import me.fobose.client.features.command.Command;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.mixin.mixins.network.packets.s2c.ISPacketPlayerPosLook;
import me.fobose.client.util.Timer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRotate
extends Module {
    private Setting<Integer> waitDelay = this.register(new Setting<Integer>("Delay", 2500, 0, 10000));
    private Timer timer = new Timer();
    private boolean cancelPackets = true;
    private boolean timerReset = false;

    public NoRotate() {
        super("NoRotate", "Dangerous to use might desync you.", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onLogout() {
        this.cancelPackets = false;
    }

    @Override
    public void onLogin() {
        this.timer.reset();
        this.timerReset = true;
    }

    @Override
    public void onUpdate() {
        if (this.timerReset && !this.cancelPackets && this.timer.passedMs(this.waitDelay.getValue().intValue())) {
            Command.sendMessage("<NoRotate> \u00a7cThis module might desync you!");
            this.cancelPackets = true;
            this.timerReset = false;
        }
    }

    @Override
    public void onEnable() {
        Command.sendMessage("<NoRotate> \u00a7cThis module might desync you!");
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() == 0 && this.cancelPackets && event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            ((ISPacketPlayerPosLook) packet).setYaw(NoRotate.mc.player.rotationYaw);
            ((ISPacketPlayerPosLook) packet).setPitch(NoRotate.mc.player.rotationPitch);
        }
    }
}

