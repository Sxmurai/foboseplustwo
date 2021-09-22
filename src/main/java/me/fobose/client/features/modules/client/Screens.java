package me.fobose.client.features.modules.client;

import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;

public class Screens extends Module {
    public static Screens INSTANCE;

    public Setting<Boolean> mainScreen = this.register(new Setting<>("MainScreen", true));

    public Screens() {
        super("Screens", "Controls custom screens used by the client", Module.Category.CLIENT, true, false, false);
        INSTANCE = this;
    }
}

