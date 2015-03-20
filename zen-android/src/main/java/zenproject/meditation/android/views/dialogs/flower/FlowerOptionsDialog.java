package zenproject.meditation.android.views.dialogs.flower;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import zenproject.meditation.android.R;
// ...

public class FlowerOptionsDialog extends DialogFragment {

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
                /*.callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        notifyColorSelected(selectedColor);
                        notifySizeChanged(selectedSize);
                        storePreferences();
                    }
                })*/
                .build();

        return materialDialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}