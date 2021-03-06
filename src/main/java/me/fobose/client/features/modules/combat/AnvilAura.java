
package me.fobose.client.features.modules.combat;

import java.util.ArrayList;
import java.util.List;
import me.fobose.client.event.events.PacketEvent;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.mixin.mixins.network.packets.c2s.ICPacketPlayer;
import me.fobose.client.util.BlockUtil;
import me.fobose.client.util.InventoryUtil;
import me.fobose.client.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AnvilAura
extends Module {
    public Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(6.0f), Float.valueOf(0.0f), Float.valueOf(10.0f)));
    public Setting<Float> wallsRange = this.register(new Setting<Float>("WallsRange", Float.valueOf(3.5f), Float.valueOf(0.0f), Float.valueOf(10.0f)));
    public Setting<Integer> placeDelay = this.register(new Setting<Integer>("PlaceDelay", 0, 0, 500));
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    public Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", true));
    public Setting<Boolean> switcher = this.register(new Setting<Boolean>("Switch", true));
    public Setting<Integer> rotations = this.register(new Setting<Integer>("Spoofs", 1, 1, 20));
    private float yaw = 0.0f;
    private float pitch = 0.0f;
    private boolean rotating = false;
    private int rotationPacketsSpoofed = 0;
    private EntityPlayer finalTarget;
    private BlockPos placeTarget;

    public AnvilAura() {
        super("AnvilAura", "Useless", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        this.doAnvilAura();
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && this.rotate.getValue().booleanValue() && this.rotating) {
            if (event.getPacket() instanceof CPacketPlayer) {
                ICPacketPlayer packet = (ICPacketPlayer)event.getPacket();
                packet.setYaw(this.yaw);
                packet.setPitch(this.pitch);
            }
            ++this.rotationPacketsSpoofed;
            if (this.rotationPacketsSpoofed >= this.rotations.getValue()) {
                this.rotating = false;
                this.rotationPacketsSpoofed = 0;
            }
        }
    }

    public void doAnvilAura() {
        this.finalTarget = this.getTarget();
        if (this.finalTarget != null) {
            this.placeTarget = this.getTargetPos((Entity)this.finalTarget);
        }
        if (this.placeTarget != null && this.finalTarget != null) {
            this.placeAnvil(this.placeTarget);
        }
    }

    public void placeAnvil(BlockPos pos) {
        if (this.rotate.getValue().booleanValue()) {
            this.rotateToPos(pos);
        }
        if (this.switcher.getValue().booleanValue() && !this.isHoldingAnvil()) {
            this.doSwitch();
        }
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, false, this.packet.getValue(), AnvilAura.mc.player.isSneaking());
    }

    public boolean isHoldingAnvil() {
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        return AnvilAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock && ((ItemBlock)AnvilAura.mc.player.getHeldItemMainhand().getItem()).getBlock() instanceof BlockAnvil || AnvilAura.mc.player.getHeldItemOffhand().getItem() instanceof ItemBlock && ((ItemBlock)AnvilAura.mc.player.getHeldItemOffhand().getItem()).getBlock() instanceof BlockAnvil;
    }

    public void doSwitch() {
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (obbySlot == -1) {
            for (int l = 0; l < 9; ++l) {
                ItemStack stack = AnvilAura.mc.player.inventory.getStackInSlot(l);
                Block block = ((ItemBlock)stack.getItem()).getBlock();
                if (!(block instanceof BlockObsidian)) continue;
                obbySlot = l;
            }
        }
        if (obbySlot != -1) {
            AnvilAura.mc.player.inventory.currentItem = obbySlot;
        }
    }

    public EntityPlayer getTarget() {
        double shortestDistance = -1.0;
        EntityPlayer target = null;
        for (EntityPlayer player : AnvilAura.mc.world.playerEntities) {
            if (this.getPlaceableBlocksAboveEntity((Entity)player).isEmpty() || shortestDistance != -1.0 && !(AnvilAura.mc.player.getDistanceSq((Entity)player) < MathUtil.square(shortestDistance))) continue;
            shortestDistance = AnvilAura.mc.player.getDistance((Entity)player);
            target = player;
        }
        return target;
    }

    public BlockPos getTargetPos(Entity target) {
        double distance = -1.0;
        BlockPos finalPos = null;
        for (BlockPos pos : this.getPlaceableBlocksAboveEntity(target)) {
            if (distance != -1.0 && !(AnvilAura.mc.player.getDistanceSq(pos) < MathUtil.square(distance))) continue;
            finalPos = pos;
            distance = AnvilAura.mc.player.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
        }
        return finalPos;
    }

    public List<BlockPos> getPlaceableBlocksAboveEntity(Entity target) {
        BlockPos pos;
        BlockPos playerPos = new BlockPos(Math.floor(AnvilAura.mc.player.posX), Math.floor(AnvilAura.mc.player.posY), Math.floor(AnvilAura.mc.player.posZ));
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        for (int i = (int)Math.floor(AnvilAura.mc.player.posY + 2.0); i <= 256 && BlockUtil.isPositionPlaceable(pos = new BlockPos(Math.floor(AnvilAura.mc.player.posX), (double)i, Math.floor(AnvilAura.mc.player.posZ)), false) != 0 && BlockUtil.isPositionPlaceable(pos, false) != -1 && BlockUtil.isPositionPlaceable(pos, false) != 2; ++i) {
            positions.add(pos);
        }
        return positions;
    }

    private void rotateToPos(BlockPos pos) {
        if (this.rotate.getValue().booleanValue()) {
            float[] angle = MathUtil.calcAngle(AnvilAura.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((double)((float)pos.getX() + 0.5f), (double)((float)pos.getY() - 0.5f), (double)((float)pos.getZ() + 0.5f)));
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }
}

