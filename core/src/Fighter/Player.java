package Fighter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by david on 12.08.2017.
 */

//ATM NUR EIN BILD DAS SICH NACH LINKS UND RECHTS BEWEGEN KANN ABER DER REST KOMMT NOCH
public class Player extends Sprite {

    public Player( Sprite sprite, float x, float y){
        super(sprite);
        setPosition(x,y);

    }


    public void moveRight(int x){
        translateX(x);
    }

    public void moveLeft(int x){
        int negX = x* (-1);
        translateX (negX);
    }

}
