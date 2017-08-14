package Fighter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


/**
 * Created by david on 12.08.2017.
 */


public class Player extends Sprite {

    private World world;
    private Body body;
    private int PPM= 100;


    public Player(World world, String name, float x, float y ){
        super(new Texture(name));
        this.world = world;
        setPosition(x - getWidth()/2f ,y - getHeight() / 2f);
        createBody();
    }

    private void createBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX()/PPM, getY()/PPM);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/2f /PPM, getHeight()/2f /PPM);//TODO klammern

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density=0;
        fixtureDef.friction=0.7f;

        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void updatePlayer(){
        this.setPosition(body.getPosition().x*PPM, body.getPosition().y*PPM);
    }

    public Body getBody(){
        return body;
    }




}
