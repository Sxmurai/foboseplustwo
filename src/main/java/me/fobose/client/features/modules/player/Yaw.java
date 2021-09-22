package me.fobose.client.features.modules.player;

import me.fobose.client.event.events.UpdateWalkingPlayerEvent;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Yaw extends Module {
    public Setting<Boolean> lockYaw = this.register(new Setting<>("LockYaw", false));
    public Setting<Direction> direction = this.register(new Setting<>("Direction", Direction.NORTH));
    public Setting<Integer> yaw = this.register(new Setting<>("Yaw", 0, 0, 360));
    public Setting<Boolean> lockPitch = this.register(new Setting<>("LockPitch", false));
    public Setting<Integer> pitch = this.register(new Setting<>("Pitch", 0, -180, 180));

    public Yaw() {
        super("Yaw", "Locks your yaw", Module.Category.PLAYER, true, false, false);
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (this.lockYaw.getValue()) {
            setYaw(direction.getValue() == Direction.CUSTOM ? yaw.getValue() : direction.getValue().getYaw());
        }

        if (this.lockPitch.getValue()) {
            Yaw.mc.player.rotationPitch = this.pitch.getValue();
        }
    }

    private void setYaw(int yaw) {
        Yaw.mc.player.rotationYaw = yaw;
    }

    public enum Direction {
        NORTH(180),
        NE(225),
        EAST(270),
        SE(315),
        SOUTH(0),
        SW(45),
        WEST(90),
        NW(135),
        CUSTOM(-1);

        public int yaw;
        Direction(int yaw) {
            this.yaw = yaw;
        }

        public int getYaw() {
            return yaw;
        }
    }
}

