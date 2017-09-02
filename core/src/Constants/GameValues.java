package Constants;

import com.badlogic.gdx.Gdx;

/**
 * Created by david on 13.08.2017.
 */

public class GameValues
{
    public static final double DENSTIY = Gdx.graphics.getDensity();
            //160dpi: DENSITY = 1
            //120dpi: DENSITY = 0.75 -> compare graphics api

    //Fighter
    public static final int FIGHTER_HEALTH = 100;

    public static final int FIGHTER_MAX_JUMP_HEIGHT = 300;
    public static final int FIGHTER_ORIGINAL_HEIGHT = 120;
    public static final int AI_MOVING_SPEED = 3;
    public static final int AI_MOVING_SPEED_WHILE_ATTACK = 5;
    public static final int PLAYER_MOVING_SPEED = 10;

    public static final float FIGHTER_ATTACK_DURATION = 0.7f;
    //////////////////////////////////////////////////////////////////////

    //AI
    public static final int AI_BLOCK_CHANCE = 40;
    public static final int AI_BLOCK_CHANCE_RANDOM = 5;
    public static final int AI_ATTACK_CHANCE = 40;
    public static final int AI_ATTACK_JUMP_CHANCE = 40;
    public static final int AI_HEAD_TOWARDS_PLAYER_CHANCE = 40;
    public static final int AI_STANDARD_MOVE_CHANCE_CLOSE = 40;

    public static final int AI_JUMP_CHANCE_MEDIUM_DISTANCE = 10;

    public static final int AI_SHOULD_CHANGE_MOVE_CHANCE_MEDIUM_DISTANCE = 50;
    public static final int AI_SHOULD_CHANGE_MOVE_CHANCE_CLOSE_DISTANCE = 50;

    //how long the ai goes in one direction before changing the direction
    public static final float AI_STANDARD_MOVE_CHANGE_TIME = 0.5f;

    public static final int AI_MIN_TIME_STANDARD_MOVE = 3;
    public static final int AI_MIN_TIME_MOVING_IN_DIRECTION = 3;
    public static final int AI_MIN_STANDING_TIME = 2;

    public static final int AI_MAX_PLAN_EXECUTION_TIME = 7;

    //border between far and medium distance
    public static final int AI_START_FIGHTING_DISTANCE = 100;
    //border between medium and close distance
    public static final int AI_START_ATTCKING_DISTANCE = 40;

    public static final int AI_TIMER_ATTACK = 10;
}
