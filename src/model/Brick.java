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

    // Decrease destructionLevel by 1 in hit
    public void hit() { destructionLevel--; }

    // Returns true if the brick has no hits remaining
    public boolean isDestroyed() { return destructionLevel <= 0; }

    /**
     * Paints the brick with a dynamic overlay:
     *  - when fully intact (destructionLevel == initialDestruction): no overlay
     *  - when partially damaged: darker overlay appears with increasing opacity
     *  - when nearly destroyed: two white cross-lines are drawn
     */
    public void paint(Graphics2D g2d) {
        // Fill the brick background
        g2d.setColor(brickColor);
        g2d.fillRoundRect((int) x, (int) y, brickWidth, brickHeight, 25, 25);

        // Draw white border
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect((int) x, (int) y, brickWidth, brickHeight, 25, 25);

        // If brick is damaged but not destroyed, draw black overlay
        if (destructionLevel < initialDestruction && destructionLevel > 0) {
            // Calculate alpha from 0.3 (light) to 1.0 (fully overlaid)
            float ratio = (initialDestruction - destructionLevel) / (float) initialDestruction;
            float alpha = 0.3f + 0.7f * ratio;

            // Apply transparency and draw overlay
            Composite oldComp = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(Color.BLACK);
            g2d.fillRoundRect((int) x, (int) y, brickWidth, brickHeight, 25, 25);
            g2d.setComposite(oldComp);
        }

        // Draw white diagonal lines if partially or critically damaged
        if(destructionLevel <= 2 && destructionLevel > 0) {
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));

            int margin = 5;
            int x1 = (int) x + margin;
            int y1 = (int) y + margin;
            int x2 = (int) x + brickWidth - margin;
            int y2 = (int) y + brickHeight - margin;

            g2d.drawLine(x1, y1, x2, y2); // First diagonal

            if(destructionLevel == 1) {
                g2d.drawLine(x2, y1, x1, y2); // Second diagonal when almost destroyed
            }
        }
    }
}