package model;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.DisplayMode;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
    private int maxScore = 0; // Maximum score achieved by the player
    private int lives = 3; // Number of lives
    private int totalBricks; // Total number of bricks
    private int LEVEL = 1; // Selected game level
    private String DIFFICULTY = "EASY"; // Selected game difficulty
    private boolean endlessModeActivated = false;

    /**
     * GAME WINDOW PARAMETERS
     */
    private static final int GAME_WINDOW_WIDTH = 800; // Game window width
    private static final int GAME_WINDOW_HEIGHT = 800; // Game window height

    // File for saving game progress (passed levels)
    private static final String PROGRESS_FILE = "progress.properties";

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

        loadProgress(); // Load previously saved game progress
    }

    // Returns sleep time
    public int getRefreshRate() { return refreshRate; }

    // Set sleep time
    public void setRefreshRate(int refreshRate) { this.refreshRate = refreshRate; }

    // Set speed based on screen refresh rate
    public int setSpeed(int baseSpeed) {
        int refreshRate = getRefreshRate();
        if (refreshRate <= 0) { setRefreshRate(60); } // Default to 60 FPS if refresh rate is unknown

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

        setScore(0);
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

    // Get maximum player score
    public int getMaxScore() { return maxScore; }

    // Set player score
    public void setScore(int score) {
        this.score = score;

        if (score > maxScore) {
            maxScore = score;
            saveProgress();
        }
    }

    // Increase player score
    public void increaseScore() { setScore(score += 5); }

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
        if (LEVEL >= currentUnlocked) {
            unlockedLevels.put(DIFFICULTY, LEVEL + 1);
            saveProgress();
        }
    }

    // Get the current unlocked levels
    public Map<String,Integer> getUnlockedLevels() { return new HashMap<>(unlockedLevels); }

    // Saves the game progress to a file
    private void saveProgress() {
        Properties props = new Properties();
        for (var entry : unlockedLevels.entrySet()) {
            props.setProperty(entry.getKey(), entry.getValue().toString());
        }
        props.setProperty("MAXSCORE", Integer.toString(maxScore));

        try (FileOutputStream fos = new FileOutputStream(PROGRESS_FILE)) {
            props.store(fos, "BrickBreaker Progress");
        } catch (IOException e) {
            System.err.println("Failed to save progress: " + e.getMessage());
        }
    }

    // Load the game progress from a file
    private void loadProgress() {
        File f = new File(PROGRESS_FILE);
        if (!f.exists()) { return; } // file does not exist -> we do nothing, default levels are unlocked

        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(f)) {
            props.load(fis);
            // Read from Properties: if key exists, put into unlockedLevels
            String easyVal = props.getProperty("EASY");
            String medVal  = props.getProperty("MEDIUM");
            String hardVal = props.getProperty("HARD");
            if (easyVal != null) unlockedLevels.put("EASY", Integer.parseInt(easyVal));
            if (medVal != null) unlockedLevels.put("MEDIUM", Integer.parseInt(medVal));
            if (hardVal != null) unlockedLevels.put("HARD", Integer.parseInt(hardVal));

            // Load maxScore
            String hsVal = props.getProperty("MAXSCORE");
            if (hsVal != null) {
                maxScore = Integer.parseInt(hsVal);
            }
        } catch (IOException e) {
            System.err.println("Failed to load progress: " + e.getMessage());
        } catch (NumberFormatException nfe) {
            System.err.println("Incorrect progress format in file: " + nfe.getMessage());
        }
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

    public boolean isEndlessModeActivated() { return endlessModeActivated; }

    public void setEndlessModeActivated(boolean endlessModeActivated) { this.endlessModeActivated = endlessModeActivated; }
}