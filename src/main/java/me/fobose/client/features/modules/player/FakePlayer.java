package me.fobose.client.features.modules.player;

import com.mojang.authlib.GameProfile;
import me.fobose.client.event.events.ValueChangeEvent;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.util.PlayerUtil;
import me.fobose.client.util.Timer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

public class FakePlayer extends Module {
    public static FakePlayer INSTANCE;

    public final Setting<String> username = this.register(new Setting<>("Username", "Fit"));
    public final Setting<Boolean> fetchUUID = this.register(new Setting<>("FetchUUID", true));
    public final Setting<Boolean> copyInventory = this.register(new Setting<>("CopyInventory", true));
    // @todo public final Setting<Boolean> moving = this.register(new Setting<>("Moving", false));

    public final Setting<AutoRemove> autoRemove = this.register(new Setting<>("AutoRemove", AutoRemove.NONE));
    public final Setting<Integer> distance = this.register(new Setting<>("Distance", 10, (v) -> autoRemove.getValue() == AutoRemove.DISTANCE));
    public final Setting<Integer> time = this.register(new Setting<>("Time", 10, (v) -> autoRemove.getValue() == AutoRemove.TIME));

    public EntityOtherPlayerMP fakePlayer;
    private final Timer timer = new Timer();
    private BlockPos lastPos = null;

    public FakePlayer() {
        super("FakePlayer", "makes a fakeplayer and shit", Category.PLAYER, false, false, false);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (!Module.fullNullCheck()) {
            UUID uuid = UUID.randomUUID();

            if (fetchUUID.getValue()) {
                UUID playerUuid = PlayerUtil.getUUIDFromName(username.getName());
                if (playerUuid != null) {
                    uuid = playerUuid;
                }
            }

            fakePlayer = new EntityOtherPlayerMP(mc.player.world, new GameProfile(uuid, username.getValue()));
            fakePlayer.copyLocationAndAnglesFrom(mc.player);
            fakePlayer.setEntityId(-694201337);

            if (copyInventory.getValue()) {
                fakePlayer.inventory.copyInventory(mc.player.inventory);
            }

            mc.world.spawnEntity(fakePlayer);
        } else {
            disable();
        }
    }

    @Override
    public void onDisable() {
        if (!Module.fullNullCheck()) {
            remove();
        }

        timer.reset();
        lastPos = null;
        fakePlayer = null;
    }

    @SubscribeEvent
    public void onSettingChange(ValueChangeEvent event) {
        if (!Module.fullNullCheck() && event.setting.getName().equalsIgnoreCase("AutoRemove")) {
            if (event.setting.getValue() == AutoRemove.TIME) {
                lastPos = null;
                timer.reset();
            } else if (event.setting.getValue() == AutoRemove.DISTANCE) {
                lastPos = mc.player.getPosition();
            }
        }
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (!Module.fullNullCheck() && event.getTarget().getEntityId() == fakePlayer.getEntityId() && autoRemove.getValue() == AutoRemove.ATTACK) {
            remove();
            disable();
        }
    }

    @Override
    public void onUpdate() {
        if (!Module.fullNullCheck()) {
            if (autoRemove.getValue() == AutoRemove.DISTANCE) {
                if (lastPos == null) {
                    lastPos = mc.player.getPosition();
                }

                if (mc.player.getDistance(lastPos.getX(), lastPos.getY(), lastPos.getZ()) >= distance.getValue()) {
                    remove();
                    disable();
                }
            } else if (autoRemove.getValue() == AutoRemove.TIME) {
                if (timer.passedS(time.getValue())) {
                    remove();
                    disable();
                }
            }
        }
    }

    private void remove() {
        if (fakePlayer != null) {
            mc.world.removeEntity(fakePlayer);
            mc.world.removeEntityDangerously(fakePlayer);
        }
    }

    public enum AutoRemove {
        NONE,
        ATTACK,
        DISTANCE,
        TIME
    }
}

