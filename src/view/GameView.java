package view;

import model.GameModel;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {
    private final MenuPanel menuPanel;
    private final CardLayout cardLayout = new CardLayout(); // Layout that allows switching between panels
    private final JPanel mainPanel = new JPanel(cardLayout); // Main container panel

    private final DifficultyPanel difficultyPanel;
    private final LevelSelectPanel levelPanel;
    private final GamePanel gamePanel;

    // Creates and configures the main game window
    public GameView(GameModel model) {
        setTitle("Brick Breaker"); // Set window title
        setSize(model.getGameWindowWidth(), model.getGameWindowHeight()); // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close app on exit
        setLocationRelativeTo(null); // Center the window on screen
        setResizable(false); // Disable window resizing

        // Initialize menu panel
        menuPanel = new MenuPanel();
        mainPanel.add(menuPanel, "Menu");

        // Initialize difficulty panel
        difficultyPanel = new DifficultyPanel();
        mainPanel.add(difficultyPanel, "Difficulty");

        // Initialize levels selection panel
        levelPanel = new LevelSelectPanel();
        mainPanel.add(levelPanel, "Level");

        // Initialize game panel
        gamePanel = new GamePanel(model);
        mainPanel.add(gamePanel, "Game");

        add(mainPanel); // Add main panel to frame
        setVisible(true); // Display the window

        setMainPanel("Menu"); // Show the menu panel by default
    }

    // Returns the menu panel
    public MenuPanel getMenuPanel() { return menuPanel; }

    // Returns the difficulty panel
    public DifficultyPanel getDifficultyPanel() { return difficultyPanel; }

    // Returns the levels panel
    public LevelSelectPanel getLevelPanel() { return levelPanel; }

    // Returns the main game panel
    public GamePanel getGamePanel() { return gamePanel; }

    // Displays the specified panel
    public void setMainPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
        // If view change to game panel, request focus for keyboard input
        if(panelName.equals("Game")) { gamePanel.requestFocusInWindow(); }
    }
}