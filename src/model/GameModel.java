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

    /**
     * GAME PARAMETERS
     */
    private boolean isGameRunning = false; // Stores the current game state (running or not)
    private boolean gamePaused = false; // Stores game state (paused or not)
    private int refreshRate; // Calculate sleep time based on refresh rate (in milliseconds)
    private int score = 0; // Player score
    private int lives = 3; // Number of lives
    private int totalBricks; // Total number of bricks
    private int LEVEL = 1; // Selected game level
    private String DIFFICULTY = "EASY"; // Selected game difficulty

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
    }

    // Returns sleep time
    public int getRefreshRate() { return refreshRate; }

    // Set sleep time
    public void setRefreshRate(int refreshRate) { this.refreshRate = refreshRate; }

    // Set speed based on screen refresh rate
    public int setSpeed(int baseSpeed) {
        int refreshRate = getRefreshRate();
        if (refreshRate <= 0) { // Default to 60 FPS if refresh rate is unknown
            setRefreshRate(60);
        }
        // Calculate speed based on refresh rate
        // 240Hz ~ 1 pixel per frame; 165Hz ~ 2; 144Hz ~ 3; 120Hz ~ 3; 60Hz ~ 7;
        return baseSpeed + Math.max(1, (int) (3.0 * 144 / refreshRate));
    }

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

    // Set LEVEL value
    public void setLEVEL(int level) {
        this.LEVEL = level;
    }

    // Set DIFFICULTY value
    public void setDIFFICULTY(String difficulty) {
        this.DIFFICULTY = difficulty;
    }

    // Starts the game
    public void startGame() {
        isGameRunning = true;

        // Generate map elements(Ball, Paddle, List<Brick>) depends on level and difficulty
        MapGenerator mapGenerator = new MapGenerator(LEVEL, DIFFICULTY, this);
        this.paddle = mapGenerator.getPaddle();
        this.bricks = mapGenerator.getBricks();
        this.ball = mapGenerator.getBall();
        this.totalBricks = mapGenerator.getBricks().size();
    }

    // Renew the game
    public void renewGame() {
        int prevLives = getLives();

        // Reset ball position
        ball.reset();

        // Reset previous number of lives
        setLives(prevLives);

        // TODO: Temporary solution until create completeLevel title
        // Reset score and game
        setScore(0);
        // Reset game state
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