package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import domain.BasicEnemy;
import domain.Game;
import domain.GameException;
import domain.Level;
import domain.LinearMovement;
import domain.RedPlayer;
import domain.YellowCoin;
import domain.Player;
import domain.Enemy;
import domain.Coin;

public class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private GameGUI gui;
    private Game game;
    private Timer timer;

    private JLabel lblTiempo;
    private JLabel lblMuertes;
    private JLabel lblMonedas;

    public GamePanel(GameGUI gui) {
        this.gui = gui;

        setPreferredSize(new Dimension(700, 500));
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        add(crearHudTop(), BorderLayout.NORTH);
        add(crearPanelJuego(), BorderLayout.CENTER);
        add(crearHudBottom(), BorderLayout.SOUTH);

        inicializarNivel1();
        configurarTimer();
        configurarTeclado();
    }

    private void inicializarNivel1() {
        Level level = new Level(9999);
        RedPlayer player = new RedPlayer(55, 155);

        level.addPlayer(player);

        BasicEnemy e1 = new BasicEnemy(200, 125);
        e1.setMovementStrategy(new LinearMovement(2, 0));
        level.addEnemy(e1);

        BasicEnemy e2 = new BasicEnemy(280, 165);
        e2.setMovementStrategy(new LinearMovement(-2, 0));
        level.addEnemy(e2);

        BasicEnemy e3 = new BasicEnemy(390, 125);
        e3.setMovementStrategy(new LinearMovement(2, 0));
        level.addEnemy(e3);

        BasicEnemy e4 = new BasicEnemy(470, 165);
        e4.setMovementStrategy(new LinearMovement(-2, 0));
        level.addEnemy(e4);

        level.addCoin(new YellowCoin(145, 242));
        level.addCoin(new YellowCoin(545, 82));

        game = new Game(level, player);
        game.start();

        actualizarHud();
    }

    private void configurarTimer() {
        timer = new Timer(30, e -> {
            game.update();

            // verificar si jugador esta en zona final
            Player p = game.getPlayer();
            boolean enZonaFinal = (p.getX() >= 570 && p.getX() + 20 <= 680
                    && p.getY() >= 70 && p.getY() + 20 <= 270);
            game.setEnZonaFinal(enZonaFinal);

            actualizarHud();
            repaint();

            if (game.isLevelCompleted()) {
                timer.stop();
                gui.showScreen(GameGUI.RESULT);
            }
        });
        timer.start();
        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

    private void configurarTeclado() {
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W:
                        case KeyEvent.VK_UP:
                            game.moverJugador(0, -5);
                            break;
                        case KeyEvent.VK_S:
                        case KeyEvent.VK_DOWN:
                            game.moverJugador(0, 5);
                            break;
                        case KeyEvent.VK_A:
                        case KeyEvent.VK_LEFT:
                            game.moverJugador(-5, 0);
                            break;
                        case KeyEvent.VK_D:
                        case KeyEvent.VK_RIGHT:
                            game.moverJugador(5, 0);
                            break;
                        case KeyEvent.VK_ESCAPE:
                            gui.showScreen(GameGUI.PAUSE);
                            break;
                    }
                } catch (GameException ex) {
                    // por ahora no hacemos nada
                }

                actualizarHud();
                repaint();
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    private JPanel crearHudTop() {
        JPanel hud = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 6));
        hud.setBackground(new Color(34, 34, 34));
        hud.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(80, 80, 80)));

        JButton btnMenu = crearHudButton("MENU", e -> gui.showScreen(GameGUI.PAUSE));
        hud.add(btnMenu);

        JLabel lblNivel = crearHudLabel("NIVEL");
        hud.add(lblNivel);

        JLabel lblNivelValor = crearBadgeLabel("1 / 3", new Color(241, 196, 15), new Color(34, 34, 34));
        hud.add(lblNivelValor);

        lblTiempo = crearBadgeLabel("Tiempo", new Color(46, 204, 113), new Color(34, 34, 34));
        hud.add(lblTiempo);

        lblMuertes = crearBadgeLabel("Muertes: 0", new Color(231, 76, 60), Color.WHITE);
        hud.add(lblMuertes);

        lblMonedas = crearBadgeLabel("Monedas: 0 / 2", new Color(241, 196, 15), new Color(34, 34, 34));
        hud.add(lblMonedas);

        JButton btnPausa = crearHudButton("PAUSA", e -> gui.showScreen(GameGUI.PAUSE));
        hud.add(btnPausa);

        return hud;
    }

    private JPanel crearPanelJuego() {
        JPanel panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarJuego((Graphics2D) g);
            }
        };
        panel.setBackground(new Color(122, 143, 201));
        panel.setPreferredSize(new Dimension(700, 400));
        return panel;
    }

    private void dibujarJuego(Graphics2D g2) {
        g2.setColor(new Color(122, 143, 201));
        g2.fillRect(0, 0, 700, 400);

        dibujarZonaSegura(g2, 20, 70, 110, 200);
        dibujarZonaSegura(g2, 570, 70, 110, 200);

        dibujarPasillo(g2, 130, 230, 40, 40);
        dibujarPasillo(g2, 530, 70, 40, 40);

        dibujarCorredor(g2, 130, 110, 440, 120);

        Player player = game.getPlayer();
        g2.setColor(new Color(231, 76, 60));
        g2.fillRect(player.getX(), player.getY(), 20, 20);
        g2.setColor(Color.WHITE);
        g2.drawRect(player.getX(), player.getY(), 20, 20);

        for (Enemy e : game.getLevel().getEnemies()) {
            g2.setColor(new Color(36, 113, 212));
            g2.fillOval(e.getX(), e.getY(), 16, 16);
            g2.setColor(new Color(26, 58, 110));
            g2.drawOval(e.getX(), e.getY(), 16, 16);
        }

        for (Coin c : game.getLevel().getCoins()) {
            if (c.isActive()) {
                g2.setColor(new Color(241, 196, 15));
                g2.fillOval(c.getX(), c.getY(), 12, 12);
                g2.setColor(new Color(183, 149, 11));
                g2.drawOval(c.getX(), c.getY(), 12, 12);
            }
        }
    }

    private void dibujarZonaSegura(Graphics2D g2, int x, int y, int w, int h) {
        g2.setColor(new Color(109, 187, 109));
        g2.fillRect(x, y, w, h);
        g2.setColor(new Color(58, 138, 58));
        g2.drawRect(x, y, w, h);
    }

    private void dibujarPasillo(Graphics2D g2, int x, int y, int w, int h) {
        dibujarCorredor(g2, x, y, w, h);
    }

    private void dibujarCorredor(Graphics2D g2, int x, int y, int w, int h) {
        int tile = 18;

        for (int col = 0; col <= w / tile; col++) {
            for (int row = 0; row <= h / tile; row++) {
                int px = x + col * tile;
                int py = y + row * tile;
                int tw = Math.min(tile, x + w - px);
                int th = Math.min(tile, y + h - py);
                if (tw <= 0 || th <= 0) {
                    continue;
                }

                if ((col + row) % 2 == 0) {
                    g2.setColor(new Color(216, 216, 216));
                } else {
                    g2.setColor(Color.WHITE);
                }
                g2.fillRect(px, py, tw, th);
            }
        }

        g2.setColor(new Color(51, 51, 51));
        g2.drawRect(x, y, w, h);
    }

    private JPanel crearHudBottom() {
        JPanel hud = new JPanel(new BorderLayout());
        hud.setBackground(new Color(34, 34, 34));
        hud.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(80, 80, 80)));

        JLabel lblInfo = new JLabel("Controles: WASD / Flechas");
        lblInfo.setFont(new Font("Courier New", Font.PLAIN, 11));
        lblInfo.setForeground(new Color(200, 200, 200));
        lblInfo.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        hud.add(lblInfo, BorderLayout.WEST);

        return hud;
    }

    private void actualizarHud() {
        lblTiempo.setText("Tiempo: " + game.getTiempoRestante());
        lblMuertes.setText("Muertes: " + game.getPlayer().getDeaths());
        lblMonedas.setText("Monedas: " + (2 - game.getLevel().getRemainingCoins()) + " / 2");
    }

    private JLabel crearHudLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Courier New", Font.BOLD, 13));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private JLabel crearBadgeLabel(String texto, Color bg, Color fg) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Courier New", Font.BOLD, 12));
        lbl.setForeground(fg);
        lbl.setBackground(bg);
        lbl.setOpaque(true);
        lbl.setBorder(BorderFactory.createEmptyBorder(2, 7, 2, 7));
        return lbl;
    }

    private JButton crearHudButton(String texto, ActionListener listener) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Courier New", Font.BOLD, 11));
        btn.setBackground(new Color(60, 60, 60));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            BorderFactory.createEmptyBorder(3, 9, 3, 9)
        ));
        btn.addActionListener(listener);
        return btn;
    }
}