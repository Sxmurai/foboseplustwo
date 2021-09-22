package me.fobose.client.features.command.commands;

import me.fobose.client.Fobose;
import me.fobose.client.features.command.Command;

public class UnloadCommand
extends Command {
    public UnloadCommand() {
        super("unload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        Fobose.unload(true);
    }
}

