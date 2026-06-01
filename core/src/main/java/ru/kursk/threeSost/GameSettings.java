package ru.kursk.threeSost;

import com.badlogic.gdx.math.Vector2;

public class GameSettings {
    public static final float PLAYER_MOVE_SPEED = 7f;
    public static final float PLAYER_ACCELERATION = 60f;
    public static final float PLAYER_AIR_ACCELERATION = 32f;
    public static final float PLAYER_JUMP_SPEED = 12f;
    public static final Vector2 GRAVITY = new Vector2(0f, -28f);

    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final float WORLD_WIDTH = SCREEN_WIDTH * 3f;

    public static final float STEP_TIME = 1f / 60f;
    public static final float SCALE = 1f / 100f;

    public static final short PLATFORM_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short PLAYER_FOOT_BIT = 4;
    public static final short WALL_BIT = 8;

    public static final int PLAYER_WIDTH = 76;
    public static final int PLAYER_HEIGHT = 44;
    public static final float WORLD_HEIGHT = SCREEN_HEIGHT * 3f;
    public static final float ACCELEROMETER_SENSITIVITY = 10f;          // горизонталь
    public static final float ACCELEROMETER_VERTICAL_SENSITIVITY = 10f; // вертикаль
    public static final float MAX_VERTICAL_ACCEL_SPEED = 100f;          // ограничение скорости от акселерометра
}
