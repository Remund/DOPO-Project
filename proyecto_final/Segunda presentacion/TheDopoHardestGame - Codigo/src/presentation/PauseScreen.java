package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PauseScreen extends JPanel {

    private GameGUI gui;

    public PauseScreen(GameGUI gui) {
        this.gui = gui;
        setPreferredSize(new Dimension(700, 500));
        setBackground(new Color(20, 20, 40));
        setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(40, 40, 60));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 3),
            BorderFactory.createEmptyBorder(30, 50, 30, 50)
        ));

        JLabel titulo = new JLabel("PAUSA", SwingConstants.CENTER);
        titulo.setFont(new Font("Courier New", Font.BOLD, 30));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titulo);
        card.add(Box.createVerticalStrut(25));

        card.add(crearBotonPausa("Continuar",
            new Color(39, 174, 96),
            e -> gui.showScreen(GameGUI.GAME)
        ));
        card.add(Box.createVerticalStrut(10));
        card.add(crearBotonPausa("Guardar partida",
            new Color(41, 128, 185),
            e -> JOptionPane.showMessageDialog(this, "Partida guardada")
        ));
        card.add(Box.createVerticalStrut(10));
        card.add(crearBotonPausa("Reiniciar nivel",
            new Color(230, 126, 34),
            e -> gui.showScreen(GameGUI.GAME)
        ));
        card.add(Box.createVerticalStrut(10));
        card.add(crearBotonPausa("Menu principal",
            new Color(231, 76, 60),
            e -> gui.showScreen(GameGUI.MENU)
        ));

        add(card);
    }

    private JButton crearBotonPausa(String texto, Color color,
                                     ActionListener listener) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Courier New", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 40));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.addActionListener(listener);
        return btn;
    }
}