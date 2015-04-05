package zenproject.meditation.android.ui.menu.dialogs.flower;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.novoda.notils.caster.Views;

import de.hdodenhof.circleimageview.CircleImageView;
import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.sketch.painting.flowers.Flower;
import zenproject.meditation.android.persistence.FlowerOptionPreferences;

import static zenproject.meditation.android.sketch.painting.ink.BrushColor.ACCENT;

/**
 * TODO Consider using an adapter when the number of flowers increases
 */
public class FlowerViewList extends LinearLayout {
    private static final int UNSELECTED_BORDER_SIZE = ContextRetriever.INSTANCE.getResources().getDimensionPixelSize(R.dimen.divider_weight);
    private static final int SELECTED_BORDER_SIZE = ContextRetriever.INSTANCE.getResources().getDimensionPixelSize(R.dimen.color_selected_weight);
    private static final int DIVIDER = ContextRetriever.INSTANCE.getResources().getColor(R.color.divider);

    private CircleImageView noneFlower;
    private CircleImageView cherryFlower;
    private CircleImageView meconopsisFlower;
    private CircleImageView poppyFlower;
    private CircleImageView inkFlower;

    private FlowerSelectedListener flowerSelectedListener;

    public FlowerViewList(Context context) {
        super(context);
    }

    public FlowerViewList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowerViewList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        noneFlower = Views.findById(this, R.id.no_flower);
        cherryFlower = Views.findById(this, R.id.cherry);
        meconopsisFlower = Views.findById(this, R.id.meconopsis);
        poppyFlower = Views.findById(this, R.id.poppy);
        inkFlower = Views.findById(this, R.id.ink_flower);

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
        inkFlower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected((CircleImageView) v);
                notifyFlowerSelected(Flower.INK_FLOWER);
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
        } else if (Flower.INK_FLOWER == flower) {
            setSelected(inkFlower);
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
        inkFlower.setBorderColor(DIVIDER);

        noneFlower.setBorderWidth(UNSELECTED_BORDER_SIZE);
        cherryFlower.setBorderWidth(UNSELECTED_BORDER_SIZE);
        meconopsisFlower.setBorderWidth(UNSELECTED_BORDER_SIZE);
        poppyFlower.setBorderWidth(UNSELECTED_BORDER_SIZE);
        inkFlower.setBorderWidth(UNSELECTED_BORDER_SIZE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        noneFlower.setOnClickListener(null);
        cherryFlower.setOnClickListener(null);
        meconopsisFlower.setOnClickListener(null);
        poppyFlower.setOnClickListener(null);
        inkFlower.setOnClickListener(null);
    }

    private void notifyFlowerSelected(Flower flower) {
        if (hasFlowerSelectedListener()) {
            flowerSelectedListener.onFlowerSelected(flower);
        }
    }

}
