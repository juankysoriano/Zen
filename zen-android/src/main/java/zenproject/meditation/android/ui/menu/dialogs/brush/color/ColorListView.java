package zenproject.meditation.android.ui.menu.dialogs.brush.color;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.novoda.notils.caster.Views;

import de.hdodenhof.circleimageview.CircleImageView;
import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.preferences.BrushOptionsPreferences;
import zenproject.meditation.android.sketch.painting.ink.BrushColor;
import zenproject.meditation.android.ui.menu.dialogs.brush.ColorSelectedListener;


@SuppressWarnings("PMD.TooManyMethods")
public class ColorListView extends LinearLayout {
    private static final int UNSELECTED_BORDER_SIZE = ContextRetriever.INSTANCE.getResources().getDimensionPixelSize(R.dimen.divider_weight);
    private static final int SELECTED_BORDER_SIZE = ContextRetriever.INSTANCE.getResources().getDimensionPixelSize(R.dimen.color_selected_weight);
    private static final int DIVIDER = ContextRetriever.INSTANCE.getResources().getColor(R.color.divider);

    private CircleImageView darkColor;
    private CircleImageView amberColor;
    private CircleImageView eraseColor;
    private CircleImageView primaryColor;
    private CircleImageView accentColor;

    private ColorSelectedListener colorSelectedListener;

    public ColorListView(Context context) {
        super(context);
    }

    public ColorListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        darkColor = Views.findById(this, R.id.dark_brush);
        amberColor = Views.findById(this, R.id.amber_brush);
        eraseColor = Views.findById(this, R.id.erase_brush);
        primaryColor = Views.findById(this, R.id.primary_brush);
        accentColor = Views.findById(this, R.id.accent_brush);

        setSelectedFrom(BrushOptionsPreferences.newInstance().getBrushColor());
    }

    private boolean hasColorSelectedListener() {
        return colorSelectedListener != null;
    }

    public void setColorSelectedListener(ColorSelectedListener colorSelectedListener) {
        this.colorSelectedListener = colorSelectedListener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        darkColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected((CircleImageView) v);
                notifyColorSelected(BrushColor.DARK);
            }
        });
        amberColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected((CircleImageView) v);
                notifyColorSelected(BrushColor.AMBER);
            }
        });
        eraseColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected((CircleImageView) v);
                notifyColorSelected(BrushColor.ERASE);
            }
        });
        primaryColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected((CircleImageView) v);
                notifyColorSelected(BrushColor.PRIMARY);
            }
        });
        accentColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedWithColor((CircleImageView) v, BrushColor.PRIMARY.toAndroidColor());
                notifyColorSelected(BrushColor.ACCENT);
            }
        });
    }

    private void setSelectedFrom(BrushColor brushColor) {
        if (BrushColor.DARK == brushColor) {
            setSelected(darkColor);
        } else if (BrushColor.AMBER == brushColor) {
            setSelected(amberColor);
        } else if (BrushColor.ERASE == brushColor) {
            setSelected(eraseColor);
        } else if (BrushColor.PRIMARY == brushColor) {
            setSelected(primaryColor);
        } else if (BrushColor.ACCENT == brushColor) {
            setSelectedWithColor(accentColor, BrushColor.PRIMARY.toAndroidColor());
        }
    }

    private void setSelected(CircleImageView circleImageView) {
        deselectAll();
        circleImageView.setBorderColor(BrushColor.ACCENT.toAndroidColor());
        circleImageView.setBorderWidth(SELECTED_BORDER_SIZE);
    }

    private void setSelectedWithColor(CircleImageView circleImageView, int color) {
        deselectAll();
        circleImageView.setBorderColor(color);
        circleImageView.setBorderWidth(SELECTED_BORDER_SIZE);
    }

    private void deselectAll() {
        darkColor.setBorderColor(DIVIDER);
        amberColor.setBorderColor(DIVIDER);
        eraseColor.setBorderColor(DIVIDER);
        primaryColor.setBorderColor(DIVIDER);
        accentColor.setBorderColor(DIVIDER);

        darkColor.setBorderWidth(UNSELECTED_BORDER_SIZE);
        amberColor.setBorderWidth(UNSELECTED_BORDER_SIZE);
        eraseColor.setBorderWidth(UNSELECTED_BORDER_SIZE);
        primaryColor.setBorderWidth(UNSELECTED_BORDER_SIZE);
        accentColor.setBorderWidth(UNSELECTED_BORDER_SIZE);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        darkColor.setOnClickListener(null);
        amberColor.setOnClickListener(null);
        eraseColor.setOnClickListener(null);
        primaryColor.setOnClickListener(null);
        accentColor.setOnClickListener(null);

    }

    private void notifyColorSelected(BrushColor color) {
        if (hasColorSelectedListener()) {
            colorSelectedListener.onColorSelected(color);
        }
    }
}
