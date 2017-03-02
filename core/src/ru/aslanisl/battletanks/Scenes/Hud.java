package ru.aslanisl.battletanks.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ru.aslanisl.battletanks.BattleTanks;

public class Hud implements Disposable{
    public Stage stage;
    public Viewport viewport;

    private static Integer worldTimer;
    private float timeCount;
    private static Integer score;

    Label countDownLabel;
    static Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label marioLable;

    private BattleTanks game;

    public Hud (BattleTanks game){
        this.game = game;

        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(BattleTanks.V_WIDTH, BattleTanks.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countDownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLable = new Label("Battle Tank", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(marioLable).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countDownLabel).expandX();

        stage.addActor(table);

    }

    public void draw () {
        stage.draw();
    }


    @Override
    public void dispose() {
        stage.dispose();
    }
}
