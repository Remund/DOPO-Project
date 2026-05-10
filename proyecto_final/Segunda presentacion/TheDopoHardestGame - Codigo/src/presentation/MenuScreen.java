package presentation;

import javax.swing.*;
import java.awt.*;

public class MenuScreen extends JPanel {

    private GameGUI gui;

    public MenuScreen(GameGUI gui) {
        this.gui = gui;
        setPreferredSize(new Dimension(700, 500));
        setBackground(new Color(30, 30, 50));
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("THE DOPO HARDEST GAME", SwingConstants.CENTER);
        titulo.setFont(new Font("Courier New", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(80, 20, 40, 20));
        add(titulo, BorderLayout.NORTH);

        JPanel botonesPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        botonesPanel.setOpaque(false);
        botonesPanel.setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 200));

        JButton btnJugar  = crearBoton("Jugar");
        JButton btnLeader = crearBoton("Leaderboard");
        JButton btnAbrir  = crearBoton("Abrir partida");

        btnJugar.addActionListener(e -> gui.showScreen(GameGUI.MODE_SELECT));
        btnLeader.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "Leaderboard proximamente"));
        btnAbrir.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "Abrir partida proximamente"));

        botonesPanel.add(btnJugar);
        botonesPanel.add(btnLeader);
        botonesPanel.add(btnAbrir);

        add(botonesPanel, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Courier New", Font.BOLD, 16));
        btn.setBackground(new Color(50, 50, 70));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}