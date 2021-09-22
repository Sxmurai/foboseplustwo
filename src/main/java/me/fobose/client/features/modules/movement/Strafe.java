package me.fobose.client.features.modules.movement;

import me.fobose.client.Fobose;
import me.fobose.client.event.events.MoveEvent;
import me.fobose.client.event.events.PacketEvent;
import me.fobose.client.event.events.UpdateWalkingPlayerEvent;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.modules.player.Freecam;
import me.fobose.client.features.setting.Setting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class Strafe extends Module {
    public static Strafe INSTANCE;

    public final Setting<Integer> speed = this.register(new Setting<>("Speed", 27, 20, 100));
    public final Setting<Integer> startStage = this.register(new Setting<>("Stage", 2, 0, 4));
    public final Setting<Boolean> strict = this.register(new Setting<>("Strict", false));
    public final Setting<Boolean> limiter = this.register(new Setting<>("Limiter", false));
    public final Setting<Boolean> noLag = this.register(new Setting<>("NoLag", true));
    public final Setting<Boolean> autoSprint = this.register(new Setting<>("Sprint", true));

    private double movementSpeed;
    private double lastDistance = 0.0;
    private int stage = 0;

    public Strafe() {
        super("Strafe", "does wizard shit and does speed brrrr", Category.MOVEMENT, true, false, false);
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        movementSpeed = 0.0f;
        stage = startStage.getValue();
    }

    @Override
    public void onUpdate() {
        if (!Module.fullNullCheck() && !shouldStop()) {
            if (autoSprint.getValue() && !mc.player.isSprinting() && mc.player.getFoodStats().getFoodLevel() > 6) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
            }

            mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook && this.noLag.getValue() && !shouldStop()) {
            this.stage = 1;
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && !shouldStop()) {
            this.lastDistance = Math.sqrt((Strafe.mc.player.posX - Strafe.mc.player.prevPosX) * (Strafe.mc.player.posX - Strafe.mc.player.prevPosX) + (Strafe.mc.player.posZ - Strafe.mc.player.prevPosZ) * (Strafe.mc.player.posZ - Strafe.mc.player.prevPosZ));
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (shouldStop()) {
            return;
        }

        if (!limiter.getValue() && mc.player.onGround) {
            stage = 2;
        }

        switch (stage) {
            case 0: {
                ++stage;
                lastDistance = 0.0;
                break;
            }

            case 2: {
                if (mc.player.moveForward == 0.0f && mc.player.moveStrafing == 0.0f || !mc.player.onGround) {
                    break;
                }

                double motionY = 0.40123128;
                if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                    motionY += (mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1;
                }

                mc.player.motionY = motionY;
                event.setY(motionY);

                movementSpeed *= strict.getValue() ? 1.1072 : 2.149;
                break;
            }

            case 3: {
                movementSpeed = lastDistance - 0.76 * (lastDistance - getBaseMoveSpeed());
                break;
            }

            default: {
                if (Strafe.mc.world.getCollisionBoxes(Strafe.mc.player, Strafe.mc.player.getEntityBoundingBox().offset(0.0, Strafe.mc.player.motionY, 0.0)).size() > 0 || Strafe.mc.player.collidedVertically && this.stage > 0) {
                    stage = Strafe.mc.player.moveForward != 0.0f || Strafe.mc.player.moveStrafing != 0.0f ? 1 : 0;
                }

                movementSpeed = lastDistance - lastDistance / 159.0;
                break;
            }
        }

        movementSpeed = Math.max(movementSpeed, getBaseMoveSpeed());
        float forward = mc.player.movementInput.moveForward,
                strafe = mc.player.movementInput.moveStrafe,
                yaw = mc.player.rotationYaw;

        if (forward == 0.0f && strafe == 0.0f) {
            event.setX(0.0);
            event.setZ(0.0);
        } else if (forward != 0.0f && strafe != 0.0f) {
            forward *= Math.sin(0.7853981633974483);
            strafe *= Math.cos(0.7853981633974483);
        }

        event.setX((forward * movementSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * movementSpeed * Math.cos(Math.toRadians(yaw))) * 0.99);
        event.setZ((forward * movementSpeed * Math.cos(Math.toRadians(yaw)) - strafe * movementSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99);
        ++stage;
    }

    private boolean shouldStop() {
        return Fobose.moduleManager.isModuleEnabled(Freecam.class) || Fobose.moduleManager.isModuleEnabled(PacketFly.class) || Fobose.moduleManager.isModuleEnabled(Phase.class) || Fobose.moduleManager.isModuleEnabled(ElytraFlight.class) || Anchor.INSTANCE.anchoring;
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = speed.getValue().doubleValue() / 100.0;

        if (Strafe.mc.player.isPotionActive(MobEffects.SPEED)) {
            baseSpeed *= 1.0 + 0.2 * (double) Objects.requireNonNull(Strafe.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
        }

        return baseSpeed;
    }
}

