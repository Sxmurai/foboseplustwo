package me.fobose.client.mixin.mixins.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Block.class)
public interface IBlock {
    @Accessor("material")
    Material getMaterial();
}
