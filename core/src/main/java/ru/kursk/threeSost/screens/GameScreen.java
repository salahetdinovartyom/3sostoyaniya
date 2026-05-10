package ru.kursk.threeSost.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.kursk.threeSost.MyGdxGame;

public class GameScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame=myGdxGame;
    }

    @Override
    public void render(float delta) {
        MyGdxGame.stepWorld();
        draw();
    }


    @Override
    public void dispose() {

    }
    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();

        myGdxGame.batch.end();
    }
}
