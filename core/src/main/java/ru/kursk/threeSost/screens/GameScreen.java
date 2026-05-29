package ru.kursk.threeSost.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

import ru.kursk.threeSost.MyGdxGame;
import ru.kursk.threeSost.managers.KeyManager;
import ru.kursk.threeSost.objects.PlatformObject;
import ru.kursk.threeSost.objects.PlayerObject;
import ru.kursk.threeSost.view.PauseButton;

import static ru.kursk.threeSost.GameSettings.PLAYER_HEIGHT;
import static ru.kursk.threeSost.GameSettings.PLAYER_WIDTH;
import static ru.kursk.threeSost.GameSettings.SCREEN_HEIGHT;
import static ru.kursk.threeSost.GameSettings.SCREEN_WIDTH;
import static ru.kursk.threeSost.GameSettings.WALL_BIT;
import static ru.kursk.threeSost.GameSettings.WORLD_WIDTH;

public class GameScreen extends ScreenAdapter {
    private static final Color BACKGROUND_COLOR = new Color(0.05f, 0.07f, 0.12f, 1f);
    private static final Color WALL_COLOR = new Color(0.16f, 0.18f, 0.25f, 1f);

    private final MyGdxGame myGdxGame;
    private final PlayerObject playerObject;
    private final PauseButton pauseButton;

    public static ArrayList<PlatformObject> platforms;
    public static ArrayList<PlatformObject> walls;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        platforms = new ArrayList<>();
        walls = new ArrayList<>();

        createLevel();
        playerObject = new PlayerObject(140, 180, PLAYER_WIDTH, PLAYER_HEIGHT, MyGdxGame.world);
        float buttonSize = SCREEN_WIDTH * 0.1f; // 6% от ширины
        pauseButton = new PauseButton(0, 0, buttonSize, buttonSize);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void createLevel() {
        platforms.add(new PlatformObject(360, 70, 720, 44, MyGdxGame.world));
        platforms.add(new PlatformObject(900, 165, 260, 32, MyGdxGame.world));
        platforms.add(new PlatformObject(1240, 260, 250, 32, MyGdxGame.world));
        platforms.add(new PlatformObject(1620, 190, 310, 32, MyGdxGame.world));
        platforms.add(new PlatformObject(2050, 315, 280, 32, MyGdxGame.world));
        platforms.add(new PlatformObject(2460, 210, 360, 32, MyGdxGame.world));
        platforms.add(new PlatformObject(3050, 92, 620, 44, MyGdxGame.world));

        walls.add(new PlatformObject(-16, SCREEN_HEIGHT / 2, 32, SCREEN_HEIGHT, MyGdxGame.world, WALL_BIT, WALL_COLOR));
        walls.add(new PlatformObject((int) WORLD_WIDTH + 16, SCREEN_HEIGHT / 2, 32, SCREEN_HEIGHT, MyGdxGame.world, WALL_BIT, WALL_COLOR));
    }

    @Override
    public void render(float delta) {
        handleInput();
        if (KeyManager.isResetPressed()) {
            playerObject.respawn();
            KeyManager.resetRespawn();
        }

        playerObject.update();
        MyGdxGame.stepWorld();

        if (playerObject.getY() < -160) {
            playerObject.respawn();
        }

        updateCamera();
        draw();
    }

    private void updateCamera() {
        float halfWidth = SCREEN_WIDTH / 2f;
        float halfHeight = SCREEN_HEIGHT / 2f;
        float targetX = Math.max(halfWidth, Math.min(playerObject.getX(), WORLD_WIDTH - halfWidth));

        myGdxGame.camera.position.set(targetX, halfHeight, 0f);
        myGdxGame.camera.update();
    }

    private void draw() {
        // 1. Отрисовка мира (платформы, стены, игрок)
        ScreenUtils.clear(BACKGROUND_COLOR);
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        myGdxGame.batch.begin();
        for (PlatformObject platform : platforms) {
            platform.update();
            platform.draw(myGdxGame.batch);
        }
        for (PlatformObject wall : walls) {
            wall.update();
            wall.draw(myGdxGame.batch);
        }
        playerObject.draw(myGdxGame.batch);
        myGdxGame.batch.end();

        // 2. Отрисовка UI (кнопка паузы) – в экранных координатах
        myGdxGame.batch.setProjectionMatrix(myGdxGame.uiCamera.combined);
        myGdxGame.batch.begin();
        pauseButton.draw(myGdxGame.batch);
        myGdxGame.batch.end();

        // (необязательно) восстанавливаем мировую проекцию для следующего кадра
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
    }

    @Override
    public void dispose() {
        playerObject.dispose();
        for (PlatformObject platform : platforms) {
            platform.dispose();
        }
        for (PlatformObject wall : walls) {
            wall.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        float buttonSize = SCREEN_WIDTH * 0.1f;
        pauseButton.setPosition(width - buttonSize - 16, height - buttonSize - 16);
    }

    public void handleInput() {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (pauseButton.isHit(touchX, touchY)) {
                pauseButton.onClick();
                Gdx.app.log("PauseButton", "Clicked!");
            }
        }
    }
}
