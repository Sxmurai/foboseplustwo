package me.fobose.client.features.modules.movement;

import me.fobose.client.event.events.KeyEvent;
import me.fobose.client.event.events.PacketEvent;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class NoSlowDown extends Module {
    private static final KeyBinding[] MOVE_BINDS = new KeyBinding[] {
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindJump,
            mc.gameSettings.keyBindSneak
    };
    public static NoSlowDown INSTANCE;

    public final Setting<Boolean> items = this.register(new Setting<>("Items", true));
    public final Setting<Boolean> soulSand = this.register(new Setting<>("SoulSand", false));
    public final Setting<Boolean> endPortals = this.register(new Setting<>("EndPortals", false));
    public final Setting<Boolean> slime = this.register(new Setting<>("Slime", false));
    public final Setting<Boolean> strict = this.register(new Setting<>("Strict", false));
    public final Setting<Boolean> sneakPacket = this.register(new Setting<>("SneakPacket", false));
    public final Setting<Boolean> guiMove = this.register(new Setting<>("GuiMove", false));
    public final Setting<Boolean> arrowLook = this.register(new Setting<>("ArrowLook", false, (v) -> guiMove.getValue()));
    public final Setting<Float> arrowSensitivity = this.register(new Setting<>("ArrowSensitivity", 0.3f, 0.1f, 3.0f, (v) -> guiMove.getValue() && arrowLook.getValue()));

    private boolean sneaking = false;

    public NoSlowDown() {
        super("NoSlowDown", "cums in your mom and makes her not so slow", Category.MOVEMENT, true, false, false);
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        sneaking = false;
    }

    @Override
    public void onUpdate() {
        sneaking = sneakPacket.getValue() && mc.player.isHandActive();
        if (!mc.player.isHandActive() && sneakPacket.getValue()) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            sneaking = false;
        }

        if (guiMove.getValue() && mc.currentScreen != null) {
            for (KeyBinding bind : MOVE_BINDS) {
                KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
            }

            if (arrowLook.getValue()) {
                // yandere dev be like
                if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                    mc.player.rotationPitch = Math.max(-90.0f, mc.player.rotationPitch - (arrowSensitivity.getValue() * 10.0f));
                } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                    mc.player.rotationPitch = Math.min(90.0f, mc.player.rotationPitch + (arrowSensitivity.getValue() * 10.0f));
                } else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    mc.player.rotationYaw += arrowSensitivity.getValue() * 10.0f;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    mc.player.rotationYaw -= arrowSensitivity.getValue() * 10.0f;
                }
            }
        }
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event) {
        if (!Module.fullNullCheck() && items.getValue() && mc.player.isHandActive()) {
            event.getMovementInput().moveForward *= 5.0f;
            event.getMovementInput().moveStrafe *= 5.0f;
        }
    }

    @SubscribeEvent
    public void onUseItem(PlayerInteractEvent.RightClickItem event) {
        if (!Module.fullNullCheck() && items.getValue() && sneaking) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (!Module.fullNullCheck() && items.getValue() && strict.getValue() && event.getPacket() instanceof CPacketPlayer && mc.player.isHandActive()) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, mc.player.getPosition(), EnumFacing.DOWN));
        }
    }

    @SubscribeEvent
    public void onKeyEvent(KeyEvent event) {
        if (!Module.fullNullCheck() && guiMove.getValue() && event.getStage() == 0 && !(mc.currentScreen instanceof GuiChat)) {
            event.info = event.pressed;
        }
    }
}

