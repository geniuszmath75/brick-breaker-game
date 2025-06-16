package view;

import utils.SoundLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class LevelSelectPanel extends JPanel {
    private final JButton level1Button; // Select game level 1
    private final JButton level2Button; // Select game level 2
    private final JButton level3Button; // Select game level 3
    private final JButton backButton; // Returns to previous panel

    public LevelSelectPanel() {
        setLayout(new BorderLayout());

        // Panel title font
        Font titleFont = new Font("Arial", Font.BOLD, 36);

        // Panel title label
        JLabel levelLabel = new JLabel("Level", SwingConstants.CENTER);
        levelLabel.setFont(titleFont);

        // Initialize buttons
        level1Button = new JButton("1");
        level1Button.setActionCommand("1");
        level2Button = new JButton("2");
        level2Button.setActionCommand("2");
        level3Button = new JButton("3");
        level3Button.setActionCommand("3");

        backButton = new JButton("Back");

        // Panel for level buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(level1Button);
        buttonPanel.add(level2Button);
        buttonPanel.add(level3Button);

        // Panel for back button
        JPanel backPanel = new JPanel();
        backPanel.add(backButton);

        // Add components to panel
        add(levelLabel, BorderLayout.NORTH);    // Title at the top
        add(buttonPanel, BorderLayout.CENTER);  // Level buttons in the center
        add(backPanel, BorderLayout.SOUTH);     // Back button at the bottom
    }

    // Add a listener to all level buttons
    public void addLevelListener(ActionListener listener) {
        ActionListener wrapper = e -> {
            SoundLoader.playWAV("/sounds/crash.wav");
            listener.actionPerformed(e);
        };
        level1Button.addActionListener(wrapper);
        level2Button.addActionListener(wrapper);
        level3Button.addActionListener(wrapper);
    }

    // Add a listener for the BACK button
    public void addBackListener(ActionListener listener) {
        addButtonListener(backButton, listener);
    }

    // Adds a listener to a button with sound effect
    private void addButtonListener(JButton button, ActionListener listener) {
        button.addActionListener(e -> {
            SoundLoader.playWAV("/sounds/crash.wav");
            listener.actionPerformed(e);
        });
    }
}