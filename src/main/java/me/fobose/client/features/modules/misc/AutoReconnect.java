
package me.fobose.client.features.modules.misc;

import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.mixin.mixins.render.gui.IGuiDisconnected;
import me.fobose.client.util.MathUtil;
import me.fobose.client.util.Timer;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoReconnect
extends Module {
    private static ServerData serverData;
    private static AutoReconnect INSTANCE;
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 5));

    public AutoReconnect() {
        super("AutoReconnect", "Reconnects you if you disconnect.", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static AutoReconnect getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoReconnect();
        }
        return INSTANCE;
    }

    @SubscribeEvent
    public void sendPacket(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiDisconnected) {
            this.updateLastConnectedServer();
            if (AutoLog.getInstance().isOff()) {
                GuiDisconnected disconnected = (GuiDisconnected)event.getGui();
                event.setGui((GuiScreen)new GuiDisconnectedHook(disconnected));
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        this.updateLastConnectedServer();
    }

    public void updateLastConnectedServer() {
        ServerData data = mc.getCurrentServerData();
        if (data != null) {
            serverData = data;
        }
    }

    static {
        INSTANCE = new AutoReconnect();
    }

    private class GuiDisconnectedHook
    extends GuiDisconnected {
        private final Timer timer;

        public GuiDisconnectedHook(GuiDisconnected disconnected) {
            super(((IGuiDisconnected) disconnected).getParentScreen(), ((IGuiDisconnected) disconnected).getReason(), ((IGuiDisconnected) disconnected).getMessage());
            this.timer = new Timer();
            this.timer.reset();
        }

        public void updateScreen() {
            if (this.timer.passedS(((Integer)AutoReconnect.this.delay.getValue()).intValue())) {
                this.mc.displayGuiScreen((GuiScreen)new GuiConnecting(((IGuiDisconnected) this).getParentScreen(), this.mc, serverData == null ? this.mc.getCurrentServerData() : serverData));
            }
        }

        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            super.drawScreen(mouseX, mouseY, partialTicks);
            String s = "Reconnecting in " + MathUtil.round((double)((long)((Integer)AutoReconnect.this.delay.getValue() * 1000) - this.timer.getPassedTimeMs()) / 1000.0, 1);
            AutoReconnect.this.renderer.drawString(s, this.width / 2 - AutoReconnect.this.renderer.getStringWidth(s) / 2, this.height - 16, 0xFFFFFF, true);
        }
    }
}

