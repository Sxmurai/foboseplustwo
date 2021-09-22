package me.fobose.client.features.command.commands;

import me.fobose.client.Fobose;
import me.fobose.client.features.command.Command;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("commands");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("You can use following commands: ");
        for (Command command : Fobose.commandManager.getCommands()) {
            HelpCommand.sendMessage(Fobose.commandManager.getPrefix() + command.getName());
        }
    }
}

