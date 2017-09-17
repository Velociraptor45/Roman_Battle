package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import Constants.GameValues;


/**
 * Created by AboutWhiteR on 12.08.2017.
 */


//HIER ENTSTEHT DAS HAUPTMENÜ ICH HABE NUR EINEN PSEUDOBUTTON EINGEFÜGT KLICKT MAN DIESEN (ODER IRGENDWO ANDERS AUF DEM BILDSCHIRM)
//STARTET DAS SPIEL MAN BEFINDET SICH DANN IN DER KLASSE GAME DIE AUCH WIEDER SCREEN IMPLEMENTIERT
//DIESEN KOMMENTAR KÖNNEN WIR DANN LÖSCHEN IST NUR FÜR EUCH btw der Inputprocessor erkennt userinput und kann den verarbeiten
// multitouch würde man aber noch etwas anders verarbeiten mit multiplex oder so hab das noch nicht so genau gelesen


public class MainMenu implements Screen, InputProcessor {
    //do we need to get the spriteBatch
    private MainClass mainClass;
    GameClass game;

    private Stage menuStage;
    private Table menuStartButtonTable, menuTopButtonsTable;

    private TextButton menuTutorialButton;
    private Button menuSettingsButton, menuStartButton;

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
        menuTopButtonsTable = new Table();
        menuStartButtonTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menuTopButtonsTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        setupTutorialButton();
        setupSettingsButton();
        setupStartButton();

        menuTopButtonsTable.top().left();

        menuStartButtonTable.debug();
        menuTopButtonsTable.debug();
        menuStage.addActor(menuStartButtonTable);
        menuStage.addActor(menuTopButtonsTable);

        maxWonGames = 0;
        currentlyWonGames = 0;
        readFile();
        game = new GameClass(mainClass, maxWonGames, currentlyWonGames);
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
                game = new GameClass(mainClass, maxWonGames, currentlyWonGames);
                mainClass.setScreen(game);
            }
        });

        menuStartButtonTable.add(menuStartButton);
    }

    private void setupTutorialButton()
    {
        TextureAtlas menuTutorialButtonAtlas = new TextureAtlas("ui/menu/menuTutorialButton.pack");
        Skin menuTutorialButtonSkin = new Skin(menuTutorialButtonAtlas);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = menuTutorialButtonSkin.getDrawable("menuTutorialButton.up");
        style.down = menuTutorialButtonSkin.getDrawable("menuTutorialButton.up");
        style.font = new BitmapFont(false); //TODO richtige Schriftart auswählen und über Bitmap setzen
        style.fontColor = Color.BLACK;
        style.pressedOffsetX = 1;
        style.pressedOffsetY = -1;

        menuTutorialButton = new TextButton("?", style);
        menuTutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                //TODO set Screen for Setting
            }
        });

        menuTopButtonsTable.add(menuTutorialButton);
    }

    private void setupSettingsButton()
    {
        TextureAtlas menuSettingsButtonAtlas = new TextureAtlas("ui/menu/menuSettingsButton.pack");
        Skin menuSettingsButtonSkin = new Skin(menuSettingsButtonAtlas);

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = menuSettingsButtonSkin.getDrawable("menuSettingsButton.up");
        style.down = menuSettingsButtonSkin.getDrawable("menuSettingsButton.up");
        style.pressedOffsetX = 1;
        style.pressedOffsetY = -1;

        menuSettingsButton = new Button(style);
        menuSettingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                //TODO set screen for Setting
            }
        });

        menuTopButtonsTable.add(menuSettingsButton);
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
