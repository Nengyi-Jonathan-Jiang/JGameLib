package jGameLib.util.files;

import jGameLib.util.caches.Cache;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Basic class representing a playable sound. Sounds can be played at the same time.
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class Sound {
    private static class SoundSource {
        public final byte[] data;
        public final DataLine.Info info;
        private final AudioFormat format;

        private SoundSource(AudioInputStream audioInputStream) throws IOException {
            final AudioFormat pcmFormat = getCorrespondingDecompressedFormat(audioInputStream);
            final AudioInputStream decodedAudioStream = AudioSystem.getAudioInputStream(pcmFormat, audioInputStream);

            this.data = decodedAudioStream.readAllBytes();
            format = decodedAudioStream.getFormat();
            this.info = new DataLine.Info(Clip.class, format);
        }

        private static @NotNull AudioFormat getCorrespondingDecompressedFormat(AudioInputStream audioInputStream) {
            final AudioFormat format = audioInputStream.getFormat();
            return new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                format.getSampleRate(),
                16,
                format.getChannels(),
                16 / 8 * format.getChannels(),
                format.getSampleRate(),
                format.isBigEndian()
            );
        }

        private Clip createClip() throws IOException, LineUnavailableException {
            Clip clip = (Clip) AudioSystem.getLine(info);
            AudioInputStream audioInputStream = new AudioInputStream(
                new ByteArrayInputStream(data),
                format,
                data.length
            );
            clip.open(audioInputStream);
            return clip;
        }
    }

    private final static Cache<String, SoundSource> sources = new Cache<>();

    private static Clip getClip(String filename) {
        try {
            var source = sources.getOrCompute(filename, () -> {
                try (InputStream inputStream = Sound.class.getResourceAsStream("/" + filename)) {
                    if (inputStream != null) {
                        var bufferedInputStream = new BufferedInputStream(inputStream);
                        var audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
                        return new SoundSource(audioInputStream);
                    } else {
                        throw new IOException("Null input stream");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            });

            return source == null ? null : source.createClip();
        } catch (Exception e) {
            e.printStackTrace();
            sources.putValue(filename, null);
            return null;
        }
    }

    private final Clip clip;
    private final FloatControl gainControl;

    /**
     * Loads a sound, given the file name. <br>
     * WARNING: This is an expensive operation.
     * TODO: check if this is expensive
     */
    public Sound(String fileName) {
        this(getClip(fileName));
    }

    private Sound(Clip clip) {
        this.clip = clip;
        if (clip != null) gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        else gainControl = null;
    }

    public boolean equals(Sound sound) {
        return sound != null && clip == sound.clip;
    }

    /**
     * A sound representing silence.
     */
    public static final Sound SILENCE = new Sound((Clip) null);

    /**
     * Play the sound and loop it until {@link Sound#stop()}
     * is called
     */
    public void playLooping() {
        if (clip == null) return;
        clip.stop();
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Stops playing the sound
     */
    public void stop() {
        if (clip == null) return;
        clip.stop();
        clip.setFramePosition(0);
    }

    /**
     * Plays the sound once.
     */
    public void play() {
        if (clip == null) return;
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    /**
     * @return Whether the sound is currently playing
     */
    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }

    /**
     * @return The volume of the sound
     */
    public double getVolume() {
        return gainControl == null ? 0 : Math.pow(10, gainControl.getValue() / 20);
    }

    /**
     * Sets the volume of the sound. Volume should be a number between 0 and 1
     */
    public Sound setVolume(double volume) {
        if (gainControl != null) {
            float vol = 20f * (float) Math.log10(Math.max(Math.min(volume, 1), 0));
            gainControl.setValue(Math.max(vol, -80f));
        }
        return this;
    }
}