package zenproject.meditation.android.sketch.painting.flowers;

import android.support.annotation.DrawableRes;

import java.util.Arrays;
import java.util.List;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;

/**
 * Here I am trying to introduce a possible enum factory pattern for easily creating flower drawers from resource elements
 */
public enum Flower {

    NONE(Dimension.DEFAULT_MIN_SIZE,
            Dimension.DEFAULT_MAX_SIZE),
    INK_FLOWER(Dimension.DEFAULT_MIN_SIZE * 1.2f,
            Dimension.DEFAULT_MAX_SIZE * 1.2f,
            R.drawable.ink_flower_1,
            R.drawable.ink_flower_2,
            R.drawable.ink_flower_3,
            R.drawable.ink_flower_4),
    POPPY(Dimension.DEFAULT_MIN_SIZE,
            Dimension.DEFAULT_MAX_SIZE,
            R.drawable.poppy_1,
            R.drawable.poppy_2,
            R.drawable.poppy_3,
            R.drawable.poppy_4,
            R.drawable.poppy_5),
    MECONOPSIS(Dimension.DEFAULT_MIN_SIZE * 0.8f,
            Dimension.DEFAULT_MAX_SIZE * 0.8f,
            R.drawable.meconopsis_1,
            R.drawable.meconopsis_2,
            R.drawable.meconopsis_3,
            R.drawable.meconopsis_4,
            R.drawable.meconopsis_5),
    CHERRY(Dimension.DEFAULT_MIN_SIZE,
            Dimension.DEFAULT_MAX_SIZE,
            R.drawable.cherry_1,
            R.drawable.cherry_2,
            R.drawable.cherry_3,
            R.drawable.cherry_4,
            R.drawable.cherry_5);

    @DrawableRes
    private Integer[] flowerLeafsRes;
    private float minSize;
    private float maxSize;

    private Flower(float minSize, float maxSize, @DrawableRes Integer... flowerLeafsRes) {
        this.flowerLeafsRes = flowerLeafsRes;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    public static Flower from(int value) {
        for (Flower flower : values()) {
            if (flower.ordinal() == value) {
                return flower;
            }
        }
        return NONE;
    }

    public List<Integer> getFlowerLeafRes() {
        return Arrays.asList(flowerLeafsRes);
    }

    public float getMaxSize() {
        return maxSize;
    }

    public float getMinSize() {
        return minSize;
    }

    private static class Dimension {
        private static final float DEFAULT_MAX_SIZE = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.max_flower_size);
        private static final float DEFAULT_MIN_SIZE = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.min_flower_size);
    }
}
