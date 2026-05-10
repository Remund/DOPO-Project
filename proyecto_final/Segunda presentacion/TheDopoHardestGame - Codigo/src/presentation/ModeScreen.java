package presentation;

import javax.swing.*;
import java.awt.*;

public class ModeScreen extends JPanel {

    private GameGUI gui;

    public ModeScreen(GameGUI gui) {
        this.gui = gui;
        setPreferredSize(new Dimension(700, 500));
        setBackground(new Color(30, 30, 50));
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("SELECCIONA EL MODO", SwingConstants.CENTER);
        titulo.setFont(new Font("Courier New", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel modosPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        modosPanel.setOpaque(false);
        modosPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        modosPanel.add(crearModoCard(
            "PLAYER",
            "1 jugador controla el cuadrado",
            new Color(39, 174, 96)
        ));
        modosPanel.add(crearModoCard(
            "PLAYER vs PLAYER",
            "2 jugadores en sentidos opuestos",
            new Color(41, 128, 185)
        ));
        modosPanel.add(crearModoCard(
            "PLAYER vs MACHINE",
            "Contra IA Aleatoria o Experta",
            new Color(142, 68, 173)
        ));

        add(modosPanel, BorderLayout.CENTER);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navPanel.setOpaque(false);
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton btnVolver = crearBotonNav("Volver");
        btnVolver.addActionListener(e -> gui.showScreen(GameGUI.MENU));
        navPanel.add(btnVolver);

        add(navPanel, BorderLayout.SOUTH);
    }

    private JPanel crearModoCard(String nombre, String desc, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(50, 50, 70));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 3),
            BorderFactory.createEmptyBorder(20, 10, 20, 10)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblNombre = new JLabel(
            "<html><center>" + nombre + "</center></html>",
            SwingConstants.CENTER
        );
        lblNombre.setFont(new Font("Courier New", Font.BOLD, 13));
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel(
            "<html><center>" + desc + "</center></html>",
            SwingConstants.CENTER
        );
        lblDesc.setFont(new Font("Courier New", Font.PLAIN, 11));
        lblDesc.setForeground(new Color(180, 180, 180));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnSelect = new JButton("Seleccionar");
        btnSelect.setFont(new Font("Courier New", Font.BOLD, 11));
        btnSelect.setBackground(color);
        btnSelect.setForeground(Color.WHITE);
        btnSelect.setFocusPainted(false);
        btnSelect.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSelect.addActionListener(e -> gui.showScreen(GameGUI.CHAR_SELECT));

        card.add(Box.createVerticalGlue());
        card.add(lblNombre);
        card.add(Box.createVerticalStrut(8));
        card.add(lblDesc);
        card.add(Box.createVerticalStrut(15));
        card.add(btnSelect);
        card.add(Box.createVerticalGlue());

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