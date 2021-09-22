package me.fobose.client.features.modules.player;

import me.fobose.client.features.modules.Module;

public class NoEntityTrace extends Module {
    public static NoEntityTrace INSTANCE;

    public NoEntityTrace() {
        super("NoEntityTrace", "allows you to mine through entities", Category.PLAYER, false, false, false);
        INSTANCE = this;
    }
}
