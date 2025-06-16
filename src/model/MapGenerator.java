package model;

import java.util.ArrayList;
import java.util.List;

public class MapGenerator {
    private final int LEVEL; // Selected game level
    private final String DIFFICULTY; // Selected game difficulty

    private final GameModel model;

    private Ball ball; // Ball instance
    private Paddle paddle; // Paddle instance
    private final List<Brick> bricks; // List of bricks

    public MapGenerator(int level, String difficulty, GameModel gameModel) {
        this.model = gameModel;
        this.LEVEL = level;
        this.DIFFICULTY = difficulty.toLowerCase();
        this.bricks = new ArrayList<>();

        generateBricks();
        generatePaddle();
        generateBall();
    }

    // Return Ball instance
    public Ball getBall() { return ball; }

    // Return paddle instance
    public Paddle getPaddle() { return paddle; }

    // Return list of Brick instances
    public List<Brick> getBricks() { return bricks; }

    // Generate Ball element on map
    private void generateBall() {
        // Check if Paddle is initialized before Ball
        if(paddle == null) { throw new IllegalStateException("Paddle must be initialized before Ball"); }

        int baseSpeed = 0;
        int diameter = 25;

        // Initialize Ball object
        double centeredX = paddle.getX() + (paddle.getWidth() / 2.0) - (diameter / 2.0);
        double centeredY = paddle.getY() - diameter; // correct initial generation of position
        ball = new Ball(centeredX, centeredY, diameter, model.setSpeed(baseSpeed), model, paddle, bricks);
    }

    // Generate Paddle element on map
    private void generatePaddle() {
        int speed; // Paddle base speed
        int width; // Paddle width

        // Set paddle base speed and width based on DIFFICULTY
        switch (DIFFICULTY) {
            case "medium":
                speed = 1;
                width = 140;
                break;
            case "hard":
                speed = 0;
                width = 120;
                break;
            default: // EASY
                speed = 2;
                width = 160;
        }

        // Initialize Paddle object
        paddle = new Paddle(320, 715, width, 15, model.setSpeed(speed));
    }

    // Generate brick pattern for given level and difficulty
    private void generateBricks() {
        int offsetY = 50; // Distance from top of window
        int padding = 5; // Space between bricks

        int brickWidth = (model.getGameWindowWidth() - 2 * padding) / 10 - padding; // GAME_WINDOW_WIDTH = 800 -> brickWidth = 74
        int brickHeight = (model.getGameWindowHeight() - offsetY) / 20 - padding; // GAME_WINDOW_HEIGHT = 800 -> brickHeight = 32

        // Set Brick destructionLevel
        int baseDestruction = switch (DIFFICULTY) {
            case "medium" -> 2;
            case "hard" -> 3;
            default -> 1; // EASY
        };

        // Get 2D array of brick pattern
        int[][] pattern = getLayoutForLevel(LEVEL, DIFFICULTY).getLayout();

        for(int row = 0; row < pattern.length; row++) {
            for(int col = 0; col < pattern[row].length; col++) {
                // Add Brick to list if it exists in pattern array
                if(pattern[row][col] == 1) {
                    int x = col * (brickWidth + padding);
                    int y = row * (brickHeight + padding) + offsetY;
                    bricks.add(new Brick(x, y, brickWidth, brickHeight, baseDestruction));
                }
            }
        }
    }

    // Return brick layout based on level and difficulty
    private BrickLayout getLayoutForLevel(int level, String difficulty) {
        return switch (level) {
            case 1 -> switch (difficulty) {
                case "medium" -> BrickLayout.LEVEL_1_MEDIUM;
                case "hard" -> BrickLayout.LEVEL_1_HARD;
                default -> BrickLayout.LEVEL_1_EASY;
            };
            case 2 -> switch (difficulty) {
                case "medium" -> BrickLayout.LEVEL_2_MEDIUM;
                case "hard" -> BrickLayout.LEVEL_2_HARD;
                default -> BrickLayout.LEVEL_2_EASY;
            };
            case 3 -> switch (difficulty) {
                case "medium" -> BrickLayout.LEVEL_3_MEDIUM;
                case "hard" -> BrickLayout.LEVEL_3_HARD;
                default -> BrickLayout.LEVEL_3_EASY;
            };
            default -> BrickLayout.LEVEL_1_EASY;
        };
    }
}