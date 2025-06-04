package controller;

import model.GameModel;
import view.GameView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameController {
    private final GameModel model;  // Stores game state
    private final GameView view;    // Manages game UI
    private final ScheduledExecutorService executor;

    // Initializes the game controller
    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        int refreshRate = model.getRefreshRate();

        // Using ScheduledExecutor class to replace controlling models movement with Thread
        // No busy-waiting warnings but remaining performance and smoothing while models moving
        // Added also Paddle movement to simplify handling model movement
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            if (!model.gamePaused() && model.isGameRunning()) {
                model.getPaddle().move(); // Move the paddle if the game is not paused
                model.getBall().move(); // Move the ball if the game is not paused
                model.getBall().checkCollision(); // Check ball collision if game is not paused

                if (model.isLevelCompleted()) {
                    model.setGamePaused(true);
                    handleLevelCompletion();
                }
            }
            view.repaint();
        }, 0, 1000L / refreshRate, TimeUnit.MILLISECONDS);

        // Add button listeners from the menu panel
        view.getMenuPanel().addStartListener(new StartListener());
        view.getMenuPanel().addExitListener(new ExitListener());

        // Add button listeners from the difficulty panel
        view.getDifficultyPanel().addEasyListener(new EasyButtonListener());
        view.getDifficultyPanel().addMediumListener(new MediumButtonListener());
        view.getDifficultyPanel().addHardListener(new HardButtonListener());
        view.getDifficultyPanel().addBackListener(new BackButtonListener("Menu"));

        // Add button listeners from the level panel
        view.getLevelPanel().addLevelListener(new LevelButtonListener());
        view.getLevelPanel().addBackListener(new BackButtonListener("Difficulty"));

        // Add key listeners from the main game panel
        view.getGamePanel().addKeyListener(new KeyHandler());
        view.getGamePanel().setFocusable(true);
    }

    // Handles the START button click
    class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) { view.setMainPanel("Difficulty"); } // Switch view to difficulty panel
    }

    // Handles the EXIT button click
    class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            executor.shutdown();
            System.exit(0);
        }
    }

    // Handles the EASY button click
    class EasyButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            model.setDIFFICULTY("EASY"); // Select EASY mode
            view.setMainPanel("Level"); // Switch view to levels panel
        }
    }

    // Handles the MEDIUM button click
    class MediumButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            model.setDIFFICULTY("MEDIUM"); // Select MEDIUM mode
            view.setMainPanel("Level"); // Switch view to levels panel
        }
    }

    // Handles the HARD button click
    class HardButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            model.setDIFFICULTY("HARD"); // Select HARD mode
            view.setMainPanel("Level"); // Switch view to levels panel
        }
    }

    // Handles the game level selection button click
    class LevelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand(); // Get the button command (level number)
            try {
                int chosenLevel = Integer.parseInt(cmd);

                if (model.canSelectLevel(chosenLevel)) {
                    model.setLEVEL(chosenLevel);
                    model.startGame();
                    view.setMainPanel("Game");
                } else {
                    JOptionPane.showMessageDialog(null,
                            "You have to pass level " + (chosenLevel - 1) + " first :)",
                            "Level still locked",
                            JOptionPane.WARNING_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                // fallback — if something goes wrong, start from level 1
                model.setLEVEL(1);
                model.startGame();
                view.setMainPanel("Game");
            }
        }
    }

    // Handles the BACK button click with a target panel name
    class BackButtonListener implements ActionListener {
        private final String panelName;

        // BACK button listener constructor
        public BackButtonListener(String panelName) { this.panelName = panelName; }

        public void actionPerformed(ActionEvent e) { view.setMainPanel(panelName); } // Returns to menu panel
     }

    // Handles keyboard events
    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // If Esc - pause the game
                model.setGamePaused(true); // stop the game loop flag
                model.getPaddle().stopMoving(); // stop the paddle

                // Show the pause dialog
                int option = JOptionPane.showOptionDialog(
                        view.getGamePanel(),
                        "Do you want to continue or go back to the menu?",
                        "PAUSE",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        new String[]{"CONTINUE", "MENU"},
                        "CONTINUE"
                );

                // If user chooses to go back to the menu
                if (option == JOptionPane.NO_OPTION) {
                    view.setMainPanel("Menu");
                    model.renewGame();
                    model.setScore(0);
                    model.setLives(3);
                }
                model.setGamePaused(false); // Continue the game animation refresh
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) { // If Space - pass the event to the Ball model
                model.getBall().keyPressed(e);
            } else { // Else pass the event to the Paddle model
                model.getPaddle().setMoving(e.getKeyCode(), true);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            model.getPaddle().setMoving(e.getKeyCode(), false); // Pass the event to the Paddle model
        }
    }

    // Handles the level completion logic
    private void handleLevelCompletion() {
        SwingUtilities.invokeLater(() -> {
            int currentLevel = model.getLEVEL();
            // If level 3, endless mode: we only reset the blocks and continue
            if (currentLevel == 3) {
                JOptionPane.showMessageDialog(
                        view.getGamePanel(),
                        "Level 3 completed! Now you are in endless mode.\n Watch out for your lives and max out score :)",
                        "Endless Mode",
                        JOptionPane.INFORMATION_MESSAGE
                );
                model.resetBricksOnly();
                model.setGamePaused(false);
            }
            // Level 1 or 2 – we ask what's next
            else {
                int choice = JOptionPane.showOptionDialog(
                        view.getGamePanel(),
                        "Level Completed! \n What do you want to do?",
                        "Level Completed",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Replay level", "Next level", "Menu"},
                        "Next level"
                );

                switch (choice) {
                    case 0 -> { // Replay Level
                        model.setLevelCompleted(false);
                        model.renewGame();
                        model.startGame();
                        model.setGamePaused(false);
                        view.setMainPanel("Game");
                    }
                    case 1 -> { // Go to the next level
                        model.setLevelCompleted(false);
                        model.setLEVEL(currentLevel + 1);
                        model.renewGame();
                        model.startGame();
                        model.setGamePaused(false);
                        view.setMainPanel("Game");
                    }
                    default -> { // Back to menu
                        model.setLevelCompleted(false);
                        model.renewGame();
                        model.setScore(0);
                        model.setLives(3);
                        view.setMainPanel("Menu");
                    }
                }
            }
        });
    }
}