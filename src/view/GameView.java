package view;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {
    private final MenuPanel menuPanel;
    private final CardLayout cardLayout = new CardLayout(); // Layout that allow to switch game views
    private final JPanel mainPanel = new JPanel(cardLayout); // Main (currently visible) panel
    private final DifficultyPanel difficultyPanel;
    private final LevelSelectPanel levelPanel;

    // Creates and configures the main game window
    public GameView() {
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

    // Set the main panel
    public void setMainPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

}
