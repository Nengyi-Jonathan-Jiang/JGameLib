package jGameLib.animation;

import java.util.Arrays;

public class SequencedAnimationGroup<T> extends Animation<T> {
    private final Animation<T>[] animations;
    private final double[] animationStartTimes;
    private final double[] animationEndTimes;
    private int currentAnimationIndex;

    @Override
    public void onAnimationStart(T entity) {
        currentAnimationIndex = 0;
        animations[currentAnimationIndex].onAnimationStart(entity);
    }

    @Override
    public void updateAnimation(T entity, double progress) {
        simulateSkippedAnimations(progress, entity);

        double start = animationStartTimes[currentAnimationIndex];
        double end = animationEndTimes[currentAnimationIndex];
        double subAnimationProgress = (progress - start) / (end - start);
        animations[currentAnimationIndex].updateAnimation(entity, subAnimationProgress);
    }

    @Override
    public void onAnimationEnd(T entity) {
        simulateSkippedAnimations(1, entity);
        animations[currentAnimationIndex].onAnimationEnd(entity);
    }

    // Precondition:
    //      0 < progress <= 1;
    //      onAnimationStart has already been called on the current animation;
    // Postcondition:
    //      onAnimationStart has been called on the current animation;
    //      animationStartTime[current] < progress <= animationEndTime[current]
    private void simulateSkippedAnimations(double progress, T entity) {
        while (progress > animationEndTimes[currentAnimationIndex]) {
            animations[currentAnimationIndex].onAnimationEnd(entity);
            currentAnimationIndex++;
            animations[currentAnimationIndex].onAnimationStart(entity);
        }
    }

    @SafeVarargs
    public SequencedAnimationGroup(Animation<T>... animations) {
        super(Arrays.stream(animations).mapToInt(i -> i.totalAnimationFrames).sum());
        this.animations = animations;

        animationEndTimes = new double[animations.length];
        animationStartTimes = new double[animations.length];
        int frames = 0;
        for (int i = 0; i < animations.length; i++) {
            animationStartTimes[i] = 1.0 * frames / totalAnimationFrames;
            frames += animations[i].totalAnimationFrames;
            animationEndTimes[i] = 1.0 * frames / totalAnimationFrames;
        }
    }
}
