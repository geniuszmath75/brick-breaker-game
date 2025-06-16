package view;

import model.Ball;
import model.GameModel;
import model.Paddle;
import model.Brick;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GamePanel extends JPanel {
    private final GameModel model; // Model reference
    private final int startLives; // Initial number of lives

    // Heart drawing constants
    private static final int HEART_SIZE = 30;
    private static final int HEART_Y_OFFSET = 13;
    private static final int HEART_SPACING = 40;

    // GamePanel constructor
    public GamePanel(GameModel model) {
        this.model = model;
        this.startLives = model.getLives();
        setBackground(Color.BLACK); // Set black background color
    }

    // Draw a heart shape (used for representing lives)
    private void drawHeart(Graphics2D g2d, int x) {
        int[] triangleX = {x + HEART_SIZE / 2, x, x + HEART_SIZE};
        int[] triangleY = {42, 12 + HEART_SIZE / 3, 12 + HEART_SIZE / 3};

        g2d.fillOval(x, HEART_Y_OFFSET, HEART_SIZE / 2, HEART_SIZE / 2);                        // Left half of heart
        g2d.fillOval(x + HEART_SIZE / 2, HEART_Y_OFFSET, HEART_SIZE / 2, HEART_SIZE / 2);    // Right half of heart
        g2d.fillPolygon(triangleX, triangleY, 3); // Bottom triangle of heart
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable antialiasing for smother rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Retrieve game elements
        Paddle paddle = model.getPaddle();
        Ball ball = model.getBall();
        List<Brick> bricks = List.copyOf(model.getBricks());

        // Draw paddle
        paddle.paint(g2d);

        // Draw ball
        ball.paint(g2d);

        // Draw bricks
        for (Brick b : bricks) {
            if (!b.isDestroyed()) { b.paint(g2d); }
        }

        // Drawing game start information
        if (ball.isStuck() && model.isGameRunning()) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            g2d.drawString("HIT SPACE TO START", 230, 500);
            g2d.drawString("USE ARROW KEYS TO MOVE", 175, 550);
        }

        // Drawing lives (hearts)
        for (int i = 0; i < startLives; i++) {
            g2d.setColor(i < model.getLives() ? Color.RED : Color.GRAY); // Red = remaining, gray = lost
            drawHeart(g2d, 12 + (i * HEART_SPACING));
        }

        // Drawing score
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.drawString("SCORE: " + model.getScore(), 310, 40);
    }
}