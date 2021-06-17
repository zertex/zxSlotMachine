package com.xapple.zxslotmachine.commands;

import com.google.common.collect.Maps;
import com.xapple.zxslotmachine.lib.Messaging;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Created by Egorka on 09.10.2015.
 */
public class MainCommand implements CommandExecutor {

    private Map<String, CommandExecutor> subCommandMap = Maps.newHashMap();

    public MainCommand() {
        //subCommandMap.put("reload", new ReloadCommand());

        //subCommandMap.put("join", new JoinCommand());
        //subCommandMap.put("leave", new LeaveCommand());

        subCommandMap.put("list", new ListCommand());
        subCommandMap.put("activate", new ActivateCommand());
        subCommandMap.put("top", new TopCommand());
        subCommandMap.put("factor", new FactorCommand());
        //subCommandMap.put("clear", new AdminClearSpawnsCommand());
        //subCommandMap.put("setlobby", new AdminSetMainLobbyCommand());
        //subCommandMap.put("setarena", new AdminSetArenaCommand());

        //subCommandMap.put("score", new ScoreCommand());
        //if (!PluginConfig.disableKits()) {
        //    subCommandMap.put("kit", new KitCommand());
        //}
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.player-only"));
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            printHelp(player, label);
            return true;
        }

        String subCommandName = args[0].toLowerCase();
        if (!subCommandMap.containsKey(subCommandName)) {
            printHelp(player, label);
            return true;
        }

        CommandExecutor subCommand = subCommandMap.get(subCommandName);
        if (!hasPermission(player, subCommand)) {
            player.sendMessage(new Messaging.MessageFormatter().format("error.insufficient-permissions"));
            return true;
        }

        return subCommand.onCommand(sender, command, label, args);
    }

    private boolean hasPermission(Player bukkitPlayer, CommandExecutor cmd) {
        CommandPermissions permissions = cmd.getClass().getAnnotation(CommandPermissions.class);
        if (permissions == null) {
            return true;
        }

        for (String permission : permissions.value()) {
            if (bukkitPlayer.hasPermission(permission)) {
                return true;
            }
        }

        return false;
    }

    private void printHelp(Player bukkitPlayer, String label) {
        bukkitPlayer.sendMessage(new Messaging.MessageFormatter().withPrefix().format("game.available-commands"));

        for (Map.Entry<String, CommandExecutor> commandEntry : subCommandMap.entrySet()) {
            if (hasPermission(bukkitPlayer, commandEntry.getValue())) {
                String description = "No description available.";

                CommandDescription cmdDescription = commandEntry.getValue().getClass().getAnnotation(CommandDescription.class);
                if (cmdDescription != null) {
                    description = cmdDescription.value();
                }

                bukkitPlayer.sendMessage("\2477/" + label + " " + commandEntry.getKey() + " \247f-\247e " + description);
            }
        }
    }



    }
