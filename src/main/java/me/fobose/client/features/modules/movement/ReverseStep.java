package me.fobose.client.features.modules.movement;

import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.util.EntityUtil;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;

// @todo credit https://github.com/FaxHack/Rip-Cosmo-Client/blob/main/cope/cosmos/client/features/modules/movement/ReverseStep.java
public class ReverseStep extends Module {
    public final Setting<Mode> mode = this.register(new Setting<>("Mode", Mode.NORMAL));
    public final Setting<Boolean> shift = this.register(new Setting<>("Shift", true, (v) -> mode.getValue() == Mode.SHIFT));
    public final Setting<Double> speed = this.register(new Setting<>("Speed", 2.0, 1.0, 1.0));
    public final Setting<Integer> height = this.register(new Setting<>("Height", 2, 1, 5));
    public final Setting<Boolean> strict = this.register(new Setting<>("Strict", false));

    public ReverseStep() {
        super("ReverseStep", "Screams chinese words and teleports you", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (!Module.fullNullCheck() && mc.player.onGround && !mc.player.isOnLadder() && !EntityUtil.isInLiquid()) {
            switch (mode.getValue()) {
                case NORMAL: {
                    mc.player.motionY -= 1.0;
                    break;
                }

                case MOTION: {
                    if (shift.getValue()) {
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    }

                    for (double y = 0.0; y < height.getValue().doubleValue() + 0.5; y += 0.1) {
                        if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                            mc.player.motionY = strict.getValue() ? -0.22 : -speed.getValue();
                        }
                    }

                    if (shift.getValue()) {
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                    }
                    break;
                }

                case SHIFT: {
                    for (double y = 0.0; y < height.getValue().doubleValue() + 0.5; y += 0.01) {
                        if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                            mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
                            mc.player.motionY *= 1.75;
                            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                        }
                    }
                    break;
                }
            }
        }
    }

    public enum Mode {
        NORMAL,
        MOTION,
        SHIFT
    }
}

