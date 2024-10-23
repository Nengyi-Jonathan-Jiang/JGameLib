package jGameLib.ecs.ui2d.utils;

import jGameLib.ecs.GameState;
import jGameLib.ecs.ui2d.rendering.UIEntity;
import jGameLib.util.ImageLoader;

import java.awt.*;

public class ImageEntity extends UIEntity {
    private final ImageRendererComponent imageRendererComponent;

    public ImageEntity(GameState state, String filename) {
        this(state, ImageLoader.get(filename));
    }

    public ImageEntity(GameState state, Image image) {
        super(state);
        addComponent(imageRendererComponent = new ImageRendererComponent(image));
    }

    public Image getImage() {
        return imageRendererComponent.getImage();
    }
}
