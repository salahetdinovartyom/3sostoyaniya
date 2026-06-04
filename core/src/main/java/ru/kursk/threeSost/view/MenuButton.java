package ru.kursk.threeSost.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuButton extends ButtonView {
    private static final Color BUTTON_COLOR = new Color(0.9f, 0.9f, 0.9f, 1f);
    private static final Color BACKGROUND_COLOR = new Color(0.1f, 0.1f, 0.15f, 0.8f);

    public MenuButton(float x, float y, float width, float height) {
        super(x, y, width, height);
        createButtonTexture();
    }

    private void createButtonTexture() {
        int w = (int) width;
        int h = (int) height;
        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();

        pixmap.setColor(BACKGROUND_COLOR);
        pixmap.fillRectangle(0, 0, w, h);
        // рамка
        pixmap.setColor(BUTTON_COLOR);
        pixmap.drawRectangle(0, 0, w, h);
        int lineWidth = (int) (w * 0.7f);
        int lineHeight = h / 7;
        int gap = h / 12;
        int startX = (w - lineWidth) / 2;
        int startY = (h - (3 * lineHeight + 2 * gap)) / 2;

        pixmap.setColor(BUTTON_COLOR);
        for (int i = 0; i < 3; i++) {
            pixmap.fillRectangle(startX, startY + i * (lineHeight + gap), lineWidth, lineHeight);
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        setTexture(texture);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (region != null) {
            batch.draw(region, x, y, width, height);
        }
    }
}
