package me.fobose.client;

import me.fobose.client.manager.ColorManager;
import me.fobose.client.manager.CommandManager;
import me.fobose.client.manager.ConfigManager;
import me.fobose.client.manager.EventManager;
import me.fobose.client.manager.FileManager;
import me.fobose.client.manager.FriendManager;
import me.fobose.client.manager.HoleManager;
import me.fobose.client.manager.InventoryManager;
import me.fobose.client.manager.ModuleManager;
import me.fobose.client.manager.NotificationManager;
import me.fobose.client.manager.PositionManager;
import me.fobose.client.manager.PotionManager;
import me.fobose.client.manager.ReloadManager;
import me.fobose.client.manager.RotationManager;
import me.fobose.client.manager.SafetyManager;
import me.fobose.client.manager.ServerManager;
import me.fobose.client.manager.SpeedManager;
import me.fobose.client.manager.TextManager;
import me.fobose.client.manager.TotemPopManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid = Fobose.MODID, name = Fobose.MODNAME, version = Fobose.MODVER)
public class Fobose {
    public static final String MODID = "fobose+2";
    public static final String MODNAME = "Fobose+2";
    public static final String MODVER = "1.9.2";

    public static final Logger LOGGER = LogManager.getLogger("Fobose+2");

    public static ModuleManager moduleManager;
    public static SpeedManager speedManager;
    public static PositionManager positionManager;
    public static RotationManager rotationManager;
    public static CommandManager commandManager;
    public static EventManager eventManager;
    public static ConfigManager configManager;
    public static FileManager fileManager;
    public static FriendManager friendManager;
    public static TextManager textManager;
    public static ColorManager colorManager;
    public static ServerManager serverManager;
    public static PotionManager potionManager;
    public static InventoryManager inventoryManager;
    public static ReloadManager reloadManager;
    public static TotemPopManager totemPopManager;
    public static HoleManager holeManager;
    public static NotificationManager notificationManager;
    public static SafetyManager safetyManager;

    private static boolean unloaded;

    @Mod.Instance
    public static Fobose INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("get out of my logs goddamn, its just a client");
        LOGGER.info("i want the domain cum-in.me buy it for me please");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Display.setTitle(MODNAME + " v" + MODVER);
        Fobose.load();
    }

    public static void load() {
        LOGGER.info("Loading {} v{}", MODNAME, MODVER);
        unloaded = false;

        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }

        LOGGER.info("Creating instances of managers...");

        totemPopManager = new TotemPopManager();
        serverManager = new ServerManager();
        colorManager = new ColorManager();
        textManager = new TextManager();
        moduleManager = new ModuleManager();
        speedManager = new SpeedManager();
        rotationManager = new RotationManager();
        positionManager = new PositionManager();
        commandManager = new CommandManager();
        eventManager = new EventManager();
        configManager = new ConfigManager();
        fileManager = new FileManager();
        friendManager = new FriendManager();
        potionManager = new PotionManager();
        inventoryManager = new InventoryManager();
        holeManager = new HoleManager();
        notificationManager = new NotificationManager();
        safetyManager = new SafetyManager();

        LOGGER.info("Initializing the managers...");

        moduleManager.init();
        configManager.init();
        eventManager.init();
        textManager.init(true);
        moduleManager.onLoad();
        totemPopManager.init();

        LOGGER.info("{} initialized!", MODNAME);
    }

    public static void unload(boolean unload) {
        LOGGER.info("Unloading {} v{}", MODNAME, MODVER);

        if (unload) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : ".");
        }

        Fobose.onUnload();
        eventManager = null;
        holeManager = null;
        moduleManager = null;
        totemPopManager = null;
        serverManager = null;
        colorManager = null;
        textManager = null;
        speedManager = null;
        rotationManager = null;
        positionManager = null;
        commandManager = null;
        configManager = null;
        fileManager = null;
        friendManager = null;
        potionManager = null;
        inventoryManager = null;
        notificationManager = null;
        safetyManager = null;

        LOGGER.info("{} unloaded!", MODNAME);
    }

    public static void reload() {
        Fobose.unload(false);
        Fobose.load();
    }

    public static void onUnload() {
        if (!unloaded) {
            eventManager.onUnload();
            moduleManager.onUnload();
            configManager.saveConfig(Fobose.configManager.config.replaceFirst("phobos/", ""));
            moduleManager.onUnloadPost();
            unloaded = true;
        }
    }

    static {
        unloaded = false;
    }
}

