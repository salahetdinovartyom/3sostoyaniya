package ru.kursk.threeSost.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public abstract class View implements Disposable {
    protected float x;
    protected float y;
    public float width;
    public float height;
    protected Texture texture;
    protected TextureRegion region;

    // Конструктор с указанием всех размеров
    public View(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        this.region = new TextureRegion(texture);
    }
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setTextureRegion(TextureRegion region) {
        this.region = region;
        this.texture = region.getTexture();
    }

    public boolean isHit(float touchX, float touchY) {
        return touchX >= x && touchX <= x + width && touchY >= y && touchY <= y + height;
    }

    public abstract void draw(SpriteBatch batch);

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
    }
}
