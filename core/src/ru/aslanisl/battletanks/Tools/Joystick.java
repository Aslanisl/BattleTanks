package ru.aslanisl.battletanks.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import ru.aslanisl.battletanks.BattleTanks;
import ru.aslanisl.battletanks.Screens.PlayScreen;
import ru.aslanisl.battletanks.Sprites.Tank;

public class Joystick {
    private OrthographicCamera camera;
    private Stage stage;
    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private Texture blockTexture;
    private Sprite blockSprite;
    private float blockSpeed;
    private Tank player;

    public Joystick (PlayScreen screen, Tank player) {
        //Create camera
        this.camera = screen.getGameCam();

        //Create a touchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("sprites/touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("sprites/touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        touchpadStyle.knob.setMinWidth(Gdx.graphics.getHeight() / 9);
        touchpadStyle.knob.setMinHeight(Gdx.graphics.getHeight() / 9);
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(Gdx.graphics.getHeight() / 64, Gdx.graphics.getHeight() / 64,
                Gdx.graphics.getHeight() / 3, Gdx.graphics.getHeight() / 3);

        //Create a Stage and add TouchPad
        stage = new Stage();
        stage.addActor(touchpad);
        Gdx.input.setInputProcessor(stage);

        //Create block sprite
        this.player = player;

    }

    public void update() {

        player.b2body.setLinearVelocity(player.getVelocityX() + touchpad.getKnobPercentX()*player.getSpeed(),
                player.getVelocityY() + touchpad.getKnobPercentY()*player.getSpeed());
        player.setRotation(MathUtils.atan2(touchpad.getKnobPercentX(), touchpad.getKnobPercentY()));
//        //Move player with TouchPad
//        player.setX(player.getX() + touchpad.getKnobPercentX()*5);
//        player.setY(player.getY() + touchpad.getKnobPercentY()*5);

    }

    public void draw (float dt){
        stage.act(dt);
        stage.draw();
    }
}
