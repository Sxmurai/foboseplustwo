package me.fobose.client.features.modules.player;

import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;

public class Reach extends Module {
    public static Reach INSTANCE;

    public final Setting<Float> distance = this.register(new Setting<>("Distance", 4.5f, 4.5f, 8.0f));

    public Reach() {
        super("Reach", "reaches further", Category.PLAYER, false, false, false);
        INSTANCE = this;
    }
}

