package jGameLib.animation;

import java.util.Arrays;

public class ParallelAnimationGroup<T> extends Animation<T> {
    private final Animation<T>[] animations;
    private boolean[] didAnimationEnd;

    public ParallelAnimationGroup(Animation<T>... animations) {
        super(
            Arrays.stream(animations)
                .mapToInt(i -> i.totalAnimationFrames)
                .max()
                .orElse(0)
        );

        this.animations = animations;
    }

    @Override
    public void onAnimationStart(T target) {
        didAnimationEnd = new boolean[animations.length];
        for (Animation<T> animation : animations) {
            animation.onAnimationStart(target);
        }
    }

    @Override
    public void updateAnimation(T target, double progress) {
        for (int i = 0; i < animations.length; i++) {
            if (didAnimationEnd[i]) continue;

            Animation<T> animation = animations[i];
            double subAnimationProgress = progress * totalAnimationFrames / animation.totalAnimationFrames;
            if (progress > 1) {
                animation.updateAnimation(target, 1);
                animation.onAnimationEnd(target);
                didAnimationEnd[i] = true;
            } else {
                animation.updateAnimation(target, subAnimationProgress);
            }
        }
    }

    @Override
    public void onAnimationEnd(T target) {
        for (int i = 0; i < animations.length; i++) {
            if (didAnimationEnd[i]) continue;

            animations[i].onAnimationEnd(target);
        }
    }
}
