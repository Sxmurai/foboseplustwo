package me.fobose.client.features.modules.player;

import me.fobose.client.features.modules.Module;

public class LiquidInteract extends Module {
    public static LiquidInteract INSTANCE;

    public LiquidInteract() {
        super("LiquidInteract", "Interact with liquids", Module.Category.PLAYER, false, false, false);
        INSTANCE = this;
    }
}

