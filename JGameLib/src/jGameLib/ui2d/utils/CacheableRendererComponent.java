package jGameLib.ui2d.utils;

import jGameLib.ui2d.JGraphics;
import jGameLib.ui2d.GameWindow;
import jGameLib.ui2d.rendering.HasBoundingBox;
import jGameLib.util.math.Vec2;
import jGameLib.util.math.Vec2i;
import jGameLib.util.caches.Cache;
import jGameLib.ui2d.rendering.UIRendererComponent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;


/**
 * <p>
 * A utility to facilitate caching {@link UIRendererComponent UIRendererComponents}' draw results as images, used for
 * components whose draw method is very expensive. Make sure to turn on
 * {@link GameWindow#enableHardwareAcceleration()} for maximum performance when using this class, as
 * it enables images to be cached in video memory.
 * </p>
 * <p>
 * To use this, extend the interface and implement its methods, then call the static method
 * {@link CacheableRendererComponent#draw(CacheableRendererComponent, JGraphics)} in the component's
 * {@link UIRendererComponent#draw(JGraphics)} method. For example,
 * </p>
 *
 * <blockquote><pre>
 * public class ExpensiveRendererComponent extends UIRendererComponent implements CacheableRendererBehavior&lt;Integer&gt; {
 *     private final int someParameter;
 *
 *     //...
 *
 *     &#64;Override
 *     public void drawToCachedImage(Vec2 size, JGraphics graphics) {
 *         drawExpensively(size, graphics, someParameter);
 *     }
 *
 *     &#64;Override
 *     public int getCacheKey() {
 *         return someParameter;
 *     }
 *
 *     &#64;Override
 *     public void draw(JGraphics graphics) {
 *         CacheableRendererBehavior.draw(this, graphics);
 *         // You are free to do other operations before and after the call, if you need
 *     }
 * }
 * </pre></blockquote>
 */
public interface CacheableRendererComponent<K> extends HasBoundingBox {
    /**
     * Draws the component to an image to be cached. Note that you must draw the component centered in the image.
     *
     * @param size     The size of the image
     * @param graphics The graphics object. (0, 0) is the center of the image.
     */
    void drawToCachedImage(Vec2 size, JGraphics graphics);

    /**
     * Get the key to be used in the image cache. When the key matches a key already in the cache, the cached image is
     * used and no new image is generated. Ideally, this should take on a relatively small number of values for caching
     * to be most effective. You do not need to include the bounding box size in this method; the cache image size
     * (which is the same as the bounding box size, unless {@link CacheableRendererComponent#getCacheImageSize()} is
     * overridden) is also included in caching.
     */
    K getCacheKey();

    /**
     * Supplies a new cache to be used in the image cache. You may override this method if your keys are better suited to a different type of cache
     * than the default, which is backed by a HashMap
     */
    default Cache<K, Image> createCache() {
        return new Cache<>();
    }

    /**
     * If the rendered result is not the same size as the bounding box, you may override this method to change the
     * cached image size. By default, this is just the size of the bounding box.
     */
    default Vec2i getCacheImageSize() {
        return new Vec2i(getBoundingBox().getSize());
    }

    // This method does some unchecked casting due to generics.
    @SuppressWarnings("unchecked")
    private static <K> ImageCache<K> getImageCacheForClass(
        CacheableRendererComponent<K> behavior
    ) {
        Class<CacheableRendererComponent<K>> cls = (Class<CacheableRendererComponent<K>>) behavior.getClass();
        return (ImageCache<K>) ImageCache.staticCache.getOrCompute(cls, () -> new ImageCache<>(behavior::createCache));
    }

    /**
     * Draws the component, with caching. See {@link CacheableRendererComponent} for more details
     */
    static <K> void draw(CacheableRendererComponent<K> behavior, JGraphics graphics) {
        ImageCache<K> cache = getImageCacheForClass(behavior);
        Vec2i cacheImageSize = behavior.getCacheImageSize();
        K key = behavior.getCacheKey();
        Image img = cache.get(cacheImageSize, key, behavior::drawToCachedImage);

        graphics.drawImage(img,
            behavior.getBoundingBox().getAbsolutePosition(),
            new Vec2(cacheImageSize)
        );
    }
}

interface DrawFunction {
    void draw(Vec2 size, JGraphics graphics);
}

class ImageCache<T> {
    static final Cache<Class<? extends CacheableRendererComponent<?>>, ImageCache<?>> staticCache = new Cache<>();

    private final Cache<Vec2i, Cache<T, Image>> cache = new Cache<>();
    private final Supplier<Cache<T, Image>> cacheSupplier;

    ImageCache(Supplier<Cache<T, Image>> cacheSupplier) {
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