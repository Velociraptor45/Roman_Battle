package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



// IN DIESER KLASSE BRAUCHEN WIR NICHTS MEHR MACHEN ---- diesen kommentar können wir dann löschen !!!!!!!!!!!

// extending Game helps to manage different Screen it´s the "root" of the game or entry-point
public class MainClass extends Game  {
    //serves as container to draw bunch of graphics, for this reason instantiate only once for all classes
    //is always used in the render-methods of each class
    private SpriteBatch spriteBatch;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();

        //this Method starts the MainMenu-Class
        setScreen(new MainMenu(this));
    }

    @Override
    public void render(){
        //this do we need to render all the screens (e.g. what we draw in the render method in the
        //MainMenu class wouldn´t be visible without this
        super.render();
    }


    public SpriteBatch getSpriteBatch(){
        return spriteBatch;
    }
}













