package zenproject.meditation.android.views.dialogs.brush;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.novoda.notils.caster.Views;

import de.hdodenhof.circleimageview.CircleImageView;
import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.preferences.BrushOptionsPreferences;

public class SizeView extends LinearLayout {
    private static final int MAX_DROP_SIZE = ContextRetriever.INSTANCE.getCurrentContext().getResources().getDimensionPixelSize(R.dimen.ink_drop_max_radius);
    public static final float PERCENTAGE_FACTOR = 0.01f;

    private SeekBar sizeSeekBar;
    private CircleImageView inkDropImage;

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
        sizeSeekBar.setProgress(BrushOptionsPreferences.newInstance().getBrushSize());
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

    private void changeInkDropImageSize(int progress) {
        float currentSize = inkDropImage.getWidth();
        float desiredSize = progress * MAX_DROP_SIZE * PERCENTAGE_FACTOR / currentSize;
        inkDropImage.setScaleX(desiredSize);
        inkDropImage.setScaleY(desiredSize);
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