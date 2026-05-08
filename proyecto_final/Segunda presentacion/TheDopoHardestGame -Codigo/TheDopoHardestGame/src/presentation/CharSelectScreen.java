package presentation;

import javax.swing.*;
import java.awt.*;

public class CharSelectScreen extends JPanel {

    private GameGUI gui;

    public CharSelectScreen(GameGUI gui) {
        this.gui = gui;
        setPreferredSize(new Dimension(700, 500));
        setBackground(new Color(30, 30, 50));
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("SELECCIONA TU PERSONAJE", SwingConstants.CENTER);
        titulo.setFont(new Font("Courier New", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel charsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        charsPanel.setOpaque(false);
        charsPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        charsPanel.add(crearCharCard(
            new Color(231, 76, 60), 30, 30,
            "BLINKY",
            "Velocidad: 1x | Tamano: 1x",
            "Sin habilidades especiales"
        ));
        charsPanel.add(crearCharCard(
            new Color(52, 152, 219), 40, 40,
            "INKY",
            "Velocidad: 1.5x | Tamano: 1.5x",
            "Mas rapido y mas grande"
        ));
        charsPanel.add(crearCharCard(
            new Color(46, 204, 113), 30, 30,
            "CLYDE",
            "Velocidad: 1x | Tamano: 1x",
            "Absorbe 1 golpe, baja a 0.7x"
        ));

        add(charsPanel, BorderLayout.CENTER);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setOpaque(false);
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton btnVolver = crearBotonNav("Volver");
        JButton btnSig    = crearBotonNav("Siguiente");
        btnSig.setBackground(Color.WHITE);
        btnSig.setForeground(new Color(30, 30, 50));

        btnVolver.addActionListener(e -> gui.showScreen(GameGUI.MODE_SELECT));
        btnSig.addActionListener(e -> gui.showScreen(GameGUI.LEVEL_SELECT));

        navPanel.add(btnVolver);
        navPanel.add(btnSig);
        add(navPanel, BorderLayout.SOUTH);
    }

    private JPanel crearCharCard(Color color, int w, int h,
                                  String nombre, String stats, String habilidad) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(50, 50, 70));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 3),
            BorderFactory.createEmptyBorder(20, 10, 20, 10)
        ));

        JPanel cuadrado = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int cx = (getWidth() - w) / 2;
                int cy = (getHeight() - h) / 2;
                g.setColor(color);
                g.fillRect(cx, cy, w, h);
                g.setColor(Color.WHITE);
                g.drawRect(cx, cy, w, h);
            }
        };
        cuadrado.setOpaque(false);
        cuadrado.setPreferredSize(new Dimension(80, 70));
        cuadrado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel lblNombre = new JLabel(nombre, SwingConstants.CENTER);
        lblNombre.setFont(new Font("Courier New", Font.BOLD, 14));
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblStats = new JLabel(
            "<html><center>" + stats + "</center></html>",
            SwingConstants.CENTER
        );
        lblStats.setFont(new Font("Courier New", Font.PLAIN, 10));
        lblStats.setForeground(new Color(200, 200, 200));
        lblStats.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblHab = new JLabel(
            "<html><center>" + habilidad + "</center></html>",
            SwingConstants.CENTER
        );
        lblHab.setFont(new Font("Courier New", Font.ITALIC, 10));
        lblHab.setForeground(color.brighter());
        lblHab.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnSel = new JButton("Elegir");
        btnSel.setFont(new Font("Courier New", Font.BOLD, 11));
        btnSel.setBackground(color);
        btnSel.setForeground(Color.WHITE);
        btnSel.setFocusPainted(false);
        btnSel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSel.addActionListener(e -> gui.showScreen(GameGUI.LEVEL_SELECT));

        card.add(cuadrado);
        card.add(Box.createVerticalStrut(10));
        card.add(lblNombre);
        card.add(Box.createVerticalStrut(6));
        card.add(lblStats);
        card.add(Box.createVerticalStrut(4));
        card.add(lblHab);
        card.add(Box.createVerticalStrut(14));
        card.add(btnSel);

        return card;
    }

    private JButton crearBotonNav(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Courier New", Font.BOLD, 12));
        btn.setBackground(new Color(50, 50, 70));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        return btn;
    }
}