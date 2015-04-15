package zenproject.meditation.android.ui.sketch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.juankysoriano.rainbow.utils.RainbowMath;
import com.oguzdev.circularfloatingactionmenu.library.CircularMenu;

import butterknife.ButterKnife;
import butterknife.InjectView;
import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.sketch.ZenSketch;
import zenproject.meditation.android.ui.menu.buttons.FloatingActionButton;
import zenproject.meditation.android.ui.menu.buttons.MenuButton;
import zenproject.meditation.android.ui.menu.buttons.creators.CircularMenuCreator;
import zenproject.meditation.android.ui.sketch.clear.ClearView;

@SuppressWarnings({"PMD.FieldDeclarationsShouldBeAtStartOfClass", "PMD.TooManyMethods"})
public class ZenSketchView extends RelativeLayout implements ZenSketch.OnPaintingListener {
    private static final int MILLISECONDS_TO_HIDE = 150;
    private static final int MILLISECONDS_TO_SHOW = 150;

    @InjectView(R.id.reveal_view) ClearView clearView;
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
        ButterKnife.inject(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        circularMenu = CircularMenuCreator.createWith(ContextRetriever.INSTANCE.getApplicationContext());
        circularMenu.setStateChangeListener(menuStateChangeListener);
        menuButton = (FloatingActionButton) circularMenu.getActionView();
    }

    public CircularMenu getCircularMenu() {
        return circularMenu;
    }

    public FloatingActionButton getButtonViewFor(MenuButton menuButton) {
        switch (menuButton) {
            case MENU:
                return (FloatingActionButton) circularMenu.getActionView();
            default:
                return (FloatingActionButton) circularMenu.findSubActionViewWithId(menuButton.getId());
        }
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

    private boolean hasOnClearListener() {
        return onClearListener != null;
    }

    private final Animator.AnimatorListener revealAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            if (hasOnClearListener()) {
                onClearListener.onCleared();
            }
        }
    };

    public void startClearAnimation(FloatingActionButton view) {
        if (!clearView.isClearing()) {
            clearView.startClearWith(revealAnimatorListener);
            clearView.setClearOrigin(view.getCentre());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        clearView.setClearRadius(RainbowMath.dist(0, 0, getWidth(), getHeight()));
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
        void onCleared();
    }
}
