import javax.swing.*;

public class GameView extends JFrame {
    private final MenuPanel menuPanel;

    // Creates and configures the main game window
    public GameView() {
        setTitle("Brick Breaker"); // Set window title
        setSize(800, 600); // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close app on exit
        setLocationRelativeTo(null); // Center the window on screen

        // Initialize menu panel
        menuPanel = new MenuPanel();
        add(menuPanel);

        // Display the window
        setVisible(true);
    }

    // Returns the menu panel
    public MenuPanel getMenuPanel() {
        return menuPanel;
    }
}
