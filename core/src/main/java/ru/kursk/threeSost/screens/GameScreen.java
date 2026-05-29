package ru.kursk.threeSost.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

import ru.kursk.threeSost.MyGdxGame;
import ru.kursk.threeSost.managers.GameSession;
import ru.kursk.threeSost.managers.GameState;
import ru.kursk.threeSost.managers.KeyManager;
import ru.kursk.threeSost.managers.TextureRegionPool;
import ru.kursk.threeSost.objects.PlatformObject;
import ru.kursk.threeSost.objects.PlayerObject;
import ru.kursk.threeSost.view.PauseButton;
import ru.kursk.threeSost.view.TextView;

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
    private GameSession gameSession;
    private TextView pausedText;
    private float buttonSize;

    public static ArrayList<PlatformObject> platforms;
    public static ArrayList<PlatformObject> walls;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        platforms = new ArrayList<>();
        walls = new ArrayList<>();

        createLevel();
        playerObject = new PlayerObject(140, 180, PLAYER_WIDTH, PLAYER_HEIGHT, MyGdxGame.world);
        gameSession = new GameSession(this);
        pausedText = new TextView(myGdxGame.largeWhiteFont, 0, 0, "PAUSED");
        buttonSize = Gdx.graphics.getWidth() * 0.08f;
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

        if (gameSession.getCurrentState() == GameState.PLAYING) {
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
        }

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
        // Отрисовка мира (как раньше)
        ScreenUtils.clear(BACKGROUND_COLOR);
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        myGdxGame.batch.begin();
        // ... платформы, стены, игрок
        myGdxGame.batch.end();

        // Отрисовка UI (кнопка и затемнение паузы)
        myGdxGame.batch.setProjectionMatrix(myGdxGame.uiCamera.combined);
        myGdxGame.batch.begin();
        pauseButton.draw(myGdxGame.batch);
        myGdxGame.batch.end();

        if (gameSession.getCurrentState() == GameState.PAUSED) {
            myGdxGame.batch.begin();
            // затемнение
            myGdxGame.batch.setColor(0, 0, 0, 0.6f);
            myGdxGame.batch.draw(TextureRegionPool.getWhitePixel(), 0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            myGdxGame.batch.setColor(Color.WHITE);
            pausedText.draw(myGdxGame.batch);
            myGdxGame.batch.end();
        }

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
        pausedText.dispose();
        pauseButton.dispose();
    }
    public void resetGame() {
        playerObject.respawn();
        // здесь можно сбросить счёт, время, положение камеры и т.д.
        Gdx.app.log("GameScreen", "Game reset");
    }

    public float getPlayerX() { return playerObject.getX(); }
    public float getPlayerY() { return playerObject.getY(); }

    private void togglePause() {
        if (gameSession.getCurrentState() == GameState.PLAYING) {
            gameSession.pauseGame();
            pauseButton.setPaused(true);
        } else if (gameSession.getCurrentState() == GameState.PAUSED) {
            gameSession.continueGame();
            pauseButton.setPaused(false);
        }
    }

    @Override
    public void resize(int width, int height) {
        buttonSize = width * 0.08f;      // 8% от ширины экрана
        pauseButton.setSize(buttonSize, buttonSize);
        pauseButton.recreateTexture(buttonSize, buttonSize);
        float margin = width * 0.02f;
        pauseButton.setPosition(width - buttonSize - margin, height - buttonSize - margin);
        pausedText.setPosition(width/2f - pausedText.width/2f, height/2f + pausedText.height/2f);
    }

    public void handleInput() {
        // Только касания по кнопке паузы – никаких клавиш!
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (pauseButton.isHit(touchX, touchY)) {
                togglePause();
            }
        }
    }
}
