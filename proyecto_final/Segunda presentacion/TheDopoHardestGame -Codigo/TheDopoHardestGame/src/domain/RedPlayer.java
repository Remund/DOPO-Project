package domain;

public class RedPlayer extends Player {

    public RedPlayer(int x, int y) {
        super(x, y);
        this.speed = 1.0;
        this.size  = 1.0;
    }

    @Override
    public double getSpeed() { return speed; }

    @Override
    public double getSize()  { return size; }

    @Override
    public void onEnemyCollision() {
        incrementDeaths();
        respawn();
    }
}