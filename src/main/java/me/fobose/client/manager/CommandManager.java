package me.fobose.client.manager;

import java.util.ArrayList;
import java.util.LinkedList;
import me.fobose.client.features.Feature;
import me.fobose.client.features.command.Command;
import me.fobose.client.features.command.commands.BindCommand;
import me.fobose.client.features.command.commands.BookCommand;
import me.fobose.client.features.command.commands.ConfigCommand;
import me.fobose.client.features.command.commands.CrashCommand;
import me.fobose.client.features.command.commands.FriendCommand;
import me.fobose.client.features.command.commands.HelpCommand;
import me.fobose.client.features.command.commands.HistoryCommand;
import me.fobose.client.features.command.commands.ModuleCommand;
import me.fobose.client.features.command.commands.PeekCommand;
import me.fobose.client.features.command.commands.PrefixCommand;
import me.fobose.client.features.command.commands.ReloadCommand;
import me.fobose.client.features.command.commands.ReloadSoundCommand;
import me.fobose.client.features.command.commands.UnloadCommand;
import me.fobose.client.features.command.commands.XrayCommand;

public class CommandManager extends Feature {
    private final ArrayList<Command> commands = new ArrayList<>();
    private String clientMessage = "<aesthetical.xyz>";
    private String prefix = ".";

    public CommandManager() {
        super("Command");

        this.commands.add(new BindCommand());
        this.commands.add(new ModuleCommand());
        this.commands.add(new PrefixCommand());
        this.commands.add(new ConfigCommand());
        this.commands.add(new FriendCommand());
        this.commands.add(new HelpCommand());
        this.commands.add(new ReloadCommand());
        this.commands.add(new UnloadCommand());
        this.commands.add(new ReloadSoundCommand());
        this.commands.add(new PeekCommand());
        this.commands.add(new XrayCommand());
        this.commands.add(new BookCommand());
        this.commands.add(new CrashCommand());
        this.commands.add(new HistoryCommand());
    }

    public void executeCommand(String command) {
        String[] parts = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String name = parts[0].substring(1);
        String[] args = CommandManager.removeElement(parts, 0);

        for (int i = 0; i < args.length; ++i) {
            if (args[i] == null) continue;
            args[i] = CommandManager.strip(args[i], "\"");
        }

        for (Command c : this.commands) {
            if (!c.getName().equalsIgnoreCase(name)) continue;
            c.execute(parts);
            return;
        }

        Command.sendMessage("Unknown command. try 'commands' for a list of commands.");
    }

    public static String[] removeElement(String[] input, int indexToDelete) {
        LinkedList<String> result = new LinkedList<>();

        for (int i = 0; i < input.length; ++i) {
            if (i == indexToDelete) continue;
            result.add(input[i]);
        }

        return result.toArray(input);
    }

    private static String strip(String str, String key) {
        if (str.startsWith(key) && str.endsWith(key)) {
            return str.substring(key.length(), str.length() - key.length());
        }

        return str;
    }

    public ArrayList<Command> getCommands() {
        return this.commands;
    }

    public String getClientMessage() {
        return this.clientMessage;
    }

    public void setClientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}

