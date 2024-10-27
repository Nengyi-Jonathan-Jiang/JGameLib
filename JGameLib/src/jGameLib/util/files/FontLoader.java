package jGameLib.util.files;

import jGameLib.util.caches.Cache;

import java.awt.*;
import java.io.IOException;

/**
 * Utility class to load fonts
 */
public final class FontLoader {
    private static final Cache<String, Font> cache = new Cache<>();

    public static final Font DefaultFont = new Font("Times New Roman", Font.PLAIN, 12);

    /**
     * Loads a .ttf font
     *
     * @param filename The location of the font file
     */
    public static Font load(String filename) {
        return cache.getOrCompute(filename, () -> {
            try {
                return Font.createFont(
                    Font.TRUETYPE_FONT,
                    FileUtil.readFileAsStream(filename)
                ).deriveFont(Font.PLAIN, 12);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
                return DefaultFont;
            }
        });
    }
}