package ru.kursk.threeSost.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static ru.kursk.threeSost.GameSettings.SCALE;

public class GameObject {
    public int width;
    public int height;
    public Body body;
    public short cBits;
    protected Sprite sprite;
    private Texture textureRef;

    public GameObject(String texturePath, int x, int y, int width, int height, short cBits, World world) {
        this.width = width;
        this.height = height;
        this.cBits = cBits;

        if (texturePath != null && !texturePath.isEmpty()) {
            try {
                textureRef = new Texture(Gdx.files.internal(texturePath));
                sprite = new Sprite(textureRef);
                sprite.setSize(width, height);
                sprite.setOrigin(width / 2f, height / 2f);
            } catch (Exception e) {
                Gdx.app.error("GameObject", "Failed to load texture: " + texturePath, e);
                sprite = null;
            }
        }

        body = createBody(x, y, world);
    }

    public void draw(SpriteBatch batch) {
        if (sprite == null || batch == null) return;

        float screenX = body.getPosition().x / SCALE;
        float screenY = body.getPosition().y / SCALE;

        sprite.setPosition(screenX - width / 2f, screenY - height / 2f);
        sprite.draw(batch);
    }

    public int getX() {
        return (int) (body.getPosition().x / SCALE);
    }

    public int getY() {
        return (int) (body.getPosition().y / SCALE);
    }

    public void setX(int x) {
        body.setTransform(x * SCALE, body.getPosition().y, 0f);
    }

    public void setY(int y) {
        body.setTransform(body.getPosition().x, y * SCALE, 0f);
    }

    private Body createBody(float x, float y, World world) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;

        Body body = world.createBody(def);
        body.setUserData(this);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox((width / 2f) * SCALE, (height / 2f) * SCALE);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = cBits;
        fixtureDef.filter.maskBits = -1;
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        polygonShape.dispose();

        body.setTransform(x * SCALE, y * SCALE, 0f);
        return body;
    }

    public void update() {
    }

    public void dispose() {
        if (textureRef != null) {
            textureRef.dispose();
        }
        if (body != null) {
            body.getWorld().destroyBody(body);
            body = null;
        }
        sprite = null;
    }
}
