package zenproject.meditation.android.preferences;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;

public enum BrushColor {
    ERASE(ContextRetriever.INSTANCE.getCurrentResources().getColor(R.color.colorSketch)),
    DARK(ContextRetriever.INSTANCE.getCurrentResources().getColor(R.color.dark_brush)),
    GREY(ContextRetriever.INSTANCE.getCurrentResources().getColor(R.color.grey_brush)),
    PRIMARY(ContextRetriever.INSTANCE.getCurrentResources().getColor(R.color.colorPrimary)),
    ACCENT(ContextRetriever.INSTANCE.getCurrentResources().getColor(R.color.colorAccent));

    private final int color;

    BrushColor(int color) {
        this.color = color;
    }

    public static BrushColor from(int color) {
        for (BrushColor brushColor : values()) {
            if (brushColor.color == color) {
                return brushColor;
            }
        }
        return DARK;
    }

    public int toAndroidColor() {
        return color;
    }
}
