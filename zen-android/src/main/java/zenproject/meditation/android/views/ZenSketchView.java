package zenproject.meditation.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowEvent;
import com.melnykov.fab.FloatingActionButton;

import zenproject.meditation.android.R;
import zenproject.meditation.android.drawers.ZenSketch;

public class ZenSketchView extends RelativeLayout {

    private static final int MILLISECONDS_TO_HIDE = 250;
    private static final int MILLISECONDS_TO_SHOW = 250;
    private ZenSketch zenSketch;
    private FloatingActionButton brushButton;
    private FloatingActionButton eraseButton;
    private FloatingActionButton flowersButton;
    private FloatingActionButton canvasButton;
    private FloatingActionButton musicButton;
    private FloatingActionButton shareButton;
    private FloatingActionButton saveButton;

    public ZenSketchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZenSketchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        zenSketch = ZenSketch.newInstance(this);
        brushButton = (FloatingActionButton) findViewById(R.id.brush_button);
        eraseButton = (FloatingActionButton) findViewById(R.id.erase_button);
        flowersButton = (FloatingActionButton) findViewById(R.id.flowers_button);
        canvasButton = (FloatingActionButton) findViewById(R.id.canvas_button);
        musicButton = (FloatingActionButton) findViewById(R.id.music_button);
        shareButton = (FloatingActionButton) findViewById(R.id.share_button);
        saveButton = (FloatingActionButton) findViewById(R.id.save_button);
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

    private ZenSketch.SketchTouchesListener sketchTouchesListener = new ZenSketch.SketchTouchesListener() {
        @Override
        public void onSketchTouched(RainbowEvent event, RainbowDrawer rainbowDrawer) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideControls();
                }
            }, MILLISECONDS_TO_HIDE);
        }

        @Override
        public void onSketchReleased(RainbowEvent event, RainbowDrawer rainbowDrawer) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    showControls();
                }
            }, MILLISECONDS_TO_SHOW);
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attachListeners();
    }

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
        super.onDetachedFromWindow();
    }

    private void attachListeners() {
        brushButton.setOnClickListener(onBrushSelectedListener);
        eraseButton.setOnClickListener(onEraseSelectedListener);
        flowersButton.setOnClickListener(onFlowersSelectedListener);
        canvasButton.setOnClickListener(onCanvasSelectedListener);
        musicButton.setOnClickListener(onMusicSelectedListener);
        shareButton.setOnClickListener(onShareSelectedListener);
        saveButton.setOnClickListener(onSaveSelectedListener);
        zenSketch.setSketchTouchesListener(sketchTouchesListener);
    }

    private void showControls() {
        brushButton.show();
        eraseButton.show();
        flowersButton.show();
        canvasButton.show();
        musicButton.show();
        shareButton.show();
        saveButton.show();
    }

    private void hideControls() {
        brushButton.hide();
        eraseButton.hide();
        flowersButton.hide();
        canvasButton.hide();
        musicButton.hide();
        shareButton.hide();
        saveButton.hide();
    }

    private void detachListeners() {
        brushButton.setOnClickListener(null);
        eraseButton.setOnClickListener(null);
        flowersButton.setOnClickListener(null);
        canvasButton.setOnClickListener(null);
        musicButton.setOnClickListener(null);
        shareButton.setOnClickListener(null);
        saveButton.setOnClickListener(null);
    }
}
