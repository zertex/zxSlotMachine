package com.xapple.zxslotmachine.lib;

import org.bukkit.entity.Player;

/**
 * Created by Egorka on 20.10.2015.
 */

public class PlayerData {
    public Integer kills = 0; // Всего убил
    public Integer death = 0; // Всего погиб
    public Integer mobkills = 0; // Всего убил мобов
    public Integer mobdeath = 0; // Всего погиб от мобов
    // Данные за раунд
    public Integer roundKills = 0; // Всего убил
    public Integer roundDeath = 0; // Всего погиб
    public Integer roundMobkills = 0; // Всего убил мобов
    public Integer roundMobdeath = 0; // Всего погиб от мобов

    public Player player = null;
    public Integer team = 0;
    public Integer points = 0;

    public Integer scoreCount = 0; // Пустая переменная, используется для анимации scoreboard
}
