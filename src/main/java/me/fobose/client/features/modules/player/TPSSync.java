package me.fobose.client.features.modules.player;

import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;

public class TPSSync extends Module {
    public static TPSSync INSTANCE;

    public Setting<Boolean> mining = this.register(new Setting<>("Mining", true));
    public Setting<Boolean> attack = this.register(new Setting<>("Attack", false));

    public TPSSync() {
        super("TpsSync", "Syncs your client with the TPS.", Module.Category.PLAYER, false, false, false);
        INSTANCE = this;
    }
}

