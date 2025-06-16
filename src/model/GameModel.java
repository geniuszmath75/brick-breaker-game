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
    private Ball ball; // Stores Ball data
    private List<Brick> bricks; // Stores Brick data
    private Paddle paddle; // Stores Paddle data

    /**
     * GAME PARAMETERS
     */
    private boolean isGameRunning = false; // Current game state (running or not)
    private boolean gamePaused = false; // Game pause state
    private boolean levelCompleted = false; // Is current level completed
    private final Map<String, Integer> unlockedLevels = new HashMap<>(); // Stores unlocked levels per difficulty
    private int refreshRate; // Refresh rate in milliseconds
    private int score = 0; // Player score
    private int maxScore = 0; // Maximum score reached
    private int lives = 3; // Number of lives
    private int totalBricks; // Total number of bricks in level
    private int LEVEL = 1; // Current game level
    private String DIFFICULTY = "EASY"; // Current game difficulty
    private boolean endlessModeActivated = false; // Endless mode status

    /**
     * GAME WINDOW PARAMETERS
     */
    private static final int GAME_WINDOW_WIDTH = 800; // Width of game window
    private static final int GAME_WINDOW_HEIGHT = 800; // Height of game window

    // File path for saving progress
    private static final String PROGRESS_FILE = "progress.properties";

    // Initialize game model
    public GameModel() {
        // Get the screen refresh rate
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        refreshRate = gd.getDisplayMode().getRefreshRate();
        if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) {
            refreshRate = 60; // Default to 60 FPS
        }

        // Initialize unlocked levels states
        unlockedLevels.put("EASY", 1);
        unlockedLevels.put("MEDIUM", 1);
        unlockedLevels.put("HARD", 1);

        loadProgress(); // Load saved progress
    }

    public int getRefreshRate() { return refreshRate; }

    public void setRefreshRate(int refreshRate) { this.refreshRate = refreshRate; }

    // Calculate speed adjusted for screen refresh rate
    public int setSpeed(int baseSpeed) {
        int refreshRate = getRefreshRate();
        if (refreshRate <= 0) { setRefreshRate(60); } // Default to 60 FPS if refresh rate is unknown

        // Calculate speed based on refresh rate
        // 240Hz ~ 1 pixel per frame; 165Hz ~ 2; 144Hz ~ 3; 120Hz ~ 3; 60Hz ~ 7;
        return baseSpeed + Math.max(1, (int) (3.0 * 144 / refreshRate));
    }

    public boolean isGameRunning() { return isGameRunning; }

    public boolean gamePaused() { return gamePaused; }

    public void setGamePaused(boolean gamePaused) { this.gamePaused = gamePaused; }

    public Ball getBall() { return ball; }

    public List<Brick> getBricks() { return bricks; }

    public Paddle getPaddle() { return paddle; }

    public int getGameWindowWidth() { return GAME_WINDOW_WIDTH; }

    public int getGameWindowHeight() { return GAME_WINDOW_HEIGHT; }

    public int getLEVEL() { return LEVEL; }

    public void setLEVEL(int level) { this.LEVEL = level; }

    public void setDIFFICULTY(String difficulty) { this.DIFFICULTY = difficulty; }

    // Starts or restart the level
    public void startGame() {
        isGameRunning = true;
        levelCompleted = false;

        // Generate map elements(Ball, Paddle, List<Brick>) depends on level and difficulty
        MapGenerator mapGenerator = new MapGenerator(LEVEL, DIFFICULTY, this);
        this.paddle = mapGenerator.getPaddle();
        this.bricks = mapGenerator.getBricks();
        this.ball = mapGenerator.getBall();
        this.totalBricks = bricks.size();

        setScore(0);
    }

    // Reset current game (used after losing a life)
    public void renewGame() {
        int prevLives = getLives();

        // Reset ball position and game state
        ball.reset();
        startGame();
        setLives(prevLives == 0 ? 3 : prevLives);
        setScore(0);
    }

    public void stopGame() { isGameRunning = false; }

    public int getLives() { return lives; }

    public void setLives(int lives) { this.lives = lives; }

    public int getScore() { return score; }

    public int getMaxScore() { return maxScore; }

    public void setScore(int score) {
        this.score = score;

        if (score > maxScore) {
            maxScore = score;
            saveProgress();
        }
    }

    // Increase player score by 5 points
    public void increaseScore() { setScore(score += 5); }

    // Decrement totalBricks and check level completion
    public void checkLevelComplete() {
        totalBricks--;

        // Check if all bricks have destroyed
        if (totalBricks <= 0) {
            levelCompleted = true;
            unlockNextLevel();
        }
    }

    // Check if player can select a given level
    public boolean canSelectLevel(int level) {
        int maxUnlocked = unlockedLevels.getOrDefault(DIFFICULTY, 1);
        return level <= maxUnlocked;
    }

    // Unlock the next level for current difficulty
    public void unlockNextLevel() {
        int currentUnlocked = unlockedLevels.getOrDefault(DIFFICULTY, 1);
        if (LEVEL >= currentUnlocked) {
            unlockedLevels.put(DIFFICULTY, LEVEL + 1);
            saveProgress();
        }
    }

    public Map<String,Integer> getUnlockedLevels() { return new HashMap<>(unlockedLevels); }

    // Saves progress to a file
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

    // Load progress from file
    private void loadProgress() {
        File f = new File(PROGRESS_FILE);
        if (!f.exists()) return; // file does not exist -> we do nothing, default levels are unlocked

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

    // Reset only bricks, paddle and ball while keeping score and lives
    public void resetBricksOnly() {
        // We re-generate Paddle and Ball and Bricks for the same LEVEL and DIFFICULTY
        MapGenerator mapGenerator = new MapGenerator(LEVEL, DIFFICULTY, this);
        this.paddle = mapGenerator.getPaddle();
        this.bricks = mapGenerator.getBricks();
        this.ball = mapGenerator.getBall();
        this.totalBricks = bricks.size();
        this.levelCompleted = false;
    }

    public boolean isLevelCompleted() { return levelCompleted; }

    public void setLevelCompleted(boolean levelCompleted) { this.levelCompleted = levelCompleted; }

    public boolean isEndlessModeActivated() { return endlessModeActivated; }

    public void setEndlessModeActivated(boolean endlessModeActivated) { this.endlessModeActivated = endlessModeActivated; }
}