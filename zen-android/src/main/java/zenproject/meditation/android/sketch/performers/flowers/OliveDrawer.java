package zenproject.meditation.android.sketch.performers.flowers;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;
import com.juankysoriano.rainbow.utils.RainbowMath;

import zenproject.meditation.android.R;

public class OliveDrawer extends FlowerDrawer {
    public static FlowerDrawer newInstance(RainbowDrawer rainbowDrawer) {
        FlowerDrawer flowerDrawer = new OliveDrawer(rainbowDrawer);
        rainbowDrawer.loadImage(R.drawable.olive, RainbowImage.LOAD_ORIGINAL_SIZE, flowerDrawer);
        return flowerDrawer;
    }

    protected OliveDrawer(RainbowDrawer rainbowDrawer) {
        super(rainbowDrawer);
    }

    @Override
    protected float getFlowerSize() {
        return RainbowMath.random(MIN_FLOWER_SIZE, MAX_FLOWER_SIZE);
    }
}
