package zenproject.meditation.android.activities;

import android.support.v4.app.FragmentManager;

import com.novoda.notils.caster.Classes;

import zenproject.meditation.android.AnalyticsTracker;
import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.views.dialogs.brush.BrushOptionsDialog;
import zenproject.meditation.android.views.dialogs.flower.FlowerOptionsDialog;

public class Navigator {

    private final FragmentManager fragmentManager;

    protected Navigator(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public static Navigator newInstance() {
        ZenActivity zenActivity = Classes.from(ContextRetriever.INSTANCE.getActivityContext());
        return new Navigator(zenActivity.getSupportFragmentManager());
    }

    public void openBrushSelectionDialog() {
        BrushOptionsDialog brushDialog = new BrushOptionsDialog();
        brushDialog.show(fragmentManager, BrushOptionsDialog.TAG);
        AnalyticsTracker.INSTANCE.trackDialogOpened(BrushOptionsDialog.TAG);
    }

    public void openFlowerSelectionDialog() {
        FlowerOptionsDialog flowerOptionsDialog = new FlowerOptionsDialog();
        flowerOptionsDialog.show(fragmentManager, FlowerOptionsDialog.TAG);
        AnalyticsTracker.INSTANCE.trackDialogOpened(FlowerOptionsDialog.TAG);
    }
}
