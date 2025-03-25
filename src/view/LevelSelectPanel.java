package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class LevelSelectPanel extends JPanel {
    private final JButton levelButton; // Select game level
    private final JButton backButton; // Returns to previous panel

    public LevelSelectPanel() {
        setLayout(new BorderLayout());

        // Panel title font
        Font titleFont = new Font("Arial", Font.BOLD, 36);

        // Panel title label
        JLabel levelLabel = new JLabel("Level", SwingConstants.CENTER);
        levelLabel.setFont(titleFont);

        // Initialize button
        levelButton = new JButton("1");
        backButton = new JButton("Back");

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(levelButton);

        // Panel for "return" button
        JPanel backPanel = new JPanel();
        backPanel.add(backButton);

        // Add components to panel
        add(levelLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(backPanel, BorderLayout.SOUTH);
    }

    // Add a listener for the level name button
    public void addLevelListener(ActionListener listener) {
        levelButton.addActionListener(listener);
    }

    // Add a listener for the BACK button
    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}