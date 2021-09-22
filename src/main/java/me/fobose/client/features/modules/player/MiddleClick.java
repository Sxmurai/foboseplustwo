package me.fobose.client.features.modules.player;

import me.fobose.client.Fobose;
import me.fobose.client.features.command.Command;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.manager.FriendManager;
import me.fobose.client.util.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

public class MiddleClick extends Module {
    public final Setting<Boolean> pearl = this.register(new Setting<>("Pearl", false));
    public final Setting<Boolean> friend = this.register(new Setting<>("Friend", false));
    public final Setting<Boolean> unfriend = this.register(new Setting<>("Unfriend", false, (v) -> friend.getValue()));

    public MiddleClick() {
        super("MiddleClick", "does things upon middle clicking", Category.PLAYER, true, false, false);
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (!Mouse.getEventButtonState() && Mouse.getEventButton() == 2) {
            if (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY && mc.objectMouseOver.entityHit instanceof EntityPlayer) {
                if (friend.getValue()) {
                    EntityPlayer player = (EntityPlayer) mc.objectMouseOver.entityHit;

                    if (!Fobose.friendManager.isFriend(player)) {
                        Fobose.friendManager.addFriend(new FriendManager.Friend(player.getName(), player.getUniqueID()));
                        Command.sendMessage("Added " + player.getName() + " as a friend.");
                    } else if (Fobose.friendManager.isFriend(player) && unfriend.getValue()) {
                        Fobose.friendManager.removeFriend(player.getName());
                        Command.sendMessage("Removed " + player.getName() + " from your friend list.");
                    }
                }

                return;
            }

            if (!pearl.getValue()) {
                return;
            }

            int slot = InventoryUtil.findItemInventorySlot(Items.ENDER_PEARL, true);
            if (slot == -1) {
                return;
            }

            int oldSlot = mc.player.inventory.currentItem;
            if (slot != 45) {
                InventoryUtil.switchToHotbarSlot(slot, false);
            }

            mc.playerController.processRightClick(mc.player, mc.world, slot == 45 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);

            if (slot != 45) {
                mc.player.inventory.currentItem = oldSlot;
            }
        }
    }
}
