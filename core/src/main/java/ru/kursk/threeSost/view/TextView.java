package ru.kursk.threeSost.view;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextView extends View {
    protected BitmapFont font;
    protected String text;
    public TextView(BitmapFont font, float x, float y, String text) {
        super(x, y, 0, 0); // временные размеры, будут пересчитаны
        this.font = font;
        setText(text);
    }

    public void setText(String text) {
        this.text = text;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (text != null && !text.isEmpty()) {
            font.draw(batch, text, x, y + height); // y + height, чтобы текст был выше точки привязки
        }
    }

    @Override
    public void dispose() {
        // Не удаляем font здесь, так как он может использоваться в других местах
        // Удаление шрифта должно быть в месте его создания (например, в MyGdxGame)
    }
}
