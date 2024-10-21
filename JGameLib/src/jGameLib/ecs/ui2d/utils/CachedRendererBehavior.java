package jGameLib.ecs.ui2d.utils;

import jGameLib.ecs.ui2d.GameWindow;
import jGameLib.ecs.ui2d.JGraphics;
import jGameLib.ecs.ui2d.rendering.UIRendererComponent;
import jGameLib.math.Vec2;
import jGameLib.math.Vec2i;
import jGameLib.util.Cache;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

/**
 * A {@link UIRendererComponent} that caches its results, used for components whose draw method is very expensive. Make
 * sure to turn on {@link GameWindow#enableHardwareAcceleration()} for maximum performance when using this class, as it
 * enables images to be cached in video memory.
 */
public abstract class CachedRendererBehavior<T> extends UIRendererComponent {
    private static final Cache<Class<? extends CachedRendererBehavior<?>>, ImageCache<?>> cache = new Cache<>();

    // This method does some unchecked casting due to generics.
    @SuppressWarnings("unchecked")
    private ImageCache<T> getImageCacheForClass() {
        Class<CachedRendererBehavior<T>> cls = (Class<CachedRendererBehavior<T>>) getClass();
        return (ImageCache<T>) cache.getOrCompute(cls, () -> new ImageCache<>(this::createCache));
    }

    // Need this functional interface to pass lambdas to the ImageCache
    private interface DrawFunction {
        void draw(Vec2 size, JGraphics graphics);
    }

    private static class ImageCache<T> {
        private final Cache<Vec2i, Cache<T, Image>> cache = new Cache<>();
        private final Supplier<Cache<T, Image>> cacheSupplier;

        private ImageCache(Supplier<Cache<T, Image>> cacheSupplier) {
            this.cacheSupplier = cacheSupplier;
        }

        public Image get(Vec2i size, T key, DrawFunction drawFunction) {
            return cache
                .getOrCompute(size, cacheSupplier)
                .getOrCompute(key, () -> generateImage(size, drawFunction));
        }

        private Image generateImage(Vec2i imageSize, DrawFunction drawFunction) {
            Image image = new BufferedImage(imageSize.x, imageSize.y, BufferedImage.TYPE_INT_ARGB);
            Vec2 size = new Vec2(imageSize);
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.translate(imageSize.x / 2.0, imageSize.y / 2.0);
            drawFunction.draw(size, new JGraphics(graphics, size));

            return image;
        }
    }

    /**
     * Draws the component to an image to be cached. Note that you must draw the component centered in the image.
     *
     * @param size     The size of the image
     * @param graphics The graphics object. (0, 0) is the center of the image.
     */
    protected abstract void draw(Vec2 size, JGraphics graphics);

    /**
     * Get the key to be used in the image cache. When the key matches a key already in the cache, the cached image is
     * used and no new image is generated. Ideally, this should take on a relatively small number of values for caching
     * to be most effective. You do not need to include the bounding box size in this method; the cache image size
     * (which is the same as the bounding box size, unless {@link CachedRendererBehavior#getCacheImageSize()} is
     * overridden) is also included in caching.
     */
    protected abstract T getCacheKey();

    /**
     * Supplies a new cache to be used in the image cache. You may override this method if your keys are better suited to a different type of cache
     * than the default, which is backed by a HashMap
     */
    protected Cache<T, Image> createCache() {
        return new Cache<>();
    }

    /**
     * If the rendered result is not the same size as the bounding box, you may override this method to change the
     * cached image size. By default, this is just the size of the bounding box.
     */
    protected Vec2i getCacheImageSize() {
        return new Vec2i(boundingBox.getSize());
    }

    @Override
    public final void draw(JGraphics graphics) {
        ImageCache<T> cache = getImageCacheForClass();
        Vec2i cacheImageSize = getCacheImageSize();
        T key = getCacheKey();
        Image img = cache.get(cacheImageSize, key, this::draw);

        graphics.drawImage(img,
            boundingBox.getAbsolutePosition(),
            new Vec2(cacheImageSize)
        );
    }
}
