package com.xapple.zxslotmachine.lib;

//import com.sun.deploy.uitoolkit.ui.ConsoleTraceListener;
import com.xapple.zxslotmachine.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Egorka on 20.01.2016.
 */
public class Functions {
    public static Material getMaterial(String BlockName) {
        for (int i = 0; i< Main.blocks.size(); i++) {
            if (Main.blocks.get(i).BlockName.equalsIgnoreCase(BlockName)) {
                return Main.blocks.get(i).BlockType;
            }
        }
        return Material.AIR;
    }


    public static void createActivateTable() {
        try {
            Statement s = Main.getSqlDatabase().getConnection().createStatement();
            s.executeUpdate("CREATE TABLE IF NOT EXISTS activates(" +
                    "id VARCHAR(36) NOT NULL," +
                    "active INT NOT NULL," +
                    "PRIMARY KEY(id)" +
                    ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createActivateUsersTable() {
        try {
            Statement s = Main.getSqlDatabase().getConnection().createStatement();
            s.executeUpdate("CREATE TABLE IF NOT EXISTS users(" +
                    "player VARCHAR(36) NOT NULL," +
                    "state INT NOT NULL," +
                    "PRIMARY KEY(player)" +
                    ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createLogTable() {
        try {
            Statement s = Main.getSqlDatabase().getConnection().createStatement();
            s.executeUpdate("CREATE TABLE IF NOT EXISTS logs(" +
                    "games INT NOT NULL," +
                    "wins INT NOT NULL," +
                    "maxwin INT NOT NULL," +
                    "amount INT NOT NULL," +
                    "name VARCHAR(16) NOT NULL," +
                    "PRIMARY KEY(name)" +
                    ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Boolean checkActivatedSlots(String id) {
        boolean res = false;
        try {
            PreparedStatement ps = Main.getSqlDatabase().getConnection().prepareStatement("SELECT active FROM activates WHERE id = '"+id+"'");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("active") == 1) {
                    res = true;
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return res;
    }


    // Деактивация автомата
    public static void deactivateMachine(String id) {
        try {
            PreparedStatement ps2 = Main.getSqlDatabase().getConnection().prepareStatement("DELETE FROM activates WHERE id = ?");
            ps2.setString(1, id);
            ps2.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    // Включаем автомат
    public static void enableSlots(String id) {
        try {
            PreparedStatement ps = Main.getSqlDatabase().getConnection().prepareStatement("INSERT OR IGNORE INTO activates (id, active) VALUES (?, 1)");
            ps.setString(1, id);
            ps.executeUpdate();

            PreparedStatement ps2 = Main.getSqlDatabase().getConnection().prepareStatement("UPDATE activates SET active = 1 WHERE id = ?");
            ps2.setString(1, id);
            ps2.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    // Активирован ли автомат
    public static Boolean getMachineActivateState(String id) {
        boolean res = false;
        int state = 0;
        try {
            PreparedStatement ps = Main.getSqlDatabase().getConnection().prepareStatement("SELECT active FROM activates WHERE id = '"+id+"'");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                state = rs.getInt("active");
                if (state == 1) {
                    res = true;
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return res;
    }

    // Активирован ли пользователь
    public static Boolean getUserActivateState(Player player) {
        boolean res = false;
        int state = 0;
        try {
            PreparedStatement ps = Main.getSqlDatabase().getConnection().prepareStatement("SELECT state FROM users WHERE player = '"+player.getName()+"'");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                state = rs.getInt("state");
                if (state == 1) {
                    res = true;
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return res;
    }

    // Деактивация пользователя
    public static void deactivateUser(Player player) {
        try {
            PreparedStatement ps2 = Main.getSqlDatabase().getConnection().prepareStatement("UPDATE users SET state = 0 WHERE player = ?");
            ps2.setString(1, player.getName());
            ps2.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    // Активация пользователя
    public static void activateUser(Player player) {
        try {
            PreparedStatement ps = Main.getSqlDatabase().getConnection().prepareStatement("INSERT OR IGNORE INTO users (player, state) VALUES (?, 1)");
            ps.setString(1, player.getName());
            ps.executeUpdate();

            PreparedStatement ps2 = Main.getSqlDatabase().getConnection().prepareStatement("UPDATE users SET state = 1 WHERE player = ?");
            ps2.setString(1, player.getName());
            ps2.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void updateSqlLog(Player player, Integer win, Integer amount) {
        try {
            PreparedStatement ps = Main.getSqlDatabase().getConnection().prepareStatement("INSERT OR IGNORE INTO logs (name, games, wins, maxwin, amount) VALUES (?, 0, 0, 0, 0)");
            ps.setString(1, player.getName());
            ps.executeUpdate();

            Integer maxwin = 0;
            PreparedStatement ps3 = Main.getSqlDatabase().getConnection().prepareStatement("SELECT maxwin FROM logs WHERE name = '"+player.getName()+"'");
            ResultSet rs = ps3.executeQuery();
            if (rs.next()) {
                maxwin = rs.getInt("maxwin");
            }

            if (amount > maxwin) {
                maxwin = amount;
            };

            PreparedStatement ps2 = Main.getSqlDatabase().getConnection().prepareStatement("UPDATE logs SET games = games+1, wins = wins + ?, amount = amount + ?, maxwin = ? WHERE name = ?");
            ps2.setInt(1, win);
            ps2.setInt(2, amount);
            ps2.setInt(3, maxwin);
            ps2.setString(4, player.getName());
            ps2.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    // Получение топ игроков / stat = gamens, win, lose, amount, maxwin
    public static String getTopPlayers(String stat, Integer num) {
        if (num > 50 || num <= 0) {num = 10;}
        String top = "";
        String sql = "";

        if (stat.equalsIgnoreCase("maxwin")) {
            sql = "SELECT name, maxwin as val FROM logs ORDER BY maxwin DESC LIMIT "+String.valueOf(num);
            top = top + new Messaging.MessageFormatter().format("game.top-maxwin")+"\n"+new Messaging.MessageFormatter().format("&7--------------------------")+"\n";
        }
        else if (stat.equalsIgnoreCase("amount")) {
            sql = "SELECT name, amount as val FROM logs ORDER BY amount DESC LIMIT "+String.valueOf(num);
            top = top + new Messaging.MessageFormatter().format("game.top-amount")+"\n"+new Messaging.MessageFormatter().format("&7--------------------------")+"\n";
        }
        else if (stat.equalsIgnoreCase("games")) {
            sql = "SELECT name, games as val FROM logs ORDER BY games DESC LIMIT "+String.valueOf(num);
            top = top + new Messaging.MessageFormatter().format("game.top-games")+"\n"+new Messaging.MessageFormatter().format("&7--------------------------")+"\n";
        }
        else if (stat.equalsIgnoreCase("lose")) {
            sql = "SELECT name, games-wins as val FROM logs ORDER BY games-wins DESC LIMIT "+String.valueOf(num);
            top = top + new Messaging.MessageFormatter().format("game.top-lose")+"\n"+new Messaging.MessageFormatter().format("&7--------------------------")+"\n";
        }
        else {
        //else if (stat.equalsIgnoreCase("win")) {
            sql = "SELECT name, wins as val FROM logs ORDER BY wins DESC LIMIT "+String.valueOf(num);
            top = top + new Messaging.MessageFormatter().format("game.top-win")+"\n"+new Messaging.MessageFormatter().format("&7--------------------------")+"\n";
        }

        try {
            PreparedStatement ps = Main.getSqlDatabase().getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Integer count = 1;
            while ( rs.next() ) {
                if (!rs.getString("name").isEmpty()) {
                    top = top + "\n" + new Messaging.MessageFormatter().setVariable("amount", String.valueOf(rs.getInt("val"))).setVariable("player", rs.getString("name")).setVariable("count", String.valueOf(count)).format("game.top-line");
                    count++;
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return top;
    }

    public static boolean isStringInt(String s) {
        try{
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex){
            return false;
        }
    }

    public static void prepareBlocksData() {
        String[] types = { "iron_block", "diamond_ore", "emerald_ore", "gold_ore", "gold_ingot", "apple", "golden_apple", "redstone_block", "cake", "iron_ingot", "diamond", "emerald", "melon",
                "lava_bucket", "water_bucket", "raw_fish", "ender_pearl", "nether_star" };

        for (Integer i = 0; i < types.length; i++) {
            BlockData bData = new BlockData();
            bData.BlockName = types[i];
            String bType = Main.get().getConfig().getString("used_blocks."+types[i]);
            if (bType != null) {
                //Main.get().getLogger().info("MATERIAL: "+bType.toUpperCase());
                bData.BlockType = Material.getMaterial(bType.toUpperCase());
                if (bData.BlockType == null) {
                    Main.get().getLogger().info("WRONG MATERIAL NAME: "+bType.toUpperCase()+". Replaced by "+types[i].toUpperCase());
                    bData.BlockType = Material.getMaterial(types[i].toUpperCase());
                    //for (Integer m = 0; m < Material.values().length; m++) {
                    //    Main.get().getLogger().info(Material.values()[m].toString());
                   // }
                }

                //Main.get().getLogger().info("NOT NULL: "+bData.BlockType.toString());
            }
            else {
                //Main.get().getLogger().info("MATERIAL: "+types[i].toUpperCase());
                bData.BlockType = Material.getMaterial(types[i].toUpperCase());
                //Main.get().getLogger().info("NULL: "+bData.BlockType.toString());
            }
            Main.blocks.add(bData);
        }
    }

}
