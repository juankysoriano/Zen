package zenproject.meditation.android.ui.menu.dialogs.brush.size;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class BrushSizeImage extends View {
    private BrushSizeDrawable brushSizeDrawable;

    public BrushSizeImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BrushSizeImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        brushSizeDrawable = BrushSizeDrawable.newInstance();
        setBackground(brushSizeDrawable);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    public void setSize(float size) {
        brushSizeDrawable.setSize(Math.min(getHeight(), size));
    }

    public void setColor(int color) {
        brushSizeDrawable.setColor(color);
    }

    public void setOrigin(int x, int y) {
        brushSizeDrawable.setOrigin(x, y);
    }
}
