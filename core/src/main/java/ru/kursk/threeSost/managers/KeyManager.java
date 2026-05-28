package ru.kursk.threeSost.managers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class KeyManager implements InputProcessor {
    private static boolean leftPressed;
    private static boolean rightPressed;
    private static boolean jumpPressed;
    private static boolean resetPressed;

    public KeyManager() {
        leftPressed = false;
        rightPressed = false;
        jumpPressed = false;
        resetPressed = false;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A, Input.Keys.LEFT -> leftPressed = true;
            case Input.Keys.D, Input.Keys.RIGHT -> rightPressed = true;
            case Input.Keys.W, Input.Keys.UP, Input.Keys.SPACE -> jumpPressed = true;
            case Input.Keys.R -> resetPressed = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A, Input.Keys.LEFT -> leftPressed = false;
            case Input.Keys.D, Input.Keys.RIGHT -> rightPressed = false;
            case Input.Keys.W, Input.Keys.UP, Input.Keys.SPACE -> jumpPressed = false;
            case Input.Keys.R -> resetPressed = false;
        }
        return false;
    }

    public static boolean isLeftPressed() { return leftPressed; }
    public static boolean isRightPressed() { return rightPressed; }
    public static boolean isJumpPressed() { return jumpPressed; }
    public static boolean isResetPressed() { return resetPressed; }

    public static boolean isAPressed() { return leftPressed; }
    public static boolean isDPressed() { return rightPressed; }
    public static boolean isSpacePressed() { return jumpPressed; }

    public static void resetJump() { jumpPressed = false; }
    public static void resetSpace() { resetJump(); }
    public static void resetRespawn() { resetPressed = false; }

    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }
}
