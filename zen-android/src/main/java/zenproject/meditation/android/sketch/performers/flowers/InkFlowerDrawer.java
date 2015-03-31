package zenproject.meditation.android.sketch.performers.flowers;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;
import com.juankysoriano.rainbow.utils.RainbowMath;

import java.util.ArrayList;

import zenproject.meditation.android.R;

public class InkFlowerDrawer extends FlowerDrawer {
    public static FlowerDrawer newInstance(RainbowDrawer rainbowDrawer) {
        FlowerDrawer flowerDrawer = new InkFlowerDrawer(rainbowDrawer);
        rainbowDrawer.loadImage(R.drawable.ink_flower_1, RainbowImage.LOAD_ORIGINAL_SIZE, flowerDrawer);
        rainbowDrawer.loadImage(R.drawable.ink_flower_2, RainbowImage.LOAD_ORIGINAL_SIZE, flowerDrawer);
        rainbowDrawer.loadImage(R.drawable.ink_flower_3, RainbowImage.LOAD_ORIGINAL_SIZE, flowerDrawer);
        rainbowDrawer.loadImage(R.drawable.ink_flower_4, RainbowImage.LOAD_ORIGINAL_SIZE, flowerDrawer);

        return flowerDrawer;
    }

    protected InkFlowerDrawer(RainbowDrawer rainbowDrawer) {
        super(new ArrayList<RainbowImage>(), rainbowDrawer);
    }

    @Override
    protected float getFlowerSize() {
        return RainbowMath.random(MIN_FLOWER_SIZE, MAX_FLOWER_SIZE);
    }
}
