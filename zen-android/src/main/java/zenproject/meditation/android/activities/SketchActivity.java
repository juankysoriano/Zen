package zenproject.meditation.android.activities;

import android.os.Bundle;
import android.view.View;

import zenproject.meditation.android.R;
import zenproject.meditation.android.ui.menu.dialogs.Navigator;
import zenproject.meditation.android.sketch.ZenSketch;
import zenproject.meditation.android.ui.menu.Menu;
import zenproject.meditation.android.sketch.actions.clear.SketchClearer;
import zenproject.meditation.android.sketch.actions.screenshot.ScreenshotTaker;
import zenproject.meditation.android.sketch.actions.share.SketchSharer;
import zenproject.meditation.android.ui.menu.buttons.FloatingActionButton;
import zenproject.meditation.android.ui.menu.buttons.MenuButton;
import zenproject.meditation.android.ui.sketch.ZenSketchView;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.FieldDeclarationsShouldBeAtStartOfClass"})
public class SketchActivity extends ZenActivity implements View.OnAttachStateChangeListener {

    private final ZenSketch zenSketch;
    private Menu menu;
    private Navigator navigator;
    private ScreenshotTaker screenshotTaker;
    private SketchClearer sketchClearer;
    private SketchSharer sketchSharer;
    private ZenSketchView zenSketchView;

    public SketchActivity() {
        this(ZenSketch.newInstance());
    }

    protected SketchActivity(ZenSketch zenSketch) {
        this.zenSketch = zenSketch;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sketch);
        zenSketchView = (ZenSketchView) findViewById(R.id.sketch);
        zenSketchView.addOnAttachStateChangeListener(this);

        menu = Menu.newInstance(zenSketchView);
        navigator = Navigator.newInstance();
        screenshotTaker = ScreenshotTaker.newInstance();
        sketchClearer = SketchClearer.newInstance(zenSketchView);
        sketchSharer = SketchSharer.newInstance();
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
        zenSketch.setOnPaintingListener(menu);
        zenSketchView.setSketchClearListener(sketchClearer);
        getMenuOptionViewFor(MenuButton.BRUSH).setOnClickListener(brushOptionsListener);
        getMenuOptionViewFor(MenuButton.FLOWER).setOnClickListener(flowerOptionsListener);
        getMenuOptionViewFor(MenuButton.RESTART).setOnClickListener(restartListener);
        getMenuOptionViewFor(MenuButton.MENU).setOnClickListener(menuToggleListener);
        getMenuOptionViewFor(MenuButton.SCREENSHOT).setOnClickListener(screenshotListener);
        getMenuOptionViewFor(MenuButton.SHARE).setOnClickListener(shareListener);
    }

    private FloatingActionButton getMenuOptionViewFor(MenuButton menuButton) {
        return menu.getButtonViewFor(menuButton);
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        detachListeners();
    }

    private void detachListeners() {
        zenSketch.setOnPaintingListener(null);
        zenSketchView.setSketchClearListener(null);
        getMenuOptionViewFor(MenuButton.BRUSH).setOnClickListener(null);
        getMenuOptionViewFor(MenuButton.FLOWER).setOnClickListener(null);
        getMenuOptionViewFor(MenuButton.RESTART).setOnClickListener(null);
        getMenuOptionViewFor(MenuButton.MENU).setOnClickListener(null);
        getMenuOptionViewFor(MenuButton.SCREENSHOT).setOnClickListener(null);
        getMenuOptionViewFor(MenuButton.SHARE).setOnClickListener(null);
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

    /**
     * Listeners which controls painting flow and menu operations.
     * TODO Consider extracting this listeners into classes. This way they can be tested.
     */
    private final View.OnClickListener menuToggleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menu.toggle();
        }
    };

    private final View.OnClickListener restartListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sketchClearer.clearSketch();
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
}
