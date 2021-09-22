package me.fobose.client.features.modules.movement;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalInverted;
import baritone.api.pathing.goals.GoalXZ;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoWalk extends Module {
    public final Setting<Mode> mode = this.register(new Setting<>("Mode", Mode.VANILLA));

    public AutoWalk() {
        super("AutoWalk", "Automatically walks for you", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onDisable() {
        if (!Module.fullNullCheck()) {
            // @todo stop baritone
        }
    }

    @Override
    public void onEnable() {
        if (Module.fullNullCheck()) {
            disable();
            return;
        }

        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalInverted(new GoalXZ(0, 0)));
    }

    @SubscribeEvent
    public void onUpdateInput(InputUpdateEvent event) {
        if (mode.getValue() == Mode.VANILLA) {
            event.getMovementInput().moveForward = 1.0f;
        }
    }

    public enum Mode {
        VANILLA,
        BARITONE
    }
}

