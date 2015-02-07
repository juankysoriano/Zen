package com.juankysoriano.rainbow.core.drawing;

import android.graphics.SurfaceTexture;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.ViewGroup;

import com.juankysoriano.rainbow.core.Rainbow;
import com.juankysoriano.rainbow.core.event.RainbowEvent;
import com.juankysoriano.rainbow.core.event.RainbowInputController;

public class RainbowTextureView extends TextureView implements SurfaceTextureListener {
    Rainbow mRainbow;

    public RainbowTextureView(ViewGroup parent, Rainbow rainbow) {
        super(parent.getContext().getApplicationContext());
        setBackground(parent.getBackground());
        setMeasuredDimension(parent.getMeasuredWidth(), parent.getMeasuredHeight());
        mRainbow = rainbow;

        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        setSurfaceTextureListener(this);
        parent.addView(this);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        RainbowInputController rainbowInputController = mRainbow.getRainbowInputController();

        if (rainbowInputController != null) {
            float px = rainbowInputController.getPreviousX();
            float py = rainbowInputController.getPreviousY();
            rainbowInputController.postEvent(RainbowEvent.from(event, px, py), mRainbow.getRainbowDrawer(), mRainbow.isRunning());
        }

        return true;
    }

    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, final int width, final int height) {
        onSurfaceTextureSizeChanged(surface, width, height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(final SurfaceTexture surface, final int width, final int height) {
        mRainbow.invalidate();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(final SurfaceTexture surface) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }
}
