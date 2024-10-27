package jGameLib.animation;

public abstract class Animation<T> {
    public final int totalAnimationFrames;

    protected Animation(int totalAnimationFrames) {
        this.totalAnimationFrames = totalAnimationFrames;
    }

    /**
     * Called before {@link Animation#updateAnimation} is called for the first time in the animation. This can be used
     * to reset the target to a known state and read the state of the target.
     */
    public abstract void onAnimationStart(T target);

    /**
     * Called during the animation.
     *
     * @param progress The progress of the animation, a double in the range (0, 1], guaranteed to be non-decreasing
     *                 while the animation is running. Note that the animation may be cancelled before this reaches 1,
     *                 in which case you may use {@link Animation#onAnimationEnd} to force the target into the end
     *                 state of the animation. In addition, the total number of calls to <code>updateAnimation()</code>
     *                 during the animation may be more or less than <code>totalAnimationFrames</code> even if the
     *                 animation was not cancelled.
     */
    public abstract void updateAnimation(T target, double progress);

    /**
     * Called after the last call to {@link Animation#updateAnimation} (or {@link Animation#onAnimationStart} if update 
     * was never called) in the same update frame. This can be used to ensure that the target is in the end state of 
     * the animation even if the animation was cancelled before {@link Animation#updateAnimation} was called with 
     * progress = 1
     */
    public abstract void onAnimationEnd(T target);
}
