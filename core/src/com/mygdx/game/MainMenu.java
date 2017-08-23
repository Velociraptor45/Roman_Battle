package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
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


/**
 * Created by AboutWhiteR on 12.08.2017.
 */


//HIER ENTSTEHT DAS HAUPTMENÜ ICH HABE NUR EINEN PSEUDOBUTTON EINGEFÜGT KLICKT MAN DIESEN (ODER IRGENDWO ANDERS AUF DEM BILDSCHIRM)
//STARTET DAS SPIEL MAN BEFINDET SICH DANN IN DER KLASSE GAME DIE AUCH WIEDER SCREEN IMPLEMENTIERT
//DIESEN KOMMENTAR KÖNNEN WIR DANN LÖSCHEN IST NUR FÜR EUCH btw der Inputprocessor erkennt userinput und kann den verarbeiten
// multitouch würde man aber noch etwas anders verarbeiten mit multiplex oder so hab das noch nicht so genau gelesen


public class MainMenu implements Screen ,InputProcessor {
    //do we need to get the spriteBatch
    private MainClass mainClass;

    private Stage menuStage;
    private Table menuStartButtonTable, menuTopButtonsTable;

    private TextureAtlas menuStartButtonAtlas, menuTutorialButtonAtlas, menuSettingsButtonAtlas;
    private Skin menuStartButtonSkin, menuTutorialButtonSkin, menuSettingsButtonSkin;

    private TextButton menuStartButton, menuTutorialButton;
    private Button menuSettingsButton;


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
    }

    private void setupStartButton()
    {
        menuStartButtonAtlas = new TextureAtlas("ui/menu/menuStartButton.pack"); //TODO neue Grafik für Buttons
        menuStartButtonSkin = new Skin(menuStartButtonAtlas);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = menuStartButtonSkin.getDrawable("menuStartButton.up");
        style.down = menuStartButtonSkin.getDrawable("menuStartButton.down");
        style.font = new BitmapFont(false); //TODO richtige Schriftart auswählen und über Bitmap setzen
        style.fontColor = Color.BLACK;
        style.pressedOffsetX = 1;
        style.pressedOffsetY = -1;

        menuStartButton = new TextButton("Start", style);
        menuStartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                mainClass.setScreen(new GameClass(mainClass));
            }
        });

        menuStartButtonTable.add(menuStartButton);
    }

    private void setupTutorialButton()
    {
        menuTutorialButtonAtlas = new TextureAtlas("ui/menu/menuTutorialButton.pack");
        menuTutorialButtonSkin = new Skin(menuTutorialButtonAtlas);

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
        menuSettingsButtonAtlas = new TextureAtlas("ui/menu/menuSettingsButton.pack");
        menuSettingsButtonSkin = new Skin(menuSettingsButtonAtlas);

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
        //before render clear display --> bin mir nicht sicher ob man das wirklich braucht hab ich nur wo gelesen aber
        // keine Begründung und auch noch nicht nachgelesen
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        menuStage.act(delta);
        menuStage.draw();

        /*mainClass.getSpriteBatch().begin();
        mainClass.getSpriteBatch().end();*/
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
