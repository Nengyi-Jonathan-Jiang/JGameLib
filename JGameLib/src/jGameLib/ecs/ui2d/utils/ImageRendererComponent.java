package jGameLib.ecs.ui2d.utils;

import jGameLib.ecs.ui2d.JGraphics;
import jGameLib.ecs.ui2d.rendering.BoundingBoxComponent;
import jGameLib.ecs.ui2d.rendering.UIRendererComponent;
import jGameLib.math.Vec2;
import jGameLib.util.ImageLoader;

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
            entity.getComponent(BoundingBoxComponent.class).getAbsolutePosition(),
            entity.getComponent(BoundingBoxComponent.class).getSize()
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