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

@CommandDescription("Show Factor Table")
@CommandPermissions("zxslotmachine.use")

/**
 * Created by Egorka on 15.10.2015.
 */
public class FactorCommand implements CommandExecutor {

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

        if (args.length == 1 && args[0].equalsIgnoreCase("factor"))   {

            //Integer num = 0;
            //if (Functions.isStringInt(args[2])) {num = Integer.valueOf(args[2]);}

            //String top = Functions.getTopPlayers(args[1].toLowerCase(), num);
            //player.sendMessage(top);
            Integer factor1 = Main.get().getConfig().getInt("wins.factor1", 1);
            Integer factor2 = Main.get().getConfig().getInt("wins.factor2", 6);
            Integer factor3 = Main.get().getConfig().getInt("wins.factor3", 12);
            Integer factor4 = Main.get().getConfig().getInt("wins.factor4", 12);
            Integer factor5 = Main.get().getConfig().getInt("wins.factor5", 25);
            Integer factor6 = Main.get().getConfig().getInt("wins.factor6", 50);
            Integer factor7 = Main.get().getConfig().getInt("wins.factor7", 100);
            Integer factor8 = Main.get().getConfig().getInt("wins.factor8", 300);
            Integer factor9 = Main.get().getConfig().getInt("wins.factor9", 1666);

            player.sendMessage(new Messaging.MessageFormatter().format("game.factor_title"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("factor", String.valueOf(factor9)).format("game.factor_9"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("factor", String.valueOf(factor8)).format("game.factor_8"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("factor", String.valueOf(factor7)).format("game.factor_7"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("factor", String.valueOf(factor6)).format("game.factor_6"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("factor", String.valueOf(factor5)).format("game.factor_5"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("factor", String.valueOf(factor4)).format("game.factor_4"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("factor", String.valueOf(factor3)).format("game.factor_3"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("factor", String.valueOf(factor2)).format("game.factor_2"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("factor", String.valueOf(factor1)).format("game.factor_1"));

        }
        else {
            player.sendMessage("Use: /zslot factor");
        }
        return true;
    }


}
