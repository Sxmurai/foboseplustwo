package me.fobose.client.features.command.commands;

import me.fobose.client.features.command.Command;

public class ReloadSoundCommand extends Command {
    public ReloadSoundCommand() {
        super("sound", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
//        try {
////            SoundManager sndManager = (SoundManager)ObfuscationReflectionHelper.getPrivateValue(SoundHandler.class, (Object)mc.getSoundHandler(), (String[])new String[]{"sndManager", "sndManager"});
////            sndManager.reloadSoundSystem();
//            ReloadSoundCommand.sendMessage("\u00a7aReloaded Sound System.");
//        }
//        catch (Exception e) {
//            System.out.println("Could not restart sound manager: " + e.toString());
//            e.printStackTrace();
//            ReloadSoundCommand.sendMessage("\u00a7cCouldnt Reload Sound System!");
//        }
    }
}

