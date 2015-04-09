package zenproject.meditation.android.sketch.painting.flowers;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.graphics.RainbowGraphics;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;
import com.juankysoriano.rainbow.utils.RainbowMath;

import java.util.ArrayList;
import java.util.List;

import zenproject.meditation.android.sketch.painting.flowers.branch.Branch;

public class FlowerDrawer implements RainbowImage.LoadPictureListener {
    private static final RainbowImage NO_IMAGE = null;
    protected static final int WHITE = 255;
    public static final int NORMAL = 1;
    public static final int REVERSE = -1;
    public static final int HUNDRED = 100;
    public static final int FIFTY = 50;

    private final Flower flower;
    private final List<RainbowImage> flowerImages;
    private final RainbowDrawer rainbowDrawer;

    protected FlowerDrawer(Flower flower, List<RainbowImage> flowerImages, RainbowDrawer rainbowDrawer) {
        this.flower = flower;
        this.flowerImages = flowerImages;
        this.rainbowDrawer = rainbowDrawer;
    }

    public static FlowerDrawer newInstance(Flower flower, RainbowDrawer rainbowDrawer) {
        List<Integer> flowerLeafsRes = flower.getFlowerLeafRes();
        return new FlowerDrawer(flower, new ArrayList<RainbowImage>(flowerLeafsRes.size()), rainbowDrawer);
    }

    public void init() {
        for (Integer leafRes : flower.getFlowerLeafRes()) {
            rainbowDrawer.loadImage(leafRes, RainbowImage.LOAD_ORIGINAL_SIZE, this);
        }
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
        if (hasFlowersToPaint()) {
            float flowerSize = getFlowerSize();
            float rotation = RainbowMath.random(RainbowMath.QUARTER_PI);
            rainbowDrawer.tint(WHITE);
            rainbowDrawer.imageMode(RainbowGraphics.CENTER);
            rainbowDrawer.pushMatrix();
            rainbowDrawer.translate(branch.getX(), branch.getY());
            rainbowDrawer.rotate(rotation);
            flipHorizontallyIfLuck();
            rainbowDrawer.image(getRandomFlower(), 0, 0, flowerSize, flowerSize);
            rainbowDrawer.popMatrix();
        }
    }

    private boolean hasFlowersToPaint() {
        return !flowerImages.isEmpty();
    }

    private RainbowImage getRandomFlower() {
        return flowerImages.get((int) RainbowMath.random(flowerImages.size()));
    }

    private void flipHorizontallyIfLuck() {
        if (flipCoin()) {
            rainbowDrawer.scale(REVERSE, NORMAL); //Tricky, but this reverses image.
        }
    }

    private boolean flipCoin() {
        return RainbowMath.random(HUNDRED) >= FIFTY;
    }

    private float getFlowerSize() {
        return RainbowMath.random(flower.getMinSize(), flower.getMaxSize());
    }

}
