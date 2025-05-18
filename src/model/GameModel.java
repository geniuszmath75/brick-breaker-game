package model;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.DisplayMode;
import java.util.List;

public class GameModel {
    /**
     * MODELS
     */
    private Ball ball; // store Ball data
    private List<Brick> bricks; // store Brick data
    private Paddle paddle; // store Paddle data
    private MapGenerator mapGenerator; // store map level data

    /**
     * GAME PARAMETERS
     */
    private boolean isGameRunning = false; // Stores the current game state (running or not)
    private boolean gamePaused = false; // Stores game state (paused or not)
    private int refreshRate; // Calculate sleep time based on refresh rate (in milliseconds)
    private int score = 0; // Player score
    private int lives = 3; // Number of lives
    private int totalBricks; // Total number of bricks

    /**
     * OTHER
     */
    private final int GAME_WINDOW_WIDTH = 800; // Game window width
    private final int GAME_WINDOW_HEIGHT = 800; // Game window height

    // Init all game models
    public GameModel() {
        // Get the screen refresh rate
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        refreshRate = gd.getDisplayMode().getRefreshRate();
        if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) {
            refreshRate = 60; // Default to 60 FPS if refresh rate is unknown
        }

        // Generate map elements(Ball, Paddle, List<Brick>) depends on level and difficulty
        this.mapGenerator = new MapGenerator(1, "EASY", this);
        this.ball = mapGenerator.getBall();
        this.bricks = mapGenerator.getBricks();
        this.paddle = mapGenerator.getPaddle();
        this.totalBricks = mapGenerator.getBricks().size();
    }

    // Returns sleep time
    public int getRefreshRate() { return refreshRate; }

    // Set sleep time
    public void setRefreshRate(int refreshRate) { this.refreshRate = refreshRate; }

    // Checks if the game is currently running
    public boolean isGameRunning() {
        return isGameRunning;
    }

    // Check if the game is currently paused
    public boolean gamePaused() {
        return gamePaused;
    }

    // Change if the game is paused or not
    public void setGamePaused(boolean gamePaused) {
        this.gamePaused = gamePaused;
    }

    // Returns Ball instance
    public Ball getBall() { return ball; }

    // Returns list of Brick instances
    public List<Brick> getBricks() { return bricks; }

    // Return Paddle instance
    public Paddle getPaddle() { return paddle; }

    // Return game window width
    public int getGameWindowWidth() { return GAME_WINDOW_WIDTH; }

    // Return game window height
    public int getGameWindowHeight() { return GAME_WINDOW_HEIGHT; }

    // Starts the game
    public void startGame() {
        isGameRunning = true;
    }

    // Renew the game
    public void renewGame() {
        int prevLives = getLives();

        // Reset game map
        this.mapGenerator = new MapGenerator(1, "EASY", this);
        this.paddle = mapGenerator.getPaddle();
        this.bricks = mapGenerator.getBricks();
        this.ball = mapGenerator.getBall();

        // Reset ball position
        ball.reset();

        // Reset previous number of lives
        setLives(prevLives);

        // TODO: Temporary solution until create completeLevel title
        // Reset total bricks, score and game
        setTotalBricks(bricks.size());
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