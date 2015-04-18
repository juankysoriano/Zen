package zenproject.meditation.android.sketch.painting;

public class PaintStepSkipper {
    private static final int FRAMES_TO_SKIP = 4;

    private int step;

    public boolean hasToSkipStep() {
        return step % (FRAMES_TO_SKIP + 1) != 0;
    }

    public void recordStep() {
        step++;
    }
}
