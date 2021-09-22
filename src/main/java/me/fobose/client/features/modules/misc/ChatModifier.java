package me.fobose.client.features.modules.misc;

import me.fobose.client.event.events.PacketEvent;
import me.fobose.client.features.command.Command;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.mixin.mixins.network.packets.c2s.ICPacketChatMessage;
import me.fobose.client.util.TextUtil;
import me.fobose.client.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatModifier extends Module {
    public Setting<Suffix> suffix = this.register(new Setting<Suffix>("Suffix", Suffix.NONE, "Your Suffix."));
    public Setting<Boolean> clean = this.register(new Setting<Boolean>("CleanChat", Boolean.valueOf(false), "Cleans your chat"));
    public Setting<Boolean> infinite = this.register(new Setting<Boolean>("Infinite", Boolean.valueOf(false), "Makes your chat infinite."));
    public Setting<Boolean> autoQMain = this.register(new Setting<Boolean>("AutoQMain", Boolean.valueOf(false), "Spams AutoQMain"));
    public Setting<Boolean> qNotification = this.register(new Setting<Object>("QNotification", Boolean.valueOf(false), v -> this.autoQMain.getValue()));
    public Setting<Integer> qDelay = this.register(new Setting<Object>("QDelay", Integer.valueOf(9), Integer.valueOf(1), Integer.valueOf(90), v -> this.autoQMain.getValue()));
    public Setting<Boolean> shrug = this.register(new Setting<Boolean>("Shrug", false));
    public Setting<Boolean> disability = this.register(new Setting<Boolean>("Disability", false));
    private final Timer timer = new Timer();
    private static ChatModifier INSTANCE = new ChatModifier();

    public ChatModifier() {
        super("Chat", "Modifies your chat", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static ChatModifier getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatModifier();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (this.shrug.getValue()) {
            ChatModifier.mc.player.sendChatMessage(TextUtil.shrug);
            this.shrug.setValue(false);
        }
        if (this.autoQMain.getValue()) {
            if (!this.shouldSendMessage(ChatModifier.mc.player)) {
                return;
            }
            if (this.qNotification.getValue()) {
                Command.sendMessage("<AutoQueueMain> Sending message: /queue main");
            }
            ChatModifier.mc.player.sendChatMessage("/queue main");
            this.timer.reset();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = event.getPacket();
            String content = packet.getMessage();
            if (content.startsWith("/")) {
                return;
            }

            if (this.suffix.getValue() != Suffix.NONE) {
                content += " \u23d0 " + suffix.getValue().unicode;
            }

            if (content.length() >= 256) {
                content = content.substring(0, 256);
            }

            ((ICPacketChatMessage) packet).setMessage(content);
        }
    }

    private boolean shouldSendMessage(EntityPlayer player) {
        if (player.dimension != 1) {
            return false;
        }

        if (!this.timer.passedS(this.qDelay.getValue())) {
            return false;
        }

        return player.getPosition().equals(new Vec3i(0, 240, 0));
    }

    public enum Suffix {
        NONE(null),
        FOBOSEPLUSTWO("\uA730\u1D0F\u0299\u1d0f\uA731\u1D07\u1D18\u029F\u1D1C\ua731\u1D1B\u1D21\u1d0f"),
        PHOBOS("\u1d18\u029c\u1d0f\u0299\u1d0f\ua731"),
        EARTH("3\u1d00\u0280\u1d1b\u029c\u029c4\u1d04\u1d0b"),
        AESTHETICIAL("\u1d00\u1d07\ua731\u1d1b\u029c\u1d07\u1d1b\u1d04\u026a\u1d00\u029f");

        public String unicode;
        Suffix(String unicode) {
            this.unicode = unicode;
        }
    }
}

