package model;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.DisplayMode;

public class GameModel {
    private boolean isGameRunning = false; // Stores the current game state (running or not)
    private final Paddle paddle; // store Paddle data
    private final Ball ball; // store Ball data
    private Brick brick; // store Brick data
    private int refreshRate; // Calculate sleep time based on refresh rate (in milliseconds)
    private int score = 0; // Player score
    private int lives = 3; // Number of lives

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
        ball = new Ball(paddle.getX() + 65, paddle.getY() - 25, 25, this, paddle, brick); // Init ball instance
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

    // Renew the game
    public void renewGame() {
        setLives(3);
        ball.reset();
        brick = new Brick(4, 8, this);
        ball.updateBrickReference(brick);
        startGame();
    }

    // Stops the game
    public void stopGame() { isGameRunning = false; }

    // Get number of lives
    public int getLives() { return lives; }

    // Set number of lives
    public void setLives(int lives) { this.lives = lives; }

    // Get player score
    public int getScore() { return score; }

    // Set player score
    public void setScore(int score) { this.score = score; }

    // Increase player score
    public void increaseScore() { score += 5; }
}