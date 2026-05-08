package domain;

import java.util.ArrayList;
import java.util.List;

public class Level {

    private int timeLimit;
    private List<Enemy>  enemies;
    private List<Coin>   coins;
    private List<Player> players;

    public Level(int timeLimit) {
        this.timeLimit = timeLimit;
        this.enemies   = new ArrayList<>();
        this.coins     = new ArrayList<>();
        this.players   = new ArrayList<>();
    }

    public void addEnemy(Enemy e)   { enemies.add(e); }
    public void addCoin(Coin c)     { coins.add(c); }
    public void addPlayer(Player p) { players.add(p); }

    public boolean isCompleted() {
        for (Coin c : coins) {
            if (!c.isCollected()) return false;
        }
        return true;
    }

    public int getRemainingCoins() {
        int count = 0;
        for (Coin c : coins) {
            if (!c.isCollected()) count++;
        }
        return count;
    }

    public boolean verificarColisionPared(int x, int y) {
        // Por ahora retorna false, se implementara con el mapa real
        return false;
    }

    public void update() {
        for (Enemy e : enemies) {
            e.update();
        }
    }

    public int getTimeLimit()          { return timeLimit; }
    public List<Enemy>  getEnemies()   { return enemies; }
    public List<Coin>   getCoins()     { return coins; }
    public List<Player> getPlayers()   { return players; }
}