package me.fobose.client.mixin.mixins.network.packets.client;

import me.fobose.client.features.modules.client.ServerModule;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(C00Handshake.class)
public class MixinC00Handshake {
    @Redirect(method = "writePacketData", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketBuffer;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketBuffer;"))
    public PacketBuffer writePacketDataHook(PacketBuffer packetBuffer, String string) {
        return packetBuffer.writeString(ServerModule.getInstance().noFML.getValue() ?
                string.substring(0, string.length() - "\u0000FML\u0000".length()) :
                string
        );
    }
}

