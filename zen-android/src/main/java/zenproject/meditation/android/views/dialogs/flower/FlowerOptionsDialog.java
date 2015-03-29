package zenproject.meditation.android.views.dialogs.flower;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import zenproject.meditation.android.R;
import zenproject.meditation.android.preferences.Flower;
import zenproject.meditation.android.preferences.FlowerOptionPreferences;
import zenproject.meditation.android.views.dialogs.ZenDialog;
// ...

public class FlowerOptionsDialog extends ZenDialog implements FlowerSelectedListener {

    private Flower selectedFlower = FlowerOptionPreferences.newInstance().getFlower();
    private FlowerSelectedListener flowerSelectedListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .title("Flower options")
                .backgroundColor(Color.WHITE)
                .customView(R.layout.flowers_options_dialog, false)
                .positiveText("Done")
                .positiveColorRes(R.color.colorPrimaryDark)
                .negativeText("Cancel")
                .negativeColorRes(R.color.colorAccent)
                .theme(Theme.LIGHT)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        storePreferences();
                        flowerSelectedListener.onFlowerSelected(selectedFlower);
                    }
                })
                .build();

        FlowerView flowerView = (FlowerView) materialDialog.getCustomView().findViewById(R.id.flower_picker);
        flowerView.setFlowerSelectedListener(this);

        return materialDialog;
    }

    private void storePreferences() {
        FlowerOptionPreferences flowerOptionPreferences = FlowerOptionPreferences.newInstance();
        flowerOptionPreferences.applyFlower(selectedFlower);
    }

    @Override
    public void onFlowerSelected(Flower flower) {
        selectedFlower = flower;
    }

    public void setFlowerSelectedListener(FlowerSelectedListener flowerSelectedListener) {
        this.flowerSelectedListener = flowerSelectedListener;
    }
}