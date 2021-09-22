package me.fobose.client.features.modules.combat;

import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import me.fobose.client.util.Timer;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class FoboseAutoCrystal extends Module {
    public final Setting<Menu> menu = this.register(new Setting<>("Menu", Menu.TARGET));

    // target shit
    public final Setting<Target> priority = this.register(new Setting<>("Priority", Target.CLOSEST, (v) -> menu.getValue() == Menu.TARGET));
    public final Setting<Float> targetRange = this.register(new Setting<>("TargetRange", 10.0f, 2.0f, 20.0f, (v) -> menu.getValue() == Menu.TARGET));
    public final Setting<Boolean> antiNaked = this.register(new Setting<>("AntiNaked", true, (v) -> menu.getValue() == Menu.TARGET));
    public final Setting<AntiFriendPop> antiFriendPop = this.register(new Setting<>("AntiFriendPop", AntiFriendPop.NONE, (v) -> menu.getValue() == Menu.TARGET));

    // place shit
    public final Setting<Boolean> place = this.register(new Setting<>("Place", true, (v) -> menu.getValue() == Menu.PLACE));
    public final Setting<Float> placeRange = this.register(new Setting<>("PlaceRange", 4.9f, 1.0f, 8.0f, (v) -> menu.getValue() == Menu.PLACE && place.getValue()));
    public final Setting<Float> placeDelay = this.register(new Setting<>("PlaceDelay", 65.0f, 0.0f, 2500.0f, (v) -> menu.getValue() == Menu.PLACE && place.getValue()));
    public final Setting<Float> minPlaceDamage = this.register(new Setting<>("MinPlaceDamage", 6.0f, 0.1f, 36.0f, (v) -> menu.getValue() == Menu.PLACE && place.getValue()));
    public final Setting<Float> maxSelfPlaceDamage = this.register(new Setting<>("MaxSelfPlaceDamage", 10.0f, 0.1f, 36.0f, (v) -> menu.getValue() == Menu.PLACE && place.getValue()));
    public final Setting<Integer> placeAmount = this.register(new Setting<>("PlaceAmount", 3, 1, 5, (v) -> menu.getValue() == Menu.PLACE && place.getValue()));
    public final Setting<Boolean> oneDotThirteen = this.register(new Setting<>("1.13", false, (v) -> menu.getValue() == Menu.PLACE && place.getValue()));
    public final Setting<Boolean> placeRotate = this.register(new Setting<>("PlaceRotate", true, (v) -> menu.getValue() == Menu.PLACE && place.getValue()));
    public final Setting<Boolean> facePlace = this.register(new Setting<>("FacePlace", true, (v) -> menu.getValue() == Menu.PLACE && place.getValue()));
    public final Setting<Float> facePlaceMinHealth = this.register(new Setting<>("FacePlaceMinHealth", 16.0f, 1.0f, 36.0f, (v) -> menu.getValue() == Menu.PLACE && place.getValue() && facePlace.getValue()));
    public final Setting<Float> facePlaceMinDamage = this.register(new Setting<>("FacePlaceMinDamage", 2.3f, 1.0f, 10.0f, (v) -> menu.getValue() == Menu.PLACE && place.getValue() && facePlace.getValue()));
    public final Setting<Boolean> packetPlace = this.register(new Setting<>("PacketPlace", true, (v) -> menu.getValue() == Menu.PLACE && place.getValue()));
    public final Setting<Boolean> placeSwing = this.register(new Setting<>("PlaceSwing", true, (v) -> menu.getValue() == Menu.PLACE && place.getValue()));
    public final Setting<Boolean> predict = this.register(new Setting<>("Predict", false, (v) -> menu.getValue() == Menu.PLACE && place.getValue()));
    public final Setting<Float> predictDelay = this.register(new Setting<>("PredictDelay", 12.0f, 0.0f, 500.0f, (v) -> menu.getValue() == Menu.PLACE && place.getValue() && predict.getValue()));
    public final Setting<Boolean> antiSurround = this.register(new Setting<>("AntiSurround", false, (v) -> menu.getValue() == Menu.PLACE && place.getValue()));

    // break
    public final Setting<Boolean> destroy = this.register(new Setting<>("Break", true, (v) -> menu.getValue() == Menu.BREAK));
    public final Setting<Float> breakRange = this.register(new Setting<>("PlaceRange", 4.9f, 1.0f, 8.0f, (v) -> menu.getValue() == Menu.BREAK && destroy.getValue()));
    public final Setting<Float> breakDelay = this.register(new Setting<>("PlaceDelay", 65.0f, 0.0f, 2500.0f, (v) -> menu.getValue() == Menu.BREAK && destroy.getValue()));
    public final Setting<BreakType> breakType = this.register(new Setting<>("BreakType", BreakType.CALC, (v) -> menu.getValue() == Menu.BREAK && destroy.getValue()));
    public final Setting<Float> minBreakDamage = this.register(new Setting<>("MinPlaceDamage", 6.0f, 0.1f, 36.0f, (v) -> menu.getValue() == Menu.BREAK && destroy.getValue()));
    public final Setting<Float> maxSelfBreakDamage = this.register(new Setting<>("MaxSelfBreakDamage", 10.0f, 0.1f, 36.0f, (v) -> menu.getValue() == Menu.BREAK && destroy.getValue()));
    public final Setting<Boolean> breakRotate = this.register(new Setting<>("BreakRotate", true, (v) -> menu.getValue() == Menu.BREAK && destroy.getValue()));
    public final Setting<Boolean> packetBreak = this.register(new Setting<>("PacketBreak", true, (v) -> menu.getValue() == Menu.BREAK && destroy.getValue()));
    public final Setting<Swing> breakSwing = this.register(new Setting<>("BreakSwing", Swing.NORMAL, (v) -> menu.getValue() == Menu.BREAK && destroy.getValue()));

    // render
    // @todo

    // misc
    public final Setting<Float> cooldown = this.register(new Setting<>("Cooldown", 350.0f, 0.0f, 2500.0f, (v) -> menu.getValue() == Menu.MISC));
    public final Setting<Boolean> autoSwitch = this.register(new Setting<>("AutoSwitch", true, (v) -> menu.getValue() == Menu.MISC));
    public final Setting<Boolean> forceWaitAfterSwitch = this.register(new Setting<>("ForceWaitAfterSwitch", false, (v) -> menu.getValue() == Menu.MISC));
    public final Setting<Float> forceWaitSwitchDelay = this.register(new Setting<>("ForceWaitSwitchDelay", 25.0f, 1.0f, 1000.0f, (v) -> menu.getValue() == Menu.MISC && forceWaitAfterSwitch.getValue()));
    public final Setting<Raytrace> raytrace = this.register(new Setting<>("Raytrace", Raytrace.NONE, (v) -> menu.getValue() == Menu.MISC));
    public final Setting<Boolean> inhibit = this.register(new Setting<>("Inhibit", false, (v) -> menu.getValue() == Menu.MISC));

    private EntityPlayer target = null;
    private BlockPos currentPos = null;
    private float currentDamage = 0.0f;
    private final Queue<EntityEnderCrystal> queuedBreakCrystals = new ConcurrentLinkedDeque<>();
    private final ArrayList<BlockPos> placePositions = new ArrayList<>();

    private EnumHand crystalHand = EnumHand.MAIN_HAND;
    private boolean shouldWaitForSwitchDelay = false;
    private int swings = 0;

    private final Timer placeTimer = new Timer();
    private final Timer predictTimer = new Timer();
    private final Timer breakTimer = new Timer();
    private final Timer cooldownTimer = new Timer();
    private final Timer itemSwitchTimer = new Timer();

    public FoboseAutoCrystal() {
        super("FoboseAutoCrystal", "i hate my life", Category.COMBAT, true, false, false);
    }

    @Override
    public void onDisable() {
        target = null;
        currentPos = null;
        currentDamage = 0.0f;
        queuedBreakCrystals.clear();
        placePositions.clear();
        crystalHand = EnumHand.MAIN_HAND;
        shouldWaitForSwitchDelay = false;
        swings = 0;
        placeTimer.reset();
        breakTimer.reset();
        cooldownTimer.reset();
        itemSwitchTimer.reset();
    }

    @Override
    public String getDisplayInfo() {
        if (target != null) {
            return target.getName();
        }

        return null;
    }

    public enum Menu {
        TARGET,
        PLACE,
        BREAK,
        RENDER,
        MISC
    }

    public enum Target {
        CLOSEST,
        DAMAGE,
    }

    public enum AntiFriendPop {
        NONE,
        PLACE,
        BREAK,
        BOTH
    }

    public enum Swing {
        NORMAL,
        MAINHAND,
        OFFHAND
    }

    public enum BreakType {
        CALC,
        OWN,
        ALWAYS
    }

    public enum Raytrace {
        NONE,
        PLACE,
        BREAK,
        BOTH
    }
}
