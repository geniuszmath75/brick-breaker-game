package controller;

import model.GameModel;
import view.GameView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController {
    private final GameModel model;  // Stores game state
    private final GameView view;    // Manages game UI
    private boolean gamePaused = false; // Stores game state (paused or not)

    // Initializes the game controller
    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        int refreshRate = model.getRefreshRate();

        // TODO: try to find a better way to refresh game animations - without warnings
        // create a new thread to refresh move of the ball smoothly
        new Thread(() -> {
            while (true) {
                if (!gamePaused) { model.getBall().move(); } // Move the ball if the game is not paused
                model.getBall().checkCollision();
                view.repaint();
                try {
                    Thread.sleep(1000 / refreshRate); // sleep based on refresh rate (in milliseconds)
                } catch (InterruptedException ignored) {}
            }
        }).start();

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
        public void actionPerformed(ActionEvent e) {
            view.setMainPanel("Difficulty"); // Switch view to difficulty panel
        }
    }

    // Handles the EXIT button click
    static class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    // Handles the EASY button click
    class EasyButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            view.setMainPanel("Level"); // Switch view to levels panel
        }
    }

    // Handles the MEDIUM button click
    static class MediumButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("MEDIUM mode selected");
        }
    }

    // Handles the HARD button click
    static class HardButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("HARD mode selected");
        }
    }

    // Handles the game level selection button click
    class LevelButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            view.setMainPanel("Game");
            model.startGame();
        }
    }

    // Handles the BACK button click with a target panel name
    class BackButtonListener implements ActionListener {
        private final String panelName;

        // BACK button listener constructor
        public BackButtonListener(String panelName) {
            this.panelName = panelName;
        }

        public void actionPerformed(ActionEvent e) {
            view.setMainPanel(panelName); // Returns to menu panel
        }
    }

    // Handles keyboard events
    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // If Esc - pause the game
                gamePaused = true; // stop the game loop flag
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
                gamePaused = false; // Continue the game animation refresh
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
}