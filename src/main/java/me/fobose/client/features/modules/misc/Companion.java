
package me.fobose.client.features.modules.misc;

import com.mojang.text2speech.Narrator;
import me.fobose.client.event.events.DeathEvent;
import me.fobose.client.event.events.TotemPopEvent;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Companion
extends Module {
    private Narrator narrator = Narrator.getNarrator();
    public Setting<String> totemPopMessage = this.register(new Setting<String>("PopMessage", "<player> watch out you're popping!"));
    public Setting<String> deathMessages = this.register(new Setting<String>("DeathMessage", "<player> you retard you just fucking died!"));

    public Companion() {
        super("Companion", "The best module", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        this.narrator.say("Hello and welcome to fobose plus two");
    }

    @Override
    public void onDisable() {
        this.narrator.clear();
    }

    @SubscribeEvent
    public void onTotemPop(TotemPopEvent event) {
        if (event.getEntity() == Companion.mc.player) {
            this.narrator.say(this.totemPopMessage.getValue().replaceAll("<player>", Companion.mc.player.getName()));
        }
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (event.player == Companion.mc.player) {
            this.narrator.say(this.deathMessages.getValue().replaceAll("<player>", Companion.mc.player.getName()));
        }
    }
}

