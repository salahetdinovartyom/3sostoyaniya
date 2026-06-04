package ru.kursk.threeSost.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PauseButton extends ButtonView {
    private static final Color BUTTON_COLOR = new Color(0.9f, 0.9f, 0.9f, 1f);
    private static final Color BACKGROUND_COLOR = new Color(0.1f, 0.1f, 0.15f, 0.8f);
    private Texture pauseTexture;
    private Texture playTexture;

    public PauseButton(float x, float y, float width, float height) {
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
        pixmap.setColor(BUTTON_COLOR);
        pixmap.drawRectangle(0, 0, w, h);

        int barWidth = w / 6;
        int barHeight = h / 2;
        int gap = w / 6;
        int startX = (w - (2 * barWidth + gap)) / 2;
        int startY = (h - barHeight) / 2;
        pixmap.fillRectangle(startX, startY, barWidth, barHeight);
        pixmap.fillRectangle(startX + barWidth + gap, startY, barWidth, barHeight);

        pauseTexture = new Texture(pixmap);
        pixmap.dispose();

        createPlayTexture();
        setTexture(pauseTexture);
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
    private void createPlayTexture() {
        int w = (int) width;
        int h = (int) height;
        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();
        pixmap.setColor(BACKGROUND_COLOR);
        pixmap.fillRectangle(0, 0, w, h);
        pixmap.setColor(BUTTON_COLOR);
        pixmap.drawRectangle(0, 0, w, h);

        int size = h / 2;
        int centerX = w / 2;
        int centerY = h / 2;
        int[] xPoints = {centerX - size/2, centerX - size/2, centerX + size/2};
        int[] yPoints = {centerY - size/2, centerY + size/2, centerY};
        pixmap.fillTriangle(xPoints[0], yPoints[0], xPoints[1], yPoints[1], xPoints[2], yPoints[2]);

        playTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    public void setPaused(boolean paused) {
        setTexture(paused ? playTexture : pauseTexture);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (playTexture != null) playTexture.dispose();
        if (pauseTexture != null && pauseTexture != texture) pauseTexture.dispose();
    }
}
