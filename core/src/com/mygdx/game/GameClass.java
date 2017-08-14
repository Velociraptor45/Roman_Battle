package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

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
    private World world;
    private TestGround ground;
    private OrthographicCamera camera;
    private Box2DDebugRenderer dDebugRenderer;
    private int PPM = 100; //TODO auslagern





    // we need the MainClass Objekt to access the SpriteBatch
    public GameClass(MainClass mainClass){
        this.mainClass = mainClass;
        Gdx.input.setInputProcessor(new GestureDetector (this));

        camera = new OrthographicCamera();
        camera.setToOrtho(false,Gdx.graphics.getWidth()/PPM,Gdx.graphics.getHeight()/PPM);
        camera.position.set(Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight()/2f,0f);

        dDebugRenderer = new Box2DDebugRenderer();

        world = new World(new Vector2(0,-9.8f),true);
        player = new Player(world,"14668.png",400, 300);//TODO BILD
        ground = new TestGround(world);
    }

    //this is like the create() method
    @Override
    public void show() {
    }




    @Override
    public void render(float delta) {
        player.updatePlayer();

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        mainClass.getSpriteBatch().begin();
        mainClass.getSpriteBatch().draw(player,player.getX()-player.getWidth()/2f,
                player.getY() - player.getHeight()/2f);


        mainClass.getSpriteBatch().draw(ground,0,0);
        mainClass.getSpriteBatch().end();


        dDebugRenderer.render(world,camera.combined);

        world.step(Gdx.graphics.getDeltaTime(),6,2);


        ground = new TestGround(world);

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
        if(Math.abs(velocityX)>Math.abs(velocityY)){
            if(velocityX>0){
                player.getBody().applyLinearImpulse(new Vector2(6,0),player.getBody().getWorldCenter(),true);
            }else{
                player.getBody().applyLinearImpulse(new Vector2(-6,0),player.getBody().getWorldCenter(),true);
            }
        }else{
            if(velocityY>0){

            }else{
                player.getBody().applyLinearImpulse(new Vector2(0,6f),player.getBody().getWorldCenter(),true);
            }
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
