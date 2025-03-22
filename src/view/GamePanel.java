package view;

import model.Ball;
import model.GameModel;
import model.Paddle;
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
        // Call parent paint method
        super.paintComponent(g);

        // Get Paddle model
        Paddle paddle = model.getPaddle();

        // Get Ball model
        Ball ball = model.getBall();

        // Drawing paddle
        g.setColor(Color.WHITE); // Set white paddle color
        g.fillRoundRect(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight(), 15, 15);

        // Drawing ball
        g.setColor(Color.WHITE);
        g.fillOval(ball.getX(), ball.getY(), ball.getDiameter(), ball.getDiameter());

        // Drawing start information text
        if (ball.isStuck()) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("HIT SPACE TO START", 280, 50);
        }

        // Drawing lives
        for (int i = 0; i < 3; i++) {
            if (i < ball.getLives()) { // Draw white circles for remaining lives
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.RED); // Draw red circles for lost lives
            }
            g.fillOval(10 + (i * 30), 10, 20, 20);
            g.setColor(Color.BLACK);
            g.drawOval(10 + (i * 30), 10, 20, 20);
        }

        // Drawing game over text
        if (!model.isGameRunning()) {
            g.setColor(Color.WHITE);
            g.setFont(FontLoader.loadFont("fonts/DominoBrick-aYy39.ttf", 52));
            g.drawString("GAME OVER", 260, 200);
        }
    }
}
