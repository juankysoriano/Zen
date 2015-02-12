package zenproject.meditation.android.views;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequentialButtonAnimator {
    private List<FloatingActionButton> floatingActionButtonList;
    private static final int MILLISECONDS_TO_HIDE = 150;
    private static final int MILLISECONDS_TO_SHOW = 150;

    public static SequentialButtonAnimator newInstance() {
        return new SequentialButtonAnimator(new ArrayList<FloatingActionButton>());
    }

    protected SequentialButtonAnimator(List<FloatingActionButton> floatingActionButtonList) {
        this.floatingActionButtonList = floatingActionButtonList;

    }

    public void hide() {
        for (int i = 0; i < size(); i++) {
            FloatingActionButton floatingActionButton = floatingActionButtonList.get(i);
            hideDelayed(floatingActionButton, MILLISECONDS_TO_HIDE * i / size());
        }
    }

    public void show() {
        for (int i = size() - 1; i >= 0; i--) {
            FloatingActionButton floatingActionButton = floatingActionButtonList.get(i);
            showDelayed(floatingActionButton, MILLISECONDS_TO_SHOW * i / size());
        }
    }

    public void add(FloatingActionButton... floatingActionButtons) {
        floatingActionButtonList.addAll(Arrays.asList(floatingActionButtons));
    }

    public void clear() {
        floatingActionButtonList.clear();
    }

    private int size() {
        return floatingActionButtonList.size();
    }

    private void hideDelayed(final FloatingActionButton floatingActionButton, long millis) {
        floatingActionButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                floatingActionButton.hide();
            }
        }, millis);
    }

    private void showDelayed(final FloatingActionButton floatingActionButton, long millis) {
        floatingActionButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                floatingActionButton.show();
            }
        }, millis);
    }
}
