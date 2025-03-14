package view;

import model.GameModel;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {
    private final MenuPanel menuPanel;
    private final CardLayout cardLayout = new CardLayout(); // Layout that allow to switch game views
    private final JPanel mainPanel = new JPanel(cardLayout); // Main (currently visible) panel
    private final DifficultyPanel difficultyPanel;
    private final LevelSelectPanel levelPanel;
    private final GamePanel gamePanel;

    // Creates and configures the main game window
    public GameView(GameModel model) {
        setTitle("Brick Breaker"); // Set window title
        setSize(800, 600); // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close app on exit
        setLocationRelativeTo(null); // Center the window on screen

        // Initialize menu panel
        menuPanel = new MenuPanel();
        mainPanel.add(menuPanel, "Menu");

        // Initialize difficulty panel
        difficultyPanel = new DifficultyPanel();
        mainPanel.add(difficultyPanel, "Difficulty");

        // Initialize levels panel
        levelPanel = new LevelSelectPanel();
        mainPanel.add(levelPanel, "Level");

        // Initialize game panel
        gamePanel = new GamePanel(model);
        mainPanel.add(gamePanel, "Game");

        // Add mainPanel to JFrame
        add(mainPanel);

        // Display the window
        setVisible(true);

        // Display the menu panel
        setMainPanel("Menu");
    }

    // Returns the menu panel
    public MenuPanel getMenuPanel() {
        return menuPanel;
    }

    // Returns the difficulty panel
    public DifficultyPanel getDifficultyPanel() { return difficultyPanel; }

    // Returns the levels panel
    public LevelSelectPanel getLevelPanel() { return levelPanel; }

    // Returns the main game panel
    public GamePanel getGamePanel() { return gamePanel; }

    // Set the main panel
    public void setMainPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
        // If view change to game panel, then set focus on this panel (to immediately handle keyboard events)
        if(panelName.equals("Game")) {
            gamePanel.requestFocusInWindow();
        }
    }

}
