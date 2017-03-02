package ru.aslanisl.battletanks.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


import ru.aslanisl.battletanks.BattleTanks;
import ru.aslanisl.battletanks.Screens.PlayScreen;

public class WorldCreator {


    public WorldCreator (PlayScreen screen){

        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        CircleShape circleShape = new CircleShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //Создание стен

        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / BattleTanks.PPM,
                    (rect.getY() + rect.getHeight() / 2) / BattleTanks.PPM);


            body = world.createBody(bdef);
            polygonShape.setAsBox(rect.getWidth() / 2 / BattleTanks.PPM, rect.getHeight() / 2 / BattleTanks.PPM);
            fdef.filter.categoryBits = BattleTanks.WALL_BIT;
            fdef.shape = polygonShape;
            body.createFixture(fdef);
        }

        //Создание препятствий

        for (MapObject object : map.getLayers().get(4).getObjects().getByType(EllipseMapObject.class)){
            Ellipse ellipse = ((EllipseMapObject) object).getEllipse();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((ellipse.x + ellipse.height / 2) / BattleTanks.PPM,
                    (ellipse.y + ellipse.height / 2) / BattleTanks.PPM);

            body = world.createBody(bdef);

            circleShape.setRadius(ellipse.height / 2 / BattleTanks.PPM);
            fdef.shape = circleShape;
            fdef.filter.categoryBits = BattleTanks.BRICK_BIT;
            body.createFixture(fdef);
        }
    }
}
