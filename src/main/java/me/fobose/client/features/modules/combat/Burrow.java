package me.fobose.client.features.modules.combat;

import me.fobose.client.features.command.Command;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.mixin.mixins.IMinecraft;
import me.fobose.client.util.BlockUtil;
import me.fobose.client.util.InventoryUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

// @todo credit https://github.com/ciruu1/InstantBurrow/blob/master/InstantBurrow.java
public class Burrow extends Module {
    public static Burrow INSTANCE;

    public final Setting<Mode> mode = this.register(new Setting<>("Mode", Mode.INSTANT));
    public final Setting<BlockType> blockType = this.register(new Setting<>("Block", BlockType.OBSIDIAN));
    public final Setting<Boolean> rotate = this.register(new Setting<>("Rotate", false));
    public final Setting<Boolean> silentSwitch = this.register(new Setting<>("SilentSwitch", false));
    public final Setting<Float> rubberband = this.register(new Setting<>("Rubberband", 2.0f, -5.0f, 5.0f));

    private BlockPos pos;
    private int oldSlot = -1;

    public Burrow() {
        super("Burrow", "burrows you into a block", Category.COMBAT, false, false, false);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (Module.fullNullCheck()) {
            disable();
            return;
        }

        pos = new BlockPos(mc.player.getPositionVector());

        int slot = InventoryUtil.findHotbarBlock(blockType.getValue() == BlockType.OBSIDIAN ? Blocks.OBSIDIAN : Blocks.ENDER_CHEST);
        if (slot == -1) {
            Command.sendMessage("No block found in hotbar. Toggling...");
            disable();
            return;
        }

        oldSlot = mc.player.inventory.currentItem;
        InventoryUtil.switchToHotbarSlot(slot, silentSwitch.getValue());
    }

    @Override
    public void onUpdate() {
        if (pos == null || oldSlot == -1) {
            return;
        }

        if (mode.getValue() == Mode.INSTANT) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698, mc.player.posZ, true));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997, mc.player.posZ, true));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214, mc.player.posZ, true));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821, mc.player.posZ, true));

            BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, rotate.getValue(), true, mc.player.isSneaking());

            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + rubberband.getValue(), mc.player.posZ, true));

            mc.player.inventory.currentItem = oldSlot;
            oldSlot = -1;
            pos = null;

            ((IMinecraft) mc).setRightClickDelayTimer(4);
            disable();
        } else if (mode.getValue() == Mode.NORMAL) {
            mc.player.jump();

            BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, rotate.getValue(), true, mc.player.isSneaking());

            mc.player.motionY = rubberband.getValue();
            mc.player.inventory.currentItem = oldSlot;

            oldSlot = -1;
            pos = null;

            ((IMinecraft) mc).setRightClickDelayTimer(4);
            disable();
        }
    }

    public enum Mode {
        INSTANT,
        NORMAL
    }

    public enum BlockType {
        OBSIDIAN,
        ECHEST
    }
}
