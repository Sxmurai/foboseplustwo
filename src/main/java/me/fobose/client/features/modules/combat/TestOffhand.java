package me.fobose.client.features.modules.combat;

import me.fobose.client.Fobose;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.util.EntityUtil;
import me.fobose.client.util.InventoryUtil;
import me.fobose.client.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class TestOffhand extends Module {
    public final Setting<MainType> defaultType = this.register(new Setting<>("Default", MainType.TOTEM));
    public final Setting<Boolean> forceTotem = this.register(new Setting<>("TotemEnsure", false));
    public final Setting<Float> totemHealthSwitch = this.register(new Setting<>("TotemHealthSwitch", 16.0f, 1.0f, 36.0f, (v) -> forceTotem.getValue()));
    public final Setting<Float> crystalPlayerRange = this.register(new Setting<>("CrystalPlayerRange", 6.0f, 2.0f, 12.0f, (v) -> defaultType.getValue() == MainType.CRYSTAL));
    public final Setting<Boolean> friendCrystalCheck = this.register(new Setting<>("FriendCrystalCheck", false, (v) -> defaultType.getValue() == MainType.CRYSTAL));
    public final Setting<Float> healthSwitch = this.register(new Setting<>("HealthSwitch", 16.0f, 1.0f, 36.0f));
    public final Setting<Integer> fallDistance = this.register(new Setting<>("FallDistance", 10, 1, 255));
    public final Setting<Boolean> offhandGapple = this.register(new Setting<>("OffhandGapple", false));
    public final Setting<Boolean> preferEnchanted = this.register(new Setting<>("PreferEnchanted", true, (v) -> offhandGapple.getValue() || defaultType.getValue() == MainType.GAPPLE));
    public final Setting<ShiftType> shiftType = this.register(new Setting<>("Shift", ShiftType.POT));
    public final Setting<Pot> potType = this.register(new Setting<>("Pot", Pot.STRENGTH, (v) -> shiftType.getValue() == ShiftType.POT));
    public final Setting<Boolean> potSword = this.register(new Setting<>("OnlySwordPot", false, (v) -> shiftType.getValue() == ShiftType.POT));
    public final Setting<Boolean> useEvenIfActive = this.register(new Setting<>("UseEventIfActive", false, (v) -> shiftType.getValue() == ShiftType.POT));
    public final Setting<Integer> delay = this.register(new Setting<>("Delay", 200, 0, 2500));
    public final Setting<Boolean> switchEvenInMainhand = this.register(new Setting<>("ForceSwitch", false));

    private final InventoryHandler inventoryHandler = new InventoryHandler();

    public TestOffhand() {
        super("Offhand", "Manages your offhand", Category.COMBAT, true, false, false);
    }

    @Override
    public void onDisable() {
        inventoryHandler.reset(true);
    }

    @Override
    public String getDisplayInfo() {
        return null; // @todo
    }

    @Override
    public void onUpdate() {
        if (!Module.fullNullCheck()) {
            inventoryHandler.run(delay.getValue().longValue());

            if (EntityUtil.getHealth(mc.player) <= totemHealthSwitch.getValue() || mc.player.fallDistance > fallDistance.getValue()) {
                switchTo(Items.TOTEM_OF_UNDYING);
                return;
            }

            boolean decided = false;

            if (shiftType.getValue() != ShiftType.NONE && mc.player.isSneaking()) {
                if (shiftType.getValue() != ShiftType.POT) {
                    switchTo(shiftType.getValue().item);
                    return;
                } else {
                    if (!useEvenIfActive.getValue() && mc.player.isPotionActive(potType.getValue().type)) {
                        return;
                    }

                    if (potSword.getValue() && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword)) {
                        return;
                    }

                    for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
                        ItemStack stack = entry.getValue();
                        if (stack.isEmpty() || !(stack.getItem() instanceof ItemPotion)) {
                            continue;
                        }

                        List<PotionEffect> effects = PotionUtils.getEffectsFromStack(stack);
                        if (effects.isEmpty()) {
                            continue;
                        }

                        if (effects.stream().anyMatch((effect) -> effect.getPotion().equals(potType.getValue().type))) {
                            switchTo(stack.getItem());
                            return;
                        }
                    }
                }
            }

            if (offhandGapple.getValue() && Mouse.isButtonDown(1)) {
                switchTo(Items.GOLDEN_APPLE);
                decided = true;
            }

            if (defaultType.getValue().item == Items.END_CRYSTAL && !decided) {
                List<EntityPlayer> possibleThreats = mc.world.playerEntities.stream()
                        .filter((player) -> mc.player.getDistance(player) > crystalPlayerRange.getValue() || friendCrystalCheck.getValue() && !Fobose.friendManager.isFriend(player))
                        .collect(Collectors.toList());

                if (!possibleThreats.isEmpty()) {
                    switchTo(defaultType.getValue().item);
                    decided = true;
                }
            } else if (!decided) {
                switchTo(defaultType.getValue().item);
            }
        }
    }

    private void switchTo(Item item) {
        if (!switchEvenInMainhand.getValue() && mc.player.getHeldItemMainhand().getItem() == item) {
            return;
        }

        if (mc.player.getHeldItemOffhand().getItem() == item) {
            return;
        }

        int slot = findItem(item);
        if (slot == -1 || slot == 45) {
            return;
        }

        InventoryTaskGroup taskGroup = new InventoryTaskGroup();

        if (!mc.player.getHeldItemOffhand().isEmpty()) {
            taskGroup.add(new InventoryUtil.Task(45));
        }

        taskGroup.add(new InventoryUtil.Task(slot));
        taskGroup.add(new InventoryUtil.Task(45));

        inventoryHandler.addGroup(taskGroup);
    }

    private int findItem(Item item) {
        int slot = -1;

        if (item == Items.GOLDEN_APPLE && preferEnchanted.getValue()) {
            for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
                ItemStack stack = entry.getValue();
                if (stack.isEmpty() || stack.getItem() != Items.GOLDEN_APPLE) {
                    continue;
                }

                if (PotionUtils.getEffectsFromStack(stack).stream().anyMatch((effect) -> effect.getPotion().equals(MobEffects.FIRE_RESISTANCE))) {
                    slot = entry.getKey();
                    break;
                }
            }
        }

        return slot == -1 ? InventoryUtil.findItemInventorySlot(item, true) : slot;
    }

    public enum MainType {
        TOTEM(Items.TOTEM_OF_UNDYING),
        GAPPLE(Items.GOLDEN_APPLE),
        CRYSTAL(Items.END_CRYSTAL);

        public Item item;
        MainType(Item item) {
            this.item = item;
        }
    }

    public enum ShiftType {
        NONE(null),
        TOTEM(Items.TOTEM_OF_UNDYING),
        GAPPLE(Items.GOLDEN_APPLE),
        CRYSTAL(Items.END_CRYSTAL),
        POT(null);

        public Item item;
        ShiftType(Item item) {
            this.item = item;
        }
    }

    public enum Pot {
        STRENGTH(MobEffects.STRENGTH),
        SPEED(MobEffects.SPEED),
        JUMP_BOOST(MobEffects.JUMP_BOOST),
        REGEN(MobEffects.REGENERATION);

        public Potion type;
        Pot(Potion type) {
            this.type = type;
        }
    }

    private static class InventoryHandler {
        private final ConcurrentLinkedDeque<InventoryTaskGroup> taskGroups = new ConcurrentLinkedDeque<>();
        private final Timer timer = new Timer();
        private boolean ohFuckOhShit = false;

        public InventoryHandler() {
            this.reset(true);
        }

        public void stop(boolean ohFuckOhShit) {
            this.ohFuckOhShit = ohFuckOhShit;
        }

        public void run(long delay) {
            if (ohFuckOhShit) {
                return;
            }

            if (timer.passedMs(delay)) {
                reset(false);

                InventoryTaskGroup group = taskGroups.poll();
                if (group == null || group.getTasks().isEmpty()) { // no tasks left
                    return;
                }

                group.getTasks().forEach(this::run);
            }
        }

        private void run(InventoryUtil.Task task) {
            if (ohFuckOhShit) {
                return;
            }

            task.run();
        }

        public void reset(boolean clear) {
            timer.reset();
            if (clear && !taskGroups.isEmpty()) {
                taskGroups.clear();
            }
            ohFuckOhShit = false;
        }

        public void addGroup(InventoryTaskGroup group) {
            taskGroups.add(group);
        }
    }

    private static class InventoryTaskGroup {
        private final ArrayList<InventoryUtil.Task> tasks = new ArrayList<>();

        public void add(InventoryUtil.Task task) {
            this.tasks.add(task);
        }

        public ArrayList<InventoryUtil.Task> getTasks() {
            return tasks;
        }
    }
}

