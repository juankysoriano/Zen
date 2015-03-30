package zenproject.meditation.android.preferences;

public enum Flower {
    NONE,
    CHERRY,
    MECONOPSIS,
    POPPY,
    VIOLET;

    public static Flower from(int value) {
        for (Flower flower : values()) {
            if (flower.ordinal() == value) {
                return flower;
            }
        }
        return NONE;
    }
}
