package jGameLib.ui2d.utils;

import jGameLib.ui2d.JGraphics;
import jGameLib.ui2d.rendering.BoundingBoxComponent;
import jGameLib.ui2d.rendering.UIRendererComponent;
import jGameLib.util.math.Vec2;
import jGameLib.util.files.ImageLoader;

import java.awt.*;

/**
 * Allows the gameObject to draw an image
 */
@SuppressWarnings("unused")
public class ImageRendererComponent extends UIRendererComponent {
    protected Image image;

    public ImageRendererComponent(Image image) {
        this.image = image;
    }

    public ImageRendererComponent(String image) {
        this(ImageLoader.get(image));
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public void draw(JGraphics graphics) {
        if (image != null) graphics.drawImage(
            image,
            boundingBox.getAbsolutePosition(),
            boundingBox.getSize()
        );
    }

    public Vec2 getImageSize() {
        return new Vec2(image.getWidth(null), image.getHeight(null));
    }

    public Image getImage() {
        return image;
    }

    public double getAspectRatio() {
        return 1.0 * image.getHeight(null) / image.getWidth(null);
    }
}