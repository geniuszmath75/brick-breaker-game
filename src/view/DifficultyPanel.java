package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DifficultyPanel extends JPanel {
    private final JButton easyModeButton; // Selects easy mode
    private final JButton mediumModeButton; // Selects medium mode
    private final JButton hardModeButton; // Selects hard mode
    private final JButton backButton; // Returns to previous panel

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

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(easyModeButton);
        buttonPanel.add(mediumModeButton);
        buttonPanel.add(hardModeButton);

        // Panel for "return" button
        JPanel backPanel = new JPanel();
        backPanel.add(backButton);

        // Add components to panel
        add(difficultyLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(backPanel, BorderLayout.SOUTH);
    }

    // Adds a listener for the EASY button
    public void addEasyListener(ActionListener listener) {
        easyModeButton.addActionListener(listener);
    }

    // Adds a listener for the MEDIUM button
    public void addMediumListener(ActionListener listener) {
        mediumModeButton.addActionListener(listener);
    }

    // Adds a listener for the HARD button
    public void addHardListener(ActionListener listener) {
        hardModeButton.addActionListener(listener);
    }

    // Adds a listener for the BACK button
    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}