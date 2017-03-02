package ru.aslanisl.battletanks.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import ru.aslanisl.battletanks.BattleTanks;
import ru.aslanisl.battletanks.Screens.PlayScreen;


public class Bullet{

    //Box2d
    private Body bullet1;
    private Body bullet2;
    private World world;

    private Tank tank;

    private boolean bulletOneToDestroy;
    private boolean bulletTwoToDestroy;

    private Animation explosionAnimation;
    private float stateTimer;

    public Bullet (PlayScreen screen, Tank tank) {

        this.world = screen.getWorld();
        this.tank = tank;

        defineBulletOne();
        defineBulletTwo();

        //Толкаем пулю от танка
        bullet1.setLinearVelocity(((tank.getGun1X()) - tank.b2body.getPosition().x) * 25,
                ((tank.getGun1Y()) - tank.b2body.getPosition().y) * 25);
        bullet2.setLinearVelocity(((tank.getGun2X()) - tank.b2body.getPosition().x) * 25,
                ((tank.getGun2Y()) - tank.b2body.getPosition().y) * 25);

    }

    public void defineBulletOne() {

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;

        bdef.position.set(tank.getGun1X(), tank.getGun1Y());

        bullet1 = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / BattleTanks.PPM);
        fdef.filter.categoryBits = BattleTanks.BULLET_ONE_BIT;
        fdef.filter.maskBits = BattleTanks.BRICK_BIT |
                BattleTanks.WALL_BIT ;

        fdef.shape = shape;
        bullet1.createFixture(fdef).setUserData(this);

        shape.dispose();
    }
    public void defineBulletTwo() {

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;

        bdef.position.set(tank.getGun2X(), tank.getGun2Y());
        bullet2 = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / BattleTanks.PPM);
        fdef.filter.categoryBits = BattleTanks.BULLET_TWO_BIT;
        fdef.filter.maskBits = BattleTanks.BRICK_BIT |
                BattleTanks.WALL_BIT ;

        fdef.shape = shape;
        bullet2.createFixture(fdef).setUserData(this);

        shape.dispose();
    }

    public void update () {
        if (bulletOneToDestroy) {
            world.destroyBody(bullet1);
            bulletOneToDestroy = false;
            stateTimer = 0;
        } else if (bulletTwoToDestroy) {
            world.destroyBody(bullet2);
            bulletTwoToDestroy = false;
            stateTimer = 0;
        }
    }

    public void draw (float dt, SpriteBatch batch) {

    }

    public void onContactBulletOne () {
        bulletOneToDestroy = true;
    }
    public void onContactBulletTwo () {
        bulletTwoToDestroy = true;
    }
    public Body getBullet2() {
        return bullet2;
    }

    public Body getBullet1() {
        return bullet1;
    }
}
