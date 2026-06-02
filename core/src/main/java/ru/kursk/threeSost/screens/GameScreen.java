package ru.kursk.threeSost.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

import ru.kursk.threeSost.MyGdxGame;
import ru.kursk.threeSost.managers.GameSession;
import ru.kursk.threeSost.managers.GameState;
import ru.kursk.threeSost.managers.KeyManager;
import ru.kursk.threeSost.managers.TextureRegionPool;
import ru.kursk.threeSost.managers.TouchInputProcessor;
import ru.kursk.threeSost.objects.PlatformObject;
import ru.kursk.threeSost.objects.PlayerObject;
import ru.kursk.threeSost.view.MenuButton;
import ru.kursk.threeSost.view.MoveButton;
import ru.kursk.threeSost.view.PauseButton;
import ru.kursk.threeSost.view.TextView;

import static ru.kursk.threeSost.GameSettings.*;

import java.util.HashMap;
import java.util.Map;

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
    private final MoveButton leftButton, rightButton, jumpButton;
    private boolean accelerometerControl = false;
    private final Map<Integer, MoveButton> activeTouches = new HashMap<>();

    public ArrayList<PlatformObject> platforms;
    public ArrayList<PlatformObject> walls;
    public ArrayList<TextView> hints;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        platforms = new ArrayList<>();
        walls = new ArrayList<>();
        hints=new ArrayList<>();

        createLevel();
        playerObject = new PlayerObject(140, 180, PLAYER_WIDTH, PLAYER_HEIGHT, MyGdxGame.world,this);
        gameSession = new GameSession(this);
        pausedText = new TextView(myGdxGame.largeWhiteFont, 0, 0, "PAUSED");
        buttonSize = Gdx.graphics.getWidth() * 0.08f;
        pauseButton = new PauseButton(0, 0, buttonSize, buttonSize);
        leftButton = new MoveButton(MoveButton.ButtonType.LEFT, 0, 0, buttonSize, buttonSize);
        rightButton = new MoveButton(MoveButton.ButtonType.RIGHT, 0, 0, buttonSize, buttonSize);
        jumpButton = new MoveButton(MoveButton.ButtonType.JUMP, 0, 0, buttonSize, buttonSize);
        resumeButton = new PauseButton(0, 0, buttonSize, buttonSize);  // будет отображать иконку play
        menuButton = new MenuButton(0, 0, buttonSize, buttonSize);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void createLevel() {
        hints.add(new TextView(myGdxGame.commonWhiteFont,300,100,"Тут всё просто"));
        platforms.add(new PlatformObject(360, 70, 720, 44, MyGdxGame.world));
        platforms.add(new PlatformObject(900, 165, 260, 32, MyGdxGame.world));
        platforms.add(new PlatformObject(1240, 350, 250, 32, MyGdxGame.world));
        platforms.add(new PlatformObject(900, 550, 310, 32, MyGdxGame.world));
        platforms.add(new PlatformObject(1240, 750, 280, 32, MyGdxGame.world));
        platforms.add(new PlatformObject(1000, 910, 360, 32, MyGdxGame.world));
        hints.add(new TextView(myGdxGame.commonWhiteFont,900,1200,"Умный в гору не пойдёт,\nУмный гору обойдёт"));
        platforms.add(new PlatformObject(1600, 1000, 1000, 44, MyGdxGame.world));
        createMaze();
        platforms.add(new PlatformObject(3000,1000,1000,44,MyGdxGame.world));
        hints.add(new TextView(myGdxGame.commonWhiteFont,3100,1200,"Не бойся, прыгай!"));
        platforms.add(new PlatformObject(3000,70,2000,44,MyGdxGame.world));
        platforms.add(new PlatformObject(2000,200,25,350,MyGdxGame.world));
        hints.add(new TextView(myGdxGame.commonWhiteFont,3000,100,"Молодец! Спасибо,\nчто принял участие\nв бета-тесте!"));
        walls.add(new PlatformObject(-16, SCREEN_HEIGHT / 2, 32, SCREEN_HEIGHT, MyGdxGame.world, WALL_BIT, WALL_COLOR));
        walls.add(new PlatformObject((int) WORLD_WIDTH + 16, SCREEN_HEIGHT / 2, 32, SCREEN_HEIGHT, MyGdxGame.world, WALL_BIT, WALL_COLOR));
    }

    @Override
    public void render(float delta) {

        if (gameSession.getCurrentState() == GameState.PLAYING) {
            if (KeyManager.isResetPressed()) {
                playerObject.respawn();
                KeyManager.resetRespawn();
            }

            playerObject.update();
            MyGdxGame.stepWorld();
            checkAccelerometerActivation();

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

        // Горизонтальное ограничение
        float targetX = Math.max(halfWidth, Math.min(playerObject.getX(), WORLD_WIDTH - halfWidth));
        // Вертикальное ограничение (теперь камера следует за игроком)
        float targetY = Math.max(halfHeight, Math.min(playerObject.getY(), WORLD_HEIGHT - halfHeight));

        myGdxGame.camera.position.set(targetX, targetY, 0f);
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
        for (TextView hint:hints) {
            hint.draw(myGdxGame.batch);
        }
        // Игрок
        playerObject.draw(myGdxGame.batch);

        myGdxGame.batch.end();

        // 3. Отрисовка UI (кнопка паузы – всегда в правом верхнем углу)
        myGdxGame.batch.setProjectionMatrix(myGdxGame.uiCamera.combined);
        myGdxGame.batch.begin();
        pauseButton.draw(myGdxGame.batch);
        leftButton.draw(myGdxGame.batch);
        rightButton.draw(myGdxGame.batch);
        jumpButton.draw(myGdxGame.batch);

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
        accelerometerControl = false;
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
        playerObject = new PlayerObject(140, 180, PLAYER_WIDTH, PLAYER_HEIGHT, MyGdxGame.world,this);

        // Сбрасываем состояние сессии
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
        resumeButton.setPosition(startX, pauseMenuY+400);
        menuButton.setSize(buttonSize, buttonSize);
        menuButton.setPosition(startX + buttonSize + margin, pauseMenuY+400);

        // пересоздаём текстуру resumeButton как кнопку play (треугольник)
        resumeButton.recreateTexture(buttonSize, buttonSize);
        resumeButton.setPaused(true); // чтобы отображалась иконка play

        pausedText.setPosition(width/2f - pausedText.width/2f, pauseMenuY + buttonSize/2);
        float moveButtonSize = width * 0.1f;
        float marginBottom = height * 0.05f;
        float marginSide = width * 0.02f;

        leftButton.setSize(moveButtonSize, moveButtonSize);
        leftButton.recreateTexture(moveButtonSize, moveButtonSize);
        leftButton.setPosition(marginSide, marginBottom);

        rightButton.setSize(moveButtonSize, moveButtonSize);
        rightButton.recreateTexture(moveButtonSize, moveButtonSize);
        rightButton.setPosition(marginSide + moveButtonSize + marginSide, marginBottom);

        jumpButton.setSize(moveButtonSize, moveButtonSize);
        jumpButton.recreateTexture(moveButtonSize, moveButtonSize);
        jumpButton.setPosition(width - moveButtonSize - marginSide, marginBottom);
    }

    @Override
    public void show() {
        gameSession.startGame();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new KeyManager());
        multiplexer.addProcessor(new TouchInputProcessor(this));
        Gdx.input.setInputProcessor(multiplexer);
    }
    public void handleTouchDown(float x, float y, int pointer) {
        if (accelerometerControl) {
            if (pauseButton.isHit(x, y)) togglePause();
            return;
        } else {
            if (leftButton.isHit(x, y)) {
                activeTouches.put(pointer, leftButton);
                leftButton.onTouchDown();
            } else if (rightButton.isHit(x, y)) {
                activeTouches.put(pointer, rightButton);
                rightButton.onTouchDown();
            } else if (jumpButton.isHit(x, y)) {
                activeTouches.put(pointer, jumpButton);
                jumpButton.onTouchDown();
            } else if (pauseButton.isHit(x, y)) {
                togglePause();
            }
        }
        if (gameSession.getCurrentState() == GameState.PAUSED) {
            if (resumeButton.isHit(x, y)) {
                togglePause(); // возобновляем
            } else if (menuButton.isHit(x, y)) {
                // возврат в главное меню
                gameSession.endGame();
                myGdxGame.setScreen(new MenuScreen(myGdxGame));
            }
        }
    }

    public void handleTouchUp(int pointer) {
        MoveButton button = activeTouches.remove(pointer);
        if (button != null) {
            button.onTouchUp();
        }
    }

    public void handleTouchDragged(float x, float y, int pointer) {
        MoveButton button = activeTouches.get(pointer);
        if (button == null) return;
        if (!button.isHit(x, y)) {   // палец ушёл за пределы кнопки – отпускаем
            button.onTouchUp();
            activeTouches.remove(pointer);
        }
    }
    private void createMaze() {
        // Цвета
        Color MAZE_FLOOR_COLOR = new Color(0.25f, 0.2f, 0.15f, 1f);
        Color MAZE_WALL_COLOR = new Color(0.6f, 0.55f, 0.5f, 1f);

        // Размеры лабиринта
        int startX = 1520;          // левый край (пиксели)
        int startY = 1040;          // нижний край (чуть выше платформы y=1000)
        int cellSize = 96;          // размер клетки
        int wallThick = 6;         // толщина стен

        // Карта лабиринта (11x11): 0 – проход, 1 – стена
        int[][] maze = {
            {0,0,0,0,0,0,0,0,0,0,0},
            {1,1,1,1,0,1,1,1,0,1,0},
            {1,1,0,1,0,1,0,1,0,1,0},
            {0,1,0,1,1,1,0,1,1,1,0},
            {0,1,0,0,0,0,0,1,0,1,0},
            {0,1,1,1,0,1,1,1,0,1,0},
            {0,0,0,1,0,1,0,0,0,1,0},
            {0,1,1,1,1,1,0,1,1,1,0},
            {0,1,0,0,0,0,0,1,0,1,0},
            {0,1,1,1,0,1,1,1,1,1,0},
            {0,0,0,0,0,0,0,0,0,0,0}
        };
        // Вход: клетка (1,1) -> координаты startX+cellSize, startY+cellSize
        // Выход: клетка (9,9)

        // 1. Пол (там, где не стена)
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                if (maze[row][col] != 1) {
                    int px = startX + col * cellSize;
                    int py = startY + row * cellSize;
                    platforms.add(new PlatformObject(
                        px + cellSize/2, py + cellSize/2,
                        cellSize, cellSize, MyGdxGame.world,
                        PLATFORM_BIT, MAZE_FLOOR_COLOR
                    ));
                }
            }
        }

        // 2. Вертикальные стены (между столбцами)
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length - 1; col++) {
                // Если одна из соседних клеток – стена, а другая нет – ставим стену между ними
                if ((maze[row][col] == 1 || maze[row][col+1] == 1) &&
                    !(maze[row][col] == 1 && maze[row][col+1] == 1)) {
                    int wallX = startX + (col+1) * cellSize - wallThick/2;
                    int wallY = startY + row * cellSize;
                    addWall(wallX, wallY + cellSize/2, wallThick, cellSize, MAZE_WALL_COLOR);
                }
            }
        }

        // 3. Горизонтальные стены (между строками)
        for (int row = 0; row < maze.length - 1; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                if ((maze[row][col] == 1 || maze[row+1][col] == 1) &&
                    !(maze[row][col] == 1 && maze[row+1][col] == 1)) {
                    int wallX = startX + col * cellSize;
                    int wallY = startY + (row+1) * cellSize - wallThick/2;
                    addWall(wallX + cellSize/2, wallY, cellSize, wallThick, MAZE_WALL_COLOR);
                }
            }
        }

        // 4. Дополнительные мостики для входа и выхода
        // Вход (с платформы y=1000 на лабиринт)
        platforms.add(new PlatformObject(1600, 1020, 120, 20, MyGdxGame.world, PLATFORM_BIT, MAZE_FLOOR_COLOR));
        // Выход (из лабиринта на платформу y=1500)
        platforms.add(new PlatformObject(1600, 1520, 120, 20, MyGdxGame.world, PLATFORM_BIT, MAZE_FLOOR_COLOR));
    }
    private void addWall(int x, int y, int w, int h, Color color) {
        walls.add(new PlatformObject(x, y, w, h, MyGdxGame.world, PLATFORM_BIT, color));
    }
    private void checkAccelerometerActivation() {
        if (playerObject == null) return;
        if (platforms.size() <= 6) return;
        boolean onTarget = isOnTarget();
        if (onTarget && !accelerometerControl) {
            accelerometerControl = true;
            Gdx.app.log("Accelerometer", "Activated");
        } else if (!onTarget && accelerometerControl) {
            accelerometerControl = false;
            playerObject.resetVelocityX();
            Gdx.app.log("Accelerometer", "Deactivated");
        }
    }

    private boolean isOnTarget() {
        PlatformObject targetPlatform = platforms.get(6); // платформа 1600,1000
        float playerY = playerObject.getY();
        float platformY = targetPlatform.getY();
        float platformLeft = targetPlatform.getX() - targetPlatform.width / 2f;
        float platformRight = targetPlatform.getX() + targetPlatform.width / 2f;
        float playerX = playerObject.getX();
        return (playerY >= platformY - 5 && playerY <= platformY + 50 &&
            playerX >= platformLeft && playerX <= platformRight);
    }

    public boolean isAccelerometerActive() {
        return accelerometerControl;
    }
}
