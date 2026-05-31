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
import ru.kursk.threeSost.view.MenuButton;
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
    private PlayerObject playerObject;
    private final PauseButton pauseButton;
    private final GameSession gameSession;
    private final TextView pausedText;
    private final PauseButton resumeButton;
    private final MenuButton menuButton;
    private float buttonSize;

    public ArrayList<PlatformObject> platforms;
    public ArrayList<PlatformObject> walls;

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
        resumeButton = new PauseButton(0, 0, buttonSize, buttonSize);  // будет отображать иконку play
        menuButton = new MenuButton(0, 0, buttonSize, buttonSize);
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
        // 1. Очистка экрана
        ScreenUtils.clear(BACKGROUND_COLOR);

        // 2. Отрисовка игрового мира (камера мира)
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        myGdxGame.batch.begin();

        // Платформы
        for (PlatformObject platform : platforms) {
            platform.update();
            platform.draw(myGdxGame.batch);
        }
        // Стены
        for (PlatformObject wall : walls) {
            wall.update();
            wall.draw(myGdxGame.batch);
        }
        // Игрок
        playerObject.draw(myGdxGame.batch);

        myGdxGame.batch.end();

        // 3. Отрисовка UI (кнопка паузы – всегда в правом верхнем углу)
        myGdxGame.batch.setProjectionMatrix(myGdxGame.uiCamera.combined);
        myGdxGame.batch.begin();
        pauseButton.draw(myGdxGame.batch);
        myGdxGame.batch.end();

        // 4. Если игра на паузе – полупрозрачное затемнение и надпись
        if (gameSession.getCurrentState() == GameState.PAUSED) {
            myGdxGame.batch.setProjectionMatrix(myGdxGame.uiCamera.combined);
            myGdxGame.batch.begin();
            // затемнение
            myGdxGame.batch.setColor(0, 0, 0, 0.6f);
            myGdxGame.batch.draw(TextureRegionPool.getWhitePixel(), 0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            myGdxGame.batch.setColor(Color.WHITE);
            // текст PAUSED
            pausedText.draw(myGdxGame.batch);
            // кнопки меню паузы
            resumeButton.draw(myGdxGame.batch);
            menuButton.draw(myGdxGame.batch);
            myGdxGame.batch.end();
        }

        // Восстанавливаем проекцию (необязательно, но для порядка)
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
        // Удаляем старые объекты
        if (playerObject != null) {
            playerObject.dispose();
            playerObject = null;
        }
        for (PlatformObject p : platforms) {
            p.dispose();
        }
        for (PlatformObject w : walls) {
            w.dispose();
        }
        platforms.clear();
        walls.clear();

        // Создаём заново
        createLevel();
        playerObject = new PlayerObject(140, 180, PLAYER_WIDTH, PLAYER_HEIGHT, MyGdxGame.world);

        // Сбрасываем состояние сессии (без создания новой!)
        gameSession.reset();

        Gdx.app.log("GameScreen", "Game reset completed");
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
        buttonSize = width * 0.08f;
        float margin = width * 0.02f;

        // кнопка паузы в правом верхнем углу
        pauseButton.setSize(buttonSize, buttonSize);
        pauseButton.recreateTexture(buttonSize, buttonSize);
        pauseButton.setPosition(width - buttonSize - margin, height - buttonSize - margin);

        // кнопки меню паузы – под надписью "PAUSED", горизонтально
        float totalWidth = buttonSize * 2 + margin;
        float startX = (width - totalWidth) / 2f;
        float pauseMenuY = height / 2f - buttonSize / 2f - 40f; // чуть ниже центра

        resumeButton.setSize(buttonSize, buttonSize);
        resumeButton.setPosition(startX, pauseMenuY);
        menuButton.setSize(buttonSize, buttonSize);
        menuButton.setPosition(startX + buttonSize + margin, pauseMenuY);

        // пересоздаём текстуру resumeButton как кнопку play (треугольник)
        resumeButton.recreateTexture(buttonSize, buttonSize);
        resumeButton.setPaused(true); // чтобы отображалась иконка play

        // текст PAUSED – позиционируем выше кнопок
        pausedText.setPosition(width/2f - pausedText.width/2f, pauseMenuY + buttonSize + 30f);
    }

    @Override
    public void show() {
        gameSession.startGame();
    }

    public void handleInput() {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // сначала проверяем кнопку паузы в углу (всегда активна)
            if (pauseButton.isHit(touchX, touchY)) {
                togglePause();
                return;
            }

            // если игра на паузе, проверяем кнопки меню
            if (gameSession.getCurrentState() == GameState.PAUSED) {
                if (resumeButton.isHit(touchX, touchY)) {
                    togglePause(); // возобновляем
                } else if (menuButton.isHit(touchX, touchY)) {
                    // возврат в главное меню
                    gameSession.endGame();
                    myGdxGame.setScreen(new MenuScreen(myGdxGame));
                }
            }
        }
    }
}
