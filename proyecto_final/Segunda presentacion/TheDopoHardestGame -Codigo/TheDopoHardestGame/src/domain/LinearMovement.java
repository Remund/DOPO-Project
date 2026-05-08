package domain;

public class LinearMovement implements MovementStrategy {

    private int directionX;
    private int directionY;

    public LinearMovement(int directionX, int directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
    }

    @Override
    public void move(Enemy enemy) {
        enemy.x += directionX;
        enemy.y += directionY;
    }

    public void reverseX() { directionX = -directionX; }
    public void reverseY() { directionY = -directionY; }
}