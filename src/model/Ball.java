package model;

import utils.SoundLoader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class Ball {
    private double x; // Coordinate X
    private double y; // Coordinate Y
    private final int diameter; // Diameter of the ball
    private double xSpeed = 0.0; // Ball movement speed on X axis
    private double ySpeed = 0.0; // Ball movement speed on Y axis
    private final int speed; // General ball movement speed value
    private final GameModel model; // GameModel reference
    private final Paddle paddle; // Paddle reference
    private final List<Brick> bricks; // Brick list reference
    private boolean stuck; // Ball is stuck to the paddle - position on start

    // Ball constructor
    public Ball(double xStart, double yStart, int diameter, int speed, GameModel modelInstance, Paddle paddle, List<Brick> bricks) {
        this.x = xStart;
        this.y = yStart;
        this.diameter = diameter;
        this.paddle = paddle;
        this.bricks = bricks;
        this.model = modelInstance;
        this.speed = speed;
        this.stuck = true;
    }

    // Get coordinate X value
    public double getX() { return x; }

    // Get coordinate Y value
    public double getY() { return y; }

    // Get diameter value of the ball
    public int getDiameter() { return diameter; }

    // Get ball stuck status
    public boolean isStuck() { return stuck; }

    /**
     * Moves the ball one step based on current speed.
     * Handles sticking to paddle, game over condition, and collision with paddle or walls.
     */
    public void move() {
        // Ball is stuck to the paddle
        if (stuck) {
            x = paddle.getX() + (paddle.getWidth() / 2.0) - (diameter / 2.0);
            y = paddle.getY() - diameter;
            return;
        }

        // Check for game over
        if (model.getLives() == 0) {
            model.stopGame();
            return;
        }

        // Ball has fallen below the paddle
        if (this.getY() >= paddle.getY() + paddle.getHeight()) {
            SoundLoader.playWAV("/sounds/brunch.wav");
            reset();
            return;
        }

        // Update ball position
        x += xSpeed;
        y += ySpeed;

        reflectFromWalls();

        // Check for paddle collision
        Rectangle paddleRect = new Rectangle((int) paddle.getX(), (int) paddle.getY(), paddle.getWidth(), paddle.getHeight());
        Rectangle ballRect = new Rectangle((int) x, (int) y, diameter, diameter);

        if (ballRect.intersects(paddleRect)) {
            SoundLoader.playWAV("/sounds/pong.wav");
            y = paddle.getY() - diameter; // Adjust position to avoid multiple collisions
            reflectFrom(paddleRect, true); // Angled reflection from paddle
        }
    }

    // Reflects the ball when it hits the left, right or top wall.
    private void reflectFromWalls() {
        // Reflection from left wall
        if (x <= 0) {
            x = 0.0;
            xSpeed = -xSpeed;

        }
        // Reflection from right wall
        else if (x + diameter >= 785) {
            x = 785 - diameter;
            xSpeed = -xSpeed;
        }

        // Reflection from the top wall
        if (y <= 0) {
            y = 0.0;
            ySpeed = -ySpeed;
        }
    }

    /**
     * Reflects the ball from a given surface (paddle or brick).
     * If 'angled' is true, the reflection angle is calculated based on where the ball hits the paddle.
     * Otherwise, the reflection is based on simple collision logic (used for bricks).
     */
    private void reflectFrom(Rectangle surface, boolean angled) {
        if (angled) {
            // Calculate bounce angle based on collision point with the paddle
            double bounceAngle = getBounceAngle(surface);
            double totalSpeed = Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);
            xSpeed = totalSpeed * Math.sin(bounceAngle);
            ySpeed = -totalSpeed * Math.cos(bounceAngle); // Always reflect upward
        } else {
            // Ball center coordinates
            double ballCenterX = x + diameter / 2.0;
            double ballCenterY = y + diameter / 2.0;

            // Surface center coordinates
            double surfaceCenterX = surface.getX() + surface.getWidth() / 2.0;
            double surfaceCenterY = surface.getY() + surface.getHeight() / 2.0;

            // Calculate relative position of collision
            double dx = ballCenterX - surfaceCenterX;
            double dy = ballCenterY - surfaceCenterY;

            double widthRatio = dx / surface.getWidth();
            double heightRatio = dy / surface.getHeight();

            // Determine bounce direction
            if (Math.abs(widthRatio - heightRatio) < 0.1) {
                // Diagonal collision - reverse both directions
                ySpeed = -ySpeed;
                xSpeed = -xSpeed;
            } else if (Math.abs(widthRatio) > Math.abs(heightRatio)) {
                // Horizontal collision (left or right side)
                xSpeed = -xSpeed;
            } else {
                // Vertical collision (top or bottom side)
                ySpeed = -ySpeed;
            }
        }
    }

    /**
     * Calculates the bounce angle based on where the ball hits the paddle.
     * The further from the center, the steeper the angle.
     */
    private double getBounceAngle(Rectangle surface) {
        double surfaceCenter = surface.getX() + surface.getWidth() / 2.0;
        double ballCenter = x + diameter / 2.0;

        // Calculate how much from the paddle center the ball has hit (-1.0 to 1.0)
        double relativeIntersect = (ballCenter - surfaceCenter) / (surface.getWidth() / 2.0);

        // Clamp the value to the range [-1.0, 1.0] for sure
        if (relativeIntersect < -1.0) {
            relativeIntersect = -1.0;
        } else if (relativeIntersect > 1.0) {
            relativeIntersect = 1.0;
        }

        // Max angle of reflection: 75deg (in radians)
        double maxBounceAngle = Math.toRadians(65);

        return relativeIntersect * maxBounceAngle;
    }

    /**
     * Handles ball collision with a brick.
     * If collision is detected, updates brick state, score, and reflects the ball.
     */
    public void checkCollision() {
        // Create new rectangles for ball and brick to check intersection
        Rectangle ballRect = new Rectangle((int) x, (int) y, diameter, diameter);

        for(Brick b : bricks) {
            Rectangle brickRect = new Rectangle((int) b.getX(), (int) b.getY(), b.getBrickWidth(), b.getBrickHeight());
            // Check if ball intersects with brick
            if (ballRect.intersects(brickRect)) {

                SoundLoader.playWAV("/sounds/ring.wav");

                // Collision detected
                b.hit();

                // Add points
                model.increaseScore();

                // Check if brick is destroyed and then if level is cleared
                if(b.isDestroyed()) { model.checkLevelComplete(); }

                // Reflect ball based on collision with brick
                reflectFrom(brickRect, false);
                break;
            }
        }
        // Update bricks list(remove destroyed elements)
        updateBricksReference(bricks);
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
        if (model.getLives() <= 0) { model.stopGame(); } // decrease lives, if 0 lives - game over

        stuck = true; // reset ball position
        xSpeed = 0;
        ySpeed = 0;
        x = paddle.getX() + (paddle.getWidth() / 2.0) - (diameter / 2.0);
        y = paddle.getY() - diameter;
    }

    // Update bricks reference
    public void updateBricksReference(List<Brick> bricks) { bricks.removeIf(Brick::isDestroyed); }

    // Handle ball starting
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) { start(); }
    }

    // Draw a ball object
    public void paint(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillOval((int) getX(), (int) getY(), getDiameter(), getDiameter());
    }
}