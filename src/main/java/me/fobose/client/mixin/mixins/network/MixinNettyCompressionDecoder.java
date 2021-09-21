package me.fobose.client.mixin.mixins.network;

import net.minecraft.network.NettyCompressionDecoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(NettyCompressionDecoder.class)
public class MixinNettyCompressionDecoder {
    // @todo this might be useful
//    @ModifyConstant(method = "decode", constant = @Constant(intValue = 0x200000))
//    private int decodeHook(int n) {
//        if (Bypass.getInstance().isOn() && Bypass.getInstance().packets.getValue() && Bypass.getInstance().noLimit.getValue()) {
//            return Integer.MAX_VALUE;
//        }
//
//        return n;
//    }
}

