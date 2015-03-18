package zenproject.meditation.android.views.dialogs.brush.size;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class BrushSizeDrawable extends Drawable {

    private final Paint sizeBrushPaint;
    private float radius;
    private int centerX;
    private int centerY;

    public static BrushSizeDrawable newInstance() {
        Paint sizeBrushPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        return new BrushSizeDrawable(sizeBrushPaint);
    }

    protected BrushSizeDrawable(Paint paint) {
        this.sizeBrushPaint = paint;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, radius, sizeBrushPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        sizeBrushPaint.setAlpha(alpha);
    }

    public void setSize(float size) {
        this.radius = size / 2;
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        sizeBrushPaint.setColorFilter(cf);
    }

    public void setColor(int color) {
        sizeBrushPaint.setColor(color);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return sizeBrushPaint.getAlpha();
    }

    public void setOrigin(int x, int y) {
        centerX = x;
        centerY = y;
    }
}
