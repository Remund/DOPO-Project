package presentation;

import javax.swing.*;
import java.awt.*;

public class GameGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public static final String MENU         = "MENU";
    public static final String MODE_SELECT  = "MODE_SELECT";
    public static final String CHAR_SELECT  = "CHAR_SELECT";
    public static final String LEVEL_SELECT = "LEVEL_SELECT";
    public static final String GAME         = "GAME";
    public static final String PAUSE        = "PAUSE";
    public static final String RESULT       = "RESULT";

    public GameGUI() {
        setTitle("The DOPO Hardest Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);

        MenuScreen        menuScreen  = new MenuScreen(this);
        ModeScreen        modeScreen  = new ModeScreen(this);
        CharSelectScreen  charScreen  = new CharSelectScreen(this);
        LevelSelectScreen levelScreen = new LevelSelectScreen(this);
        GamePanel         gamePanel   = new GamePanel(this);
        PauseScreen       pauseScreen = new PauseScreen(this);
        ResultScreen      resultScreen = new ResultScreen(this);

        mainPanel.add(menuScreen,   MENU);
        mainPanel.add(modeScreen,   MODE_SELECT);
        mainPanel.add(charScreen,   CHAR_SELECT);
        mainPanel.add(levelScreen,  LEVEL_SELECT);
        mainPanel.add(gamePanel,    GAME);
        mainPanel.add(pauseScreen,  PAUSE);
        mainPanel.add(resultScreen, RESULT);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        showScreen(MENU);
    }

    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
        SwingUtilities.invokeLater(() -> {
            for (Component c : mainPanel.getComponents()) {
                if (c.isVisible()) {
                    c.requestFocusInWindow();
                    break;
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameGUI());
    }
    
    public void reiniciarNivel() {
        GamePanel panelAntiguo = getGamePanel();
        if (panelAntiguo != null) {
            mainPanel.remove(panelAntiguo);
        }
        GamePanel nuevoPanel = new GamePanel(this);
        mainPanel.add(nuevoPanel, GAME);
        mainPanel.revalidate();
    }

    private GamePanel getGamePanel() {
        for (Component c : mainPanel.getComponents()) {
            if (c instanceof GamePanel) {
                return (GamePanel) c;
            }
        }
        return null;
    }
}