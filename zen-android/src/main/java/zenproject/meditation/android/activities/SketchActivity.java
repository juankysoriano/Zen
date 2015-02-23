package zenproject.meditation.android.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.dialogs.BrushOptionsDialog;
import zenproject.meditation.android.drawers.ZenSketch;
import zenproject.meditation.android.views.ZenSketchView;

public class SketchActivity extends FragmentActivity implements ZenSketch.OnPaintingListener, ZenSketchView.OnRevealListener {

    private ZenSketch zenSketch;
    private ZenSketchView zenSketchView;

    public SketchActivity() {
        zenSketch = ZenSketch.newInstance();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextRetriever.INSTANCE.inject(this);
        setContentView(R.layout.sketch);

        initUI();
        setupUI();
    }

    private void initUI() {
        zenSketchView = (ZenSketchView) findViewById(R.id.sketch);
    }

    private void setupUI() {
        zenSketch.injectInto(zenSketchView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        zenSketch.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        attachListeners();
        zenSketch.resume();
    }

    private void attachListeners() {
        zenSketchView.setOnRevealListener(this);
        zenSketch.setOnPaintingListener(this);
    }

    @Override
    protected void onPause() {
        zenSketch.pause();
        detachListeners();
        super.onPause();
    }

    private void detachListeners() {
        zenSketch.setOnPaintingListener(null);
    }

    @Override
    protected void onStop() {
        zenSketch.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ContextRetriever.INSTANCE.inject(null);
        zenSketch.destroy();
        super.onDestroy();
    }

    @Override
    public void onPaintingStart() {
        zenSketchView.hideControlsWithDelay();
    }

    @Override
    public void onPaintingEnd() {
        zenSketchView.showControlsWithDelay();
    }

    private final View.OnClickListener onRestartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            zenSketchView.startRestartAnimation();
        }
    };

    private final View.OnClickListener onPaintMenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
/*            if (paintingMenu.isExpanded()) {
                paintingMenu.collapse();
            } else {
                paintingMenu.expand();
            }*/
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

    @Override
    public void onRevealed() {
        zenSketch.clear();
    }
}
