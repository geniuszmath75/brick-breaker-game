package utils;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {
    // Loads font from file with specified size
    public static Font loadFont(String path, float size) {
        try {
            InputStream fontStream = FontLoader.class.getClassLoader().getResourceAsStream(path);
            // Throw open file error
            if (fontStream == null) {
                throw new IOException("Font file not found: " + path);
            }

            // Create font
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            return font.deriveFont(size);
        } catch (IOException | FontFormatException e) {
            // Catch other exceptions
            e.printStackTrace();
            // Set default font
            return new Font("Arial", Font.BOLD, (int) size);
        }
    }
}
