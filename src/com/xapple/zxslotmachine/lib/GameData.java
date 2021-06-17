package com.xapple.zxslotmachine.lib;

import com.xapple.zxslotmachine.Main;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import com.xapple.zxslotmachine.lib.GameData;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by Egorka on 21.10.2015.
 */
public class GameData {
    public String id = "";
    public Integer counter1 = 0; // Счетчик для автомата
    public Integer counter2 = 0; // Счетчик для автомата
    public Integer counter3 = 0; // Счетчик для автомата
    public Integer lampCounter = 4; // Счетчик для лампы
    //public Set<Material> reels = new HashSet<Material>(); // Результат на барабанах
    public Player player = null;
    public Block block = null;
    public Block lamp = null;
    public Integer bet = 0;

    public Material reel1 = null;
    public Material reel2 = null;
    public Material reel3 = null;

    public ItemFrame frame1 = null;
    public ItemFrame frame2 = null;
    public ItemFrame frame3 = null;

    public void updateReels1() {
        frame1.setItem(new ItemStack(getRandMaterial()));
    }
    public void updateReels2() {
        frame2.setItem(new ItemStack(getRandMaterial()));
    }
    public void updateReels3() {

        //Main.get().getLogger().info(String.valueOf(Main.get().getConfig().getBoolean("sounds")));

//        if (Main.soundsOn) { // Если звуки включены
//            if (EnumUtils.isValidEnum(Sound.class, Main.rotationSound.toUpperCase())) {
                //Main.get().getLogger().info("1.9 >>> JRIGINAL ");
 //               player.playSound(player.getLocation(), Sound.valueOf(Main.rotationSound.toUpperCase()), 1, 1);
//            } else {
//                if (EnumUtils.isValidEnum(Sound.class, "CLICK")) { // <= 1.8.8
//                    player.playSound(player.getLocation(), Sound.valueOf("CLICK"), 1, 1);// .playEffect(player.getLocation(), Effect.CLICK2, 0);
//                } else {  /// >= 1.9
//                    player.playSound(player.getLocation(), Sound.valueOf("BLOCK_STONE_BUTTON_CLICK_ON"), 1, 1);
                    //Main.get().getLogger().info("1.9 >>> ");
//                }
//            }
//        }
        frame3.setItem(new ItemStack(getRandMaterial()));
    }

    public Material getRandMaterial() {
       // Main.get().getLogger().info("START RAND");
        Material[] m = new Material[]{Functions.getMaterial("redstone_block"), Functions.getMaterial("cake"), Functions.getMaterial("gold_ingot"), Functions.getMaterial("iron_ingot"), Functions.getMaterial("diamond"),
                Functions.getMaterial("emerald"), Functions.getMaterial("nether_star"), Functions.getMaterial("ender_pearl"), Functions.getMaterial("diamond_ore"), Functions.getMaterial("gold_ore"), Functions.getMaterial("emerald_ore"),
                Functions.getMaterial("melon"), Functions.getMaterial("lava_bucket"), Functions.getMaterial("water_bucket"), Functions.getMaterial("raw_fish"), Functions.getMaterial("apple"), Functions.getMaterial("golden_apple")};
        //Main.get().getLogger().info("MIDDLE RAND");
        Random rand = new Random();
        Integer r = rand.nextInt(m.length);
        //Main.get().getLogger().info("GET RAND");
        if (m[r] == null) {
            Main.get().getLogger().info("ERROR - Confirm Develop Team "+String.valueOf(r));
        }
        return m[r];
    }

    public void stopReel1() {
        //player.sendMessage(reel1.toString());
        //if (reel1 == null) {Main.get().getLogger().info("Reel1 NULL!!!");};
        try {
            frame1.setItem(new ItemStack(reel1));
        }
        catch (NullPointerException e) {
            Main.get().getLogger().info("Reel1: "+e.getMessage());
        }

    }
    public void stopReel2() {
        //player.sendMessage(reel1.toString());
        //player.sendMessage(reel2.toString());
        //if (reel2 == null) {Main.get().getLogger().info("Reel2 NULL!!!");};
        try {
            frame2.setItem(new ItemStack(reel2));
        }
        catch (NullPointerException e) {
            Main.get().getLogger().info("Reel2: "+e.getMessage());
        }
    }
    public void stopReel3() {
        //player.sendMessage(reel1.toString());
        //player.sendMessage(reel2.toString());
        //player.sendMessage(reel3.toString());

        //frame1.setItem(new ItemStack(reel1));
        //frame2.setItem(new ItemStack(reel2));

        double balance = Main.get().eco.getBalance(player);
        //if (reel3 == null) {Main.get().getLogger().info("Reel3 NULL!!!");};
        try {
            frame3.setItem(new ItemStack(reel3));
        }
        catch (NullPointerException e) {
            Main.get().getLogger().info("Reel3: "+e.getMessage());
        }

        //player.getWorld().playSound(block.getLocation(), Sound.LEVEL_UP, 2, 0);
        //player.getWorld().playEffect(new Location(player.getWorld(), block.getX(), block.getY()+3, block.getZ()), org.bukkit.Effect.HEART, 0);
        //player.getWorld().playEffect(new Location(player.getWorld(), block.getX(), block.getY()+3, block.getZ()), org.bukkit.Effect.HEART, 2);
        //player.getWorld().playEffect(new Location(player.getWorld(), block.getX(), block.getY()+3, block.getZ()), org.bukkit.Effect.HEART, 3);
        //player.getWorld().playEffect(new Location(player.getWorld(), block.getX(), block.getY()+3, block.getZ()), org.bukkit.Effect.HEART, 4);
        //player.getWorld().playEffect(new Location(player.getWorld(), block.getX(), block.getY()+3, block.getZ()), org.bukkit.Effect.HEART, 5);

        //

        //player.playSound();
        //Sound.
        //player.getWorld().playSound(block.getLocation(), Sound.valueOf("sogs.ogg"), 2, 0);
        //Packet62LevelSound
        //Packet62NamedSoundEffect packet = new Packet62NamedSoundEffect("portal.travel", p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ(), 1.0F, 1.0F);

        for (Iterator<GameData> i = Main.get().games.iterator(); i.hasNext();) {
        //for (Iterator i = Main.get().games.iterator(); i.hasNext();) {
            GameData element = i.next();
            //Object element2 = i.next();
            //GameData element = (GameData) element2;

            if (element.id.equalsIgnoreCase(id)) {

                // Окончание игры
                Integer factor1 = Main.get().getConfig().getInt("wins.factor1", 1);
                Integer factor2 = Main.get().getConfig().getInt("wins.factor2", 6);
                Integer factor3 = Main.get().getConfig().getInt("wins.factor3", 12);
                Integer factor4 = Main.get().getConfig().getInt("wins.factor4", 12);
                Integer factor5 = Main.get().getConfig().getInt("wins.factor5", 25);
                Integer factor6 = Main.get().getConfig().getInt("wins.factor6", 50);
                Integer factor7 = Main.get().getConfig().getInt("wins.factor7", 100);
                Integer factor8 = Main.get().getConfig().getInt("wins.factor8", 300);
                Integer factor9 = Main.get().getConfig().getInt("wins.factor9", 1666);

                // Одно яблоко
                if ((element.reel1 == Functions.getMaterial("apple") && element.reel2 != Functions.getMaterial("apple") && element.reel3 != Functions.getMaterial("apple")) ||
                        (element.reel1 != Functions.getMaterial("apple") && element.reel2 == Functions.getMaterial("apple") && element.reel3 != Functions.getMaterial("apple")) ||
                        (element.reel1 != Functions.getMaterial("apple") && element.reel2 != Functions.getMaterial("apple") && element.reel3 == Functions.getMaterial("apple"))) {
                    Main.get().eco.depositPlayer(player, bet*factor1); balance = Main.get().eco.getBalance(player);
                    Functions.updateSqlLog(player, 1, bet*factor1); // Запись в бд
                    player.sendMessage(new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor1)).setVariable("balance", String.valueOf(balance)).format("win"));
                    // Broadcast message
                    if (bet*factor1 >= Main.broadcastAmount) { Bukkit.broadcastMessage((new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor1)).setVariable("player", player.getName()).format("game.broadcast_message"))); };
                    //player.getWorld().playEffect(new Location(player.getWorld(), block.getX(), block.getY()+3, block.getZ()), org.bukkit.Effect.HEART, 0);
                    player.getWorld().spawnParticle(Particle.HEART, player.getLocation(), 1, block.getX(), block.getY()+3, block.getZ(), 0.01F);
                }
                // Два яблока
                else if ((element.reel1 == Functions.getMaterial("apple") && element.reel2 == Functions.getMaterial("apple") && element.reel3 != Functions.getMaterial("apple")) ||
                        (element.reel1 == Functions.getMaterial("apple") && element.reel2 != Functions.getMaterial("apple") && element.reel3 == Functions.getMaterial("apple")) ||
                        (element.reel1 != Functions.getMaterial("apple") && element.reel2 == Functions.getMaterial("apple") && element.reel3 == Functions.getMaterial("apple"))) {
                    Main.get().eco.depositPlayer(player, bet*factor2); balance = Main.get().eco.getBalance(player);
                    Functions.updateSqlLog(player, 1, bet*factor2); // Запись в бд
                    player.sendMessage(new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor2)).setVariable("balance", String.valueOf(balance)).format("win"));
                    // Broadcast message
                    if (bet*factor2 >= Main.broadcastAmount) { Bukkit.broadcastMessage((new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor2)).setVariable("player", player.getName()).format("game.broadcast_message"))); };
                    //player.getWorld().playEffect(new Location(player.getWorld(), block.getX(), block.getY()+3, block.getZ()), org.bukkit.Effect.HEART, 0);
                    player.getWorld().spawnParticle(Particle.HEART, player.getLocation(), 1, block.getX(), block.getY()+3, block.getZ(), 0.01F);
                }
                else if (element.reel1 ==Functions.getMaterial("apple") && element.reel2 == Functions.getMaterial("apple") && element.reel3 == Functions.getMaterial("apple")) {
                    Main.get().eco.depositPlayer(player, bet*factor3); balance = Main.get().eco.getBalance(player);
                    Functions.updateSqlLog(player, 1, bet*factor3); // Запись в бд
                    player.sendMessage(new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor3)).setVariable("balance", String.valueOf(balance)).format("win"));
                    // Broadcast message
                    if (bet*factor3 >= Main.broadcastAmount) { Bukkit.broadcastMessage((new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor3)).setVariable("player", player.getName()).format("game.broadcast_message"))); };
                    //player.getWorld().playEffect(new Location(player.getWorld(), block.getX(), block.getY()+3, block.getZ()), org.bukkit.Effect.HEART, 0);
                    player.getWorld().spawnParticle(Particle.HEART, player.getLocation(), 1, block.getX(), block.getY()+3, block.getZ(), 0.01F);
                }
                else if ((element.reel1 == Functions.getMaterial("diamond_ore") && element.reel2 == Functions.getMaterial("gold_ore") && element.reel3 == Functions.getMaterial("emerald_ore")) ||
                        (element.reel1 == Functions.getMaterial("diamond_ore") && element.reel2 == Functions.getMaterial("emerald_ore") && element.reel3 == Functions.getMaterial("gold_ore")) ||
                        (element.reel1 == Functions.getMaterial("emerald_ore") && element.reel2 == Functions.getMaterial("diamond_ore") && element.reel3 == Functions.getMaterial("gold_ore")) ||
                        (element.reel1 == Functions.getMaterial("gold_ore") && element.reel2 == Functions.getMaterial("diamond_ore") && element.reel3 == Functions.getMaterial("emerald_ore")) ||
                        (element.reel1 == Functions.getMaterial("emerald_ore") && element.reel2 == Functions.getMaterial("gold_ore") && element.reel3 == Functions.getMaterial("diamond_ore")) ||
                        (element.reel1 == Functions.getMaterial("gold_ore") && element.reel2 == Functions.getMaterial("emerald_ore") && element.reel3 == Functions.getMaterial("diamond_ore")) ||
                        (element.reel1 == Functions.getMaterial("diamond_ore") && element.reel2 == Functions.getMaterial("diamond_ore") && element.reel3 ==Functions.getMaterial("gold_ore")) ||
                        (element.reel1 == Functions.getMaterial("diamond_ore") && element.reel2 == Functions.getMaterial("diamond_ore") && element.reel3 == Functions.getMaterial("emerald_ore")) ||
                        (element.reel1 == Functions.getMaterial("diamond_ore") && element.reel2 == Functions.getMaterial("gold_ore") && element.reel3 == Functions.getMaterial("diamond_ore")) ||
                        (element.reel1 == Functions.getMaterial("diamond_ore") && element.reel2 == Functions.getMaterial("emerald_ore") && element.reel3 == Functions.getMaterial("diamond_ore"))||
                        (element.reel1 == Functions.getMaterial("emerald_ore") && element.reel2 == Functions.getMaterial("diamond_ore") && element.reel3 == Functions.getMaterial("diamond_ore")) ||
                        (element.reel1 == Functions.getMaterial("gold_ore") && element.reel2 == Functions.getMaterial("diamond_ore") && element.reel3 == Functions.getMaterial("diamond_ore")) ||
                        (element.reel1 == Functions.getMaterial("gold_ore") && element.reel2 == Functions.getMaterial("gold_ore") && element.reel3 == Functions.getMaterial("diamond_ore"))||
                        (element.reel1 == Functions.getMaterial("gold_ore") && element.reel2 == Functions.getMaterial("gold_ore") && element.reel3 == Functions.getMaterial("emerald_ore")) ||
                        (element.reel1 == Functions.getMaterial("gold_ore") && element.reel2 == Functions.getMaterial("diamond_ore") && element.reel3 == Functions.getMaterial("gold_ore")) ||
                        (element.reel1 == Functions.getMaterial("gold_ore") && element.reel2 == Functions.getMaterial("emerald_ore") && element.reel3 == Functions.getMaterial("gold_ore")) ||
                        (element.reel1 ==Functions.getMaterial("emerald_ore") && element.reel2 == Functions.getMaterial("gold_ore") && element.reel3 == Functions.getMaterial("gold_ore")) ||
                        (element.reel1 == Functions.getMaterial("diamond_ore") && element.reel2 == Functions.getMaterial("gold_ore") && element.reel3 == Functions.getMaterial("gold_ore")) ||
                        (element.reel1 == Functions.getMaterial("emerald_ore") && element.reel2 == Functions.getMaterial("emerald_ore") && element.reel3 == Functions.getMaterial("gold_ore")) ||
                        (element.reel1 == Functions.getMaterial("emerald_ore") && element.reel2 == Functions.getMaterial("emerald_ore") && element.reel3 == Functions.getMaterial("diamond_ore")) ||
                        (element.reel1 == Functions.getMaterial("emerald_ore") && element.reel2 == Functions.getMaterial("diamond_ore") && element.reel3 == Functions.getMaterial("emerald_ore")) ||
                        (element.reel1 == Functions.getMaterial("emerald_ore") && element.reel2 == Functions.getMaterial("gold_ore") && element.reel3 == Functions.getMaterial("emerald_ore")) ||
                        (element.reel1 == Functions.getMaterial("diamond_ore") && element.reel2 == Functions.getMaterial("emerald_ore") && element.reel3 == Functions.getMaterial("emerald_ore")) ||
                        (element.reel1 == Functions.getMaterial("gold_ore") && element.reel2 == Functions.getMaterial("emerald_ore") && element.reel3 == Functions.getMaterial("emerald_ore"))) {
                    Main.get().eco.depositPlayer(player, bet*factor4); balance = Main.get().eco.getBalance(player);
                    Functions.updateSqlLog(player, 1, bet*factor4); // Запись в бд
                    player.sendMessage(new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor4)).setVariable("balance", String.valueOf(balance)).format("win"));
                    // Broadcast message
                    if (bet*factor4 >= Main.broadcastAmount) { Bukkit.broadcastMessage((new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor4)).setVariable("player", player.getName()).format("game.broadcast_message"))); };
                    //player.getWorld().playEffect(new Location(player.getWorld(), block.getX(), block.getY()+3, block.getZ()), org.bukkit.Effect.HEART, 0);
                    player.getWorld().spawnParticle(Particle.HEART, player.getLocation(), 1, block.getX(), block.getY()+3, block.getZ(), 0.01F);
                }
                else if (element.reel1 == Functions.getMaterial("gold_ore") && element.reel2 == Functions.getMaterial("gold_ore") && element.reel3 == Functions.getMaterial("gold_ore")) {
                    Main.get().eco.depositPlayer(player, bet*factor5); balance = Main.get().eco.getBalance(player);
                    Functions.updateSqlLog(player, 1, bet*factor5); // Запись в бд
                    player.sendMessage(new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor5)).setVariable("balance", String.valueOf(balance)).format("win"));
                    // Broadcast message
                    if (bet*factor5 >= Main.broadcastAmount) { Bukkit.broadcastMessage((new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor5)).setVariable("player", player.getName()).format("game.broadcast_message"))); };
                    //player.getWorld().playEffect(new Location(player.getWorld(), block.getX(), block.getY()+3, block.getZ()), org.bukkit.Effect.HEART, 0);
                    player.getWorld().spawnParticle(Particle.HEART, player.getLocation(), 1, block.getX(), block.getY()+3, block.getZ(), 0.01F);
                }
                else if (element.reel1 == Functions.getMaterial("emerald_ore") && element.reel2 == Functions.getMaterial("emerald_ore") && element.reel3 == Functions.getMaterial("emerald_ore")) {
                    Main.get().eco.depositPlayer(player, bet*factor6); balance = Main.get().eco.getBalance(player);
                    Functions.updateSqlLog(player, 1, bet*factor6); // Запись в бд
                    player.sendMessage(new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor6)).setVariable("balance", String.valueOf(balance)).format("win"));
                    // Broadcast message
                    if (bet*factor6 >= Main.broadcastAmount) { Bukkit.broadcastMessage((new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor6)).setVariable("player", player.getName()).format("game.broadcast_message"))); };
                    player.getWorld().spawnParticle(Particle.HEART, player.getLocation(), 1, block.getX(), block.getY()+3, block.getZ(), 0.01F);
                    //player.getWorld().playEffect(new Location(player.getWorld(), block.getX(), block.getY()+3, block.getZ()), org.bukkit.Effect.HEART, 0);
                }
                else if (element.reel1 == Functions.getMaterial("diamond_ore") && element.reel2 == Functions.getMaterial("diamond_ore") && element.reel3 == Functions.getMaterial("diamond_ore")) {
                    Main.get().eco.depositPlayer(player, bet*factor7); balance = Main.get().eco.getBalance(player);
                    Functions.updateSqlLog(player, 1, bet*factor7); // Запись в бд
                    player.sendMessage(new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor7)).setVariable("balance", String.valueOf(balance)).format("win"));
                    // Broadcast message
                    if (bet*factor7 >= Main.broadcastAmount) { Bukkit.broadcastMessage((new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor7)).setVariable("player", player.getName()).format("game.broadcast_message"))); };
                    //player.getWorld().playEffect(new Location(player.getWorld(), block.getX(), block.getY()+3, block.getZ()), org.bukkit.Effect.HEART, 0);
                    player.getWorld().spawnParticle(Particle.HEART, player.getLocation(), 1, block.getX(), block.getY()+3, block.getZ(), 0.01F);
                }
                else if (element.reel1 == Functions.getMaterial("golden_apple") && element.reel2 == Functions.getMaterial("golden_apple") && element.reel3 == Functions.getMaterial("golden_apple")) {
                    Main.get().eco.depositPlayer(player, bet*factor8); balance = Main.get().eco.getBalance(player);
                    Functions.updateSqlLog(player, 1, bet*factor8); // Запись в бд
                    player.sendMessage(new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor8)).setVariable("balance", String.valueOf(balance)).format("win"));
                    // Broadcast message
                    if (bet*factor8 >= Main.broadcastAmount) { Bukkit.broadcastMessage((new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor8)).setVariable("player", player.getName()).format("game.broadcast_message"))); };
                    //player.getWorld().playEffect(new Location(player.getWorld(), block.getX(), block.getY()+3, block.getZ()), org.bukkit.Effect.HEART, 0);
                    player.getWorld().spawnParticle(Particle.HEART, player.getLocation(), 1, block.getX(), block.getY()+3, block.getZ(), 0.01F);
                }
                else if (element.reel1 == Functions.getMaterial("gold_ingot") && element.reel2 == Functions.getMaterial("gold_ingot") && element.reel3 == Functions.getMaterial("gold_ingot")) {
                    Main.get().eco.depositPlayer(player, bet * factor9); balance = Main.get().eco.getBalance(player);
                    player.sendMessage(new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor9)).setVariable("balance", String.valueOf(balance)).format("win"));
                    // Broadcast message
                    if (bet*factor9 >= Main.broadcastAmount) { Bukkit.broadcastMessage((new Messaging.MessageFormatter().setVariable("amount", String.valueOf(bet*factor9)).setVariable("player", player.getName()).format("game.broadcast_message"))); };
                    Functions.updateSqlLog(player, 1, bet*factor9); // Запись в бд
                    player.getWorld().spawnParticle(Particle.HEART, player.getLocation(), 1, block.getX(), block.getY()+3, block.getZ(), 0.01F);
                    //player.getWorld().playEffect(new Location(player.getWorld(), block.getX(), block.getY()+3, block.getZ()), org.bukkit.Effect. .HEART, 0);
                    //element.lampCounter=4;
                    //element.lamp.setType(Material.GLOWSTONE);

                }
                else {
                    //player.sendMessage(new Messaging.MessageFormatter().setVariable("balance", String.valueOf(balance)).format("lose"));
                    Functions.updateSqlLog(player, 0, 0); // Запись в бд
                    player.sendMessage(new Messaging.MessageFormatter().setVariable("balance", String.valueOf(Main.get().eco.getBalance(player))).format("lose"));
                    //element.lampCounter=0;
                }

                //element.lamp.setType(Material.GLOWSTONE);



                i.remove();
            }
        }



        //player.sendMessage("GAME END");
    }

}
