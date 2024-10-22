package jGameLib.ecs.ui2d.utils;

import jGameLib.ecs.ui2d.rendering.UIEntity;
import jGameLib.util.ImageLoader;

import java.awt.*;

public class ImageEntity extends UIEntity {
    private final ImageRendererComponent imageRendererComponent;

    public ImageEntity(String filename) {
        this(ImageLoader.get(filename));
    }

    public ImageEntity(Image image) {
        super();
        addComponent(imageRendererComponent = new ImageRendererComponent(image));
    }

    public Image getImage() {
        return imageRendererComponent.getImage();
    }
}
