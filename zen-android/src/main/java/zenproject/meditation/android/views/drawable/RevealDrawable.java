package zenproject.meditation.android.views.drawable;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class RevealDrawable extends Drawable {

    private Paint revealPaint;
    private int color;
    private int radius;
    private long animationTime = 2000;

    protected float revealScale;
    protected int alpha;

    private Animator animator;
    private AnimatorSet animatorSet;

    /**
     * @param color         color
     * @param radius        radius
     * @param animationTime time
     */
    public RevealDrawable(int color, int radius, long animationTime) {
        this(color, radius);
        this.animationTime = animationTime;
    }

    /**
     * @param color  colro
     * @param radius radius
     */
    public RevealDrawable(int color, int radius) {
        this.color = color;
        this.radius = radius;
        this.revealScale = 0f;
        this.alpha = 255;

        revealPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        animatorSet = new AnimatorSet();

    }

    @Override
    public void draw(Canvas canvas) {

        final Rect bounds = getBounds();

        // circle
        revealPaint.setStyle(Paint.Style.FILL);
        revealPaint.setColor(color);
        canvas.drawCircle(bounds.width(), 0, radius * revealScale, revealPaint);

    }

    public void startAnimation() {
        generateAnimation().start();
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        revealPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return revealPaint.getAlpha();
    }

    private Animator generateAnimation() {
        ObjectAnimator revealAnimator = ObjectAnimator.ofFloat(this, "revealScale", 0f, 1f);
        revealAnimator.setDuration(animationTime);
        return revealAnimator;
    }
}