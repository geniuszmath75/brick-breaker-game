package model;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.DisplayMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private boolean levelCompleted = false; // Is current level is completed
    private final Map<String, Integer> unlockedLevels = new HashMap<>(); // Stores unlocked levels
    private int refreshRate; // Calculate sleep time based on refresh rate (in milliseconds)
    private int score = 0; // Player score
    private int lives = 3; // Number of lives
    private int totalBricks; // Total number of bricks
    private int LEVEL = 1; // Selected game level
    private String DIFFICULTY = "EASY"; // Selected game difficulty

    /**
     * GAME WINDOW PARAMETERS
     */
    private static final int GAME_WINDOW_WIDTH = 800; // Game window width
    private static final int GAME_WINDOW_HEIGHT = 800; // Game window height

    // Init all game models
    public GameModel() {
        // Get the screen refresh rate
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        refreshRate = gd.getDisplayMode().getRefreshRate();
        if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) { refreshRate = 60; } // Default to 60 FPS if refresh rate is unknown

        // Initialize unlocked levels states
        unlockedLevels.put("EASY", 1);
        unlockedLevels.put("MEDIUM", 1);
        unlockedLevels.put("HARD", 1);
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
    public boolean isGameRunning() { return isGameRunning; }

    // Check if the game is currently paused
    public boolean gamePaused() { return gamePaused; }

    // Change if the game is paused or not
    public void setGamePaused(boolean gamePaused) { this.gamePaused = gamePaused; }

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

    // Get LEVEL value
    public int getLEVEL() { return LEVEL; }

    // Set LEVEL value
    public void setLEVEL(int level) { this.LEVEL = level; }

    // Set DIFFICULTY value
    public void setDIFFICULTY(String difficulty) { this.DIFFICULTY = difficulty; }

    // Starts the game
    public void startGame() {
        isGameRunning = true;
        levelCompleted = false;

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

        // Reset ball position and game state
        ball.reset();
        startGame();

        // Reset previous number of lives
        setLives(prevLives);

        // Reset score and game
        setScore(0);
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

    // Check if level is completed (all bricks are destroyed)
    public void checkLevelComplete() {
        // Decrease number of destroyed bricks
        totalBricks--;

        // Check if all bricks have destroyed
        if (totalBricks <= 0) {
            levelCompleted = true;
            unlockNextLevel();
        }
    }

    // Check if the player can select a specific level
    public boolean canSelectLevel(int level) {
        int maxUnlocked = unlockedLevels.getOrDefault(DIFFICULTY, 1);
        return level <= maxUnlocked;
    }

    // Unlock the next level for the current difficulty
    public void unlockNextLevel() {
        int currentUnlocked = unlockedLevels.getOrDefault(DIFFICULTY, 1);
        if (LEVEL >= currentUnlocked) { unlockedLevels.put(DIFFICULTY, LEVEL + 1); }
    }

    // Get total number of bricks
    public void resetBricksOnly() {
        // We re-generate Paddle and Ball and Bricks for the same LEVEL and DIFFICULTY
        MapGenerator mapGenerator = new MapGenerator(LEVEL, DIFFICULTY, this);
        this.paddle = mapGenerator.getPaddle();
        this.bricks = mapGenerator.getBricks();
        this.ball = mapGenerator.getBall();
        this.totalBricks = mapGenerator.getBricks().size();
        this.levelCompleted = false;
    }

    public boolean isLevelCompleted() { return levelCompleted; }

    public void setLevelCompleted(boolean levelCompleted) { this.levelCompleted = levelCompleted; }
}