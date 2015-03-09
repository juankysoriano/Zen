package zenproject.meditation.android.views.dialogs.brush;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import zenproject.meditation.android.R;
import zenproject.meditation.android.preferences.BrushOptionsPreferences;
// ...

public class BrushOptionsDialog extends DialogFragment implements ColorSelectedListener, SizeChangedListener {

    private ColorSelectedListener colorSelectedListener;
    private SizeChangedListener sizeChangedListener;
    private int selectedColor = BrushOptionsPreferences.newInstance().getBrushColor();
    private int selectedSize = BrushOptionsPreferences.newInstance().getBrushSize();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .title("Brush options")
                .backgroundColor(Color.WHITE)
                .customView(R.layout.brush_options_dialog, false)
                .positiveText("Done")
                .positiveColorRes(R.color.colorPrimaryDark)
                .negativeText("Cancel")
                .negativeColorRes(R.color.colorAccent)
                .theme(Theme.LIGHT)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        notifyColorSelected(selectedColor);
                        notifySizeChanged(selectedSize);
                        storePreferences();
                    }
                })
                .build();

        ColorView colorView = (ColorView) materialDialog.getCustomView().findViewById(R.id.color_picker);
        colorView.setColorSelectedListener(this);

        SizeView sizeView = (SizeView) materialDialog.getCustomView().findViewById(R.id.brush_size);
        sizeView.setSizeChangedListener(this);

        return materialDialog;
    }

    private void storePreferences() {
        BrushOptionsPreferences brushOptionsPreferences = BrushOptionsPreferences.newInstance();
        brushOptionsPreferences.applyBrushColor(selectedColor);
        brushOptionsPreferences.applyBrushSize(selectedSize);
    }

    public void setColorSelectedListener(ColorSelectedListener colorSelectedListener) {
        this.colorSelectedListener = colorSelectedListener;
    }

    public void setSizeChangedListener(SizeChangedListener sizeChangedListener) {
        this.sizeChangedListener = sizeChangedListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onColorSelected(int color) {
        selectedColor = color;
    }

    private void notifyColorSelected(int color) {
        if (colorSelectedListener != null) {
            colorSelectedListener.onColorSelected(color);
        }
    }

    @Override
    public void onSizeChanged(int size) {
        selectedSize = size;
    }

    private void notifySizeChanged(int size) {
        if (sizeChangedListener != null) {
            sizeChangedListener.onSizeChanged(size);
        }
    }
}