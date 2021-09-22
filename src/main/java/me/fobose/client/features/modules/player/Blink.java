package me.fobose.client.features.modules.player;

import me.fobose.client.event.events.PacketEvent;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.util.Timer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class Blink extends Module {
    public static Blink INSTANCE;

    public Setting<Mode> mode = this.register(new Setting<>("Mode", Mode.MANUAL));
    public Setting<Boolean> spawnPlayer = this.register(new Setting<>("SpawnPlayer", true));

    public Setting<Integer> time = this.register(new Setting<>("Time", 20, 1, 500, (m) -> mode.getValue() == Mode.TIME));
    public Setting<Integer> packets = this.register(new Setting<>("Packets", 20, 1, 500, (m) -> mode.getValue() == Mode.PACKETS));
    public Setting<Float> distance = this.register(new Setting<>("Distance", 10.0f, 1.0f, 100.0f, (m) -> mode.getValue() == Mode.DISTANCE));

    private EntityOtherPlayerMP fakePlayer;
    private final ArrayList<CPacketPlayer> playerPackets = new ArrayList<>();
    private final Timer timer = new Timer();
    private boolean pauseSaving = false;
    private BlockPos lastPos;

    public Blink() {
        super("Blink", "Suspends movement packets until a threshold is reached", Category.PLAYER, true, false, false);
    }

    @Override
    public void onEnable() {
        if (!Module.fullNullCheck()) {
            lastPos = mc.player.getPosition();
            spawnPlayer(false);
        } else {
            disable();
        }
    }

    @Override
    public void onDisable() {
        if (!Module.fullNullCheck()) {
            sendPackets();
            spawnPlayer(true);
        }
    }

    @Override
    public void onUpdate() {
        if (!Module.fullNullCheck()) {
            if (mode.getValue() == Mode.TIME && timer.passedS(time.getValue().doubleValue())) {
                sendPackets();
                spawnPlayer(false);
                timer.reset();
            }

            if (mode.getValue() == Mode.DISTANCE && mc.player.getDistance(lastPos.getX(), lastPos.getY(), lastPos.getZ()) >= distance.getValue()) {
                sendPackets();
                spawnPlayer(false);
                lastPos = mc.player.getPosition();
            }
        }
    }

    @Override
    public void onLogout() {
        playerPackets.clear();
        timer.reset();
        fakePlayer = null;
        pauseSaving = false;
        disable();
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (!Module.fullNullCheck() && event.getPacket() instanceof CPacketPlayer && pauseSaving) {
            playerPackets.add(event.getPacket());
            event.setCanceled(true);

            if (playerPackets.size() >= packets.getValue()) {
                sendPackets();
                spawnPlayer(false);
            }
        }
    }

    private void spawnPlayer(boolean justRemove) {
        if (fakePlayer != null) {
            mc.world.removeEntity(fakePlayer);
            mc.world.removeEntityDangerously(fakePlayer);
        }

        if (!spawnPlayer.getValue() || justRemove) {
            return;
        }

        fakePlayer = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
        fakePlayer.copyLocationAndAnglesFrom(mc.player);
        fakePlayer.inventory.copyInventory(mc.player.inventory);
        fakePlayer.setEntityId(-694201337);

        mc.world.spawnEntity(fakePlayer);
    }

    private void sendPackets() {
        pauseSaving = true;
        for (int i = 0; i < playerPackets.size(); ++i) {
            CPacketPlayer packet = playerPackets.get(i);
            mc.player.connection.sendPacket(packet);
            playerPackets.remove(packet);
        }
        pauseSaving = false;
    }

    public enum Mode {
        MANUAL,
        TIME,
        DISTANCE,
        PACKETS;
    }
}

