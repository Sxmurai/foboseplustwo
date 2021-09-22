package me.fobose.client.features.modules.movement;

import me.fobose.client.event.events.StepEvent;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Step extends Module {
    public Setting<Mode> mode = this.register(new Setting<>("Mode", Mode.NCP));
    public Setting<Integer> stepHeight = this.register(new Setting<>("Height", 2, 1, 2));
    public Setting<Boolean> turnOff = this.register(new Setting<>("Disable", false));

    public Step() {
        super("Step", "Allows you to step up blocks", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public String getDisplayInfo() {
        return mode.currentEnumName();
    }

    @SubscribeEvent
    public void onStep(StepEvent event) {
        if (Step.mc.player.onGround && !Step.mc.player.isInsideOfMaterial(Material.WATER) && !Step.mc.player.isInsideOfMaterial(Material.LAVA) && Step.mc.player.collidedVertically && Step.mc.player.fallDistance == 0.0f && !Step.mc.gameSettings.keyBindJump.isPressed() && !Step.mc.player.isOnLadder()) {
            event.setHeight(this.stepHeight.getValue());

            double height = Step.mc.player.getEntityBoundingBox().minY - Step.mc.player.posY;

            if (height >= 0.625) {
                if (mode.getValue() == Mode.NCP) {
                    this.ncpStep(height);
                }

                if (this.turnOff.getValue()) {
                    this.disable();
                }
            }
        } else {
            event.setHeight(0.6f);
        }
    }

    private void ncpStep(double height) {
        block12: {
            double y = mc.player.posY,
                    posZ = mc.player.posZ,
                    posX = mc.player.posX;

            block11: {
                if (!(height < 1.1)) {
                    break block11;
                }

                double first = 0.42, second = 0.75;
                if (height != 1.0) {
                    first *= height;
                    second *= height;
                    if (first > 0.425) {
                        first = 0.425;
                    }
                    if (second > 0.78) {
                        second = 0.78;
                    }
                    if (second < 0.49) {
                        second = 0.49;
                    }
                }

                Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, y + first, posZ, false));

                if (!(y + second < y + height)) {
                    break block12;
                }

                Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, y + second, posZ, false));

                break block12;
            }

            if (height < 1.6) {
                for (double offset : new double[] {0.42, 0.33, 0.24, 0.083, -0.078}) {
                    Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, y += offset, posZ, false));
                }
            } else if (height < 2.1) {
                for (double offset : new double[] {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869}) {
                    Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, y + offset, posZ, false));
                }
            } else {
                for (double offset : new double[] {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907}) {
                    Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, y + offset, posZ, false));
                }
            }
        }
    }

    public enum Mode {
        NCP,
        VANILLA
    }
}

