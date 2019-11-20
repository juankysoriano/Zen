package zenproject.meditation.android.ui.sketch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.juankysoriano.rainbow.utils.RainbowMath;
import com.oguzdev.circularfloatingactionmenu.library.CircularMenu;

import zenproject.meditation.android.R;
import zenproject.meditation.android.ui.menu.buttons.FloatingActionButton;
import zenproject.meditation.android.ui.menu.buttons.MenuButton;
import zenproject.meditation.android.ui.menu.buttons.creators.CircularMenuCreator;
import zenproject.meditation.android.ui.sketch.clear.ClearView;
import zenproject.meditation.android.ui.sketch.clear.SketchClearListener;

@SuppressWarnings({"PMD.FieldDeclarationsShouldBeAtStartOfClass", "PMD.TooManyMethods"})
public class ZenSketchView extends RelativeLayout {
    private ClearView clearView;
    private SketchClearListener sketchClearListener;
    private CircularMenu circularMenu;

    public ZenSketchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZenSketchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        clearView = findViewById(R.id.reveal_view);
    }

    public CircularMenu getCircularMenu() {
        if (circularMenu == null) {
            circularMenu = CircularMenuCreator.create();
        }
        return circularMenu;
    }

    public void setSketchClearListener(SketchClearListener onClearListener) {
        this.sketchClearListener = onClearListener;
    }

    private boolean hasSketchClearListener() {
        return sketchClearListener != null;
    }

    private final Animator.AnimatorListener revealAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            if (hasSketchClearListener()) {
                sketchClearListener.onSketchCleared();
            }
        }
    };

    public void clear() {
        if (!clearView.isClearing()) {
            clearView.startClearWith(revealAnimatorListener);
            FloatingActionButton menu = (FloatingActionButton) (circularMenu.findSubActionViewWithId(MenuButton.RESTART.getId()));
            clearView.setClearOrigin(menu.getCentre());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        clearView.setClearRadius(RainbowMath.dist(0, 0, getWidth(), getHeight()));
    }
}
