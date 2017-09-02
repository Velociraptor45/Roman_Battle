package Fighter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;
import java.util.Random;

import AI_Action.Plan;
import Constants.GameValues;


public class AI extends Fighter
{
    private ArrayList<Plan> plansToExecute;
    private float timer, attackTimer, distanceToPlayer;

    public AI(int ID, TextureAtlas atlas, float xPos, float yPos)
    {
        super(atlas, xPos, yPos);
        plansToExecute = new ArrayList<Plan>();
        timer = 0;
        attackTimer = 0;
        //plansToExecute.add(getPlan(FighterMovementState.MOVINGRIGHT, FighterMovementState.MOVINGLEFT, FighterFightingState.NONE));
        //plansToExecute.add(getPlan(FighterMovementState.JUMPING, FighterFightingState.NONE));
    }

    //central method that is called every loop in GameClass.render() and that manages the AI actions
    public void act(Player player, float delta)
    {

        timer += delta;
        attackTimer += delta;

        checkForExecutedPlans();

        if(plansToExecute.size() > 0)
        {
            //a plan is given and will be checked if it's up to date
            plansToExecute.get(0).addDuration(delta);
            isUpToDate(plansToExecute.get(0), player);
        }
        else
        {
            //no plan given and new one will be calculated
            calculatePlanForCurrentSituation(player, getDistanceToPlayer(player));
        }
        updateCurrentStates(plansToExecute.get(0));

        executePlan(getDistanceToPlayer(player));

        ///////////////TODO austauschen
        /*Plan currentPlan = plansToExecute.get(0);
        currentPlan.addDuration(delta);
        setMovementState(currentPlan.getMovement());
        executePlan();*/
    }

    private void isUpToDate(Plan currentPlan, Player player)
    {
        distanceToPlayer = getDistanceToPlayer(player);
        if (distanceToPlayer >= GameValues.AI_START_FIGHTING_DISTANCE)
        {
            //AI is too far away and has to decrease the distance to the player
            if(!(isNextMoveLeftOrRight(currentPlan) && !currentPlan.isSecondMovementEnabled()))
            {
                //AI will head towards player
                calculatePlanForCurrentSituation(player, distanceToPlayer);
            }
        } else if(distanceToPlayer <= GameValues.AI_START_ATTCKING_DISTANCE)
        {
            //distance to player is small, chance to attack is given
            switch(getCurrentMovement(currentPlan))
            {
                case MOVINGLEFT:
                case MOVINGRIGHT:
                    if(isOnGround())
                    {
                        if(getCurrentFightingMove(currentPlan) == FighterFightingState.ATTACK)
                        {
                            //AI is attacking
                        }
                        else if(currentPlan.isSecondMovementEnabled())
                        {
                            if(currentPlan.getExecutionTime() >= GameValues.AI_MIN_TIME_STANDARD_MOVE)
                            {
                                if(shouldExecute(GameValues.AI_SHOULD_CHANGE_MOVE_CHANCE_MEDIUM_DISTANCE))
                                {
                                    calculatePlanForCurrentSituation(player, distanceToPlayer);
                                }
                            }
                        }
                        else
                        {
                            calculatePlanForCurrentSituation(player, distanceToPlayer);
                        }
                    }
                    else
                    {
                        if(!(currentPlan.getFighting() == FighterFightingState.ATTACK_DOWN))
                        {
                            //AI attacks from above
                        }
                    }
                    break;
                case JUMPING:

                    break;
                case DUCKING:

                    break;
                case STANDING:
                    if(currentPlan.getExecutionTime() >= GameValues.AI_MIN_STANDING_TIME)
                    {
                        if(shouldExecute(GameValues.AI_SHOULD_CHANGE_MOVE_CHANCE_MEDIUM_DISTANCE))
                        {
                            calculatePlanForCurrentSituation(player, distanceToPlayer);
                        }
                    }
                    break;
                default:
                    calculatePlanForCurrentSituation(player, distanceToPlayer);
                    break;
            }
        }
        else
        {
            //AI is somewhere between near and far away
            if(getCurrentMovement(currentPlan) != null)
            {
                switch (getCurrentMovement(currentPlan))
                {
                    case MOVINGRIGHT:
                    case MOVINGLEFT:
                        if(currentPlan.isSecondMovementEnabled())
                        {
                            //standard move is executed
                            if(currentPlan.getExecutionTime() >= GameValues.AI_MIN_TIME_STANDARD_MOVE)
                            {
                                if(shouldExecute(GameValues.AI_SHOULD_CHANGE_MOVE_CHANCE_MEDIUM_DISTANCE))
                                {
                                    calculatePlanForCurrentSituation(player, distanceToPlayer);
                                }
                            }
                        }
                        else
                        {
                            //ai is moving in just one direction
                            if(currentPlan.getExecutionTime() >= GameValues.AI_MIN_TIME_MOVING_IN_DIRECTION)
                            {
                                if(shouldExecute(GameValues.AI_SHOULD_CHANGE_MOVE_CHANCE_MEDIUM_DISTANCE))
                                {
                                    calculatePlanForCurrentSituation(player, distanceToPlayer);
                                }
                            }
                        }
                        break;
                    case JUMPING:
                        if(currentPlan.isSecondMovementEnabled())
                        {
                            //ai jumps in a certain direction
                            if(currentPlan.getSecondMovement() == FighterMovementState.MOVINGLEFT)
                            {
                                //jumps to left side

                            }
                            else
                            {
                                //jumps to right side

                            }
                        }
                        else
                        {
                            //ai is just jumping in the air

                        }
                        break;
                    case DUCKING:
                        //ai is ducking, wait until it's over

                        break;
                    default:
                        if(getY() > GameValues.FIGHTER_ORIGINAL_HEIGHT)
                        {
                            //ai is still in the air
                        }
                        break;
                }
            }
            else
            {
                calculatePlanForCurrentSituation(player, distanceToPlayer);
            }
        }
    }


    /*
        isUpToDate() calls this method when the current plan is not up to date because the
        situation has dramatically changed;
        a completely new plan will be calculated
    */
    private void calculatePlanForCurrentSituation(Player player, float distance)
    {
        if(distance <= GameValues.AI_START_ATTCKING_DISTANCE)
        {
            //AI is close to the player
            if (player.isOnGround())
            {
                //player is not jumping right now
                if (isOnGround())
                {
                    //AI is not jumping right now
                    if (attackTimer >= GameValues.AI_TIMER_ATTACK && shouldExecute(GameValues.AI_ATTACK_CHANCE))
                    {
                        //AI will attack
                        if (shouldExecute(GameValues.AI_ATTACK_JUMP_CHANCE))
                        {
                            //AI will do jump attack
                            if (facingDirection == facingRight)
                            {
                                resetAndAddPlanToArray(getPlan(FighterMovementState.JUMPING, FighterMovementState.MOVINGRIGHT, FighterFightingState.NONE));
                                addPlanToArray(getPlan(FighterMovementState.MOVINGRIGHT, FighterFightingState.ATTACK_DOWN));
                            }
                            else
                            {
                                resetAndAddPlanToArray(getPlan(FighterMovementState.JUMPING, FighterMovementState.MOVINGLEFT, FighterFightingState.NONE));
                                addPlanToArray(getPlan(FighterMovementState.MOVINGLEFT, FighterFightingState.ATTACK_DOWN));
                            }
                        }
                        else
                        {
                            //AI will do normal attack
                            if (facingDirection == facingRight)
                            {
                                resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGRIGHT, FighterFightingState.ATTACK));
                            }
                            else
                            {
                                resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGLEFT, FighterFightingState.ATTACK));
                            }
                        }
                        attackTimer = 0;
                    }
                    else
                    {
                        //AI will not attack and eventually move instead
                        if (shouldExecute(GameValues.AI_STANDARD_MOVE_CHANCE_CLOSE))
                        {
                            //standard move
                            resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGRIGHT, FighterMovementState.MOVINGLEFT, FighterFightingState.NONE));
                        }
                        else if (shouldExecute(GameValues.AI_BLOCK_CHANCE_RANDOM))
                        {
                            //random block
                            resetAndAddPlanToArray(getPlan(FighterMovementState.STANDING, FighterFightingState.BLOCK));
                        }
                        else
                        {
                            //standing still
                            resetAndAddPlanToArray(getPlan(FighterMovementState.STANDING, FighterFightingState.NONE));
                        }
                    }
                }
                else
                {
                    //ai is jumping
                    if (facingDirection == facingRight)
                    {
                        resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGRIGHT, FighterFightingState.ATTACK_DOWN));
                    }
                    else
                    {
                        resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGLEFT, FighterFightingState.ATTACK_DOWN));
                    }
                }
            }
        }
        else //if(distance > GameValues.AI_START_FIGHTING_DISTANCE)
        {
            if(facingDirection == facingRight)
            {
                resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGRIGHT, FighterFightingState.NONE));
            }
            else
            {
                resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGLEFT, FighterFightingState.NONE));
            }
        }
        //Gdx.app.log("New Plan", plansToExecute.get(0).getMovement().toString() + " " + plansToExecute.get(0).getSecondMovement() + " " + plansToExecute.get(0).getFighting().toString() + " " + getDistanceToPlayer(player));
    }

    private boolean canBeExecuted(Player player, Plan plan)
    {
        //kann aktuelle Anweisung so überhaupt ausgeführt werden?
        return true;
    }

    private boolean shouldExecute(int chance)
    {
        Random r = new Random();
        return (r.nextInt(100) <= chance);
    }


    /*
        depending on the current movement and fighting state of the ai this method executes
         the movement
     */
    private void executePlan(float distanceToPlayer)
    {
        if(movementState != FighterMovementState.JUMPING && getY() > GameValues.FIGHTER_ORIGINAL_HEIGHT)
        {
            fall();
        }
        switch (movementState)
        {
            //TODO vervollständigen
            case MOVINGRIGHT:
                if(!(fightingState == FighterFightingState.ATTACK || fightingState == FighterFightingState.ATTACK_DOWN || fightingState == FighterFightingState.ATTACK_UP))
                {
                    if(facingDirection == facingRight)
                    {
                        if(distanceToPlayer - GameValues.AI_MOVING_SPEED > 0)
                        {
                            //makes sure that AI doesn't walk through player
                            moveRight(GameValues.AI_MOVING_SPEED);
                        }
                    }
                    else
                    {
                        moveRight(GameValues.AI_MOVING_SPEED);
                    }
                }
                else
                {
                    if(facingDirection == facingRight)
                    {
                        if(distanceToPlayer - GameValues.AI_MOVING_SPEED_WHILE_ATTACK >= - GameValues.AI_MOVING_SPEED_WHILE_ATTACK)
                        {
                            //makes sure that AI doesn't walk through player
                            moveRight(GameValues.AI_MOVING_SPEED_WHILE_ATTACK);
                        }
                    }
                    else
                    {
                        moveRight(GameValues.AI_MOVING_SPEED_WHILE_ATTACK);
                    }
                }
                break;
            case MOVINGLEFT:
                if(!(fightingState == FighterFightingState.ATTACK || fightingState == FighterFightingState.ATTACK_DOWN || fightingState == FighterFightingState.ATTACK_UP))
                {
                    if(facingDirection == facingLeft)
                    {
                        if(distanceToPlayer - GameValues.AI_MOVING_SPEED > 0)
                        {
                            //makes sure that AI doesn't walk through player
                            moveLeft(GameValues.AI_MOVING_SPEED);
                        }
                    }
                    else
                    {
                        moveLeft(GameValues.AI_MOVING_SPEED);
                    }
                }
                else
                {
                    if(facingDirection == facingLeft)
                    {
                        if(distanceToPlayer - GameValues.AI_MOVING_SPEED_WHILE_ATTACK >= - GameValues.AI_MOVING_SPEED_WHILE_ATTACK)
                        {
                            //makes sure that AI doesn't walk through player
                            moveLeft(GameValues.AI_MOVING_SPEED_WHILE_ATTACK);
                        }
                    }
                    else
                    {
                        moveLeft(GameValues.AI_MOVING_SPEED_WHILE_ATTACK);
                    }
                }
                break;
            case JUMPING:
                if(!jump())
                {
                    plansToExecute.get(0).setMovement(FighterMovementState.STANDING);
                }
                break;
            default:
                //Gdx.app.log("executePlan", "not right state for execution");
                break;
        }
        switch(fightingState)
        {
            case ATTACK:
                //Gdx.app.log("Fight", "Attack!");
                break;
            default:
                break;
        }
    }

    private void terminate(Plan plan)
    {
        if(plansToExecute.size() > 0)
        {
            plan.setExecuted(true);
        }
    }

    private void checkForExecutedPlans()
    {
        for(int i = 0; i < plansToExecute.size(); i++)
        {
            if(!isOnGround() && plansToExecute.get(i).getMovement() == FighterMovementState.STANDING && plansToExecute.get(i).getFighting() == FighterFightingState.NONE)
            {
                terminate(plansToExecute.get(i));
            }

            if(plansToExecute.get(i).isExecuted())
            {
                plansToExecute.remove(i);
            }
            else
            {
                break;
            }
        }
    }

    private Fighter.FighterMovementState getCurrentMovement(Plan plan)
    {
        if(plansToExecute.size() > 0)
        {
            return plan.getMovement();
        }
        else
        {
            return null;
        }
    }

    private Fighter.FighterFightingState getCurrentFightingMove(Plan plan)
    {
        if(plansToExecute.size() > 0)
        {
            return plan.getFighting();
        }
        else
        {
            return null;
        }
    }

    private void updateCurrentStates(Plan plan)
    {
        movementState = plan.getMovement();
        fightingState = plan.getFighting();
    }

    private boolean isNextMoveLeftOrRight(Plan plan)
    {
        return (getCurrentMovement(plan) == FighterMovementState.MOVINGRIGHT || getCurrentMovement(plan) == FighterMovementState.MOVINGLEFT);
    }

    private Plan getPlan(Fighter.FighterMovementState move, Fighter.FighterFightingState fight)
    {
        return new Plan(move, fight);
    }

    private Plan getPlan(Fighter.FighterMovementState move, Fighter.FighterMovementState secMove, Fighter.FighterFightingState fight)
    {
        return new Plan(move, secMove, fight);
    }

    private void addPlanToArray(Plan plan)
    {
        plansToExecute.add(plan);
    }

    private void resetAndAddPlanToArray(Plan plan)
    {
        plansToExecute.clear();
        plansToExecute.add(plan);
    }

    private float getDistanceToPlayer(Player player)
    {
        if(facingDirection == facingLeft)
        {
            return getX() - (player.getX() + getRegionWidth());
        }
        else
        {
            return player.getX() - (getX() + getRegionWidth());
        }

    }


    @Override
    public void setMovementState(FighterMovementState state)
    {
        if(!(state == FighterMovementState.JUMPING && getY() > GameValues.FIGHTER_ORIGINAL_HEIGHT))
        {
            movementState = state;
        }
    }
}
