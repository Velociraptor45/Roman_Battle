package Fighter;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.joints.GearJoint;
import com.badlogic.gdx.utils.Array;

import Constants.GameValues;

public class Fighter extends Sprite
{

    public enum FighterMovementState{STANDING, MOVING, JUMPING, DUCKING}
    protected FighterMovementState movementState = FighterMovementState.STANDING;
    public enum FighterFightingState {ATTACK, BLOCK, NONE}
    protected FighterFightingState fightingState = FighterFightingState.NONE;

    protected TextureRegion facingRight;
    protected TextureRegion facingLeft;
    protected TextureRegion facingDirection;

    protected Animation <TextureRegion> runAnimation;
    protected Animation <TextureRegion> jump;
    protected TextureAtlas atlas;

    protected FighterMovementState currentState;
    protected FighterMovementState previousState;
    protected float stateTimer;

    ////////////////////////////////////////////////////////////
    protected int HP = GameValues.FIGHTER_HEALTH;

    public Fighter() {}

    public Fighter (TextureAtlas atlas, float xPos, float yPos)
    {
        this.atlas = atlas;
        currentState = FighterMovementState.STANDING;
        previousState = FighterMovementState.STANDING;
        stateTimer = 0f;

        //init animations
        //get Textureregion for animation run
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i=0; i<11; i++){
            frames.add(new TextureRegion(atlas.findRegion("run",i)));
        }

        runAnimation = new Animation(1f/20,frames);
        frames.clear();


        //get images for jumpanimation
        for (int i=0;i<11; i++) {
            frames.add(new TextureRegion(atlas.findRegion("jump", i)));
        }

        jump = new Animation(1f/10,frames);
        //get run animation frames
        // runAnimation =new Animation<TextureRegion>(1f/10f,atlas.findRegions("run"));

        //get jump animation frames
        // jump = new Animation<TextureRegion>(1f/10f,atlas.findRegions("jump"));

        //picture in TextureAtlas at index 0
        facingRight = new TextureRegion(atlas.findRegion("run",0));
        facingLeft = new TextureRegion(atlas.findRegion("run",0));
        facingLeft.flip(true,false);

        facingDirection = facingLeft;
        setPosition(xPos, yPos);
        setRegion(facingLeft);
    }

    public void setMovementState(FighterMovementState state)
    {
        movementState = state;
    }

    public void setFightingState(FighterFightingState state)
    {
        fightingState = state;
    }

    public void update(float delta){
        setRegion(getFrame(delta));
    }

    public TextureRegion getFrame(float delta){
        currentState = movementState;
        TextureRegion region ;

        switch (currentState){
            case MOVING:
                region = new TextureRegion(runAnimation.getKeyFrame(stateTimer,true));
                break;
            case STANDING:
                region = facingDirection;
                break;
            case JUMPING:
                region = new TextureRegion(jump.getKeyFrame(stateTimer,true));
                break;
            default:
                region = facingDirection;
                break;
        }

        stateTimer = currentState == previousState? stateTimer + delta :0;//TODO !!!!!!!!!!!!!!
        previousState = currentState;
        return region;
    }

    public void moveRight(){
        setPosition(getX() + GameValues.FIGHTER_MOVING_SPEED,getY());
        facingDirection = facingLeft;

    }

    public void moveLeft(){
        setPosition(getX() - GameValues.FIGHTER_MOVING_SPEED,getY());
        facingDirection = facingRight;
    }

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

    public boolean isOnGround()
    {
        return getY() == GameValues.FIGHTER_ORIGINAL_HEIGHT;
    }
}
