package view;

import model.Ball;
import model.GameModel;
import model.Paddle;

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

        // Drawing ball
        g.setColor(Color.WHITE);
        g.fillOval(ball.getX(), ball.getY(), ball.getDiameter(), ball.getDiameter());

        // Drawing paddle
        g.setColor(Color.WHITE); // Set white paddle color
        g.fillRoundRect(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight(), 15, 15);

        // Drawing information text
        if (ball.isStuck()) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("HIT SPACE TO START", 280, 50);
        }
    }
}
