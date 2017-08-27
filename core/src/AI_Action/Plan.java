package AI_Action;

import Fighter.Fighter;

/**
 * Created by david on 27.08.2017.
 */

/*
plan:
    0 = stand still / do nothing
    1 = walk left
    2 = walk right
    3 = duck
    4 = jump
    5 = attack
    6 = attackUp
    7 = attackDown
    8 = block
 */

public class Plan
{
    private boolean executed;
    private boolean enableSecondMovement;
    private Fighter.FighterMovementState movement;
    private Fighter.FighterMovementState secondMovement;
    private Fighter.FighterFightingState fighting;


    public Plan()
    {
        executed = false;
        enableSecondMovement = false;
    }

    public Plan (Fighter.FighterMovementState move, Fighter.FighterFightingState fight)
    {
        executed = false;
        enableSecondMovement = false;
        movement = move;
        fighting = fight;
    }

    public Plan (Fighter.FighterMovementState move, Fighter.FighterMovementState secondMove, Fighter.FighterFightingState fight)
    {
        executed = false;
        enableSecondMovement = true;
        movement = move;
        secondMovement = secondMove;
        fighting = fight;
    }

    public void setExecuted(boolean state)
    {
        executed = state;
    }

    public boolean isExecuted()
    {
        return executed;
    }

    public boolean isSecondMovementEnabled()
    {
        return enableSecondMovement;
    }

    public Fighter.FighterFightingState getFighting()
    {
        return fighting;
    }

    public Fighter.FighterMovementState getMovement()
    {
        return movement;
    }

    public Fighter.FighterMovementState getSecondMovement()
    {
        if(isSecondMovementEnabled())
            return secondMovement;
        else
            return null;
    }
}
