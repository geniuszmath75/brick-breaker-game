package model;

import java.awt.*;
import java.util.Random;

public class Brick {
    private final int[][] map; // 2D array to store brick map
    private final int brickWidth; // Every brick width
    private final int brickHeight; // Every brick height
    private final GameModel model; // GameModel reference
    private int totalBricks; // Total number of remaining bricks
    private int score = 0; // Player score
    private Color[][] brickColors; // Table to store brick colors

    // Brick constructor
    public Brick(int row, int col, GameModel model) {
        this.model = model;
        totalBricks = row * col; // Calculate total number of bricks
        map = new int[row][col]; // Init brick map
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = 1;
            }
        }
        brickWidth = 785/col; // width per brick
        brickHeight = 160/row; // height per brick
        initColors();
    }

    // Initialize brick colors (random for each brick)
    public void initColors() {
        Random rand = new Random();
        brickColors = new Color[map.length][map[0].length]; // Init brick colors table

        // Set random color for each brick
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    Color randomColor;
                    do {
                        randomColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
                    } while (randomColor.equals(Color.BLACK)); // avoid black color

                    brickColors[i][j] = randomColor;
                }
            }
        }
    }

    // Drawing function for all bricks
    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    g.setColor(brickColors[i][j]);
                    g.fillRoundRect(j * brickWidth, i * brickHeight + 70, brickWidth, brickHeight, 25, 25); // Draw brick

                    g.setStroke(new BasicStroke(3)); // Set brick border width
                    g.setColor(Color.BLACK);
                    g.drawRoundRect(j * brickWidth, i * brickHeight + 70, brickWidth, brickHeight, 25, 25); // Draw brick border
                }
            }
        }
    }

    // Get brick map
    public int[][] getMap() { return map; }

    // Get brick width
    public int getBrickWidth() { return brickWidth; }

    // Get brick height
    public int getBrickHeight() { return brickHeight; }

    // Get player score
    public int getScore() { return score; }

    // Increase player score
    public void increaseScore() { score += 5; }

    // Decrease total number of bricks
    public void decreaseTotalBricks() {
        if (--totalBricks <= 0) {
            model.stopGame();
        }
    }

    // Setting specific brick value
    public void setBrickValue(int value, int row, int col) { map[row][col] = value; }
}