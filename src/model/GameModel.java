package model;

public class GameModel {
    // Stores the current game state (running or not)
    private boolean isGameRunning = false;

    // Checks if the game is currently running
    public boolean isGameRunning() {
        return isGameRunning;
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