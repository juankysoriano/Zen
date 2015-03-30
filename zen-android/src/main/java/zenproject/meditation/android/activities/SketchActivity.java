package zenproject.meditation.android.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.novoda.notils.logger.toast.Toaster;
import com.oguzdev.circularfloatingactionmenu.library.CircularMenu;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.sketch.ZenSketch;
import zenproject.meditation.android.views.ZenSketchView;
import zenproject.meditation.android.views.dialogs.brush.BrushOptionsDialog;
import zenproject.meditation.android.views.dialogs.flower.FlowerOptionsDialog;
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
    private FloatingActionButton flowersButton;
    private FloatingActionButton saveButton;

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
        flowersButton = (FloatingActionButton) circularMenu.findSubActionViewWithId(MenuId.FLOWERS_ID);
        saveButton = (FloatingActionButton) circularMenu.findSubActionViewWithId(MenuId.SAVE_ID);
        menuButton = (FloatingActionButton) circularMenu.getActionView();
    }

    private void attachListeners() {
        zenSketchView.setOnRevealListener(onRevealListener);
        zenSketch.setOnPaintingListener(zenSketchView);
        brushButton.setOnClickListener(onBrushListener);
        flowersButton.setOnClickListener(onFlowersListener);
        restartButton.setOnClickListener(onRestartListener);
        menuButton.setOnClickListener(onMenuToggleListener);
        saveButton.setOnClickListener(onSaveListener);
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
        flowersButton.setOnClickListener(null);
        restartButton.setOnClickListener(null);
        menuButton.setOnClickListener(null);
        saveButton.setOnClickListener(null);
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
            brushDialog.show(fm, "fragment_edit_name");
        }
    };

    /**
     * TODO open flowers options dialog
     */
    private final View.OnClickListener onFlowersListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getSupportFragmentManager();
            FlowerOptionsDialog flowersDialog = new FlowerOptionsDialog();
            flowersDialog.setFlowerSelectedListener(zenSketch);
            flowersDialog.show(fm, "fragment_edit_name");
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
            zenSketch.getRainbowDrawer().save("Zen", "User sketch");
            Toaster.newInstance(ContextRetriever.INSTANCE.getCurrentContext()).popToast(R.string.save_sketch);
        }
    };

    private final ZenSketchView.OnRevealListener onRevealListener = new ZenSketchView.OnRevealListener() {
        @Override
        public void onRevealed() {
            zenSketch.clear();
        }
    };
}
