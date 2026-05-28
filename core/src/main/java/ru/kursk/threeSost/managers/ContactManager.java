package ru.kursk.threeSost.managers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import ru.kursk.threeSost.objects.PlayerObject;

import static ru.kursk.threeSost.GameSettings.PLATFORM_BIT;

public class ContactManager {
    public ContactManager(World world) {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                PlayerObject player = getPlayerFootContact(contact.getFixtureA(), contact.getFixtureB());
                if (player != null) {
                    player.groundContactStarted();
                }
            }

            @Override
            public void endContact(Contact contact) {
                PlayerObject player = getPlayerFootContact(contact.getFixtureA(), contact.getFixtureB());
                if (player != null) {
                    player.groundContactEnded();
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }

    private PlayerObject getPlayerFootContact(Fixture fixtureA, Fixture fixtureB) {
        PlayerObject footPlayerA = getFootPlayer(fixtureA);
        PlayerObject footPlayerB = getFootPlayer(fixtureB);

        if (footPlayerA != null && isPlatform(fixtureB)) return footPlayerA;
        if (footPlayerB != null && isPlatform(fixtureA)) return footPlayerB;
        return null;
    }

    private PlayerObject getFootPlayer(Fixture fixture) {
        Object userData = fixture.getUserData();
        if (userData instanceof PlayerObject.FootSensor) {
            return ((PlayerObject.FootSensor) userData).getPlayer();
        }
        return null;
    }

    private boolean isPlatform(Fixture fixture) {
        return fixture.getFilterData().categoryBits == PLATFORM_BIT;
    }
}
