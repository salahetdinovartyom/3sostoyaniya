package ru.kursk.threeSost.managers;

import com.badlogic.gdx.Gdx;
import ru.kursk.threeSost.screens.GameScreen;

public class GameSession {
    private GameState currentState;
    private final GameScreen gameScreen;

    private float savedPlayerX;
    private float savedPlayerY;

    public GameSession(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        currentState = GameState.PLAYING;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void startGame() {
        currentState = GameState.PLAYING;
        gameScreen.resetGame();
        Gdx.app.log("GameSession", "Game started");
    }
    public void reset() {
        currentState = GameState.PLAYING;
        Gdx.app.log("GameSession", "State reset to PLAYING");
    }
    public void pauseGame() {
        if (currentState == GameState.PLAYING) {
            currentState = GameState.PAUSED;
            Gdx.app.log("GameSession", "Game paused");
        }
    }

    public void continueGame() {
        if (currentState == GameState.PAUSED) {
            currentState = GameState.PLAYING;
            Gdx.app.log("GameSession", "Game continued");
        }
    }

    public void endGame() {
        if (currentState == GameState.PLAYING || currentState == GameState.PAUSED) {
            savedPlayerX = gameScreen.getPlayerX();
            savedPlayerY = gameScreen.getPlayerY();
            currentState = GameState.ENDED;
            Gdx.app.log("GameSession", "Game ended. Saved position: " + savedPlayerX + ", " + savedPlayerY);

        }
    }

    public float getSavedPlayerX() { return savedPlayerX; }
    public float getSavedPlayerY() { return savedPlayerY; }
}
