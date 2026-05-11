package ru.kursk.threeSost.objects;

import com.badlogic.gdx.physics.box2d.World;
import static ru.kursk.threeSost.GameSettings.*;

public class PlayerObject extends GameObject {
    int livesLeft;

    public PlayerObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, PLAYER_BIT, world);
        livesLeft=3;
        body.setLinearDamping(10); //Это чтобы игрок не скользил, как на льду.
    }

    @Override
    public void hit() {livesLeft--;}
    public boolean isAlive() {return livesLeft>0;}
}
