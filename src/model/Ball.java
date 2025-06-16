package model;

import utils.SoundLoader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class Ball {
    private double x; // X coordinate
    private double y; // Y coordinate
    private final int diameter; // Ball diameter
    private double xSpeed = 0.0; // Speed on X axis
    private double ySpeed = 0.0; // Speed on Y axis
    private final int speed; // General speed
    private final GameModel model; // Game model reference
    private final Paddle paddle; // Paddle reference
    private final List<Brick> bricks; // Bricks reference
    private boolean stuck; // Ball is stuck to paddle

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

    public double getX() { return x; }
    public double getY() { return y; }
    public int getDiameter() { return diameter; }
    public boolean isStuck() { return stuck; }

    /**
     * Moves the ball one step based on current speed.
     * Handles sticking to paddle, game over condition, wall and paddle collision.
     */
    public void move() {
        if (stuck) {
            x = paddle.getX() + (paddle.getWidth() / 2.0) - (diameter / 2.0);
            y = paddle.getY() - diameter;
            return;
        }

        // Ball has fallen below the paddle
        if (getY() >= paddle.getY() + paddle.getHeight()) {
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
            reflectFrom(paddleRect, true);
        }
    }

    // Handles collision with walls (left, right, top)
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
     * Reflects the ball from a surface.
     * If angled = true → paddle bounce with angle.
     * If angled = false → standard reflection (bricks).
     */
    private void reflectFrom(Rectangle surface, boolean angled) {
        if (angled) {
            // Calculate bounce angle based on collision point with the paddle
            double bounceAngle = getBounceAngle(surface);
            double totalSpeed = Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);
            xSpeed = totalSpeed * Math.sin(bounceAngle);
            ySpeed = -totalSpeed * Math.cos(bounceAngle);
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
     * Calculates the bounce angle from paddle based on hit position.
     */
    private double getBounceAngle(Rectangle surface) {
        double surfaceCenter = surface.getX() + surface.getWidth() / 2.0;
        double ballCenter = x + diameter / 2.0;

        // Calculate how much from the paddle center the ball has hit (-1.0 to 1.0)
        double relativeIntersect = (ballCenter - surfaceCenter) / (surface.getWidth() / 2.0);

        // Clamp to [-1.0, 1.0]
        relativeIntersect = Math.max(-1.0, Math.min(1.0, relativeIntersect));

        // Max angle of reflection: 75deg (in radians)
        double maxBounceAngle = Math.toRadians(65);
        return relativeIntersect * maxBounceAngle;
    }

    /**
     * Checks for collision with bricks and reflects if hit.
     * Also increases score and checks level completion.
     */
    public void checkCollision() {
        Rectangle ballRect = new Rectangle((int) x, (int) y, diameter, diameter);

        for(Brick b : bricks) {
            Rectangle brickRect = new Rectangle((int) b.getX(), (int) b.getY(), b.getBrickWidth(), b.getBrickHeight());
            // Check if ball intersects with brick
            if (ballRect.intersects(brickRect)) {
                SoundLoader.playWAV("/sounds/ring.wav");
                b.hit(); // Collision detected
                model.increaseScore(); // Add points

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

    // Start ball movement if stuck
    public void start() {
        if (stuck) {
            stuck = false;
            xSpeed = speed; // Ball starts moving
            ySpeed = -speed;
        }
    }

    // Reset ball position and speed after falling below paddle
    public void reset() {
        model.setLives(model.getLives() - 1);
        if (model.getLives() <= 0) { model.stopGame(); } // decrease lives, if 0 lives - game over

        stuck = true; // reset ball position
        xSpeed = 0;
        ySpeed = 0;
        x = paddle.getX() + (paddle.getWidth() / 2.0) - (diameter / 2.0);
        y = paddle.getY() - diameter;
    }

    // Removes destroyed bricks from the list
    public void updateBricksReference(List<Brick> bricks) { bricks.removeIf(Brick::isDestroyed); }

    // Handles spacebar to start the ball
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) { start(); }
    }

    // Draw a ball object
    public void paint(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillOval((int) getX(), (int) getY(), getDiameter(), getDiameter());
    }
}