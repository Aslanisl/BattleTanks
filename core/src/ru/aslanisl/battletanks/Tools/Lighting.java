package ru.aslanisl.battletanks.Tools;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class Lighting {

    private PointLight pointLight;

    public Lighting (RayHandler rayHandler, Body body, int rays, Color color, float distance) {
        pointLight = new PointLight(rayHandler, rays);
        pointLight.attachToBody(body);
        pointLight.setColor(color);
        pointLight.setDistance(distance);
    }

}
