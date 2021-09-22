package me.fobose.client.features.command.commands;

import me.fobose.client.Fobose;
import me.fobose.client.features.command.Command;

public class ReloadCommand
extends Command {
    public ReloadCommand() {
        super("reload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        Fobose.reload();
    }
}

