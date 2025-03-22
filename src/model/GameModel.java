package model;

public class GameModel {
    // Stores the current game state (running or not)
    private boolean isGameRunning = false;
    private final Paddle paddle; // store Paddle data
    private final Ball ball; // store Ball data

    // Init all game models
    public GameModel() {
        paddle = new Paddle(315, 515, 155, 15, 15); // Init paddle instance
        ball = new Ball(paddle.getX() + 65, paddle.getY() - 25, paddle, this); // Init ball instance
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

    // Returns Ball instance
    public Ball getBall() { return ball; }

    // Start ball
    public void startBall() { ball.start(); }

    // Move ball
    public void moveBall() { ball.move(); }

    // Starts the game
    public void startGame() {
        isGameRunning = true;
    }

    // Stops the game
    public void stopGame() { isGameRunning = false; }
}