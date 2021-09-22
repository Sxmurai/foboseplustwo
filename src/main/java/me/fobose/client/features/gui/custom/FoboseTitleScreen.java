package me.fobose.client.features.gui.custom;

import me.fobose.client.Fobose;
import me.fobose.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class FoboseTitleScreen extends GuiScreen {
    private static final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation("textures/fobose_background.png");

    private float x, y;
    private float xOffset, yOffset;

    public void initGui() {
        this.x = this.width / 2;
        this.y = this.height / 4 + 48;
        this.buttonList.add(new TextButton(0, (int) this.x, (int) this.y + 20, "Singleplayer"));
        this.buttonList.add(new TextButton(1, (int) this.x, (int) this.y + 44, "Multiplayer"));
        this.buttonList.add(new TextButton(2, (int) this.x, (int) this.y + 66, "Settings"));
        this.buttonList.add(new TextButton(2, (int) this.x, (int) this.y + 88, "Exit"));
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    // @todo clean this dogshit up wtf
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(this.x - Fobose.textManager.getStringWidth("Singleplayer") / 2, this.y + 20, Fobose.textManager.getStringWidth("Singleplayer"), Fobose.textManager.getFontHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
        } else if (isHovered(this.x - Fobose.textManager.getStringWidth("Multiplayer") / 2, this.y + 44, Fobose.textManager.getStringWidth("Multiplayer"), Fobose.textManager.getFontHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        } else if (isHovered(this.x - Fobose.textManager.getStringWidth("Settings") / 2, this.y + 66, Fobose.textManager.getStringWidth("Settings"), Fobose.textManager.getFontHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        } else if (isHovered(this.x - Fobose.textManager.getStringWidth("Exit") / 2, this.y + 88, Fobose.textManager.getStringWidth("Exit"), Fobose.textManager.getFontHeight(), mouseX, mouseY)) {
            this.mc.shutdown();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.xOffset = -1.0f * (((float) mouseX - (float) width / 2.0f) / ((float) width / 32.0f));
        this.yOffset = -1.0f * (((float) mouseY - (float) height / 2.0f) / ((float) height / 18.0f));
        this.x = width / 2.0f;
        this.y = height / 4.0f + 48.0f;

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        mc.getTextureManager().bindTexture(BACKGROUND_LOCATION);
        drawBackgroundImage(-16.0f + xOffset, -9.0f + yOffset, width + 32, height + 18);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawBackgroundImage(float posX, float posY, float width, float height) {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, 0.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(0.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(0.0f, height, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f(width, height, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f(width, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    private static boolean isHovered(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY < y + height;
    }

    private static class TextButton extends GuiButton {
        public TextButton(int buttonId, int x, int y, String buttonText) {
            super(buttonId, x, y, Fobose.textManager.getStringWidth(buttonText), Fobose.textManager.getFontHeight(), buttonText);
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                this.enabled = true;
                this.hovered = (float)mouseX >= (float)this.x - (float)Fobose.textManager.getStringWidth(this.displayString) / 2.0f && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                Fobose.textManager.drawStringWithShadow(this.displayString, (float)this.x - (float)Fobose.textManager.getStringWidth(this.displayString) / 2.0f, this.y, Color.WHITE.getRGB());
                if (this.hovered) {
                    RenderUtil.drawLine((float)(this.x - 1) - (float)Fobose.textManager.getStringWidth(this.displayString) / 2.0f, this.y + 2 + Fobose.textManager.getFontHeight(), (float)this.x + (float)Fobose.textManager.getStringWidth(this.displayString) / 2.0f + 1.0f, this.y + 2 + Fobose.textManager.getFontHeight(), 1.0f, Color.WHITE.getRGB());
                }
            }
        }

        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            return this.enabled && this.visible && (float)mouseX >= (float)this.x - (float)Fobose.textManager.getStringWidth(this.displayString) / 2.0f && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        }
    }
}
