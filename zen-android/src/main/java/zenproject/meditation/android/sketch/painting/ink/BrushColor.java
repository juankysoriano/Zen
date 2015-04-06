package zenproject.meditation.android.sketch.painting.ink;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;

public enum BrushColor {
    DARK(ContextRetriever.INSTANCE.getResources().getColor(R.color.dark_brush)),
    GREY(ContextRetriever.INSTANCE.getResources().getColor(R.color.grey_brush)),
    PRIMARY(ContextRetriever.INSTANCE.getResources().getColor(R.color.colorPrimary)),
    ACCENT(ContextRetriever.INSTANCE.getResources().getColor(R.color.colorAccent)),
    ERASE(ContextRetriever.INSTANCE.getResources().getColor(R.color.colorSketch));

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
