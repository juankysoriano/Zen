package zenproject.meditation.android.sketch.performers;

import android.media.MediaPlayer;

import com.juankysoriano.rainbow.core.event.RainbowInputController;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;

public class MusicPerformer implements StepPerformer {
    private final MediaPlayer mediaPlayer;
    private final RainbowInputController rainbowInputController;
    private float volume;

    protected MusicPerformer(MediaPlayer mediaPlayer, RainbowInputController rainbowInputController) {
        this.mediaPlayer = mediaPlayer;
        this.rainbowInputController = rainbowInputController;
    }

    public static MusicPerformer newInstance(RainbowInputController rainbowInputController) {
        MediaPlayer mediaPlayer = MediaPlayer.create(ContextRetriever.INSTANCE.getCurrentContext(), R.raw.zen);
        mediaPlayer.setVolume(0, 0);
        mediaPlayer.start();
        return new MusicPerformer(mediaPlayer, rainbowInputController);
    }

    @Override
    public void doStep() {
        if (rainbowInputController.isScreenTouched()) {
            increaseVolume();
        } else {
            decreaseVolume();
        }
    }

    private void increaseVolume() {
        if (mediaPlayer.isPlaying()) {
            volume += 0.02f;
            volume = Math.min(1f, volume);
            mediaPlayer.setVolume(volume, volume);
        }
    }

    private void decreaseVolume() {
        if (mediaPlayer.isPlaying()) {
            volume -= 0.02f;
            volume = Math.max(0, volume);
            mediaPlayer.setVolume(volume, volume);
        }
    }

    @Override
    public void reset() {
        volume = 0f;
        mediaPlayer.setVolume(volume, volume);
    }

    @Override
    public void disable() {
        stopMusic();
    }

    private void stopMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void enable() {
        reset();
        startMusic();
    }

    private void startMusic() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void release() {
        mediaPlayer.release();
    }
}
