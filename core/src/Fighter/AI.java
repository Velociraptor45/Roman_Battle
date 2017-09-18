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
    //the "try..." timers make sure that the AI has just one chance per attack to block, dodge, etc. this attack of the player
    private float tryBlockTimer, tryDodgeForAttackDownTimer, tryDodgeForAttackUpTimer, tryCounterAttackTimer, attackTimer;

    public AI(int ID, TextureAtlas atlas, float xPos, float yPos)
    {
        super(atlas, xPos, yPos);
        plansToExecute = new ArrayList<Plan>();
        tryBlockTimer = 0;
        tryDodgeForAttackDownTimer = 0;
        tryDodgeForAttackUpTimer = 0;
        tryCounterAttackTimer = 0;
        attackTimer = 0;
    }

    //central method that is called every loop in GameClass.render() and that manages the AI actions
    public void act(Player player, float delta)
    {
        tryBlockTimer += delta;
        tryDodgeForAttackDownTimer += delta;
        tryDodgeForAttackUpTimer += delta;
        tryCounterAttackTimer += delta;
        attackTimer += delta;

        checkForExecutedPlans();

        if(!analysePlayerAction(player))
        {
            if (plansToExecute.size() > 0)
            {
                //a plan is given and will be checked if it's up to date
                plansToExecute.get(0).addDuration(delta);
                isUpToDate(plansToExecute.get(0), player);
            } else
            {
                //no plan given and new one will be calculated
                calculatePlanForCurrentSituation(player, getDistanceToPlayer(player));
            }
        }

        //makes sure that plansToExecute is never empty -> prevent NullPointerException
        if(plansToExecute.isEmpty())
        {
            plansToExecute.add(getPlan(FighterMovementState.STANDING, FighterFightingState.NONE));
        }

        updateCurrentStates(plansToExecute.get(0));

        executePlan(getDistanceToPlayer(player), player, delta);
    }

    private boolean analysePlayerAction(Player player)
    {
        switch (player.getCurrentFightingState())
        {
            case ATTACK:
                if(tryBlockTimer >= GameValues.FIGHTER_ATTACK_DURATION)
                {
                    if (shouldExecute(GameValues.AI_BLOCK_CHANCE))
                    {
                        resetAndAddPlanToArray(getPlan(FighterMovementState.STANDING, FighterFightingState.BLOCK));
                        return true;
                    }
                    tryBlockTimer = 0;
                }
                break;
            case ATTACK_DOWN:
                if(tryDodgeForAttackDownTimer >= GameValues.FIGHTER_JUMP_DURATION)
                {
                    if(plansToExecute.isEmpty() || plansToExecute.get(0).isSecondMovementEnabled() || (plansToExecute.get(0).getMovement() != FighterMovementState.MOVINGLEFT || plansToExecute.get(0).getMovement() != FighterMovementState.MOVINGRIGHT))
                    if (shouldExecute(GameValues.AI_DODGE_ATTACK_FROM_ABOVE_CHANCE))
                    {
                        resetAndAddPlanToArray(getPlan(FighterMovementState.DUCKING, FighterFightingState.NONE));
                        /*if (!facingLeft)
                        {
                            resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGRIGHT, FighterFightingState.NONE));
                        } else
                        {
                            resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGLEFT, FighterFightingState.NONE));
                        }*/
                        return true;
                    }
                    tryDodgeForAttackDownTimer = 0;
                }
                break;
            case ATTACK_UP:
                if(tryDodgeForAttackUpTimer >= GameValues.FIGHTER_JUMP_DURATION)
                {
                    if (!isOnGround() && shouldExecute(GameValues.AI_DODGE_ATTACK_FROM_UNDERNEATH_CHANCE))
                    {
                        if (plansToExecute.size() > 0)
                        {
                            if (plansToExecute.get(0).getOriginalFacingDirection().equals("facingRight"))
                            {
                                resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGRIGHT, FighterFightingState.NONE));
                            } else
                            {
                                resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGLEFT, FighterFightingState.NONE));
                            }

                            return true;
                        }
                    }
                    tryDodgeForAttackUpTimer = 0;
                }
                break;
            default:

                break;
        }

        switch (player.getCurrentMovementState())
        {
            case JUMPING:
                if(tryCounterAttackTimer >= GameValues.FIGHTER_JUMP_DURATION)
                {
                    if (isOnGround() && shouldExecute(GameValues.AI_COUNTER_ATTACK_JUMP_CHANCE))
                    {
                        resetAndAddPlanToArray(getPlan(FighterMovementState.JUMPING, FighterFightingState.ATTACK_UP));
                        return true;
                    }
                    Gdx.app.log("AI", "tried!");
                    tryCounterAttackTimer = 0;
                }
                break;
            default:

                return false;
        }
        return false;
    }

    private void isUpToDate(Plan currentPlan, Player player)
    {
        float distanceToPlayer = getDistanceToPlayer(player);
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
                                if(shouldExecute(GameValues.AI_SHOULD_CHANGE_MOVE_CHANCE_CLOSE_DISTANCE))
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
                        if(shouldExecute(GameValues.AI_SHOULD_CHANGE_MOVE_CHANCE_CLOSE_DISTANCE))
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


    /**
        @param distance: distance between the AI and the player
        isUpToDate() calls this method when the current plan is not up to date because the
        situation has dramatically changed or the last plan has expired;
        a completely new plan will be calculated
    */
    private void calculatePlanForCurrentSituation(Player player, float distance)
    {
        if(distance <= 0)
        {
            if(!facingLeft)
            {
                resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGLEFT, FighterFightingState.NONE));
            }
            else
            {
                resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGRIGHT, FighterFightingState.NONE));
            }
        }
        else if(distance <= GameValues.AI_START_ATTCKING_DISTANCE)
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
                            if (!facingLeft)
                            {
                                resetAndAddPlanToArray(getPlan(FighterMovementState.JUMPING, FighterMovementState.MOVINGRIGHT, FighterFightingState.NONE));
                                plansToExecute.get(0).setJumpInformation("facingRight");
                                addPlanToArray(getPlan(FighterMovementState.MOVINGRIGHT, FighterFightingState.ATTACK_DOWN));
                            }
                            else
                            {
                                resetAndAddPlanToArray(getPlan(FighterMovementState.JUMPING, FighterMovementState.MOVINGLEFT, FighterFightingState.NONE));
                                plansToExecute.get(0).setJumpInformation("facingLeft");
                                addPlanToArray(getPlan(FighterMovementState.MOVINGLEFT, FighterFightingState.ATTACK_DOWN));
                            }
                        }
                        else
                        {
                            //AI will do normal attack
                            if ( !facingLeft)
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
                        else if(shouldExecute(GameValues.AI_GO_BACK_CHANCE_CLOSE))
                        {
                            //AI moves backwards
                            if(!facingLeft)
                            {
                                resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGLEFT, FighterFightingState.NONE));
                            }
                            else
                            {
                                resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGRIGHT, FighterFightingState.NONE));
                            }
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
                    if (!facingLeft)
                    {
                        resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGRIGHT, FighterFightingState.ATTACK_DOWN));
                    }
                    else
                    {
                        resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGLEFT, FighterFightingState.ATTACK_DOWN));
                    }
                }
            }
            else
            {
                //player is jumping
                //should never reach this point because of analysePlayerAction()
                if(!facingLeft)
                {
                    resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGRIGHT, FighterFightingState.NONE));
                }
                else
                {
                    resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGLEFT, FighterFightingState.NONE));
                }
            }
        }
        else if(distance > GameValues.AI_START_FIGHTING_DISTANCE)
        {
            //ai is far away from the player
            if(!facingLeft)
            {
                resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGRIGHT, FighterFightingState.NONE));
            }
            else
            {
                resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGLEFT, FighterFightingState.NONE));
            }
        }
        else
        {
            //AI is between far away and close -> medium distance
            if(shouldExecute(GameValues.AI_STANDARD_MOVE_CHANCE_MEDIUM_DISTANCE))
            {
                //standard move
                resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGRIGHT, FighterMovementState.MOVINGLEFT, FighterFightingState.NONE));
            }
            else if(shouldExecute(GameValues.AI_HEAD_TOWARDS_PLAYER_CHANCE_MEDIUM_DISTANCE))
            {
                //head towards player
                if(!facingLeft)
                {
                    resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGRIGHT, FighterFightingState.NONE));
                }
                else
                {
                    resetAndAddPlanToArray(getPlan(FighterMovementState.MOVINGLEFT, FighterFightingState.NONE));
                }
            }
            else if(shouldExecute(GameValues.AI_JUMP_ATTACK_CHANCE_MEDIUM_DISTANCE))
            {
                //jump attack
                if(!facingLeft)
                {
                    resetAndAddPlanToArray(getPlan(FighterMovementState.JUMPING, FighterMovementState.MOVINGRIGHT, FighterFightingState.NONE));
                    plansToExecute.get(0).setJumpInformation("facingRight");
                    addPlanToArray(getPlan(FighterMovementState.MOVINGRIGHT, FighterFightingState.ATTACK_DOWN));
                }
                else
                {
                    resetAndAddPlanToArray(getPlan(FighterMovementState.JUMPING, FighterMovementState.MOVINGLEFT, FighterFightingState.NONE));
                    plansToExecute.get(0).setJumpInformation("facingLeft");
                    addPlanToArray(getPlan(FighterMovementState.MOVINGLEFT, FighterFightingState.ATTACK_DOWN));
                }

            }
            else if(shouldExecute(GameValues.AI_JUMP_CHANCE_MEDIUM_DISTANCE))
            {
                //jump
                if(!facingLeft)
                {
                    resetAndAddPlanToArray(getPlan(FighterMovementState.JUMPING, FighterMovementState.MOVINGRIGHT, FighterFightingState.NONE));
                    plansToExecute.get(0).setJumpInformation("facingRight");
                }
                else
                {
                    resetAndAddPlanToArray(getPlan(FighterMovementState.JUMPING, FighterMovementState.MOVINGLEFT, FighterFightingState.NONE));
                    plansToExecute.get(0).setJumpInformation("facingLeft");
                }
            }
            else
            {
                //AI just standing
                resetAndAddPlanToArray(getPlan(FighterMovementState.STANDING, FighterFightingState.NONE));
            }
        }
        //Gdx.app.log("New Plan", plansToExecute.get(0).getMovement().toString() + " " + plansToExecute.get(0).getSecondMovement() + " " + plansToExecute.get(0).getFighting().toString() + " " + getDistanceToPlayer(player));
    }

    /**
        @param chance: chance between 0 and 100 that a action is executed;
        gambles depending on the chance whether a action should be executed
     */
    private boolean shouldExecute(int chance)
    {
        Random r = new Random();
        int a = r.nextInt(100);
        return (a < chance);
    }

    /**
       @param delta: time since last frame
        depending on the current movement and fighting state of the ai this method executes
         the movement
     */
    private void executePlan(float distanceToPlayer, Player player, float delta)
    {
        if(movementState != FighterMovementState.JUMPING && getY() > GameValues.FIGHTER_ORIGINAL_HEIGHT)
        {
            fall();
        }
        if(fightingState != FighterFightingState.BLOCK)
        {
            switch (movementState)
            {
                case MOVINGRIGHT:
                    if ((player.getCurrentFightingState() == FighterFightingState.ATTACK_DOWN && isOnGround()) || (player.getCurrentFightingState() == FighterFightingState.ATTACK_UP && !isOnGround()))
                    {
                        //player is attacking down and AI has to dodge on the ground
                        //or player is attacking up and AI has to dodge in the air
                        moveRight(GameValues.AI_MOVING_SPEED_DODGE);
                    }
                    else if (!(fightingState == FighterFightingState.ATTACK || fightingState == FighterFightingState.ATTACK_DOWN || fightingState == FighterFightingState.ATTACK_UP))
                    {
                        if ( !facingLeft)
                        {
                            if (distanceToPlayer - GameValues.AI_MOVING_SPEED > 0)
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
                        if (!facingLeft)
                        {
                            if (distanceToPlayer - GameValues.AI_MOVING_SPEED_WHILE_ATTACK >= -GameValues.AI_MOVING_SPEED_WHILE_ATTACK)
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
                    if ((player.getCurrentFightingState() == FighterFightingState.ATTACK_DOWN && isOnGround())  || (player.getCurrentFightingState() == FighterFightingState.ATTACK_UP && !isOnGround()))
                    {
                        //player is attacking down and AI has to dodge on the ground
                        //or player is attacking up and AI has to dodge in the air
                        moveLeft(GameValues.AI_MOVING_SPEED_DODGE);
                    }
                    else if (!(fightingState == FighterFightingState.ATTACK || fightingState == FighterFightingState.ATTACK_DOWN || fightingState == FighterFightingState.ATTACK_UP))
                    {
                        if (facingLeft)
                        {
                            if (distanceToPlayer - GameValues.AI_MOVING_SPEED > 0)
                            {
                                //makes sure that AI doesn't walk through player
                                moveLeft(GameValues.AI_MOVING_SPEED);
                            }
                        }
                        else
                        {
                            moveLeft(GameValues.AI_MOVING_SPEED);
                        }
                    } else
                    {
                        if (facingLeft)
                        {
                            if (distanceToPlayer - GameValues.AI_MOVING_SPEED_WHILE_ATTACK >= -GameValues.AI_MOVING_SPEED_WHILE_ATTACK)
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
                    if (!jump())
                    {
                        plansToExecute.get(0).setMovement(FighterMovementState.STANDING);
                    }
                    break;
                case DUCKING:
                    //duck();
                    break;
                default:
                    //Gdx.app.log("executePlan", "not right state for execution");
                    break;
            }
        }

        switch(fightingState)
        {
            case ATTACK:
                plansToExecute.get(0).setExecuted(!attack(delta));
                break;
            case ATTACK_DOWN:
                plansToExecute.get(0).setExecuted(!attackDown(delta));
                break;
            case ATTACK_UP:
                plansToExecute.get(0).setExecuted(!attackUp(delta));
                break;
            case BLOCK:
                plansToExecute.get(0).setExecuted(!block(delta));
                break;
            default:
                break;
        }
    }

    private void terminate(Plan plan)
    {
        if(plan != null)
        {
            plan.setExecuted(true);
        }
    }

    /*
        checks for already executed plans and deletes them from plansToExecute
     */
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
        Plan plan = new Plan(move, fight);
        plan.setJumpInformation(facingDirection);

        return plan;
    }

    private Plan getPlan(Fighter.FighterMovementState move, Fighter.FighterMovementState secMove, Fighter.FighterFightingState fight)
    {
        Plan plan = new Plan(move, secMove, fight);
        plan.setJumpInformation(facingDirection);

        return plan;
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
        if(facingLeft)
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
