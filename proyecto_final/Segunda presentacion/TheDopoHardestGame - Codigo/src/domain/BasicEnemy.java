package domain;

public class BasicEnemy extends Enemy {

    public BasicEnemy(int x, int y) {
        super(x, y, 16, 16, 1.0);
    }

    @Override
    public void update() {
        executeMovement();
    }
}