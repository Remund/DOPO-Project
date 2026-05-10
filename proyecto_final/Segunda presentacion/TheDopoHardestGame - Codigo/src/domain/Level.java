package domain;

import java.util.ArrayList;
import java.util.List;

public class Level {

    private int timeLimit;
    private List<Enemy> enemies;
    private List<Coin> coins;
    private List<Player> players;

    public Level(int timeLimit) {
        this.timeLimit = timeLimit;
        this.enemies = new ArrayList<>();
        this.coins = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    public void addEnemy(Enemy e) {
        enemies.add(e);
    }

    public void addCoin(Coin c) {
        coins.add(c);
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public boolean isCompleted() {
        for (Coin c : coins) {
            if (!c.isCollected()) {
                return false;
            }
        }
        return true;
    }

    public int getRemainingCoins() {
        int count = 0;
        for (Coin c : coins) {
            if (!c.isCollected()) {
                count++;
            }
        }
        return count;
    }

    public boolean verificarColisionPared(int x, int y) {
        int size = 20;

        // zona segura izquierda
        boolean enZonaIzquierda = (x >= 20 && x + size <= 130
                && y >= 70 && y + size <= 270);

        // zona segura derecha
        boolean enZonaDerecha = (x >= 570 && x + size <= 680
                && y >= 70 && y + size <= 270);

        // rectangulo central
        boolean enRectanguloCentral = (x >= 130 && x + size <= 570
                && y >= 110 && y + size <= 230);

        // pasillo superior derecho
        // conecta zona derecha con rectangulo central
        // esquina superior derecha del rectangulo central
        boolean enPasilloSuperiorDerecho = (x >= 530 && x + size <= 680
                && y >= 70 && y + size <= 130);

        // pasillo inferior izquierdo
        // conecta zona izquierda con rectangulo central
        // esquina inferior izquierda del rectangulo central
        boolean enPasilloInferiorIzquierdo = (x >= 20 && x + size <= 170
                && y >= 210 && y + size <= 270);

        return !(enZonaIzquierda
                || enZonaDerecha
                || enRectanguloCentral
                || enPasilloSuperiorDerecho
                || enPasilloInferiorIzquierdo);
    }

    public void update() {
        for (Enemy e : enemies) {
            e.update();
        }
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public List<Player> getPlayers() {
        return players;
    }
}