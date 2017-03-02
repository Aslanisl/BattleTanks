package ru.aslanisl.battletanks.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import ru.aslanisl.battletanks.BattleTanks;
import ru.aslanisl.battletanks.Screens.PlayScreen;

public class EnemyTank extends Sprite{
    private Texture tankTexture;
    private TextureRegion tankTextureRegion;
    private Circle tankCircleBound;
    private Sprite gun1, gun2;
    private Texture gunsBlueTexture;
    private TextureRegion gunsBlueTextureRegion;
    private float gun1Rotation;
    private float gun2Rotation = 180;

    public EnemyTank(PlayScreen screen, float positionX, float positionY) {
        tankTexture = new Texture(Gdx.files.internal("sprites/Tank_Red.png"));
        tankTextureRegion = new TextureRegion(tankTexture);
        setRegion(tankTextureRegion, 0, 0, 64, 64);
        setPosition(positionX, positionY);
        setBounds(0, 0, 64 / BattleTanks.PPM, 64 / BattleTanks.PPM);

        //Пушки
        gunsBlueTexture = new Texture(Gdx.files.internal("sprites/Blue.png"));
        gunsBlueTextureRegion = new TextureRegion(gunsBlueTexture);
        gun1 = new Sprite(gunsBlueTextureRegion);
        gun2 = new Sprite(gunsBlueTextureRegion);

        gun1.setBounds(0, 0, 20 / BattleTanks.PPM, 20 / BattleTanks.PPM);
        gun2.setBounds(0, 0, 20 / BattleTanks.PPM, 20 / BattleTanks.PPM);

        //Боаундс
        tankCircleBound = new Circle();
        tankCircleBound.setRadius(32  / BattleTanks.PPM);
        tankCircleBound.setPosition(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

        gun1.draw(batch);
        gun2.draw(batch);
    }

    public void update (float dt){
        tankCircleBound.setPosition(getX() + getWidth() / 2, getY() + getHeight() / 2);

        //Крутим пушки
//        gun1Rotation = gun1Rotation + dt*50;
//        gun2Rotation = gun2Rotation + dt*50;
//
//        gun1.setPosition(getX() + 0.32f - gun1.getWidth() / 2 - MathUtils.sin(gun1Rotation  * MathUtils.degreesToRadians) / 2.5f,
//                getY() + 0.32f - gun1.getHeight() / 2 - MathUtils.cos(gun1Rotation * MathUtils.degreesToRadians) / 2.5f);
//
//        gun2.setPosition(getX() + 0.32f - gun2.getWidth() / 2 - MathUtils.sin(gun2Rotation  * MathUtils.degreesToRadians) / 2.5f,
//                getY() + 0.32f - gun2.getHeight() / 2 - MathUtils.cos(gun2Rotation * MathUtils.degreesToRadians) / 2.5f);
    }

    public Sprite getGun1() {
        return gun1;
    }

    public void setGun1(Sprite gun1) {
        this.gun1 = gun1;
    }

    public Sprite getGun2() {
        return gun2;
    }

    public void setGun2(Sprite gun2) {
        this.gun2 = gun2;
    }
}
