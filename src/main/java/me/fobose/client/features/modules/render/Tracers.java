package me.fobose.client.features.modules.render;

import me.fobose.client.Fobose;
import me.fobose.client.event.events.Render3DEvent;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.util.EntityUtil;
import me.fobose.client.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Tracers extends Module {
    public Setting<Boolean> players = this.register(new Setting<>("Players", true));
    public Setting<Boolean> mobs = this.register(new Setting<>("Mobs", false));
    public Setting<Boolean> animals = this.register(new Setting<>("Animals", false));
    public Setting<Boolean> invisibles = this.register(new Setting<>("Invisibles", false));
    public Setting<Float> width = this.register(new Setting<>("Width", 1.0f, 0.1f, 5.0f));
    public Setting<Integer> distance = this.register(new Setting<>("Radius", 300, 0, 300));
    public final Setting<Float> lineWidth = this.register(new Setting<>("LineWidth", 1.0f, 0.1f, 5.0f));

    public Tracers() {
        super("Tracers", "Draws lines to other players.", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (!Module.fullNullCheck()) {
            GL11.glPushMatrix();

            for (int i = 0; i < mc.world.loadedEntityList.size(); ++i) {
                Entity entity = mc.world.loadedEntityList.get(i);
                if (!EntityUtil.isLiving(entity) || entity == mc.player || mc.player.getDistance(entity) > distance.getValue() || (!invisibles.getValue() && entity.isInvisibleToPlayer(mc.player)) || (!players.getValue() && EntityUtil.isPlayer(entity)) || (!mobs.getValue() && !EntityUtil.isPassive(entity)) || (!animals.getValue() && EntityUtil.isFriendlyMob(entity))) {
                    continue;
                }

                float[] colors;
                if (entity instanceof EntityPlayer && Fobose.friendManager.isFriend(entity.getName())) {
                    colors = new float[] { 0.0f, 0.5f, 1.0f, 1.0f };
                } else {
                    float distance = mc.player.getDistance(entity) / 20.0f;
                    colors = new float[] { 2 - distance, distance, 0.0f, 1.0f };
                }

                Vec3d eyes = mc.player.getLookVec();
                Vec3d interpolated = EntityUtil.getInterpolatedRenderPos(EntityUtil.interpolateEntity(entity, event.getPartialTicks()));

                RenderUtil.drawLine(eyes.x, eyes.y + mc.player.getEyeHeight(), eyes.z, interpolated.x, interpolated.y, interpolated.z, lineWidth.getValue(), colors[0], colors[1], colors[2], colors[3]);
            }

            GL11.glPopMatrix();
        }
    }
}

