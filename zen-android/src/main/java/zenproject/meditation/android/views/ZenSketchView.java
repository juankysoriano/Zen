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
import zenproject.meditation.android.views.creators.FloatingActionButtonMenuCreator;

public class ZenSketchView extends RelativeLayout {
    private static final int MILLISECONDS_TO_HIDE = 150;
    private static final int MILLISECONDS_TO_SHOW = 150;
    private RevealView revealView;
    private OnRevealListener onRevealListener;
    private CircularMenu circularMenu;
    private FloatingActionButton menuButton;

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
        circularMenu = FloatingActionButtonMenuCreator.createWith(ContextRetriever.INSTANCE.getCurrentContext());
        menuButton = (FloatingActionButton) circularMenu.getActionView();
    }

    public CircularMenu getMenu() {
        return circularMenu;
    }

    @Override
    protected void onDetachedFromWindow() {
        circularMenu = null;
        super.onDetachedFromWindow();
    }

    public void hideControlsWithDelay() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                hideControls();
            }
        }, MILLISECONDS_TO_HIDE);
    }

    public void showControlsWithDelay() {
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
            circularMenu.setStateChangeListener(menuStateChangeListener);
        } else {
            menuButton.hide();
        }
    }

    private void showControls() {
        menuButton.show();
    }

    private CircularMenu.MenuStateChangeListener menuStateChangeListener = new CircularMenu.MenuStateChangeListener() {
        @Override
        public void onMenuOpened(CircularMenu circularMenu) {
            //no-op
        }

        @Override
        public void onMenuClosed(CircularMenu circularMenu) {
            circularMenu.setStateChangeListener(null);
            menuButton.hide();
        }
    };

    public void setOnRevealListener(OnRevealListener onRevealListener) {
        this.onRevealListener = onRevealListener;
    }

    private boolean hasOnRevealListener() {
        return onRevealListener != null;
    }

    @SuppressWarnings("PMD.FieldDeclarationsShouldBeAtStartOfClass")
    private final Animator.AnimatorListener revealAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            if (hasOnRevealListener()) {
                onRevealListener.onRevealed();
            }
        }
    };

    public void startRestartAnimation() {
        if (!revealView.isRevealing()) {
            revealView.startRevealWith(revealAnimatorListener);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        revealView.setRevealRadius(RainbowMath.dist(0, 0, getWidth(), getHeight()));
        //  revealView.setRevealOrigin(restartButton.getCentre());
    }

    public interface OnRevealListener {
        void onRevealed();
    }
}
