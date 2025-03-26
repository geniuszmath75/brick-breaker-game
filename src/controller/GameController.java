package controller;

import model.GameModel;
import view.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController {
    private final GameModel model;  // Stores game state
    private final GameView view;    // Manages game UI

    // Initializes the game controller
    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        int refreshRate = model.getRefreshRate();

        // TODO: try to find a better way to refresh the game loop - without warnings
        // create a new thread to refresh move of the ball smoothly
        new Thread(() -> {
            while (true) {
                model.getBall().move();
                model.getBall().checkCollision();
                view.repaint();
                try {
                    Thread.sleep(1000 / refreshRate); // sleep based on refresh rate (in milliseconds)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
            model.getPaddle().keyPressed(e); // Pass event to Paddle model
            model.getBall().keyPressed(e); // Pass event to Ball model
        }
    }
}