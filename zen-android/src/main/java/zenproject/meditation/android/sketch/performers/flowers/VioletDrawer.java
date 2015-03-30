package zenproject.meditation.android.sketch.performers.flowers;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;
import com.juankysoriano.rainbow.utils.RainbowMath;

import java.util.ArrayList;

import zenproject.meditation.android.R;

public class VioletDrawer extends FlowerDrawer {
    public static FlowerDrawer newInstance(RainbowDrawer rainbowDrawer) {
        FlowerDrawer flowerDrawer = new VioletDrawer(rainbowDrawer);
        rainbowDrawer.loadImage(R.drawable.violet_1, RainbowImage.LOAD_ORIGINAL_SIZE, flowerDrawer);

        return flowerDrawer;
    }

    protected VioletDrawer(RainbowDrawer rainbowDrawer) {
        super(new ArrayList<RainbowImage>(), rainbowDrawer);
    }

    @Override
    protected float getFlowerSize() {
        return RainbowMath.random(MIN_FLOWER_SIZE*1.2f, MAX_FLOWER_SIZE*1.2f);
    }
}
