package ru.kursk.threeSost.view;

public abstract class ButtonView extends View {
    protected boolean isPressed;

    public ButtonView(float x, float y, float width, float height) {
        super(x, y, width, height);
        isPressed = false;
    }

}
