package zenproject.meditation.android.activities;

import android.os.Bundle;
import android.view.View;

import zenproject.meditation.android.R;
import zenproject.meditation.android.sketch.ZenSketch;
import zenproject.meditation.android.sketch.actions.clear.SketchClearer;
import zenproject.meditation.android.sketch.actions.screenshot.ScreenshotTaker;
import zenproject.meditation.android.sketch.actions.share.SketchSharer;
import zenproject.meditation.android.ui.menu.buttons.FloatingActionButton;
import zenproject.meditation.android.ui.menu.buttons.MenuButton;
import zenproject.meditation.android.ui.sketch.ZenSketchView;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.FieldDeclarationsShouldBeAtStartOfClass"})
public class SketchActivity extends ZenActivity implements View.OnAttachStateChangeListener {

    private final ZenSketch zenSketch;
    private final Navigator navigator;
    private final ScreenshotTaker screenshotTaker;
    private final SketchClearer sketchClearer;
    private final SketchSharer sketchSharer;
    private ZenSketchView zenSketchView;

    public SketchActivity() {
        super();
        zenSketch = ZenSketch.newInstance();
        navigator = Navigator.newInstance(zenSketch);
        screenshotTaker = ScreenshotTaker.newInstance(zenSketch);
        sketchClearer = SketchClearer.newInstance(zenSketch);
        sketchSharer = SketchSharer.newInstance(zenSketch);
    }

    protected SketchActivity(ZenSketch zenSketch,
                             Navigator navigator,
                             ScreenshotTaker screenshotTaker,
                             SketchClearer sketchClearer,
                             SketchSharer sketchSharer) {
        super();
        this.zenSketch = zenSketch;
        this.navigator = navigator;
        this.screenshotTaker = screenshotTaker;
        this.sketchClearer = sketchClearer;
        this.sketchSharer = sketchSharer;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sketch);

        zenSketchView = (ZenSketchView) findViewById(R.id.sketch);
        zenSketchView.addOnAttachStateChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        zenSketch.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        zenSketch.resume();
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        zenSketch.injectInto(zenSketchView);
        attachListeners();

    }

    private void attachListeners() {
        zenSketchView.setOnClearListener(onClearListener);
        zenSketchView.getButtonViewFor(MenuButton.BRUSH).setOnClickListener(onBrushListener);
        zenSketchView.getButtonViewFor(MenuButton.FLOWER).setOnClickListener(onFlowersListener);
        zenSketchView.getButtonViewFor(MenuButton.RESTART).setOnClickListener(onRestartListener);
        zenSketchView.getButtonViewFor(MenuButton.MENU).setOnClickListener(onMenuToggleListener);
        zenSketchView.getButtonViewFor(MenuButton.SCREENSHOT).setOnClickListener(onScreenshotListener);
        zenSketchView.getButtonViewFor(MenuButton.SHARE).setOnClickListener(onShareListener);
        zenSketch.setOnPaintingListener(zenSketchView);
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        detachListeners();
    }

    @Override
    protected void onPause() {
        zenSketch.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        zenSketch.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zenSketch.destroy();
        super.onDestroy();
    }

    private void detachListeners() {
        zenSketchView.setOnClearListener(null);
        zenSketchView.getButtonViewFor(MenuButton.BRUSH).setOnClickListener(null);
        zenSketchView.getButtonViewFor(MenuButton.FLOWER).setOnClickListener(null);
        zenSketchView.getButtonViewFor(MenuButton.RESTART).setOnClickListener(null);
        zenSketchView.getButtonViewFor(MenuButton.MENU).setOnClickListener(null);
        zenSketchView.getButtonViewFor(MenuButton.SCREENSHOT).setOnClickListener(null);
        zenSketchView.getButtonViewFor(MenuButton.SHARE).setOnClickListener(null);
        zenSketch.setOnPaintingListener(null);
    }

    /**
     * Listeners which controls painting flow and menu operations.
     * TODO Consider extracting this listeners into classes. This way they can be tested.
     */
    private final View.OnClickListener onMenuToggleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            zenSketchView.getCircularMenu().toggle(true);
            zenSketchView.getButtonViewFor(MenuButton.MENU).rotate();
        }
    };

    private final View.OnClickListener onRestartListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            zenSketchView.startClearAnimation((FloatingActionButton) view);
        }
    };

    private final View.OnClickListener onBrushListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navigator.openBrushSelectionDialog();
        }
    };

    private final View.OnClickListener onFlowersListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navigator.openFlowerSelectionDialog();
        }
    };

    private final View.OnClickListener onShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sketchSharer.shareSketch();
        }
    };

    private final View.OnClickListener onScreenshotListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            screenshotTaker.takeScreenshot();
        }
    };

    private final ZenSketchView.OnClearListener onClearListener = new ZenSketchView.OnClearListener() {
        @Override
        public void onCleared() {
            sketchClearer.clearSketch();
        }
    };
}
