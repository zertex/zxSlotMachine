package com.xapple.zxslotmachine.commands;

import com.xapple.zxslotmachine.Main;
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

@CommandDescription("List available server materials")
@CommandPermissions("zxslotmachine.admin")

/**
 * Created by Egorka on 15.10.2015.
 */
public class ListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.player-only"));
            return true;
        }
        Player player = (Player) sender;
        //if ((args.length == 1 && args[0].equalsIgnoreCase("list")) || (args.length == 2 && args[0].equalsIgnoreCase("list") && args[1].equalsIgnoreCase("1") )) {
        if (args.length == 1 && args[0].equalsIgnoreCase("list"))   {
            //for (Integer m = 0; m < 15; m++) {
            //    //Main.get().getLogger().info(Material.values()[m].toString());
            //    player.sendMessage(Material.values()[m].toString());
            //}
            //player.sendMessage("---------- 1 ----------");
            player.sendMessage("Use: /zslot list <NUM>");
        }
        else if (args.length == 2) {
            Integer num = Integer.valueOf(args[1]);
            if (num > 0) {
                Integer pages = (Integer) Material.values().length / 15;
                if (Material.values().length % 15 > 0) {
                    pages++;
                }
                if (num <= pages) {
                    for (Integer m = num * 15 - 15; (m < num * 15 && m < Material.values().length); m++) {
                        //ItemStack is = new ItemStack(Material.values()[m]);
                        //player.sendMessage(ChatColor.AQUA+Material.values()[m].toString() + " - " + ChatColor.GRAY + is.getItemMeta().getDisplayName());
                        player.sendMessage(ChatColor.WHITE+ String.valueOf(m) + ". " +ChatColor.AQUA+Material.values()[m].toString());
                    }
                    player.sendMessage(ChatColor.DARK_GRAY+"---------- " + ChatColor.YELLOW + String.valueOf(num) + ChatColor.DARK_GRAY + " / " + ChatColor.YELLOW + String.valueOf(pages) + ChatColor.DARK_GRAY+" ----------");
                    player.sendMessage("Use: /zslot list <NUM>");
                }
                else {
                    player.sendMessage(ChatColor.DARK_GRAY+"End material list");
                    player.sendMessage("Use: /zslot list <NUM>");
                }
            }
        }
        else {
            player.sendMessage("Use: /zslot list <NUM>");
        }
        return true;
    }


}
