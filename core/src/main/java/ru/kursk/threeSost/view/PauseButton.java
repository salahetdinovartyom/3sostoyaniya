package ru.kursk.threeSost.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PauseButton extends ButtonView {
    private static final Color BUTTON_COLOR = new Color(0.9f, 0.9f, 0.9f, 1f);
    private static final Color BACKGROUND_COLOR = new Color(0.1f, 0.1f, 0.15f, 0.8f);

    public PauseButton(float x, float y, float width, float height) {
        super(x, y, width, height);
        createButtonTexture();
    }

    private void createButtonTexture() {
        int w = (int) width;
        int h = (int) height;
        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        // прозрачный фон
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();

        // фон кнопки (полупрозрачный тёмный круг/прямоугольник – для красоты)
        pixmap.setColor(BACKGROUND_COLOR);
        pixmap.fillRectangle(0, 0, w, h);
        // рамка
        pixmap.setColor(BUTTON_COLOR);
        pixmap.drawRectangle(0, 0, w, h);

        // два вертикальных прямоугольника – символ паузы
        int barWidth = w / 6;
        int barHeight = h / 2;
        int gap = w / 6;
        int startX = (w - (2 * barWidth + gap)) / 2;
        int startY = (h - barHeight) / 2;

        pixmap.setColor(BUTTON_COLOR);
        pixmap.fillRectangle(startX, startY, barWidth, barHeight);
        pixmap.fillRectangle(startX + barWidth + gap, startY, barWidth, barHeight);

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        setTexture(texture);
    }
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void recreateTexture(float newWidth, float newHeight) {
        this.width = newWidth;
        this.height = newHeight;
        if (texture != null) {
            texture.dispose();
        }
        createButtonTexture();
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (region != null) {
            batch.draw(region, x, y, width, height);
        }
    }
}
