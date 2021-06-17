package com.xapple.zxslotmachine.commands;

import com.xapple.zxslotmachine.Main;
import com.xapple.zxslotmachine.lib.Functions;
import com.xapple.zxslotmachine.lib.Messaging;
import com.xapple.zxslotmachine.lib.PluginConfig;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.Set;

@CommandDescription("Change activate mode")
@CommandPermissions("zxslotmachine.admin")

/**
 * Created by Egorka on 15.10.2015.
 */
public class ActivateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.player-only"));
            return true;
        }
        Player player = (Player) sender;

        if (!Main.activateUse) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.activate-mode-disabled"));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("activate"))   {
            // Смотрим, активен ли пользователь
            if (Functions.getUserActivateState(player)) {
                // Деактивация пользователя
                Functions.deactivateUser(player);
                player.sendMessage(new Messaging.MessageFormatter().format("game.user-deactive"));
                player.sendMessage(new Messaging.MessageFormatter().format("game.user-active-repeat"));
            }
            else {
                // Активация пользователя
                Functions.activateUser(player);
                player.sendMessage(new Messaging.MessageFormatter().format("game.user-active"));
                player.sendMessage(new Messaging.MessageFormatter().format("game.user-deactive-repeat"));
            }

        }
        else {
            player.sendMessage("Use: /zslot activate");
        }
        return true;
    }


}
