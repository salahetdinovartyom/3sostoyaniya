package ru.kursk.threeSost.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import ru.kursk.threeSost.managers.TextureRegionPool;

import static ru.kursk.threeSost.GameSettings.PLATFORM_BIT;
import static ru.kursk.threeSost.GameSettings.SCALE;

public class PlatformObject extends GameObject {
    private static final Color DEFAULT_COLOR = new Color(0.22f, 0.78f, 0.56f, 1f);

    private final float width;
    private final float height;
    private final Rectangle bounds;
    private final Color color;

    public PlatformObject(int x, int y, float width, float height, World world) {
        this(x, y, width, height, world, PLATFORM_BIT, DEFAULT_COLOR);
    }

    public PlatformObject(int x, int y, float width, float height, World world, short categoryBits, Color color) {
        super(null, x, y, (int) width, (int) height, categoryBits, world);
        this.width = width;
        this.height = height;
        this.color = new Color(color);

        if (body != null) {
            body.getWorld().destroyBody(body);
        }

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(x * SCALE, y * SCALE);

        body = world.createBody(def);
        body.setUserData(this);
        Gdx.app.log("Platform", "Body created at: " + x + ", " + y);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2f) * SCALE, (height / 2f) * SCALE);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.filter.maskBits = -1;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        shape.dispose();

        bounds = new Rectangle(x - width / 2f, y - height / 2f, width, height);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (batch == null) return;

        Color oldColor = new Color(batch.getColor());
        batch.setColor(color);
        batch.draw(TextureRegionPool.getWhitePixel(), bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(oldColor);
    }

    @Override
    public void update() {
        float screenX = body.getPosition().x / SCALE;
        float screenY = body.getPosition().y / SCALE;
        bounds.setPosition(screenX - width / 2f, screenY - height / 2f);
    }
}
