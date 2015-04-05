package zenproject.meditation.android.activities;

import com.novoda.notils.caster.Classes;

import zenproject.meditation.android.analytics.AnalyticsTracker;
import zenproject.meditation.android.ContextRetriever;
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

    public static Navigator newInstance(FlowerSelectedListener flowerSelectedListener) {
        BrushOptionsDialog brushOptionsDialog = new BrushOptionsDialog();
        FlowerOptionsDialog flowerOptionsDialog = new FlowerOptionsDialog();
        flowerOptionsDialog.setFlowerSelectedListener(flowerSelectedListener);
        return new Navigator(brushOptionsDialog, flowerOptionsDialog);
    }

    public void openBrushSelectionDialog() {
        if (!brushOptionsDialog.isAdded()) {
            ZenActivity zenActivity = Classes.from(ContextRetriever.INSTANCE.getActivityContext());

            brushOptionsDialog.show(zenActivity.getSupportFragmentManager(), BrushOptionsDialog.TAG);
            AnalyticsTracker.INSTANCE.trackDialogOpened(BrushOptionsDialog.TAG);
        }
    }

    public void openFlowerSelectionDialog() {
        if (!flowerOptionsDialog.isAdded()) {
            ZenActivity zenActivity = Classes.from(ContextRetriever.INSTANCE.getActivityContext());
            flowerOptionsDialog.show(zenActivity.getSupportFragmentManager(), FlowerOptionsDialog.TAG);
            AnalyticsTracker.INSTANCE.trackDialogOpened(FlowerOptionsDialog.TAG);
        }
    }
}
