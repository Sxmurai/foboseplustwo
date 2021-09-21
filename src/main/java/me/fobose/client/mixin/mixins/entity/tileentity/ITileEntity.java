package me.fobose.client.mixin.mixins.entity.tileentity;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TileEntity.class)
public interface ITileEntity {
    @Accessor("blockType")
    void setBlockType(Block blockType);
}
