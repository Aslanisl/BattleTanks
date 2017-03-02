package ru.aslanisl.battletanks;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.aslanisl.battletanks.Screens.MenuScreen;
import ru.aslanisl.battletanks.Screens.PlayScreen;

public class BattleTanks extends Game {

	public static final int V_WIDTH = 800;
	public static final int V_HEIGHT = 480;
	public static final float PPM = 100;

	public static final short NOTHING_BIT = 0;
	public static final short TANK_BIT = 1;
	public static final short WALL_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short BULLET_ONE_BIT = 8;
	public static final short BULLET_TWO_BIT = 16;

	public SpriteBatch batch;

	public static AssetManager loader;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		loader = new AssetManager();
		loader.finishLoading();

		setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		loader.update();
		super.render();
	}
	
	@Override
	public void dispose () {
		loader.dispose();
		batch.dispose();
	}
}
