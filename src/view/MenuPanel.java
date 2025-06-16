package view;

import utils.FontLoader;
import utils.SoundLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    private final JButton startButton; // Button to start the game
    private final JButton statsButton;  // Button to see stats
    private final JButton exitButton;  // Button to exit the game

    // Creates the main menu layout
    public MenuPanel() {
        setLayout(new BorderLayout());

        // Game title font
        Font titleFont = FontLoader.loadFont("fonts/DominoBrick-aYy39.ttf", 36);

        // Game title label
        JLabel titleLabel = new JLabel("Brick Breaker", SwingConstants.CENTER);
        titleLabel.setFont(titleFont); // Apply title font

        // Initialize buttons
        startButton = new JButton("START");
        statsButton = new JButton("STATS");
        exitButton = new JButton("EXIT");

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(statsButton);
        buttonPanel.add(exitButton);

        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);  // Title at the top
        add(buttonPanel, BorderLayout.CENTER); // Buttons in the center
    }

    // Adds a listener for the START button
    public void addStartListener(ActionListener listener) {
        addButtonListener(startButton, listener);
    }

    // Adds a listener for the STATS button
    public void addStatsListener(ActionListener listener) {
        addButtonListener(statsButton, listener);
    }

    // Adds a listener for the EXIT button
    public void addExitListener(ActionListener listener) {
        addButtonListener(exitButton, listener);
    }

    // Adds a listener to a button with sound effect
    private void addButtonListener(JButton button, ActionListener listener) {
        button.addActionListener(e -> {
            SoundLoader.playWAV("/sounds/crash.wav");
            listener.actionPerformed(e);
        });
    }
}