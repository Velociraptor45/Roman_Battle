package Fighter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PixmapPackerIO;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by AboutWhiteR on 14.08.2017.
 */

public class TestGround extends Sprite  {

    private World world;
    private Body body;
    private int PPM=100;

    public TestGround (World world){
        super(new Texture("ground.png"));
        this.world = world;
        setPosition(Gdx.graphics.getWidth()/2,getHeight()/2);
        createBody();
    }

    private void createBody(){
        BodyDef bodyDef = new BodyDef();

        bodyDef.type= BodyDef.BodyType.StaticBody;

        bodyDef.position.set(getX() / PPM,getY()/ PPM);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(    getWidth()  ,  ( getHeight()/2f)  /PPM  );

        FixtureDef fixtureDef= new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;

        Fixture fixture= body.createFixture(fixtureDef);
        shape.dispose();

    }



}
