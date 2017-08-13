package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;



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


    //fake Button
    Texture img;
    Sprite sprite;



   // we need the MainClass Objekt to access the SpriteBatch
   public MainMenu(MainClass mainClass){
        this.mainClass = mainClass;
    }




    //this is like the create() method instantiate all objects we need
    @Override
    public void show() {
        img = new Texture(Gdx.files.internal("button.png"));
        sprite = new Sprite(img);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        //before render clear display --> bin mir nicht sicher ob man das wirklich braucht hab ich nur wo gelesen aber
        // keine Begründung und auch noch nicht nachgelesen
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        mainClass.getSpriteBatch().begin();
        sprite.draw(mainClass.getSpriteBatch());
        mainClass.getSpriteBatch().end();
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
        mainClass.setScreen(new GameClass(mainClass));
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
