package zenproject.meditation.android.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.oguzdev.circularfloatingactionmenu.library.CircularMenu;

import zenproject.meditation.android.R;
import zenproject.meditation.android.drawers.ZenSketch;
import zenproject.meditation.android.views.ZenSketchView;
import zenproject.meditation.android.views.dialogs.brush.BrushOptionsDialog;
import zenproject.meditation.android.views.menu.FloatingActionButton;

import static zenproject.meditation.android.views.menu.creators.FloatingActionCircularMenuCreator.MenuId;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.FieldDeclarationsShouldBeAtStartOfClass"})
public class SketchActivity extends ZenActivity {

    private final ZenSketch zenSketch;
    private ZenSketchView zenSketchView;
    private CircularMenu circularMenu;
    private FloatingActionButton menuButton;
    private FloatingActionButton restartButton;
    private FloatingActionButton brushButton;

    public SketchActivity() {
        zenSketch = ZenSketch.newInstance();
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
        restartButton = (FloatingActionButton) circularMenu.findSubActionViewWithId(MenuId.RESTART_ID);
        brushButton = (FloatingActionButton) circularMenu.findSubActionViewWithId(MenuId.BRUSH_ID);
        menuButton = (FloatingActionButton) circularMenu.getActionView();
    }

    private void attachListeners() {
        zenSketchView.setOnRevealListener(onRevealListener);
        zenSketch.setOnPaintingListener(zenSketchView);
        brushButton.setOnClickListener(onBrushListener);
        restartButton.setOnClickListener(onRestartListener);
        menuButton.setOnClickListener(onMenuToggleListener);
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
        zenSketchView.setOnRevealListener(null);
        zenSketch.setOnPaintingListener(null);
        brushButton.setOnClickListener(null);
        restartButton.setOnClickListener(null);
        menuButton.setOnClickListener(null);
    }

    /**
     * Listeners which controls painting flow and menu operations.
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
            zenSketchView.startRestartAnimation((FloatingActionButton) view);
        }
    };

    private final View.OnClickListener onBrushListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getSupportFragmentManager();
            BrushOptionsDialog brushDialog = new BrushOptionsDialog();
            brushDialog.setColorSelectedListener(zenSketch);
            brushDialog.setSizeChangedListener(zenSketch);
            brushDialog.show(fm, "fragment_edit_name");
            zenSketch.selectPainting();
        }
    };

    /**
     * TODO open canvas options dialog
     */
    private final View.OnClickListener onCanvasListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //no-op
        }
    };

    /**
     * TODO open flowers options dialog
     */
    private final View.OnClickListener onFlowersListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //no-op
        }
    };

    /**
     * TODO open music options dialog
     */
    private final View.OnClickListener onMusicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //no-op
        }
    };

    /**
     * TODO do share
     */
    private final View.OnClickListener onShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //no-op
        }
    };

    /**
     * TODO do save
     */
    private final View.OnClickListener onSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //no-op
        }
    };

    private final ZenSketchView.OnRevealListener onRevealListener = new ZenSketchView.OnRevealListener() {
        @Override
        public void onRevealed() {
            zenSketch.clear();
        }
    };
}
