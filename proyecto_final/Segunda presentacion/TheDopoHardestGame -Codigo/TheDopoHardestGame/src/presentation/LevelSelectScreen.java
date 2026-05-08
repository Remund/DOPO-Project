package presentation;

import javax.swing.*;
import java.awt.*;

public class LevelSelectScreen extends JPanel {

    private GameGUI gui;

    public LevelSelectScreen(GameGUI gui) {
        this.gui = gui;
        setPreferredSize(new Dimension(700, 500));
        setBackground(new Color(30, 30, 50));
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("SELECCIONA EL NIVEL", SwingConstants.CENTER);
        titulo.setFont(new Font("Courier New", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel nivelesPanel = new JPanel();
        nivelesPanel.setLayout(new BoxLayout(nivelesPanel, BoxLayout.Y_AXIS));
        nivelesPanel.setOpaque(false);
        nivelesPanel.setBorder(BorderFactory.createEmptyBorder(0, 80, 0, 80));

        nivelesPanel.add(crearNivelItem(
            "Nivel 1", "Original - The World's Hardest Game",
            "Basico", new Color(39, 174, 96)
        ));
        nivelesPanel.add(Box.createVerticalStrut(12));
        nivelesPanel.add(crearNivelItem(
            "Nivel 2", "Laberinto intermedio",
            "Medio", new Color(230, 126, 34)
        ));
        nivelesPanel.add(Box.createVerticalStrut(12));
        nivelesPanel.add(crearNivelItem(
            "Nivel 3", "Caos total",
            "Dificil", new Color(231, 76, 60)
        ));

        add(nivelesPanel, BorderLayout.CENTER);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setOpaque(false);
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton btnVolver = crearBotonNav("Volver");
        btnVolver.addActionListener(e -> gui.showScreen(GameGUI.CHAR_SELECT));
        navPanel.add(btnVolver);

        add(navPanel, BorderLayout.SOUTH);
    }

    private JPanel crearNivelItem(String nivel, String nombre,
                                   String dif, Color color) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(new Color(50, 50, 70));
        item.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lblNivel = new JLabel(nivel + " - " + nombre);
        lblNivel.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblNivel.setForeground(Color.WHITE);

        JButton btnJugar = new JButton("Jugar");
        btnJugar.setFont(new Font("Courier New", Font.BOLD, 11));
        btnJugar.setBackground(color);
        btnJugar.setForeground(Color.WHITE);
        btnJugar.setFocusPainted(false);
        btnJugar.addActionListener(e -> gui.showScreen(GameGUI.GAME));

        JLabel lblDif = new JLabel(dif);
        lblDif.setFont(new Font("Courier New", Font.BOLD, 11));
        lblDif.setForeground(color);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(lblDif);
        rightPanel.add(btnJugar);

        item.add(lblNivel, BorderLayout.WEST);
        item.add(rightPanel, BorderLayout.EAST);

        return item;
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