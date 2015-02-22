package zenproject.meditation.android.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MenuBackground extends View {

    Context context;
    Paint mButtonPaint;
    Paint mDrawablePaint;

    public MenuBackground(Context context) {
        super(context);
        this.context = context;
        init(Color.WHITE);
    }

    public void setFloatingActionButtonColor(int FloatingActionButtonColor) {
        init(FloatingActionButtonColor);
    }

    public void init(int FloatingActionButtonColor) {
        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mButtonPaint.setColor(FloatingActionButtonColor);
        mButtonPaint.setStyle(Paint.Style.FILL);
        mButtonPaint.setShadowLayer(10.0f, 0.0f, 3.5f, Color.argb(100, 0, 0, 0));
        mDrawablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (float) (getWidth() / 2.6), mButtonPaint);
    }

    static public class Builder {
        private final Activity activity;

        int color = Color.WHITE;

        public Builder(Context context) {

            this.activity = (Activity) context;
        }

        /**
         * Sets the FAB color
         */
        public Builder withColor(final int color) {
            this.color = color;
            return this;
        }

        public MenuBackground create() {
            final MenuBackground button = new MenuBackground(activity);
            button.setFloatingActionButtonColor(this.color);
            return button;
        }
    }
}