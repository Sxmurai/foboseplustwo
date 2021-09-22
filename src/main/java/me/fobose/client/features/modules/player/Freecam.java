package me.fobose.client.features.modules.player;

import me.fobose.client.event.events.PacketEvent;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.util.MathUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// @todo baritone freecam
public class Freecam extends Module {
    public static Freecam INSTANCE;

    public Setting<Double> speed = this.register(new Setting<>("Speed", 0.5, 0.1, 5.0));
    public Setting<Boolean> disable = this.register(new Setting<>("DisableOnLog", true));

    private EntityOtherPlayerMP entity;
    private BlockPos pos;

    public Freecam() {
        super("Freecam", "allows you to have an out of body experience", Module.Category.PLAYER, true, false, false);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (!Module.fullNullCheck()) {
            if (entity != null) {
                despawn();
            }

            pos = mc.player.getPosition();

            entity = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
            entity.copyLocationAndAnglesFrom(mc.player);
            entity.inventory.copyInventory(mc.player.inventory);
            entity.setEntityId(-694201337);

            mc.world.spawnEntity(entity);
        } else {
            disable();
        }
    }

    @Override
    public void onDisable() {
        if (!Module.fullNullCheck()) {
            mc.player.setPosition(pos.getX(), pos.getY(), pos.getZ());
            mc.player.noClip = false;
            mc.player.capabilities.isFlying = false;
            despawn();
        }
    }

    @Override
    public void onUpdate() {
        if (Module.fullNullCheck()) {
            disable();
            return;
        }

        mc.player.noClip = true;
        mc.player.capabilities.isFlying = true;

        if (mc.player.movementInput.moveForward != 0.0f || mc.player.movementInput.moveStrafe != 0.0f) {
            double[] directionalSpeed = MathUtil.directionSpeed(speed.getValue());
            mc.player.motionX = directionalSpeed[0];
            mc.player.motionZ = directionalSpeed[1];
        }

        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.motionY += speed.getValue().floatValue();
        } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.player.motionY -= speed.getValue().floatValue();
        }
    }

    @Override
    public void onLogout() {
        if (disable.getValue()) {
            disable();
            entity = null;
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (!Module.fullNullCheck() && event.getPacket() instanceof CPacketPlayer) {
            event.setCanceled(true);
        }
    }

    private void despawn() {
        if (entity != null) {
            mc.world.removeEntity(entity);
            mc.world.removeEntityDangerously(entity);
            entity = null;
        }
    }
}

