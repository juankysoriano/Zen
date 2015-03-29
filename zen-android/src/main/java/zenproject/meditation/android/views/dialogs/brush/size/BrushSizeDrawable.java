package zenproject.meditation.android.views.dialogs.brush.size;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class BrushSizeDrawable extends Drawable {

    public static final int BORDER_ALPHA = 40;
    private final Paint borderBrushPaint;
    private final Paint sizeBrushPaint;
    private float radius;
    private int centerX;
    private int centerY;

    public static BrushSizeDrawable newInstance() {
        Paint borderBrushPaint = generateBorderPaint();
        Paint sizeBrushPaint = generateBrushSizePaint();
        return new BrushSizeDrawable(borderBrushPaint, sizeBrushPaint);
    }

    private static Paint generateBrushSizePaint() {
        Paint sizeBrushPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sizeBrushPaint.setStyle(Paint.Style.FILL);
        return sizeBrushPaint;
    }

    private static Paint generateBorderPaint() {
        Paint borderBrushPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderBrushPaint.setStyle(Paint.Style.STROKE);
        borderBrushPaint.setAlpha(BORDER_ALPHA);
        return borderBrushPaint;
    }

    protected BrushSizeDrawable(Paint borderBrushPaint, Paint sizeBrushPaint) {
        this.borderBrushPaint = borderBrushPaint;
        this.sizeBrushPaint = sizeBrushPaint;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, radius, sizeBrushPaint);
        canvas.drawCircle(centerX, centerY, radius, borderBrushPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        sizeBrushPaint.setAlpha(alpha);
        borderBrushPaint.setAlpha(BORDER_ALPHA);
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
