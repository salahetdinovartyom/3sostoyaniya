package ru.kursk.threeSost;

import static ru.kursk.threeSost.GameSettings.*;

import ru.kursk.threeSost.managers.ContactManager;
import ru.kursk.threeSost.managers.KeyManager;
import ru.kursk.threeSost.managers.TextureRegionPool;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;

import ru.kursk.threeSost.screens.GameScreen;

public class MyGdxGame extends Game {
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public static World world;
    static float accumulator = 0f;
    public Vector3 touch;
    public GameScreen gameScreen;

    @Override
    public void create() {
        Box2D.init();
        // ✅ Используем GRAVITY из GameSettings
        // Масштабируем гравитацию, чтобы она работала корректно с вашим SCALE
        Vector2 gravity = new Vector2(GRAVITY); // Просто -20 по Y, без * SCALE
        world = new World(gravity, true);
        new ContactManager(world);

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        gameScreen = new GameScreen(this);
        setScreen(gameScreen);

        // Установка InputProcessor
        Gdx.input.setInputProcessor(new KeyManager());
    }

    @Override
    public void dispose() {
        gameScreen.dispose();
        TextureRegionPool.dispose();
        batch.dispose();
        world.dispose(); // ✅ Не забываем освободить Box2D-мир
    }

    public static void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += Math.min(delta, 0.25f);
        while (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }
}
