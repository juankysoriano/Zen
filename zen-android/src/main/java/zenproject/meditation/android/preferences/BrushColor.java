package zenproject.meditation.android.preferences;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;

public enum BrushColor {
    DARK(ContextRetriever.INSTANCE.getCurrentContext().getResources().getColor(R.color.dark_brush)),
    GREY(ContextRetriever.INSTANCE.getCurrentContext().getResources().getColor(R.color.grey_brush)),
    ERASE(ContextRetriever.INSTANCE.getCurrentContext().getResources().getColor(R.color.colorSketch)),
    PRIMARY(ContextRetriever.INSTANCE.getCurrentContext().getResources().getColor(R.color.colorPrimary)),
    ACCENT(ContextRetriever.INSTANCE.getCurrentContext().getResources().getColor(R.color.colorAccent));

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
