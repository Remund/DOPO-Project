package domain;

import java.awt.Rectangle;

public abstract class Enemy extends GameElement {

    protected double speed;
    private MovementStrategy movementStrategy;

    public Enemy(int x, int y, int width, int height, double speed) {
        super(x, y, width, height);
        this.speed = speed;
    }

    public abstract void update();

    public void setMovementStrategy(MovementStrategy strategy) {
        this.movementStrategy = strategy;
    }

    protected void executeMovement() {
        if (movementStrategy != null) {
            movementStrategy.move(this);
        }
    }
}