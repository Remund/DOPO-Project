package presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ResultScreen extends JPanel {

    private GameGUI gui;

    public ResultScreen(GameGUI gui) {
        this.gui = gui;
        setPreferredSize(new Dimension(700, 500));
        setBackground(new Color(30, 30, 50));
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Nivel Completado!", SwingConstants.CENTER);
        titulo.setFont(new Font("Courier New", Font.BOLD, 22));
        titulo.setForeground(new Color(46, 204, 113));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        JPanel stats = new JPanel(new GridLayout(3, 1, 0, 4));
        stats.setBackground(new Color(50, 50, 70));
        stats.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 130), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        stats.add(crearStatLabel("Muertes totales:      5"));
        stats.add(crearStatLabel("Monedas recolectadas: 10 / 10"));
        stats.add(crearStatLabel("Tiempo restante:      00:42"));
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        centro.add(stats);
        centro.add(Box.createVerticalStrut(15));

        JLabel lblLead = new JLabel("LEADERBOARD");
        lblLead.setFont(new Font("Courier New", Font.BOLD, 13));
        lblLead.setForeground(Color.WHITE);
        lblLead.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblLead);
        centro.add(Box.createVerticalStrut(5));

        String[] cols = {"#", "Nombre", "Muertes", "Tiempo"};
        Object[][] data = {
            {"1", "PlayerOne",   "2", "01:10"},
            {"2", "PlayerTwo",   "4", "01:05"},
            {"3", "PlayerThree", "7", "00:58"}
        };
        JTable tabla = new JTable(new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        tabla.setFont(new Font("Courier New", Font.PLAIN, 12));
        tabla.setRowHeight(22);
        tabla.setBackground(new Color(50, 50, 70));
        tabla.setForeground(Color.WHITE);
        tabla.getTableHeader().setBackground(new Color(40, 40, 60));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Courier New", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(scroll);
        centro.add(Box.createVerticalStrut(10));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        inputPanel.setOpaque(false);
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel lblNombre = new JLabel("Tu nombre:");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("Courier New", Font.PLAIN, 12));

        JTextField txtNombre = new JTextField(12);
        txtNombre.setFont(new Font("Courier New", Font.PLAIN, 12));

        JButton btnReg = new JButton("Registrar");
        btnReg.setFont(new Font("Courier New", Font.BOLD, 11));
        btnReg.setBackground(new Color(41, 128, 185));
        btnReg.setForeground(Color.WHITE);
        btnReg.setFocusPainted(false);

        inputPanel.add(lblNombre);
        inputPanel.add(txtNombre);
        inputPanel.add(btnReg);
        centro.add(inputPanel);

        add(centro, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botones.setOpaque(false);

        JButton btnSiguiente = crearBotonResult("Siguiente nivel", new Color(39, 174, 96));
        btnSiguiente.addActionListener(e -> gui.showScreen(GameGUI.LEVEL_SELECT));
        botones.add(btnSiguiente);

        JButton btnReintentar = crearBotonResult("Reintentar", new Color(230, 126, 34));
        btnReintentar.addActionListener(e -> {
            gui.reiniciarNivel();
            gui.showScreen(GameGUI.GAME);
        });
        botones.add(btnReintentar);

        JButton btnMenu = crearBotonResult("Menu principal", new Color(231, 76, 60));
        btnMenu.addActionListener(e -> gui.showScreen(GameGUI.MENU));
        botones.add(btnMenu);

        add(botones, BorderLayout.SOUTH);
    }

    private JLabel crearStatLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Courier New", Font.PLAIN, 13));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private JButton crearBotonResult(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Courier New", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return btn;
    }
}