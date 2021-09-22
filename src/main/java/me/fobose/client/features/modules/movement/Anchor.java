package me.fobose.client.features.modules.movement;

import me.fobose.client.Fobose;
import me.fobose.client.event.events.UpdateWalkingPlayerEvent;
import me.fobose.client.features.command.Command;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.modules.combat.Burrow;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.util.BlockUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Anchor extends Module {
    public static Anchor INSTANCE;

    public final Setting<Boolean> guaranteedHole = this.register(new Setting<>("GuaranteedHole", false));
    public final Setting<Float> pitch = this.register(new Setting<>("Pitch", 90.0f, -90.0f, 90.0f));
    public final Setting<Boolean> packetLook = this.register(new Setting<>("PacketLook", true));
    public final Setting<Integer> height = this.register(new Setting<>("Height", 2, 1, 5));

    private float oldPitch = -1.0f;
    public boolean anchoring = false;

    public Anchor() {
        super("Anchor", "Stops movement over a hole", Category.MOVEMENT, true, false, false);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1 && Burrow.INSTANCE.isOff() && !mc.gameSettings.keyBindJump.isKeyDown()) {
            if (anchoring && BlockUtil.isInHole()) {
                anchoring = false;
                oldPitch = -1.0f;
                return;
            }

            double flooredX = Math.floor(mc.player.posX);
            double flooredZ = Math.floor(mc.player.posZ);

            double offsetX = Math.abs(flooredX - mc.player.posX);
            double offsetZ = Math.abs(flooredZ - mc.player.posZ);

            if (guaranteedHole.getValue() && (offsetX < 0.3 || offsetX > 0.7 || offsetZ < 0.3 || offsetZ > 0.7)) {
                return;
            }

            double nearestBelowY = BlockUtil.getNearestBlockBelow();
            if (Fobose.holeManager.isSafe(new BlockPos(flooredX, Math.floor(nearestBelowY) + 1.0, flooredZ))) {
                Command.sendMessage("hacker shit");
                center();

                mc.player.motionX = 0.0;
                mc.player.motionZ = 0.0;
            }
        }
    }

    private void center() {
        BlockPos playerPos = new BlockPos(mc.player.getPositionVector());
        double xOffset = Math.abs(playerPos.getX() - mc.player.posX);
        double zOffset = Math.abs(playerPos.getZ() - mc.player.posZ);

        if (xOffset > 0.1 || zOffset > 0.1) {
            double x = playerPos.getX() - mc.player.posX;
            double z = playerPos.getZ() - mc.player.posZ;

            mc.player.motionX = x / 2.0;
            mc.player.motionZ = z / 2.0;
        }
    }
}
