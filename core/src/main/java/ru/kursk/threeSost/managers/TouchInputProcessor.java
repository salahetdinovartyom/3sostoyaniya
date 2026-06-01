package ru.kursk.threeSost.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import ru.kursk.threeSost.screens.GameScreen;

public class TouchInputProcessor implements InputProcessor {
    private final GameScreen gameScreen;

    public TouchInputProcessor(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float x = screenX;
        float y = Gdx.graphics.getHeight() - screenY;
        gameScreen.handleTouchDown(x, y, pointer);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        float x = screenX;
        float y = Gdx.graphics.getHeight() - screenY;
        gameScreen.handleTouchUp(x, y, pointer);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float x = screenX;
        float y = Gdx.graphics.getHeight() - screenY;
        gameScreen.handleTouchDragged(x, y, pointer);
        return true;
    }

    @Override public boolean keyDown(int keycode) { return false; }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }
}
