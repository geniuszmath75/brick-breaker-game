package controller;

import model.GameModel;
import utils.SoundLoader;
import view.GameView;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameController {
    private final GameModel model;  // Stores game state
    private final GameView view;    // Manages game UI
    private ScheduledExecutorService executor;
    private Clip backgroundClip;
    private boolean executorStarted = false;

    // Initializes the game controller
    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;

        // Add button listeners from the menu panel
        view.getMenuPanel().addStartListener(new StartListener());
        view.getMenuPanel().addStatsListener(new StatsListener());
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

    // Check if executor is running
    private boolean isExecutorRunning() {
        return executorStarted && executor != null && !executor.isShutdown();
    }

    // Starting game logic and executor
    // No busy-waiting warnings but remaining performance and smoothing while models moving
    private void startExecutor() {
        if (!isExecutorRunning()) {
            executor = Executors.newSingleThreadScheduledExecutor();
            executorStarted = true;

            int refreshRate = model.getRefreshRate();

            executor.scheduleAtFixedRate(() -> {
                try {
                    if (!model.gamePaused() && model.isGameRunning()) {
                        model.getPaddle().move(); // Move the paddle if the game is not paused
                        model.getBall().move(); // Move the ball if the game is not paused
                        model.getBall().checkCollision(); // Check ball collision if game is not paused

                        if (model.isLevelCompleted()) {
                            model.setGamePaused(true);
                            handleLevelCompletion();
                        }

                        if (!model.isGameRunning()) {
                            SwingUtilities.invokeLater(this::showGameOverDialog);
                        }
                    }
                    view.repaint();
                } catch (Exception e) {
                    System.err.println("Error: " + e);
                }
            }, 0, 1000L / refreshRate, TimeUnit.MILLISECONDS);
        }
    }

    // Handles the START button click
    class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) { view.setMainPanel("Difficulty"); } // Switch view to difficulty panel
    }

    // Handles the STATS button click
    class StatsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get statistics from the model
            int maxScore = model.getMaxScore();
            String text = getString(maxScore);

            // Display in a simple JOptionPane:
            JOptionPane.showMessageDialog(view.getMenuPanel(), text, "Statistics", JOptionPane.INFORMATION_MESSAGE);
            SoundLoader.playWAV("/sounds/crash.wav");
        }

        private String getString(int maxScore) {
            Map<String,Integer> unlocked = model.getUnlockedLevels();

            // Build the text to display
            return " "
                    + "=== Game Stats ===\n\n"
                    + "Max Score: " + maxScore + "\n\n"
                    + "Unlocked Levels:\n"
                    + "  EASY:   Level " + unlocked.getOrDefault("EASY", 1) + "\n"
                    + "  MEDIUM: Level " + unlocked.getOrDefault("MEDIUM", 1) + "\n"
                    + "  HARD:   Level " + unlocked.getOrDefault("HARD", 1) + "\n";
        }
    }

    // Handles the EXIT button click
    class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(isExecutorRunning()) {
                executor.shutdown();
            }
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
                    startLevel(chosenLevel);
                } else {
                    showLockedLevelDialog(chosenLevel);
                }

            } catch (NumberFormatException ex) {
                startLevel(1); // fallback if parsing fails
            }
        }

        private void startLevel(int level) {
            model.setLEVEL(level);
            model.setGamePaused(false);
            model.startGame();
            startExecutor();
            if (backgroundClip == null) { backgroundClip = SoundLoader.loadLoopClip("/sounds/background.wav"); }
            view.setMainPanel("Game");
        }

        private void showLockedLevelDialog(int chosenLevel) {
            JOptionPane.showMessageDialog(null,
                    "You have to pass level " + (chosenLevel - 1) + " first :)",
                    "Level still locked",
                    JOptionPane.WARNING_MESSAGE);
            SoundLoader.playWAV("/sounds/crash.wav");
        }
    }

    // Handles the BACK button click with a target panel name
    class BackButtonListener implements ActionListener {
        private final String panelName;

        // BACK button listener constructor
        public BackButtonListener(String panelName) { this.panelName = panelName; }

        public void actionPerformed(ActionEvent e) {
            SoundLoader.playWAV("/sounds/crash.wav");
            view.setMainPanel(panelName);
        } // Returns to the selected panel
     }

    // Handles keyboard input during the game
    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_ESCAPE) {
                // If Esc - pause the game
                handlePause();
            } else if (key == KeyEvent.VK_SPACE) {
                model.getBall().keyPressed(e); // Pass space to Ball
            } else {
                model.getPaddle().setMoving(key, true); // Pass arrow keys to Paddle
            }
        }

        @Override
        public void keyReleased(KeyEvent e) { model.getPaddle().setMoving(e.getKeyCode(), false); } // Pass the event to the Paddle model

        private void handlePause() {
            SoundLoader.pauseClip(backgroundClip);
            model.setGamePaused(true); // Stop the game loop flag
            model.getPaddle().stopMoving(); // Stop the paddle

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

            SoundLoader.playWAV("/sounds/crash.wav");

            // If user chooses to go back to the menu
            if (option == JOptionPane.NO_OPTION) {
                stopBackgroundMusic();
                if (isExecutorRunning()) {
                    executor.shutdownNow();
                    executorStarted = false;
                }
                view.setMainPanel("Menu");
                model.setLives(3);
            } else {
                // User selected "CONTINUE" → resume music exactly from where we stopped it
                model.setGamePaused(false);
                resumeBackgroundMusic();
            }
        }
    }

    // Handles the level completion logic
    private void handleLevelCompletion() {
        SwingUtilities.invokeLater(() -> {
            int currentLevel = model.getLEVEL();

            // If level 3, activate endless mode and reset only the bricks
            if (currentLevel == 3) {
                if (!model.isEndlessModeActivated()) {
                    SoundLoader.pauseClip(backgroundClip);
                    JOptionPane.showMessageDialog(
                            view.getGamePanel(),
                            "Level 3 completed! Now you are in endless mode.\n Watch your lives and max out score :)",
                            "Endless Mode",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    SoundLoader.playWAV("/sounds/crash.wav");
                    resumeBackgroundMusic();
                    model.setEndlessModeActivated(true);
                }
                model.resetBricksOnly();
                model.setGamePaused(false);
            } else {
                // Level 1 or 2 completed – prompt user for next action
                SoundLoader.pauseClip(backgroundClip);
                int choice = JOptionPane.showOptionDialog(
                        view.getGamePanel(),
                        "What do you want to do?",
                        "Level Completed",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Replay level", "Next level", "Menu"},
                        "Next level"
                );

                SoundLoader.playWAV("/sounds/crash.wav");

                switch (choice) {
                    case 0 -> restartLevel(currentLevel);       // Replay current level
                    case 1 -> restartLevel(currentLevel + 1);   // Go to next level
                    default -> goToMenu();                      // Return to menu
                }
            }
        });
    }

    // Displays the Game Over dialog and handles user decision
    private void showGameOverDialog() {
        // Pause background music (isGameRunning = false)
        SoundLoader.pauseClip(backgroundClip);

        // Set a flag so that the dialogue does not repeat itself multiple times in one round
        model.setGamePaused(true);

        int option = JOptionPane.showOptionDialog(
                view.getGamePanel(),
                "Do you want to restart the game or go back to the menu?",
                "GAME OVER",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"RESTART", "MENU"},
                "RESTART"
        );
        SoundLoader.playWAV("/sounds/crash.wav");

        if (option == JOptionPane.YES_OPTION) {
            // Restart the same level
            restartLevel(model.getLEVEL());
        } else {
            goToMenu();
        }
    }

    private void restartLevel(int level) {
        model.setLEVEL(level);
        model.renewGame();
        model.setGamePaused(false);
        resumeBackgroundMusic();
        view.setMainPanel("Game");
    }

    private void goToMenu() {
        stopBackgroundMusic();
        if (isExecutorRunning()) {
            executor.shutdownNow();
            executorStarted = false;
        }
        model.setLevelCompleted(false);
        model.setLives(3);
        view.setMainPanel("Menu");
    }

    private void resumeBackgroundMusic() {
        if(backgroundClip != null) {
            SoundLoader.resumeClip(backgroundClip);
        }
    }

    private void stopBackgroundMusic() {
        if(backgroundClip != null) {
            SoundLoader.stopClip(backgroundClip);
            backgroundClip = null;
        }
    }
}