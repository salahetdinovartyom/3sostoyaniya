package ru.kursk.threeSost.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import ru.kursk.threeSost.managers.KeyManager;

import ru.kursk.threeSost.screens.GameScreen;
import static ru.kursk.threeSost.GameSettings.*;

public class PlayerObject extends GameObject {
    private final float startX;
    private final float startY;
    private Texture shipTexture;
    private int groundContacts;
    private boolean facingRight;
    private final GameScreen gameScreen;

    public PlayerObject(int x, int y, int width, int height, World world, GameScreen gameScreen) {
        super(null, x, y, width, height, PLAYER_BIT, world);
        this.startX = x;
        this.startY = y;
        this.facingRight = true;
        this.gameScreen = gameScreen;

        body.setFixedRotation(true);
        body.setLinearDamping(0f);
        createFootSensor();
        createShipSprite();
    }

    private void createFootSensor() {
        PolygonShape footShape = new PolygonShape();
        Vector2 center = new Vector2(0f, (-height / 2f - 3f) * SCALE);
        footShape.setAsBox((width * 0.33f) * SCALE, 5f * SCALE, center, 0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = footShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = PLAYER_FOOT_BIT;
        fixtureDef.filter.maskBits = PLATFORM_BIT;

        Fixture footFixture = body.createFixture(fixtureDef);
        footFixture.setUserData(new FootSensor(this));
        footShape.dispose();
    }

    private void createShipSprite() {
        Pixmap pixmap = new Pixmap(96, 56, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();

        pixmap.setColor(new Color(0.08f, 0.23f, 0.34f, 1f));
        pixmap.fillTriangle(8, 45, 78, 45, 94, 28);
        pixmap.fillTriangle(8, 11, 78, 11, 94, 28);
        pixmap.fillRectangle(18, 14, 58, 28);

        pixmap.setColor(new Color(0.13f, 0.76f, 0.84f, 1f));
        pixmap.fillTriangle(18, 40, 76, 40, 89, 28);
        pixmap.fillTriangle(18, 16, 76, 16, 89, 28);
        pixmap.fillRectangle(23, 18, 49, 20);

        pixmap.setColor(new Color(0.93f, 0.98f, 1f, 1f));
        pixmap.fillRectangle(54, 21, 14, 14);

        pixmap.setColor(new Color(1f, 0.62f, 0.16f, 1f));
        pixmap.fillTriangle(0, 28, 18, 19, 18, 37);

        shipTexture = new Texture(pixmap);
        shipTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        pixmap.dispose();

        sprite = new Sprite(shipTexture);
        sprite.setSize(width, height);
        sprite.setOrigin(width / 2f, height / 2f);
    }

    public void groundContactStarted() {
        groundContacts++;
    }

    public void groundContactEnded() {
        groundContacts = Math.max(0, groundContacts - 1);
    }

    public void respawn() {
        body.setTransform(startX * SCALE, startY * SCALE, 0f);
        body.setLinearVelocity(0f, 0f);
        body.setAwake(true);
        groundContacts = 0;
    }

    @Override
    public void update() {
        move();
    }

    private void move() {
        float delta = Math.min(Gdx.graphics.getDeltaTime(), 1f / 30f);
        Vector2 velocity = body.getLinearVelocity();
        float targetXSpeed = 0f;

        if (gameScreen != null && gameScreen.isAccelerometerActive()) {
            float accelX = -Gdx.input.getAccelerometerY();
            float accelY = Gdx.input.getAccelerometerX();

            targetXSpeed = -accelX * PLAYER_MOVE_SPEED * ACCELEROMETER_SENSITIVITY;
            targetXSpeed = MathUtils.clamp(targetXSpeed, -PLAYER_MOVE_SPEED, PLAYER_MOVE_SPEED);

            float verticalForce = -accelY * ACCELEROMETER_VERTICAL_SENSITIVITY;
            float newYSpeed = velocity.y + verticalForce;
            newYSpeed = MathUtils.clamp(newYSpeed, -MAX_VERTICAL_ACCEL_SPEED, MAX_VERTICAL_ACCEL_SPEED);
            body.setLinearVelocity(velocity.x, newYSpeed);
        } else {
            if (KeyManager.isLeftPressed()) targetXSpeed -= PLAYER_MOVE_SPEED;
            if (KeyManager.isRightPressed()) targetXSpeed += PLAYER_MOVE_SPEED;
        }

        // Применяем горизонтальное ускорение (общее для обоих режимов)
        if (targetXSpeed != 0f || Math.abs(velocity.x) > 0.01f) {
            float acceleration = isGrounded() ? PLAYER_ACCELERATION : PLAYER_AIR_ACCELERATION;
            float alpha = Math.min(1f, acceleration * delta / PLAYER_MOVE_SPEED);
            float newSpeedX = MathUtils.lerp(velocity.x, targetXSpeed, alpha);
            body.setLinearVelocity(newSpeedX, body.getLinearVelocity().y);
        }

        if (KeyManager.isJumpPressed() && isGrounded()) {
            body.setLinearVelocity(body.getLinearVelocity().x, PLAYER_JUMP_SPEED);
            KeyManager.resetJump();
            groundContacts = 0;
        }

        if (targetXSpeed < -0.01f) facingRight = false;
        if (targetXSpeed > 0.01f) facingRight = true;
    }

    private boolean isGrounded() {
        return groundContacts > 0;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (sprite == null || batch == null) return;

        float screenX = body.getPosition().x / SCALE;
        float screenY = body.getPosition().y / SCALE;
        float tilt = MathUtils.clamp(body.getLinearVelocity().y * 1.6f, -14f, 14f);

        sprite.setFlip(!facingRight, false);
        sprite.setRotation(facingRight ? tilt : -tilt);
        sprite.setPosition(screenX - width / 2f, screenY - height / 2f);
        sprite.draw(batch);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (shipTexture != null) {
            shipTexture.dispose();
        }
    }
    public void resetVelocityX() {
        body.setLinearVelocity(0f, body.getLinearVelocity().y);
    }

    public static class FootSensor {
        private final PlayerObject player;
        public FootSensor(PlayerObject player) {
            this.player = player;
        }

        public PlayerObject getPlayer() {
            return player;
        }
    }
}
