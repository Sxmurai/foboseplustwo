package me.fobose.client.features.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import me.fobose.client.Fobose;
import me.fobose.client.features.Feature;
import me.fobose.client.features.gui.components.Component;
import me.fobose.client.features.gui.components.items.Item;
import me.fobose.client.features.gui.components.items.buttons.ModuleButton;
import me.fobose.client.features.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

public class PhobosGui extends GuiScreen {
    private final ArrayList<Component> components = new ArrayList<>();
    private static PhobosGui INSTANCE;

    public PhobosGui() {
        this.setInstance();
        this.load();
    }

    public static PhobosGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PhobosGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static PhobosGui getClickGui() {
        return PhobosGui.getInstance();
    }

    private void load() {
        int x = -84;

        for (final Module.Category category : Fobose.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), x += 90, 4, true) {
                @Override
                public void setupItems() {
                    Fobose.moduleManager.getModulesByCategory(category).stream().filter((m) -> !m.hidden).forEach(m -> addButton(new ModuleButton(m)));
                }
            });
        }

        this.components.forEach(components -> components.getItems().sort(Comparator.comparing(Feature::getName)));
    }

    public void updateModule(Module module) {
        block0: for (Component component : this.components) {
            for (Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) continue;
                ModuleButton button = (ModuleButton)item;
                Module mod = button.getModule();
                if (module == null || !module.equals(mod)) continue;
                button.initSettings();
                continue block0;
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.checkMouseWheel();
        this.drawDefaultBackground();
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
    }

    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public final ArrayList<Component> getComponents() {
        return this.components;
    }

    public void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        } else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
    }

    public int getTextOffset() {
        return -6;
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }

    static {
        INSTANCE = new PhobosGui();
    }
}

