package model;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Paddle {
    private int x; // Coordinate X
    private int y; // Coordinate Y
    private int width; // Paddle width
    private int height; // Paddle height
    private int speed; // Paddle movement speed
    private Color color; // Paddle color

    // Paddle constructor
    public Paddle(int x, int y, int width, int height, int speed, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.color = color;
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

    // Get speed value of the paddle
    public int getSpeed() {
        return speed;
    }

    // Get color of the paddle
    public Color getColor() {
        return color;
    }

    // Move paddle to left (decrease coordinate X by speed value)
    public void moveLeft() {
        x -= speed;
    }

    // Move paddle to right (increase coordinate X by speed value)
    public void moveRight() {
        x += speed;
    }

    // Handle paddle movement
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> moveLeft();
            case KeyEvent.VK_RIGHT -> moveRight();
        }
    }
}
