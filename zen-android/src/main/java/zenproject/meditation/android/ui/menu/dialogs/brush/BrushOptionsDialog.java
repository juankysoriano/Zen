package zenproject.meditation.android.ui.menu.dialogs.brush;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import androidx.annotation.NonNull;
import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.analytics.AnalyticsTracker;
import zenproject.meditation.android.preferences.BrushOptionsPreferences;
import zenproject.meditation.android.sketch.painting.ink.BrushColor;
import zenproject.meditation.android.ui.menu.dialogs.ZenDialog;
import zenproject.meditation.android.ui.menu.dialogs.brush.color.ColorListView;
import zenproject.meditation.android.ui.menu.dialogs.brush.size.SizeView;

// TODO this class could be provided with the required collaborators and then tested.
public class BrushOptionsDialog extends ZenDialog implements ColorSelectedListener, SizeChangedListener {
    public static final String TAG = "BrushOptionsDialog";

    private BrushColor selectedColor = BrushOptionsPreferences.newInstance().getBrushColor();
    private int selectedSize = BrushOptionsPreferences.newInstance().getBrushSizePercentage();
    private SizeView sizeView;

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

        ColorListView colorListView = materialDialog.getCustomView().findViewById(R.id.color_picker);
        colorListView.setColorSelectedListener(this);

        sizeView = materialDialog.getCustomView().findViewById(R.id.brush_size);
        sizeView.setSizeChangedListener(this);

        return materialDialog;
    }

    private void trackPreferences() {
        AnalyticsTracker.INSTANCE.trackBrush(selectedColor, selectedSize);
    }

    private void storePreferences() {
        BrushOptionsPreferences brushOptionsPreferences = BrushOptionsPreferences.newInstance();
        brushOptionsPreferences.applyBrushColor(selectedColor);
        brushOptionsPreferences.applyBrushSizePercentage(selectedSize);
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
