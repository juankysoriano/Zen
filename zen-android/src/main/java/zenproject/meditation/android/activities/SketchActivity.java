package zenproject.meditation.android.activities;

import android.os.Bundle;

import zenproject.meditation.android.R;
import zenproject.meditation.android.sketch.ZenSketch;
import zenproject.meditation.android.sketch.actions.clear.SketchClearer;
import zenproject.meditation.android.sketch.actions.share.SketchSharer;
import zenproject.meditation.android.ui.menu.ZenMenu;
import zenproject.meditation.android.ui.menu.buttons.MenuButton;
import zenproject.meditation.android.ui.menu.dialogs.Navigator;
import zenproject.meditation.android.ui.sketch.ZenSketchView;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.FieldDeclarationsShouldBeAtStartOfClass"})
public class SketchActivity extends ZenActivity {

    private ZenSketch zenSketch;
    private ZenMenu zenMenu;
    private Navigator navigator;
    private SketchClearer sketchClearer;
    private SketchSharer sketchSharer;
    private ZenSketchView zenSketchView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sketch);
        zenSketchView = findViewById(R.id.sketch);
        zenSketch = ZenSketch.newInstance(zenSketchView);
        zenMenu = ZenMenu.newInstance(zenSketchView);
        navigator = Navigator.newInstance();
        sketchClearer = SketchClearer.newInstance(zenSketchView);
        sketchSharer = SketchSharer.newInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        zenSketch.start();
        zenSketch.setOnPaintingListener(zenMenu);
        zenSketchView.setSketchClearListener(sketchClearer);
        zenMenu.getButtonViewFor(MenuButton.BRUSH).setOnClickListener(v -> navigator.openBrushSelectionDialog());
        zenMenu.getButtonViewFor(MenuButton.FLOWER).setOnClickListener(v -> navigator.openFlowerSelectionDialog());
        zenMenu.getButtonViewFor(MenuButton.RESTART).setOnClickListener(view -> sketchClearer.clearSketch());
        zenMenu.getButtonViewFor(MenuButton.MENU).setOnClickListener(v -> zenMenu.toggle());
        zenMenu.getButtonViewFor(MenuButton.SHARE).setOnClickListener(v -> sketchSharer.shareSketch());
    }

    @Override
    protected void onResume() {
        super.onResume();
        zenSketch.resume();
    }

    @Override
    protected void onPause() {
        zenSketch.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        zenSketch.stop();
        zenSketch.setOnPaintingListener(null);
        zenSketchView.setSketchClearListener(null);
        zenMenu.getButtonViewFor(MenuButton.BRUSH).setOnClickListener(null);
        zenMenu.getButtonViewFor(MenuButton.FLOWER).setOnClickListener(null);
        zenMenu.getButtonViewFor(MenuButton.RESTART).setOnClickListener(null);
        zenMenu.getButtonViewFor(MenuButton.MENU).setOnClickListener(null);
        zenMenu.getButtonViewFor(MenuButton.SHARE).setOnClickListener(null);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zenSketch.destroy();
        super.onDestroy();
    }
}
