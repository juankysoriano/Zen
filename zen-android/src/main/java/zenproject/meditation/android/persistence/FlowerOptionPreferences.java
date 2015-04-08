package zenproject.meditation.android.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.sketch.painting.flowers.Flower;

public class FlowerOptionPreferences {
    private static final String PREF_NAME = "FlowerOptionPreferences";
    private static final String PREF_FLOWER = PREF_NAME + "Flower";

    private final SharedPreferences sharedPreferences;

    public static FlowerOptionPreferences newInstance() {
        return new FlowerOptionPreferences(ContextRetriever.INSTANCE.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE));
    }

    FlowerOptionPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void applyFlower(Flower flower) {
        sharedPreferences.edit().putInt(PREF_FLOWER, flower.ordinal()).apply();
    }

    public Flower getFlower() {
        return Flower.from(sharedPreferences.getInt(PREF_FLOWER, Flower.POPPY.ordinal()));
    }
}
