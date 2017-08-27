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
    public static final int FIGHTER_MOVING_SPEED = 10;
    //////////////////////////////////////////////////////////////////////

    //AI
    public static final int AI_BLOCK_CHANCE = 40;
    public static final int AI_ATTACK_CHANCE = 40;

    public static final int AI_START_FIGHTING_DISTANCE = 100;
}
