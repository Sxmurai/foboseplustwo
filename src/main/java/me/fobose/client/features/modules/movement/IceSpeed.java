package me.fobose.client.features.modules.movement;

import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import net.minecraft.init.Blocks;

public class IceSpeed extends Module {
    private Setting<Float> speed = this.register(new Setting<Float>("Speed", 0.4f, 0.2f, 1.5f));
    private static IceSpeed INSTANCE = new IceSpeed();

    public IceSpeed() {
        super("IceSpeed", "Speeds you up on ice.", Module.Category.MOVEMENT, false, false, false);
        INSTANCE = this;
    }

    public static IceSpeed getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new IceSpeed();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        Blocks.ICE.slipperiness = this.speed.getValue();
        Blocks.PACKED_ICE.slipperiness = this.speed.getValue();
        Blocks.FROSTED_ICE.slipperiness = this.speed.getValue();
    }

    @Override
    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
    }
}

