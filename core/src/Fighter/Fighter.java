package Fighter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.joints.GearJoint;
import com.badlogic.gdx.utils.Array;

import Constants.GameValues;

public class Fighter extends Sprite
{
    public enum FighterMovementState{STANDING, MOVINGRIGHT, MOVINGLEFT, JUMPING, DUCKING}
    protected FighterMovementState movementState = FighterMovementState.STANDING;
    public enum FighterFightingState {ATTACK_UP, ATTACK_DOWN, ATTACK, BLOCK, NONE}
    protected FighterFightingState fightingState = FighterFightingState.NONE;

    protected TextureRegion facingRight;
    protected TextureRegion facingLeft;
    protected TextureRegion facingDirection;
    protected TextureRegion duck;

    protected Animation <TextureRegion> runAnimation;
    protected Animation <TextureRegion> jump;

    protected TextureAtlas atlas;

    protected FighterMovementState currentMovementState;
    protected FighterMovementState previousMovementState;
    protected float stateTimer;

    protected short wonGames = 0;

    ////////////////////////////////////////////////////////////
    protected int HP = GameValues.FIGHTER_HEALTH;

    public Fighter() {}

    public Fighter (TextureAtlas atlas, float xPos, float yPos)
    {
        this.atlas = atlas;
        currentMovementState = FighterMovementState.STANDING;
        previousMovementState = FighterMovementState.STANDING;
        stateTimer = 0f;
        wonGames = 0;

        //init animations

        Array<TextureRegion> frames = new Array<TextureRegion>();

        frames.add(new TextureRegion((atlas.findRegion("WALK1_C"))));
        frames.add(new TextureRegion((atlas.findRegion("WALK2_C"))));
        runAnimation = new Animation(1f/5,frames);
        frames.clear();


        frames.add(new TextureRegion(atlas.findRegion("JUMP1_C")));
        frames.add(new TextureRegion(atlas.findRegion("JUMP2_C")));
        frames.add(new TextureRegion(atlas.findRegion("JUMP3_C")));
        frames.add(new TextureRegion(atlas.findRegion("JUMP4_C")));
        jump = new Animation(1f/10,frames);
        frames.clear();




        duck = new TextureRegion(atlas.findRegion("DUCK_1_C"));
        facingRight = new TextureRegion(atlas.findRegion("IDLE_C"));
        facingLeft = new TextureRegion(atlas.findRegion("IDLE_C"));
        facingLeft.flip(true,false);

        facingDirection = facingLeft;
        setPosition(xPos, yPos);
        setRegion(facingLeft);
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
        fightingState = state;
    }

    public void update(float delta){
        setRegion(getFrame(delta));
    }

    public TextureRegion getFrame(float delta){
        currentMovementState = movementState;
        TextureRegion region ;

        switch (currentMovementState){
            case MOVINGRIGHT:
            case MOVINGLEFT:
                region = new TextureRegion(runAnimation.getKeyFrame(stateTimer,true));
                break;
            case STANDING:
                region = facingDirection;
                break;
            case JUMPING:
                region = new TextureRegion(jump.getKeyFrame(stateTimer,true));
                break;
            case DUCKING:
                region = duck;
                break;
            default:
                region = facingDirection;
                break;
        }

        stateTimer = currentMovementState == previousMovementState? stateTimer + delta :0;//TODO !!!!!!!!!!!!!!
        previousMovementState = currentMovementState;
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

    public void duck(){
        //TODO
    }

    public void block()
    {

    }

    public void attack()
    {

    }

    public void attackDown()
    {

    }

    public void attackUp()
    {

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
            facingDirection = facingRight;
        }
        else
        {
            facingDirection = facingLeft;
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
}
