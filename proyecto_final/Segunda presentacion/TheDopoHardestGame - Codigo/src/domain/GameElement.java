package domain;

import java.awt.Rectangle;

public abstract class GameElement {

    protected int x;
    protected int y;
    private int width;
    private int height;

    public GameElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean isActive() { return true; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}