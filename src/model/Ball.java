package model;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Ball {
    private int x; // Coordinate X
    private int y; // Coordinate Y
    private final int diameter; // Diameter of the ball
    private int xSpeed = 0; // Ball movement speed on X axis
    private int ySpeed = 0; // Ball movement speed on Y axis
    private int speed; // General ball movement speed value
    private final GameModel model; // GameModel reference
    private final Paddle paddle; // Paddle reference
    private Brick brick; // Brick reference
    private boolean stuck = true; // Ball is stuck to the paddle - position on start

    // Ball constructor
    public Ball(int xStart, int yStart, int diameter, GameModel model, Paddle paddle, Brick brick) {
        this.x = xStart;
        this.y = yStart;
        this.diameter = diameter;
        this.paddle = paddle;
        this.brick = brick;
        this.model = model;
        setSpeed();
    }

    // Set ball speed based on screen refresh rate
    private void setSpeed() {
        int refreshRate = model.getRefreshRate();
        if (refreshRate <= 0) { // Default to 60 FPS if refresh rate is unknown
            refreshRate = 60;
        }
        speed = Math.max(1, (int) (3.0 * 144 / refreshRate)); // Calculate speed based on refresh rate
        // 240Hz ~ 1 pixel per frame; 165Hz ~ 2; 144Hz ~ 3; 120Hz ~ 3; 60Hz ~ 7;
    }

    // Get coordinate X value
    public int getX() { return x; }

    // Get coordinate Y value
    public int getY() { return y; }

    // Get diameter value of the ball
    public int getDiameter() { return diameter; }

    // Get ball stuck status
    public boolean isStuck() { return stuck; }

    // TODO: improve ball reflection from the paddle (different bounce angle if paddle moves)
    // Set ball movement speed and direction on X & Y axis
    public void move() {
        if (stuck) { // Ball is stuck to the paddle
            x = paddle.getX() + (paddle.getWidth() / 2) - (diameter / 2);
            y = paddle.getY() - diameter;
        } else if (this.getY() >= paddle.getY() + paddle.getHeight()) { // Ball under the paddle
            reset();
        } else if (model.getLives() == 0) { // Game over
            model.stopGame();
        } else { // Ball is moving
            x += xSpeed;
            y += ySpeed;

            // Reflection from left wall
            if (x <= 0) {
                x = 0;
                xSpeed = -xSpeed;
            }

            // Reflection from right wall
            if(x + diameter >= 785) {
                x = 785 - diameter;
                xSpeed = -xSpeed;
            }

            // Reflection from the top wall
            if (y <= 0) { ySpeed = -ySpeed; }

            // Reflection from the paddle
            if (y + diameter >= paddle.getY() && x + diameter >= paddle.getX() && x <= paddle.getX() + paddle.getWidth()) {
                ySpeed = -ySpeed;
            }
        }
    }

    // TODO: improve ball reflection from the bricks (sometimes ball destroys 3 bricks at once)
    // Handle ball collision with bricks
    public void checkCollision() {
        for (int i = 0; i < brick.getMap().length; i++) {
            for (int j = 0; j < brick.getMap()[0].length; j++) {
                if (brick.getMap()[i][j] > 0) { // Check only existing bricks
                    int brickX = j * brick.getBrickWidth();
                    int brickY = i * brick.getBrickHeight() + 70;
                    int brickWidth = brick.getBrickWidth();
                    int brickHeight = brick.getBrickHeight();

                    Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                    Rectangle ballRect = new Rectangle(x, y, diameter, diameter);

                    // Check if ball intersects with brick
                    if (ballRect.intersects(brickRect)) {
                        brick.setBrickValue(0, i, j); // set brick value to 0 (destroyed)
                        brick.decreaseTotalBricks(); // decrease total number of bricks
                        model.increaseScore(); // increase score
                        if (x + diameter <= brickRect.x || x + 1 >= brickRect.x + brickRect.width) {
                            xSpeed = -xSpeed; // reflection from the left or right side of the brick
                        } else {
                            ySpeed = -ySpeed; // reflection from the top or bottom side of the brick
                        }
                    }
                }
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
        model.setLives(model.getLives() - 1);
        if (model.getLives() <= 0) { // decrease lives
            model.stopGame(); // if 0 lives - game over
            return;
        }

        stuck = true; // reset ball position
        xSpeed = 0;
        ySpeed = 0;
        x = paddle.getX() + (paddle.getWidth() / 2) - (diameter / 2);
        y = paddle.getY() - diameter;
    }

    public void updateBrickReference(Brick brick) {
        this.brick = brick;
    }

    // Handle ball starting
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) { start(); }
    }
}