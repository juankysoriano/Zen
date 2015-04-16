package zenproject.meditation.android.ui.menu.dialogs.brush.size;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.novoda.notils.caster.Views;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.persistence.BrushOptionsPreferences;
import zenproject.meditation.android.ui.menu.dialogs.brush.SizeChangedListener;

@SuppressWarnings("PMD.TooManyMethods")
public class SizeView extends LinearLayout {
    private static final int MAX_DROP_SIZE = ContextRetriever.INSTANCE.getResources().getDimensionPixelSize(R.dimen.ink_drop_max_radius);
    private static final int MIN_DROP_SIZE = ContextRetriever.INSTANCE.getResources().getDimensionPixelSize(R.dimen.ink_drop_min_radius);

    private static final float PERCENTAGE_FACTOR = 0.005f;

    private SeekBar sizeSeekBar;
    private BrushSizeImage inkDropImage;
    private BrushOptionsPreferences brushOptionsPreferences;

    private SizeChangedListener sizeChangedListener;

    public SizeView(Context context) {
        super(context);
    }

    public SizeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SizeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        sizeSeekBar = Views.findById(this, R.id.brush_size_slider);
        inkDropImage = Views.findById(this, R.id.brush_size_image);
        brushOptionsPreferences = BrushOptionsPreferences.newInstance();

        sizeSeekBar.setProgress(brushOptionsPreferences.getBrushSizePercentage());
        updateInkDropImageColor(brushOptionsPreferences.getBrushColor().toAndroidColor());
    }

    private boolean hasSizeChangedListener() {
        return sizeChangedListener != null;
    }

    public void setSizeChangedListener(SizeChangedListener sizeChangedListener) {
        this.sizeChangedListener = sizeChangedListener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        sizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                changeInkDropImageSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //no-op
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                notifySizeChangedListener(seekBar.getProgress());
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        changeInkDropImageSize(brushOptionsPreferences.getBrushSizePercentage());
    }

    private void changeInkDropImageSize(int progress) {
        float targetSize = Math.max(MIN_DROP_SIZE, MAX_DROP_SIZE * progress * PERCENTAGE_FACTOR);
        inkDropImage.setSize(targetSize);
    }

    public void updateInkDropImageColor(int color) {
        inkDropImage.setColor(color);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        sizeSeekBar.setOnSeekBarChangeListener(null);
    }

    private void notifySizeChangedListener(int percentage) {
        if (hasSizeChangedListener()) {
            sizeChangedListener.onSizeChanged(percentage);
        }
    }
}
