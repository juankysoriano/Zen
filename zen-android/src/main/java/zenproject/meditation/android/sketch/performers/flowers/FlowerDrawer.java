package zenproject.meditation.android.sketch.performers.flowers;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.graphics.RainbowGraphics;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;
import com.juankysoriano.rainbow.utils.RainbowMath;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;

public abstract class FlowerDrawer implements RainbowImage.LoadPictureListener {
    private static final RainbowImage NO_IMAGE = null;
    private static final int WHITE = 255;
    private static final int ALPHA = 225;
    protected static final int MIN_FLOWER_SIZE = ContextRetriever.INSTANCE.getCurrentResources().getDimensionPixelSize(R.dimen.min_flower_size);
    protected static final int MAX_FLOWER_SIZE = ContextRetriever.INSTANCE.getCurrentResources().getDimensionPixelSize(R.dimen.max_flower_size);

    private RainbowImage image;
    private RainbowDrawer rainbowDrawer;

    protected FlowerDrawer(RainbowDrawer rainbowDrawer) {
        this.rainbowDrawer = rainbowDrawer;
    }

    @Override
    public void onLoadSucceed(RainbowImage imageLoaded) {
        image = imageLoaded;
    }

    @Override
    public void onLoadFail() {
        image = NO_IMAGE;
    }

    public void paintFlowerFor(Branch branch) {
        float flowerSize = getFlowerSize();
        float rotation = RainbowMath.random(RainbowMath.QUARTER_PI);
        rainbowDrawer.tint(WHITE, ALPHA);
        rainbowDrawer.imageMode(RainbowGraphics.CENTER);
        rainbowDrawer.pushMatrix();
        rainbowDrawer.translate(branch.getX(), branch.getY());
        rainbowDrawer.rotate(rotation);
        flipHorizontalyIfLuck();
        rainbowDrawer.image(image, 0, 0, flowerSize, flowerSize);
        rainbowDrawer.popMatrix();
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
            case ORANGE:
                return OrangeDrawer.newInstance(rainbowDrawer);
            case OLIVE:
                return OliveDrawer.newInstance(rainbowDrawer);
            case GYSOPHILIA:
                return GypsophilaDrawer.newInstance(rainbowDrawer);
            default:
                return NullFlowerDrawer.newInstance(rainbowDrawer);
        }
    }

    public enum Flower {
        NONE,
        CHERRY,
        ORANGE,
        OLIVE,
        GYSOPHILIA;

        public static Flower from(int value) {
            for (Flower flower : values()) {
                if (flower.ordinal() == value) {
                    return flower;
                }
            }
            return NONE;
        }
    }
}
