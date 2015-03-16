package zenproject.meditation.android.drawers;

public class MusicPerformer implements StepPerformer {
    private boolean enabled;

    @Override
    public void doStep() {
        //no-op
    }

    @Override
    public void reset() {
        //no-op
    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public void enable() {
        enabled = true;
    }
}
