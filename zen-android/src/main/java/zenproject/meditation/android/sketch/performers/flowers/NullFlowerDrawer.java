package zenproject.meditation.android.sketch.performers.flowers;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;

public class NullFlowerDrawer extends FlowerDrawer {

    public static FlowerDrawer newInstance(RainbowDrawer rainbowDrawer) {
        return new NullFlowerDrawer(rainbowDrawer);
    }

    protected NullFlowerDrawer(RainbowDrawer rainbowDrawer) {
        super(rainbowDrawer);
    }

    @Override
    public void paintFlowerFor(Branch branch) {
        //no-op;
    }

    @Override
    protected float getFlowerSize() {
        return 0;
    }
}
