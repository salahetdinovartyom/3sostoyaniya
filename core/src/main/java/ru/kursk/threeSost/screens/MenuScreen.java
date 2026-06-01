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
    private TextButton startButton, continueButton, exitButton;

    public MenuScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
    }

    @Override
    public void show() {
        // Создаём элементы (позиции будут установлены в resize)
        title = new TextView(myGdxGame.largeWhiteFont, 0, 0, "Three states");
        float buttonWidth = 600f;
        float buttonHeight = 160f;
        startButton = new TextButton(myGdxGame.commonWhiteFont, "Start game",
            0, 0, buttonWidth, buttonHeight);
        continueButton = new TextButton(myGdxGame.commonWhiteFont, "Coming Soon",
            0, 0, buttonWidth, buttonHeight);
        exitButton = new TextButton(myGdxGame.commonWhiteFont, "Exit",
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
            if (continueButton.isHit(touchX,touchY)) {
                continueButton.onClick();
                continueButton.setText("I SAY COMING SOON!!!!!!");
            }
            if (exitButton.isHit(touchX,touchY)) {
                exitButton.onClick();
                Gdx.app.exit();

            }
        }
    }

    private void draw() {
        ScreenUtils.clear(new Color(0.1f, 0.12f, 0.2f, 1f));
        myGdxGame.batch.setProjectionMatrix(myGdxGame.uiCamera.combined);
        myGdxGame.batch.begin();
        title.draw(myGdxGame.batch);
        startButton.draw(myGdxGame.batch);
        continueButton.draw(myGdxGame.batch);
        exitButton.draw(myGdxGame.batch);
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
        continueButton.setPosition(buttonX,buttonY-continueButton.height-10);
        exitButton.setPosition(buttonX,buttonY-continueButton.height-10-exitButton.height-10);
    }

    @Override
    public void dispose() {
        title.dispose();
        startButton.dispose();
        continueButton.dispose();
        exitButton.dispose();
    }
}
