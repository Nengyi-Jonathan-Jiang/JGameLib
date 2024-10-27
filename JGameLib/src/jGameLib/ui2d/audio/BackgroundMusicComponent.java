package jGameLib.ui2d.audio;

import jGameLib.core.Component;
import jGameLib.util.files.Sound;

@SuppressWarnings("unused")
class BackgroundMusicComponent extends Component {
    final Sound sound;

    public BackgroundMusicComponent(Sound sound) {
        this.sound = sound;
    }
}
