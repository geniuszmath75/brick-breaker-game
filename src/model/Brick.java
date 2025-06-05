package model;

import java.awt.*;
import java.util.Random;

public class Brick {
    private final double x;               // X coordinate
    private final double y;               // Y coordinate
    private final int brickWidth;         // Brick width
    private final int brickHeight;        // Brick height
    private final Color brickColor;       // Random background color of the brick

    private final int initialDestruction; // How many hits the brick can take (1, 2, or 3)
    private int destructionLevel;         // Hits remaining before destruction

    // Brick constructor
    public Brick(double x, double y, int width, int height, int destruction) {
        this.x = x;
        this.y = y;
        this.brickWidth = width;
        this.brickHeight = height;
        this.initialDestruction = destruction;
        this.destructionLevel = destruction;
        this.brickColor = initColor();
    }

    // Generates a random color
    private Color initColor() {
        Random rand = new Random();
        return new Color(rand.nextInt(1, 256), rand.nextInt(1, 256), rand.nextInt(1, 256));
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getBrickWidth() { return brickWidth; }
    public int getBrickHeight() { return brickHeight; }

    // Decrease destructionLevel on each hit
    public void hit() { destructionLevel--; }

    public boolean isDestroyed() { return destructionLevel <= 0; }

    /**
     * Paints the brick with a dynamic overlay:
     *  - initially (destructionLevel == initialDestruction) – no overlay
     *  - as destructionLevel decreases, a semi-transparent white overlay is drawn,
     *    with alpha increasing linearly up to 1.0 (when nearly destroyed)
     */
    public void paint(Graphics2D g2d) {
        // Fill the brick background
        g2d.setColor(brickColor);
        g2d.fillRoundRect((int) x, (int) y, brickWidth, brickHeight, 25, 25);

        // Draw white border
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect((int) x, (int) y, brickWidth, brickHeight, 25, 25);

        // If brick is damaged but not destroyed, draw white overlay
        if (destructionLevel < initialDestruction && destructionLevel > 0) {
            // Calculate alpha from 0.3 (light) to 1.0 (fully overlaid)
            float ratio = (initialDestruction - destructionLevel) / (float) initialDestruction;
            float alpha = 0.3f + 0.7f * ratio;
            if (alpha > 1f) alpha = 1f;

            // Set compositing and draw semi-transparent white overlay over the brick
            Composite oldComp = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(Color.BLACK);
            g2d.fillRoundRect((int) x, (int) y, brickWidth, brickHeight, 25, 25);
            g2d.setComposite(oldComp);
        }

        // Draw destruction lines based on destructionLevel
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));

        int margin = 5;
        int x1 = (int) x + margin;
        int y1 = (int) y + margin;
        int x2 = (int) x + brickWidth - margin;
        int y2 = (int) y + brickHeight - margin;

        switch (destructionLevel) {
            case 2 -> g2d.drawLine(x1, y1, x2, y2);
            case 1 -> {
                g2d.drawLine(x1, y1, x2, y2);
                g2d.drawLine(x2, y1, x1, y2);
            }
            default -> {}
        }
    }
}