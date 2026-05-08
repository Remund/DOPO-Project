package domain;

import java.awt.Point;
import java.awt.Rectangle;

public abstract class Player {

    protected int x;
    protected int y;
    protected int deaths;
    protected double speed;
    protected double size;
    protected Point spawnPoint;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.deaths = 0;
        this.spawnPoint = new Point(x, y);
    }

    public void move(int dx, int dy) {
        x += (int)(dx * getSpeed());
        y += (int)(dy * getSpeed());
    }

    public abstract double getSpeed();
    public abstract double getSize();
    public abstract void onEnemyCollision();

    public void incrementDeaths() {
        deaths++;
    }

    public void setPosition(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public void setSpawnPoint(Point p) {
        this.spawnPoint = p;
    }

    public void respawn() {
        this.x = spawnPoint.x;
        this.y = spawnPoint.y;
    }

    public void setSkinTemporal(String skinType) {
        // Se implementara en versiones futuras
    }

    public int getDeaths() { return deaths; }
    public int getX()      { return x; }
    public int getY()      { return y; }

    public Rectangle getBounds() {
        int s = (int)(20 * getSize());
        return new Rectangle(x, y, s, s);
    }
}