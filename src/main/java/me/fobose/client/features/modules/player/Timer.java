package me.fobose.client.features.modules.player;

import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.mixin.mixins.IMinecraft;
import me.fobose.client.mixin.mixins.world.ITimer;

public class Timer extends Module {
    public final Setting<Float> speed = this.register(new Setting<>("Speed", 2.0f, 0.1f, 20.0f));

    public Timer() {
        super("Timer", "does shit", Category.PLAYER, false, false, false);
    }

    @Override
    public String getDisplayInfo() {
        return String.valueOf(speed.getValue());
    }

    @Override
    public void onDisable() {
        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f);
    }

    @Override
    public void onUpdate() {
        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f / speed.getValue());
    }
}

