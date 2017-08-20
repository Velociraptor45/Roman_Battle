package Fighter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import Constants.GameValues;


/**
 * Created by david on 12.08.2017.
 */


public class Player extends Sprite {

    public enum PlayerState{STANDING,MOVING,JUMPING}
    public PlayerState setState = PlayerState.STANDING;

    private TextureRegion facingRight;
    private TextureRegion facingLeft;
    private TextureRegion facingDirection;

    private Animation <TextureRegion> runAnimation;
    private Animation <TextureRegion> jump;
    private TextureAtlas atlas;

    public PlayerState currentState;
    public PlayerState previousState;
    private float stateTimer ;


    public Player(TextureAtlas atlas, float xPos, float yPos ){
        this.atlas = atlas;
        currentState = PlayerState.STANDING;
        previousState = PlayerState.STANDING;
        stateTimer = 0f;

        //init animations
        //get Textureregion for animation run
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i=0; i<11; i++){
            frames.add(new TextureRegion(atlas.findRegion("run",i)));
        }

        runAnimation = new Animation(1f/10,frames);
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
        setPosition(xPos,yPos);
        setRegion(facingLeft);

    }



    public void updatePlayer(float delta){
        setRegion(getFrame(delta));

    }

    public TextureRegion getFrame(float delta){
        currentState = setState;
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
        setPosition(getX()+5,getY());
        facingDirection = facingLeft;

    }

    public void moveLeft(){
        setPosition(getX()-5,getY());
        facingDirection = facingRight;
    }

    public boolean jump(){
        setPosition(getX(),getY() + GameValues.FIGHTER_JUMP_HEIGHT);
        if(GameValues.FIGHTER_ORIGINAL_HEIGHT + GameValues.FIGHTER_MAX_JUMP_HEIGHT > getY())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void fall()
    {
        setPosition(getX(),getY() - GameValues.FIGHTER_JUMP_HEIGHT);
    }

    public void duck(){
        //TODO
    }






}
