package model;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Ball {
    private double x; // Coordinate X
    private double y; // Coordinate Y
    private final int diameter; // Diameter of the ball
    private double xSpeed = 0.0; // Ball movement speed on X axis
    private double ySpeed = 0.0; // Ball movement speed on Y axis
    private int speed; // General ball movement speed value
    private final GameModel model; // GameModel reference
    private Paddle paddle; // Paddle reference
    private Brick brick; // Brick reference
    private boolean stuck = true; // Ball is stuck to the paddle - position on start

    // Ball constructor
    public Ball(double xStart, double yStart, int diameter, GameModel model, Paddle paddle, Brick brick) {
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
    public double getX() { return x; }

    // Get coordinate Y value
    public double getY() { return y; }

    // Get diameter value of the ball
    public int getDiameter() { return diameter; }

    // Get ball stuck status
    public boolean isStuck() { return stuck; }

    // Set ball movement speed and direction on X & Y axis
    public void move() {
        if (stuck) { // Ball is stuck to the paddle
            x = paddle.getX() + (paddle.getWidth() / 2.0) - (diameter / 2.0);
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
                x = 0.0;
                xSpeed = -xSpeed;
            }

            // Reflection from right wall
            if (x + diameter >= 785) {
                x = 785 - diameter;
                xSpeed = -xSpeed;
            }

            // Reflection from the top wall
            if (y <= 0) {
                y = 0.0;
                ySpeed = -ySpeed;
            }

            // Reflection from the paddle
            if (y + diameter >= paddle.getY() && y + diameter - ySpeed < paddle.getY() &&
                    x + diameter >= paddle.getX() && x <= paddle.getX() + paddle.getWidth()) {

                y = paddle.getY() - diameter; // ensure that the ball does not "jump" the paddle

                // Ball reflection angle
                double bounceAngle = getBounceAngle();

                // Total velocity
                double totalSpeed = Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);

                // New velocity components
                xSpeed = totalSpeed * Math.sin(bounceAngle);
                ySpeed = -totalSpeed * Math.cos(bounceAngle);
            }
        }
    }

    // Calculate the angle of the ball reflection from the paddle
    private double getBounceAngle() {
        double paddleCenter = paddle.getX() + paddle.getWidth() / 2.0;
        double ballCenter = x + diameter / 2.0;

        // Calculate how much from the paddle center the ball has hit (-1.0 to 1.0)
        double relativeIntersect = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0);

        // Max angle of reflection: 75deg (in radians)
        double maxBounceAngle = Math.toRadians(75);

        return relativeIntersect * maxBounceAngle;
    }

    // Handle ball collision with bricks
    public void checkCollision() {
        for (int i = 0; i < brick.getMap().length; i++) {
            for (int j = 0; j < brick.getMap()[0].length; j++) {
                if (brick.getMap()[i][j] > 0) { // Check only existing bricks
                    int brickX = j * brick.getBrickWidth();
                    int brickY = i * brick.getBrickHeight() + 70;
                    int brickWidth = brick.getBrickWidth();
                    int brickHeight = brick.getBrickHeight();

                    // Create new rectangles for ball and brick to check intersection
                    Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                    Rectangle ballRect = new Rectangle((int)x, (int)y, diameter, diameter);

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
                        return; // exit the loop after the first brick collision (case of destroying multiple bricks at once)
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
        }

        stuck = true; // reset ball position
        xSpeed = 0;
        ySpeed = 0;
        x = paddle.getX() + (paddle.getWidth() / 2.0) - (diameter / 2.0);
        y = paddle.getY() - diameter;
    }

    // Update brick reference
    public void updateBrickReference(Brick brick) {
        this.brick = brick;
    }

    // Update paddle reference
    public void updatePaddleReference(Paddle paddle) {
        this.paddle = paddle;
    }

    // Handle ball starting
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) { start(); }
    }
}