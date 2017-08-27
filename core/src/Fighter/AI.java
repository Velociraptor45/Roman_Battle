package Fighter;

import java.util.ArrayList;
import java.util.Random;

import AI_Action.Plan;
import Constants.GameValues;


public class AI extends Fighter
{
    private ArrayList<Plan> plansToExecute;

    public AI(int ID)
    {
        plansToExecute = new ArrayList<Plan>();
    }

    //central method that is called every loop in GameClass.render() and that manages the AI actions
    public void act(Player player)
    {

    }

    private void calculatePlan(Player player, Plan deniedPlan)
    {
        if(facingDirection == facingRight)
        {
            if(player.getX() - (getX() + getWidth()) > GameValues.AI_START_FIGHTING_DISTANCE)
            {
                plansToExecute.clear();
                plansToExecute.add(new Plan(FighterMovementState.MOVINGRIGHT, FighterFightingState.NONE));
            }
            else
            {

            }
        }
    }

    private boolean isUpToDate(Plan currentPlan, Player player)
    {
        switch(currentPlan.getMovement())
        {
            case MOVINGRIGHT:
                if(player.getX() - (getX() + getWidth()) > GameValues.AI_START_FIGHTING_DISTANCE)
                {
                    //AI is too far away and has to decrease the distance to the player
                    return true;
                }
                else if(currentPlan.isSecondMovementEnabled() && currentPlan.getSecondMovement() == FighterMovementState.MOVINGLEFT)
                {
                    //AI is in range and does its standard left/right move

                    Random r = new Random();
                    if(r.nextInt(100) <= GameValues.AI_ATTACK_CHANCE)
                    {
                        return false;
                    }
                }
                break;
        }


        return false;
    }
}
