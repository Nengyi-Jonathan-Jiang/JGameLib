package jGameLib.util.files;

import jGameLib.util.caches.Cache;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;

/**
 * Utility class to load images from files
 */
public final class ImageLoader {
    private final static Cache<String, Image> imageCache = new Cache<>();

    private ImageLoader() {

    }

    /**
     * Gets an image by filename. Images are cached, so no image is loaded twice
     */
    public static Image get(String filename) {
        return imageCache.getOrCompute(filename, ImageLoader::load);
    }

    /**
     * Loads a file into the class and returns it
     *
     * @param filename The location of the image file
     */
    private static Image load(String filename) {
        try (InputStream inputStream = FileUtil.readFileAsStream(filename)) {
            Image image = ImageIO.read(inputStream);
            System.out.println("Loaded image \"" + filename + "\"");
            return image;
        } catch (Exception e) {
            System.out.println("Could not load image \"" + filename + "\"");
            e.printStackTrace();
        }
        return null;
    }
}