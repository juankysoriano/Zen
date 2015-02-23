package zenproject.meditation.android.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewPropertyAnimator;

public class FloatingActionButton extends View {
    private static final int DURATION = 400;
    private static final int ROTATED = 90;
    private static final int NOT_ROTATED = 0;
    private Paint mButtonPaint;
    private Paint mDrawablePaint;
    private Bitmap mBitmap;
    private boolean mHidden = false;
    private boolean mTouching = false;
    private boolean mRotated = false;
    private boolean mRotating = false;
    private int color;
    private int pressedColor;
    protected float angle = NOT_ROTATED;

    public FloatingActionButton(Context context) {
        super(context);
        init(Color.WHITE);
    }

    public void setFloatingActionButtonColor(int FloatingActionButtonColor) {
        this.color = FloatingActionButtonColor;
    }

    public void setFloatingActionPressedButtonColor(int FloatingActionButtonPressedColor) {
        this.pressedColor = FloatingActionButtonPressedColor;
    }

    public void setFloatingActionButtonDrawable(Bitmap FloatingActionButtonDrawable) {
        mBitmap = FloatingActionButtonDrawable;
        invalidate();
    }

    public void init(int FloatingActionButtonColor) {
        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mButtonPaint.setStyle(Paint.Style.FILL);
        mDrawablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setClickable(true);
        mButtonPaint.setShadowLayer(mTouching ? 15 : 10, 0.0f, 3.5f, Color.argb(mTouching ? 200 : 100, 0, 0, 0));
        mButtonPaint.setColor(mTouching ? pressedColor : color);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (float) (getWidth() / 2.6), mButtonPaint);
        canvas.save();
        canvas.rotate(angle, getWidth() / 2, getHeight() / 2);
        canvas.drawBitmap(mBitmap, (getWidth() - mBitmap.getWidth()) / 2,
                (getHeight() - mBitmap.getHeight()) / 2, mDrawablePaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mTouching = false;
            invalidate();
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mTouching = true;
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    public void hide() {
        if (!isHidden()) {
            ViewPropertyAnimator.animate(this).setInterpolator(new AccelerateDecelerateInterpolator())
                    .translationY(-getBottom());
            mHidden = true;
        }
    }

    public void show() {
        if (isHidden()) {
            ViewPropertyAnimator.animate(this).setInterpolator(new AccelerateDecelerateInterpolator())
                    .translationY(0);
            mHidden = false;
        }
    }

    public void rotate() {
        if (!mRotating) {
            float startAngle = mRotated ? ROTATED : NOT_ROTATED;
            float endAngle = mRotated ? NOT_ROTATED : ROTATED;
            Animator animator = ObjectAnimator.ofFloat(this, "angle", startAngle, endAngle);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(DURATION);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mRotating = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mRotated = !mRotated;
                    mRotating = false;
                }
            });
            animator.start();
        }

    }

    public boolean isHidden() {
        return mHidden;
    }

    public Point getCentre() {
        return new Point((int) getX() + getWidth() / 2,
                (int) getY() + getHeight() / 2);
    }

    protected float getAngle() {
        return angle;
    }

    protected void setAngle(float angle) {
        this.angle = angle;
        invalidate();
    }

    static public class Builder {
        int id;
        private FrameLayout.LayoutParams params;
        private final Activity activity;
        int gravity = Gravity.BOTTOM | Gravity.RIGHT; // default bottom right
        Drawable drawable;
        int color = Color.WHITE;
        int pressedColor;
        int size;
        float scale;

        public Builder(Context context) {
            scale = context.getResources().getDisplayMetrics().density;
            size = convertToPixels(72, scale); // default size is 72dp by 72dp
            params = new FrameLayout.LayoutParams(size, size);
            params.gravity = gravity;

            this.activity = (Activity) context;
        }

        /**
         * Sets the gravity for the FAB
         */
        public Builder withGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * Sets the margins for the FAB in dp
         */
        public Builder withMargins(int left, int top, int right, int bottom) {
            params.setMargins(left, top, right, bottom);
            return this;
        }

        /**
         * Sets the FAB drawable
         */
        public Builder withDrawable(final Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        /**
         * Sets the ID
         */
        public Builder withId(final int id) {
            this.id = id;
            return this;
        }

        /**
         * Set the Tag
         */

        /**
         * Sets the FAB color
         */
        public Builder withButtonColor(final int color) {
            this.color = color;
            return this;
        }

        /**
         * Sets the FAB color
         */
        public Builder withButtonPressedColor(final int color) {
            this.pressedColor = color;
            return this;
        }

        /**
         * Sets the FAB size in dp
         */
        public Builder withButtonSize(int size) {
            params = new FrameLayout.LayoutParams(size, size);
            return this;
        }

        public FloatingActionButton create() {
            final FloatingActionButton button = new FloatingActionButton(activity);
            button.setFloatingActionButtonColor(this.color);
            button.setFloatingActionPressedButtonColor(this.pressedColor == 0 ? getDarkerFrom(this.color) : this.pressedColor);
            button.setFloatingActionButtonDrawable(Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), params.width / 2, params.height / 2, true));
            button.setId(this.id);
            params.gravity = this.gravity;
            ViewGroup root = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
            root.addView(button, params);
            return button;
        }

        private int getDarkerFrom(int color) {
            return Color.rgb((int) (Color.red(color) / 1.25f), (int) (Color.green(color) / 1.25f), (int) (Color.blue(color) / 1.25f));
        }

        private int convertToPixels(int dp, float scale) {
            return (int) (dp * scale + 0.5f);
        }
    }
}