package model;

import java.awt.*;

public class GameModel {
    // Stores the current game state (running or not)
    private boolean isGameRunning = false;
    private final Paddle paddle; // store Paddle data

    // Init all game models
    public GameModel() {
        paddle = new Paddle(350, 500, 100, 10, 10, Color.BLACK); // Init paddle instance
    }

    // Checks if the game is currently running
    public boolean isGameRunning() {
        return isGameRunning;
    }

    // Returns Paddle instance
    public Paddle getPaddle() {
        return paddle;
    }

    // Move paddle to left
    public void movePaddleLeft() {
        paddle.moveLeft();
    }

    // Move paddle to right
    public void movePaddleRight() {
        paddle.moveRight();
    }

    // Starts the game
    public void startGame() {
        isGameRunning = true;
    }

    // Stops the game
    public void stopGame() {
        isGameRunning = false;
    }
}