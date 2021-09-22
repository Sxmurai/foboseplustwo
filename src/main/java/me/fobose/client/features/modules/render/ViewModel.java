package me.fobose.client.features.modules.render;

import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.Vec3d;

public class ViewModel extends Module {
    public static ViewModel INSTANCE;

    public final Setting<Float> leftX = this.register(new Setting<>("LeftX", 0.0f, -2.0f, 2.0f));
    public final Setting<Float> leftY = this.register(new Setting<>("LeftY", 0.0f, -2.0f, 2.0f));
    public final Setting<Float> leftZ = this.register(new Setting<>("LeftZ", 0.0f, -2.0f, 2.0f));
    public final Setting<Float> rightX = this.register(new Setting<>("RightX", 0.0f, -2.0f, 2.0f));
    public final Setting<Float> rightY = this.register(new Setting<>("RightY", 0.0f, -2.0f, 2.0f));
    public final Setting<Float> rightZ = this.register(new Setting<>("RightZ", 0.0f, -2.0f, 2.0f));

    public ViewModel() {
        super("ViewModel", "does view model shit", Category.RENDER, false, false, false);
        INSTANCE = this;
    }

    public static Vec3d getOffsets(EnumHandSide hand) {
        return new Vec3d(
                hand == EnumHandSide.LEFT ? INSTANCE.leftX.getValue() : INSTANCE.rightX.getValue(),
                hand == EnumHandSide.LEFT ? INSTANCE.leftY.getValue() : INSTANCE.rightY.getValue(),
                hand == EnumHandSide.LEFT ? INSTANCE.leftZ.getValue() : INSTANCE.rightZ.getValue()
        );
    }
}
