
package me.fobose.client.features.gui.components.items.buttons;

import me.fobose.client.Fobose;
import me.fobose.client.features.gui.PhobosGui;
import me.fobose.client.features.gui.components.Component;
import me.fobose.client.features.modules.client.ClickGui;
import me.fobose.client.features.modules.client.HUD;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.util.ColorUtil;
import me.fobose.client.util.MathUtil;
import me.fobose.client.util.RenderUtil;
import org.lwjgl.input.Mouse;

public class Slider
extends Button {
    public Setting setting;
    private Number min;
    private Number max;
    private int difference;

    public Slider(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.min = (Number)setting.getMin();
        this.max = (Number)setting.getMax();
        this.difference = this.max.intValue() - this.min.intValue();
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.dragSetting(mouseX, mouseY);
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height - 0.5f, !this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515);
        if (ClickGui.getInstance().rainbowRolling.getValue().booleanValue()) {
            int color = ColorUtil.changeAlpha(HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)), Fobose.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue());
            int color1 = ColorUtil.changeAlpha(HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)), Fobose.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue());
            RenderUtil.drawGradientRect(this.x, this.y, ((Number)this.setting.getValue()).floatValue() <= this.min.floatValue() ? 0.0f : ((float)this.width + 7.4f) * this.partialMultiplier(), (float)this.height - 0.5f, !this.isHovering(mouseX, mouseY) ? HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)) : color, !this.isHovering(mouseX, mouseY) ? HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)) : color1);
        } else {
            RenderUtil.drawRect(this.x, this.y, ((Number)this.setting.getValue()).floatValue() <= this.min.floatValue() ? this.x : this.x + ((float)this.width + 7.4f) * this.partialMultiplier(), this.y + (float)this.height - 0.5f, !this.isHovering(mouseX, mouseY) ? Fobose.colorManager.getColorWithAlpha(Fobose.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : Fobose.colorManager.getColorWithAlpha(Fobose.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue()));
        }
        Fobose.textManager.drawStringWithShadow(this.getName() + " " + "\u00a77" + (this.setting.getValue() instanceof Float ? (Number)((Number)this.setting.getValue()) : (Number)((Number)this.setting.getValue()).doubleValue()), this.x + 2.3f, this.y - 1.7f - (float)PhobosGui.getClickGui().getTextOffset(), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            this.setSettingFromX(mouseX);
        }
    }

    @Override
    public boolean isHovering(int mouseX, int mouseY) {
        for (Component component : PhobosGui.getClickGui().getComponents()) {
            if (!component.drag) continue;
            return false;
        }
        return (float)mouseX >= this.getX() && (float)mouseX <= this.getX() + (float)this.getWidth() + 8.0f && (float)mouseY >= this.getY() && (float)mouseY <= this.getY() + (float)this.height;
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    private void dragSetting(int mouseX, int mouseY) {
        if (this.isHovering(mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
            this.setSettingFromX(mouseX);
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    private void setSettingFromX(int mouseX) {
        float percent = ((float)mouseX - this.x) / ((float)this.width + 7.4f);
        if (this.setting.getValue() instanceof Double) {
            double result = (Double)this.setting.getMin() + (double)((float)this.difference * percent);
            this.setting.setValue((double)Math.round(10.0 * result) / 10.0);
        } else if (this.setting.getValue() instanceof Float) {
            float result = ((Float)this.setting.getMin()).floatValue() + (float)this.difference * percent;
            this.setting.setValue(Float.valueOf((float)Math.round(10.0f * result) / 10.0f));
        } else if (this.setting.getValue() instanceof Integer) {
            this.setting.setValue((Integer)this.setting.getMin() + (int)((float)this.difference * percent));
        }
    }

    private float middle() {
        return this.max.floatValue() - this.min.floatValue();
    }

    private float part() {
        return ((Number)this.setting.getValue()).floatValue() - this.min.floatValue();
    }

    private float partialMultiplier() {
        return this.part() / this.middle();
    }
}

