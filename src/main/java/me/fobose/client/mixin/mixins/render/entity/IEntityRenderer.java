package me.fobose.client.mixin.mixins.render.entity;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityRenderer.class)
public interface IEntityRenderer {
    @Invoker("orientCamera")
    void runOrientCamera(float partialTicks);
}
