package jGameLib.ui2d;

import jGameLib.util.math.Vec2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public record JGraphics(Graphics2D originalGraphics, Vec2 targetSize) {
    public JGraphics(Graphics2D originalGraphics, Vec2 targetSize) {
        this.originalGraphics = originalGraphics;
        this.targetSize = targetSize;

        // Set some things

        // Prevent jagged text (slower)
        originalGraphics.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        // Prevent jagged shapes (slower)
        originalGraphics.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Use the highest image quality
        originalGraphics.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );

        // Use the highest render quality
        originalGraphics.setRenderingHint(
            RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY
        );

        // Use 2 px wide stroke for better legibility
        originalGraphics.setStroke(new BasicStroke(2));
    }

    public JGraphics setColor(int r, int g, int b) {
        return setColor(r / 255., g / 255., b / 255., 1.0);
    }

    public JGraphics setColor(int r, int g, int b, float a) {
        return setColor(new Color(r, g, b, a * 255));
    }

    public JGraphics setColor(float r, float g, float b) {
        return setColor(r, g, b, 1.0);
    }

    public JGraphics setColor(double r, double g, double b, double a) {
        return setColor(new Color((float) r, (float) g, (float) b, (float) a));
    }

    public JGraphics setColor(Color color) {
        if (color == null) color = new Color(0, 0, 0, 0);
        originalGraphics.setColor(color);
        return this;
    }

    /**
     * Draws an image with the given center and size
     */
    public JGraphics drawImage(Image img, Vec2 position, Vec2 size) {
        // One would think that calling drawRotatedImage is inefficient because there is no need for transformations. 
        // However, due to Java AWT weirdness, we actually do need to apply a transform matrix anyway to achieve 
        // sub-pixel accuracy when drawing images. 
        return drawRotatedImage(img, position, size, 0);
    }

    /**
     * Draws a rotated image with the given center, size, and rotation in radians
     */
    public JGraphics drawRotatedImage(Image img, Vec2 position, Vec2 size, double rotation) {
        return drawRotatedImage(img, position.x, position.y, size.x, size.y, rotation);
    }

    private JGraphics drawRotatedImage(Image img, double x, double y, double width, double height, double rotation) {
        // Save transform
        AffineTransform originalTransform = originalGraphics.getTransform();

        // Apply transformations
        originalGraphics.translate(x, y);
        int imgWidth = img.getWidth(null);
        int imgHeight = img.getHeight(null);
        originalGraphics.scale(width / imgWidth, height / imgHeight);
        originalGraphics.rotate(rotation);
        originalGraphics.translate(-imgWidth / 2., -imgHeight / 2.);
        originalGraphics.drawImage(img, 0, 0, null);

        // Reset transform
        originalGraphics.setTransform(originalTransform);

        return this;
    }

    public JGraphics drawRect(Vec2 position, Vec2 size) {
        return drawRect(position, size.x(), size.y());
    }

    public JGraphics drawRect(Vec2 position, double width, double height) {
        return drawRect(position.x(), position.y(), width, height);
    }

    public JGraphics drawRect(double x, double y, Vec2 size) {
        return drawRect(x, y, size.x(), size.y());
    }

    public JGraphics drawRect(double x, double y, double width, double height) {
        originalGraphics.draw(new Rectangle2D.Double(x, y, width, height));
        return this;
    }

    public JGraphics clear() {
        return fillRect(targetSize.times(-0.5), targetSize);
    }

    public JGraphics fillRect(Vec2 position, Vec2 size) {
        return fillRect(position, size.x(), size.y());
    }

    public JGraphics fillRect(Vec2 position, double width, double height) {
        return fillRect(position.x(), position.y(), width, height);
    }

    public JGraphics fillRect(double x, double y, Vec2 size) {
        return fillRect(x, y, size.x(), size.y());
    }

    public JGraphics fillRect(double x, double y, double width, double height) {
        originalGraphics.fill(new Rectangle2D.Double(x, y, width, height));
        return this;
    }

    public JGraphics setDrawFont(Font font) {
        originalGraphics.setFont(font);
        return this;
    }

    public JGraphics drawText(String text, Vec2 position) {
        return drawText(text, position.x(), position.y());
    }

    public JGraphics drawText(String text, double x, double y) {
        originalGraphics.drawString(text, (float) x, (float) y);
        return this;
    }
}
