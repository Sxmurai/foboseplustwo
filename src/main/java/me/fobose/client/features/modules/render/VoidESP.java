

package me.fobose.client.features.modules.render;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import me.fobose.client.event.events.Render3DEvent;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.util.BlockUtil;
import me.fobose.client.util.EntityUtil;
import me.fobose.client.util.RenderUtil;
import me.fobose.client.util.RotationUtil;
import me.fobose.client.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class VoidESP
extends Module {
    private final Setting<Float> radius = this.register(new Setting<Float>("Radius", 8.0f, 0.0f, 50.0f));
    public Setting<Boolean> air = this.register(new Setting<Boolean>("OnlyAir", true));
    public Setting<Boolean> noEnd = this.register(new Setting<Boolean>("NoEnd", true));
    private Setting<Integer> updates = this.register(new Setting<Integer>("Updates", 500, 0, 1000));
    private Setting<Integer> voidCap = this.register(new Setting<Integer>("VoidCap", 500, 0, 1000));
    public Setting<Boolean> box = this.register(new Setting<Boolean>("Box", true));
    public Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", true));
    public Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Sync", false));
    public Setting<Double> height = this.register(new Setting<Double>("Height", 0.0, -2.0, 2.0));
    private Setting<Integer> red = this.register(new Setting<Integer>("Red", 0, 0, 255));
    private Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    private Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 0, 0, 255));
    private Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    private Setting<Integer> boxAlpha = this.register(new Setting<Object>("BoxAlpha", 125, Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue()));
    private Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue()));
    public Setting<Boolean> customOutline = this.register(new Setting<Object>("CustomLine", Boolean.valueOf(false), v -> this.outline.getValue()));
    private Setting<Integer> cRed = this.register(new Setting<Object>("OL-Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() != false && this.outline.getValue() != false));
    private Setting<Integer> cGreen = this.register(new Setting<Object>("OL-Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() != false && this.outline.getValue() != false));
    private Setting<Integer> cBlue = this.register(new Setting<Object>("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() != false && this.outline.getValue() != false));
    private Setting<Integer> cAlpha = this.register(new Setting<Object>("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() != false && this.outline.getValue() != false));
    private final Timer timer = new Timer();
    private List<BlockPos> voidHoles = new CopyOnWriteArrayList<BlockPos>();

    public VoidESP() {
        super("VoidEsp", "Esps the void", Module.Category.RENDER, true, false, false);
    }

    @Override
    public void onToggle() {
        this.timer.reset();
    }

    @Override
    public void onLogin() {
        this.timer.reset();
    }

    @Override
    public void onTick() {
        if (!(VoidESP.fullNullCheck() || this.noEnd.getValue().booleanValue() && VoidESP.mc.player.dimension == 1 || !this.timer.passedMs(this.updates.getValue().intValue()))) {
            this.voidHoles.clear();
            this.voidHoles = this.findVoidHoles();
            if (this.voidHoles.size() > this.voidCap.getValue()) {
                this.voidHoles.clear();
            }
            this.timer.reset();
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (VoidESP.fullNullCheck() || this.noEnd.getValue().booleanValue() && VoidESP.mc.player.dimension == 1) {
            return;
        }
        for (BlockPos pos : this.voidHoles) {
            if (!RotationUtil.isInFov(pos)) continue;
            RenderUtil.drawBoxESP(pos, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), false, false, false, false, 0);
        }
    }

    private List<BlockPos> findVoidHoles() {
        BlockPos playerPos = EntityUtil.getPlayerPos((EntityPlayer)VoidESP.mc.player);
        return BlockUtil.getDisc(playerPos.add(0, -playerPos.getY(), 0), this.radius.getValue().floatValue()).stream().filter(this::isVoid).collect(Collectors.toList());
    }

    private boolean isVoid(BlockPos pos) {
        return (VoidESP.mc.world.getBlockState(pos).getBlock() == Blocks.AIR || this.air.getValue() == false && VoidESP.mc.world.getBlockState(pos).getBlock() != Blocks.BEDROCK) && pos.getY() < 1 && pos.getY() >= 0;
    }
}

