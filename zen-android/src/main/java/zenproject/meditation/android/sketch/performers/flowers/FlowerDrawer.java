package zenproject.meditation.android.sketch.performers.flowers;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.graphics.RainbowGraphics;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;
import com.juankysoriano.rainbow.utils.RainbowMath;

import java.util.List;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.preferences.Flower;

public abstract class FlowerDrawer implements RainbowImage.LoadPictureListener {
    private static final RainbowImage NO_IMAGE = null;
    protected static final int WHITE = 255;
    protected static final float MIN_FLOWER_SIZE = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.min_flower_size);
    protected static final float MAX_FLOWER_SIZE = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.max_flower_size);

    private final List<RainbowImage> flowerImages;
    private RainbowDrawer rainbowDrawer;

    protected FlowerDrawer(List<RainbowImage> flowerImages, RainbowDrawer rainbowDrawer) {
        this.flowerImages = flowerImages;
        this.rainbowDrawer = rainbowDrawer;
    }

    @Override
    public void onLoadSucceed(RainbowImage imageLoaded) {
        flowerImages.add(imageLoaded);
    }

    @Override
    public void onLoadFail() {
        flowerImages.add(NO_IMAGE);
    }

    public void paintFlowerFor(Branch branch) {
        float flowerSize = getFlowerSize();
        float rotation = RainbowMath.random(RainbowMath.QUARTER_PI);
        rainbowDrawer.tint(WHITE);
        rainbowDrawer.imageMode(RainbowGraphics.CENTER);
        rainbowDrawer.pushMatrix();
        rainbowDrawer.translate(branch.getX(), branch.getY());
        rainbowDrawer.rotate(rotation);
        flipHorizontalyIfLuck();
        rainbowDrawer.image(getRandomFlower(), 0, 0, flowerSize, flowerSize);
        rainbowDrawer.popMatrix();
    }

    private RainbowImage getRandomFlower() {
        return flowerImages.get((int) RainbowMath.random(flowerImages.size()));
    }

    private void flipHorizontalyIfLuck() {
        if (RainbowMath.random(2) > 1) {
            rainbowDrawer.scale(-1, 1);
        }
    }

    protected abstract float getFlowerSize();

    public static FlowerDrawer from(Flower flower, RainbowDrawer rainbowDrawer) {
        switch (flower) {
            case NONE:
                return NullFlowerDrawer.newInstance(rainbowDrawer);
            case CHERRY:
                return CherryDrawer.newInstance(rainbowDrawer);
            case MECONOPSIS:
                return MeconopsisDrawer.newInstance(rainbowDrawer);
            case POPPY:
                return PoppyDrawer.newInstance(rainbowDrawer);
            case INK_FLOWER:
                return InkFlowerDrawer.newInstance(rainbowDrawer);
            default:
                return NullFlowerDrawer.newInstance(rainbowDrawer);
        }
    }

}
