package model;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Paddle {
    private double x; // X coordinate
    private final double y; // Y coordinate
    private final int width; // Paddle width
    private final int height; // Paddle height
    private final int speed; // Paddle movement speed
    private boolean movingLeft = false; // Indicates if paddle is moving left
    private boolean movingRight = false; // Indicates if paddle is moving right

    // Paddle constructor
    public Paddle(double x, double y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    // Moves the paddle left if within bounds
    public void moveLeft() { if (x - speed >= 0) { x -= speed; } } // moving paddle if it doesn't exceed the left window border

    // Moves the paddle right if within bounds (assuming 800px window width)
    public void moveRight() { if (x + width < 785) { x += speed; } } // moving paddle if it doesn't exceed the right window border

    // Moves the paddle based on current direction flags
    public void move() {
        if (movingLeft) { moveLeft(); }
        if (movingRight) { moveRight(); }
    }

    // Updates movement flags based on key input
    public void setMoving(int keyCode, boolean isMoving) {
        if (keyCode == KeyEvent.VK_LEFT) {
            movingLeft = isMoving;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            movingRight = isMoving;
        }
    }

    // Stops all paddle movement (e.g., after game over)
    public void stopMoving() {
        movingLeft = false;
        movingRight = false;
    }

    // Paints the paddle
    public void paint(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect((int) getX(), (int) getY(), getWidth(), getHeight(), 15, 15);
    }
}