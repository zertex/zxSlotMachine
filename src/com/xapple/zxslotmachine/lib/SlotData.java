package com.xapple.zxslotmachine.lib;

import com.xapple.zxslotmachine.Main;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Egorka on 21.10.2015.
 */
public class SlotData {

    //public static Set<String> games = new HashSet<String>();

    //public static Set<Material> getReelItems() {
    public static Material[] getReelItems() {
        Set<Material> res = new HashSet<Material>();
        Random rand = new Random();
        Integer reel1 = rand.nextInt(128)+1;
        Integer reel2 = rand.nextInt(128)+1;
        Integer reel3 = rand.nextInt(128)+1;

        //if (reel3 > 128 || reel3 < 1) {
        //    Main.get().getLogger().info("REEL3 INTEGER RAND 0");
        //}

        res.add(getChanceMaterial(reel1));
        res.add(getChanceMaterial(reel2));
        res.add(getChanceMaterial(reel3));

        return new Material[] {getChanceMaterial(reel1), getChanceMaterial(reel2), getChanceMaterial(reel3)};
    }

    public static Material getChanceMaterial(Integer reel) {
        //Material res = null;
        Material res = Material.BOOK;

        if ((reel >= 1 && reel <= 2) || (reel >= 42 && reel <= 43) || (reel == 63)) {
            res = Functions.getMaterial("apple");
        }
        else if ((reel >= 8 && reel <=12) || (reel >= 31 && reel <= 35) || (reel >= 82 && reel <= 87)) {
            res = Functions.getMaterial("gold_ore");
        }
        else if (reel >= 18 && reel <= 25) {
            res = Functions.getMaterial("golden_apple");
        }
        else if ((reel >= 50 && reel <= 56)|| (reel >= 70 && reel <= 75)) {
            res = Functions.getMaterial("emerald_ore");
        }
        else if (reel >= 94 && reel <= 104) {
            res = Functions.getMaterial("diamond_ore");
        }
        else if (reel >= 116 && reel <= 117) {
            res = Functions.getMaterial("gold_ingot");
        }
        else if (reel >= 3 && reel <= 7) {
            res = Functions.getMaterial("cake");
        }
        else if (reel >= 13 && reel <= 17) {
            res = Functions.getMaterial("iron_ingot");
        }
        else if (reel >= 26 && reel <= 30) {
            res = Functions.getMaterial("nether_star");
        }
        else if (reel >= 36 && reel <= 41) {
            res = Functions.getMaterial("diamond");
        }
        else if (reel >= 44 && reel <= 49) {
            res = Functions.getMaterial("emerald");
        }
        else if (reel >= 57 && reel <= 62) {
            res = Functions.getMaterial("melon");
        }
        else if (reel >= 64 && reel <= 69) {
            res = Functions.getMaterial("redstone_block");
        }
        else if (reel >= 76 && reel <= 81) {
            res = Functions.getMaterial("lava_bucket");
        }
        else if (reel >= 88 && reel <= 93) {
            res = Functions.getMaterial("water_bucket");
        }
        else if (reel >= 105 && reel <= 115) {
            res = Functions.getMaterial("raw_fish");
        }
        else if (reel >= 118 && reel <= 128) {
            res = Functions.getMaterial("ender_pearl");
        }

        //if (res == Material.BOOK) {Main.get().getLogger().info("BOOOK: "+reel);}
        return res;
    }

}
