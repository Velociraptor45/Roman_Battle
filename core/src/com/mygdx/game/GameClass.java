package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import Constants.GameValues;
import Fighter.Fighter;
import Fighter.Player;
import Fighter.AI;
import Fighter.TestGround;

/**
 * Created by david on 12.08.2017.
 */

// This class represents the GameScreen where the Game is played
// Here are control elements, player, enemy....

public class GameClass implements Screen, GestureDetector.GestureListener {
    private MainClass mainClass;
    private Player player;
    private AI ai;
    private TextureAtlas atlas;


    private Stage gameStage;
    private Table movementButtonsTable, jbaButtonsTable; //jba = jump block attack
    private Button buttonLeft, buttonRight, buttonUp, buttonDown;
    private TextButton buttonAttack, buttonBlock, buttonJump;

    ////////////////////////////////////////////////////////////////////////
    private boolean moveRight = false;
    private boolean moveLeft = false;
    private boolean jump = false;
    private boolean standing = true;
    private boolean attackDown = false;
    private boolean attackUp = true;
    private boolean duck = false;
    private boolean attack = false;
    private boolean block = false;
   //////////////////////////////////////////////////////////////////

    // we need the MainClass Objekt to access the SpriteBatch
    public GameClass(MainClass mainClass){
        this.mainClass = mainClass;
        //atlas = new TextureAtlas(Gdx.files.internal("moves.pack"));
        atlas = new TextureAtlas(Gdx.files.internal("player.pack"));//TODO NEWWWWWWWWWWW 1

        player = new Player(atlas, GameValues.PLAYER_ORIGINAL_X, GameValues.FIGHTER_ORIGINAL_HEIGHT);
        ai = new AI(0, atlas, GameValues.AI_ORIGINAL_X, GameValues.FIGHTER_ORIGINAL_HEIGHT);

        player.updateFacingDirection(ai);
        ai.updateFacingDirection(player);
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
        jbaButtonsTable = new Table();
        movementButtonsTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        jbaButtonsTable.setBounds(0, 0 , Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        setupUpButton();
        movementButtonsTable.row();
        setupLeftButton();
        setupRightButton();
        movementButtonsTable.row();
        setupDownButton();

        setupJumpButton();
        setupBlockButton();
        jbaButtonsTable.row();
        setupAttackButton();

        movementButtonsTable.bottom().left();
        jbaButtonsTable.bottom().right();

        movementButtonsTable.debug();
        jbaButtonsTable.debug();
        gameStage.addActor(movementButtonsTable);
        gameStage.addActor(jbaButtonsTable);
    }

    private void setupLeftButton()
    {
        TextureAtlas leftButtonAtlas = new TextureAtlas("ui/game/buttons/arrowLeft.pack");

        Skin leftButtonSkin = new Skin(leftButtonAtlas);

        Button.ButtonStyle leftStyle = new Button.ButtonStyle();

        leftStyle.up = leftButtonSkin.getDrawable("arrowRight.up");
        leftStyle.down = leftButtonSkin.getDrawable("arrowRight.up");

        buttonLeft = new Button(leftStyle);

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
                if(!moveRight){
                    standing=true;
                }
            }
        });

        movementButtonsTable.add(buttonLeft);
    }

    private void setupRightButton()
    {
        TextureAtlas rightButtonAtlas = new TextureAtlas("ui/game/buttons/arrowRight.pack");

        Skin rightButtonSkin = new Skin(rightButtonAtlas);

        Button.ButtonStyle rightStyle = new Button.ButtonStyle();

        rightStyle.up = rightButtonSkin.getDrawable("arrowRight.up");
        rightStyle.down = rightButtonSkin.getDrawable("arrowRight.up");

        buttonRight = new Button(rightStyle);

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


                if(!moveLeft){
                    standing=true;
                }

            }
        });

        movementButtonsTable.add(buttonRight);
    }

    private void setupUpButton()
    {
        TextureAtlas atlas = new TextureAtlas("ui/game/buttons/arrowUp.pack");

        Skin skin = new Skin(atlas);

        Button.ButtonStyle style = new Button.ButtonStyle();

        style.up = skin.getDrawable("arrowUp.up");
        style.down = skin.getDrawable("arrowUp.up");

        buttonUp = new Button(style);
        buttonUp.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
            {
                attackUp = true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
            {
                attackUp = true;
            }
        });

        movementButtonsTable.add(buttonUp);
    }

    private void setupDownButton()
    {
        TextureAtlas atlas = new TextureAtlas("ui/game/buttons/arrowDown.pack");

        Skin skin = new Skin(atlas);

        Button.ButtonStyle style = new Button.ButtonStyle();

        style.up = skin.getDrawable("arrowDown.up");
        style.down = skin.getDrawable("arrowDown.up");

        buttonDown = new Button(style);
        buttonDown.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
            {
                if(player.isOnGround())
                {
                    duck = true;
                }
                else
                {
                    attackDown = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
            {
                duck = false;
                attackDown = false;
            }
        });

        movementButtonsTable.add(buttonDown);
    }

    private void setupAttackButton()
    {
        TextureAtlas atlas = new TextureAtlas("ui/game/buttons/jba.pack"); //jba = jump block attack

        Skin skin = new Skin(atlas);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        style.up = skin.getDrawable("jba.up");
        style.down = skin.getDrawable("jba.up");
        style.font = new BitmapFont();
        style.fontColor = Color.BLACK;
        style.pressedOffsetX = 1;
        style.pressedOffsetY = -1;

        buttonAttack = new TextButton("A", style);
        buttonAttack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                attack = true;
            }
        });

        jbaButtonsTable.add(buttonAttack);
    }

    private void setupBlockButton()
    {
        TextureAtlas atlas = new TextureAtlas("ui/game/buttons/jba.pack"); //jba = jump block attack

        Skin skin = new Skin(atlas);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        style.up = skin.getDrawable("jba.up");
        style.down = skin.getDrawable("jba.up");
        style.font = new BitmapFont();
        style.fontColor = Color.BLACK;
        style.pressedOffsetX = 1;
        style.pressedOffsetY = -1;

        buttonBlock = new TextButton("B", style);
        buttonBlock.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                block = true;
            }
        });

        jbaButtonsTable.add(buttonBlock);
    }

    private void setupJumpButton()
    {
        TextureAtlas atlas = new TextureAtlas("ui/game/buttons/jba.pack"); //jba = jump block attack

        Skin skin = new Skin(atlas);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        style.up = skin.getDrawable("jba.up");
        style.down = skin.getDrawable("jba.up");
        style.font = new BitmapFont();
        style.fontColor = Color.BLACK;
        style.pressedOffsetX = 1;
        style.pressedOffsetY = -1;

        buttonJump = new TextButton("J", style);
        buttonJump.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(player.getY() <= GameValues.FIGHTER_ORIGINAL_HEIGHT)
                {
                    jump = true;
                }
            }
        });

        jbaButtonsTable.add(buttonJump);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.updateFacingDirection(ai);
        ai.updateFacingDirection(player);

        player.update(delta);
        ai.update(delta);


        ai.act(player, delta);


        switchPlayerMovementState();
        switchPlayerFightingState();

        collision(player, ai);

        mainClass.getSpriteBatch().begin();
        mainClass.getSpriteBatch().draw(player,player.getX(),player.getY());
        mainClass.getSpriteBatch().draw(ai, ai.getX(), ai.getY());
        mainClass.getSpriteBatch().end();


        gameStage.act(delta);
        gameStage.draw();
    }

    /*
        checks collision of player and AI
     */
    private void collision(Player player, AI ai)
    {
        if(Intersector.overlaps(player.getBoundingRectangle(), ai.getBoundingRectangle()))
        {
            if(player.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK && !(ai.getCurrentFightingState() == Fighter.FighterFightingState.BLOCK || ai.getCurrentMovementState() == Fighter.FighterMovementState.DUCKING))
            {
                //player attacked nomal, ai couldn't block
                ai.takeDamage(GameValues.PLAYER_DAMAGE);
                ai.stun();
            }
            else if(ai.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK && !(player.getCurrentFightingState() == Fighter.FighterFightingState.BLOCK || player.getCurrentMovementState() == Fighter.FighterMovementState.DUCKING))
            {
                //ai attacked normal, player couldn't block
                player.takeDamage(GameValues.AI_DAMAGE);
                player.stun();
            }
            else if(player.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_DOWN && !(ai.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_UP))
            {
                //player attacked from above
                ai.takeDamage(GameValues.PLAYER_DAMAGE);
                ai.stun();
            }
            else if(ai.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_DOWN && !(player.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_UP))
            {
                //ai attacked from above
                player.takeDamage(GameValues.AI_DAMAGE);
                player.stun();
            }
            else if(player.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_UP && !(ai.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_DOWN))
            {
                //player attacked from underneath
                ai.takeDamage(GameValues.PLAYER_DAMAGE);
                ai.stun();
            }
            else if(ai.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_UP && !(player.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_DOWN))
            {
                //ai attacked from underneath
                player.takeDamage(GameValues.AI_DAMAGE);
                player.stun();
            }
            else if((ai.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_UP || ai.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_DOWN || ai.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK) && (player.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_DOWN || player.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_UP || player.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK))
            {
                //both attacked at the same time
                ai.takeDamage(GameValues.PLAYER_DAMAGE);
                player.takeDamage(GameValues.AI_DAMAGE);

                ai.stun();
                player.stun();
            }
        }
    }

    private void switchPlayerFightingState()
    {
        if(attackUp && attack)
        {
            player.setFightingState(Player.FighterFightingState.ATTACK_UP);
        }
        else if(attackDown && attack)
        {
            player.setFightingState(Player.FighterFightingState.ATTACK_DOWN);
        }
        else if(attack)
        {
            player.setFightingState(Player.FighterFightingState.ATTACK);
        }
        else if(block)
        {
            player.setFightingState(Player.FighterFightingState.BLOCK);
        }
        else
        {
            player.setFightingState(Player.FighterFightingState.NONE);
        }
    }

    private void switchPlayerMovementState()
    {
        //TODO switch wohl sauberer
        if(moveRight){
            player.moveRight(GameValues.PLAYER_MOVING_SPEED);
            player.setMovementState(Player.FighterMovementState.MOVINGRIGHT);
        }
        else if (moveLeft){
            player.moveLeft(GameValues.PLAYER_MOVING_SPEED);
            player.setMovementState(Player.FighterMovementState.MOVINGLEFT);
        }
        else if(standing){
            player.setMovementState(Player.FighterMovementState.STANDING);
        }
        else if(attackDown && !attack)
        {
            player.setMovementState(Player.FighterMovementState.DUCKING);
        }

        if(jump)
        {
            Gdx.app.log("Player", "jump");
            player.setMovementState(Player.FighterMovementState.JUMPING);
            jump = player.jump();
        }
        else if(player.getY() > GameValues.FIGHTER_ORIGINAL_HEIGHT)
        {
            Gdx.app.log("Player", "fall");
            player.setMovementState(Player.FighterMovementState.JUMPING);
            player.fall();
        }
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
