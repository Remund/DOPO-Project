package domain;

public abstract class Coin extends GameElement {

    protected boolean collected;

    public Coin(int x, int y) {
        super(x, y, 12, 12);
        this.collected = false;
    }

    public void collect(Player player) {
        this.collected = true;
    }

    public boolean isCollected() { return collected; }

    @Override
    public boolean isActive() { return !collected; }
}