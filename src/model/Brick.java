package model;

import java.awt.*;
import java.util.Random;

public class Brick {
    private final double x; // Coordinate X
    private final double y; // Coordinate Y
    private final int brickWidth; // Brick width
    private final int brickHeight; // Brick height
    protected final GameModel model; // GameModel reference
    private final Color brickColor; // Brick color
    private int destructionLevel; // Level of brick destruction

    // Brick constructor
    public Brick(double x, double y, int width, int height, int destruction, GameModel modelInstance) {
        this.model = modelInstance;
        this.x = x;
        this.y = y;
        this.brickWidth = width;
        this.brickHeight = height;
        this.destructionLevel = destruction;
        this.brickColor = initColor();
    }

    // Initialize brick random color
    public Color initColor() {
        Random rand = new Random();

        // Set random color for brick
        Color randomColor;
        randomColor = new Color(rand.nextInt(1, 256), rand.nextInt(1, 256), rand.nextInt(1, 256));
        return randomColor;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Get brick width
    public int getBrickWidth() {
        return brickWidth;
    }

    // Get brick height
    public int getBrickHeight() {
        return brickHeight;
    }

    // Handle ball hit
    public void hit() {
        destructionLevel--;
    }

    // Get brick color
    public Color getBrickColor() {
        return brickColor;
    }

    // Get destruction level
    public int getDestructionLevel() {
        return destructionLevel;
    }
}