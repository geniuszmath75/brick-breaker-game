import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController {
    private final GameModel model;  // Stores game state
    private final GameView view;    // Manages game UI

    // Initializes the game controller
    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;

        // Add button listeners from the menu panel
        view.getMenuPanel().addStartListener(new StartListener());
        view.getMenuPanel().addExitListener(new ExitListener());
    }

    // Handles the START button click
    class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            model.startGame();
            System.out.println("Game started!"); // Placeholder, later will switch view
        }
    }

    // Handles the EXIT button click
    static class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
