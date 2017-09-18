package AI_Action;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Constants.GameValues;
import Fighter.Fighter;

public class Plan
{
    private boolean executed;
    private boolean enableSecondMovement;
    private String originalFacingDirection;
    private Fighter.FighterMovementState movement;
    private Fighter.FighterMovementState secondMovement;
    private Fighter.FighterFightingState fighting;
    private float durationMovementChange, durationPlanExecution;


    public Plan()
    {
        executed = false;
        enableSecondMovement = false;
        originalFacingDirection = "";
        durationMovementChange = 0;
        durationPlanExecution = 0;
    }

    public Plan (Fighter.FighterMovementState move, Fighter.FighterFightingState fight)
    {
        this();
        movement = move;
        fighting = fight;
    }

    public Plan (Fighter.FighterMovementState move, Fighter.FighterMovementState secondMove, Fighter.FighterFightingState fight)
    {
        this();
        movement = move;
        secondMovement = secondMove;
        fighting = fight;
        enableSecondMovement = true;
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

    public void addDuration(float delta)
    {
        if(durationPlanExecution >= GameValues.AI_MAX_PLAN_EXECUTION_TIME)
        {
            setExecuted(true);
        }
        durationMovementChange += delta;
        durationPlanExecution += delta;

        if(isSecondMovementEnabled() && (durationMovementChange >= GameValues.AI_STANDARD_MOVE_CHANGE_TIME) && (movement != Fighter.FighterMovementState.JUMPING))
        {
            durationMovementChange = 0;
            changeMovement();
        }
    }

    public void setMovement(Fighter.FighterMovementState state)
    {
        movement = state;
    }

    private void changeMovement()
    {
        Fighter.FighterMovementState state = movement;
        movement = secondMovement;
        secondMovement = state;
    }

    public void setJumpInformation(String faceDir)
    {
        originalFacingDirection = faceDir;
    }

    public String getOriginalFacingDirection()
    {
        if(movement == Fighter.FighterMovementState.JUMPING)
            return originalFacingDirection;
        else
            return null;
    }

    public float getExecutionTime()
    {
        return durationPlanExecution;
    }
}
