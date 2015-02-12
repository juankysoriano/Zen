package zenproject.meditation.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.melnykov.fab.FloatingActionButton;

import zenproject.meditation.android.R;
import zenproject.meditation.android.drawers.ZenSketch;

public class ZenSketchView extends RelativeLayout implements ZenSketch.OnPaintingListener {

    private static final int MILLISECONDS_TO_HIDE = 150;
    private static final int MILLISECONDS_TO_SHOW = 150;
    private ZenSketch zenSketch;
    private SequentialButtonAnimator sequentialButtonAnimator;
    private FloatingActionButton brushButton;
    private FloatingActionButton eraseButton;
    private FloatingActionButton flowersButton;
    private FloatingActionButton canvasButton;
    private FloatingActionButton musicButton;
    private FloatingActionButton shareButton;
    private FloatingActionButton saveButton;
    private TopFloatingActionButton restartButton;

    public ZenSketchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZenSketchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        zenSketch = ZenSketch.newInstance(this);
        sequentialButtonAnimator = SequentialButtonAnimator.newInstance();
        brushButton = (FloatingActionButton) findViewById(R.id.brush_button);
        eraseButton = (FloatingActionButton) findViewById(R.id.erase_button);
        flowersButton = (FloatingActionButton) findViewById(R.id.flowers_button);
        canvasButton = (FloatingActionButton) findViewById(R.id.canvas_button);
        musicButton = (FloatingActionButton) findViewById(R.id.music_button);
        shareButton = (FloatingActionButton) findViewById(R.id.share_button);
        saveButton = (FloatingActionButton) findViewById(R.id.save_button);
        restartButton = TopFloatingActionButton.from((FloatingActionButton) findViewById(R.id.reset_button));

        sequentialButtonAnimator.add(brushButton, eraseButton, flowersButton, canvasButton, musicButton, shareButton, saveButton);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attachListeners();
    }

    private void attachListeners() {
        brushButton.setOnClickListener(onBrushSelectedListener);
        eraseButton.setOnClickListener(onEraseSelectedListener);
        flowersButton.setOnClickListener(onFlowersSelectedListener);
        canvasButton.setOnClickListener(onCanvasSelectedListener);
        musicButton.setOnClickListener(onMusicSelectedListener);
        shareButton.setOnClickListener(onShareSelectedListener);
        saveButton.setOnClickListener(onSaveSelectedListener);
        restartButton.setOnClickListener(onRestartSelectedListener);
        zenSketch.setOnPaintingListener(this);
    }

    private View.OnClickListener onBrushSelectedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            zenSketch.enablePainting();
            zenSketch.disableErasing();
        }
    };
    private View.OnClickListener onEraseSelectedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            zenSketch.enableErasing();
            zenSketch.disablePainting();
        }
    };
    private View.OnClickListener onFlowersSelectedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //no-op
        }
    };
    private View.OnClickListener onCanvasSelectedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //no-op
        }
    };
    private View.OnClickListener onMusicSelectedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //no-op
        }
    };
    private View.OnClickListener onShareSelectedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //no-op
        }
    };
    private View.OnClickListener onSaveSelectedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //no-op
        }
    };
    private View.OnClickListener onRestartSelectedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            zenSketch.clear();
        }
    };

    public void startSketch() {
        zenSketch.start();
    }

    public void stopSketch() {
        zenSketch.stop();
    }

    public void pauseSketch() {
        zenSketch.pause();
    }

    public void destroySketch() {
        zenSketch.destroy();
    }

    @Override
    protected void onDetachedFromWindow() {
        detachListeners();
        sequentialButtonAnimator.clear();
        super.onDetachedFromWindow();
    }

    private void detachListeners() {
        brushButton.setOnClickListener(null);
        eraseButton.setOnClickListener(null);
        flowersButton.setOnClickListener(null);
        canvasButton.setOnClickListener(null);
        musicButton.setOnClickListener(null);
        shareButton.setOnClickListener(null);
        saveButton.setOnClickListener(null);
        restartButton.setOnClickListener(null);
        zenSketch.setOnPaintingListener(null);
    }

    @Override
    public void onPaintingStart() {
        hideControlsWithDelay();
    }

    @Override
    public void onPaintingEnd() {
        showControlsWithDelay();
    }

    private void hideControlsWithDelay() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                hideControls();
            }
        }, MILLISECONDS_TO_HIDE);
    }

    private void showControlsWithDelay() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                showControls();
            }
        }, MILLISECONDS_TO_SHOW);
    }

    private void hideControls() {
        sequentialButtonAnimator.hide();
        restartButton.hide();
    }

    private void showControls() {
        sequentialButtonAnimator.show();
        restartButton.show();
    }
}
