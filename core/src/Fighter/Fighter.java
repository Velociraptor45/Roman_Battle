package Fighter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
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
    protected TextureRegion jumpKick; // attack down
    protected TextureRegion jumpKickLeft;
    protected TextureRegion hit;
    protected TextureRegion hitLeft;
    protected Animation <TextureRegion> runAnimation;
    protected Animation <TextureRegion> runAnimationLeft;
    protected Animation <TextureRegion> jump;
    protected Animation <TextureRegion> jumpLeft;
    protected Animation <TextureRegion> stunAnimation;
    protected Animation <TextureRegion> stunLeftAnimation;



    protected TextureAtlas atlas;

    protected FighterMovementState currentMovementState;
    protected FighterMovementState previousMovementState;

    protected boolean stunned;
    protected float stunTimer, stateTimer, blockTimer, attackTimer, attackUpTimer, attackDownTimer, takeDamageTimer;

    protected short wonRounds = 0;

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
        stunTimer = 0f;
        takeDamageTimer = GameValues.FIGHTER_ATTACK_DURATION;
        wonRounds = 0;

        stunned = false;

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


        frames.add(new TextureRegion(atlas.findRegion("Stun_C2")));
        frames.add(new TextureRegion(atlas.findRegion("Stun_C3")));
        frames.add(new TextureRegion(atlas.findRegion("Stun_C4")));
        stunAnimation = new Animation<TextureRegion>(1f/10,frames);
        frames.clear();

        TextureRegion stun1 = new TextureRegion(atlas.findRegion("Stun_C2"));
        TextureRegion stun2 = new TextureRegion(atlas.findRegion("Stun_C3"));
        TextureRegion stun3 = new TextureRegion(atlas.findRegion("Stun_C4"));
        stun1.flip(true,false);
        stun2.flip(true,false);
        stun3.flip(true,false);
        frames.add(stun1);
        frames.add(stun2);
        frames.add(stun3);
        stunLeftAnimation = new Animation<TextureRegion>(1f/10,frames);
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

        if(takeDamageTimer <= GameValues.FIGHTER_ATTACK_DURATION)
        {
            takeDamageTimer += delta;
        }
    }




    public TextureRegion getFrame(float delta){
        TextureRegion region;

        if(stunned){
            region = getStunAnimation();
        }
        else if (fightingState == NONE) {
            region = getMoveState();

          }else {
             region = getFightState();
          }

        stateTimer = currentMovementState == previousMovementState? stateTimer + delta :0;//TODO !!!!!!!!!!!!!!
        previousMovementState = currentMovementState;


        return region;
    }

    private TextureRegion getStunAnimation (){
        TextureRegion region;
        if (facingLeft){
            region = new TextureRegion(stunLeftAnimation.getKeyFrame(stateTimer,true));
        }else {
            region = new TextureRegion(stunAnimation.getKeyFrame(stateTimer,true));
        }
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


    //////////////////////////////////////// Actions

    public void moveRight(int speed)
    {
        if(getX() + getRegionWidth() + speed <= Gdx.graphics.getWidth())
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

    /**
     *
     * @param y: y-coordinate of the fighter
     * calculates the falling/jump speed based on a normal parable
     * @return falling/jump speed
     */
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

    public void stun(boolean status)
    {
        stunned = status;
    }

    public boolean isStunned(float delta)
    {
        if(stunned)
        {
            stunTimer += delta;
            if(stunTimer >= GameValues.FIGHTER_STUN_DURATION)
            {
                stunTimer = 0f;
                stunned = false;
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isOnGround()
    {
        return getY() == GameValues.FIGHTER_ORIGINAL_HEIGHT;
    }

    public void takeDamage(int damage)
    {
        if(takeDamageTimer >= GameValues.FIGHTER_ATTACK_DURATION)
        {
            HP -= damage;
            takeDamageTimer = 0;
        }
    }

    public int getHealth()
    {
        if(HP > 0)
        {
            return HP;
        }
        else
        {
            return 0;
        }
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

    public void roundWon()
    {
        wonRounds++;
    }

    public int getWonRounds()
    {
        return wonRounds;
    }

    public boolean isFacingLeft()
    {
        return facingLeft;
    }

    public Rectangle getRectangle()
    {
        Rectangle r = new Rectangle();
        r.set(getX(), getY(), getRegionWidth(), getRegionHeight());
        return r;
    }

    //checks if the fighter has won 3 of 5 rounds
    public boolean hasWonGame()
    {
        return wonRounds >= GameValues.GAME_ROUNDS_NEEDED_FOR_WIN;
    }

    /**
     * @param x: original x position of the fighter
     */
    public void reset(float x)
    {
        setPosition(x, GameValues.FIGHTER_ORIGINAL_HEIGHT);
        HP = GameValues.FIGHTER_HEALTH;
        fightingState = FighterFightingState.NONE;
        movementState = FighterMovementState.STANDING;
    }

    public void resetWonRounds()
    {
        wonRounds = 0;
    }
}
