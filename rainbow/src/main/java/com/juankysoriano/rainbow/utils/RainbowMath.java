package com.juankysoriano.rainbow.utils;

import com.juankysoriano.rainbow.core.graphics.RainbowGraphics;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by juankysoriano on 08/04/2014.
 */
public abstract class RainbowMath {
    public static final float PI = (float) Math.PI;
    public static final float RAD_TO_DEG = 180.0f / PI;
    public static final float DEG_TO_RAD = PI / 180.0f;
    public static final float TAU = PI * 2.0f;
    public static final float TWO_PI = PI * 2.0f;
    public static final float QUARTER_PI = PI / 4.0f;
    public static final float HALF_PI = PI / 2.0f;
    public static final float THIRD_PI = PI / 3.0f;
    public static final float EPSILON = 0.0001f;
    static final String ERROR_MIN_MAX = "Cannot use min() or max() on an empty array.";
    static final int PERLIN_YWRAPB = 4;
    static final int PERLIN_YWRAP = 1 << RainbowMath.PERLIN_YWRAPB;
    static final int PERLIN_ZWRAPB = 8;
    static final int PERLIN_ZWRAP = 1 << RainbowMath.PERLIN_ZWRAPB;
    static final int PERLIN_SIZE = 4095;
    protected static HashMap<String, Pattern> matchPatterns;
    static Random internalRandom;
    static int perlin_octaves = 4; // default to medium smooth
    static float perlin_amp_falloff = 0.5f; // 50% reduction/octave
    // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    // [toxi 031112]
    // new vars needed due to recent change of cos table in PGraphics
    static int perlin_TWOPI;
    static int perlin_PI;
    static float[] perlin_cosTable;
    static float[] perlin;
    // ////////////////////////////////////////////////////////////
    static Random perlinRandom;
    /**
     * Integer number formatter.
     */
    private static NumberFormat int_nf;
    private static int int_nf_digits;
    private static boolean int_nf_commas;
    private static NumberFormat float_nf;
    private static int float_nf_left, float_nf_right;
    private static boolean float_nf_commas;

    public static final float abs(final float n) {
        return (n < 0) ? -n : n;
    }

    public static final int abs(final int n) {
        return (n < 0) ? -n : n;
    }

    public static final float log(final float a) {
        return (float) Math.log(a);
    }

    public static final float exp(final float a) {
        return (float) Math.exp(a);
    }

    public static final float pow(final float a, final float b) {
        return (float) Math.pow(a, b);
    }

    public static final int max(final int a, final int b) {
        return (a > b) ? a : b;
    }

    public static final float max(final float a, final float b) {
        return (a > b) ? a : b;
    }

    public static final int max(final int a, final int b, final int c) {
        return (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
    }

    public static final float max(final float a, final float b, final float c) {
        return (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
    }

    /**
     * Find the maximum value in an array. Throws an
     * ArrayIndexOutOfBoundsException if the array is length 0.
     *
     * @param list the source array
     * @return The maximum value
     */
    public static final int max(final int[] list) {
        if (list.length == 0) {
            throw new ArrayIndexOutOfBoundsException(RainbowMath.ERROR_MIN_MAX);
        }
        int max = list[0];
        for (int i = 1; i < list.length; i++) {
            if (list[i] > max) {
                max = list[i];
            }
        }
        return max;
    }

    /**
     * Find the maximum value in an array. Throws an
     * ArrayIndexOutOfBoundsException if the array is length 0.
     *
     * @param list the source array
     * @return The maximum value
     */
    public static final float max(final float[] list) {
        if (list.length == 0) {
            throw new ArrayIndexOutOfBoundsException(RainbowMath.ERROR_MIN_MAX);
        }
        float max = list[0];
        for (int i = 1; i < list.length; i++) {
            if (list[i] > max) {
                max = list[i];
            }
        }
        return max;
    }

    public static final int min(final int a, final int b) {
        return (a < b) ? a : b;
    }

    public static final float min(final float a, final float b) {
        return (a < b) ? a : b;
    }

    public static final int min(final int a, final int b, final int c) {
        return (a < b) ? ((a < c) ? a : c) : ((b < c) ? b : c);
    }

    public static final float min(final float a, final float b, final float c) {
        return (a < b) ? ((a < c) ? a : c) : ((b < c) ? b : c);
    }

    /**
     * Find the minimum value in an array. Throws an
     * ArrayIndexOutOfBoundsException if the array is length 0.
     *
     * @param list the source array
     * @return The minimum value
     */
    public static final int min(final int[] list) {
        if (list.length == 0) {
            throw new ArrayIndexOutOfBoundsException(RainbowMath.ERROR_MIN_MAX);
        }
        int min = list[0];
        for (int i = 1; i < list.length; i++) {
            if (list[i] < min) {
                min = list[i];
            }
        }
        return min;
    }

    /**
     * Find the minimum value in an array. Throws an
     * ArrayIndexOutOfBoundsException if the array is length 0.
     *
     * @param list the source array
     * @return The minimum value
     */
    public static final float min(final float[] list) {
        if (list.length == 0) {
            throw new ArrayIndexOutOfBoundsException(RainbowMath.ERROR_MIN_MAX);
        }
        float min = list[0];
        for (int i = 1; i < list.length; i++) {
            if (list[i] < min) {
                min = list[i];
            }
        }
        return min;
    }

    public static final int constrain(final int amt, final int low, final int high) {
        return (amt < low) ? low : ((amt > high) ? high : amt);
    }

    // ////////////////////////////////////////////////////////////

    public static final float constrain(final float amt, final float low, final float high) {
        return (amt < low) ? low : ((amt > high) ? high : amt);
    }

    public static final float sin(final float angle) {
        return (float) Math.sin(angle);
    }

    // ////////////////////////////////////////////////////////////
    // getting the time

    public static final float cos(final float angle) {
        return (float) Math.cos(angle);
    }

    public static final float tan(final float angle) {
        return (float) Math.tan(angle);
    }

    public static final float asin(final float value) {
        return (float) Math.asin(value);
    }

    public static final float acos(final float value) {
        return (float) Math.acos(value);
    }

    public static final float atan(final float value) {
        return (float) Math.atan(value);
    }

    public static final float atan2(final float a, final float b) {
        return (float) Math.atan2(a, b);
    }

    public static final float degrees(final float radians) {
        return radians * RAD_TO_DEG;
    }

    public static final float radians(final float degrees) {
        return degrees * DEG_TO_RAD;
    }

    // ////////////////////////////////////////////////////////////

    // controlling time (playing god)

    public static final int ceil(final float what) {
        return (int) Math.ceil(what);
    }

    public static final int floor(final float what) {
        return (int) Math.floor(what);
    }

    // ////////////////////////////////////////////////////////////

    public static final int round(final float what) {
        return Math.round(what);
    }

    public static final float mag(final float a, final float b) {
        return (float) Math.sqrt((a * a) + (b * b));
    }

    public static final float mag(final float a, final float b, final float c) {
        return (float) Math.sqrt((a * a) + (b * b) + (c * c));
    }

    public static final float dist(final float x1, final float y1, final float x2, final float y2) {
        return RainbowMath.sqrt(RainbowMath.sq(x2 - x1) + RainbowMath.sq(y2 - y1));
    }

    public static final float sqrt(final float a) {
        return (float) Math.sqrt(a);
    }

    // ////////////////////////////////////////////////////////////

    public static final float sq(final float a) {
        return a * a;
    }

    public static final float dist(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {
        return RainbowMath.sqrt(RainbowMath.sq(x2 - x1) + RainbowMath.sq(y2 - y1) + RainbowMath.sq(z2 - z1));
    }

    public static final float lerp(final float start, final float stop, final float amt) {
        return start + ((stop - start) * amt);
    }

    /**
     * Normalize a value to exist between 0 and 1 (inclusive). Mathematically
     * the opposite of lerp(), figures out what proportion a particular value is
     * relative to start and stop coordinates.
     */
    public static final float norm(final float value, final float start, final float stop) {
        return (value - start) / (stop - start);
    }

    /**
     * Convenience function to map a variable from one coordinate space to
     * another. Equivalent to unlerp() followed by lerp().
     */
    public static final float map(final float value, final float istart, final float istop, final float ostart, final float ostop) {
        return ostart + ((ostop - ostart) * ((value - istart) / (istop - istart)));
    }

    // ////////////////////////////////////////////////////////////

    public static byte[] sort(final byte what[]) {
        return RainbowMath.sort(what, what.length);
    }

    public static byte[] sort(final byte[] what, final int count) {
        final byte[] outgoing = new byte[what.length];
        System.arraycopy(what, 0, outgoing, 0, what.length);
        Arrays.sort(outgoing, 0, count);
        return outgoing;
    }

    public static char[] sort(final char what[]) {
        return RainbowMath.sort(what, what.length);
    }

    public static char[] sort(final char[] what, final int count) {
        final char[] outgoing = new char[what.length];
        System.arraycopy(what, 0, outgoing, 0, what.length);
        Arrays.sort(outgoing, 0, count);
        return outgoing;
    }

    public static int[] sort(final int what[]) {
        return RainbowMath.sort(what, what.length);
    }

    public static int[] sort(final int[] what, final int count) {
        final int[] outgoing = new int[what.length];
        System.arraycopy(what, 0, outgoing, 0, what.length);
        Arrays.sort(outgoing, 0, count);
        return outgoing;
    }

    public static float[] sort(final float what[]) {
        return RainbowMath.sort(what, what.length);
    }

    public static float[] sort(final float[] what, final int count) {
        final float[] outgoing = new float[what.length];
        System.arraycopy(what, 0, outgoing, 0, what.length);
        Arrays.sort(outgoing, 0, count);
        return outgoing;
    }

    public static String[] sort(final String what[]) {
        return RainbowMath.sort(what, what.length);
    }

    public static String[] sort(final String[] what, final int count) {
        final String[] outgoing = new String[what.length];
        System.arraycopy(what, 0, outgoing, 0, what.length);
        Arrays.sort(outgoing, 0, count);
        return outgoing;
    }

    public static boolean[] expand(final boolean list[]) {
        return RainbowMath.expand(list, list.length << 1);
    }

    public static boolean[] expand(final boolean list[], final int newSize) {
        final boolean temp[] = new boolean[newSize];
        System.arraycopy(list, 0, temp, 0, Math.min(newSize, list.length));
        return temp;
    }

    public static byte[] expand(final byte list[]) {
        return RainbowMath.expand(list, list.length << 1);
    }

    public static byte[] expand(final byte list[], final int newSize) {
        final byte temp[] = new byte[newSize];
        System.arraycopy(list, 0, temp, 0, Math.min(newSize, list.length));
        return temp;
    }

    public static char[] expand(final char list[]) {
        return RainbowMath.expand(list, list.length << 1);
    }

    //

	/*
     * // not very useful, because it only works for public (and protected?) //
	 * fields of a class, not local variables to methods public void
	 * printvar(String name) { try { Field field =
	 * getClass().getDeclaredField(name); println(name + " = " +
	 * field.get(this)); } catch (Exception e) { e.printStackTrace(); } }
	 */

    // ////////////////////////////////////////////////////////////

    // MATH

    // lots of convenience methods for math with floats.
    // doubles are overkill for processing applets, and casting
    // things all the time is annoying, thus the functions below.

    public static char[] expand(final char list[], final int newSize) {
        final char temp[] = new char[newSize];
        System.arraycopy(list, 0, temp, 0, Math.min(newSize, list.length));
        return temp;
    }

    public static int[] expand(final int list[]) {
        return RainbowMath.expand(list, list.length << 1);
    }

    public static int[] expand(final int list[], final int newSize) {
        final int temp[] = new int[newSize];
        System.arraycopy(list, 0, temp, 0, Math.min(newSize, list.length));
        return temp;
    }

    public static RainbowImage[] expand(final RainbowImage list[]) {
        return RainbowMath.expand(list, list.length << 1);
    }

    public static RainbowImage[] expand(final RainbowImage list[], final int newSize) {
        final RainbowImage temp[] = new RainbowImage[newSize];
        System.arraycopy(list, 0, temp, 0, Math.min(newSize, list.length));
        return temp;
    }

    public static float[] expand(final float list[]) {
        return RainbowMath.expand(list, list.length << 1);
    }

    public static float[] expand(final float list[], final int newSize) {
        final float temp[] = new float[newSize];
        System.arraycopy(list, 0, temp, 0, Math.min(newSize, list.length));
        return temp;
    }

    public static String[] expand(final String list[]) {
        return RainbowMath.expand(list, list.length << 1);
    }

    public static String[] expand(final String list[], final int newSize) {
        final String temp[] = new String[newSize];
        // in case the new size is smaller than list.length
        System.arraycopy(list, 0, temp, 0, Math.min(newSize, list.length));
        return temp;
    }

    public static Object expand(final Object array) {
        return RainbowMath.expand(array, Array.getLength(array) << 1);
    }

    public static Object expand(final Object list, final int newSize) {
        final Class<?> type = list.getClass().getComponentType();
        final Object temp = Array.newInstance(type, newSize);
        System.arraycopy(list, 0, temp, 0, Math.min(Array.getLength(list), newSize));
        return temp;
    }

    public static byte[] append(byte b[], final byte value) {
        b = RainbowMath.expand(b, b.length + 1);
        b[b.length - 1] = value;
        return b;
    }

    public static char[] append(char b[], final char value) {
        b = RainbowMath.expand(b, b.length + 1);
        b[b.length - 1] = value;
        return b;
    }

    public static int[] append(int b[], final int value) {
        b = RainbowMath.expand(b, b.length + 1);
        b[b.length - 1] = value;
        return b;
    }

    public static float[] append(float b[], final float value) {
        b = RainbowMath.expand(b, b.length + 1);
        b[b.length - 1] = value;
        return b;
    }

    public static String[] append(String b[], final String value) {
        b = RainbowMath.expand(b, b.length + 1);
        b[b.length - 1] = value;
        return b;
    }

    public static Object append(Object b, final Object value) {
        final int length = Array.getLength(b);
        b = RainbowMath.expand(b, length + 1);
        Array.set(b, length, value);
        return b;
    }

    public static boolean[] shorten(final boolean list[]) {
        return RainbowMath.subset(list, 0, list.length - 1);
    }

    public static boolean[] subset(final boolean list[], final int start, final int count) {
        final boolean output[] = new boolean[count];
        System.arraycopy(list, start, output, 0, count);
        return output;
    }

    public static byte[] shorten(final byte list[]) {
        return RainbowMath.subset(list, 0, list.length - 1);
    }

    public static byte[] subset(final byte list[], final int start, final int count) {
        final byte output[] = new byte[count];
        System.arraycopy(list, start, output, 0, count);
        return output;
    }

    public static char[] shorten(final char list[]) {
        return RainbowMath.subset(list, 0, list.length - 1);
    }

    public static char[] subset(final char list[], final int start, final int count) {
        final char output[] = new char[count];
        System.arraycopy(list, start, output, 0, count);
        return output;
    }

    public static int[] shorten(final int list[]) {
        return RainbowMath.subset(list, 0, list.length - 1);
    }

    public static int[] subset(final int list[], final int start, final int count) {
        final int output[] = new int[count];
        System.arraycopy(list, start, output, 0, count);
        return output;
    }

    public static float[] shorten(final float list[]) {
        return RainbowMath.subset(list, 0, list.length - 1);
    }

    public static float[] subset(final float list[], final int start, final int count) {
        final float output[] = new float[count];
        System.arraycopy(list, start, output, 0, count);
        return output;
    }

    public static String[] shorten(final String list[]) {
        return RainbowMath.subset(list, 0, list.length - 1);
    }

    public static String[] subset(final String list[], final int start, final int count) {
        final String output[] = new String[count];
        System.arraycopy(list, start, output, 0, count);
        return output;
    }

    public static Object shorten(final Object list) {
        final int length = Array.getLength(list);
        return RainbowMath.subset(list, 0, length - 1);
    }

    public static Object subset(final Object list, final int start, final int count) {
        final Class<?> type = list.getClass().getComponentType();
        final Object outgoing = Array.newInstance(type, count);
        System.arraycopy(list, start, outgoing, 0, count);
        return outgoing;
    }

    static final public boolean[] splice(final boolean list[], final boolean v, final int index) {
        final boolean outgoing[] = new boolean[list.length + 1];
        System.arraycopy(list, 0, outgoing, 0, index);
        outgoing[index] = v;
        System.arraycopy(list, index, outgoing, index + 1, list.length - index);
        return outgoing;
    }

    static final public boolean[] splice(final boolean list[], final boolean v[], final int index) {
        final boolean outgoing[] = new boolean[list.length + v.length];
        System.arraycopy(list, 0, outgoing, 0, index);
        System.arraycopy(v, 0, outgoing, index, v.length);
        System.arraycopy(list, index, outgoing, index + v.length, list.length - index);
        return outgoing;
    }

    static final public byte[] splice(final byte list[], final byte v, final int index) {
        final byte outgoing[] = new byte[list.length + 1];
        System.arraycopy(list, 0, outgoing, 0, index);
        outgoing[index] = v;
        System.arraycopy(list, index, outgoing, index + 1, list.length - index);
        return outgoing;
    }

    static final public byte[] splice(final byte list[], final byte v[], final int index) {
        final byte outgoing[] = new byte[list.length + v.length];
        System.arraycopy(list, 0, outgoing, 0, index);
        System.arraycopy(v, 0, outgoing, index, v.length);
        System.arraycopy(list, index, outgoing, index + v.length, list.length - index);
        return outgoing;
    }

    static final public char[] splice(final char list[], final char v, final int index) {
        final char outgoing[] = new char[list.length + 1];
        System.arraycopy(list, 0, outgoing, 0, index);
        outgoing[index] = v;
        System.arraycopy(list, index, outgoing, index + 1, list.length - index);
        return outgoing;
    }

    static final public char[] splice(final char list[], final char v[], final int index) {
        final char outgoing[] = new char[list.length + v.length];
        System.arraycopy(list, 0, outgoing, 0, index);
        System.arraycopy(v, 0, outgoing, index, v.length);
        System.arraycopy(list, index, outgoing, index + v.length, list.length - index);
        return outgoing;
    }

    // ////////////////////////////////////////////////////////////

    // RANDOM NUMBERS

    static final public int[] splice(final int list[], final int v, final int index) {
        final int outgoing[] = new int[list.length + 1];
        System.arraycopy(list, 0, outgoing, 0, index);
        outgoing[index] = v;
        System.arraycopy(list, index, outgoing, index + 1, list.length - index);
        return outgoing;
    }

    static final public int[] splice(final int list[], final int v[], final int index) {
        final int outgoing[] = new int[list.length + v.length];
        System.arraycopy(list, 0, outgoing, 0, index);
        System.arraycopy(v, 0, outgoing, index, v.length);
        System.arraycopy(list, index, outgoing, index + v.length, list.length - index);
        return outgoing;
    }

    static final public float[] splice(final float list[], final float v, final int index) {
        final float outgoing[] = new float[list.length + 1];
        System.arraycopy(list, 0, outgoing, 0, index);
        outgoing[index] = v;
        System.arraycopy(list, index, outgoing, index + 1, list.length - index);
        return outgoing;
    }

    static final public float[] splice(final float list[], final float v[], final int index) {
        final float outgoing[] = new float[list.length + v.length];
        System.arraycopy(list, 0, outgoing, 0, index);
        System.arraycopy(v, 0, outgoing, index, v.length);
        System.arraycopy(list, index, outgoing, index + v.length, list.length - index);
        return outgoing;
    }

    // ////////////////////////////////////////////////////////////

    // PERLIN NOISE

    // [toxi 040903]
    // octaves and amplitude amount per octave are now user controlled
    // via the noiseDetail() function.

    // [toxi 030902]
    // cleaned up code and now using bagel's cosine table to speed up

    // [toxi 030901]
    // implementation by the german demo group farbrausch
    // as used in their demo "art": http://www.farb-rausch.de/fr010src.zip

    static final public String[] splice(final String list[], final String v, final int index) {
        final String outgoing[] = new String[list.length + 1];
        System.arraycopy(list, 0, outgoing, 0, index);
        outgoing[index] = v;
        System.arraycopy(list, index, outgoing, index + 1, list.length - index);
        return outgoing;
    }

    static final public String[] splice(final String list[], final String v[], final int index) {
        final String outgoing[] = new String[list.length + v.length];
        System.arraycopy(list, 0, outgoing, 0, index);
        System.arraycopy(v, 0, outgoing, index, v.length);
        System.arraycopy(list, index, outgoing, index + v.length, list.length - index);
        return outgoing;
    }

    static final public Object splice(final Object list, final Object v, final int index) {
        Object[] outgoing = null;
        final int length = Array.getLength(list);

        // check whether item being spliced in is an array
        if (v.getClass().getName().charAt(0) == '[') {
            final int vlength = Array.getLength(v);
            outgoing = new Object[length + vlength];
            System.arraycopy(list, 0, outgoing, 0, index);
            System.arraycopy(v, 0, outgoing, index, vlength);
            System.arraycopy(list, index, outgoing, index + vlength, length - index);

        } else {
            outgoing = new Object[length + 1];
            System.arraycopy(list, 0, outgoing, 0, index);
            Array.set(outgoing, index, v);
            System.arraycopy(list, index, outgoing, index + 1, length - index);
        }
        return outgoing;
    }

    public static boolean[] subset(final boolean list[], final int start) {
        return RainbowMath.subset(list, start, list.length - start);
    }

    public static byte[] subset(final byte list[], final int start) {
        return RainbowMath.subset(list, start, list.length - start);
    }

    public static char[] subset(final char list[], final int start) {
        return RainbowMath.subset(list, start, list.length - start);
    }

    public static int[] subset(final int list[], final int start) {
        return RainbowMath.subset(list, start, list.length - start);
    }

    public static float[] subset(final float list[], final int start) {
        return RainbowMath.subset(list, start, list.length - start);
    }

    public static String[] subset(final String list[], final int start) {
        return RainbowMath.subset(list, start, list.length - start);
    }

    public static Object subset(final Object list, final int start) {
        final int length = Array.getLength(list);
        return RainbowMath.subset(list, start, length - start);
    }

    public static boolean[] concat(final boolean a[], final boolean b[]) {
        final boolean c[] = new boolean[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static byte[] concat(final byte a[], final byte b[]) {
        final byte c[] = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static char[] concat(final char a[], final char b[]) {
        final char c[] = new char[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static int[] concat(final int a[], final int b[]) {
        final int c[] = new int[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static float[] concat(final float a[], final float b[]) {
        final float c[] = new float[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    // [toxi 040903]
    // make perlin noise quality user controlled to allow
    // for different levels of detail. lower values will produce
    // smoother results as higher octaves are surpressed

    public static String[] concat(final String a[], final String b[]) {
        final String c[] = new String[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static Object concat(final Object a, final Object b) {
        final Class<?> type = a.getClass().getComponentType();
        final int alength = Array.getLength(a);
        final int blength = Array.getLength(b);
        final Object outgoing = Array.newInstance(type, alength + blength);
        System.arraycopy(a, 0, outgoing, 0, alength);
        System.arraycopy(b, 0, outgoing, alength, blength);
        return outgoing;
    }

    public static boolean[] reverse(final boolean list[]) {
        final boolean outgoing[] = new boolean[list.length];
        final int length1 = list.length - 1;
        for (int i = 0; i < list.length; i++) {
            outgoing[i] = list[length1 - i];
        }
        return outgoing;
    }

    public static byte[] reverse(final byte list[]) {
        final byte outgoing[] = new byte[list.length];
        final int length1 = list.length - 1;
        for (int i = 0; i < list.length; i++) {
            outgoing[i] = list[length1 - i];
        }
        return outgoing;
    }

    public static char[] reverse(final char list[]) {
        final char outgoing[] = new char[list.length];
        final int length1 = list.length - 1;
        for (int i = 0; i < list.length; i++) {
            outgoing[i] = list[length1 - i];
        }
        return outgoing;
    }

    public static int[] reverse(final int list[]) {
        final int outgoing[] = new int[list.length];
        final int length1 = list.length - 1;
        for (int i = 0; i < list.length; i++) {
            outgoing[i] = list[length1 - i];
        }
        return outgoing;
    }

    public static float[] reverse(final float list[]) {
        final float outgoing[] = new float[list.length];
        final int length1 = list.length - 1;
        for (int i = 0; i < list.length; i++) {
            outgoing[i] = list[length1 - i];
        }
        return outgoing;
    }

    public static String[] reverse(final String list[]) {
        final String outgoing[] = new String[list.length];
        final int length1 = list.length - 1;
        for (int i = 0; i < list.length; i++) {
            outgoing[i] = list[length1 - i];
        }
        return outgoing;
    }

    public static Object reverse(final Object list) {
        final Class<?> type = list.getClass().getComponentType();
        final int length = Array.getLength(list);
        final Object outgoing = Array.newInstance(type, length);
        for (int i = 0; i < length; i++) {
            Array.set(outgoing, i, Array.get(list, (length - 1) - i));
        }
        return outgoing;
    }

    /**
     * Remove whitespace characters from the beginning and ending of a String.
     * Works like String.trim() but includes the unicode nbsp character as well.
     */
    public static String trim(final String str) {
        return str.replace('\u00A0', ' ').trim();
    }

    /**
     * Trim the whitespace from a String array. This returns a new array and
     * does not affect the passed-in array.
     */
    public static String[] trim(final String[] array) {
        final String[] outgoing = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                outgoing[i] = array[i].replace('\u00A0', ' ').trim();
            }
        }
        return outgoing;
    }

    /**
     * Join an array of Strings together as a single String, separated by the
     * whatever's passed in for the separator.
     */
    public static String join(final String str[], final char separator) {
        return RainbowMath.join(str, String.valueOf(separator));
    }

    /**
     * Join an array of Strings together as a single String, separated by the
     * whatever's passed in for the separator.
     * <p/>
     * To use this on numbers, first pass the array to nf() or nfs() to get a
     * list of String objects, then use join on that.
     * <p/>
     * <PRE>
     * e.g.String stuff[] = { &quot;apple&quot;, &quot;bear&quot;, &quot;cat&quot; };
     * String list = join(stuff, &quot;, &quot;);
     * // list is now &quot;apple, bear, cat&quot;
     * </PRE>
     */
    public static String join(final String str[], final String separator) {
        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            if (i != 0) {
                buffer.append(separator);
            }
            buffer.append(str[i]);
        }
        return buffer.toString();
    }

    // ////////////////////////////////////////////////////////////

    // DATA I/O

    /**
     * Splits a string into pieces, using any of the chars in the String 'delim'
     * as separator characters. For instance, in addition to white space, you
     * might want to treat commas as a separator. The delimeter characters won't
     * appear in the returned String array.
     * <p/>
     * <PRE>
     * i.e. splitTokens("a, b", " ,") -> { "a", "b" }
     * </PRE>
     * <p/>
     * To include all the whitespace possibilities, use the variable WHITESPACE,
     * found in PConstants:
     * <p/>
     * <PRE>
     * i.e. splitTokens("a   | b", WHITESPACE + "|");  ->  { "a", "b" }
     * </PRE>
     */
    public static String[] splitTokens(final String what, final String delim) {
        final StringTokenizer toker = new StringTokenizer(what, delim);
        final String pieces[] = new String[toker.countTokens()];

        int index = 0;
        while (toker.hasMoreTokens()) {
            pieces[index++] = toker.nextToken();
        }
        return pieces;
    }

    /**
     * Split a string into pieces along a specific character. Most commonly used
     * to break up a String along a space or a tab character.
     * <p/>
     * This operates differently than the others, where the single delimeter is
     * the only breaking point, and consecutive delimeters will produce an empty
     * string (""). This way, one can split on tab characters, but maintain the
     * column alignments (of say an excel file) where there are empty columns.
     */
    public static String[] split(final String what, final char delim) {
        // do this so that the exception occurs inside the user's
        // program, rather than appearing to be a bug inside split()
        if (what == null) {
            return null;
            // return split(what, String.valueOf(delim)); // huh
        }

        final char chars[] = what.toCharArray();
        int splitCount = 0; // 1;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == delim) {
                splitCount++;
            }
        }
        // make sure that there is something in the input string
        // if (chars.length > 0) {
        // if the last char is a delimeter, get rid of it..
        // if (chars[chars.length-1] == delim) splitCount--;
        // on second thought, i don't agree with this, will disable
        // }
        if (splitCount == 0) {
            final String splits[] = new String[1];
            splits[0] = new String(what);
            return splits;
        }
        // int pieceCount = splitCount + 1;
        final String splits[] = new String[splitCount + 1];
        int splitIndex = 0;
        int startIndex = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == delim) {
                splits[splitIndex++] = new String(chars, startIndex, i - startIndex);
                startIndex = i + 1;
            }
        }
        // if (startIndex != chars.length) {
        splits[splitIndex] = new String(chars, startIndex, chars.length - startIndex);
        // }
        return splits;
    }

    /**
     * Split a String on a specific delimiter. Unlike Java's String.split()
     * method, this does not parse the delimiter as a regexp because it's more
     * confusing than necessary, and String.split() is always available for
     * those who want regexp.
     */
    public static String[] split(final String what, final String delim) {
        final ArrayList<String> items = new ArrayList<String>();
        int index;
        int offset = 0;
        while ((index = what.indexOf(delim, offset)) != -1) {
            items.add(what.substring(offset, index));
            offset = index + delim.length();
        }
        items.add(what.substring(offset));
        final String[] outgoing = new String[items.size()];
        items.toArray(outgoing);
        return outgoing;
    }

    /**
     * Match a string with a regular expression, and returns the match as an
     * array. The first index is the matching expression, and array elements [1]
     * and higher represent each of the groups (sequences found in parens).
     * <p/>
     * This uses multiline matching (Pattern.MULTILINE) and dotall mode
     * (Pattern.DOTALL) by default, so that ^ and $ match the beginning and end
     * of any lines found in the source, and the . operator will also pick up
     * newline characters.
     */
    public static String[] match(final String what, final String regexp) {
        final Pattern p = RainbowMath.matchPattern(regexp);
        final Matcher m = p.matcher(what);
        if (m.find()) {
            final int count = m.groupCount() + 1;
            final String[] groups = new String[count];
            for (int i = 0; i < count; i++) {
                groups[i] = m.group(i);
            }
            return groups;
        }
        return null;
    }

    static Pattern matchPattern(final String regexp) {
        Pattern p = null;
        if (RainbowMath.matchPatterns == null) {
            RainbowMath.matchPatterns = new HashMap<String, Pattern>();
        } else {
            p = RainbowMath.matchPatterns.get(regexp);
        }
        if (p == null) {
            if (RainbowMath.matchPatterns.size() == 10) {
                // Just clear out the match patterns here if more than 10 are
                // being
                // used. It's not terribly efficient, but changes that you have
                // >10
                // different match patterns are very slim, unless you're doing
                // something really tricky (like custom match() methods), in
                // which
                // case match() won't be efficient anyway. (And you should just
                // be
                // using your own Java code.) The alternative is using a queue
                // here,
                // but that's a silly amount of work for negligible benefit.
                RainbowMath.matchPatterns.clear();
            }
            p = Pattern.compile(regexp, Pattern.MULTILINE | Pattern.DOTALL);
            RainbowMath.matchPatterns.put(regexp, p);
        }
        return p;
    }

    /**
     * Identical to match(), except that it returns an array of all matches in
     * the specified String, rather than just the first.
     */
    public static String[][] matchAll(final String what, final String regexp) {
        final Pattern p = RainbowMath.matchPattern(regexp);
        final Matcher m = p.matcher(what);
        final ArrayList<String[]> results = new ArrayList<String[]>();
        final int count = m.groupCount() + 1;
        while (m.find()) {
            final String[] groups = new String[count];
            for (int i = 0; i < count; i++) {
                groups[i] = m.group(i);
            }
            results.add(groups);
        }
        if (results.isEmpty()) {
            return null;
        }
        final String[][] matches = new String[results.size()][count];
        for (int i = 0; i < matches.length; i++) {
            matches[i] = results.get(i);
        }
        return matches;
    }

    /**
     * <p>
     * Convert an integer to a boolean. Because of how Java handles upgrading
     * numbers, this will also cover byte and char (as they will upgrade to an
     * int without any sort of explicit cast).
     * </p>
     * <p>
     * The preprocessor will convert boolean(what) to parseBoolean(what).
     * </p>
     *
     * @return false if 0, true if any other number
     */
    static final public boolean parseBoolean(final int what) {
        return (what != 0);
    }

    /**
     * Convert the string "true" or "false" to a boolean.
     *
     * @return true if 'what' is "true" or "TRUE", false otherwise
     */
    static final public boolean parseBoolean(final String what) {
        return Boolean.valueOf(what);
    }

    /**
     * Convert a byte array to a boolean array. Each element will be evaluated
     * identical to the integer case, where a byte equal to zero will return
     * false, and any other value will return true.
     *
     * @return array of boolean elements
     */
    static final public boolean[] parseBoolean(final byte what[]) {
        final boolean outgoing[] = new boolean[what.length];
        for (int i = 0; i < what.length; i++) {
            outgoing[i] = (what[i] != 0);
        }
        return outgoing;
    }

    /**
     * Convert an int array to a boolean array. An int equal to zero will return
     * false, and any other value will return true.
     *
     * @return array of boolean elements
     */
    static final public boolean[] parseBoolean(final int what[]) {
        final boolean outgoing[] = new boolean[what.length];
        for (int i = 0; i < what.length; i++) {
            outgoing[i] = (what[i] != 0);
        }
        return outgoing;
    }

    static final public boolean[] parseBoolean(final String what[]) {
        final boolean outgoing[] = new boolean[what.length];
        for (int i = 0; i < what.length; i++) {
            outgoing[i] = new Boolean(what[i]).booleanValue();
        }
        return outgoing;
    }

    static final public byte parseByte(final boolean what) {
        return what ? (byte) 1 : 0;
    }

    // FONT I/O

    static final public byte parseByte(final char what) {
        return (byte) what;
    }

    static final public byte parseByte(final int what) {
        return (byte) what;
    }

    static final public byte parseByte(final float what) {
        return (byte) what;
    }

    static final public byte[] parseByte(final boolean what[]) {
        final byte outgoing[] = new byte[what.length];
        for (int i = 0; i < what.length; i++) {
            outgoing[i] = what[i] ? (byte) 1 : 0;
        }
        return outgoing;
    }

    static final public byte[] parseByte(final char what[]) {
        final byte outgoing[] = new byte[what.length];
        for (int i = 0; i < what.length; i++) {
            outgoing[i] = (byte) what[i];
        }
        return outgoing;
    }

    // ////////////////////////////////////////////////////////////

    // READERS AND WRITERS

    static final public byte[] parseByte(final int what[]) {
        final byte outgoing[] = new byte[what.length];
        for (int i = 0; i < what.length; i++) {
            outgoing[i] = (byte) what[i];
        }
        return outgoing;
    }

    static final public byte[] parseByte(final float what[]) {
        final byte outgoing[] = new byte[what.length];
        for (int i = 0; i < what.length; i++) {
            outgoing[i] = (byte) what[i];
        }
        return outgoing;
    }

    static final public char parseChar(final byte what) {
        return (char) (what & 0xff);
    }

    static final public char parseChar(final int what) {
        return (char) what;
    }

    static final public char[] parseChar(final byte what[]) {
        final char outgoing[] = new char[what.length];
        for (int i = 0; i < what.length; i++) {
            outgoing[i] = (char) (what[i] & 0xff);
        }
        return outgoing;
    }

    static final public char[] parseChar(final int what[]) {
        final char outgoing[] = new char[what.length];
        for (int i = 0; i < what.length; i++) {
            outgoing[i] = (char) what[i];
        }
        return outgoing;
    }

    // ////////////////////////////////////////////////////////////

    // FILE INPUT

    static final public int parseInt(final boolean what) {
        return what ? 1 : 0;
    }

    /**
     * Note that parseInt() will un-sign a signed byte value.
     */
    static final public int parseInt(final byte what) {
        return what & 0xff;
    }

    /**
     * Note that parseInt('5') is unlike String in the sense that it won't
     * return 5, but the ascii value. This is because ((int) someChar) returns
     * the ascii value, and parseInt() is just longhand for the cast.
     */
    static final public int parseInt(final char what) {
        return what;
    }

    /**
     * Same as floor(), or an (int) cast.
     */
    static final public int parseInt(final float what) {
        return (int) what;
    }

    /**
     * Parse a String into an int value. Returns 0 if the value is bad.
     */
    static final public int parseInt(final String what) {
        return RainbowMath.parseInt(what, 0);
    }

    /**
     * Parse a String to an int, and provide an alternate value that should be
     * used when the number is invalid.
     */
    static final public int parseInt(final String what, final int otherwise) {
        try {
            final int offset = what.indexOf('.');
            if (offset == -1) {
                return Integer.parseInt(what);
            } else {
                return Integer.parseInt(what.substring(0, offset));
            }
        } catch (final NumberFormatException e) {
        }
        return otherwise;
    }

    static final public int[] parseInt(final boolean what[]) {
        final int list[] = new int[what.length];
        for (int i = 0; i < what.length; i++) {
            list[i] = what[i] ? 1 : 0;
        }
        return list;
    }

    static final public int[] parseInt(final byte what[]) { // note this unsigns
        final int list[] = new int[what.length];
        for (int i = 0; i < what.length; i++) {
            list[i] = (what[i] & 0xff);
        }
        return list;
    }

    static final public int[] parseInt(final char what[]) {
        final int list[] = new int[what.length];
        for (int i = 0; i < what.length; i++) {
            list[i] = what[i];
        }
        return list;
    }

    public static int[] parseInt(final float what[]) {
        final int inties[] = new int[what.length];
        for (int i = 0; i < what.length; i++) {
            inties[i] = (int) what[i];
        }
        return inties;
    }

    // ////////////////////////////////////////////////////////////

    // FILE OUTPUT

    /**
     * Make an array of int elements from an array of String objects. If the
     * String can't be parsed as a number, it will be set to zero.
     * <p/>
     * String s[] = { "1", "300", "44" }; int numbers[] = parseInt(s);
     * <p/>
     * numbers will contain { 1, 300, 44 }
     */
    public static int[] parseInt(final String what[]) {
        return RainbowMath.parseInt(what, 0);
    }

    /**
     * Make an array of int elements from an array of String objects. If the
     * String can't be parsed as a number, its entry in the array will be set to
     * the value of the "missing" parameter.
     * <p/>
     * String s[] = { "1", "300", "apple", "44" }; int numbers[] = parseInt(s,
     * 9999);
     * <p/>
     * numbers will contain { 1, 300, 9999, 44 }
     */
    public static int[] parseInt(final String what[], final int missing) {
        final int output[] = new int[what.length];
        for (int i = 0; i < what.length; i++) {
            try {
                output[i] = Integer.parseInt(what[i]);
            } catch (final NumberFormatException e) {
                output[i] = missing;
            }
        }
        return output;
    }

    /**
     * Convert an int to a float value. Also handles bytes because of Java's
     * rules for upgrading values.
     */
    static final public float parseFloat(final int what) { // also handles byte
        return what;
    }

    static final public float parseFloat(final String what) {
        return RainbowMath.parseFloat(what, Float.NaN);
    }

    static final public float parseFloat(final String what, final float otherwise) {
        try {
            return new Float(what).floatValue();
        } catch (final NumberFormatException e) {
        }

        return otherwise;
    }

    static final public float[] parseByte(final byte what[]) {
        final float floaties[] = new float[what.length];
        for (int i = 0; i < what.length; i++) {
            floaties[i] = what[i];
        }
        return floaties;
    }

    static final public float[] parseFloat(final int what[]) {
        final float floaties[] = new float[what.length];
        for (int i = 0; i < what.length; i++) {
            floaties[i] = what[i];
        }
        return floaties;
    }

    static final public float[] parseFloat(final String what[]) {
        return RainbowMath.parseFloat(what, Float.NaN);
    }

    static final public float[] parseFloat(final String what[], final float missing) {
        final float output[] = new float[what.length];
        for (int i = 0; i < what.length; i++) {
            try {
                output[i] = new Float(what[i]).floatValue();
            } catch (final NumberFormatException e) {
                output[i] = missing;
            }
        }
        return output;
    }

    //

    static final public String str(final boolean x) {
        return String.valueOf(x);
    }

    static final public String str(final byte x) {
        return String.valueOf(x);
    }

    static final public String str(final char x) {
        return String.valueOf(x);
    }

    // ////////////////////////////////////////////////////////////

    static final public String str(final int x) {
        return String.valueOf(x);
    }

    static final public String str(final float x) {
        return String.valueOf(x);
    }

    static final public String[] str(final boolean x[]) {
        final String s[] = new String[x.length];
        for (int i = 0; i < x.length; i++) {
            s[i] = String.valueOf(x[i]);
        }
        return s;
    }

    static final public String[] str(final byte x[]) {
        final String s[] = new String[x.length];
        for (int i = 0; i < x.length; i++) {
            s[i] = String.valueOf(x[i]);
        }
        return s;
    }

    static final public String[] str(final char x[]) {
        final String s[] = new String[x.length];
        for (int i = 0; i < x.length; i++) {
            s[i] = String.valueOf(x[i]);
        }
        return s;
    }

    static final public String[] str(final int x[]) {
        final String s[] = new String[x.length];
        for (int i = 0; i < x.length; i++) {
            s[i] = String.valueOf(x[i]);
        }
        return s;
    }

    static final public String[] str(final float x[]) {
        final String s[] = new String[x.length];
        for (int i = 0; i < x.length; i++) {
            s[i] = String.valueOf(x[i]);
        }
        return s;
    }

    // ////////////////////////////////////////////////////////////

    // URL ENCODING

    public static String[] nf(final int num[], final int digits) {
        final String formatted[] = new String[num.length];
        for (int i = 0; i < formatted.length; i++) {
            formatted[i] = RainbowMath.nf(num[i], digits);
        }
        return formatted;
    }

    public static String nf(final int num, final int digits) {
        if ((RainbowMath.int_nf != null) && (RainbowMath.int_nf_digits == digits) && !RainbowMath.int_nf_commas) {
            return RainbowMath.int_nf.format(num);
        }

        RainbowMath.int_nf = NumberFormat.getInstance();
        RainbowMath.int_nf.setGroupingUsed(false); // no commas
        RainbowMath.int_nf_commas = false;
        RainbowMath.int_nf.setMinimumIntegerDigits(digits);
        RainbowMath.int_nf_digits = digits;
        return RainbowMath.int_nf.format(num);
    }

    // ////////////////////////////////////////////////////////////
    // SORT

    public static String[] nfc(final int num[]) {
        final String formatted[] = new String[num.length];
        for (int i = 0; i < formatted.length; i++) {
            formatted[i] = RainbowMath.nfc(num[i]);
        }
        return formatted;
    }

    public static String nfc(final int num) {
        if ((RainbowMath.int_nf != null) && (RainbowMath.int_nf_digits == 0) && RainbowMath.int_nf_commas) {
            return RainbowMath.int_nf.format(num);
        }

        RainbowMath.int_nf = NumberFormat.getInstance();
        RainbowMath.int_nf.setGroupingUsed(true);
        RainbowMath.int_nf_commas = true;
        RainbowMath.int_nf.setMinimumIntegerDigits(0);
        RainbowMath.int_nf_digits = 0;
        return RainbowMath.int_nf.format(num);
    }

    public static String[] nfs(final int num[], final int digits) {
        final String formatted[] = new String[num.length];
        for (int i = 0; i < formatted.length; i++) {
            formatted[i] = RainbowMath.nfs(num[i], digits);
        }
        return formatted;
    }

    /**
     * number format signed (or space) Formats a number but leaves a blank space
     * in the front when it's positive so that it can be properly aligned with
     * numbers that have a negative sign in front of them.
     */
    public static String nfs(final int num, final int digits) {
        return (num < 0) ? RainbowMath.nf(num, digits) : (' ' + RainbowMath.nf(num, digits));
    }

    public static String[] nfp(final int num[], final int digits) {
        final String formatted[] = new String[num.length];
        for (int i = 0; i < formatted.length; i++) {
            formatted[i] = RainbowMath.nfp(num[i], digits);
        }
        return formatted;
    }

    /**
     * number format positive (or plus) Formats a number, always placing a - or
     * + sign in the front when it's negative or positive.
     */
    public static String nfp(final int num, final int digits) {
        return (num < 0) ? RainbowMath.nf(num, digits) : ('+' + RainbowMath.nf(num, digits));
    }

    public static String[] nf(final float num[], final int left, final int right) {
        final String formatted[] = new String[num.length];
        for (int i = 0; i < formatted.length; i++) {
            formatted[i] = RainbowMath.nf(num[i], left, right);
        }
        return formatted;
    }

    public static String nf(final float num, final int left, final int right) {
        if ((RainbowMath.float_nf != null) && (RainbowMath.float_nf_left == left) && (RainbowMath.float_nf_right == right) && !RainbowMath.float_nf_commas) {
            return RainbowMath.float_nf.format(num);
        }

        RainbowMath.float_nf = NumberFormat.getInstance();
        RainbowMath.float_nf.setGroupingUsed(false);
        RainbowMath.float_nf_commas = false;

        if (left != 0) {
            RainbowMath.float_nf.setMinimumIntegerDigits(left);
        }
        if (right != 0) {
            RainbowMath.float_nf.setMinimumFractionDigits(right);
            RainbowMath.float_nf.setMaximumFractionDigits(right);
        }
        RainbowMath.float_nf_left = left;
        RainbowMath.float_nf_right = right;
        return RainbowMath.float_nf.format(num);
    }

    public static String[] nfc(final float num[], final int right) {
        final String formatted[] = new String[num.length];
        for (int i = 0; i < formatted.length; i++) {
            formatted[i] = RainbowMath.nfc(num[i], right);
        }
        return formatted;
    }

    public static String nfc(final float num, final int right) {
        if ((RainbowMath.float_nf != null) && (RainbowMath.float_nf_left == 0) && (RainbowMath.float_nf_right == right) && RainbowMath.float_nf_commas) {
            return RainbowMath.float_nf.format(num);
        }

        RainbowMath.float_nf = NumberFormat.getInstance();
        RainbowMath.float_nf.setGroupingUsed(true);
        RainbowMath.float_nf_commas = true;

        if (right != 0) {
            RainbowMath.float_nf.setMinimumFractionDigits(right);
            RainbowMath.float_nf.setMaximumFractionDigits(right);
        }
        RainbowMath.float_nf_left = 0;
        RainbowMath.float_nf_right = right;
        return RainbowMath.float_nf.format(num);
    }

    // ////////////////////////////////////////////////////////////
    // ARRAY UTILITIES

    /**
     * Number formatter that takes into account whether the number has a sign
     * (positive, negative, etc) in front of it.
     */
    public static String[] nfs(final float num[], final int left, final int right) {
        final String formatted[] = new String[num.length];
        for (int i = 0; i < formatted.length; i++) {
            formatted[i] = RainbowMath.nfs(num[i], left, right);
        }
        return formatted;
    }

    public static String nfs(final float num, final int left, final int right) {
        return (num < 0) ? RainbowMath.nf(num, left, right) : (' ' + RainbowMath.nf(num, left, right));
    }

    public static String[] nfp(final float num[], final int left, final int right) {
        final String formatted[] = new String[num.length];
        for (int i = 0; i < formatted.length; i++) {
            formatted[i] = RainbowMath.nfp(num[i], left, right);
        }
        return formatted;
    }

    //

    public static String nfp(final float num, final int left, final int right) {
        return (num < 0) ? RainbowMath.nf(num, left, right) : ('+' + RainbowMath.nf(num, left, right));
    }

    /**
     * Convert a byte into a two digit hex string.
     */
    static final public String hex(final byte what) {
        return RainbowMath.hex(what, 2);
    }

    /**
     * Format an integer as a hex string using the specified number of digits.
     *
     * @param what   the value to format
     * @param digits the number of digits (maximum 8)
     * @return a String object with the formatted values
     */
    static final public String hex(final int what, int digits) {
        final String stuff = Integer.toHexString(what).toUpperCase();
        if (digits > 8) {
            digits = 8;
        }

        final int length = stuff.length();
        if (length > digits) {
            return stuff.substring(length - digits);

        } else if (length < digits) {
            return "00000000".substring(8 - (digits - length)) + stuff;
        }
        return stuff;
    }

    /**
     * Convert a Unicode character into a four digit hex string.
     */
    static final public String hex(final char what) {
        return RainbowMath.hex(what, 4);
    }

    /**
     * Convert an integer into an eight digit hex string.
     */
    static final public String hex(final int what) {
        return RainbowMath.hex(what, 8);
    }

    static final public int unhex(final String what) {
        // has to parse as a Long so that it'll work for numbers bigger than
        // 2^31
        return (int) (Long.parseLong(what, 16));
    }

    /**
     * Returns a String that contains the binary value of a byte. The returned
     * value will always have 8 digits.
     */
    static final public String binary(final byte what) {
        return RainbowMath.binary(what, 8);
    }

    /**
     * Returns a String that contains the binary value of an int. The digits
     * parameter determines how many digits will be used.
     */
    static final public String binary(final int what, int digits) {
        final String stuff = Integer.toBinaryString(what);
        if (digits > 32) {
            digits = 32;
        }

        final int length = stuff.length();
        if (length > digits) {
            return stuff.substring(length - digits);

        } else if (length < digits) {
            final int offset = 32 - (digits - length);
            return "00000000000000000000000000000000".substring(offset) + stuff;
        }
        return stuff;
    }

    /**
     * Returns a String that contains the binary value of a char. The returned
     * value will always have 16 digits because chars are two bytes long.
     */
    static final public String binary(final char what) {
        return RainbowMath.binary(what, 16);
    }

    /**
     * Returns a String that contains the binary value of an int. The length
     * depends on the size of the number itself. If you want a specific number
     * of digits use binary(int what, int digits) to specify how many.
     */
    static final public String binary(final int what) {
        return RainbowMath.binary(what, 32);
    }

    /**
     * Unpack a binary String into an int. i.e. unbinary("00001000") would
     * return 8.
     */
    static final public int unbinary(final String what) {
        return Integer.parseInt(what, 2);
    }

    /**
     * Return a random number in the range [howsmall, howbig).
     * <p/>
     * The number returned will range from 'howsmall' up to (but not including
     * 'howbig'.
     * <p/>
     * If howsmall is >= howbig, howsmall will be returned, meaning that
     * random(5, 5) will return 5 (useful) and random(7, 4) will return 7 (not
     * useful.. better idea?)
     */
    public static final float random(final float howsmall, final float howbig) {
        if (howsmall >= howbig) {
            return howsmall;
        }
        final float diff = howbig - howsmall;
        return random(diff) + howsmall;
    }

    /**
     * Return a random number in the range [0, howbig).
     * <p/>
     * The number returned will range from zero up to (but not including)
     * 'howbig'.
     */
    public static final float random(final float howbig) {
        // for some reason (rounding error?) Math.random() * 3
        // can sometimes return '3' (once in ~30 million tries)
        // so a check was added to avoid the inclusion of 'howbig'

        // avoid an infinite loop
        if (howbig == 0) {
            return 0;
        }

        // internal random number object
        if (internalRandom == null) {
            internalRandom = new Random();
        }

        float value = 0;
        do {
            // value = (float)Math.random() * howbig;
            value = internalRandom.nextFloat() * howbig;
        } while (value == howbig);
        return value;
    }

    public static final void randomSeed(final long what) {
        // internal random number object
        if (internalRandom == null) {
            internalRandom = new Random();
        }
        internalRandom.setSeed(what);
    }

    /**
     * Computes the Perlin noise function value at point x.
     */
    public static float noise(final float x) {
        // is this legit? it's a dumb way to do it (but repair it later)
        return noise(x, 0f, 0f);
    }

    // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

    /**
     * Computes the Perlin noise function value at x, y, z.
     */
    public static float noise(float x, float y, float z) {
        if (perlin == null) {
            if (perlinRandom == null) {
                perlinRandom = new Random();
            }
            perlin = new float[RainbowMath.PERLIN_SIZE + 1];
            for (int i = 0; i < (RainbowMath.PERLIN_SIZE + 1); i++) {
                perlin[i] = perlinRandom.nextFloat(); // (float)Math.random();
            }
            // [toxi 031112]
            // noise broke due to recent change of cos table in PGraphics
            // this will take care of it
            perlin_cosTable = RainbowGraphics.cosLUT;
            perlin_TWOPI = perlin_PI = RainbowGraphics.SINCOS_LENGTH;
            perlin_PI >>= 1;
        }

        if (x < 0) {
            x = -x;
        }
        if (y < 0) {
            y = -y;
        }
        if (z < 0) {
            z = -z;
        }

        int xi = (int) x, yi = (int) y, zi = (int) z;
        float xf = (x - xi);
        float yf = (y - yi);
        float zf = (z - zi);
        float rxf, ryf;

        float r = 0;
        float ampl = 0.5f;

        float n1, n2, n3;

        for (int i = 0; i < perlin_octaves; i++) {
            int of = xi + (yi << RainbowMath.PERLIN_YWRAPB) + (zi << RainbowMath.PERLIN_ZWRAPB);

            rxf = noise_fsc(xf);
            ryf = noise_fsc(yf);

            n1 = perlin[of & RainbowMath.PERLIN_SIZE];
            n1 += rxf * (perlin[(of + 1) & RainbowMath.PERLIN_SIZE] - n1);
            n2 = perlin[(of + RainbowMath.PERLIN_YWRAP) & RainbowMath.PERLIN_SIZE];
            n2 += rxf * (perlin[(of + RainbowMath.PERLIN_YWRAP + 1) & RainbowMath.PERLIN_SIZE] - n2);
            n1 += ryf * (n2 - n1);

            of += RainbowMath.PERLIN_ZWRAP;
            n2 = perlin[of & RainbowMath.PERLIN_SIZE];
            n2 += rxf * (perlin[(of + 1) & RainbowMath.PERLIN_SIZE] - n2);
            n3 = perlin[(of + RainbowMath.PERLIN_YWRAP) & RainbowMath.PERLIN_SIZE];
            n3 += rxf * (perlin[(of + RainbowMath.PERLIN_YWRAP + 1) & RainbowMath.PERLIN_SIZE] - n3);
            n2 += ryf * (n3 - n2);

            n1 += noise_fsc(zf) * (n2 - n1);

            r += n1 * ampl;
            ampl *= perlin_amp_falloff;
            xi <<= 1;
            xf *= 2;
            yi <<= 1;
            yf *= 2;
            zi <<= 1;
            zf *= 2;

            if (xf >= 1.0f) {
                xi++;
                xf--;
            }
            if (yf >= 1.0f) {
                yi++;
                yf--;
            }
            if (zf >= 1.0f) {
                zi++;
                zf--;
            }
        }
        return r;
    }

    // [toxi 031112]
    // now adjusts to the size of the cosLUT used via
    // the new variables, defined above
    private static float noise_fsc(final float i) {
        // using bagel's cosine table instead
        return 0.5f * (1.0f - perlin_cosTable[(int) (i * perlin_PI) % perlin_TWOPI]);
    }

    /**
     * Computes the Perlin noise function value at the point x, y.
     */
    public static float noise(final float x, final float y) {
        return noise(x, y, 0f);
    }

    public static void noiseSeed(final long what) {
        if (perlinRandom == null) {
            perlinRandom = new Random();
        }
        perlinRandom.setSeed(what);
        // force table reset after changing the random number seed [0122]
        perlin = null;
    }

    public void noiseDetail(final int lod) {
        if (lod > 0) {
            perlin_octaves = lod;
        }
    }

    // ////////////////////////////////////////////////////////////

    // INT NUMBER FORMATTING

    public void noiseDetail(final int lod, final float falloff) {
        if (lod > 0) {
            perlin_octaves = lod;
        }
        if (falloff > 0) {
            perlin_amp_falloff = falloff;
        }
    }
}
