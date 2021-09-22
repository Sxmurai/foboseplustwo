package me.fobose.client.features.modules.render;

import java.util.ArrayList;
import java.util.List;
import me.fobose.client.event.events.Render3DEvent;
import me.fobose.client.features.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemSnowball;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

public class Trajectories extends Module {
    // @todo add color options for NoHit Color, EntityHit color

    public Trajectories() {
        super("Trajectories", "Shows where projectiles will land", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (!Module.fullNullCheck()) {
            this.drawTrajectories(Trajectories.mc.player, event.getPartialTicks());
        }
    }

    public void enableGL3D(float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        Trajectories.mc.entityRenderer.disableLightmap();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(lineWidth);
    }

    public void disableGL3D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    private void drawTrajectories(EntityPlayer player, float partialTicks) {
        float pow = 0;
        double renderPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
        double renderPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
        double renderPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;

        if (!(Trajectories.mc.gameSettings.thirdPersonView == 0 && (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow || player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemFishingRod || player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEnderPearl || player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEgg || player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSnowball || player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemExpBottle))) {
            return;
        }

        GL11.glPushMatrix();
        Item item = player.getHeldItem(EnumHand.MAIN_HAND).getItem();

        double posX = renderPosX - (double) (MathHelper.cos(player.rotationYaw / 180.0f * (float) Math.PI) * 0.16f);
        double posY = renderPosY + (double) player.getEyeHeight() - 0.1000000014901161;
        double posZ = renderPosZ - (double) (MathHelper.sin(player.rotationYaw / 180.0f * (float) Math.PI) * 0.16f);
        double motionX = (double) (-MathHelper.sin(player.rotationYaw / 180.0f * (float) Math.PI) * MathHelper.cos(player.rotationPitch / 180.0f * (float) Math.PI)) * (item instanceof ItemBow ? 1.0 : 0.4);
        double motionY = (double) (-MathHelper.sin(player.rotationPitch / 180.0f * (float) Math.PI)) * (item instanceof ItemBow ? 1.0 : 0.4);
        double motionZ = (double) (MathHelper.cos(player.rotationYaw / 180.0f * (float) Math.PI) * MathHelper.cos(player.rotationPitch / 180.0f * (float) Math.PI)) * (item instanceof ItemBow ? 1.0 : 0.4);

        int useAmount = 72000 - player.getItemInUseCount();
        float power = (float) useAmount / 20.0f;
        power = (power * power + power * 2.0f) / 3.0f;
        if (power > 1.0f) {
            power = 1.0f;
        }

        float distance = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
        motionX /= distance;
        motionY /= distance;
        motionZ /= distance;
        motionX *= pow * (item instanceof ItemFishingRod ? 0.75f : (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE ? 0.75f : 1.5f));
        motionY *= pow * (item instanceof ItemFishingRod ? 0.75f : (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE ? 0.75f : 1.5f));
        motionZ *= pow * (item instanceof ItemFishingRod ? 0.75f : (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE ? 0.75f : 1.5f));

        this.enableGL3D(2.0f);

        if (power > 0.6f) {
            GlStateManager.color(0.0f, 1.0f, 0.0f, 1.0f);
        } else {
            GlStateManager.color(0.8f, 0.5f, 0.0f, 1.0f);
        }

        GL11.glEnable(2848);

        float size = item instanceof ItemBow ? 0.3f : 0.25f;
        boolean hasLanded = false;
        Entity landingOnEntity = null;
        RayTraceResult landingPosition = null;

        while (!hasLanded && posY > 0.0) {
            Vec3d present = new Vec3d(posX, posY, posZ);
            Vec3d future = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
            RayTraceResult possibleLandingStrip = Trajectories.mc.world.rayTraceBlocks(present, future, false, true, false);

            if (possibleLandingStrip != null && possibleLandingStrip.typeOfHit != RayTraceResult.Type.MISS) {
                landingPosition = possibleLandingStrip;
                hasLanded = true;
            }

            AxisAlignedBB arrowBox = new AxisAlignedBB(posX - (double) size, posY - (double) size, posZ - (double) size, posX + (double) size, posY + (double) size, posZ + (double) size);

            for (Entity entity : this.getEntitiesWithinAABB(arrowBox.offset(motionX, motionY, motionZ).expand(1.0, 1.0, 1.0))) {
                if (entity == null || !entity.canBeCollidedWith() || entity == player) {
                    continue;
                }

                AxisAlignedBB box = entity.getEntityBoundingBox().expand(0.3f, 0.3f, 0.3f);
                RayTraceResult possibleEntityLanding = box.calculateIntercept(present, future);

                if (possibleEntityLanding == null) {
                    continue;
                }

                hasLanded = true;
                landingOnEntity = entity;
                landingPosition = possibleEntityLanding;
            }

            if (landingOnEntity != null) {
                GlStateManager.color(1.0f, 0.0f, 0.0f, 1.0f);
            }

            posX += motionX;
            posY += motionY;
            posZ += motionZ;

            motionX *= 0.99f;
            motionY *= 0.99f;
            motionZ *= 0.99f;
            motionY -= item instanceof ItemBow ? 0.05 : 0.03;
        }

        if (landingPosition != null && landingPosition.typeOfHit == RayTraceResult.Type.BLOCK) {
            GlStateManager.translate(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);

            int side = landingPosition.sideHit.getIndex();
            GlStateManager.rotate(90.0f, side == 2 || side == 3 ? 1.0f : 0.0f, 0.0f, side == 4 || side == 5 ? 1.0f : 0.0f);

            Cylinder c = new Cylinder();
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            c.setDrawStyle(100011);

            if (landingOnEntity != null) {
                GlStateManager.color(0.0f, 0.0f, 0.0f, 1.0f);
                GL11.glLineWidth(2.5f);
                c.draw(0.6f, 0.3f, 0.0f, 4, 1);
                GL11.glLineWidth(0.1f);
                GlStateManager.color(1.0f, 0.0f, 0.0f, 1.0f);
            }

            c.draw(0.6f, 0.3f, 0.0f, 4, 1);
        }

        this.disableGL3D();
        GL11.glPopMatrix();
    }

    private List<Entity> getEntitiesWithinAABB(AxisAlignedBB bb) {
        final ArrayList<Entity> entities = new ArrayList<>();

        int chunkMinX = MathHelper.floor((bb.minX - 2.0) / 16.0);
        int chunkMaxX = MathHelper.floor((bb.maxX + 2.0) / 16.0);
        int chunkMinZ = MathHelper.floor((bb.minZ - 2.0) / 16.0);
        int chunkMaxZ = MathHelper.floor((bb.maxZ + 2.0) / 16.0);

        for (int x = chunkMinX; x <= chunkMaxX; ++x) {
            for (int z = chunkMinZ; z <= chunkMaxZ; ++z) {
                if (Trajectories.mc.world.getChunkProvider().getLoadedChunk(x, z) == null) {
                    continue;
                }

                Trajectories.mc.world.getChunk(x, z).getEntitiesWithinAABBForEntity(Trajectories.mc.player, bb, entities, null);
            }
        }

        return entities;
    }
}

