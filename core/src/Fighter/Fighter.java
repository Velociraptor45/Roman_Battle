package Fighter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import Constants.GameValues;

import static Fighter.Fighter.FighterFightingState.ATTACK;
import static Fighter.Fighter.FighterFightingState.NONE;

public class Fighter extends Sprite
{
    public enum FighterMovementState{STANDING, MOVINGRIGHT, MOVINGLEFT, JUMPING, DUCKING}
    protected FighterMovementState movementState = FighterMovementState.STANDING;

    public enum FighterFightingState {ATTACK_UP, ATTACK_DOWN, ATTACK, BLOCK, NONE}
    protected FighterFightingState fightingState = FighterFightingState.NONE;



    protected boolean facingLeft = false;
    protected String facingDirection;

    protected TextureRegion standing;
    protected TextureRegion standingLeft;
    protected TextureRegion duck;
    protected TextureRegion duckLeft;
    protected TextureRegion blocked;
    protected TextureRegion blockedLeft;
    protected TextureRegion hitHigh;
    protected TextureRegion hitHighLeft;
    protected TextureRegion jumpKick; // für schlag nach unten
    protected TextureRegion jumpKickLeft;
    protected TextureRegion hit;
    protected TextureRegion hitLeft;
    protected Animation <TextureRegion> runAnimation;
    protected Animation <TextureRegion> runAnimationLeft;
    protected Animation <TextureRegion> jump;
    protected Animation <TextureRegion> jumpLeft;



    protected TextureAtlas atlas;

    protected FighterMovementState currentMovementState;
    protected FighterMovementState previousMovementState;


    protected float stateTimer, blockTimer, attackTimer, attackUpTimer, attackDownTimer;

    protected short wonGames = 0;

    ////////////////////////////////////////////////////////////
    protected int HP = GameValues.FIGHTER_HEALTH;



    public Fighter (TextureAtlas atlas, float xPos, float yPos)
    {
        this.atlas = atlas;
        currentMovementState = FighterMovementState.STANDING;
        previousMovementState = FighterMovementState.STANDING;
        stateTimer = 0f;
        blockTimer = 0f;
        attackTimer = 0f;
        attackDownTimer = 0f;
        attackUpTimer = 0f;
        wonGames = 0;


        // init Animation
        Array<TextureRegion> frames = new Array<TextureRegion>();

        frames.add(new TextureRegion((atlas.findRegion("WALK1_C"))));
        frames.add(new TextureRegion((atlas.findRegion("WALK2_C"))));
        runAnimation = new Animation(1f/5,frames);
        frames.clear();

        TextureRegion leftWalkOne = new TextureRegion(atlas.findRegion("WALK1_C"));
        TextureRegion leftWalkTwo = new TextureRegion(atlas.findRegion("WALK2_C"));
        leftWalkOne.flip(true,false);
        leftWalkTwo.flip(true,false);
        frames.add(leftWalkOne);
        frames.add(leftWalkTwo);
        runAnimationLeft = new Animation(1f/5,frames);
        frames.clear();





        frames.add(new TextureRegion(atlas.findRegion("JUMP1_C")));
        frames.add(new TextureRegion(atlas.findRegion("JUMP2_C")));
        frames.add(new TextureRegion(atlas.findRegion("JUMP3_C")));
        frames.add(new TextureRegion(atlas.findRegion("JUMP4_C")));
        jump = new Animation(1f/10,frames);
        frames.clear();

        TextureRegion jump1 = new TextureRegion(atlas.findRegion("JUMP1_C"));
        TextureRegion jump2 = new TextureRegion(atlas.findRegion("JUMP2_C"));
        TextureRegion jump3 = new TextureRegion(atlas.findRegion("JUMP3_C"));
        TextureRegion jump4 = new TextureRegion(atlas.findRegion("JUMP4_C"));
        jump1.flip(true,false);
        jump2.flip(true,false);
        jump3.flip(true,false);
        jump4.flip(true,false);
        frames.add(jump1);
        frames.add(jump2);
        frames.add(jump3);
        frames.add(jump4);
        jumpLeft = new Animation<TextureRegion>(1f/10,frames);
        frames.clear();


        // init Textures
        duck = new TextureRegion(atlas.findRegion("DUCK_1_C"));
        duckLeft = new TextureRegion(atlas.findRegion("DUCK_1_C"));
        duckLeft.flip(true,false);
        blocked = new TextureRegion(atlas.findRegion("BLOCK_1_C"));
        blockedLeft = new TextureRegion(atlas.findRegion("BLOCK_1_C"));
        blockedLeft.flip(true,false);
        hitHigh = new TextureRegion(atlas.findRegion("HIT_HIGH2_C"));
        hitHighLeft = new TextureRegion(atlas.findRegion("HIT_HIGH2_C"));
        hitHighLeft.flip(true,false);
        jumpKick = new TextureRegion(atlas.findRegion("JUMP_KICK_C"));
        jumpKickLeft = new TextureRegion(atlas.findRegion("JUMP_KICK_C"));
        jumpKickLeft.flip(true,false);
        hit = new TextureRegion(atlas.findRegion("HIT_RIGHT_C"));
        hitLeft = new TextureRegion(atlas.findRegion("HIT_RIGHT_C"));
        hitLeft.flip(true,false);
        standing = new TextureRegion(atlas.findRegion("IDLE_C"));
        standingLeft = new TextureRegion(atlas.findRegion("IDLE_C"));
        standingLeft.flip(true,false);


        setPosition(xPos, yPos);
        setRegion(standing);
        setRegionWidth(30);


    }

    public void setMovementState(FighterMovementState state)
    {
        movementState = state;
    }

    public FighterMovementState getCurrentMovementState()
    {
        return movementState;
    }

    public FighterFightingState getCurrentFightingState()
    {
        return fightingState;
    }

    public void setFightingState(FighterFightingState state)
    {
        if(fightingState == ATTACK && fightingState != state && facingLeft)
        {
            moveRight(GameValues.PLAYER_ATTACK_LEFT_SPEED);
        }
        fightingState = state;
    }

    public void update(float delta){
        setRegion(getFrame(delta));
    }




    public TextureRegion getFrame(float delta){
        TextureRegion region;


        if (fightingState == NONE) {
            region = getMoveState();

          }else {
             region = getFightState();
          }

        stateTimer = currentMovementState == previousMovementState? stateTimer + delta :0;//TODO !!!!!!!!!!!!!!
        previousMovementState = currentMovementState;


        return region;
    }


    private TextureRegion getFightState(){
        TextureRegion region;
        switch (getCurrentFightingState()){
            case BLOCK:
                if (facingLeft){
                    region = blockedLeft;
                }else {
                    region = blocked;
                }
                break;
            case ATTACK_UP:
                if(facingLeft) {
                    region = hitHighLeft;
                }else {
                    region = hitHigh;
                }
                break;
            case ATTACK_DOWN:
                if (facingLeft){
                    region = jumpKickLeft;
                }else {
                    region = jumpKick;
                }
                break;
            case ATTACK:
                if(facingLeft) {
                    region = hitLeft;
                }else {
                    region = hit;
                }
                break;
            default:
                region= getMoveState();
                break;
        }

        return region;
    }


    private TextureRegion getMoveState(){
        TextureRegion region;
        currentMovementState = movementState;
        switch (currentMovementState) {
            case MOVINGRIGHT:
            case MOVINGLEFT:
                if(facingLeft) {
                    region = new TextureRegion(runAnimationLeft.getKeyFrame(stateTimer,true));
                }else {
                    region = new TextureRegion(runAnimation.getKeyFrame(stateTimer, true));
                }
                break;
            case STANDING:
                if(facingLeft) {
                    region = standingLeft;
                }else {
                    region=standing;
                }
                break;
            case JUMPING:
                if(facingLeft) {
                    region = new TextureRegion(jumpLeft.getKeyFrame(stateTimer, true));
                }else {
                    region = new TextureRegion(jump.getKeyFrame(stateTimer,true));
                }
                break;
            case DUCKING:
                if (facingLeft) {
                    region = duckLeft;
                }else {
                    region = duck;
                }
                break;
            default:
                if(facingLeft) {
                    region = standingLeft;
                }else {
                    region=standing;
                }
                break;
        }


        return region;

    }






    public void moveRight(int speed)
    {
        if(getX() + getRegionWidth() + speed <= Gdx.graphics.getWidth()) //TODO getWidth() austauschen
        {
            setPosition(getX() + speed, getY());
        }
    }

    public void moveLeft(int speed)
    {
        if(getX() - speed >= 0)
        {
            setPosition(getX() - speed, getY());
        }
    }

    /*
        returns true until maximum jump height is reached
     */
    public boolean jump()
    {
        setPosition(getX(), getY() + calcJumpFallSpeed(getY() - GameValues.FIGHTER_ORIGINAL_HEIGHT));
        return (GameValues.FIGHTER_ORIGINAL_HEIGHT + GameValues.FIGHTER_MAX_JUMP_HEIGHT > getY());
    }

    public void fall()
    {
        setPosition(getX(),getY() - calcJumpFallSpeed(getY() - GameValues.FIGHTER_ORIGINAL_HEIGHT));
        if(getY() < GameValues.FIGHTER_ORIGINAL_HEIGHT)
        {
            setPosition(getX(), GameValues.FIGHTER_ORIGINAL_HEIGHT);
        }
    }

    private float calcJumpFallSpeed(double y)
    {
        float x;

        double radicand = -1 * y + GameValues.FIGHTER_MAX_JUMP_HEIGHT;

        if(radicand < 0)
        {
            radicand = Math.abs(radicand);
        }
        x = -1 * (float) Math.sqrt(radicand);

        return -2*x;
    }

    public void duck()
    {

    }

    public boolean block(float delta)
    {
        blockTimer += delta;
        if(blockTimer <= GameValues.FIGHTER_BLOCK_DURATION)
        {
            return true;
        }
        else
        {
            blockTimer = 0f;
            return false;
        }
    }

    public boolean attack(float delta)
    {
        attackTimer += delta;
        if(attackTimer <= GameValues.FIGHTER_ATTACK_DURATION)
        {
            return true;
        }
        else
        {
            //Gdx.app.log("attack", "move back");
            //moveRight(GameValues.PLAYER_ATTACK_LEFT_SPEED);
            attackTimer = 0f;
            return false;
        }
    }

    public boolean attackDown(float delta)
    {
        attackDownTimer += delta;
        if(attackDownTimer <= GameValues.FIGHTER_ATTACK_DOWN_DURATION)
        {
            return true;
        }
        else
        {
            attackDownTimer = 0f;
            return false;
        }
    }

    public boolean attackUp(float delta)
    {
        attackUpTimer += delta;
        if(attackUpTimer <= GameValues.FIGHTER_ATTACK_UP_DURATION)
        {
            return true;
        }
        else
        {
            attackUpTimer = 0f;
            return false;
        }
    }

    public void stun()
    {

    }

    public boolean isOnGround()
    {
        return getY() == GameValues.FIGHTER_ORIGINAL_HEIGHT;
    }

    public void takeDamage(int damage)
    {
        HP -= damage;
    }

    public void updateFacingDirection(Fighter fighter)
    {
        if(getX() < fighter.getX())
        {
            facingDirection = "facingRight";
            facingLeft = false;
        }
        else
        {
            facingDirection = "facingLeft";
            facingLeft = true;

        }
    }

    public boolean isAlive()
    {
        return !(HP <= 0);
    }

    public void gameWon()
    {
        wonGames++;
    }

    public boolean isFacingLeft()
    {
        return facingLeft;
    }
}
