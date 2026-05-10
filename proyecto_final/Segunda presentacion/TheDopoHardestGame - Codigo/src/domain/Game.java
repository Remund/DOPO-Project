package domain;

public class Game {

    private Level  level;
    private Player player;
    private boolean running;
    private boolean paused;
    private int timeRemaining;
    private boolean monedasCompletas;
    private boolean enZonaFinal;

    public Game(Level level, Player player) {
        this.level         = level;
        this.player        = player;
        this.running       = false;
        this.paused        = false;
        this.timeRemaining = level.getTimeLimit();
        this.monedasCompletas = false;
        this.enZonaFinal = false;
    }

    public void start() {
        running = true;
    }

    public void pause() {
        setPaused(true);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void reset() {
        player.respawn();
        timeRemaining = level.getTimeLimit();
        running = true;
        paused = false;
        monedasCompletas = false;
        enZonaFinal = false;
        // resetear monedas
        for (Coin c : level.getCoins()) {
            c.reset();
        }
    }

    public void checkCollisions() {
        for (Enemy e : level.getEnemies()) {
            if (player.getBounds().intersects(e.getBounds())) {
                player.onEnemyCollision();
                // resetear monedas al morir
                for (Coin c : level.getCoins()) {
                    c.reset();
                }
                monedasCompletas = false;
                enZonaFinal = false;
            }
        }

        for (Coin c : level.getCoins()) {
            if (c.isActive() && player.getBounds().intersects(c.getBounds())) {
                c.collect(player);
            }
        }
    }

    public void moverJugador(int dx, int dy) throws GameException {
        if (!running || paused) {
            throw new GameException("El juego no esta en ejecucion", 100);
        }
        boolean hayPared = level.verificarColisionPared(
            player.getX() + dx,
            player.getY() + dy
        );
        if (!hayPared) {
            player.move(dx, dy);
        }
    }

    public void actualizarTiempo() {
        if (running && !paused) {
            timeRemaining--;
        }
    }
    
    public void update() {
        if (running && !paused) {
            level.update();
            checkCollisions();
        }
    }

    public boolean isLevelCompleted() {
        if (level.isCompleted()) {
            monedasCompletas = true;
        }
        return monedasCompletas && enZonaFinal;
    }

    public void setEnZonaFinal(boolean enZonaFinal) {
        this.enZonaFinal = enZonaFinal;
    }

    public boolean isMonedasCompletas() {
        return monedasCompletas;
    }

    public int getTiempoRestante() { return timeRemaining; }
    public boolean isRunning()     { return running; }
    public boolean isPaused()      { return paused; }
    public Level getLevel()        { return level; }
    public Player getPlayer()      { return player; }
}