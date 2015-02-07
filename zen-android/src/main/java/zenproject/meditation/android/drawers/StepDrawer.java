package zenproject.meditation.android.drawers;

public interface StepDrawer {

    void paintStep();

    void initDrawingAt(float x, float y);

    void disable();

    void enable();
}
