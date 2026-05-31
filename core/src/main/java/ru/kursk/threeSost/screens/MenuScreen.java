package ru.kursk.threeSost.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.kursk.threeSost.MyGdxGame;
import ru.kursk.threeSost.view.TextButton;
import ru.kursk.threeSost.view.TextView;

public class MenuScreen extends ScreenAdapter {
    private final MyGdxGame myGdxGame;
    private TextView title;
    private TextButton startButton;

    public MenuScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
    }

    @Override
    public void show() {
        // Создаём элементы (позиции будут установлены в resize)
        title = new TextView(myGdxGame.largeWhiteFont, 0, 0, "Three states");
        float buttonWidth = 300f;
        float buttonHeight = 80f;
        startButton = new TextButton(myGdxGame.largeWhiteFont, "Start game",
            0, 0, buttonWidth, buttonHeight);
        // Вызываем resize для начального позиционирования
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        handleInput();
        draw();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (startButton.isHit(touchX, touchY)) {
                startButton.onClick();
                myGdxGame.setScreen(new GameScreen(myGdxGame));
                dispose(); // освобождаем ресурсы меню
            }
        }
    }

    private void draw() {
        ScreenUtils.clear(new Color(0.1f, 0.12f, 0.2f, 1f));
        myGdxGame.batch.setProjectionMatrix(myGdxGame.uiCamera.combined);
        myGdxGame.batch.begin();
        title.draw(myGdxGame.batch);
        startButton.draw(myGdxGame.batch);
        myGdxGame.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Заголовок
        float titleX = width / 2f;
        float titleY = height * 0.7f;
        title.setPosition(titleX - title.width / 2f, titleY - title.height / 2f);
        // Кнопка
        float buttonX = width / 2f - startButton.width / 2f;
        float buttonY = height / 2f - startButton.height / 2f;
        startButton.setPosition(buttonX, buttonY);
    }

    @Override
    public void dispose() {
        title.dispose();
        startButton.dispose();
    }
}
