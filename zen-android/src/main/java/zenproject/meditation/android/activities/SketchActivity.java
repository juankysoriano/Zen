package zenproject.meditation.android.activities;

import android.os.Bundle;
import android.view.View;

import com.oguzdev.circularfloatingactionmenu.library.CircularMenu;

import zenproject.meditation.android.R;
import zenproject.meditation.android.sketch.ZenSketch;
import zenproject.meditation.android.sketch.actions.clear.SketchClearer;
import zenproject.meditation.android.sketch.actions.screenshot.ScreenshotTaker;
import zenproject.meditation.android.ui.sketch.ZenSketchView;
import zenproject.meditation.android.ui.menu.buttons.FloatingActionButton;

import static zenproject.meditation.android.ui.menu.buttons.MenuButton.*;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.FieldDeclarationsShouldBeAtStartOfClass"})
public class SketchActivity extends ZenActivity implements View.OnAttachStateChangeListener {

    private final ZenSketch zenSketch;
    private final Navigator navigator;
    private final ScreenshotTaker screenshotTaker;
    private final SketchClearer sketchClearer;
    private ZenSketchView zenSketchView;
    private CircularMenu circularMenu;
    private FloatingActionButton menuButton;
    private FloatingActionButton restartButton;
    private FloatingActionButton brushButton;
    private FloatingActionButton flowersButton;
    private FloatingActionButton screenshotButton;

    public SketchActivity() {
        super();
        zenSketch = ZenSketch.newInstance();
        navigator = Navigator.newInstance(zenSketch);
        screenshotTaker = ScreenshotTaker.newInstance(zenSketch);
        sketchClearer = SketchClearer.newInstance(zenSketch);
    }

    protected SketchActivity(ZenSketch zenSketch, Navigator navigator, ScreenshotTaker screenshotTaker, SketchClearer sketchClearer) {
        super();
        this.zenSketch = zenSketch;
        this.navigator = navigator;
        this.screenshotTaker = screenshotTaker;
        this.sketchClearer = sketchClearer;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sketch);

        initUI();
    }

    private void initUI() {
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
        setupUI();
        attachListeners();
    }

    private void setupUI() {
        zenSketch.injectInto(zenSketchView);
        circularMenu = zenSketchView.getCircularMenu();
        restartButton = (FloatingActionButton) circularMenu.findSubActionViewWithId(RESTART.getId());
        brushButton = (FloatingActionButton) circularMenu.findSubActionViewWithId(BRUSH.getId());
        flowersButton = (FloatingActionButton) circularMenu.findSubActionViewWithId(FLOWER.getId());
        screenshotButton = (FloatingActionButton) circularMenu.findSubActionViewWithId(SAVE.getId());
        menuButton = (FloatingActionButton) circularMenu.getActionView();
    }

    private void attachListeners() {
        zenSketchView.setOnClearListener(onClearListener);
        zenSketch.setOnPaintingListener(zenSketchView);
        brushButton.setOnClickListener(onBrushListener);
        flowersButton.setOnClickListener(onFlowersListener);
        restartButton.setOnClickListener(onRestartListener);
        menuButton.setOnClickListener(onMenuToggleListener);
        screenshotButton.setOnClickListener(onScreenshotListener);
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
        zenSketch.setOnPaintingListener(null);
        brushButton.setOnClickListener(null);
        flowersButton.setOnClickListener(null);
        restartButton.setOnClickListener(null);
        menuButton.setOnClickListener(null);
        screenshotButton.setOnClickListener(null);
    }

    /**
     * Listeners which controls painting flow and menu operations.
     * TODO Consider extracting this listeners into classes. This way they can be tested.
     */
    private final View.OnClickListener onMenuToggleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            circularMenu.toggle(true);
            menuButton.rotate();
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

    /**
     * TODO open music options dialog. Implement what's needed to have a fully working music feature.
     */
    private final View.OnClickListener onMusicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //no-op
        }
    };

    /**
     * TODO do share sketch with friends feature.
     */
    private final View.OnClickListener onShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //no-op
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
        public void onRevealed() {
            sketchClearer.clearSketch();
        }
    };
}
