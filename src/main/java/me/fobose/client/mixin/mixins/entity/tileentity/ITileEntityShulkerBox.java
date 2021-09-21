package me.fobose.client.mixin.mixins.entity.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TileEntityShulkerBox.class)
public interface ITileEntityShulkerBox {
    @Accessor("items")
    NonNullList<ItemStack> getItems();
}
