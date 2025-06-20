package view;

import utils.SoundLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DifficultyPanel extends JPanel {
    private final JButton easyModeButton;   // Selects easy mode
    private final JButton mediumModeButton; // Selects medium mode
    private final JButton hardModeButton;   // Selects hard mode
    private final JButton backButton;       // Returns to previous panel

    public DifficultyPanel() {
        setLayout(new BorderLayout());

        // Panel title font
        Font titleFont = new Font("Arial", Font.BOLD, 36);

        // Panel title label
        JLabel difficultyLabel = new JLabel("Difficulty", SwingConstants.CENTER);
        difficultyLabel.setFont(titleFont); // Apply title font

        // Initialize buttons
        easyModeButton = new JButton("EASY");
        mediumModeButton = new JButton("MEDIUM");
        hardModeButton = new JButton("HARD");
        backButton = new JButton("Back");

        // Panel for difficulty buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(easyModeButton);
        buttonPanel.add(mediumModeButton);
        buttonPanel.add(hardModeButton);

        // Panel for back button
        JPanel backPanel = new JPanel();
        backPanel.add(backButton);

        // Add components to panel
        add(difficultyLabel, BorderLayout.NORTH);   // Title at the top
        add(buttonPanel, BorderLayout.CENTER);      // Difficulty buttons in the center
        add(backPanel, BorderLayout.SOUTH);         // Back button at the bottom
    }

    // Adds a listener for the EASY button
    public void addEasyListener(ActionListener listener) {
        addButtonListener(easyModeButton, listener);
    }

    // Adds a listener for the MEDIUM button
    public void addMediumListener(ActionListener listener) {
        addButtonListener(mediumModeButton, listener);
    }

    // Adds a listener for the HARD button
    public void addHardListener(ActionListener listener) {
        addButtonListener(hardModeButton, listener);
    }

    // Adds a listener for the BACK button
    public void addBackListener(ActionListener listener) {
        addButtonListener(backButton, listener);
    }

    // Adds a listener to a button with sound effect
    public void addButtonListener(JButton button, ActionListener listener) {
        button.addActionListener(e -> {
            SoundLoader.playWAV("/sounds/crash.wav");
            listener.actionPerformed(e);
        });
    }
}