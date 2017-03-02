package ru.aslanisl.battletanks.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import box2dLight.RayHandler;
import box2dLight.PointLight;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import ru.aslanisl.battletanks.BattleTanks;
import ru.aslanisl.battletanks.Scenes.Hud;
import ru.aslanisl.battletanks.Sprites.Bullet;
import ru.aslanisl.battletanks.Sprites.EnemyTank;
import ru.aslanisl.battletanks.Sprites.Tank;
import ru.aslanisl.battletanks.Tools.Joystick;
import ru.aslanisl.battletanks.Tools.Lighting;
import ru.aslanisl.battletanks.Tools.WorldContactListener;
import ru.aslanisl.battletanks.Tools.WorldCreator;

public class PlayScreen implements Screen {

    private BattleTanks game;
    private TextureAtlas atlas;
    private Hud hud;

    private OrthographicCamera gameCam;
    private Viewport gamePort;

    //Map
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    //Sprites
    private Tank player;
    private Array<Bullet> bullets;
    private float bulletTimer;

    //Создание мира
    //Box2d
    private World world;
    private Box2DDebugRenderer b2dr;
    private WorldCreator creator;

    //Свет
    private RayHandler rayHandler;
    private PointLight pointLight;

    private Array<Lighting> lightings;
    //Джолстик
    private Joystick joystick;

    //Мультиплеер
    private Socket socket;


    //Сервер
    private String idPlayerConnected;
    private String idEnemyPlayerConnected;
    private String idPlayerDisconnected;
    private String idPlayer;
    private Vector2 enemyPosition;
    private boolean playerIsConnected;
    private boolean enemyPlayerIsConnected;
    private boolean enemyPlayerIsDisconnected;
    private boolean isPlayerWasConnected;
    private final float UPDATE_TIME = 1/60f;
    private float timer;


    //Другой игрок
    private HashMap<String, EnemyTank> enemyPlayers;
    private String idGetPlayer;
    private Vector2 gun1EnemyPosition;
    private Vector2 gun2EnemyPosition;


    public PlayScreen(BattleTanks game) {

        this.game = game;
        //Загрузка текстур
        atlas = new TextureAtlas("sprites/sprites.pack");

        //Создание камеры
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(BattleTanks.V_WIDTH / BattleTanks.PPM, BattleTanks.V_HEIGHT  / BattleTanks.PPM, gameCam);

        //hud = new Hud(game);

        //Загрузка карты
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/level1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / BattleTanks.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //Мир
        world = new World(new Vector2(0, 0), true);
        creator = new WorldCreator(this);
        world.setContactListener(new WorldContactListener());
        b2dr = new Box2DDebugRenderer();

        rayHandler = new RayHandler(world);
        rayHandler.setShadows(false);

        //Подключение сервера
        connectSocket();
        configSocketEvents();

        //Enemy player
        enemyPlayers = new HashMap<String, EnemyTank>();
        enemyPosition = new Vector2();
        gun1EnemyPosition = new Vector2();
        gun2EnemyPosition = new Vector2();

        bullets = new Array<Bullet>();
        lightings = new Array<Lighting>();

        //Свет у плеера
//        pointLight = new PointLight(rayHandler, 25);
//        pointLight.setColor(Color.NAVY);
//        pointLight.attachToBody(player.b2body);
//        pointLight.setDistance(5);

        //Джостик и плеер

    }

    public void connectSocket(){
        try {
            socket = IO.socket("http://localhost:8080");
            socket.connect();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void configSocketEvents(){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketID", "Connected");
                playerIsConnected = true;
            }
        }).on("socketID", new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    idPlayerConnected = data.getString("id");
                    Gdx.app.log("SocketIO", "My ID: " + idPlayerConnected);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    idEnemyPlayerConnected = data.getString("id");
                    Gdx.app.log("SocketIO", "New Player Connect: " + idPlayerConnected);
                    enemyPlayerIsConnected = true;
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting New Player ID");
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    idPlayerDisconnected = data.getString("id");
                    enemyPlayerIsDisconnected = true;
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting New Player ID");
                }
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerID = data.getString("id");
                    Double x = data.getDouble("x");
                    Double y = data.getDouble("y");
                    Double gun1X = data.getDouble("gun1X");
                    Double gun2X = data.getDouble("gun2X");
                    Double gun1Y = data.getDouble("gun1Y");
                    Double gun2Y = data.getDouble("gun2Y");
                    if (enemyPlayers.get(playerID) != null){
                        enemyPlayers.get(playerID).setPosition(x.floatValue() - ((enemyPlayers.get(playerID).getWidth() / 2) / BattleTanks.PPM),
                                y.floatValue() - ((enemyPlayers.get(playerID).getHeight() / 2) / BattleTanks.PPM));
                        enemyPlayers.get(playerID).getGun1().setPosition(gun1X.floatValue() - (enemyPlayers.get(playerID).getGun1().getWidth() / 2),
                                gun1Y.floatValue() - (enemyPlayers.get(playerID).getGun1().getHeight() / 2));
                        System.out.println((enemyPlayers.get(playerID).getGun1().getWidth() / 2) + " " + (enemyPlayers.get(playerID).getGun1().getHeight() / 2));
                        enemyPlayers.get(playerID).getGun2().setPosition(gun2X.floatValue() - (enemyPlayers.get(playerID).getGun2().getWidth() / 2),
                                gun2Y.floatValue() - (enemyPlayers.get(playerID).getGun2().getWidth() / 2));

                    }

                } catch (JSONException e) {

                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray objects = (JSONArray) args [0];
                try {
                    for (int i = 0; i < objects.length(); i++){
                            enemyPosition.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
                            enemyPosition.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
                            gun1EnemyPosition.x = ((Double) objects.getJSONObject(i).getDouble("gun1X")).floatValue();
                            gun1EnemyPosition.y = ((Double) objects.getJSONObject(i).getDouble("gun1Y")).floatValue();
                            gun2EnemyPosition.x = ((Double) objects.getJSONObject(i).getDouble("gun1X")).floatValue();
                            gun2EnemyPosition.y = ((Double) objects.getJSONObject(i).getDouble("gun2Y")).floatValue();

                            isPlayerWasConnected = true;
                            idGetPlayer = ((String) objects.getJSONObject(i).get("id"));
                    }
                } catch (JSONException e){

                }
            }
        });
    }

    public void updateServer (float dt){
        timer += dt;
        if (timer >= UPDATE_TIME && player != null){
            JSONObject data = new JSONObject();
            try {
                data.put("x", player.b2body.getPosition().x);
                data.put("y", player.b2body.getPosition().y);
                data.put("gun1X", player.getGun1X());
                data.put("gun2X", player.getGun2X());
                data.put("gun1Y", player.getGun1Y());
                data.put("gun2Y", player.getGun2Y());
                socket.emit("playerMoved", data);
            } catch (JSONException e){
                Gdx.app.log("SOCKET.IO", "Error sending update date");
            }
        }
    }

    public void update (float dt){

        handleInput(dt);

        world.step(1/60f, 6, 2);

        mapRenderer.setView(gameCam);
        gameCam.update();


        updateServer(dt);

        if (playerIsConnected){
            player = new Tank(PlayScreen.this, 100, 100);
            joystick = new Joystick(PlayScreen.this, player);
            playerIsConnected = false;
        }

        if (enemyPlayerIsConnected){
            enemyPlayers.put(idEnemyPlayerConnected, new EnemyTank(PlayScreen.this, 100, 100));
            enemyPlayerIsConnected = false;
        }

        if (enemyPlayerIsDisconnected){
            enemyPlayers.remove(idPlayerDisconnected);
            enemyPlayerIsDisconnected = false;
        }

        if (isPlayerWasConnected){
            enemyPlayers.put(idGetPlayer, new EnemyTank(PlayScreen.this, enemyPosition.x, enemyPosition.y));
            enemyPlayers.get(idGetPlayer).getGun1().setPosition(gun1EnemyPosition.x, gun1EnemyPosition.y);
            enemyPlayers.get(idGetPlayer).getGun2().setPosition(gun2EnemyPosition.x, gun2EnemyPosition.y);
            System.out.println(enemyPosition);
            isPlayerWasConnected = false;

        }

        if (player !=null){
            player.update(dt);
        }

        for (HashMap.Entry<String, EnemyTank> entry : enemyPlayers.entrySet()){
            entry.getValue().update(dt);
        }

        for (Bullet bullet : bullets) {
            bullet.update();
        }
    }

    @Override
    public void show() {

    }

    public void handleInput (float dt){

        if (player !=null) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                player.b2body.getPosition().set(100 / BattleTanks.PPM, 100 / BattleTanks.PPM);

                System.out.println("UP pressed");
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                player.b2body.getPosition().set(100 / BattleTanks.PPM, 100 / BattleTanks.PPM);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                player.b2body.getPosition().set(100 / BattleTanks.PPM, 100 / BattleTanks.PPM);
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                player.b2body.getPosition().set(100 / BattleTanks.PPM, 100 / BattleTanks.PPM);

            player.b2body.setLinearVelocity(player.getVelocityX(), player.getVelocityY());
            player.setVelocityX(0);
            player.setVelocityY(0);

            joystick.update();


            bulletTimer = bulletTimer + dt;


            if (Gdx.input.justTouched() && NotJoystick()) {
                if (bulletTimer > 0.5f)
                    bullets.add(new Bullet(this, player));
                bulletTimer = 0;

                //Создаем свет у пуль
//            lightings.add(new Lighting(rayHandler, bullets.get(bullets.size - 1).getBullet1(), 10, Color.RED, 0.2f));
//            lightings.add(new Lighting(rayHandler, bullets.get(bullets.size - 1).getBullet2(), 10, Color.RED, 0.2f));
            }
        }
    }

    @Override
    public void render(float dt) {
        update(dt);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (player !=null) {
            gameCam.position.x = player.b2body.getPosition().x + player.getWidth() / 2;
            gameCam.position.y = player.b2body.getPosition().y + player.getHeight() / 2;
        }

        //Прорисовка карты
        mapRenderer.render();

        b2dr.render(world, gameCam.combined);

        //hud.draw();

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();

        if (player != null) {
            player.draw(game.batch);
        }

        for (HashMap.Entry<String, EnemyTank> entry : enemyPlayers.entrySet()){
            entry.getValue().draw(game.batch);
        }

        for (Bullet bullet : bullets){
            bullet.draw(dt, game.batch);
        }

        game.batch.end();

        gameCam.update();

        rayHandler.setCombinedMatrix(gameCam);
        rayHandler.updateAndRender();

        //Прорисовка джостика
        if (player !=null) {
            joystick.draw(dt);
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        atlas.dispose();
        world.dispose();
        rayHandler.dispose();
        player.dispose();
    }

    public TextureAtlas getAtlas (){
        return atlas;
    }

    public TiledMap getMap (){
        return map;
    }

    public OrthographicCamera getGameCam() {
        return gameCam;
    }

    public World getWorld() {
        return world;
    }

    public boolean NotJoystick () {
        return (Gdx.input.justTouched() && (Gdx.input.isTouched(0) || Gdx.input.isTouched(1)) && (Gdx.input.getX() > Gdx.graphics.getWidth() / 3.5f
                || (Gdx.input.getY()) < Gdx.graphics.getHeight() / 3));
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }
}
