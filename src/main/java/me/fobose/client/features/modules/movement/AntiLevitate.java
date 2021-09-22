package me.fobose.client.features.modules.movement;

import me.fobose.client.features.modules.Module;
import net.minecraft.init.MobEffects;

public class AntiLevitate extends Module {
    public AntiLevitate() {
        super("AntiLevitate", "Removes shulker levitation", Module.Category.MOVEMENT, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (AntiLevitate.mc.player.isPotionActive(MobEffects.LEVITATION)) {
            AntiLevitate.mc.player.removeActivePotionEffect(MobEffects.LEVITATION);
        }
    }
}

