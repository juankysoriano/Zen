package zenproject.meditation.android.sketch.music;

import android.media.MediaPlayer;

import com.juankysoriano.rainbow.core.event.RainbowInputController;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import zenproject.meditation.android.RobolectricLauncherGradleTestRunner;
import zenproject.meditation.android.persistence.BrushOptionsPreferences;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.*;

@RunWith(RobolectricLauncherGradleTestRunner.class)
public class MusicPerformerTest {
    private static final float MUSIC_STEP = 0.01f;
    private static final float MIN_VOLUME = 0.05f;
    @Mock
    private RainbowInputController rainbowInputController;
    @Mock
    private MediaPlayer mediaPlayer;

    private MusicPerformer musicPerformer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        musicPerformer = new MusicPerformer(mediaPlayer, rainbowInputController);
    }

    @Test
    public void onEveryStepIfScreenIsTouchedAndIsPlayingVolumeIsIncreased() {
        givenThatIsPlaying();
        givenThatScreenIsTouched();
        musicPerformer.doStep();
        verify(mediaPlayer).setVolume(MIN_VOLUME + MUSIC_STEP, MIN_VOLUME + MUSIC_STEP);
    }

    @Test
    public void onEveryStepIfScreenIsTouchedAndIsNotPlayingVolumeNotChanged() {
        givenThatIsNotPlaying();
        givenThatScreenIsTouched();
        musicPerformer.doStep();

        verify(mediaPlayer,never()).setVolume(anyFloat(), anyFloat());
    }

    @Test
    public void onEveryStepIfScreenIsNotTouchedAndIsPlayingVolumeIsDecreased() {
        givenThatIsPlaying();
        givenThatVolumeIsTwoStepsOverInitial();
        givenThatScreenIsNotTouched();
        musicPerformer.doStep();

        verify(mediaPlayer, times(2)).setVolume(MIN_VOLUME + MUSIC_STEP, MIN_VOLUME + MUSIC_STEP); //one when increasing, one decreasing down.
    }

    @Test
    public void onEveryStepIfScreenIsNotTouchedAndIsNotPlayingVolumeNotChanged() {
        givenThatIsNotPlaying();
        givenThatScreenIsNotTouched();
        musicPerformer.doStep();

        verify(mediaPlayer, never()).setVolume(anyFloat(), anyFloat());
    }

    @Test
    public void testThatWhenResetIsCalledIfItIsNotPlayingMusicStartIsCalled() {
        givenThatIsNotPlaying();
        musicPerformer.reset();

        verify(mediaPlayer).start();
    }

    @Test
    public void testThatWhenResetIsCalledIfItIsPlayingMusicStartIsCalled() {
        givenThatIsPlaying();
        musicPerformer.reset();

        verify(mediaPlayer, never()).start();
    }

    @Test
    public void testThatWhenEnableIsCalledIfItIsNotPlayingMusicStartIsCalled() {
        givenThatIsNotPlaying();
        musicPerformer.enable();

        verify(mediaPlayer).start();
    }

    @Test
    public void testThatWhenEnableIsCalledIfItIsPlayingMusicStartIsCalled() {
        givenThatIsPlaying();
        musicPerformer.enable();

        verify(mediaPlayer, never()).start();
    }

    @Test
    public void testThatWhenDisableIsCalledIfItIsPlayingMusicStopIsCalled() {
        givenThatIsPlaying();
        musicPerformer.disable();

        verify(mediaPlayer).stop();
    }

    @Test
    public void testThatWhenDisableIsCalledIfItIsNotPlayingMusicStopIsNotCalled() {
        givenThatIsNotPlaying();
        musicPerformer.disable();

        verify(mediaPlayer, never()).stop();
    }

    @Test
    public void testThatWhenResetIsCalledIfItIsPlayingMediaPlayerReleaseIsCalled() {
        givenThatIsPlaying();
        musicPerformer.disable();

        verify(mediaPlayer).release();
    }

    @Test
    public void testThatWhenResetIsCalledIfItIsNotPlayingMediaPlayerReleaseIsNotCalled() {
        givenThatIsNotPlaying();
        musicPerformer.disable();

        verify(mediaPlayer, never()).release();
    }

    @Test
    public void testThatMusicPerformerNewInstanceReturnsNotNullMusicPerformer() {
        Assertions.assertThat(BrushOptionsPreferences.newInstance()).isNotNull();
    }

    @Test
    public void testThatMusicPerformerNewInstanceReturnsANewInstance() {
        MusicPerformer firstInstance = MusicPerformer.newInstance(rainbowInputController);
        MusicPerformer secondInstance = MusicPerformer.newInstance(rainbowInputController);

        Assertions.assertThat(firstInstance).isNotEqualTo(secondInstance);
    }

    private void givenThatIsPlaying() {
        when(mediaPlayer.isPlaying()).thenReturn(true);
    }

    private void givenThatIsNotPlaying() {
        when(mediaPlayer.isPlaying()).thenReturn(false);
    }

    private void givenThatScreenIsTouched() {
        when(rainbowInputController.isScreenTouched()).thenReturn(true);
    }

    private void givenThatVolumeIsTwoStepsOverInitial() {
        when(rainbowInputController.isScreenTouched()).thenReturn(true);
        musicPerformer.doStep();
        musicPerformer.doStep();
    }

    private void givenThatScreenIsNotTouched() {
        when(rainbowInputController.isScreenTouched()).thenReturn(false);
    }
}