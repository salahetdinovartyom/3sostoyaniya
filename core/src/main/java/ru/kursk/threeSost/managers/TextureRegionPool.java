package ru.kursk.threeSost.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureRegionPool {
    private static TextureRegion whitePixel;

    public static TextureRegion getWhitePixel() {
        if (whitePixel == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            Texture texture = new Texture(pixmap);
            pixmap.dispose();
            whitePixel = new TextureRegion(texture);
        }
        return whitePixel;
    }

    public static void dispose() {
        if (whitePixel != null) {
            whitePixel.getTexture().dispose();
            whitePixel = null;
        }
    }
}
