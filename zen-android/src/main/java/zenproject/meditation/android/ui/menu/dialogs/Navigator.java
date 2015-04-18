package zenproject.meditation.android.ui.menu.dialogs;

import android.app.FragmentManager;

import com.novoda.notils.caster.Classes;

import zenproject.meditation.android.SketchRetriever;
import zenproject.meditation.android.analytics.AnalyticsTracker;
import zenproject.meditation.android.ui.menu.dialogs.brush.BrushOptionsDialog;
import zenproject.meditation.android.ui.menu.dialogs.flower.FlowerOptionsDialog;
import zenproject.meditation.android.ui.menu.dialogs.flower.FlowerSelectedListener;

public class Navigator {

    private final BrushOptionsDialog brushOptionsDialog;
    private final FlowerOptionsDialog flowerOptionsDialog;

    protected Navigator(BrushOptionsDialog brushOptionsDialog, FlowerOptionsDialog flowerOptionsDialog) {
        this.brushOptionsDialog = brushOptionsDialog;
        this.flowerOptionsDialog = flowerOptionsDialog;
    }

    public static Navigator newInstance() {
        BrushOptionsDialog brushOptionsDialog = new BrushOptionsDialog();
        FlowerOptionsDialog flowerOptionsDialog = new FlowerOptionsDialog();
        FlowerSelectedListener flowerSelectedListener = Classes.from(SketchRetriever.INSTANCE.getZenSketch());
        flowerOptionsDialog.setFlowerSelectedListener(flowerSelectedListener);
        return new Navigator(brushOptionsDialog, flowerOptionsDialog);
    }

    public void openBrushSelectionDialogWith(FragmentManager fragmentManager) {
        if (!brushOptionsDialog.isAdded()) {
            brushOptionsDialog.show(fragmentManager, BrushOptionsDialog.TAG);
            AnalyticsTracker.INSTANCE.trackDialogOpened(BrushOptionsDialog.TAG);
        }
    }

    public void openFlowerSelectionDialogWith(FragmentManager fragmentManager) {
        if (!flowerOptionsDialog.isAdded()) {
            flowerOptionsDialog.show(fragmentManager, FlowerOptionsDialog.TAG);
            AnalyticsTracker.INSTANCE.trackDialogOpened(FlowerOptionsDialog.TAG);
        }
    }
}
