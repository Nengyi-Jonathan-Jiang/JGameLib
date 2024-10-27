package jGameLib.animation;

import jGameLib.core.Entity;
import jGameLib.ui2d.rendering.UIRendererComponent;

@SuppressWarnings("unused")
public class AnimationComponent extends UIRendererComponent {
    private Animation<Entity> currentAnimation;
    private int currentAnimationFrame = 0;

    @Override
    public void update() {
        if (!isAnimating()) return;

        currentAnimationFrame++;
        double animationProgress = (double) currentAnimationFrame / currentAnimation.totalAnimationFrames;
        currentAnimation.updateAnimation(entity, animationProgress);

        if (currentAnimationFrame >= currentAnimation.totalAnimationFrames) {
            stopCurrentAnimation();
        }
    }

    public boolean isAnimating() {
        return currentAnimation != null;
    }

    public AnimationComponent applyAnimation(Animation<Entity> animation) {
        stopCurrentAnimation();
        currentAnimationFrame = 0;
        currentAnimation = animation;
        currentAnimation.onAnimationStart(entity);
        return this;
    }

    public void stopCurrentAnimation() {
        if (isAnimating()) {
            currentAnimation.onAnimationEnd(entity);
            currentAnimation = null;
        }
    }
}