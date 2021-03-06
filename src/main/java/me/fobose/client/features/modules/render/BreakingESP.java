package me.fobose.client.features.modules.render;

import java.util.HashMap;
import java.util.Map;
import me.fobose.client.event.events.BlockBreakingEvent;
import me.fobose.client.event.events.Render3DEvent;
import me.fobose.client.features.modules.Module;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BreakingESP extends Module {
    private final Map<BlockPos, Integer> breakingProgressMap = new HashMap<>();

    public BreakingESP() {
        super("BreakingESP", "Shows block breaking progress", Module.Category.RENDER, true, false, false);
    }

    @SubscribeEvent
    public void onBlockBreak(BlockBreakingEvent event) {
        this.breakingProgressMap.put(event.pos, event.breakStage);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
    }

    public static enum Mode {
        BAR,
        ALPHA,
        WIDTH;

    }
}

