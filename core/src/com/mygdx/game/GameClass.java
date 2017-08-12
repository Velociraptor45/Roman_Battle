package com.mygdx.game;

import Fighter.AI;
import Fighter.Player;

/**
 * Created by david on 12.08.2017.
 */

public class GameClass {

    private AI enemy;
    private Player player;

    /*
        The enemyID is the ID of the enemy so we can identify different opponents if we decide to implement more than one
        If we have only one enemy his index is 0
     */
    public GameClass(int enemyID)
    {
        init(enemyID);
    }

    public void init(int enemyIndex)
    {
        enemy = new AI(enemyIndex);
        player = new Player();
    }
}
