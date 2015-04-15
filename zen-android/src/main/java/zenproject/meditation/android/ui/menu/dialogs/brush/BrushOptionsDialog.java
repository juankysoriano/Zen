package zenproject.meditation.android.ui.menu.dialogs.brush;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import butterknife.ButterKnife;
import butterknife.InjectView;
import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.analytics.AnalyticsTracker;
import zenproject.meditation.android.persistence.BrushOptionsPreferences;
import zenproject.meditation.android.sketch.painting.ink.BrushColor;
import zenproject.meditation.android.ui.menu.dialogs.ZenDialog;
import zenproject.meditation.android.ui.menu.dialogs.brush.color.ColorListView;
import zenproject.meditation.android.ui.menu.dialogs.brush.size.SizeView;

public class BrushOptionsDialog extends ZenDialog implements ColorSelectedListener, SizeChangedListener {
    public static final String TAG = "BrushOptionsDialog";

    @InjectView(R.id.color_picker) ColorListView colorListView;
    @InjectView(R.id.brush_size) SizeView sizeView;
    private BrushColor selectedColor = BrushOptionsPreferences.newInstance().getBrushColor();
    private int selectedSize = BrushOptionsPreferences.newInstance().getBrushSize();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .title(ContextRetriever.INSTANCE.getResources().getString(R.string.brush_options_title))
                .backgroundColor(Color.WHITE)
                .customView(R.layout.brush_options_dialog, false)
                .positiveText(ContextRetriever.INSTANCE.getResources().getString(R.string.option_done))
                .positiveColorRes(R.color.colorPrimaryDark)
                .negativeText(ContextRetriever.INSTANCE.getResources().getString(R.string.option_cancel))
                .negativeColorRes(R.color.colorAccent)
                .theme(Theme.LIGHT)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        trackPreferences();
                        storePreferences();
                    }
                })
                .build();

        ButterKnife.inject(this, materialDialog.getCustomView());
        colorListView.setColorSelectedListener(this);
        sizeView.setSizeChangedListener(this);

        return materialDialog;
    }

    private void trackPreferences() {
        AnalyticsTracker.INSTANCE.trackBrush(selectedColor, selectedSize);
    }

    private void storePreferences() {
        BrushOptionsPreferences brushOptionsPreferences = BrushOptionsPreferences.newInstance();
        brushOptionsPreferences.applyBrushColor(selectedColor);
        brushOptionsPreferences.applyBrushSize(selectedSize);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onColorSelected(BrushColor color) {
        selectedColor = color;
        sizeView.updateInkDropImageColor(color.toAndroidColor());
    }

    @Override
    public void onSizeChanged(int size) {
        selectedSize = size;
    }
}
