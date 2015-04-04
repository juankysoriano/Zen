package zenproject.meditation.android.views.dialogs.flower;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import zenproject.meditation.android.AnalyticsTracker;
import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.preferences.Flower;
import zenproject.meditation.android.preferences.FlowerOptionPreferences;
import zenproject.meditation.android.views.dialogs.ZenDialog;
// ...

public class FlowerOptionsDialog extends ZenDialog implements FlowerSelectedListener {
    public static final String TAG = "FlowerOptionsDialog";

    private Flower selectedFlower = FlowerOptionPreferences.newInstance().getFlower();
    private FlowerSelectedListener flowerSelectedListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .title(ContextRetriever.INSTANCE.getCurrentResources().getString(R.string.flower_options_title))
                .backgroundColor(Color.WHITE)
                .customView(R.layout.flowers_options_dialog, false)
                .positiveText(ContextRetriever.INSTANCE.getCurrentResources().getString(R.string.option_done))
                .positiveColorRes(R.color.colorPrimaryDark)
                .negativeText(ContextRetriever.INSTANCE.getCurrentResources().getString(R.string.option_cancel))
                .negativeColorRes(R.color.colorAccent)
                .theme(Theme.LIGHT)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        if (hasFlowerSelectedListener()) {
                            flowerSelectedListener.onFlowerSelected(selectedFlower);
                        }
                        trackPreferences();
                        storePreferences();
                    }
                })
                .build();

        FlowerView flowerView = (FlowerView) materialDialog.getCustomView().findViewById(R.id.flower_picker);
        flowerView.setFlowerSelectedListener(this);

        return materialDialog;
    }

    private boolean hasFlowerSelectedListener() {
        return this.flowerSelectedListener != null;
    }

    public void setFlowerSelectedListener(FlowerSelectedListener flowerSelectedListener) {
        this.flowerSelectedListener = flowerSelectedListener;
    }

    private void trackPreferences() {
        AnalyticsTracker.INSTANCE.trackFlower(selectedFlower);
    }

    private void storePreferences() {
        FlowerOptionPreferences flowerOptionPreferences = FlowerOptionPreferences.newInstance();
        flowerOptionPreferences.applyFlower(selectedFlower);
    }

    @Override
    public void onFlowerSelected(Flower flower) {
        selectedFlower = flower;
    }
}
