package jGameLib.ui2d.audio;

import jGameLib.core.Entity;
import jGameLib.core.GameState;
import jGameLib.util.files.Sound;

@SuppressWarnings("unused")
public final class BackgroundMusicEntity extends Entity {
    public BackgroundMusicEntity(GameState state, Sound sound) {
        super(state);
        addComponent(new BackgroundMusicComponent(sound));
    }
}
