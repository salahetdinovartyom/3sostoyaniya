package ru.kursk.threeSost.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.kursk.threeSost.managers.KeyManager;

public class MoveButton extends ButtonView {
    public enum ButtonType { LEFT, RIGHT, JUMP }
    private final ButtonType type;

    public MoveButton(ButtonType type, float x, float y, float width, float height) {
        super(x, y, width, height);
        this.type = type;
        createButtonTexture();
    }

    private void createButtonTexture() {
        int w = (int) width;
        int h = (int) height;
        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();

        // фон (полупрозрачный тёмный)
        pixmap.setColor(new Color(0.2f, 0.2f, 0.3f, 0.7f));
        pixmap.fillRectangle(0, 0, w, h);
        pixmap.setColor(new Color(0.9f, 0.9f, 0.9f, 1f));
        pixmap.drawRectangle(0, 0, w, h);

        pixmap.setColor(Color.WHITE);
        int centerX = w / 2;
        int centerY = h / 2;

        switch (type) {
            case LEFT: {
                int size = h / 3;
                int[] xPoints = {centerX + size/2, centerX - size/2, centerX + size/2};
                int[] yPoints = {centerY - size/2, centerY, centerY + size/2};
                pixmap.fillTriangle(xPoints[0], yPoints[0], xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
                break;
            }
            case RIGHT: {
                int size = h / 3;
                int[] xPoints = {centerX - size/2, centerX + size/2, centerX - size/2};
                int[] yPoints = {centerY - size/2, centerY, centerY + size/2};
                pixmap.fillTriangle(xPoints[0], yPoints[0], xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
                break;
            }
            case JUMP: {
                // увеличенный и перевёрнутый треугольник (стрелка вниз)
                int largerSize = (int)(h * 0.45f); // 45% от высоты – пропорционально увеличен
                int[] xPoints = {centerX - largerSize/2, centerX, centerX + largerSize/2};
                int[] yPoints = {centerY + largerSize/2, centerY - largerSize/4 , centerY + largerSize/2};
                pixmap.fillTriangle(xPoints[0], yPoints[0], xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
                break;
            }
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        setTexture(texture);
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

    public void onTouchDown() {
        switch (type) {
            case LEFT -> KeyManager.setLeftPressed(true);
            case RIGHT -> KeyManager.setRightPressed(true);
            case JUMP -> KeyManager.setJumpPressed(true);
        }
    }

    public void onTouchUp() {
        switch (type) {
            case LEFT -> KeyManager.setLeftPressed(false);
            case RIGHT -> KeyManager.setRightPressed(false);
            case JUMP -> KeyManager.setJumpPressed(false);
        }
    }
}
