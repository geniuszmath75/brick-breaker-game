package model;

import java.util.ArrayList;
import java.util.List;

public class MapGenerator {
    private final int level;
    private final String difficulty;

    private final GameModel model;

    private Ball ball; // store Ball data
    private Paddle paddle; // store Paddle data
    private final List<Brick> bricks; // store list of Brick objects data

    public MapGenerator(int level, String difficulty, GameModel gameModel) {
        this.model = gameModel;
        this.level = level;
        this.difficulty = difficulty.toLowerCase();
        this.bricks = new ArrayList<>();

        generateBricks();
        generatePaddle();
        generateBall();
    }

    public Ball getBall() { return ball; };

    // Return paddle instance
    public Paddle getPaddle() { return paddle; }
    public List<Brick> getBricks() { return bricks; }

    private void generateBall() {
        if(paddle == null) {
            throw new IllegalStateException("Paddle must be initialized before Ball");
        }
        int speed = switch (difficulty) {
            case "medium" -> 1;
            case "hard" -> 2;
            default -> 0;
        };

        ball = new Ball(paddle.getX(), paddle.getY(), 25, speed, model, paddle, bricks);
    }

    private void generatePaddle() {
        int speed;
        int width;
        switch (difficulty) {
            case "medium": {
                speed = 7;
                width = 130;
                break;
            }
            case "hard": {
                speed = 6;
                width = 100;
                break;
            }
            // EASY mode
            default: {
                speed = 8;
                width = 160;
            }
        }

        paddle = new Paddle(320, 715, width, 15, speed);
    }

    private void generateBricks() {
        int rows = 10;
        int cols = 10;
        int offsetY = 50;
        int padding = 5;
        int brickWidth = (model.getGameWindowWidth() - 2*padding) / 10 - padding; // GAME_WINDOW_WIDTH = 800 -> brickWidth = 74
        int brickHeight = (model.getGameWindowHeight() - offsetY) / 20 - padding; // GAME_WINDOW_HEIGHT = 800 -> brickHeight = 32

        int baseDestruction;
        switch (difficulty) {
            case "medium": {
                baseDestruction = 2;
                break;
            }
            case "hard": {
                baseDestruction = 3;
                break;
            }
            default: {
                baseDestruction = 1;
            }
        }
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                int x = col * (brickWidth + padding);
                int y = row * (brickHeight + padding) + offsetY;
                bricks.add(new Brick(x, y, brickWidth, brickHeight, baseDestruction));
            }
        }
    }
}
