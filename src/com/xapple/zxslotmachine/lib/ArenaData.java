package com.xapple.zxslotmachine.lib;

import java.util.ArrayList;

/**
 * Created by Egorka on 18.10.2015.
 */
public class ArenaData {
    public String gameId = "";
    public Integer maxPlayers = 0;
    public Integer minPlayers = 0;
    public Integer waitTime = 0;
    public Integer startTime = 0;
    public Integer gameTime = 0;
    public Integer boundsType = 0; // 0 - квадрат, 1 - круг (Вид арены)
    // для bType = 0
    public Integer boundsHigh[] = {0,0,0}; // x,y,z
    public Integer boundsLow[] = {0,0,0}; // x,y,z
    // для bType = 1
    public Integer boundsRadius[] = {0,0,0,0}; // x,y,z, radius

    // Ожидающие начала игроки
    public ArrayList<PlayerData> waitPlayers = new ArrayList<PlayerData>();
    // Играющие игроки
    public ArrayList<PlayerData> players = new ArrayList<PlayerData>();

}
