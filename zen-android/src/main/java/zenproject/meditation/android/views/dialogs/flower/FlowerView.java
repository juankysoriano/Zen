package zenproject.meditation.android.views.dialogs.flower;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.novoda.notils.caster.Views;

import de.hdodenhof.circleimageview.CircleImageView;
import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.preferences.FlowerOptionPreferences;

import static zenproject.meditation.android.preferences.BrushColor.ACCENT;
import static zenproject.meditation.android.sketch.performers.flowers.FlowerDrawer.Flower;

public class FlowerView extends LinearLayout {
    private static final int UNSELECTED_BORDER_SIZE = ContextRetriever.INSTANCE.getCurrentResources().getDimensionPixelSize(R.dimen.divider_weight);
    private static final int SELECTED_BORDER_SIZE = ContextRetriever.INSTANCE.getCurrentResources().getDimensionPixelSize(R.dimen.color_selected_weight);
    private static final int DIVIDER = ContextRetriever.INSTANCE.getCurrentResources().getColor(R.color.divider);

    private CircleImageView noneFlower;
    private CircleImageView cherryFlower;
    private CircleImageView meconopsisFlower;
    private CircleImageView poppyFlower;
    private CircleImageView autumnMixFlower;

    private FlowerSelectedListener flowerSelectedListener;

    public FlowerView(Context context) {
        super(context);
    }

    public FlowerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        noneFlower = Views.findById(this, R.id.no_flower);
        cherryFlower = Views.findById(this, R.id.cherry);
        meconopsisFlower = Views.findById(this, R.id.meconopsis);
        poppyFlower = Views.findById(this, R.id.poppy);
        autumnMixFlower = Views.findById(this, R.id.autumn_mix);

        setSelectedFrom(FlowerOptionPreferences.newInstance().getFlower());
    }

    private boolean hasFlowerSelectedListener() {
        return flowerSelectedListener != null;
    }

    public void setFlowerSelectedListener(FlowerSelectedListener flowerSelectedListener) {
        this.flowerSelectedListener = flowerSelectedListener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        noneFlower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected((CircleImageView) v);
                notifyFlowerSelected(Flower.NONE);
            }
        });
        cherryFlower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected((CircleImageView) v);
                notifyFlowerSelected(Flower.CHERRY);
            }
        });
        meconopsisFlower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected((CircleImageView) v);
                notifyFlowerSelected(Flower.MECONOPSIS);
            }
        });
        poppyFlower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected((CircleImageView) v);
                notifyFlowerSelected(Flower.POPPY);
            }
        });
        autumnMixFlower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected((CircleImageView) v);
                notifyFlowerSelected(Flower.AUTUMN_MIX);
            }
        });
    }

    private void setSelectedFrom(Flower flower) {
        if (Flower.NONE == flower) {
            setSelected(noneFlower);
        } else if (Flower.CHERRY == flower) {
            setSelected(cherryFlower);
        } else if (Flower.MECONOPSIS == flower) {
            setSelected(meconopsisFlower);
        } else if (Flower.POPPY == flower) {
            setSelected(poppyFlower);
        } else if (Flower.AUTUMN_MIX == flower) {
            setSelected(autumnMixFlower);
        }
    }

    private void setSelected(CircleImageView circleImageView) {
        unselectAll();
        circleImageView.setBorderColor(ACCENT.toAndroidColor());
        circleImageView.setBorderWidth(SELECTED_BORDER_SIZE);
    }

    private void unselectAll() {
        noneFlower.setBorderColor(DIVIDER);
        cherryFlower.setBorderColor(DIVIDER);
        meconopsisFlower.setBorderColor(DIVIDER);
        poppyFlower.setBorderColor(DIVIDER);
        autumnMixFlower.setBorderColor(DIVIDER);

        noneFlower.setBorderWidth(UNSELECTED_BORDER_SIZE);
        cherryFlower.setBorderWidth(UNSELECTED_BORDER_SIZE);
        meconopsisFlower.setBorderWidth(UNSELECTED_BORDER_SIZE);
        poppyFlower.setBorderWidth(UNSELECTED_BORDER_SIZE);
        autumnMixFlower.setBorderWidth(UNSELECTED_BORDER_SIZE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        noneFlower.setOnClickListener(null);
        cherryFlower.setOnClickListener(null);
        meconopsisFlower.setOnClickListener(null);
        poppyFlower.setOnClickListener(null);
        autumnMixFlower.setOnClickListener(null);
    }

    private void notifyFlowerSelected(Flower flower) {
        if (hasFlowerSelectedListener()) {
            flowerSelectedListener.onFlowerSelected(flower);
        }
    }

}
