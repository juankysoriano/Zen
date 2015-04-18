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
import zenproject.meditation.android.ui.sketch.clear.SketchClearListener;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.FieldDeclarationsShouldBeAtStartOfClass"})
public class ZenSketchActivity extends ZenActivity implements View.OnAttachStateChangeListener {

    private final ZenSketch zenSketch;
    private final Navigator navigator;
    private final ScreenshotTaker screenshotTaker;
    private final SketchClearer sketchClearer;
    private final SketchSharer sketchSharer;
    private ZenSketchView zenSketchView;

    public ZenSketchActivity() {
        this(ZenSketch.newInstance(),
                Navigator.newInstance(),
                ScreenshotTaker.newInstance(),
                SketchClearer.newInstance(),
                SketchSharer.newInstance());
    }

    protected ZenSketchActivity(ZenSketch zenSketch,
                                Navigator navigator,
                                ScreenshotTaker screenshotTaker,
                                SketchClearer sketchClearer,
                                SketchSharer sketchSharer) {
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
        zenSketchView.setSketchClearListener(sketchClearListener);
        zenSketchView.getButtonViewFor(MenuButton.BRUSH).setOnClickListener(brushOptionsListener);
        zenSketchView.getButtonViewFor(MenuButton.FLOWER).setOnClickListener(flowerOptionsListener);
        zenSketchView.getButtonViewFor(MenuButton.RESTART).setOnClickListener(restartListener);
        zenSketchView.getButtonViewFor(MenuButton.MENU).setOnClickListener(menuToggleListener);
        zenSketchView.getButtonViewFor(MenuButton.SCREENSHOT).setOnClickListener(screenshotListener);
        zenSketchView.getButtonViewFor(MenuButton.SHARE).setOnClickListener(shareListener);
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
        zenSketchView.setSketchClearListener(null);
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
    private final View.OnClickListener menuToggleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            zenSketchView.getCircularMenu().toggle(true);
            zenSketchView.getButtonViewFor(MenuButton.MENU).rotate();
        }
    };

    private final View.OnClickListener restartListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            zenSketchView.startClearAnimation((FloatingActionButton) view);
        }
    };

    private final View.OnClickListener brushOptionsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navigator.openBrushSelectionDialogWith(getFragmentManager());
        }
    };

    private final View.OnClickListener flowerOptionsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navigator.openFlowerSelectionDialogWith(getFragmentManager());
        }
    };

    private final View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sketchSharer.shareSketch();
        }
    };

    private final View.OnClickListener screenshotListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            screenshotTaker.takeScreenshot();
        }
    };

    private final SketchClearListener sketchClearListener = new SketchClearListener() {
        @Override
        public void onSketchCleared() {
            sketchClearer.clearSketch();
        }
    };

}
