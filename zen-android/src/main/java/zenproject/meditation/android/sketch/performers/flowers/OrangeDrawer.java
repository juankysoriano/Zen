package zenproject.meditation.android.sketch.performers.flowers;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;
import com.juankysoriano.rainbow.utils.RainbowMath;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;

public class OrangeDrawer extends FlowerDrawer {
    protected static final int MIN_FLOWER_SIZE = ContextRetriever.INSTANCE.getCurrentResources().getDimensionPixelSize(R.dimen.min_flower_size);
    protected static final int MAX_FLOWER_SIZE = ContextRetriever.INSTANCE.getCurrentResources().getDimensionPixelSize(R.dimen.max_flower_size);

    public static FlowerDrawer newInstance(RainbowDrawer rainbowDrawer) {
        FlowerDrawer flowerDrawer = new OrangeDrawer(rainbowDrawer);
        rainbowDrawer.loadImage(R.drawable.orange, RainbowImage.LOAD_ORIGINAL_SIZE, flowerDrawer);
        return flowerDrawer;
    }

    protected OrangeDrawer(RainbowDrawer rainbowDrawer) {
        super(rainbowDrawer);
    }

    @Override
    protected float getFlowerSize() {
      return RainbowMath.random(MIN_FLOWER_SIZE, MAX_FLOWER_SIZE);
    }
}
