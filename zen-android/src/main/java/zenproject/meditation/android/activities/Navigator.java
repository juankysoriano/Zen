package zenproject.meditation.android.activities;

import android.app.FragmentManager;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.analytics.AnalyticsTracker;
import zenproject.meditation.android.ui.menu.dialogs.brush.BrushOptionsDialog;
import zenproject.meditation.android.ui.menu.dialogs.flower.FlowerOptionsDialog;
import zenproject.meditation.android.ui.menu.dialogs.flower.FlowerSelectedListener;

public class Navigator {

    private final BrushOptionsDialog brushOptionsDialog;
    private final FlowerOptionsDialog flowerOptionsDialog;
    private final FragmentManager fragmentManager;

    protected Navigator(FragmentManager fragmentManager, BrushOptionsDialog brushOptionsDialog, FlowerOptionsDialog flowerOptionsDialog) {
        this.fragmentManager = fragmentManager;
        this.brushOptionsDialog = brushOptionsDialog;
        this.flowerOptionsDialog = flowerOptionsDialog;
    }

    public static Navigator newInstance(FlowerSelectedListener flowerSelectedListener) {
        BrushOptionsDialog brushOptionsDialog = new BrushOptionsDialog();
        FlowerOptionsDialog flowerOptionsDialog = new FlowerOptionsDialog();
        flowerOptionsDialog.setFlowerSelectedListener(flowerSelectedListener);
        FragmentManager fragmentManager = ContextRetriever.INSTANCE.getActivity().getFragmentManager();
        return new Navigator(fragmentManager,brushOptionsDialog, flowerOptionsDialog);
    }

    public void openBrushSelectionDialog() {
        if (!brushOptionsDialog.isAdded()) {
            brushOptionsDialog.show(fragmentManager, BrushOptionsDialog.TAG);
            AnalyticsTracker.INSTANCE.trackDialogOpened(BrushOptionsDialog.TAG);
        }
    }

    public void openFlowerSelectionDialog() {
        if (!flowerOptionsDialog.isAdded()) {
            flowerOptionsDialog.show(fragmentManager, FlowerOptionsDialog.TAG);
            AnalyticsTracker.INSTANCE.trackDialogOpened(FlowerOptionsDialog.TAG);
        }
    }
}
