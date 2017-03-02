package ru.aslanisl.battletanks.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import ru.aslanisl.battletanks.BattleTanks;
import ru.aslanisl.battletanks.Sprites.Bullet;
import ru.aslanisl.battletanks.Sprites.Tank;


public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case BattleTanks.BULLET_ONE_BIT | BattleTanks.BRICK_BIT:
            case BattleTanks.BULLET_ONE_BIT | BattleTanks.WALL_BIT:
                if (fixA.getFilterData().categoryBits == BattleTanks.BULLET_ONE_BIT)
                    ((Bullet) fixA.getUserData()).onContactBulletOne();
                else
                    ((Bullet) fixB.getUserData()).onContactBulletOne();
                break;
            case BattleTanks.BULLET_TWO_BIT | BattleTanks.BRICK_BIT:
            case BattleTanks.BULLET_TWO_BIT | BattleTanks.WALL_BIT:
                if (fixA.getFilterData().categoryBits == BattleTanks.BULLET_TWO_BIT)
                    ((Bullet) fixA.getUserData()).onContactBulletTwo();
                else
                    ((Bullet) fixB.getUserData()).onContactBulletTwo();
                break;

        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
