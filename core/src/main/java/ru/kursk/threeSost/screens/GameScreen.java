package ru.kursk.threeSost.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.kursk.threeSost.MyGdxGame;
import ru.kursk.threeSost.objects.PlayerObject;

import static ru.kursk.threeSost.GameSettings.*;
import static ru.kursk.threeSost.GameResources.*;

public class GameScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    PlayerObject playerObject;
    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame=myGdxGame;
        playerObject=new PlayerObject(SCREEN_WIDTH/2,100,PLAYER_WIDTH,PLAYER_HEIGHT,PLAYER_IMG_PATH,MyGdxGame.world);
    }


    @Override
    public void render(float delta) {
        MyGdxGame.stepWorld();
        draw();
    }


    @Override
    public void dispose() {
        playerObject.dispose();
    }
    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();

        playerObject.draw(myGdxGame.batch);

        myGdxGame.batch.end();
    }
}
