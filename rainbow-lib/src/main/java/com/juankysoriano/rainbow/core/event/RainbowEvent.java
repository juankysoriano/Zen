package com.juankysoriano.rainbow.core.event;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.MotionEvent;

public class RainbowEvent implements Parcelable {
    private float x, px;
    private float y, py;
    private int action;
    private MotionEvent motionEvent;

    public RainbowEvent(Parcel in) {
        float[] locations = new float[4];
        in.readFloatArray(locations);

        this.action = in.readInt();
        this.motionEvent = in.readParcelable(MotionEvent.class.getClassLoader());
        this.x = locations[0];
        this.px = locations[1];
        this.y = locations[2];
        this.py = locations[3];
    }

    RainbowEvent(MotionEvent motionEvent, float px, float py) {
        this.x = motionEvent.getX();
        this.y = motionEvent.getY();
        this.px = px;
        this.py = py;
        this.action = motionEvent.getAction();
        this.motionEvent = motionEvent;
    }

    public static RainbowEvent from(MotionEvent legacyEvent, float px, float py) {
        return new RainbowEvent(legacyEvent, px, py);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(motionEvent, flags);
        dest.writeFloatArray(new float[]{this.x, this.px, this.y, this.py});
        dest.writeInt(action);
    }

    public float getX() {
        return x;
    }

    public float getPreviousX() {
        if (px == -1) {
            return x;
        } else {
            return px;
        }
    }

    public float getY() {
        return y;
    }

    public float getPreviousY() {
        if (py == -1) {
            return y;
        } else {
            return py;
        }
    }

    public int getAction() {
        return action;
    }

    public MotionEvent toMotionEvent() {
        return motionEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RainbowEvent that = (RainbowEvent) o;

        return action == that.action && Float.compare(that.px, px) == 0
                && Float.compare(that.py, py) == 0
                && Float.compare(that.x, x) == 0
                && Float.compare(that.y, y) == 0
                && motionEvent.equals(that.motionEvent);

    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (px != +0.0f ? Float.floatToIntBits(px) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (py != +0.0f ? Float.floatToIntBits(py) : 0);
        result = 31 * result + action;
        result = 31 * result + motionEvent.hashCode();
        return result;
    }
}
