package zenproject.meditation.android.sketch.performers.flowers;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;
import com.juankysoriano.rainbow.utils.RainbowMath;

import java.util.ArrayList;
import java.util.List;

import zenproject.meditation.android.R;

public class PoppyDrawer extends FlowerDrawer {
    private static final int NUM_FLOWERS = 5;

    public static FlowerDrawer newInstance(RainbowDrawer rainbowDrawer) {
        PoppyDrawer poppyDrawer = new PoppyDrawer(new ArrayList<RainbowImage>(NUM_FLOWERS), rainbowDrawer);
        rainbowDrawer.loadImage(R.drawable.poppy_1, RainbowImage.LOAD_ORIGINAL_SIZE, poppyDrawer);
        rainbowDrawer.loadImage(R.drawable.poppy_2, RainbowImage.LOAD_ORIGINAL_SIZE, poppyDrawer);
        rainbowDrawer.loadImage(R.drawable.poppy_3, RainbowImage.LOAD_ORIGINAL_SIZE, poppyDrawer);
        rainbowDrawer.loadImage(R.drawable.poppy_4, RainbowImage.LOAD_ORIGINAL_SIZE, poppyDrawer);
        rainbowDrawer.loadImage(R.drawable.poppy_5, RainbowImage.LOAD_ORIGINAL_SIZE, poppyDrawer);

        return poppyDrawer;
    }

    private PoppyDrawer(List<RainbowImage> flowerImages, RainbowDrawer rainbowDrawer) {
        super(flowerImages, rainbowDrawer);
    }

    @Override
    protected float getFlowerSize() {
        return RainbowMath.random(MIN_FLOWER_SIZE, MAX_FLOWER_SIZE);
    }
}
