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
    public static final int FIGHTER_ORIGINAL_HEIGHT = 150;
    public static final int PLAYER_ORIGINAL_X = 20;
    public static final int AI_ORIGINAL_X = 700;
    public static final int AI_MOVING_SPEED = 3;
    public static final int AI_MOVING_SPEED_WHILE_ATTACK = 5;
    public static final int AI_MOVING_SPEED_DODGE = 7;
    public static final int PLAYER_MOVING_SPEED = 10;

    public static final int PLAYER_DAMAGE = 17;
    public static final int AI_DAMAGE = 17;

    public static final float FIGHTER_ATTACK_DURATION = 0.7f;
    public static final float FIGHTER_JUMP_DURATION = 0.5f; //TODO erneuern
    //////////////////////////////////////////////////////////////////////

    //AI

    //chance to block a normal attack from the player
    public static final int AI_BLOCK_CHANCE = 40;
    //chance to dodge an attack from above from the player
    public static final int AI_DODGE_ATTACK_FROM_ABOVE_CHANCE = 40;
    //chance to dodge an attack from underneath from the player
    public static final int AI_DODGE_ATTACK_FROM_UNDERNEATH_CHANCE = 40;
    //chance that AI will block randomly
    public static final int AI_BLOCK_CHANCE_RANDOM = 5;
    //normal attack chance on the ground
    public static final int AI_ATTACK_CHANCE = 40;
    //chance that AI will jump and attack player from above
    public static final int AI_ATTACK_JUMP_CHANCE = 40;
    //chance when player is jumping that AI will jump and attack upwards
    public static final int AI_COUNTER_ATTACK_JUMP_CHANCE = 5;


    public static final int AI_HEAD_TOWARDS_PLAYER_CHANCE = 40;
    //standard move = moving left/right alternately
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
