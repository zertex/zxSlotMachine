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

@CommandDescription("Show Top Statistics")
@CommandPermissions("zxslotmachine.use")

/**
 * Created by Egorka on 15.10.2015.
 */
public class TopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.player-only"));
            return true;
        }
        Player player = (Player) sender;

        //if (!Main.activateUse) {
        //    sender.sendMessage(new Messaging.MessageFormatter().format("error.activate-mode-disabled"));
        //    return true;
        //}

        if (args.length == 3 && args[0].equalsIgnoreCase("top"))   {

            Integer num = 0;
            if (Functions.isStringInt(args[2])) {num = Integer.valueOf(args[2]);}

            String top = Functions.getTopPlayers(args[1].toLowerCase(), num);
            player.sendMessage(top);
        }
        else {
            player.sendMessage("Use: /zslot top amount/win/games/lose/maxwin NUM");
        }
        return true;
    }


}
