package model;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.DisplayMode;

public class GameModel {
    // Stores the current game state (running or not)
    private boolean isGameRunning = false;
    private final Paddle paddle; // store Paddle data
    private final Ball ball; // store Ball data
    private final Brick brick; // store Brick data
    private int refreshRate; // Calculate sleep time based on refresh rate (in milliseconds)

    // Init all game models
    public GameModel() {
        // Get the screen refresh rate
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        refreshRate = gd.getDisplayMode().getRefreshRate();
        if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) {
            refreshRate = 60; // Default to 60 FPS if refresh rate is unknown
        }

        paddle = new Paddle(320, 715, 140, 15, 20); // Init paddle instance
        brick = new Brick(4, 8, this); // Init brick instance
        ball = new Ball(paddle.getX() + 65, paddle.getY() - 25, paddle, brick,this); // Init ball instance
    }

    // Returns sleep tim
    public int getRefreshRate() { return refreshRate; }

    // Checks if the game is currently running
    public boolean isGameRunning() {
        return isGameRunning;
    }

    // Returns Paddle instance
    public Paddle getPaddle() {
        return paddle;
    }

    // Returns Ball instance
    public Ball getBall() { return ball; }

    // Returns Brick instance
    public Brick getBrick() { return brick; }

    // Starts the game
    public void startGame() {
        isGameRunning = true;
    }

    // Stops the game
    public void stopGame() { isGameRunning = false; }
}