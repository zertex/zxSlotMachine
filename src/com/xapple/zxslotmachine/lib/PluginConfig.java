package com.xapple.zxslotmachine.lib;

import com.xapple.zxslotmachine.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by Egorka on 10.10.2015.
 */
public class PluginConfig {

    private static FileConfiguration storage;

    static {
        storage = Main.get().getConfig();
    }



    public static void updateConfig() {
        if (!storage.isSet("used_blocks.iron_block")) {
            storage.set("used_blocks.iron_block", "iron_block");
        }
        if (!storage.isSet("used_blocks.diamond_ore")) {
            storage.set("used_blocks.diamond_ore","diamond_ore");
        }
        if (!storage.isSet("used_blocks.emerald_ore")) {
            storage.set("used_blocks.emerald_ore","emerald_ore");
        }
        if (!storage.isSet("used_blocks.gold_ingot")) {
            storage.set("used_blocks.gold_ingot","gold_ingot");
        }
        if (!storage.isSet("used_blocks.apple")) {
            storage.set("used_blocks.apple","apple");
        }
        if (!storage.isSet("used_blocks.golden_apple")) {
            storage.set("used_blocks.golden_apple","golden_apple");
        }

        if (!storage.isSet("used_blocks.redstone_block")) {
            storage.set("used_blocks.redstone_block","redstone_block");
        }
        if (!storage.isSet("used_blocks.cake")) {
            storage.set("used_blocks.cake","cake");
        }
        if (!storage.isSet("used_blocks.iron_ingot")) {
            storage.set("used_blocks.iron_ingot","iron_ingot");
        }
        if (!storage.isSet("used_blocks.diamond")) {
            storage.set("used_blocks.diamond","diamond");
        }
        if (!storage.isSet("used_blocks.emerald")) {
            storage.set("used_blocks.emerald","emerald");
        }
        if (!storage.isSet("used_blocks.melon")) {
            storage.set("used_blocks.melon","melon");
        }
        if (!storage.isSet("used_blocks.lava_bucket")) {
            storage.set("used_blocks.lava_bucket","lava_bucket");
        }
        if (!storage.isSet("used_blocks.water_bucket")) {
            storage.set("used_blocks.water_bucket","water_bucket");
        }
        if (!storage.isSet("used_blocks.raw_fish")) {
            storage.set("used_blocks.raw_fish","raw_fish");
        }
        if (!storage.isSet("used_blocks.ender_pearl")) {
            storage.set("used_blocks.ender_pearl","ender_pearl");
        }
        if (!storage.isSet("used_blocks.nether_star")) {
            storage.set("used_blocks.nether_star","nether_star");
        }
        if (!storage.isSet("broadcast_after_amount")) {
            storage.set("broadcast_after_amount",100);
        }
        if (!storage.isSet("activate_use")) {
            storage.set("activate_use",false);
        }
        if (!storage.isSet("single_play")) {
            storage.set("single_play",false);
        }
        if (!storage.isSet("rotation_sound")) {
            storage.set("rotation_sound","CLICK");
        }

        if (!storage.isSet("wins.factor9")) {
            storage.set("wins.factor9",1666);
        }
        if (!storage.isSet("wins.factor8")) {
            storage.set("wins.factor8",300);
        }
        if (!storage.isSet("wins.factor7")) {
            storage.set("wins.factor7",100);
        }
        if (!storage.isSet("wins.factor6")) {
            storage.set("wins.factor6",50);
        }
        if (!storage.isSet("wins.factor5")) {
            storage.set("wins.factor5",25);
        }
        if (!storage.isSet("wins.factor4")) {
            storage.set("wins.factor4",12);
        }
        if (!storage.isSet("wins.factor3")) {
            storage.set("wins.factor3",12);
        }
        if (!storage.isSet("wins.factor2")) {
            storage.set("wins.factor2",6);
        }
        if (!storage.isSet("wins.factor1")) {
            storage.set("wins.factor1",1);
        }
        if (!storage.isSet("sounds")) {
            storage.set("sounds",true);
        }


        /*if (storage.isSet("fill-chests")) {
            Boolean fill = storage.getBoolean("fill-chests");
            storage.set("fill-empty-chests", fill);
            storage.set("fill-populated-chests", fill);
            storage.set("fill-chests", null);
        }
        if (!storage.isSet("lobby.radius")) {
            storage.set("lobby.radius", 0);
        }
        if (storage.isSet("island-size")) {
            storage.set("island-size", null);
        }
        if (!storage.isSet("island-buffer")) {
            storage.set("island-buffer", 5);
        }
        if (!storage.isSet("disable-kits")) {
            storage.set("disable-kits", false);
        }
        if (!storage.isSet("enable-soundeffects")) {
            storage.set("enable-soundeffects", false);
        }*/
        saveConfig();
    }

    private static boolean saveConfig() {
        File file = new File(Main.get().getDataFolder(), "config.yml");
        try {
            Main.get().getConfig().save(file);
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

}
