package model;

import java.awt.event.KeyEvent;

public class Paddle {
    private int x; // Coordinate X
    private final int y; // Coordinate Y
    private final int width; // Paddle width
    private final int height; // Paddle height
    private final int speed; // Paddle movement speed

    // Paddle constructor
    public Paddle(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
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
        if (x + width + speed < 800) { x += speed; } // moving paddle if it doesn't exceed the right window border
    }

    // TODO: try to find better way to handle paddle movement (for better fluency and responsiveness)
    // Handle paddle movement
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> moveLeft();
            case KeyEvent.VK_RIGHT -> moveRight();
        }
    }
}