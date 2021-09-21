
package me.fobose.client.mixin.mixins.world.block;

import me.fobose.client.features.modules.render.XRay;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Block.class})
public abstract class MixinBlock {
    @Shadow
    @Deprecated
    public abstract float getBlockHardness(IBlockState var1, World var2, BlockPos var3);

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    public void shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> info) {
        if (XRay.getInstance().isOn()) {
            info.setReturnValue(XRay.getInstance().shouldRender(blockState.getBlock()));
        }
    }

//    @Inject(method={"addCollisionBoxToList(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;Z)V"}, at={@At(value="HEAD")}, cancellable=true)
//    public void addCollisionBoxToListHook(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState, CallbackInfo info) {
//        if (entityIn != null && Util.mc.player != null && (entityIn.equals((Object)Util.mc.player) || Util.mc.player.getRidingEntity() != null && entityIn.equals((Object)Util.mc.player.getRidingEntity())) || Phase.getInstance().isOn() && Phase.getInstance().mode.getValue() == Phase.Mode.PACKETFLY && Phase.getInstance().type.getValue() == Phase.PacketFlyMode.SETBACK && Phase.getInstance().boundingBox.getValue()) {
//            info.cancel();
//        }
//        try {
//            // @todo wtf freecam
//            if (Freecam.INSTANCE.isOff() && Jesus.getInstance().isOn() && Jesus.getInstance().mode.getValue() == Jesus.Mode.TRAMPOLINE && Util.mc.player != null && state != null && state.getBlock() instanceof BlockLiquid && !(entityIn instanceof EntityBoat) && !Util.mc.player.isSneaking() && Util.mc.player.fallDistance < 3.0f && !EntityUtil.isAboveLiquid((Entity)Util.mc.player) && EntityUtil.checkForLiquid((Entity)Util.mc.player, false) || EntityUtil.checkForLiquid((Entity)Util.mc.player, false) && Util.mc.player.getRidingEntity() != null && Util.mc.player.getRidingEntity().fallDistance < 3.0f && EntityUtil.isAboveBlock((Entity)Util.mc.player, pos)) {
//                AxisAlignedBB offset = Jesus.offset.offset(pos);
//                if (entityBox.intersects(offset)) {
//                    collidingBoxes.add(offset);
//                }
//                info.cancel();
//            }
//        }
//        catch (Exception exception) {
//            // empty catch block
//        }
//    }

//    @Inject(method={"isFullCube"}, at={@At(value="HEAD")}, cancellable=true)
//    public void isFullCubeHook(IBlockState blockState, CallbackInfoReturnable<Boolean> info) {
//        try {
//            if (XRay.getInstance().isOn()) {
//                info.setReturnValue(XRay.getInstance().shouldRender((Block)Block.class.cast(this)));
//                info.cancel();
//            }
//        }
//        catch (Exception exception) {
//            // empty catch block
//        }
//    }
}

