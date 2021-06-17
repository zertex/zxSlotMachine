package com.xapple.zxslotmachine;

import com.xapple.zxslotmachine.commands.MainCommand;
import com.xapple.zxslotmachine.lib.*;
import com.xapple.zxslotmachine.listeners.EntityListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Egorka on 18.10.2015.
 */
public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    //public Set<GameData> games = new HashSet<GameData>();
    public static List<GameData> games = new ArrayList<>();
    public Economy eco = null;
    public static List<BlockData> blocks = new ArrayList<>();
    public static Integer broadcastAmount = 0;
    //public GameData[] games = new GameData[]{};
    //public static ArrayList<GameData> games = new ArrayList<GameData>();
    public static Boolean activateUse = false; // Требуется ли активация автомата
    private static Database database;
    public static Boolean singlePlay = false;
    public static String rotationSound = "CLICK";
    public static Boolean soundsOn = true;

    //public static Boolean activatorState = false; // Режим активации выключен
    //public static String activatorName = ""; // Имя игрока, кторорый сожет активировать


    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        instance = this;
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        PluginConfig.updateConfig();
        reloadConfig();

        setupEconomy();

        // Используется ли активация автоматов
        activateUse = getConfig().getBoolean("activate_use");

        database = new SQLite(this, getDataFolder().toString() + File.separator + "slots.db");
        database.openConnection();
        Functions.createActivateTable();
        Functions.createLogTable();
        Functions.createActivateUsersTable();

        // Check Update
        if (UpdateCheck.needUpdate()) {
            getLogger().info("Update available");
        }
        else {
            getLogger().info("No update needed");
        }

        // Check Single Play
        singlePlay = Main.get().getConfig().getBoolean("single_play");
        // Тянем звук вращения
        rotationSound = Main.get().getConfig().getString("rotation_sound");
        // Проверяем включенность звука
        soundsOn =  Main.get().getConfig().getBoolean("sounds");

        new Messaging(this);
        getCommand("zslot").setExecutor(new MainCommand());

        // Prepare Blocks
        Functions.prepareBlocksData();
        // Prepare Broadcast amount for message
        Main.broadcastAmount = Main.get().getConfig().getInt("broadcast_after_amount");

        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
        //Bukkit.getPluginManager().registerEvents(new JoinSign(), this);

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                // Смотрим games и уменьшаем всем счетчики... После завершения какждого счетчика зпускать процедуру, после 3-нго заканчивать игру
                //for (Iterator<GameData> i = Main.get().games.iterator(); i.hasNext();) {
                for (int i = 0; i < Main.games.size(); i++) {
                    //if (i.hasNext()) {
                        //GameData item = i.next();
                        GameData item = Main.games.get(i);
                        if (item.counter1 > 0) {
                            item.counter1--;
                            item.updateReels1();
                        }
                        if (item.counter2 > 0) {
                            item.counter2--;
                            item.updateReels2();
                        }
                        if (item.counter3 > 0) {
                            item.counter3--;
                            item.updateReels3();
                        }

                        if (item.counter1 == 0) {
                            item.stopReel1();
                        }
                        if (item.counter2 == 0) {
                            item.stopReel2();
                        }
                        if (item.counter3 == 0) {
                            item.stopReel3();
                        }
                    //}
                }


            }
        }, 0L, 5L);

        this.getLogger().info("Slot Machines enabled.");
    }

    private Boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            eco = economyProvider.getProvider();
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        database.closeConnection();
        this.getLogger().info("Slot Machines disabled.");
    }

    public static Database getSqlDatabase() {
        return database;
    }

    public static Main get() {
        return instance;
    }
}
