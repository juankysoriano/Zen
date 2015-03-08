package zenproject.meditation.android.views.dialogs.brush;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import zenproject.meditation.android.R;
// ...

public class BrushOptionsDialog extends DialogFragment implements ColorSelectedListener, SizeChangedListener {

    private ColorSelectedListener colorSelectedListener;
    private SizeChangedListener sizeChangedListener;

    public BrushOptionsDialog() {
        //no-op
    }

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
                .build();

        ColorView colorView = (ColorView) materialDialog.getCustomView().findViewById(R.id.color_picker);
        colorView.setColorSelectedListener(this);

        SizeView sizeView = (SizeView) materialDialog.getCustomView().findViewById(R.id.brush_size);
        sizeView.setSizeChangedListener(this);

        return materialDialog;
    }

    public void setColorSelectedListener(ColorSelectedListener colorSelectedListener) {
        this.colorSelectedListener = colorSelectedListener;
    }

    public void setSizeChangedListener(SizeChangedListener sizeChangedListener) {
        this.sizeChangedListener = sizeChangedListener;
    }

    @Override
    public void onColorSelected(int color) {
        if (colorSelectedListener != null) {
            colorSelectedListener.onColorSelected(color);
        }
    }

    @Override
    public void onSizeChanged(float pertentage) {
        if (sizeChangedListener != null) {
            sizeChangedListener.onSizeChanged(pertentage);
        }
    }
}