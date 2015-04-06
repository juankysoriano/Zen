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
        FlowerDrawer flowerDrawer = new FlowerDrawer(flower, new ArrayList<RainbowImage>(flowerLeafsRes.size()), rainbowDrawer);

        for (Integer leafRes : flowerLeafsRes) {
            rainbowDrawer.loadImage(leafRes, RainbowImage.LOAD_ORIGINAL_SIZE, flowerDrawer);
        }

        return flowerDrawer;
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

    private float getFlowerSize() {
        return RainbowMath.random(flower.getMinSize(), flower.getMaxSize());
    }

}
