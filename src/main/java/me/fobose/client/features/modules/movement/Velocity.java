package me.fobose.client.features.modules.movement;

import me.fobose.client.event.events.PacketEvent;
import me.fobose.client.event.events.PushEvent;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.mixin.mixins.network.packets.s2c.ISPacketEntityVelocity;
import me.fobose.client.mixin.mixins.network.packets.s2c.ISPacketExplosion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity extends Module {
    public Setting<Boolean> knockBack = this.register(new Setting<>("KnockBack", true));
    public Setting<Boolean> noPush = this.register(new Setting<>("NoPush", true));
    public Setting<Float> horizontal = this.register(new Setting<>("Horizontal", 0.0f, 0.0f, 100.0f));
    public Setting<Float> vertical = this.register(new Setting<>("Vertical", 0.0f, 0.0f, 100.0f));
    public Setting<Boolean> explosions = this.register(new Setting<>("Explosions", true));
    public Setting<Boolean> bobbers = this.register(new Setting<>("Bobbers", true));
    public Setting<Boolean> water = this.register(new Setting<>("Water", false));
    public Setting<Boolean> blocks = this.register(new Setting<>("Blocks", false));
    public Setting<Boolean> ice = this.register(new Setting<>("Ice", false));

    public Velocity() {
        super("Velocity", "Allows you to control your velocity", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (IceSpeed.getINSTANCE().isOff() && this.ice.getValue()) {
            Blocks.ICE.slipperiness = 0.6f;
            Blocks.PACKED_ICE.slipperiness = 0.6f;
            Blocks.FROSTED_ICE.slipperiness = 0.6f;
        }
    }

    @Override
    public void onDisable() {
        if (IceSpeed.getINSTANCE().isOff()) {
            Blocks.ICE.slipperiness = 0.98f;
            Blocks.PACKED_ICE.slipperiness = 0.98f;
            Blocks.FROSTED_ICE.slipperiness = 0.98f;
        }
    }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        if (event.getStage() == 0 && Velocity.mc.player != null) {
            Entity entity;
            SPacketEntityStatus packet;
            SPacketEntityVelocity velocity;

            if (this.knockBack.getValue() && event.getPacket() instanceof SPacketEntityVelocity && (velocity = event.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
                if (this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) {
                    event.setCanceled(true);
                    return;
                }

                ISPacketEntityVelocity p = (ISPacketEntityVelocity) velocity;
                p.setMotionX(p.getMotionX() * horizontal.getValue().intValue());
                p.setMotionY(p.getMotionY() * vertical.getValue().intValue());
                p.setMotionZ(p.getMotionZ() * horizontal.getValue().intValue());
            }

            if (event.getPacket() instanceof SPacketEntityStatus && this.bobbers.getValue() && (packet = event.getPacket()).getOpCode() == 31 && (entity = packet.getEntity(Velocity.mc.world)) instanceof EntityFishHook) {
                if (((EntityFishHook) entity).caughtEntity == Velocity.mc.player) {
                    event.setCanceled(true);
                }
            }

            if (this.explosions.getValue() && event.getPacket() instanceof SPacketExplosion) {
                ISPacketExplosion p = event.getPacket();
                p.setMotionX(p.getMotionX() * horizontal.getValue());
                p.setMotionY(p.getMotionY() * vertical.getValue());
                p.setMotionZ(p.getMotionZ() * horizontal.getValue());
            }
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent event) {
        if (event.getStage() == 0 && this.noPush.getValue() && event.entity.equals(Velocity.mc.player)) {
            if (this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) {
                event.setCanceled(true);
                return;
            }

            event.x = -event.x * (double) this.horizontal.getValue();
            event.y = -event.y * (double) this.vertical.getValue();
            event.z = -event.z * (double) this.horizontal.getValue();
        } else if (event.getStage() == 1 && this.blocks.getValue()) {
            event.setCanceled(true);
        } else if (event.getStage() == 2 && this.water.getValue() && Velocity.mc.player != null && Velocity.mc.player.equals(event.entity)) {
            event.setCanceled(true);
        }
    }
}

