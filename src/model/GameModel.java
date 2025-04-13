package model;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.DisplayMode;

public class GameModel {
    private boolean isGameRunning = false; // Stores the current game state (running or not)
    private Paddle paddle; // store Paddle data
    private final Ball ball; // store Ball data
    private Brick brick; // store Brick data
    private int refreshRate; // Calculate sleep time based on refresh rate (in milliseconds)
    private int score = 0; // Player score
    private int lives = 3; // Number of lives
    // TODO: Temporary value, must be move to the MapGenerator class
    private int totalBricks = 1; // Total number of bricks

    // Init all game models
    public GameModel() {
        // Get the screen refresh rate
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        refreshRate = gd.getDisplayMode().getRefreshRate();
        if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) {
            refreshRate = 60; // Default to 60 FPS if refresh rate is unknown
        }

        paddle = new Paddle(320, 715, 140, 15, 5); // Init paddle instance
        brick = new Brick(90, 100, 200, 70, 1,this); // Init brick instance
        ball = new Ball(paddle.getX() + 65, paddle.getY() - 25, 25, this, paddle, brick); // Init ball instance
    }

    // Returns sleep time
    public int getRefreshRate() { return refreshRate; }

    // Set sleep time
    public void setRefreshRate(int refreshRate) { this.refreshRate = refreshRate; }

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
        int prevLives = getLives();

        // Reset ball position
        ball.reset();

        // Reset brick position
        brick = new Brick(90, 100, 200, 70, 1, this);
        ball.updateBrickReference(brick);

        // Reset paddle position
        paddle = new Paddle(320, 715, 140, 15, 5);
        ball.updatePaddleReference(paddle);

        // Reset previous number of lives
        setLives(prevLives);

        // TODO: Temporary solution until create completeLevel title
        // Reset total bricks, score and game
        setTotalBricks(1);
        setScore(0);
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

    // Set total number of bricks
    public void setTotalBricks(int totalBricks) { this.totalBricks = totalBricks; }

    // Check if level is completed (all bricks are destroyed)
    public void checkLevelComplete() {
        // Decrease number of destroyed bricks
        totalBricks--;

        // Check if all bricks have destroyed
        if(totalBricks == 0) {
            System.out.println("Level Complete");
            renewGame();
        }
    }
}