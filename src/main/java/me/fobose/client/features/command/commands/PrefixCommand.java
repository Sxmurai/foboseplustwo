package me.fobose.client.features.command.commands;

import me.fobose.client.Fobose;
import me.fobose.client.features.command.Command;
import me.fobose.client.features.modules.client.ClickGui;

public class PrefixCommand extends Command {
    public PrefixCommand() {
        super("prefix", new String[] {"<new prefix>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage("\u00a7cSpecify a new prefix.");
            return;
        }

        Fobose.moduleManager.getModuleByClass(ClickGui.class).prefix.setValue(commands[0]);
        Command.sendMessage("Prefix set to \u00a7a" + Fobose.commandManager.getPrefix());
    }
}

