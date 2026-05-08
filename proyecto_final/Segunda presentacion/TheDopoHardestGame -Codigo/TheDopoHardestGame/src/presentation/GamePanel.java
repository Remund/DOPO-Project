package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private GameGUI gui;

    public GamePanel(GameGUI gui) {
        this.gui = gui;

        setPreferredSize(new Dimension(700, 500));
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        add(crearHudTop(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearHudBottom(), BorderLayout.SOUTH);
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

        JLabel lblTiempo = crearBadgeLabel("01:24", new Color(46, 204, 113), new Color(34, 34, 34));
        hud.add(lblTiempo);

        JLabel lblMuertes = crearBadgeLabel("Muertes: 0", new Color(231, 76, 60), Color.WHITE);
        hud.add(lblMuertes);

        JLabel lblMonedas = crearBadgeLabel("Monedas: 0 / 10", new Color(241, 196, 15), new Color(34, 34, 34));
        hud.add(lblMonedas);

        JButton btnPausa = crearHudButton("PAUSA", e -> gui.showScreen(GameGUI.PAUSE));
        hud.add(btnPausa);

        return hud;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(122, 143, 201));
        panel.setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(40, 40, 40), 3),
            BorderFactory.createEmptyBorder(35, 45, 35, 45)
        ));

        JLabel titulo = new JLabel("NIVEL BASICO EN CONSTRUCCION", SwingConstants.CENTER);
        titulo.setFont(new Font("Courier New", Font.BOLD, 20));
        titulo.setForeground(new Color(30, 30, 50));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("La logica del juego estara disponible proximamente.", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Courier New", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(80, 80, 80));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descripcion = new JLabel(
            "<html><center>" +
            "Esta pantalla representa el GamePanel.<br>" +
            "Aqui se mostrara el nivel basico con jugador rojo,<br>" +
            "monedas, enemigos basicos, zonas seguras y objetos especiales." +
            "</center></html>",
            SwingConstants.CENTER
        );
        descripcion.setFont(new Font("Courier New", Font.PLAIN, 12));
        descripcion.setForeground(new Color(70, 70, 70));
        descripcion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnResultado = new JButton("Simular victoria");
        btnResultado.setFont(new Font("Courier New", Font.BOLD, 12));
        btnResultado.setBackground(new Color(39, 174, 96));
        btnResultado.setForeground(Color.WHITE);
        btnResultado.setFocusPainted(false);
        btnResultado.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnResultado.addActionListener(e -> gui.showScreen(GameGUI.RESULT));

        JButton btnPausa = new JButton("Ir a pausa");
        btnPausa.setFont(new Font("Courier New", Font.BOLD, 12));
        btnPausa.setBackground(new Color(41, 128, 185));
        btnPausa.setForeground(Color.WHITE);
        btnPausa.setFocusPainted(false);
        btnPausa.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPausa.addActionListener(e -> gui.showScreen(GameGUI.PAUSE));

        card.add(titulo);
        card.add(Box.createVerticalStrut(15));
        card.add(subtitulo);
        card.add(Box.createVerticalStrut(20));
        card.add(descripcion);
        card.add(Box.createVerticalStrut(25));
        card.add(btnResultado);
        card.add(Box.createVerticalStrut(10));
        card.add(btnPausa);

        panel.add(card);

        return panel;
    }

    private JPanel crearHudBottom() {
        JPanel hud = new JPanel(new BorderLayout());
        hud.setBackground(new Color(34, 34, 34));
        hud.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(80, 80, 80)));

        JLabel lblInfo = new JLabel("Controles: WASD / Flechas     Estado: Maqueta de interfaz");
        lblInfo.setFont(new Font("Courier New", Font.PLAIN, 11));
        lblInfo.setForeground(new Color(200, 200, 200));
        lblInfo.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        hud.add(lblInfo, BorderLayout.WEST);

        return hud;
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