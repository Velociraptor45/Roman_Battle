package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import Fighter.Player;
import Fighter.TestGround;

/**
 * Created by david on 12.08.2017.
 */

// This class represents the GameScreen where the Game is played
// Here are control elements, player, enemy....

public class GameClass implements Screen, GestureDetector.GestureListener {
    private MainClass mainClass;
    private Player player;
    private TextureAtlas atlas;


    private Stage gameStage;
    private Table movementButtonsTable;
    private Button buttonLeft, buttonRight;





    //TODO evtl auslagern in Player?
    ////////////////////////////////////////////////////////////////////////
    private static boolean moveRight = false;
    private static boolean moveLeft = false;
    private static boolean jump = false;
    private boolean standing = true;
   //////////////////////////////////////////////////////////////////

    // we need the MainClass Objekt to access the SpriteBatch
    public GameClass(MainClass mainClass){
        this.mainClass = mainClass;
        atlas = new TextureAtlas(Gdx.files.internal("moves.pack"));
        player = new Player(atlas,20,20);
    }

    //this is like the create() method
    @Override
    public void show()
    {
        setup();
    }

    private void setup() {

        gameStage = new Stage();
        Gdx.input.setInputProcessor(gameStage);

        movementButtonsTable = new Table();
        movementButtonsTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        setupButtons();

        movementButtonsTable.bottom().left();

        movementButtonsTable.debug();
        gameStage.addActor(movementButtonsTable);
    }

    private void setupButtons()
    {
        TextureAtlas rightButtonAtlas = new TextureAtlas("ui/game/buttons/arrowRight.pack");
        TextureAtlas leftButtonAtlas = new TextureAtlas("ui/game/buttons/arrowLeft.pack");

        Skin rightButtonSkin = new Skin(rightButtonAtlas);
        Skin leftButtonSkin = new Skin(leftButtonAtlas);

        Button.ButtonStyle rightStyle = new Button.ButtonStyle();
        Button.ButtonStyle leftStyle = new Button.ButtonStyle();

        rightStyle.up = rightButtonSkin.getDrawable("arrowRight.up");
        rightStyle.down = rightButtonSkin.getDrawable("arrowRight.up");
        leftStyle.up = leftButtonSkin.getDrawable("arrowRight.up");
        leftStyle.down = leftButtonSkin.getDrawable("arrowRight.up");

        buttonRight = new Button(rightStyle);
        buttonLeft = new Button(leftStyle);

        buttonRight.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
            {
                Gdx.app.log("right", "true");
                moveRight = true;
                standing = false;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor)
            {
                Gdx.app.log("right", "false");
                moveRight = false;


                if(moveLeft == false){
                    standing=true;
                }

            }
        });

        buttonLeft.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
            {
                Gdx.app.log("left", "true");
                moveLeft = true;
                standing = false;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor)
            {
                Gdx.app.log("left", "false");
                moveLeft = false;
                if(moveRight == false){
                    standing=true;
                }
            }
        });

        movementButtonsTable.add(buttonLeft);
        movementButtonsTable.add(buttonRight);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.updatePlayer(delta);


        //TODO switch wohl sauberer
        if(moveRight){
            player.moveRight();
            player.setState = Player.PlayerState.MOVING;
        }

        if (moveLeft){
            player.moveLeft();
            player.setState= Player.PlayerState.MOVING;
        }

        if(standing){
            player.setState = Player.PlayerState.STANDING;
        }


        mainClass.getSpriteBatch().begin();
        mainClass.getSpriteBatch().draw(player,player.getX(),player.getY());
        mainClass.getSpriteBatch().end();


        gameStage.act(delta);
        gameStage.draw();



    }



    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }


    //methods of gesture interface
    @Override
    public boolean fling(float velocityX, float velocityY, int button) {return false; }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }


    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }



}
