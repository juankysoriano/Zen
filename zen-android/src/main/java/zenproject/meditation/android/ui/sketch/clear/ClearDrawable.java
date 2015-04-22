package zenproject.meditation.android.ui.sketch.clear;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateDecelerateInterpolator;

import zenproject.meditation.android.R;

public class ClearDrawable extends Drawable {

    private static final float CONCEALED = 0f;
    private static final float REVEALED = 1f;
    private static final int OPAQUE = 255;
    private static final int TRANSPARENT = 0;
    private static final int REVEAL_DURATION = 500;
    private final Paint clearPaint;
    private float radius;
    private Point origin;
    private float clearScale;
    private int clearAlpha;

    public static ClearDrawable newInstance(Context context) {
        Paint clearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int color = context.getResources().getColor(R.color.colorAccent);
        clearPaint.setColor(color);
        clearPaint.setStyle(Paint.Style.FILL);
        return new ClearDrawable(clearPaint);
    }

    protected ClearDrawable(Paint paint) {
        this.clearPaint = paint;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(origin.x, origin.y, radius * clearScale, clearPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        clearAlpha = alpha;
        clearPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public int getAlpha() {
        return clearAlpha;
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        clearPaint.setColorFilter(cf);
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    @Override
    public int getOpacity() {
        return clearPaint.getAlpha();
    }

    protected float getScale() {
        return clearScale;
    }

    protected void setScale(float revealScale) {
        this.clearScale = revealScale;
        invalidateSelf();
    }

    public void startRadiusAnimation(Animator.AnimatorListener animatorListener) {
        setAlpha(OPAQUE);

        Animator animator = generateRadiusAnimation();
        animator.addListener(animatorListener);
        animator.setDuration(REVEAL_DURATION);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    public void startAlphaAnimation(Animator.AnimatorListener animatorListener) {
        Animator animator = generateAlphaAnimation();
        animator.addListener(animatorListener);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setStartDelay(100);
        animator.setDuration(200);
        animator.start();
    }

    private Animator generateRadiusAnimation() {
        return ObjectAnimator.ofFloat(this, "scale", CONCEALED, REVEALED);
    }

    private Animator generateAlphaAnimation() {
        return ObjectAnimator.ofInt(this, "alpha", OPAQUE, TRANSPARENT);
    }
}
