package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import Fighter.Player;

/**
 * Created by david on 12.08.2017.
 */

// This class represents the GameScreen where the Game is played
// Here are control elements, player, enemy....

public class GameClass implements Screen, GestureDetector.GestureListener {

    //private AI enemy;
    private TextureAtlas textureAtlas;
    private Player player;
    private Sprite sprite;
    private MainClass mainClass;


    // we need the MainClass Objekt to access the SpriteBatch
    public GameClass(MainClass mainClass){
        this.mainClass = mainClass;

    }



    //this is like the create() method
    @Override
    public void show() {
        Gdx.input.setInputProcessor(new GestureDetector (this));

        //textureAtlas contains all pictures and is in assets folder
        //14668 is the name of the first picture
        textureAtlas = new TextureAtlas(Gdx.files.internal("textureAtlas.pack"));
        sprite=textureAtlas.createSprite("14668");

        player = new Player(sprite,20,20);
    }



    @Override
    public void render(float delta) {
        //before render clear display
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        mainClass.getSpriteBatch().begin();
        player.draw(mainClass.getSpriteBatch());
        mainClass.getSpriteBatch().end();


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
    public boolean fling(float velocityX, float velocityY, int button) {
        if (velocityX > 0){
            //move right
            //player.translateX(80);
            player.moveRight(80);


        }
        if (velocityX <0){
            //move left
            //player.translateX(-80);
            player.moveLeft(80);
        }

        return true;
    }

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
