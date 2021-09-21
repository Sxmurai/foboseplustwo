package me.fobose.client.mixin;

import java.util.Map;
import me.fobose.client.Fobose;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

@IFMLLoadingPlugin.Name("PhobosMixinLoader")
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class FoboseMixinLoader implements IFMLLoadingPlugin {
    public FoboseMixinLoader() {
        MixinBootstrap.init();
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        Mixins.addConfiguration("mixins.foboseplustwo.json");
        Mixins.addConfiguration("mixins.baritone.json");
        Fobose.LOGGER.info("Fobose+2 mixins and baritone mixins initialized");
    }

    public String[] getASMTransformerClass() {
        return new String[0];
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(Map<String, Object> data) { }

    public String getAccessTransformerClass() {
        return null;
    }
}

