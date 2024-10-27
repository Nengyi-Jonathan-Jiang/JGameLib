package jGameLib.animation;

public class EmptyAnimation<T> extends Animation<T> {
    public EmptyAnimation(int totalAnimationFrames) {
        super(totalAnimationFrames);
    }

    @Override
    public void onAnimationStart(T target) {

    }

    @Override
    public void updateAnimation(T target, double progress) {

    }

    @Override
    public void onAnimationEnd(T target) {

    }

}
