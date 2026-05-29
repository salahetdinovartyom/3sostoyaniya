package ru.kursk.threeSost.view;

public abstract class ButtonView extends View {
    protected boolean isPressed;

    public ButtonView(float x, float y, float width, float height) {
        super(x, y, width, height);
        isPressed = false;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public boolean isPressed() {
        return isPressed;
    }

    // вызывается при нажатии (можно переопределить)
    public void onClick() {
        // заглушка для наследников
    }
}
