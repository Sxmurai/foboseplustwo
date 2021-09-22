package me.fobose.client.features.notifications;

import me.fobose.client.Fobose;
import me.fobose.client.features.modules.client.HUD;
import me.fobose.client.util.RenderUtil;
import me.fobose.client.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Notification {
    private final String text;
    private final long disableTime;
    private final float width;
    private final Timer timer = new Timer();

    public Notification(String text, long disableTime) {
        this.text = text;
        this.disableTime = disableTime;
        this.width = Fobose.moduleManager.getModuleByClass(HUD.class).renderer.getStringWidth(text);
        this.timer.reset();
    }

    public void onDraw(int y) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        if (this.timer.passedMs(this.disableTime)) {
            Fobose.notificationManager.getNotifications().remove(this);
        }
        RenderUtil.drawRect((float)(scaledResolution.getScaledWidth() - 4) - this.width, y, scaledResolution.getScaledWidth() - 2, y + Fobose.moduleManager.getModuleByClass(HUD.class).renderer.getFontHeight() + 3, 0x75000000);
        Fobose.moduleManager.getModuleByClass(HUD.class).renderer.drawString(this.text, (float)scaledResolution.getScaledWidth() - this.width - 3.0f, y + 2, -1, true);
    }
}

