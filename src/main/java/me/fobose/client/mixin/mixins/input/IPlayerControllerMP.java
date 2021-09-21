package me.fobose.client.mixin.mixins.input;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerControllerMP.class)
public interface IPlayerControllerMP {
    @Invoker("syncCurrentPlayItem")
    void runSyncCurrentPlayItem();

    @Accessor("blockHitDelay")
    void setHitBlockDelay(int hitBlockDelay);

    @Accessor("curBlockDamageMP")
    float getcurBlockDamageMP();

    @Accessor("curBlockDamageMP")
    void setCurBlockDamageMP(float curBlockDamageMP);

    @Accessor("isHittingBlock")
    void setIsHittingBlock(boolean isHittingBlock);
}
