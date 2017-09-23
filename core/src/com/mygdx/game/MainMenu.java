package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


/**
 * Created by AboutWhiteR on 12.08.2017.
 */

public class MainMenu implements Screen, InputProcessor {
    //do we need to get the spriteBatch
    private MainClass mainClass;
    GameClass game;

    private Stage menuStage;
    private Table menuStartButtonTable, menuBottomButtonsTable, highscoreMenuTable, highscoreScoreTable;
    private Image highscoreMenuImage;
    private Label scoreLabel;

    private Button menuHighscoreButton, menuStartButton, menuTutorialButton;

    private Music backgroundMusic;

    private boolean isHighscoreDisplayed, isTutorialDisplayed;

    ///File
    private int maxWonGames;
    private int currentlyWonGames;



   // we need the MainClass Objekt to access the SpriteBatch
   public MainMenu(MainClass mainClass){
        this.mainClass = mainClass;
    }


    //this is like the create() method instantiate all objects we need
    @Override
    public void show()
    {
        setup();
    }

    private void setup()
    {
        menuStage = new Stage();
        Gdx.input.setInputProcessor(menuStage);

        menuStartButtonTable = new Table();
        menuBottomButtonsTable = new Table();
        menuStartButtonTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menuBottomButtonsTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        highscoreMenuTable = new Table();
        highscoreScoreTable = new Table();
        highscoreMenuTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        highscoreScoreTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        setupTutorialButton();
        setupHighscoreButton();
        setupStartButton();
        setupBackgroundImage();
        setupHighscoreMenu();

        highscoreMenuTable.center();
        highscoreScoreTable.center();
        menuBottomButtonsTable.bottom().left();

        menuStage.addActor(menuStartButtonTable);
        menuStage.addActor(menuBottomButtonsTable);
        menuStage.addActor(highscoreMenuTable);
        menuStage.addActor(highscoreScoreTable);

        isHighscoreDisplayed = false;
        isTutorialDisplayed = false;

        maxWonGames = 0;
        currentlyWonGames = 0;
        readFile();
        game = new GameClass(mainClass, maxWonGames, currentlyWonGames);

        setupMusic();
    }

    private void setupMusic()
    {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu_music.ogg"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    private void setupHighscoreScore()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("ui/menu/font/tarzan.fnt"));
        labelStyle.fontColor = Color.BLACK;
        scoreLabel = new Label(Integer.toString(maxWonGames), labelStyle);

    }

    private void setupHighscoreMenu()
    {
        TextureAtlas atlas = new TextureAtlas("ui/menu/highscoreMenu.pack");

        Skin skin = new Skin(atlas);

        highscoreMenuImage = new Image(skin, "highscoreMenu.up");
    }

    private void setupBackgroundImage()
    {
        TextureAtlas atlas = new TextureAtlas("ui/menu/menuBackground.pack");

        Skin skin = new Skin(atlas);

        menuStartButtonTable.background(skin.getDrawable("menuBackground.up"));
    }

    private void setupStartButton()
    {
        TextureAtlas menuStartButtonAtlas = new TextureAtlas("ui/menu/menuStartButton.pack");
        Skin menuStartButtonSkin = new Skin(menuStartButtonAtlas);

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = menuStartButtonSkin.getDrawable("menuStartButton.up");
        style.down = menuStartButtonSkin.getDrawable("menuStartButton.down");
        style.pressedOffsetX = 1;
        style.pressedOffsetY = -1;

        menuStartButton = new Button(style);
        menuStartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                backgroundMusic.dispose();
                game = new GameClass(mainClass, maxWonGames, currentlyWonGames);
                mainClass.setScreen(game);
            }
        });

        menuStartButtonTable.add(menuStartButton);
    }

    private void setupTutorialButton()
    {
        TextureAtlas menuTutorialButtonAtlas = new TextureAtlas("ui/menu/tutorial.pack");
        Skin menuTutorialButtonSkin = new Skin(menuTutorialButtonAtlas);

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = menuTutorialButtonSkin.getDrawable("tutorial.up");
        style.down = menuTutorialButtonSkin.getDrawable("tutorial.down");

        menuTutorialButton = new Button(style);
        menuTutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                //TODO set Screen for Tutorial
            }
        });

        menuBottomButtonsTable.add(menuTutorialButton);
    }

    private void setupHighscoreButton()
    {
        TextureAtlas menuSettingsButtonAtlas = new TextureAtlas("ui/menu/highscoreButton.pack");
        Skin menuSettingsButtonSkin = new Skin(menuSettingsButtonAtlas);

        final Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = menuSettingsButtonSkin.getDrawable("highscoreButton.up");
        style.down = menuSettingsButtonSkin.getDrawable("highscoreButton.down");

        menuHighscoreButton = new Button(style);
        menuHighscoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(isHighscoreDisplayed)
                {
                    isHighscoreDisplayed = false;
                    highscoreMenuTable.clearChildren();
                    highscoreScoreTable.clearChildren();
                }
                else
                {
                    setupHighscoreScore();
                    isHighscoreDisplayed = true;
                    highscoreMenuTable.add(highscoreMenuImage);
                    highscoreScoreTable.add(scoreLabel);
                }
            }
        });

        menuBottomButtonsTable.add(menuHighscoreButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        maxWonGames = game.getMaxWonGames();
        currentlyWonGames = game.getCurrentlyWonGames();

        menuStage.act(delta);
        menuStage.draw();
    }


    ////////////File
    private void readFile()
    {
        if(Gdx.files.isLocalStorageAvailable())
        {
            FileHandle fileHandle = Gdx.files.local("data/scores.txt");
            if(Gdx.files.local("data/scores.txt").exists())
            {
                String content = fileHandle.readString();
                stringToInt(content);
            }
        }
    }

    /*
        converts the string from the file into int and sets maxWonGames and currentlyWonGames
     */
    private void stringToInt(String string)
    {
        char[] charArray = string.toCharArray();
        boolean firstPart = true;
        String maxWon = "";
        String currentlyWon = "";
        for(int i = 0;i < charArray.length; i++)
        {
            if(charArray[i] == ' ')
            {
                firstPart = false;
            }
            else if(firstPart)
            {
                maxWon += charArray[i];
            }
            else
            {
                currentlyWon += charArray[i];
            }
        }

        maxWonGames = Integer.parseInt(maxWon);
        currentlyWonGames = Integer.parseInt(currentlyWon);
    }



    //also methods of Screen interface
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



    //methods of InputProcessor (interface)

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {



        //mainClass.setScreen(new GameClass(mainClass));
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }



    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
