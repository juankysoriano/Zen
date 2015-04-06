package zenproject.meditation.android.ui.menu.dialogs.brush.color;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.novoda.notils.caster.Views;

import de.hdodenhof.circleimageview.CircleImageView;
import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.sketch.painting.ink.BrushColor;
import zenproject.meditation.android.persistence.BrushOptionsPreferences;
import zenproject.meditation.android.ui.menu.dialogs.brush.ColorSelectedListener;

import static zenproject.meditation.android.sketch.painting.ink.BrushColor.*;

public class ColorListView extends LinearLayout {
    private static final int UNSELECTED_BORDER_SIZE = ContextRetriever.INSTANCE.getResources().getDimensionPixelSize(R.dimen.divider_weight);
    private static final int SELECTED_BORDER_SIZE = ContextRetriever.INSTANCE.getResources().getDimensionPixelSize(R.dimen.color_selected_weight);
    private static final int DIVIDER = ContextRetriever.INSTANCE.getResources().getColor(R.color.divider);

    private CircleImageView darkColor;
    private CircleImageView greyColor;
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
        greyColor = Views.findById(this, R.id.grey_brush);
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
                notifyColorSelected(DARK);
            }
        });
        greyColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected((CircleImageView) v);
                notifyColorSelected(GREY);
            }
        });
        eraseColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected((CircleImageView) v);
                notifyColorSelected(ERASE);
            }
        });
        primaryColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected((CircleImageView) v);
                notifyColorSelected(PRIMARY);
            }
        });
        accentColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedWithColor((CircleImageView) v, PRIMARY.toAndroidColor());
                notifyColorSelected(ACCENT);
            }
        });
    }

    private void setSelectedFrom(BrushColor brushColor) {
        if (DARK == brushColor) {
            setSelected(darkColor);
        } else if (GREY == brushColor) {
            setSelected(greyColor);
        } else if (ERASE == brushColor) {
            setSelected(eraseColor);
        } else if (PRIMARY == brushColor) {
            setSelected(primaryColor);
        } else if (ACCENT == brushColor) {
            setSelectedWithColor(accentColor, PRIMARY.toAndroidColor());
        }
    }

    private void setSelected(CircleImageView circleImageView) {
        unselectAll();
        circleImageView.setBorderColor(ACCENT.toAndroidColor());
        circleImageView.setBorderWidth(SELECTED_BORDER_SIZE);
    }

    private void setSelectedWithColor(CircleImageView circleImageView, int color) {
        unselectAll();
        circleImageView.setBorderColor(color);
        circleImageView.setBorderWidth(SELECTED_BORDER_SIZE);
    }

    private void unselectAll() {
        darkColor.setBorderColor(DIVIDER);
        greyColor.setBorderColor(DIVIDER);
        eraseColor.setBorderColor(DIVIDER);
        primaryColor.setBorderColor(DIVIDER);
        accentColor.setBorderColor(DIVIDER);

        darkColor.setBorderWidth(UNSELECTED_BORDER_SIZE);
        greyColor.setBorderWidth(UNSELECTED_BORDER_SIZE);
        eraseColor.setBorderWidth(UNSELECTED_BORDER_SIZE);
        primaryColor.setBorderWidth(UNSELECTED_BORDER_SIZE);
        accentColor.setBorderWidth(UNSELECTED_BORDER_SIZE);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        darkColor.setOnClickListener(null);
        greyColor.setOnClickListener(null);
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
