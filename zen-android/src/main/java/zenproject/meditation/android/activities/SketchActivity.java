package zenproject.meditation.android.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.oguzdev.circularfloatingactionmenu.library.CircularMenu;

import zenproject.meditation.android.R;
import zenproject.meditation.android.dialogs.BrushOptionsDialog;
import zenproject.meditation.android.drawers.ZenSketch;
import zenproject.meditation.android.views.FloatingActionButton;
import zenproject.meditation.android.views.ZenSketchView;

import static zenproject.meditation.android.views.creators.FloatingActionButtonMenuCreator.MenuId;

public class SketchActivity extends ZenActivity {

    private ZenSketch zenSketch;
    private ZenSketchView zenSketchView;
    private CircularMenu circularMenu;
    private FloatingActionButton menuButton;
    private FloatingActionButton restartButton;

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

    private void setupUI() {
        zenSketch.injectInto(zenSketchView);
        circularMenu = zenSketchView.getCircularMenu();
        restartButton = (FloatingActionButton) circularMenu.findSubActionViewWithId(MenuId.RESTART_ID);
        menuButton = (FloatingActionButton) circularMenu.getActionView();
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

    private void attachListeners() {
        zenSketchView.setOnRevealListener(onRevealListener);
        zenSketch.setOnPaintingListener(zenSketchView);
        restartButton.setOnClickListener(onRestartListener);
        menuButton.setOnClickListener(onMenuToggleListener);
    }

    private void detachListeners() {
        zenSketchView.setOnRevealListener(null);
        zenSketch.setOnPaintingListener(null);
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
            BrushOptionsDialog editNameDialog = new BrushOptionsDialog();
            editNameDialog.show(fm, "fragment_edit_name");
            zenSketch.selectPainting();
        }
    };

    private final View.OnClickListener onCanvasListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private final View.OnClickListener onFlowersListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private final View.OnClickListener onMusicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private final View.OnClickListener onShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private final View.OnClickListener onSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private final ZenSketchView.OnRevealListener onRevealListener = new ZenSketchView.OnRevealListener() {
        @Override
        public void onRevealed() {
            zenSketch.clear();
        }
    };
}
