package com.xapple.zxslotmachine.listeners;


import com.xapple.zxslotmachine.Main;
import com.xapple.zxslotmachine.lib.Functions;
import com.xapple.zxslotmachine.lib.GameData;
import com.xapple.zxslotmachine.lib.Messaging;
import com.xapple.zxslotmachine.lib.SlotData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Button;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * Created by Egorka on 10.10.2015.
 */
public class EntityListener implements Listener {

    public Integer counter1 = 1;
    public Integer taskId1 = 0;
    // Если игрок что-то сделал

    @EventHandler
    //public void onPlayerInteract1(PlayerInteractEvent e) {
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
    //public void onPlayerInteractEvent(PlayerInteractEntityEvent e) {

        //e.getPlayer().getNearbyEntities()

        Player player = e.getPlayer();
        //player.sendMessage(Functions.getMaterial("iron_block").toString());
        // Если клик правой мышкой
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            Block b = e.getClickedBlock();
            //player.sendMessage(b.getType().toString());

            if (b.getType() == Material.STONE_BUTTON) {
                //player.sendMessage("Stone Button Clicked");

                if (player.hasPermission("zxslotmachine.use")) {

                    boolean error = false;

                    String gameId = "gid" + String.valueOf(b.getX()) + "=" + String.valueOf(b.getY())+"="+String.valueOf(b.getZ());

                    // Если используется активация автоматов
                    if (Main.activateUse) {
                        // Если у пользователя включена активация
                        if (Functions.getUserActivateState(player)) {
                            // Смотрим, активен ли автомат
                            if (Functions.getMachineActivateState(gameId)) {
                                // Отключаем автомат
                                Functions.deactivateMachine(gameId);
                                player.sendMessage(new Messaging.MessageFormatter().format("game.slots_deactivated"));
                            }
                            else {
                                // Включаем автомат
                                Functions.enableSlots(gameId);
                                player.sendMessage(new Messaging.MessageFormatter().format("game.slots_activated"));
                            }
                        }
                        else {
                            // Проверяем, активен ли автомат
                            if (!Functions.getMachineActivateState(gameId)) {
                                player.sendMessage(new Messaging.MessageFormatter().format("error.slots_deny"));
                                e.setCancelled(true);
                                error = true;
                            }
                        }
                    }

                    if (inGame(gameId)) {
                        player.sendMessage(new Messaging.MessageFormatter().format("error.game_started"));
                        e.setCancelled(true);
                        error = true;
                    }

                    if (inGame(player) && Main.singlePlay) {
                        player.sendMessage(new Messaging.MessageFormatter().format("error.already_playing"));
                        e.setCancelled(true);
                        error = true;
                    }

                    if (!error) {
                        if (haveAutomate(b, player)) {

                            // Смотрим активность машины
                            if (!Main.activateUse || Functions.checkActivatedSlots(gameId)) {

                                // Смотрим баланс игрока
                                double balance = Main.get().eco.getBalance(player);
                                // Смотрим стоимость ставки
                                Integer bet = getBet(b, player);
                                if (balance - (double) bet > 0) {
                                    //String gameId = "gid" + String.valueOf(b.getX()) + "=" + String.valueOf(b.getY())+"="+String.valueOf(b.getZ());
                                    if (inGame(gameId)) {
                                        //if (setContains(SlotData.games, gameId)) {
                                        //player.sendMessage(new Messaging.MessageFormatter().format("error.game_started"));
                                    } else {
                                        //player.sendMessage("Slot Machine Clicked");
                                        Main.get().eco.withdrawPlayer(player, (double) bet);
                                        //player.sendMessage(String.valueOf(Main.get().eco.getBalance(player)));
                                        //game.your_balance
                                        player.sendMessage(new Messaging.MessageFormatter().setVariable("balance", String.valueOf(Main.get().eco.getBalance(player))).setVariable("bet", String.valueOf(bet)).format("game.bet_accepted"));
                                        startGame(e.getClickedBlock(), player);
                                    }
                                } else {
                                    player.sendMessage(new Messaging.MessageFormatter().format("error.not_enought_money"));
                                }
                            }
                            else {
                                player.sendMessage(new Messaging.MessageFormatter().format("error.slots_deny"));
                            }
                        }
                    }
                }
                else {
                    player.sendMessage(new Messaging.MessageFormatter().format("error.insufficient-permissions"));
                }
                //player.sendMessage("Stone Button Location: "+String.valueOf(x)+"."+String.valueOf(y)+"."+String.valueOf(z));
                //player.sendMessage("Iron Block Location: "+String.valueOf(xa)+"."+String.valueOf(ya)+"."+String.valueOf(za));
                //b.getX()
            }
        }
    }

    // Игрок играет
    public Boolean inGame(Player player) {
        boolean res = false;
        for (int i = 0; i < Main.games.size(); i++) {
            GameData element = Main.games.get(i);
            if (element.player.getName().equalsIgnoreCase(player.getName())) {
                res = true;
            }
        }
        return res;
    }

    // Идет игра на автомате ID
    public Boolean inGame(String id) {
        boolean res = false;
        /*for (Iterator<GameData> i = Main.get().games.iterator(); i.hasNext();) {
            GameData element = i.next();
            if (element.id.equalsIgnoreCase(id)) {
                res = true;
                Main.get().getLogger().info(element.id + " :: " + id + " :: "+ Main.get().games.size());
            }
            Main.get().getLogger().info(element.id + " :: " + id);
        }*/

        for (Integer i = 0; i < Main.games.size(); i++) {
            GameData element = Main.games.get(i);
            if (element.id.equalsIgnoreCase(id)) {
                res = true;
                //Main.get().getLogger().info(element.id + " :: " + id + " :: "+ Main.get().games.size());
            }
            //Main.get().getLogger().info(element.id + " :: " + id);
        }

        return res;
    }

    /*public Boolean setContains(Set<String> s, String str) {
        for(String item : s) {
            if (item.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }*/

    // Запуск игры
    public void startGame(Block b, Player player) {
        //Integer counter1 = 1;
        String gameId = "gid" + String.valueOf(b.getX()) + "=" + String.valueOf(b.getY())+"="+String.valueOf(b.getZ());
        //Main.get().getLogger().info("GAMEID: "+gameId);

        if (inGame(gameId)) {
        //if (setContains(SlotData.games, gameId)) {
            player.sendMessage(new Messaging.MessageFormatter().format("error.game_started"));
        }
        else {
            //Set<Material> reels = SlotData.getReelItems();
            GameData game = new GameData();
            game.counter1=12;
            game.counter2=16;
            game.counter3=20;
            game.id = gameId;
            Material[] reels = SlotData.getReelItems();
            game.reel1 = reels[0];
            game.reel2 = reels[1];
            game.reel3 = reels[2];
            game.player = player;
            game.block = b;
            game.lamp = getLamp(b, player);
            game.lampCounter = 2;
            ItemFrame[] frames = getFrames(b, player);
            game.frame1 = frames[2];
            game.frame2 = frames[1];
            game.frame3 = frames[0];

            game.bet = getBet(b, player);

            //player.sendMessage("START GAME");
            Main.get().games.add(game);
            // Запускаем рандомную генерацию барабанов
  //          BukkitScheduler scheduler1 = Bukkit.getServer().getScheduler();
            //Integer taskId1 = 0;

            //scheduler1.scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
            /*taskId1 = scheduler1.scheduleSyncRepeatingTask(Main.get(), new Runnable() {
                @Override
                public void run() {
                    if (counter1 > 12) {
                        Bukkit.getScheduler().cancelTask(taskId1);
                        //Main.get().getLogger().info(String.valueOf(counter1));
                        player.sendMessage(String.valueOf(counter1));
                        //cancel();
                    }
                    counter1++;
                }
            }, 0L, 5L); // 20L - 1 sec */
        }

    }

    // Проверка наличия рамки по координатам
    public static ItemFrame getFrame(Location loc) {
        for (Entity e : loc.getChunk().getEntities())
            if (e instanceof ItemFrame)
                if (e.getLocation().getBlock().getLocation().distance(loc) == 0)
                    return (ItemFrame) e;
        return null;
    }


    // Проверка строки на число
    public Boolean parseInteger(String s)
    {
        try {
            Integer number = Integer.parseInt(s);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }


    // Обновление табличек
    public Boolean updateSigns(Player player, Sign s, Sign s2) {
        Boolean res = false;
        Integer coinprice = 0;
        String line1 = s.getLine(0);
        String line3 = s.getLine(2);
        if (!line1.equalsIgnoreCase("") && parseInteger(line1)) {
            if (parseInteger(line1) && player.hasPermission("zxslotmachine.create")) {
                coinprice = Integer.valueOf(line1);
            }
            else if (!player.hasPermission("zxslotmachine.create")) {
                player.sendMessage(new Messaging.MessageFormatter().format("error.insufficient-permissions"));
                return false;
            }
            else {
                player.sendMessage(new Messaging.MessageFormatter().format("error.wrong_price"));
            }
        }
        else if (!line3.equalsIgnoreCase("") && line3.length()>4) {
            if (parseInteger(line3.substring(4))) {
                coinprice = Integer.valueOf(line3.substring(4));
            }
            else {
                player.sendMessage(new Messaging.MessageFormatter().format("error.wrong_price"));
            }
            //coinprice = Integer.valueOf(line3.substring(4));
        }


        //if (coinprice > 0 && player.hasPermission("zxslotmachine.create")) {
        if (coinprice > 0) {
            s2.setLine(0, new Messaging.MessageFormatter().format("title"));
            s2.setLine(1, "");
            s2.setLine(2, new Messaging.MessageFormatter().format("last_player"));
            String message = new Messaging.MessageFormatter()
                    .setVariable("player", player.getDisplayName())
                    .format("last_player_name");
            s2.setLine(3, message);

            s.setLine(0, "");
            s.setLine(1, new Messaging.MessageFormatter().format("price"));
            s.setLine(2, new Messaging.MessageFormatter().format("&4&l"+String.valueOf(coinprice)));
            s.setLine(3, "");

            s.update();
            s2.update();
            res = true;
        }
        else {
            player.sendMessage(new Messaging.MessageFormatter().format("error.not_active"));
            res = false;
        }
        return res;
    }

    // Получение лампы
    public Block getLamp(Block b, Player player) {
        Block lamp = null;
        Button button = (Button)b.getState().getData();
        Block attached = b.getRelative(button.getAttachedFace());

        int x = b.getX();
        Integer y = b.getY();
        int z = b.getZ();

        Integer xa = attached.getX();
        Integer ya = attached.getY();
        Integer za = attached.getZ();

        if (x > xa) {
            Location ll = new Location(player.getWorld(), xa, ya+2, za+1); lamp = ll.getBlock();
        }
        else if (x < xa) {
            Location ll = new Location(player.getWorld(), xa, ya+2, za-1); lamp = ll.getBlock();
        }
        else if (z < za) {
            Location ll = new Location(player.getWorld(), xa+1, ya+2, za); lamp = ll.getBlock();
        }
        else if (z > za) {
            Location ll = new Location(player.getWorld(), xa-1, ya+2, za); lamp = ll.getBlock();
        }
        return lamp;
    }


    // Получение ставки
    public Integer getBet(Block b, Player player) {
        Integer bet = 0;
        Button button = (Button)b.getState().getData();
        Block attached = b.getRelative(button.getAttachedFace());

        Integer x = b.getX();
        Integer y = b.getY();
        Integer z = b.getZ();

        Integer xa = attached.getX();
        Integer ya = attached.getY();
        Integer za = attached.getZ();
        Block bt1 = null;
        Block bt2 = null;
        if (x > xa) {
            Location lt1 = new Location(player.getWorld(), x, ya+2, za); bt1 = lt1.getBlock();
            Location lt2 = new Location(player.getWorld(), x, ya+2, za+2); bt2 = lt2.getBlock();

        }
        else if (x < xa) {
            Location lt1 = new Location(player.getWorld(), x, ya+2, za); bt1 = lt1.getBlock();
            Location lt2 = new Location(player.getWorld(), x, ya+2, za-2); bt2 = lt2.getBlock();
        }
        else if (z < za) {
            Location lt1 = new Location(player.getWorld(), xa, ya+2, z); bt1 = lt1.getBlock();
            Location lt2 = new Location(player.getWorld(), xa+2, ya+2, z); bt2 = lt2.getBlock();
        }
        else if (z > za) {
            Location lt1 = new Location(player.getWorld(), xa, ya+2, z); bt1 = lt1.getBlock();
            Location lt2 = new Location(player.getWorld(), xa-2, ya+2, z); bt2 = lt2.getBlock();
        }
        Sign s = (Sign) bt1.getState();
        String line1 = s.getLine(0);
        String line3 = s.getLine(2);

        if (!line1.equalsIgnoreCase("")) {
            if (parseInteger(line1)) {
                bet = Integer.valueOf(line1);
            }
        }
        else if (!line3.equalsIgnoreCase("")) {
            bet = Integer.valueOf(line3.substring(4));
        }
        return bet;
    }

    // Получение рамок у автомата
    public ItemFrame[] getFrames(Block b, Player player) {
        ItemFrame[] frames = new ItemFrame[3];
        Button button = (Button)b.getState().getData();
        Block attached = b.getRelative(button.getAttachedFace());

        Integer x = b.getX();
        Integer y = b.getY();
        Integer z = b.getZ();

        Integer xa = attached.getX();
        Integer ya = attached.getY();
        Integer za = attached.getZ();

        if (x > xa) {
            // Проверяем рамки
            Location lb3 = new Location(player.getWorld(), x, y + 1, za);
            frames[0] = getFrame(lb3); //Block br3 = lb3.getBlock();
            Location lb2 = new Location(player.getWorld(), x, y + 1, za + 1);
            frames[1] = getFrame(lb2); //Block br2 = lb2.getBlock();
            Location lb1 = new Location(player.getWorld(), x, y + 1, za + 2);
            frames[2] = getFrame(lb1); //Block br1 = lb1.getBlock();
        }
        else if (x < xa) {
            // Проверяем рамки
            Location lb3 = new Location(player.getWorld(), x, y+1, za); frames[0] = getFrame(lb3);
            Location lb2 = new Location(player.getWorld(), x, y+1, za-1); frames[1] = getFrame(lb2);
            Location lb1 = new Location(player.getWorld(), x, y+1, za-2); frames[2] = getFrame(lb1);
        }
        else if (z < za) {
            // Проверяем рамки
            Location lb3 = new Location(player.getWorld(), xa, y+1, z); frames[0] = getFrame(lb3);
            Location lb2 = new Location(player.getWorld(), xa+1, y+1, z); frames[1] = getFrame(lb2);
            Location lb1 = new Location(player.getWorld(), xa+2, y+1, z); frames[2] = getFrame(lb1);
        }
        else if (z > za) {
            // Проверяем рамки
            Location lb3 = new Location(player.getWorld(), xa, y+1, z); frames[0] = getFrame(lb3);
            Location lb2 = new Location(player.getWorld(), xa-1, y+1, z); frames[1] = getFrame(lb2);
            Location lb1 = new Location(player.getWorld(), xa-2, y+1, z); frames[2] = getFrame(lb1);
        }
        return frames;
    }

    // b - Блок кнопки
    public Boolean haveAutomate(Block b, Player player) {
        Boolean res = false;
        Button button = (Button)b.getState().getData();
        Block attached = b.getRelative(button.getAttachedFace());

        Integer x = b.getX();
        Integer y = b.getY();
        Integer z = b.getZ();

        Integer xa = attached.getX();
        Integer ya = attached.getY();
        Integer za = attached.getZ();

        // X- (Z+,Y+)
        if (x > xa) {
            Location l9 = new Location(player.getWorld(), xa, ya, za); Block b9 = l9.getBlock();
            Location l8 = new Location(player.getWorld(), xa, ya, za+1); Block b8 = l8.getBlock();
            Location l7 = new Location(player.getWorld(), xa, ya, za+2); Block b7 = l7.getBlock();
            Location l6 = new Location(player.getWorld(), xa, ya+1, za); Block b6 = l6.getBlock();
            Location l5 = new Location(player.getWorld(), xa, ya+1, za+1); Block b5 = l5.getBlock();
            Location l4 = new Location(player.getWorld(), xa, ya+1, za+2); Block b4 = l4.getBlock();
            Location l3 = new Location(player.getWorld(), xa, ya+2, za); Block b3 = l3.getBlock();
            Location l2 = new Location(player.getWorld(), xa, ya+2, za+1); Block b2 = l2.getBlock();
            Location l1 = new Location(player.getWorld(), xa, ya+2, za+2); Block b1 = l1.getBlock();
            // Проверяем рамки
            Location lb3 = new Location(player.getWorld(), x, y+1, za); ItemFrame fr3 = getFrame(lb3); //Block br3 = lb3.getBlock();
            Location lb2 = new Location(player.getWorld(), x, y+1, za+1); ItemFrame fr2 = getFrame(lb2); //Block br2 = lb2.getBlock();
            Location lb1 = new Location(player.getWorld(), x, y+1, za+2); ItemFrame fr1 = getFrame(lb1); //Block br1 = lb1.getBlock();
            // Проверяем таблички
            Location lt1 = new Location(player.getWorld(), x, ya+2, za); Block bt1 = lt1.getBlock();
            Location lt2 = new Location(player.getWorld(), x, ya+2, za+2); Block bt2 = lt2.getBlock();

            // Если сзади железная стена
            if (b1.getType() == Functions.getMaterial("iron_block") && b2.getType() == Material.WHITE_CONCRETE && b3.getType() == Functions.getMaterial("iron_block") &&
                    b4.getType() == Functions.getMaterial("iron_block") && b5.getType() == Functions.getMaterial("iron_block") && b6.getType() == Functions.getMaterial("iron_block") &&
                    b7.getType() == Functions.getMaterial("iron_block") && b8.getType() == Functions.getMaterial("iron_block") && b9.getType() == Functions.getMaterial("iron_block") &&
                    fr1 != null && fr2 != null && fr3 != null &&
                    bt1.getType() == Material.SPRUCE_WALL_SIGN && bt2.getType() == Material.SPRUCE_WALL_SIGN) {
            /*if (b1.getType() == Material.IRON_BLOCK && b2.getType() == Material.REDSTONE_LAMP_OFF && b3.getType() == Material.IRON_BLOCK &&
                    b4.getType() == Material.IRON_BLOCK && b5.getType() == Material.IRON_BLOCK && b6.getType() == Material.IRON_BLOCK &&
                    b7.getType() == Material.IRON_BLOCK && b8.getType() == Material.IRON_BLOCK && b9.getType() == Material.IRON_BLOCK &&
                    fr1 != null && fr2 != null && fr3 != null &&
                    bt1.getType() == Material.WALL_SIGN && bt2.getType() == Material.WALL_SIGN) {*/

                // Проверяем текст таблички
                Sign s = (Sign) bt1.getState();
                Sign s2 = (Sign) bt2.getState();
                // Обновление табличек
                res = updateSigns(player, s, s2);
            }
        }
        // X+ (Z-, Y+)
        else if (x < xa) {
            Location l9 = new Location(player.getWorld(), xa, ya, za); Block b9 = l9.getBlock();
            Location l8 = new Location(player.getWorld(), xa, ya, za-1); Block b8 = l8.getBlock();
            Location l7 = new Location(player.getWorld(), xa, ya, za-2); Block b7 = l7.getBlock();
            Location l6 = new Location(player.getWorld(), xa, ya+1, za); Block b6 = l6.getBlock();
            Location l5 = new Location(player.getWorld(), xa, ya+1, za-1); Block b5 = l5.getBlock();
            Location l4 = new Location(player.getWorld(), xa, ya+1, za-2); Block b4 = l4.getBlock();
            Location l3 = new Location(player.getWorld(), xa, ya+2, za); Block b3 = l3.getBlock();
            Location l2 = new Location(player.getWorld(), xa, ya+2, za-1); Block b2 = l2.getBlock();
            Location l1 = new Location(player.getWorld(), xa, ya+2, za-2); Block b1 = l1.getBlock();
            // Проверяем рамки
            Location lb3 = new Location(player.getWorld(), x, y+1, za); ItemFrame fr3 = getFrame(lb3);
            Location lb2 = new Location(player.getWorld(), x, y+1, za-1); ItemFrame fr2 = getFrame(lb2);
            Location lb1 = new Location(player.getWorld(), x, y+1, za-2); ItemFrame fr1 = getFrame(lb1);
            // Проверяем таблички
            Location lt1 = new Location(player.getWorld(), x, ya+2, za); Block bt1 = lt1.getBlock();
            Location lt2 = new Location(player.getWorld(), x, ya+2, za-2); Block bt2 = lt2.getBlock();
            // Если сзади железная стена
            if (b1.getType() == Functions.getMaterial("iron_block") && b2.getType() == Material.WHITE_CONCRETE && b3.getType() == Functions.getMaterial("iron_block") &&
                    b4.getType() == Functions.getMaterial("iron_block") && b5.getType() == Functions.getMaterial("iron_block") && b6.getType() == Functions.getMaterial("iron_block") &&
                    b7.getType() == Functions.getMaterial("iron_block") && b8.getType() == Functions.getMaterial("iron_block") && b9.getType() == Functions.getMaterial("iron_block") &&
                    fr1 != null && fr2 != null && fr3 != null &&
                    bt1.getType() == Material.SPRUCE_WALL_SIGN && bt2.getType() == Material.SPRUCE_WALL_SIGN) {
            /*if (b1.getType() == Material.IRON_BLOCK && b2.getType() == Material.REDSTONE_LAMP_OFF && b3.getType() == Material.IRON_BLOCK &&
                    b4.getType() == Material.IRON_BLOCK && b5.getType() == Material.IRON_BLOCK && b6.getType() == Material.IRON_BLOCK &&
                    b7.getType() == Material.IRON_BLOCK && b8.getType() == Material.IRON_BLOCK && b9.getType() == Material.IRON_BLOCK &&
                    fr1 != null && fr2 != null && fr3 != null &&
                    bt1.getType() == Material.WALL_SIGN && bt2.getType() == Material.WALL_SIGN) {*/
                // Проверяем текст таблички
                Sign s = (Sign) bt1.getState();
                Sign s2 = (Sign) bt2.getState();
                // Обновление табдичек
                res = updateSigns(player, s, s2);
            }
        }
        // Z+ (X+, Y+)
        else if (z < za) {
            Location l9 = new Location(player.getWorld(), xa, ya, za); Block b9 = l9.getBlock();
            Location l8 = new Location(player.getWorld(), xa+1, ya, za); Block b8 = l8.getBlock();
            Location l7 = new Location(player.getWorld(), xa+2, ya, za); Block b7 = l7.getBlock();
            Location l6 = new Location(player.getWorld(), xa, ya+1, za); Block b6 = l6.getBlock();
            Location l5 = new Location(player.getWorld(), xa+1, ya+1, za); Block b5 = l5.getBlock();
            Location l4 = new Location(player.getWorld(), xa+2, ya+1, za); Block b4 = l4.getBlock();
            Location l3 = new Location(player.getWorld(), xa, ya+2, za); Block b3 = l3.getBlock();
            Location l2 = new Location(player.getWorld(), xa+1, ya+2, za); Block b2 = l2.getBlock();
            Location l1 = new Location(player.getWorld(), xa+2, ya+2, za); Block b1 = l1.getBlock();
            // Проверяем рамки
            Location lb3 = new Location(player.getWorld(), xa, y+1, z); ItemFrame fr3 = getFrame(lb3);
            Location lb2 = new Location(player.getWorld(), xa+1, y+1, z); ItemFrame fr2 = getFrame(lb2);
            Location lb1 = new Location(player.getWorld(), xa+2, y+1, z); ItemFrame fr1 = getFrame(lb1);
            // Проверяем таблички
            Location lt1 = new Location(player.getWorld(), xa, ya+2, z); Block bt1 = lt1.getBlock();
            Location lt2 = new Location(player.getWorld(), xa+2, ya+2, z); Block bt2 = lt2.getBlock();
            // Если сзади железная стена
            if (b1.getType() == Functions.getMaterial("iron_block") && b2.getType() == Material.WHITE_CONCRETE && b3.getType() == Functions.getMaterial("iron_block") &&
                    b4.getType() == Functions.getMaterial("iron_block") && b5.getType() == Functions.getMaterial("iron_block") && b6.getType() == Functions.getMaterial("iron_block") &&
                    b7.getType() == Functions.getMaterial("iron_block") && b8.getType() == Functions.getMaterial("iron_block") && b9.getType() == Functions.getMaterial("iron_block") &&
                    fr1 != null && fr2 != null && fr3 != null &&
                    bt1.getType() == Material.SPRUCE_WALL_SIGN && bt2.getType() == Material.SPRUCE_WALL_SIGN) {
            /*if (b1.getType() == Material.IRON_BLOCK && b2.getType() == Material.REDSTONE_LAMP_OFF && b3.getType() == Material.IRON_BLOCK &&
                    b4.getType() == Material.IRON_BLOCK && b5.getType() == Material.IRON_BLOCK && b6.getType() == Material.IRON_BLOCK &&
                    b7.getType() == Material.IRON_BLOCK && b8.getType() == Material.IRON_BLOCK && b9.getType() == Material.IRON_BLOCK &&
                    fr1 != null && fr2 != null && fr3 != null &&
                    bt1.getType() == Material.WALL_SIGN && bt2.getType() == Material.WALL_SIGN) {*/
                // Проверяем текст таблички
                Sign s = (Sign) bt1.getState();
                Sign s2 = (Sign) bt2.getState();
                // Обновление табдичек
                res = updateSigns(player, s, s2);
            }
        }
        // Z- (X-, Y+)
        else if (z > za) {
            Location l9 = new Location(player.getWorld(), xa, ya, za); Block b9 = l9.getBlock();
            Location l8 = new Location(player.getWorld(), xa-1, ya, za); Block b8 = l8.getBlock();
            Location l7 = new Location(player.getWorld(), xa-2, ya, za); Block b7 = l7.getBlock();
            Location l6 = new Location(player.getWorld(), xa, ya+1, za); Block b6 = l6.getBlock();
            Location l5 = new Location(player.getWorld(), xa-1, ya+1, za); Block b5 = l5.getBlock();
            Location l4 = new Location(player.getWorld(), xa-2, ya+1, za); Block b4 = l4.getBlock();
            Location l3 = new Location(player.getWorld(), xa, ya+2, za); Block b3 = l3.getBlock();
            Location l2 = new Location(player.getWorld(), xa-1, ya+2, za); Block b2 = l2.getBlock();
            Location l1 = new Location(player.getWorld(), xa-2, ya+2, za); Block b1 = l1.getBlock();
            // Проверяем рамки
            Location lb3 = new Location(player.getWorld(), xa, y+1, z); ItemFrame fr3 = getFrame(lb3);
            Location lb2 = new Location(player.getWorld(), xa-1, y+1, z); ItemFrame fr2 = getFrame(lb2);
            Location lb1 = new Location(player.getWorld(), xa-2, y+1, z); ItemFrame fr1 = getFrame(lb1);
            // Проверяем таблички
            Location lt1 = new Location(player.getWorld(), xa, ya+2, z); Block bt1 = lt1.getBlock();
            Location lt2 = new Location(player.getWorld(), xa-2, ya+2, z); Block bt2 = lt2.getBlock();
            // Если сзади железная стена
            if (b1.getType() == Functions.getMaterial("iron_block") && b2.getType() == Material.WHITE_CONCRETE && b3.getType() == Functions.getMaterial("iron_block") &&
                    b4.getType() == Functions.getMaterial("iron_block") && b5.getType() == Functions.getMaterial("iron_block") && b6.getType() == Functions.getMaterial("iron_block") &&
                    b7.getType() == Functions.getMaterial("iron_block") && b8.getType() == Functions.getMaterial("iron_block") && b9.getType() == Functions.getMaterial("iron_block") &&
                    fr1 != null && fr2 != null && fr3 != null &&
                    bt1.getType() == Material.SPRUCE_WALL_SIGN && bt2.getType() == Material.SPRUCE_WALL_SIGN) {
            /*if (b1.getType() == Material.IRON_BLOCK && b2.getType() == Material.REDSTONE_LAMP_OFF && b3.getType() == Material.IRON_BLOCK &&
                    b4.getType() == Material.IRON_BLOCK && b5.getType() == Material.IRON_BLOCK && b6.getType() == Material.IRON_BLOCK &&
                    b7.getType() == Material.IRON_BLOCK && b8.getType() == Material.IRON_BLOCK && b9.getType() == Material.IRON_BLOCK &&
                    fr1 != null && fr2 != null && fr3 != null &&
                    bt1.getType() == Material.WALL_SIGN && bt2.getType() == Material.WALL_SIGN) {*/
                // Проверяем текст таблички
                Sign s = (Sign) bt1.getState();
                Sign s2 = (Sign) bt2.getState();
                // Обновление табдичек
                res = updateSigns(player, s, s2);
            }
        }


        return res;
    }


}
