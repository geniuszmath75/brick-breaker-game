package model;

import java.awt.event.KeyEvent;

public class Paddle {
    private int x; // Coordinate X
    private final int y; // Coordinate Y
    private final int width; // Paddle width
    private final int height; // Paddle height
    private final int speed; // Paddle movement speed
    private boolean movingLeft = false; // Paddle moving left flag
    private boolean movingRight = false; // Paddle moving right flag

    // Paddle constructor
    public Paddle(int x, int y, int width, int height, int speed, GameModel model) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;

        // New thread to handle paddle movement
        new Thread(() -> {
            while (true) {
                if (movingLeft) { moveLeft(); }
                if (movingRight) { moveRight(); }
                try {
                    Thread.sleep(1000 / model.getRefreshRate() * 2); // sleep based on refresh rate (in milliseconds) * 2
                } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    // Get coordinate X value
    public int getX() {
        return x;
    }

    // Get coordinate Y value
    public int getY() {
        return y;
    }

    // Get width value of the paddle
    public int getWidth() {
        return width;
    }

    // Get height value of the paddle
    public int getHeight() {
        return height;
    }

    // Move paddle to left (decrease coordinate X by speed value)
    public void moveLeft() {
        if (x - speed >= 0) { x -= speed; } // moving paddle if it doesn't exceed the left window border
    }

    // Move paddle to right (increase coordinate X by speed value)
    public void moveRight() {
        if (x + width < 785) { x += speed; } // moving paddle if it doesn't exceed the right window border
    }

    // Handle paddle movement
    public void setMoving(int keyCode, boolean isMoving) {
        if (keyCode == KeyEvent.VK_LEFT) {
            movingLeft = isMoving;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            movingRight = isMoving;
        }
    }

    // Stop paddle movement (case when in the moment of game over paddle is still moving)
    public void stopMoving() {
        movingLeft = false;
        movingRight = false;
    }
}