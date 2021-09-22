package me.fobose.client.features.modules.misc;

import me.fobose.client.event.events.PacketEvent;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PortalModifier extends Module {
    public Setting<Boolean> portalChat = this.register(new Setting<Boolean>("Chat", Boolean.valueOf(true), "Allows you to chat in portals."));
    public Setting<Boolean> godmode = this.register(new Setting<Boolean>("Godmode", Boolean.valueOf(false), "Portal Godmode."));
    public Setting<Boolean> fastPortal = this.register(new Setting<Boolean>("FastPortal", false));
    public Setting<Integer> cooldown = this.register(new Setting<Object>("Cooldown", 5, 1, 10, v -> this.fastPortal.getValue(), "Portal cooldown."));
    public Setting<Integer> time = this.register(new Setting<Object>("Time", 5, 0, 80, v -> this.fastPortal.getValue(), "Time in Portal"));
    private static PortalModifier INSTANCE = new PortalModifier();

    public PortalModifier() {
        super("PortalModifier", "Tweaks for Portals", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static PortalModifier getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PortalModifier();
        }
        return INSTANCE;
    }

    @Override
    public String getDisplayInfo() {
        if (this.godmode.getValue().booleanValue()) {
            return "Godmode";
        }
        return null;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && this.godmode.getValue().booleanValue() && event.getPacket() instanceof CPacketConfirmTeleport) {
            event.setCanceled(true);
        }
    }
}

