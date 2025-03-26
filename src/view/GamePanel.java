package view;

import model.Ball;
import model.GameModel;
import model.Paddle;
import model.Brick;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final GameModel model; // Model reference
    private boolean gameOverDisplayed = false;

    // GamePanel constructor
    public GamePanel(GameModel model) {
        this.model = model;
        setBackground(Color.BLACK); // set black background color
    }

    // Draw heart shape
    private void drawHeart(Graphics g, int x) {
        int[] triangleX = { x + 30 / 2, x, x + 30};
        int[] triangleY = { 42, 12 + 30 / 3, 12 + 30 / 3 };

        g.fillOval(x, 13, 30 / 2, 30 / 2); // left half of heart
        g.fillOval(x + 30 / 2, 13, 30 / 2, 30 / 2); // right half of heart
        g.fillPolygon(triangleX, triangleY, 3); // triangle down part of heart
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Call parent paint method
        super.paintComponent(g);

        // Get Paddle model
        Paddle paddle = model.getPaddle();

        // Get Ball model
        Ball ball = model.getBall();

        // Drawing bricks
        Brick brick = model.getBrick();

        // Drawing paddle
        g.setColor(Color.WHITE); // Set white paddle color
        g.fillRoundRect(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight(), 15, 15);

        // Drawing ball
        g.setColor(Color.WHITE);
        g.fillOval(ball.getX(), ball.getY(), ball.getDiameter(), ball.getDiameter());

        // Drawing bricks
        int[][] map = brick.getMap();
        int brickWidth = brick.getBrickWidth();
        int brickHeight = brick.getBrickHeight();
        Color[][] brickColors = brick.getBrickColors();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    g.setColor(brickColors[i][j]);
                    g.fillRoundRect(j * brickWidth, i * brickHeight + 70, brickWidth, brickHeight, 25, 25); // Draw brick

                    Graphics2D g2 = (Graphics2D) g;
                    g2.setStroke(new BasicStroke(3)); // Set brick border width
                    g2.setColor(Color.BLACK);
                    g2.drawRoundRect(j * brickWidth, i * brickHeight + 70, brickWidth, brickHeight, 25, 25); // Draw brick border
                }
            }
        }

        // Drawing start information text
        if (ball.isStuck() && model.isGameRunning()) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("HIT SPACE TO START", 235, 300);
            g.drawString("USE ARROW KEYS TO MOVE", 180, 350);
        }

        // Drawing lives
        for (int i = 0; i < 3; i++) {
            if (i < model.getLives()) { // red hearts for remaining lives
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.GRAY); // gray hearts for lost lives
            }
            drawHeart(g, 12 + (i * 40));
        }

        // Drawing score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("SCORE: " + model.getScore(), 310, 40);

        // Drawing game over text
        if (!model.isGameRunning()) {
            model.setLives(0); // Set lives to 0 to prevent further drawing and hide ball
            g.setColor(Color.BLACK);
            g.fillOval(ball.getX(), ball.getY(), ball.getDiameter(), ball.getDiameter());

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.BLACK);
            g2.drawOval(ball.getX(), ball.getY(), ball.getDiameter(), ball.getDiameter()); // Draw brick border

            if (!gameOverDisplayed) {
                gameOverDisplayed = true;

                int option = JOptionPane.showOptionDialog(
                        this,
                        "Do you want to restart the game or go back to the menu?",
                        "GAME OVER",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        new String[]{"RESTART", "MENU"},
                        "RESTART"
                );

                if (option == JOptionPane.YES_OPTION) { // Restart the game, score and lives
                    model.renewGame();
                    model.setScore(0);
                    model.setLives(3);
                } else {
                    GameView parentView = (GameView) SwingUtilities.getWindowAncestor(this); // Get parent view
                    parentView.returnToMenu(); // Return to the menu
                }
                gameOverDisplayed = false;
            }
        }
    }
}