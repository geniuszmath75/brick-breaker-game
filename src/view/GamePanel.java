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

    // GamePanel constructor
    public GamePanel(GameModel model) {
        this.model = model;
        this.startLives = model.getLives();
        setBackground(Color.BLACK); // set black background color
    }

    // Draw heart shape
    private void drawHeart(Graphics2D g2d, int x) {
        int[] triangleX = {x + 30 / 2, x, x + 30};
        int[] triangleY = {42, 12 + 30 / 3, 12 + 30 / 3};

        g2d.fillOval(x, 13, 30 / 2, 30 / 2); // left half of heart
        g2d.fillOval(x + 30 / 2, 13, 30 / 2, 30 / 2); // right half of heart
        g2d.fillPolygon(triangleX, triangleY, 3); // triangle down part of heart
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Get Paddle model
        Paddle paddle = model.getPaddle();

        // Get Ball model
        Ball ball = model.getBall();

        // We take the original list and make a copy of it
        List<Brick> bricks = model.getBricks();
        List<Brick> bricksCopy = new java.util.ArrayList<>(bricks);

        // Drawing paddle
        paddle.paint(g2d);

        // Drawing ball
        ball.paint(g2d);

        // Drawing bricks
        for (Brick b : bricksCopy) {
            if (!b.isDestroyed()) { b.paint(g2d); }
        }

        // Drawing start information text
        if (ball.isStuck() && model.isGameRunning()) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            g2d.drawString("HIT SPACE TO START", 230, 500);
            g2d.drawString("USE ARROW KEYS TO MOVE", 175, 550);
        }

        // Drawing lives
        for (int i = 0; i < startLives; i++) {
            if (i < model.getLives()) { // red hearts for remaining lives
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.GRAY); // gray hearts for lost lives
            }
            drawHeart(g2d, 12 + (i * 40));
        }

        // Drawing score
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.drawString("SCORE: " + model.getScore(), 310, 40);
    }
}