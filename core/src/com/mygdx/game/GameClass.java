package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.Random;

import Constants.GameValues;
import Fighter.Fighter;
import Fighter.Player;
import Fighter.AI;

/**
 * Created by david on 12.08.2017.
 */

// This class represents the GameScreen where the Game is played
// Here are control elements, player, enemy....

public class GameClass implements Screen, GestureDetector.GestureListener
{
    private MainClass mainClass;
    private Player player;
    private AI ai;
    private Texture background;
    private TextureAtlas atlasAI;
    private TextureAtlas atlasPlayer;
    private Skin wonRoundsSkin;

    private Stage gameStage;
    private Table playerWonRoundsTable, aiWonRoundsTable, playerHealthTable, aiHealthTable, playerStatsTable, aiStatsTable, movementButtonsTable, prePostScreenTable, pauseTable, jbaButtonsTable; //jba = jump block attack
    private Button buttonLeft, buttonRight, buttonUp, buttonDown, buttonAttack, buttonBlock, buttonJump, buttonPause, buttonMenu, buttonRestart;
    private Image preMatchImage, postMatchImage, postMatchImageKO, postMatchImageTIE, healthBarPlayer, healthBarAI, healthPlayer, healthAI;
    private Image playerDotOne, playerDotTwo, playerDotThree, aiDotOne, aiDotTwo, aiDotThree, playerWonRounds, aiWonRounds;
    private Image[] playerDots, aiDots;

    private boolean gameIsRunning, preMatchIsRunning, gameIsPaused;
    private float gameStartTimer, postMatchTimer;

    ////////////////////////////////////////////////////////////////////////
    private boolean moveRight = false;
    private boolean moveLeft = false;
    private boolean jump = false;
    private boolean standing = true;
    private boolean attackDown = false;
    private boolean attackUp = false;
    private boolean duck = false;
    private boolean attack = false;
    private boolean block = false;
    //////////////////////////////////////////////////////////////////
    private int maxWonGames;
    private int currentlyWonGames;


    // we need the MainClass object to access the SpriteBatch
    public GameClass(MainClass mainClass, int maxWon, int curWon)
    {
        this.mainClass = mainClass;
        background = new Texture("fighter/Arena.png");

        atlasAI = new TextureAtlas(Gdx.files.internal("fighter/AI_Final.pack"));
        atlasPlayer = new TextureAtlas((Gdx.files.internal("fighter/Player_Final.pack")));

        player = new Player(atlasPlayer, GameValues.PLAYER_ORIGINAL_X, GameValues.FIGHTER_ORIGINAL_HEIGHT);
        ai = new AI(0, atlasAI, GameValues.AI_ORIGINAL_X, GameValues.FIGHTER_ORIGINAL_HEIGHT);

        player.updateFacingDirection(ai);
        ai.updateFacingDirection(player);

        gameIsRunning = false;
        preMatchIsRunning = true;
        gameIsPaused = false;
        gameStartTimer = 0;
        postMatchTimer = 0;

        maxWonGames = maxWon;
        currentlyWonGames = curWon;
    }

    //this is like the create() method
    @Override
    public void show()
    {
        setup();
    }

    private void setup()
    {
        gameStage = new Stage();
        Gdx.input.setInputProcessor(gameStage);


        movementButtonsTable = new Table();
        jbaButtonsTable = new Table();
        movementButtonsTable.setBounds(0, 0, 200, 296);
        jbaButtonsTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        prePostScreenTable = new Table();
        prePostScreenTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        playerStatsTable = new Table();
        playerStatsTable.setBounds(0, Gdx.graphics.getHeight() - 214, Gdx.graphics.getWidth(), 200);
        playerStatsTable.left().top();

        aiStatsTable = new Table();
        aiStatsTable.setBounds(0, Gdx.graphics.getHeight() - 200, Gdx.graphics.getWidth(), 200);
        aiStatsTable.right().top();

        playerHealthTable = new Table();
        playerHealthTable.setBounds(13, Gdx.graphics.getHeight() - 131, Gdx.graphics.getWidth(), 100);
        playerHealthTable.left().top();

        aiHealthTable = new Table();
        aiHealthTable.setBounds(0, Gdx.graphics.getHeight() - 131, Gdx.graphics.getWidth() - 113, 100);
        aiHealthTable.right().top();

        pauseTable = new Table();
        pauseTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //TODO aktualisieren
        pauseTable.center();

        setupUpButton();
        movementButtonsTable.row();
        setupLeftButton();
        setupRightButton();
        movementButtonsTable.row();
        setupDownButton();

        setupAttackButton();
        setupBlockButton();
        jbaButtonsTable.row();
        setupJumpButton();

        setupPreMatchImage();
        setupPostMatchImage();

        prePostScreenTable.center();
        movementButtonsTable.right();
        jbaButtonsTable.bottom().right();

        setupHealthBars();
        setupHealth();

        setupPauseButton();
        setupMenuButton();
        setupRestartButton();

        playerWonRoundsTable = new Table();
        playerWonRoundsTable.setBounds(0, Gdx.graphics.getHeight() - 147, healthBarPlayer.getWidth(), 100); //TODO aktualisieren
        playerWonRoundsTable.right().bottom();

        aiWonRoundsTable = new Table();
        aiWonRoundsTable.setBounds(Gdx.graphics.getWidth() - (healthBarAI.getWidth() + buttonPause.getWidth()), Gdx.graphics.getHeight() - 147, healthBarAI.getWidth() + buttonPause.getWidth(), 100); //TODO aktualisieren
        aiWonRoundsTable.left().bottom();

        setupWonRoundsImage();
        setupDots();



        /*prePostScreenTable.debug();
        movementButtonsTable.debug();
        jbaButtonsTable.debug();
        //playerStatsTable.debug();
        //aiStatsTable.debug();
        playerWonRoundsTable.debug();
        aiWonRoundsTable.debug();*/
        gameStage.addActor(prePostScreenTable);
        gameStage.addActor(movementButtonsTable);
        gameStage.addActor(jbaButtonsTable);
        gameStage.addActor(playerWonRoundsTable);
        gameStage.addActor(aiWonRoundsTable);
        gameStage.addActor(aiHealthTable);
        gameStage.addActor(playerHealthTable);
        gameStage.addActor(aiStatsTable);
        gameStage.addActor(playerStatsTable);
        gameStage.addActor(pauseTable);
    }

    private void setupWonRoundsImage()
    {
        TextureAtlas atlas = new TextureAtlas("ui/game/won_rounds/wonRounds.pack");

        Skin skin = new Skin(atlas);

        playerWonRounds = new Image(skin, "wonRounds.up");
        aiWonRounds = new Image(skin, "wonRounds.up");

        playerStatsTable.row();
        playerStatsTable.add(playerWonRounds).align(Align.right);

        aiStatsTable.row();
        aiStatsTable.add(aiWonRounds).align(Align.left);
    }

    private void setupDots()
    {
        TextureAtlas atlas = new TextureAtlas("ui/game/won_rounds/dot.pack");

        wonRoundsSkin = new Skin(atlas);

        playerDotOne = new Image(wonRoundsSkin, "dot.black");
        playerDotTwo = new Image(wonRoundsSkin, "dot.black");
        playerDotThree = new Image(wonRoundsSkin, "dot.black");

        aiDotOne = new Image(wonRoundsSkin, "dot.black");
        aiDotTwo = new Image(wonRoundsSkin, "dot.black");
        aiDotThree = new Image(wonRoundsSkin, "dot.black");

        playerDots = new Image[] {playerDotOne, playerDotTwo, playerDotThree};
        aiDots = new Image[] {aiDotOne, aiDotTwo, aiDotThree};

        addDotsToTable();
    }

    private void addDotsToTable()
    {
        playerWonRoundsTable.clearChildren();
        aiWonRoundsTable.clearChildren();

        playerWonRoundsTable.add(playerDots[0]).spaceRight(5);
        playerWonRoundsTable.add(playerDots[1]).spaceRight(5);
        playerWonRoundsTable.add(playerDots[2]);

        aiWonRoundsTable.add(aiDots[2]).spaceRight(5);
        aiWonRoundsTable.add(aiDots[1]).spaceRight(5);
        aiWonRoundsTable.add(aiDots[0]);
    }

    private void setupMenuButton()
    {
        TextureAtlas atlas = new TextureAtlas("ui/game/buttons/menu.pack");

        Skin skin = new Skin(atlas);

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("menu.up");
        style.down = skin.getDrawable("menu.down");

        buttonMenu = new Button(style);
        buttonMenu.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                mainClass.setScreen(new MainMenu(mainClass));
            }
        });
    }

    private void setupRestartButton()
    {
        TextureAtlas atlas = new TextureAtlas("ui/game/buttons/restart.pack");

        Skin skin = new Skin(atlas);

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = skin.getDrawable("restart.up");
        style.down = skin.getDrawable("restart.down");

        buttonRestart = new Button(style);
        buttonRestart.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                reset();
            }
        });
    }

    private void setupPauseButton()
    {
        TextureAtlas atlas = new TextureAtlas("ui/game/buttons/pause.pack");

        Skin skin = new Skin(atlas);

        Button.ButtonStyle style = new Button.ButtonStyle();

        style.up = skin.getDrawable("pause.up");
        style.down = skin.getDrawable("pause.down");

        buttonPause = new Button(style);
        buttonPause.addListener(new ClickListener()
        {
           @Override
           public void clicked(InputEvent event, float x, float y)
           {
               if(gameIsRunning || preMatchIsRunning)
               {
                   if (!gameIsPaused)
                   {
                       gameIsPaused = true; //TODO activate
                       setupPauseMenu();
                   } else
                   {
                       gameIsPaused = false;
                       pauseTable.clearChildren();
                   }
               }
           }
        });

        aiStatsTable.add(buttonPause);
    }

    private void setupPauseMenu()
    {
        pauseTable.add(buttonRestart);
        pauseTable.row();
        pauseTable.add(buttonMenu);
    }

    private void setupHealthBars()
    {
        TextureAtlas atlas = new TextureAtlas("ui/game/healthbar/healthbar.pack");

        Skin skin = new Skin(atlas);

        healthBarAI = new Image(skin, "healthbar.up");
        healthBarPlayer = new Image(skin, "healthbar.up");

        playerStatsTable.add(healthBarPlayer).spaceBottom(14);
        aiStatsTable.add(healthBarAI);
    }

    private void setupHealth()
    {
        TextureAtlas atlas = new TextureAtlas("ui/game/healthbar/health.pack");

        Skin skin = new Skin(atlas);

        healthPlayer = new Image(skin, "health.up");
        healthAI = new Image(skin, "health.up");
        healthAI.setOrigin(280, 0);

        playerHealthTable.add(healthPlayer);
        aiHealthTable.add(healthAI);
    }

    private void setupPreMatchImage()
    {
        TextureAtlas atlas = new TextureAtlas("ui/game/screen_images/preScreen.pack");

        Skin skin = new Skin(atlas);

        preMatchImage = new Image(skin, "preScreen.up");

        prePostScreenTable.add(preMatchImage);
    }

    private void setupPostMatchImage()
    {
        TextureAtlas atlas = new TextureAtlas("ui/game/screen_images/postScreen.pack");

        Skin skin = new Skin(atlas);

        postMatchImageKO = new Image(skin, "postScreenKO.up");
        postMatchImageTIE = new Image(skin, "postScreenTIE.up");
    }

    private void setupLeftButton()
    {
        TextureAtlas leftButtonAtlas = new TextureAtlas("ui/game/buttons/arrowLeft.pack");

        Skin leftButtonSkin = new Skin(leftButtonAtlas);

        Button.ButtonStyle leftStyle = new Button.ButtonStyle();

        leftStyle.up = leftButtonSkin.getDrawable("arrowLeft.up");
        leftStyle.down = leftButtonSkin.getDrawable("arrowLeft.down");

        buttonLeft = new Button(leftStyle);

        buttonLeft.addListener(new ClickListener()
        {
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
                if (!moveRight)
                {
                    standing = true;
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
        rightStyle.down = rightButtonSkin.getDrawable("arrowRight.down");

        buttonRight = new Button(rightStyle);

        buttonRight.addListener(new ClickListener()
        {
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


                if (!moveLeft)
                {
                    standing = true;
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
        style.down = skin.getDrawable("arrowUp.down");

        buttonUp = new Button(style);
        buttonUp.addListener(new ClickListener()
        {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
            {
                attackUp = true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
            {
                attackUp = false;
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
        style.down = skin.getDrawable("arrowDown.down");

        buttonDown = new Button(style);
        buttonDown.addListener(new ClickListener()
        {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
            {
                if (player.isOnGround())
                {
                    duck = true;
                } else
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
        TextureAtlas atlas = new TextureAtlas("ui/game/buttons/attack.pack"); //jba = jump block attack

        Skin skin = new Skin(atlas);

        Button.ButtonStyle style = new Button.ButtonStyle();

        style.up = skin.getDrawable("attack.up");
        style.down = skin.getDrawable("attack.down");

        buttonAttack = new Button(style);
        buttonAttack.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                attack = true;
                if(player.isFacingLeft())
                {
                    player.moveLeft(GameValues.PLAYER_ATTACK_LEFT_SPEED);
                }
            }
        });

        jbaButtonsTable.add(buttonAttack);
    }

    private void setupBlockButton()
    {
        TextureAtlas atlas = new TextureAtlas("ui/game/buttons/block.pack"); //jba = jump block attack

        Skin skin = new Skin(atlas);

        Button.ButtonStyle style = new Button.ButtonStyle();

        style.up = skin.getDrawable("block.up");
        style.down = skin.getDrawable("block.down");

        buttonBlock = new Button(style);
        buttonBlock.addListener(new ClickListener()
        {
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
        TextureAtlas atlas = new TextureAtlas("ui/game/buttons/jump.pack"); //jba = jump block attack

        Skin skin = new Skin(atlas);

        Button.ButtonStyle style = new Button.ButtonStyle();

        style.up = skin.getDrawable("jump.up");
        style.down = skin.getDrawable("jump.down");

        buttonJump = new Button(style);
        buttonJump.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (player.getY() <= GameValues.FIGHTER_ORIGINAL_HEIGHT)
                {
                    jump = true;
                }
            }
        });

        jbaButtonsTable.add(buttonJump);
    }

    /////////////////////////////////setup end

    //main method
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(gameIsPaused)
        {

        }
        else if (gameIsRunning)
        {
            //actual game is running
            player.updateFacingDirection(ai);
            ai.updateFacingDirection(player);

            ai.act(player, delta);

            checkAcceleration();

            switchPlayerMovementState();
            switchPlayerFightingState(delta);

            collision(player, ai);

            areFightersAlive();
        }
        else if (preMatchIsRunning)
        {
            //pre match display is shown, game will start
            gameStartTimer += delta;
            if (gameStartTimer >= GameValues.GAME_START_TIME)
            {
                startGame();
            }
        }
        else if(!(player.hasWonGame() || ai.hasWonGame()))
        {
            postMatchTimer += delta;
            if(postMatchTimer >= GameValues.GAME_RESET_TIME)
            {
                postMatchTimer = 0;
                reset();
            }
        }

        if(!gameIsPaused && (gameIsRunning || preMatchIsRunning))
        {
            player.update(delta);
            ai.update(delta);
        }

        mainClass.getSpriteBatch().begin();
        mainClass.getSpriteBatch().draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        mainClass.getSpriteBatch().draw(player, player.getX(), player.getY());
        mainClass.getSpriteBatch().draw(ai, ai.getX(), ai.getY());
        mainClass.getSpriteBatch().end();


        gameStage.act(delta);
        gameStage.draw();
    }

    private void reset()
    {
        if(gameIsPaused)
        {
            gameIsPaused = false;
            pauseTable.clearChildren();
            currentlyWonGames = 0;
            writeFile();
        }
        gameIsRunning = false;
        preMatchIsRunning = true;
        gameStartTimer = 0;
        prePostScreenTable.clearChildren();
        prePostScreenTable.add(preMatchImage);

        moveRight = false;
        moveLeft = false;
        jump = false;
        standing = true;
        attackDown = false;
        attackUp = false;
        duck = false;
        attack = false;
        block = false;

        if(player.hasWonGame() || ai.hasWonGame())
        {
            player.resetWonRounds();
            ai.resetWonRounds();
            setupDots();
        }

        player.reset(GameValues.PLAYER_ORIGINAL_X);
        ai.reset(GameValues.AI_ORIGINAL_X);

        healthPlayer.setScaleX(1f);
        healthAI.setScaleX(1f);

        player.updateFacingDirection(ai);
        ai.updateFacingDirection(player);
    }

    private void startGame()
    {
        gameIsRunning = true;
        preMatchIsRunning = false;
        prePostScreenTable.clearChildren();
    }

    private void checkAcceleration()
    {
        float accY = Gdx.input.getAccelerometerX();
        float accX = Gdx.input.getAccelerometerY();
        //Gdx.app.log("AccX", Float.toString(accX));
        //Gdx.app.log("AccY", Float.toString(accY));
        if (Math.abs(accX) > GameValues.GAME_ACCELEROMETER_X_MIN_SPEED)
        {
            if (Math.abs(accY) > Math.abs(accX) && !attack && !block)
            {
                //moved mobile phone faster in y-direction than in x-direction
                //attack up or down
                if (accY > 0 && !attackDown)
                {
                    //attack up
                    attackUp = true;
                    attack = true;
                    //Gdx.app.log("AccN", "attack Up" + accY);
                } else if (!player.isOnGround() && !attackUp)
                {
                    //attack down
                    attackDown = true;
                    attack = true;
                    //Gdx.app.log("AccN", "attack down" + accY);
                }
            } else
            {
                //attack left/right
                if (!attackUp && !attackDown && !block)
                {
                    if(accX > 0 && player.isFacingLeft())
                    {
                        attack = true;
                    }
                    else if(accX < 0 && !player.isFacingLeft())
                    {
                        attack = true;
                    }
                    //Gdx.app.log("AccN", "attack " + accX);
                }
            }
        } else if (Math.abs(accY) > GameValues.GAME_ACCELEROMETER_Y_MIN_SPEED)
        {
            if (!attack && !block)
            {
                if (accY > 0 && !attackDown)
                {
                    attackUp = true;
                    attack = true;
                    //Gdx.app.log("AccN", "attack Up" + accY);
                } else if (!player.isOnGround() && !attackUp)
                {
                    attackDown = true;
                    attack = true;
                    //Gdx.app.log("AccN", "attack down" + accY);
                }
            }
        }
    }

    /*
        checks if one of the fighters is K.O. and ends the game
     */
    private void areFightersAlive()
    {
        if (!player.isAlive())
        {
            if (!ai.isAlive())
            {
                tie();
            }
            else
            {
                KO();
                hasWon(ai);
                currentlyWonGames = 0;
                writeFile();
            }
            gameIsRunning = false;

            if(player.hasWonGame() || ai.hasWonGame())
            {
                showPostMatchScreen();
            }
            else
            {
                showPostRoundScreen();
            }
        }
        else if (!ai.isAlive())
        {
            KO();
            hasWon(player);
            currentlyWonGames++;
            writeFile();
            gameIsRunning = false;


            if(player.hasWonGame() || ai.hasWonGame())
            {
                showPostMatchScreen();
            }
            else
            {
                showPostRoundScreen();
            }
        }
    }

    /*
        updates the displayed health bars
     */
    private void updateHealth()
    {
        float health = player.getHealth();
        health /= 100;
        healthPlayer.setScaleX(health);

        health = ai.getHealth();
        health /= 100;
        healthAI.setScaleX(health);
    }

    private void hasWon(Fighter winner)
    {
        winner.roundWon();
        updateDisplayedWonGames();
    }

    private void updateDisplayedWonGames()
    {
        playerWonRoundsTable.clearChildren();
        aiWonRoundsTable.clearChildren();

        if(player.getWonRounds() > 0)
        {

            playerDots[player.getWonRounds() - 1] = new Image(wonRoundsSkin, "dot.red");
        }
        if(ai.getWonRounds() > 0)
        {
            aiDots[ai.getWonRounds() - 1] = new Image(wonRoundsSkin, "dot.red");
        }

        addDotsToTable();
    }

    private void tie()
    {
        postMatchImage = postMatchImageTIE;
    }

    private void KO()
    {
        postMatchImage = postMatchImageKO;
    }

    private void showPostMatchScreen()
    {
        prePostScreenTable.add(postMatchImage);
        prePostScreenTable.row();
        prePostScreenTable.add(buttonRestart);
        prePostScreenTable.row();
        prePostScreenTable.add(buttonMenu);
    }

    private void showPostRoundScreen()
    {
        prePostScreenTable.add(postMatchImage);
    }


    /*
        checks collision of player and AI
     */
    private void collision(Player player, AI ai)
    {
        if (Intersector.overlaps(player.getRectangle(), ai.getRectangle()))
        {
            Random random = new Random();
            if (player.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK && !(ai.getCurrentFightingState() == Fighter.FighterFightingState.BLOCK || ai.getCurrentMovementState() == Fighter.FighterMovementState.DUCKING))
            {
                //player attacked nomal, ai couldn't block
                ai.takeDamage(GameValues.PLAYER_DAMAGE);
                if(random.nextInt(100) <= GameValues.FIGHTER_STUN_CHANCE)
                {
                    ai.stun(true);
                }
            } else if (ai.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK && !(player.getCurrentFightingState() == Fighter.FighterFightingState.BLOCK || player.getCurrentMovementState() == Fighter.FighterMovementState.DUCKING))
            {
                //ai attacked normal, player couldn't block
                player.takeDamage(GameValues.AI_DAMAGE);
                if(random.nextInt(100) <= GameValues.FIGHTER_STUN_CHANCE)
                {
                    player.stun(true);
                }
            } else if (player.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_DOWN && !(ai.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_UP))
            {
                //player attacked from above
                ai.takeDamage(GameValues.PLAYER_DAMAGE);
                if(random.nextInt(100) <= GameValues.FIGHTER_STUN_CHANCE)
                {
                    ai.stun(true);
                }
            } else if (ai.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_DOWN && !(player.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_UP))
            {
                //ai attacked from above
                player.takeDamage(GameValues.AI_DAMAGE);
                if(random.nextInt(100) <= GameValues.FIGHTER_STUN_CHANCE)
                {
                    player.stun(true);
                }
            } else if (player.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_UP && !(ai.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_DOWN))
            {
                //player attacked from underneath
                ai.takeDamage(GameValues.PLAYER_DAMAGE);
                if(random.nextInt(100) <= GameValues.FIGHTER_STUN_CHANCE)
                {
                    ai.stun(true);
                }
            } else if (ai.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_UP && !(player.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_DOWN))
            {
                //ai attacked from underneath
                player.takeDamage(GameValues.AI_DAMAGE);
                if(random.nextInt(100) <= GameValues.FIGHTER_STUN_CHANCE)
                {
                    player.stun(true);
                }
            } else if ((ai.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_UP || ai.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_DOWN || ai.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK) && (player.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_DOWN || player.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK_UP || player.getCurrentFightingState() == Fighter.FighterFightingState.ATTACK))
            {
                //both attacked at the same time
                ai.takeDamage(GameValues.PLAYER_DAMAGE);
                player.takeDamage(GameValues.AI_DAMAGE);

                if(random.nextInt(100) <= GameValues.FIGHTER_STUN_CHANCE)
                {
                    ai.stun(true);
                    player.stun(true);
                }
            }
            updateHealth();
        }
    }

    private void switchPlayerFightingState(float delta)
    {
        if (attackUp && attack)
        {
            attackUp = player.attackUp(delta);
            attack = attackUp;
            player.setFightingState(Player.FighterFightingState.ATTACK_UP);
        } else if (attackDown && attack)
        {
            attackDown = player.attackDown(delta);
            attack = attackDown;
            player.setFightingState(Player.FighterFightingState.ATTACK_DOWN);
        } else if (attack)
        {
            attack = player.attack(delta);
            player.setFightingState(Player.FighterFightingState.ATTACK);
        } else if (block)
        {
            block = player.block(delta);
            player.setFightingState(Player.FighterFightingState.BLOCK);
        } else
        {
            player.setFightingState(Player.FighterFightingState.NONE);
        }
    }

    private void switchPlayerMovementState()
    {
        if (duck)
        {
            player.setMovementState(Player.FighterMovementState.DUCKING);
        } else if (moveRight)
        {
            if(player.getX() + player.getRegionWidth() + GameValues.PLAYER_MOVING_SPEED < ai.getX() || player.isFacingLeft() || !player.isOnGround() || !ai.isOnGround())
            {
                player.moveRight(GameValues.PLAYER_MOVING_SPEED);
            }
            player.setMovementState(Player.FighterMovementState.MOVINGRIGHT);
        }
        else if (moveLeft)
        {
            if(player.getX() - GameValues.PLAYER_MOVING_SPEED > ai.getX() + ai.getRegionWidth() || !player.isFacingLeft() || !player.isOnGround() || !ai.isOnGround())
            {
                player.moveLeft(GameValues.PLAYER_MOVING_SPEED);
            }
            player.setMovementState(Player.FighterMovementState.MOVINGLEFT);
        }
        else if (standing)
        {
            player.setMovementState(Player.FighterMovementState.STANDING);
        }

        if (jump && !duck)
        {
            Gdx.app.log("Player", "jump");
            player.setMovementState(Player.FighterMovementState.JUMPING);
            jump = player.jump();
        }
        else if (player.getY() > GameValues.FIGHTER_ORIGINAL_HEIGHT)
        {
            Gdx.app.log("Player", "fall");
            player.setMovementState(Player.FighterMovementState.JUMPING);
            player.fall();
        }
    }

    ////////////////////////////File

    /*
        writes the current highscore and the current amount of won games in a file
        for permanent save
     */
    private void writeFile()
    {
        checkCurrentlyWonGames();
        if(Gdx.files.isLocalStorageAvailable())
        {
            FileHandle fileHandle = Gdx.files.local("data/scores.txt");
            fileHandle.writeString(Integer.toString(maxWonGames) + " " + Integer.toString(currentlyWonGames), false);
        }
    }

    /*
        checks if the amount of currently won games is higher than the old highscore
     */
    private void checkCurrentlyWonGames()
    {
        if(currentlyWonGames > maxWonGames)
        {
            maxWonGames = currentlyWonGames;
        }
    }

    public int getMaxWonGames()
    {
        return maxWonGames;
    }

    public int getCurrentlyWonGames()
    {
        return currentlyWonGames;
    }

    ///////////////////////////////File end

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
