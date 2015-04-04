package zenproject.meditation.android.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.juankysoriano.rainbow.utils.RainbowMath;
import com.oguzdev.circularfloatingactionmenu.library.CircularMenu;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.sketch.ZenSketch;
import zenproject.meditation.android.views.menu.FloatingActionButton;
import zenproject.meditation.android.views.menu.creators.FloatingActionCircularMenuCreator;
import zenproject.meditation.android.views.reveal.RevealView;

@SuppressWarnings("PMD.FieldDeclarationsShouldBeAtStartOfClass")
public class ZenSketchView extends RelativeLayout implements ZenSketch.OnPaintingListener {
    private static final int MILLISECONDS_TO_HIDE = 150;
    private static final int MILLISECONDS_TO_SHOW = 150;
    private RevealView revealView;
    private OnClearListener onClearListener;
    private CircularMenu circularMenu;
    private FloatingActionButton menuButton;
    private boolean isPainting;

    public ZenSketchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZenSketchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        revealView = (RevealView) findViewById(R.id.reveal_view);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        circularMenu = FloatingActionCircularMenuCreator.createWith(ContextRetriever.INSTANCE.getCurrentContext());
        circularMenu.setStateChangeListener(menuStateChangeListener);
        menuButton = (FloatingActionButton) circularMenu.getActionView();
    }

    public CircularMenu getCircularMenu() {
        return circularMenu;
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
        if (circularMenu.isOpen()) {
            circularMenu.close(true);
            menuButton.rotate();
        } else {
            menuButton.hide();
        }
    }

    private void showControls() {
        menuButton.show();
    }

    private final CircularMenu.MenuStateChangeListener menuStateChangeListener = new CircularMenu.MenuStateChangeListener() {
        @Override
        public void onMenuOpened(CircularMenu circularMenu) {
            if (isPainting) {
                hideControlsWithDelay();
            }
        }

        @Override
        public void onMenuClosed(CircularMenu circularMenu) {
            if (isPainting) {
                menuButton.hide();
            }
        }
    };

    public void setOnClearListener(OnClearListener onClearListener) {
        this.onClearListener = onClearListener;
    }

    private boolean hasOnRevealListener() {
        return onClearListener != null;
    }

    private final Animator.AnimatorListener revealAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            if (hasOnRevealListener()) {
                onClearListener.onRevealed();
            }
        }
    };

    public void startRestartAnimation(FloatingActionButton view) {
        if (!revealView.isRevealing()) {
            revealView.startRevealWith(revealAnimatorListener);
            revealView.setRevealOrigin(view.getCentre());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        revealView.setRevealRadius(RainbowMath.dist(0, 0, getWidth(), getHeight()));
    }

    @Override
    public void onPaintingStart() {
        isPainting = true;
        hideControlsWithDelay();
    }

    @Override
    public void onPaintingEnd() {
        isPainting = false;
        showControlsWithDelay();
    }

    public interface OnClearListener {
        void onRevealed();
    }
}
