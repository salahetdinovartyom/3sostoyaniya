package ru.kursk.threeSost.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextButton extends ButtonView {
    private static final Color DEFAULT_BG = new Color(0.2f, 0.3f, 0.5f, 1f);
    private static final Color DEFAULT_BORDER = new Color(0.9f, 0.9f, 0.9f, 1f);
    private static final Color DEFAULT_TEXT = new Color(1f, 1f, 1f, 1f);

    private final BitmapFont font;
    private String text;
    private final Color bgColor,borderColor,textColor;
    private final GlyphLayout layout;

    public TextButton(BitmapFont font, String text, float x, float y, float width, float height) {
        this(font, text, x, y, width, height, DEFAULT_BG, DEFAULT_BORDER, DEFAULT_TEXT);
    }

    public TextButton(BitmapFont font, String text, float x, float y, float width, float height,
                      Color bgColor, Color borderColor, Color textColor) {
        super(x, y, width, height);
        this.font = font;
        this.text = text;
        this.bgColor = bgColor;
        this.borderColor = borderColor;
        this.textColor = textColor;
        this.layout = new GlyphLayout(font, text);
        createButtonTexture();
    }

    private void createButtonTexture() {
        int w = (int) width;
        int h = (int) height;
        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();

        // фон
        pixmap.setColor(bgColor);
        pixmap.fillRectangle(0, 0, w, h);
        // рамка
        pixmap.setColor(borderColor);
        pixmap.drawRectangle(0, 0, w, h);

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        setTexture(texture);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (region != null) {
            batch.draw(region, x, y, width, height);
        }
        // рисуем текст по центру
        float textX = x + (width - layout.width) / 2f;
        float textY = y + (height + layout.height) / 2f;
        font.setColor(textColor);
        font.draw(batch, text, textX, textY);
    }

    @Override
    public void dispose() {
        super.dispose();
        // шрифт не удаляем, он общий
    }
    public void setText (String text) {
        this.text=text;
    }
}
