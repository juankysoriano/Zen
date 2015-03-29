package zenproject.meditation.android.sketch.performers.flowers;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;
import com.juankysoriano.rainbow.utils.RainbowMath;

import java.util.ArrayList;

import zenproject.meditation.android.R;

public class AutumnMixDrawer extends FlowerDrawer {
    public static FlowerDrawer newInstance(RainbowDrawer rainbowDrawer) {
        FlowerDrawer flowerDrawer = new AutumnMixDrawer(rainbowDrawer);
        rainbowDrawer.loadImage(R.drawable.autumn_mix, RainbowImage.LOAD_ORIGINAL_SIZE, flowerDrawer);
        return flowerDrawer;
    }

    protected AutumnMixDrawer(RainbowDrawer rainbowDrawer) {
        super(new ArrayList<RainbowImage>(), rainbowDrawer);
    }

    @Override
    protected float getFlowerSize() {
        return RainbowMath.random(MIN_FLOWER_SIZE * 0.7f, MAX_FLOWER_SIZE * 0.7f);
    }
}
