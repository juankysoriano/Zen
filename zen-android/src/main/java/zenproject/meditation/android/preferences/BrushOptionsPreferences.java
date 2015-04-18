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
        sharedPreferences.edit().putInt(PREF_BRUSH_COLOR, color.toAndroidColor()).apply();
    }

    public BrushColor getBrushColor() {
        int defaultColor = BrushColor.DARK.toAndroidColor();
        int color = sharedPreferences.getInt(PREF_BRUSH_COLOR, defaultColor);
        return BrushColor.from(color);
    }

    public int getBranchColor() {
        int color = getBrushColor().toAndroidColor();
        return Color.rgb((int) (Color.red(color) / DARKENING_FACTOR),
                (int) (Color.green(color) / DARKENING_FACTOR),
                (int) (Color.blue(color) / DARKENING_FACTOR));
    }

    //TODO consider refactoring logic inside into a collaborator LeafColorRetriever or similar
    public int getLeafColor() {
        BrushColor brushColor = getBrushColor();
        switch (brushColor) {
            case PRIMARY: return BrushColor.ACCENT.toAndroidColor();
            case ACCENT: return BrushColor.PRIMARY.toAndroidColor();
            case DARK: return BrushColor.ACCENT.toAndroidColor();
            case AMBER: return BrushColor.PRIMARY.toAndroidColor();
            default: return BrushColor.PRIMARY.toAndroidColor();
        }
    }
}
