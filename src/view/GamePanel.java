package view;

import model.Ball;
import model.GameModel;
import model.Paddle;
import model.Brick;
import utils.FontLoader;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final GameModel model; // Model reference

    public GamePanel(GameModel model) {
        this.model = model;
        setBackground(Color.BLACK); // set black background color
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Call parent paint method
        super.paintComponent(g2);

        // Get Paddle model
        Paddle paddle = model.getPaddle();

        // Get Ball model
        Ball ball = model.getBall();

        // Drawing bricks
        Brick brick = model.getBrick();

        // Drawing paddle
        g2.setColor(Color.WHITE); // Set white paddle color
        g2.fillRoundRect(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight(), 15, 15);

        // Drawing ball
        g2.setColor(Color.WHITE);
        g2.fillOval(ball.getX(), ball.getY(), ball.getDiameter(), ball.getDiameter());

        // Drawing bricks
        brick.draw(g2);

        // Drawing start information text
        if (ball.isStuck() && model.isGameRunning()) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 30));
            g2.drawString("HIT SPACE TO START", 235, 300);
            g2.drawString("USE ARROW KEYS TO MOVE", 180, 350);
        }

        // Drawing lives
        for (int i = 0; i < 3; i++) {
            if (i < ball.getLives()) { // Draw white circles for remaining lives
                g2.setColor(Color.WHITE);
            } else {
                g2.setColor(Color.RED); // Draw red circles for lost lives
            }
            g2.fillOval(12 + (i * 40), 12, 30, 30);
            g2.setColor(Color.BLACK);
            g2.drawOval(12 + (i * 40), 12, 30, 30);
        }

        // Drawing score
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString("SCORE: " + brick.getScore(), 310, 40);

        // Drawing game over text
        if (!model.isGameRunning()) {
            g2.setColor(Color.WHITE);
            g2.setFont(FontLoader.loadFont("fonts/DominoBrick-aYy39.ttf", 52));
            g2.drawString("GAME OVER", 260, 350);

            ball.setLives(0); // Set lives to 0 to prevent further drawing
            g2.setColor(Color.BLACK);
            g2.fillOval(ball.getX(), ball.getY(), ball.getDiameter(), ball.getDiameter());
        }
    }
}