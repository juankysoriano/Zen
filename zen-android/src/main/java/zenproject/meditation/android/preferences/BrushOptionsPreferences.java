package zenproject.meditation.android.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import zenproject.meditation.android.ContextRetriever;

public class BrushOptionsPreferences {
    private static final String PREF_NAME = "BrushPreferences";
    private static final String PREF_BRUSH_COLOR = PREF_NAME + "Color";
    private static final String PREF_BRUSH_SIZE = PREF_NAME + "Size";

    private final SharedPreferences sharedPreferences;

    public static BrushOptionsPreferences newInstance() {
        return new BrushOptionsPreferences(ContextRetriever.INSTANCE.getCurrentContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE));
    }

    BrushOptionsPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void applyBrushSize(int percentage) {
        sharedPreferences.edit().putInt(PREF_BRUSH_SIZE, percentage).apply();
    }

    public int getBrushSize() {
        return sharedPreferences.getInt(PREF_BRUSH_SIZE, 0);
    }

    public void applyBrushColor(int color) {
        sharedPreferences.edit().putInt(PREF_BRUSH_COLOR, color).apply();
    }

    public int getBrushColor() {
        return sharedPreferences.getInt(PREF_BRUSH_COLOR, Color.BLACK);
    }
}
