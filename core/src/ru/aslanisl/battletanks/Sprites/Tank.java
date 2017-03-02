package ru.aslanisl.battletanks.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import ru.aslanisl.battletanks.BattleTanks;
import ru.aslanisl.battletanks.Screens.PlayScreen;

public class Tank extends Sprite {
    //Textures
//    private TextureRegion tank;
//    private Rectangle tankBounds;
//    private Vector2 previousPosition;
//
//    private Sprite gun;

    //Box2D
    public World world;
    public Body b2body;

    private float velocityX, velocityY;
    private float speed = 4;

    private Texture tank;
    private TextureRegion tankRegion;

    private float rotation = 0;

    //Пушка
    private Sprite gun1, gun2;
    private Texture gunsRedTexture;
    private TextureRegion gunsRedTextureRegion;
    private float gun1Rotation;
    private float gun2Rotation = 180;

    public Tank(PlayScreen screen, float positionX, float positionY) {

        this.world = screen.getWorld();
        tank = new Texture(Gdx.files.internal("sprites/Tank_Blue.png"));
        tankRegion = new TextureRegion(tank);


        gunsRedTexture = new Texture(Gdx.files.internal("sprites/Red.png"));
        gunsRedTextureRegion = new TextureRegion(gunsRedTexture);

//        gunsTexture = new Texture(Gdx.files.internal("sprites/sprites.png"));
//        gunsTextureRegion = new TextureRegion(gunsTexture, 0, 64, 64, 64);

        setRegion(tankRegion, 0, 0, 64, 64);
        setBounds(0, 0, 64  / BattleTanks.PPM, 64  / BattleTanks.PPM);

//        setPosition(100, 100);
//        previousPosition = new Vector2();
//
//        tankBounds = new Rectangle(getX()+10, getY()+10, getWidth()-10, getHeight()-10);
//
//        //Создание пушки
//        gun = new Sprite(screen.getAtlas().findRegion("tank_and_gun"), 0, 64, 64, 64);
//        gun.setPosition(100, 100);

        defineTank(positionX, positionY);
        defineGuns(screen);

    }

    public void defineTank (float positionX, float positionY){

        BodyDef bdef = new BodyDef();
        bdef.position.set(positionX  / BattleTanks.PPM, positionY  / BattleTanks.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(32 / BattleTanks.PPM);
        fdef.filter.categoryBits = BattleTanks.TANK_BIT;
        fdef.filter.maskBits = BattleTanks.BRICK_BIT |
                BattleTanks.WALL_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        shape.dispose();
    }

    public void defineGuns (PlayScreen screen) {

        gun1 = new Sprite(gunsRedTextureRegion);
        gun2 = new Sprite(gunsRedTextureRegion);

        gun1.setBounds(0, 0, 20 / BattleTanks.PPM, 20 / BattleTanks.PPM);
        gun2.setBounds(0, 0, 20 / BattleTanks.PPM, 20 / BattleTanks.PPM);
    }

    @Override
    public void draw(Batch batch) {

        super.draw(batch);

        gun1.draw(batch);
        gun2.draw(batch);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRotation(rotation =+ dt);

        //Скорость вращения орудий
        gun1Rotation = gun1Rotation + dt*50;
        gun2Rotation = gun2Rotation + dt*50;

        gun1.setPosition(b2body.getPosition().x - gun1.getWidth() / 2 - MathUtils.sin(gun1Rotation  * MathUtils.degreesToRadians) / 2.5f,
                b2body.getPosition().y - gun1.getHeight() / 2 - MathUtils.cos(gun1Rotation * MathUtils.degreesToRadians) / 2.5f);

        gun2.setPosition(b2body.getPosition().x - gun2.getWidth() / 2 - MathUtils.sin(gun2Rotation  * MathUtils.degreesToRadians) / 2.5f,
                b2body.getPosition().y - gun2.getHeight() / 2 - MathUtils.cos(gun2Rotation * MathUtils.degreesToRadians) / 2.5f);

    }

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getGun1X () {
        return (gun1.getX() + gun1.getWidth() / 2f);
    }
    public float getGun2X () {
        return (gun2.getX() + gun2.getWidth() / 2f);
    }

    public float getGun1Y () {
        return (gun1.getY() + gun1.getHeight() / 2f);
    }
    public float getGun2Y () {
        return (gun2.getY() + gun2.getHeight() / 2f);
    }


    public void dispose(){
        gunsRedTexture.dispose();
        tank.dispose();
    }
}

