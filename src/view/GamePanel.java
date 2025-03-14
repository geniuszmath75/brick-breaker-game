package view;

import model.GameModel;
import model.Paddle;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private GameModel model; // Model reference

    public GamePanel(GameModel model) {
        this.model = model;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Call parent paint method
        super.paintComponent(g);

        // Get Paddle model
        Paddle paddle = model.getPaddle();

        // Drawing paddle
        g.fillRect(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());
    }
}
