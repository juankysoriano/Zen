package zenproject.meditation.android.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.sketch.painting.ink.BrushColor;

public class BrushOptionsPreferences {
    private static final float DARKENING_FACTOR = 1.4f;
    private static final String PREF_NAME = "BrushPreferences";
    private static final String PREF_BRUSH_COLOR = PREF_NAME + "Color";
    private static final String PREF_BRUSH_SIZE = PREF_NAME + "Size";
    private static final int DEFAULT_PERCENTAGE = 20;
    private static final int LEAF_COLOR = Color.rgb(59, 158, 58);
    private final SharedPreferences sharedPreferences;

    public static BrushOptionsPreferences newInstance() {
        return new BrushOptionsPreferences(ContextRetriever.INSTANCE.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE));
    }

    protected BrushOptionsPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void applyBrushSizePercentage(int percentage) {
        sharedPreferences.edit().putInt(PREF_BRUSH_SIZE, percentage).apply();
    }

    public int getBrushSizePercentage() {
        return sharedPreferences.getInt(PREF_BRUSH_SIZE, DEFAULT_PERCENTAGE);
    }

    public void applyBrushColor(BrushColor color) {
        sharedPreferences.edit().putInt(PREF_BRUSH_COLOR, color.color()).apply();
    }

    public BrushColor getBrushColor() {
        int defaultColor = BrushColor.DARK.color();
        int color = sharedPreferences.getInt(PREF_BRUSH_COLOR, defaultColor);
        return BrushColor.from(color);
    }

    public int getBranchColor() {
        int color = getBrushColor().color();
        return Color.rgb((int) (Color.red(color) / DARKENING_FACTOR),
                (int) (Color.green(color) / DARKENING_FACTOR),
                (int) (Color.blue(color) / DARKENING_FACTOR));
    }

    public int getLeafColor() {
        return LEAF_COLOR;
    }
}
