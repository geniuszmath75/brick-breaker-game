package model;

import java.awt.event.KeyEvent;

public class Ball {
    private int x; // Coordinate X
    private int y; // Coordinate Y
    private final int diameter = 25; // Diameter of the ball
    private int xSpeed = 0; // Ball movement speed on X axis
    private int ySpeed = 0; // Ball movement speed on Y axis
    private final int speed = 3; // General ball movement speed value
    private final Paddle paddle; // Paddle reference
    private final GameModel model; // GameModel reference
    private boolean stuck = true; // Ball is stuck to the paddle - position on start
    private int lives = 3; // Number of lives

    // Ball constructor
    public Ball(int xStart, int yStart, Paddle paddle, GameModel model) {
        this.x = xStart;
        this.y = yStart;
        this.paddle = paddle;
        this.model = model;
    }

    // Get coordinate X value
    public int getX() { return x; }

    // Get coordinate Y value
    public int getY() { return y; }

    // Get diameter value of the ball
    public int getDiameter() { return diameter; }

    // Get ball movement speed on X axis
    public int getXSpeed() { return xSpeed; }

    // Get ball movement speed on Y axis
    public int getYSpeed() { return ySpeed; }

    // Get general ball movement speed
    public int getSpeed() { return speed; }

    // Get ball stuck status
    public boolean isStuck() { return stuck; }

    // Get number of lives
    public int getLives() { return lives; }

    // Set ball movement speed and direction on X & Y axis
    public void move() {
        if (stuck) { // Ball is stuck to the paddle
            x = paddle.getX() + (paddle.getWidth() / 2) - (diameter / 2);
            y = paddle.getY() - diameter;
        } else if (this.getY() >= 600) { // Ball is out of the down border
            reset();
        } else { // Ball is moving
            x += xSpeed;
            y += ySpeed;

            // Reflection from left and right walls
            if (x <= 0 || x + (2 * diameter) > 800) {
                xSpeed = -xSpeed;
            }

            // Reflection from the top wall
            if (y <= 0) {
                ySpeed = -ySpeed;
            }

            // Reflection from the paddle
            if (y + diameter >= paddle.getY() && x + diameter >= paddle.getX() && x <= paddle.getX() + paddle.getWidth()) {
                ySpeed = -ySpeed; // reflection in the opposite direction
            }
        }
    }

    // Start the ball movement
    public void start() {
        if (stuck) {
            stuck = false; // Ball is not stuck anymore
            xSpeed = speed; // Ball starts moving
            ySpeed = -speed;
        }
    }

    // Reset ball position after it's out of the down border
    public void reset() {
        if (--lives <= 0) { // decrease lives
            model.stopGame(); // if 0 lives - game over
            return;
        }

        stuck = true; // reset ball position
        xSpeed = 0;
        ySpeed = 0;
        x = paddle.getX() + (paddle.getWidth() / 2) - (diameter / 2);
        y = paddle.getY() - diameter;
    }

    // Handle ball starting
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) { start(); }
    }
}
