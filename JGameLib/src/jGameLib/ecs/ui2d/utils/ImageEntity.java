package jGameLib.ecs.ui2d.utils;

import jGameLib.ecs.ui2d.rendering.UIEntity;
import jGameLib.util.ImageLoader;

import java.awt.*;

public class ImageEntity extends UIEntity {
    private final ImageRendererBehavior imageRendererBehavior;

    public ImageEntity(String filename) {
        this(ImageLoader.get(filename));
    }

    public ImageEntity(Image image) {
        super();
        addComponent(imageRendererBehavior = new ImageRendererBehavior(image));
    }

    public Image getImage() {
        return imageRendererBehavior.getImage();
    }
}
