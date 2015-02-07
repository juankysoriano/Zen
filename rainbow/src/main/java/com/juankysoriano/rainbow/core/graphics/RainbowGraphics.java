/* -*- mode: java; c-basic-offset: 2; indent-tabs-mode: nil -*- */

/*
 Part of the Processing project - http://processing.org

 Copyright (c) 2004-10 Ben Fry and Casey Reas
 Copyright (c) 2001-04 Massachusetts Institute of Technology

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License version 2.1 as published by the Free Software Foundation.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General
 Public License along with this library; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 Boston, MA  02111-1307  USA
 */

package com.juankysoriano.rainbow.core.graphics;

import android.graphics.Color;

import com.juankysoriano.rainbow.core.Rainbow;
import com.juankysoriano.rainbow.core.extra.RainbowStyle;
import com.juankysoriano.rainbow.core.matrix.RMatrix;
import com.juankysoriano.rainbow.core.matrix.RMatrix2D;
import com.juankysoriano.rainbow.core.matrix.RMatrix3D;
import com.juankysoriano.rainbow.utils.RainbowMath;

import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * Main graphics and rendering context, as well as the base API implementation.
 * <p/>
 * <h2>Subclassing and initializing PGraphics objects</h2> Starting in release
 * 0149, subclasses of PGraphics are handled differently. The constructor for
 * subclasses takes no parameters, instead a series of functions are called by
 * the hosting Imagine to specify its attributes.
 * <ul>
 * <li>setParent(Imagine) - is called to specify the parent
 * Imagine.
 * <li>setPrimary(boolean) - called with true if this PGraphics will be the
 * primary drawing surface used by the sketch, or false if not.
 * <li>setPath(String) - called when the renderer needs a filename or output
 * path, such as with the PDF or DXF renderers.
 * <li>setSize(int, int) - this is called last, at which point it's safe for the
 * renderer to complete its initialization routine.
 * </ul>
 * The functions were broken out because of the growing number of parameters
 * such as these that might be used by a renderer, yet with the exception of
 * setSize(), it's not clear which will be necessary. So while the size could be
 * passed in to the constructor instead of a setSize() function, a function
 * would still be needed that would notify the renderer that it was time to
 * finish its initialization. Thus, setSize() simply does both.
 * <p/>
 * <h2>Know your rights: public vs. private methods</h2> Methods that are
 * protected are often subclassed by other renderers, however they are not set
 * 'public' because they shouldn't be part of the user-facing public API
 * accessible from Imagine. That is, we don't want sketches calling
 * textModeCheck() or vertexTexture() directly.
 * <p/>
 * <h2>Handling warnings and exceptions</h2> Methods that are unavailable
 * generally show a warning, unless their lack of availability will soon cause
 * another exception. For instance, if a method like getMatrix() returns null
 * because it is unavailable, an exception will be thrown stating that the
 * method is unavailable, rather than waiting for the NullPointerException that
 * will occur when the sketch tries to use that method. As of release 0149,
 * warnings will only be shown once, and exceptions have been changed to
 * warnings where possible.
 * <p/>
 * <h2>Using xxxxImpl() for subclassing smoothness</h2> The xxxImpl() methods
 * are generally renderer-specific handling for some subset if tasks for a
 * particular function (vague enough for you?) For instance, imageImpl() handles
 * drawing an image whose x/y/w/h and u/v coords have been specified, and screen
 * placement (independent of imageMode) has been determined. There's no point in
 * all renderers implementing the <tt>if (imageMode == BLAH)</tt>
 * placement/sizing logic, so that's handled by PGraphics, which then calls
 * imageImpl() once all that is figured out.
 * <p/>
 * <h2>His brother PImage</h2> PGraphics subclasses PImage so that it can be
 * drawn and manipulated in a similar fashion. As such, many methods are
 * inherited from PGraphics, though many are unavailable: for instance, resize()
 * is not likely to be implemented; the same goes for mask(), depending on the
 * situation.
 * <p/>
 * <h2>What's in PGraphics, what ain't</h2> For the benefit of subclasses, as
 * much as possible has been placed inside PGraphics. For instance, bezier
 * interpolation code and implementations of the strokeCap() method (that simply
 * sets the strokeCap variable) are handled here. Features that will vary widely
 * between renderers are located inside the subclasses themselves. For instance,
 * all matrix handling code is per-renderer: Java 2D uses its own
 * AffineTransform, P2D uses a PMatrix2D, and PGraphics3D needs to keep
 * continually update forward and reverse transformations. A proper (future)
 * OpenGL implementation will have all its matrix madness handled by the card.
 * Lighting also falls under this category, however the base material property
 * settings (emissive, specular, et al.) are handled in PGraphics because they
 * use the standard colorMode() logic. Subclasses should override methods like
 * emissiveFromCalc(), which is a point where a valid color has been defined
 * internally, and can be applied in some manner based on the calcXxxx values.
 * <p/>
 * <h2>What's in the PGraphics documentation, what ain't</h2> Some things are
 * noted here, some things are not. For public API, always refer to the <a
 * href="http://processing.org/reference">reference</A> on Processing.org for
 * proper explanations. <b>No attempt has been made to keep the javadoc up to
 * date or complete.</b> It's an enormous task for which we simply do not have
 * the time. That is, it's not something that to be done once&mdash;it's a
 * matter of keeping the multiple references synchronized (to say nothing of the
 * translation issues), while targeting them for their separate audiences. Ouch.
 */

public abstract class RainbowGraphics extends RainbowImage {

    public static final int VERTEX_FIELD_COUNT = 37;
    public static final float cosLUT[];
    public static final int R = 3;
    public static final int G = 4;
    public static final int B = 5;
    public static final int A = 6;
    public static final int U = 7;
    public static final int V = 8;
    public static final int NX = 9;
    public static final int NY = 10;
    public static final int NZ = 11;
    public static final int EDGE = 12;
    public static final int SR = 13;
    public static final int SG = 14;
    public static final int SB = 15;
    public static final int SA = 16;
    public static final int SW = 17;
    public static final int HAS_NORMAL = 36;
    public static final int ROUND = 1 << 1;
    public static final int DEFAULT_STROKE_CAP = ROUND;
    public int strokeCap = DEFAULT_STROKE_CAP;
    public static final int PROJECT = 1 << 2;  // called 'square' in the svg spec
    public static final int MITER = 1 << 3;
    public static final int DEFAULT_STROKE_JOIN = MITER;
    public int strokeJoin = DEFAULT_STROKE_JOIN;
    public static final float DEFAULT_STROKE_WEIGHT = 1;
    public float strokeWeight = DEFAULT_STROKE_WEIGHT;
    public static final float[] sinLUT;
    public static final float SINCOS_PRECISION = 0.5f;
    public static final int SINCOS_LENGTH = (int) (360f / SINCOS_PRECISION);
    public static final int X = 0;  // model coords xyz (formerly MX/MY/MZ)
    public static final int Y = 1;
    public static final int Z = 2;
    /**
     * Draw mode convention to use (x, y) to (width, height)
     */
    public static final int CORNER = 0;
    /**
     * The current image alignment (read-only)
     */
    private int imageMode = CORNER;
    /**
     * Draw mode convention to use (x1, y1) to (x2, y2) coordinates
     */
    public static final int CORNERS = 1;
    /**
     * Draw mode from the center, and using the radius
     */
    public static final int RADIUS = 2;
    /**
     * Draw from the center, using second pair of values as the diameter.
     * Formerly called CENTER_DIAMETER in alpha releases.
     */
    public static final int CENTER = 3;

    // arc drawing modes
    /**
     * Synonym for the CENTER constant. Draw from the center,
     * using second pair of values as the diameter.
     */
    public static final int DIAMETER = 3;
    public static final int CHORD = 2;

    // vertically alignment modes for text
    public static final int PIE = 3;

    // error messages
    /**
     * texture coordinates based on image width/height
     */
    public static final int IMAGE = 2;
    /**
     * Sets whether texture coordinates passed to vertex() calls will be based
     * on coordinates that are based on the IMAGE or NORMALIZED.
     */
    private int textureMode = IMAGE;
    public static final int RGB = 1;  // image & color
    public static final int ARGB = 2;  // image
    public static final int HSB = 3;  // color
    public static final int ALPHA = 4;  // image
    public static final int CLEAR = -200;

    public static final int POINTS = 3;   // vertices

    public static final int LINES = 5;   // beginShape(), createShape()
    public static final int TRIANGLES = 9;   // vertices
    public static final int TRIANGLE_STRIP = 10;  // vertices
    public static final int TRIANGLE_FAN = 11;  // vertices
    public static final int QUAD = 16;  // primitive
    public static final int QUADS = 17;  // vertices
    public static final int QUAD_STRIP = 18;  // vertices
    public static final int POLYGON = 20;  // in the end, probably cannot

    // shape closing modes

    public static final int OPEN = 1;
    public static final int CLOSE = 2;

    static {
        sinLUT = new float[SINCOS_LENGTH];
        cosLUT = new float[SINCOS_LENGTH];
        for (int i = 0; i < SINCOS_LENGTH; i++) {
            sinLUT[i] = (float) Math.sin(i * RainbowMath.DEG_TO_RAD * SINCOS_PRECISION);
            cosLUT[i] = (float) Math.cos(i * RainbowMath.DEG_TO_RAD * SINCOS_PRECISION);
        }
    }

    private static final String ERROR_BACKGROUND_IMAGE_SIZE =
            "background image must be the same size as your application";
    private static final String ERROR_BACKGROUND_IMAGE_FORMAT =
            "background images should be RGB or ARGB";
    private static final int NORMAL_MODE_AUTO = 0;
    private static final int NORMAL_MODE_SHAPE = 1;
    private static final int NORMAL_MODE_VERTEX = 2;
    private static final int STYLE_STACK_DEPTH = 64;
    private RainbowStyle[] styleStack = new RainbowStyle[STYLE_STACK_DEPTH];
    private static final int DEFAULT_VERTICES = 512;
    protected float vertices[][] = new float[DEFAULT_VERTICES][VERTEX_FIELD_COUNT];
    private static HashMap<String, Object> warnings;
    private static float[] lerpColorHSB1;
    private static float[] lerpColorHSB2;
    private static float[] lerpColorHSB3;
    private final WeakHashMap<RainbowImage, Object> cacheMap = new WeakHashMap<RainbowImage, Object>();
    private final RMatrix3D bezierBasisMatrix = new RMatrix3D(-1, 3, -3, 1, 3, -6, 3, 0, -3, 3, 0, 0, 1, 0, 0, 0);
    private final float[] cacheHsbValue = new float[3];
    public int pixelCount;
    public boolean smooth = false;
    /**
     * True if tint() is enabled (read-only).
     * <p/>
     * Using tint/tintColor seems a better option for naming than
     * tintEnabled/tint because the latter seems ugly, even though g.tint as the
     * actual color seems a little more intuitive, it's just that g.tintEnabled
     * is even more unintuitive. Same goes for fill and stroke, et al.
     */
    public boolean tint;
    /**
     * tint that was last set (read-only)
     */
    public int tintColor;
    /**
     * true if fill() is enabled, (read-only)
     */
    public boolean fill;

    // ........................................................

    // Additional stroke properties
    /**
     * fill that was last set (read-only)
     */
    public int fillColor = 0xffFFFFFF;
    /**
     * true if stroke() is enabled, (read-only)
     */
    public boolean stroke;
    /**
     * stroke that was last set (read-only)
     */
    public int strokeColor = 0xff000000;
    /**
     * Last background color that was set, zero if an image
     */
    public int backgroundColor = 0xffCCCCCC;
    protected int quality;
    /**
     * true if this is the main drawing surface for a particular sketch. This
     * would be set to false for an offscreen buffer or if it were created any
     * other way than size(). When this is set, the listeners are also added to
     * the sketch.
     */
    protected boolean primarySurface;
    /**
     * Type of shape passed to beginShape(), zero if no shape is currently being
     * drawn.
     */
    protected int shape;
    protected int vertexCount; // total number of vertices
    protected RMatrix3D curveToBezierMatrix;
    protected int curveVertexCount;
    /**
     * The current colorMode
     */
    private int colorMode;
    /**
     * Max value for red (or hue) set by colorMode
     */
    private float colorModeX;
    /**
     * Max value for green (or saturation) set by colorMode
     */
    private float colorModeY;
    /**
     * Max value for blue (or value) set by colorMode
     */
    private float colorModeZ;
    /**
     * Max value for alpha set by colorMode
     */
    private float colorModeA;
    /**
     * The current rect mode (read-only)
     */
    private int rectMode;
    /**
     * The current ellipse mode (read-only)
     */
    private int ellipseMode;
    /**
     * The current shape alignment mode (read-only)
     */
    private int shapeMode;
    private float ambientR;
    private float ambientG;
    private float ambientB;
    private float specularR;
    private float specularG;
    private float specularB;
    private float emissiveR;
    private float emissiveG;
    private float emissiveB;
    private float shininess;
    private int bezierDetail = 20;
    private int curveDetail = 20;
    private float curveTightness = 0;

    /**
     * Array of hint[] items. These are hacks to get around various temporary
     * workarounds inside the environment.
     * <p/>
     * Note that this array cannot be static, as a hint() may result in a
     * runtime change specific to a renderer. For instance, calling
     * hint(DISABLE_DEPTH_TEST) has to call glDisable() right away on an
     * instance of PGraphicsOpenGL.
     * <p/>
     * The hints[] array is allocated early on because it might be used inside
     * beginDraw(), allocate(), etc.
     */
    private boolean edge = true;
    /**
     * Current normal vector.
     */
    private float normalX;
    private float normalY;
    private float normalZ;
    /**
     * Current horizontal coordinate for texture, will always be between 0 and
     * 1, even if using textureMode(IMAGE).
     */
    private float textureU;
    /**
     * Current vertical coordinate for texture, see above.
     */
    private float textureV;
    /**
     * Current image being used as a texture
     */
    private RainbowImage textureImage = null;
    // ........................................................
    // / Number of U steps (aka "theta") around longitudinally spanning 2*pi
    private int sphereDetailU = 0;
    // ........................................................
    // / Number of V steps (aka "phi") along latitudinally top-to-bottom
    // spanning pi
    private int sphereDetailV = 0;
    // / true if defaults() has been called a first time
    private boolean settingsInited;
    // / set to a PGraphics object being used inside a beginRaw/endRaw() block
    private RainbowGraphics raw;

    private float tintR;
    private float tintG;
    private float tintB;
    private float tintA;

    private float fillR;
    private float fillG;
    private float fillB;
    private float fillA;
    private float strokeR;
    private float strokeG;

    private float strokeB;
    private float strokeA;

    private float calcR;

    /**
     * The current font if a Java version of it is installed
     */
    private float calcG;
    private float calcB;
    private float calcA;
    private int calcRi;
    private int calcGi;
    private int calcBi;
    private int calcAi;
    private int calcColor;
    private boolean calcAlpha;

    /**
     * Metrics for the current native Java font
     */

    private boolean bezierInited = false;
    private RMatrix3D bezierDrawMatrix;
    private boolean curveInited = false;
    private RMatrix3D curveBasisMatrix;
    private RMatrix3D curveDrawMatrix;
    private RMatrix3D bezierBasisInverse;
    private float[][] curveVertices;
    private int normalMode;
    private boolean autoNormal;
    private float[] sphereX;
    private float[] sphereY;
    private float[] sphereZ;

    /**
     * True if colors are not in the range 0..1
     */
    private boolean colorModeScale;
    /**
     * True if colorMode(RGB, 255)
     */
    private boolean colorModeDefault;
    private int styleStackDepth;
    /**
     * The last RGB value converted to HSB
     */
    private int cacheHsbKey;

    /**
     * Constructor for the PGraphics object. Use this to ensure that the
     * defaults get set properly. In a subclass, use this(w, h) as the first
     * line of a subclass' constructor to properly set the internal fields and
     * defaults.
     */
    public RainbowGraphics() {
    }

    /**
     * Display a warning that the specified method is only available with 3D.
     *
     * @param method The method name (no parentheses)
     */
    public static void showDepthWarning(String method) {
        showWarning(method + "() can only be used with a renderer that " + "supports 3D, such as P3D or OPENGL.");
    }

    /**
     * Show a renderer error, and keep track of it so that it's only shown once.
     *
     * @param msg the error message (which will be stored for later comparison)
     */
    private static void showWarning(String msg) { // ignore
        if (warnings == null) {
            warnings = new HashMap<String, Object>();
        }
        if (!warnings.containsKey(msg)) {
            System.err.println(msg);
            warnings.put(msg, new Object());
        }
    }

    /**
     * Display a warning that the specified method that takes x, y, z parameters
     * can only be used with x and y parameters in this renderer.
     *
     * @param method The method name (no parentheses)
     */
    public static void showDepthWarningXYZ(String method) {
        showWarning(method + "() with x, y, and z coordinates " + "can only be used with a renderer that " + "supports 3D, such as P3D or OPENGL. " + "Use a version without a z-coordinate instead.");
    }

    /**
     * Error that a particular variation of a method is unavailable (even though
     * other variations are). For instance, if vertex(x, y, u, v) is not
     * available, but vertex(x, y) is just fine.
     */
    public static void showVariationWarning(String str) {
        showWarning(str + " is not available with this renderer.");
    }

    /**
     * Show an renderer-related exception that halts the program. Currently just
     * wraps the message as a RuntimeException and throws it, but might do
     * something more specific might be used in the future.
     */
    public static void showException(String msg) { // ignore
        throw new RuntimeException(msg);
    }

    public void setParent(Rainbow parent) { // ignore
        this.parent = parent;
    }

    /**
     * Set (or unset) this as the main drawing surface. Meaning that it can
     * safely be set to opaque (and given a default gray background), or
     * anything else that goes along with that.
     */
    public void setPrimary(boolean primary) {
        this.primarySurface = primary;
        if (primarySurface) {
            format = RGB;
        }
    }

    /**
     * The final step in setting up a renderer, set its size of this renderer.
     * This was formerly handled by the constructor, but instead it's been
     * broken out so that setParent/setPrimary/setPath can be handled
     * differently.
     * <p/>
     * Important that this is ignored by preproc.pl because otherwise it will
     * override setSize() in Imagine/Applet/Component, which will 1) not
     * call super.setSize(), and 2) will cause the renderer to be resized from
     * the event thread (EDT), causing a nasty crash as it collides with the
     * animation thread.
     */
    public void setSize(int w, int h) { // ignore
        width = w;
        height = h;

        allocate();
        reapplySettings();
    }

    /**
     * Allocate memory for this renderer. Generally will need to be implemented
     * for all renderers.
     */
    protected void allocate() {
    }

    /**
     * Store data of some kind for the renderer that requires extra metadata of
     * some kind. Usually this is a renderer-specific representation of the
     * image data, for instance a BufferedImage with tint() settings applied for
     * PGraphicsJava2D, or resized image data and OpenGL texture indices for
     * PGraphicsOpenGL.
     *
     * @param image   The PGraphics renderer associated to the image
     * @param storage The metadata required by the renderer
     */
    public void setCache(RainbowImage image, Object storage) {
        cacheMap.put(image, storage);
    }

    /**
     * Get cache storage data for the specified renderer. Because each renderer
     * will cache data in different formats, it's necessary to store cache data
     * keyed by the renderer object. Otherwise, attempting to draw the same
     * image to both a PGraphicsJava2D and a PGraphicsOpenGL will cause errors.
     *
     * @param image The PGraphics renderer associated to the image
     * @return metadata stored for the specified renderer
     */
    public Object getCache(RainbowImage image) {
        return cacheMap.get(image);
    }

    /**
     * Remove information associated with this renderer from the cache, if any.
     *
     * @param image The PGraphics renderer whose cache data should be removed
     */
    public void removeCache(RainbowImage image) {
        cacheMap.remove(image);
    }

    protected void checkSettings() {
        if (!settingsInited) {
            defaultSettings();
        }
    }

    /**
     * Set engine's default values. This has to be called by Imagine,
     * somewhere inside setup() or draw() because it talks to the graphics
     * buffer, meaning that for subclasses like OpenGL, there needs to be a
     * valid graphics context to mess with otherwise you'll get some good
     * crashing action.
     * <p/>
     * This is currently called by checkSettings(), during beginDraw().
     */
    void defaultSettings() { // ignore
        smooth(); // 2.0a5

        colorMode(RGB, 255);
        fill(255);
        stroke(0);

        strokeWeight(DEFAULT_STROKE_WEIGHT);
        strokeJoin(DEFAULT_STROKE_JOIN);
        strokeCap(DEFAULT_STROKE_CAP);

        shape = 0;

        rectMode(CORNER);
        ellipseMode(DIAMETER);

        autoNormal = true;

        settingsInited = true;
    }

    /**
     * Re-apply current settings. Some methods, such as textFont(), require that
     * their methods be called (rather than simply setting the textFont
     * variable) because they affect the graphics context, or they require
     * parameters from the context (e.g. getting native fonts for text).
     * <p/>
     * This will only be called from an allocate(), which is only called from
     * size(), which is safely called from inside beginDraw(). And it cannot be
     * called before defaultSettings(), so we should be safe.
     */
    protected void reapplySettings() {
        if (!settingsInited) {
            return;
        }

        colorMode(colorMode, colorModeX, colorModeY, colorModeZ);
        if (fill) {
            fill(fillColor);
        } else {
            noFill();
        }
        if (stroke) {
            stroke(strokeColor);
            strokeWeight(strokeWeight);
            strokeCap(strokeCap);
            strokeJoin(strokeJoin);
        } else {
            noStroke();
        }
        if (tint) {
            tint(tintColor);
        } else {
            noTint();
        }
        if (smooth) {
            smooth();
        } else {
            noSmooth();
        }

        background(backgroundColor);
    }

    /**
     * Set texture mode to either to use coordinates based on the IMAGE (more
     * intuitive for new users) or NORMALIZED (better for advanced chaps)
     */
    public void textureMode(int mode) {
        this.textureMode = mode;
    }

    public void textureWrap(int wrap) {
        showMissingWarning("textureWrap");
    }

    /**
     * Display a warning that the specified method is not implemented, meaning
     * that it could be either a completely missing function, although other
     * variations of it may still work properly.
     */
    public static void showMissingWarning(String method) {
        showWarning(method + "(), or this particular variation of it, " + "is not available with this renderer.");
    }

    /**
     * Removes texture image for current shape. Needs to be called between @see
     * beginShape and @see endShape
     */
    public void noTexture() {
        textureImage = null;
    }

    /**
     * Used by renderer subclasses or PShape to efficiently pass in already
     * formatted vertex information.
     *
     * @param v vertex parameters, as a float array of length
     *          VERTEX_FIELD_COUNT
     */
    public void vertex(float[] v) {
        vertexCheck();
        curveVertexCount = 0;
        float[] vertex = vertices[vertexCount];
        System.arraycopy(v, 0, vertex, 0, VERTEX_FIELD_COUNT);
        vertexCount++;
    }

    void vertexCheck() {
        if (vertexCount == vertices.length) {
            float temp[][] = new float[vertexCount << 1][VERTEX_FIELD_COUNT];
            System.arraycopy(vertices, 0, temp, 0, vertexCount);
            vertices = temp;
        }
    }

    public void vertex(float x, float y, float z, float u, float v) {
        vertexTexture(u, v);
        vertex(x, y, z);
    }

    /**
     * Set (U, V) coords for the next vertex in the current shape. This is ugly
     * as its own function, and will (almost?) always be coincident with a call
     * to vertex. As of beta, this was moved to the protected method you see
     * here, and called from an optional param of and overloaded vertex().
     * <p/>
     * The parameters depend on the current textureMode. When using
     * textureMode(IMAGE), the coordinates will be relative to the size of the
     * image texture, when used with textureMode(NORMAL), they'll be in the
     * range 0..1.
     * <p/>
     * Used by both PGraphics2D (for images) and PGraphics3D.
     */
    void vertexTexture(float u, float v) {
        if (textureImage == null) {
            throw new RuntimeException("You must first call texture() before " + "using u and v coordinates with vertex()");
        }
        if (textureMode == IMAGE) {
            u /= (float) textureImage.width;
            v /= (float) textureImage.height;
        }

        textureU = u;
        textureV = v;
    }

    public void vertex(float x, float y, float z) {
        vertexCheck();
        float[] vertex = vertices[vertexCount];
        if (shape == POLYGON) {
            if (vertexCount > 0) {
                float pvertex[] = vertices[vertexCount - 1];
                if ((Math.abs(pvertex[X] - x) < RainbowMath.EPSILON) && (Math.abs(pvertex[Y] - y) < RainbowMath.EPSILON) && (Math.abs(pvertex[Z] - z) < RainbowMath.EPSILON)) {
                    return;
                }
            }
        }

        curveVertexCount = 0;

        vertex[X] = x;
        vertex[Y] = y;
        vertex[Z] = z;

        vertex[EDGE] = edge ? 1 : 0;

        boolean textured = textureImage != null;
        if (fill || textured) {
            if (!textured) {
                vertex[R] = fillR;
                vertex[G] = fillG;
                vertex[B] = fillB;
                vertex[A] = fillA;
            } else {
                if (tint) {
                    vertex[R] = tintR;
                    vertex[G] = tintG;
                    vertex[B] = tintB;
                    vertex[A] = tintA;
                } else {
                    vertex[R] = 1;
                    vertex[G] = 1;
                    vertex[B] = 1;
                    vertex[A] = 1;
                }
            }
        }

        if (stroke) {
            vertex[SR] = strokeR;
            vertex[SG] = strokeG;
            vertex[SB] = strokeB;
            vertex[SA] = strokeA;
            vertex[SW] = strokeWeight;
        }

        vertex[U] = textureU;
        vertex[V] = textureV;

        if (autoNormal) {
            float norm2 = normalX * normalX + normalY * normalY + normalZ * normalZ;
            if (norm2 < RainbowMath.EPSILON) {
                vertex[HAS_NORMAL] = 0;
            } else {
                if (Math.abs(norm2 - 1) > RainbowMath.EPSILON) {
                    float norm = RainbowMath.sqrt(norm2);
                    normalX /= norm;
                    normalY /= norm;
                    normalZ /= norm;
                }
                vertex[HAS_NORMAL] = 1;
            }
        } else {
            vertex[HAS_NORMAL] = 1;
        }

        vertex[NX] = normalX;
        vertex[NY] = normalY;
        vertex[NZ] = normalZ;

        vertexCount++;
    }

    /**
     * This feature is in testing, do not use or rely upon its implementation
     */
    public void breakShape() {
        showWarning("This renderer cannot currently handle concave shapes, " + "or shapes with holes.");
    }

    public void beginContour() {
        showMissingWarning("beginContour");
    }

    public void endContour() {
        showMissingWarning("endContour");
    }

    public void clip(float a, float b, float c, float d) {
        if (imageMode == CORNER) {
            if (c < 0) { // reset a negative width
                a += c;
                c = -c;
            }
            if (d < 0) { // reset a negative height
                b += d;
                d = -d;
            }

            clipImpl(a, b, a + c, b + d);

        } else if (imageMode == CORNERS) {
            if (c < a) { // reverse because x2 < x1
                float temp = a;
                a = c;
                c = temp;
            }
            if (d < b) { // reverse because y2 < y1
                float temp = b;
                b = d;
                d = temp;
            }

            clipImpl(a, b, c, d);

        } else if (imageMode == CENTER) {
            // c and d are width/height
            if (c < 0) {
                c = -c;
            }
            if (d < 0) {
                d = -d;
            }
            float x1 = a - c / 2;
            float y1 = b - d / 2;

            clipImpl(x1, y1, x1 + c, y1 + d);
        }
    }

    void clipImpl(float x1, float y1, float x2, float y2) {
        showMissingWarning("clip");
    }

    public void noClip() {
        showMissingWarning("noClip");
    }

    public void blendMode(int mode) {
        showMissingWarning("blendMode");
    }

    public void quadraticVertex(float cx, float cy, float cz, float x3, float y3, float z3) {
        bezierVertexCheck();
        float[] prev = vertices[vertexCount - 1];
        float x1 = prev[X];
        float y1 = prev[Y];
        float z1 = prev[Z];

        bezierVertex(
                x1 + ((cx - x1) * 2 / 3.0f),
                y1 + ((cy - y1) * 2 / 3.0f),
                z1 + ((cz - z1) * 2 / 3.0f),
                x3 + ((cx - x3) * 2 / 3.0f),
                y3 + ((cy - y3) * 2 / 3.0f),
                z3 + ((cz - z3) * 2 / 3.0f),
                x3,
                y3,
                z3);
    }

    protected void bezierVertexCheck() {
        bezierVertexCheck(shape, vertexCount);
    }

    public void bezierVertex(float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        bezierInitCheck();
        bezierVertexCheck();
        RMatrix3D draw = bezierDrawMatrix;

        float[] prev = vertices[vertexCount - 1];
        float x1 = prev[X];
        float y1 = prev[Y];
        float z1 = prev[Z];

        float xplot1 = draw.m10 * x1 + draw.m11 * x2 + draw.m12 * x3 + draw.m13 * x4;
        float xplot2 = draw.m20 * x1 + draw.m21 * x2 + draw.m22 * x3 + draw.m23 * x4;
        float xplot3 = draw.m30 * x1 + draw.m31 * x2 + draw.m32 * x3 + draw.m33 * x4;

        float yplot1 = draw.m10 * y1 + draw.m11 * y2 + draw.m12 * y3 + draw.m13 * y4;
        float yplot2 = draw.m20 * y1 + draw.m21 * y2 + draw.m22 * y3 + draw.m23 * y4;
        float yplot3 = draw.m30 * y1 + draw.m31 * y2 + draw.m32 * y3 + draw.m33 * y4;

        float zplot1 = draw.m10 * z1 + draw.m11 * z2 + draw.m12 * z3 + draw.m13 * z4;
        float zplot2 = draw.m20 * z1 + draw.m21 * z2 + draw.m22 * z3 + draw.m23 * z4;
        float zplot3 = draw.m30 * z1 + draw.m31 * z2 + draw.m32 * z3 + draw.m33 * z4;

        for (int j = 0; j < bezierDetail; j++) {
            x1 += xplot1;
            xplot1 += xplot2;
            xplot2 += xplot3;
            y1 += yplot1;
            yplot1 += yplot2;
            yplot2 += yplot3;
            z1 += zplot1;
            zplot1 += zplot2;
            zplot2 += zplot3;
            vertex(x1, y1, z1);
        }
    }

    void bezierVertexCheck(int shape, int vertexCount) {
        if (shape == 0 || shape != POLYGON) {
            throw new RuntimeException("beginShape() or beginShape(POLYGON) " + "must be used before bezierVertex() or quadraticVertex()");
        }
        if (vertexCount == 0) {
            throw new RuntimeException("vertex() must be used at least once" + "before bezierVertex() or quadraticVertex()");
        }
    }

    void bezierInitCheck() {
        if (!bezierInited) {
            bezierInit();
        }
    }

    void bezierInit() {
        bezierDetail(bezierDetail);
        bezierInited = true;
    }

    public void bezierDetail(int detail) {
        bezierDetail = detail;

        if (bezierDrawMatrix == null) {
            bezierDrawMatrix = new RMatrix3D();
        }

        splineForward(detail, bezierDrawMatrix);
        bezierDrawMatrix.apply(bezierBasisMatrix);
    }

    /**
     * Setup forward-differencing matrix to be used for speedy curve rendering.
     * It's based on using a specific number of curve segments and just doing
     * incremental adds for each vertex of the segment, rather than running the
     * mathematically expensive cubic equation.
     *
     * @param segments number of curve segments to use when drawing
     * @param matrix   target object for the new matrix
     */
    void splineForward(int segments, RMatrix3D matrix) {
        float f = 1.0f / segments;
        float ff = f * f;
        float fff = ff * f;

        matrix.set(0, 0, 0, 1, fff, ff, f, 0, 6 * fff, 2 * ff, 0, 0, 6 * fff, 0, 0, 0);
    }

    public void point(float x, float y) {
        beginShape(POINTS);
        vertex(x, y);
        endShape();
    }

    /**
     * Start a new shape.
     * <p/>
     * <B>Differences between beginShape() and line() and point() methods.</B>
     * <p/>
     * beginShape() is intended to be more flexible at the expense of being a
     * little more complicated to use. it handles more complicated shapes that
     * can consist of many connected lines (so you get joins) or lines mixed
     * with curves.
     * <p/>
     * The line() and point() command are for the far more common cases
     * (particularly for our audience) that simply need to draw a line or a
     * point on the screen.
     * <p/>
     * From the code side of things, line() may or may not call beginShape() to
     * do the drawing. In the beta code, they do, but in the alpha code, they
     * did not. they might be implemented one way or the other depending on
     * tradeoffs of runtime efficiency vs. implementation efficiency &mdash
     * meaning the speed that things run at vs. the speed it takes me to write
     * the code and maintain it. for beta, the latter is most important so
     * that's how things are implemented.
     */
    public void beginShape(int kind) {
        shape = kind;
    }

    public void vertex(float x, float y) {
        vertexCheck();
        float[] vertex = vertices[vertexCount];

        curveVertexCount = 0;

        vertex[X] = x;
        vertex[Y] = y;
        vertex[Z] = 0;

        vertex[EDGE] = edge ? 1 : 0;

        boolean textured = textureImage != null;
        if (fill || textured) {
            if (!textured) {
                vertex[R] = fillR;
                vertex[G] = fillG;
                vertex[B] = fillB;
                vertex[A] = fillA;
            } else {
                if (tint) {
                    vertex[R] = tintR;
                    vertex[G] = tintG;
                    vertex[B] = tintB;
                    vertex[A] = tintA;
                } else {
                    vertex[R] = 1;
                    vertex[G] = 1;
                    vertex[B] = 1;
                    vertex[A] = 1;
                }
            }
        }

        if (stroke) {
            vertex[SR] = strokeR;
            vertex[SG] = strokeG;
            vertex[SB] = strokeB;
            vertex[SA] = strokeA;
            vertex[SW] = strokeWeight;
        }

        vertex[U] = textureU;
        vertex[V] = textureV;

        if (autoNormal) {
            float norm2 = normalX * normalX + normalY * normalY + normalZ * normalZ;
            if (norm2 < RainbowMath.EPSILON) {
                vertex[HAS_NORMAL] = 0;
            } else {
                if (Math.abs(norm2 - 1) > RainbowMath.EPSILON) {
                    // The normal vector is not normalized.
                    float norm = RainbowMath.sqrt(norm2);
                    normalX /= norm;
                    normalY /= norm;
                    normalZ /= norm;
                }
                vertex[HAS_NORMAL] = 1;
            }
        } else {
            vertex[HAS_NORMAL] = 1;
        }

        vertex[NX] = normalX;
        vertex[NY] = normalY;
        vertex[NZ] = normalZ;

        vertexCount++;
    }

    public void endShape() {
        endShape(OPEN);
    }

    public void endShape(int mode) {
    }

    public void point(float x, float y, float z) {
        beginShape(POINTS);
        vertex(x, y, z);
        endShape();
    }

    public void line(float x1, float y1, float x2, float y2) {
        beginShape(LINES);
        vertex(x1, y1);
        vertex(x2, y2);
        endShape();
    }

    public void line(float x1, float y1, float z1, float x2, float y2, float z2) {
        beginShape(LINES);
        vertex(x1, y1, z1);
        vertex(x2, y2, z2);
        endShape();
    }

    public void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        beginShape(TRIANGLES);
        vertex(x1, y1);
        vertex(x2, y2);
        vertex(x3, y3);
        endShape();
    }

    public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        beginShape(QUADS);
        vertex(x1, y1);
        vertex(x2, y2);
        vertex(x3, y3);
        vertex(x4, y4);
        endShape();
    }

    public void rectMode(int mode) {
        rectMode = mode;
    }

    public void rect(float a, float b, float c, float d) {
        float hradius, vradius;
        switch (rectMode) {
            case CORNERS:
                break;
            case CORNER:
                c += a;
                d += b;
                break;
            case RADIUS:
                hradius = c;
                vradius = d;
                c = a + hradius;
                d = b + vradius;
                a -= hradius;
                b -= vradius;
                break;
            case CENTER:
                hradius = c / 2.0f;
                vradius = d / 2.0f;
                c = a + hradius;
                d = b + vradius;
                a -= hradius;
                b -= vradius;
        }

        if (a > c) {
            float temp = a;
            a = c;
            c = temp;
        }

        if (b > d) {
            float temp = b;
            b = d;
            d = temp;
        }

        rectImpl(a, b, c, d);
    }

    protected void rectImpl(float x1, float y1, float x2, float y2) {
        quad(x1, y1, x2, y1, x2, y2, x1, y2);
    }

    public void rect(float a, float b, float c, float d, float r) {
        rect(a, b, c, d, r, r, r, r);
    }

    public void rect(float a, float b, float c, float d, float tl, float tr, float br, float bl) {
        float hradius, vradius;
        switch (rectMode) {
            case CORNERS:
                break;
            case CORNER:
                c += a;
                d += b;
                break;
            case RADIUS:
                hradius = c;
                vradius = d;
                c = a + hradius;
                d = b + vradius;
                a -= hradius;
                b -= vradius;
                break;
            case CENTER:
                hradius = c / 2.0f;
                vradius = d / 2.0f;
                c = a + hradius;
                d = b + vradius;
                a -= hradius;
                b -= vradius;
        }

        if (a > c) {
            float temp = a;
            a = c;
            c = temp;
        }

        if (b > d) {
            float temp = b;
            b = d;
            d = temp;
        }

        float maxRounding = RainbowMath.min((c - a) / 2, (d - b) / 2);
        if (tl > maxRounding) {
            tl = maxRounding;
        }
        if (tr > maxRounding) {
            tr = maxRounding;
        }
        if (br > maxRounding) {
            br = maxRounding;
        }
        if (bl > maxRounding) {
            bl = maxRounding;
        }

        rectImpl(a, b, c, d, tl, tr, br, bl);
    }

    void rectImpl(float x1, float y1, float x2, float y2, float tl, float tr, float br, float bl) {
        beginShape();
        // vertex(x1+tl, y1);
        if (tr != 0) {
            vertex(x2 - tr, y1);
            quadraticVertex(x2, y1, x2, y1 + tr);
        } else {
            vertex(x2, y1);
        }
        if (br != 0) {
            vertex(x2, y2 - br);
            quadraticVertex(x2, y2, x2 - br, y2);
        } else {
            vertex(x2, y2);
        }
        if (bl != 0) {
            vertex(x1 + bl, y2);
            quadraticVertex(x1, y2, x1, y2 - bl);
        } else {
            vertex(x1, y2);
        }
        if (tl != 0) {
            vertex(x1, y1 + tl);
            quadraticVertex(x1, y1, x1 + tl, y1);
        } else {
            vertex(x1, y1);
        }
        // endShape();
        endShape(CLOSE);
    }

    /**
     * Start a new shape of type POLYGON
     */
    public void beginShape() {
        beginShape(POLYGON);
    }

    public void quadraticVertex(float cx, float cy, float x3, float y3) {
        bezierVertexCheck();
        float[] prev = vertices[vertexCount - 1];
        float x1 = prev[X];
        float y1 = prev[Y];

        bezierVertex(x1 + ((cx - x1) * 2 / 3.0f), y1 + ((cy - y1) * 2 / 3.0f), x3 + ((cx - x3) * 2 / 3.0f), y3 + ((cy - y3) * 2 / 3.0f), x3, y3);
    }

    // ////////////////////////////////////////////////////////////

    // ELLIPSE AND ARC

    public void bezierVertex(float x2, float y2, float x3, float y3, float x4, float y4) {
        bezierInitCheck();
        bezierVertexCheck();
        RMatrix3D draw = bezierDrawMatrix;

        float[] prev = vertices[vertexCount - 1];
        float x1 = prev[X];
        float y1 = prev[Y];

        float xplot1 = draw.m10 * x1 + draw.m11 * x2 + draw.m12 * x3 + draw.m13 * x4;
        float xplot2 = draw.m20 * x1 + draw.m21 * x2 + draw.m22 * x3 + draw.m23 * x4;
        float xplot3 = draw.m30 * x1 + draw.m31 * x2 + draw.m32 * x3 + draw.m33 * x4;

        float yplot1 = draw.m10 * y1 + draw.m11 * y2 + draw.m12 * y3 + draw.m13 * y4;
        float yplot2 = draw.m20 * y1 + draw.m21 * y2 + draw.m22 * y3 + draw.m23 * y4;
        float yplot3 = draw.m30 * y1 + draw.m31 * y2 + draw.m32 * y3 + draw.m33 * y4;

        for (int j = 0; j < bezierDetail; j++) {
            x1 += xplot1;
            xplot1 += xplot2;
            xplot2 += xplot3;
            y1 += yplot1;
            yplot1 += yplot2;
            yplot2 += yplot3;
            vertex(x1, y1);
        }
    }

    public void ellipseMode(int mode) {
        ellipseMode = mode;
    }

    public void ellipse(float a, float b, float c, float d) {
        float x = a;
        float y = b;
        float w = c;
        float h = d;

        if (ellipseMode == CORNERS) {
            w = c - a;
            h = d - b;

        } else if (ellipseMode == RADIUS) {
            x = a - c;
            y = b - d;
            w = c * 2;
            h = d * 2;

        } else if (ellipseMode == DIAMETER) {
            x = a - c / 2f;
            y = b - d / 2f;
        }

        if (w < 0) { // undo negative width
            x += w;
            w = -w;
        }

        if (h < 0) { // undo negative height
            y += h;
            h = -h;
        }

        ellipseImpl(x, y, w, h);
    }

    protected void ellipseImpl(float x, float y, float w, float h) {
    }

    /**
     * Identical parameters and placement to ellipse, but draws only an arc of
     * that ellipse.
     * <p/>
     * start and stop are always radians because angleMode() was goofy.
     * ellipseMode() sets the placement.
     * <p/>
     * also tries to be smart about start < stop.
     */
    public void arc(float a, float b, float c, float d, float start, float stop) {
        arc(a, b, c, d, start, stop, 0);
    }

    public void arc(float a, float b, float c, float d, float start, float stop, int mode) {
        float x = a;
        float y = b;
        float w = c;
        float h = d;

        if (ellipseMode == CORNERS) {
            w = c - a;
            h = d - b;

        } else if (ellipseMode == RADIUS) {
            x = a - c;
            y = b - d;
            w = c * 2;
            h = d * 2;

        } else if (ellipseMode == CENTER) {
            x = a - c / 2f;
            y = b - d / 2f;
        }

        // make sure the loop will exit before starting while
        if (!Float.isInfinite(start) && !Float.isInfinite(stop)) {
            // ignore equal and degenerate cases
            if (stop > start) {
                // make sure that we're starting at a useful point
                while (start < 0) {
                    start += RainbowMath.TWO_PI;
                    stop += RainbowMath.TWO_PI;
                }

                if (stop - start > RainbowMath.TWO_PI) {
                    start = 0;
                    stop = RainbowMath.TWO_PI;
                }
                arcImpl(x, y, w, h, start, stop, mode);
            }
        }
    }

    // ////////////////////////////////////////////////////////////

    // BOX

    /**
     * Start and stop are in radians, converted by the parent function. Note
     * that the radians can be greater (or less) than TWO_PI. This is so that an
     * arc can be drawn that crosses zero mark, and the user will still collect
     * $200.
     */
    protected void arcImpl(float x, float y, float w, float h, float start, float stop, int mode) {
        showMissingWarning("arc");
    }

    public void box(float size) {
        box(size, size, size);
    }

    // ////////////////////////////////////////////////////////////

    // SPHERE

    // TODO not the least bit efficient, it even redraws lines
    // along the vertices. ugly ugly ugly!
    public void box(float w, float h, float d) {
        float x1 = -w / 2f;
        float x2 = w / 2f;
        float y1 = -h / 2f;
        float y2 = h / 2f;
        float z1 = -d / 2f;
        float z2 = d / 2f;

        beginShape(QUADS);

        // front
        normal(0, 0, 1);
        vertex(x1, y1, z1);
        vertex(x2, y1, z1);
        vertex(x2, y2, z1);
        vertex(x1, y2, z1);

        // right
        normal(1, 0, 0);
        vertex(x2, y1, z1);
        vertex(x2, y1, z2);
        vertex(x2, y2, z2);
        vertex(x2, y2, z1);

        // back
        normal(0, 0, -1);
        vertex(x2, y1, z2);
        vertex(x1, y1, z2);
        vertex(x1, y2, z2);
        vertex(x2, y2, z2);

        // left
        normal(-1, 0, 0);
        vertex(x1, y1, z2);
        vertex(x1, y1, z1);
        vertex(x1, y2, z1);
        vertex(x1, y2, z2);

        // top
        normal(0, 1, 0);
        vertex(x1, y1, z2);
        vertex(x2, y1, z2);
        vertex(x2, y1, z1);
        vertex(x1, y1, z1);

        // bottom
        normal(0, -1, 0);
        vertex(x1, y2, z1);
        vertex(x2, y2, z1);
        vertex(x2, y2, z2);
        vertex(x1, y2, z2);

        endShape();
    }

    /**
     * Sets the current normal vector. Only applies with 3D rendering and inside
     * a beginShape/endShape block.
     * <p/>
     * This is for drawing three dimensional shapes and surfaces, allowing you
     * to specify a vector perpendicular to the surface of the shape, which
     * determines how lighting affects it.
     * <p/>
     * For people familiar with OpenGL, this function is basically identical to
     * glNormal3f().
     */
    public void normal(float nx, float ny, float nz) {
        normalX = nx;
        normalY = ny;
        normalZ = nz;
        if (shape != 0) {
            if (normalMode == NORMAL_MODE_AUTO) {
                // One normal per begin/end shape
                normalMode = NORMAL_MODE_SHAPE;
            } else if (normalMode == NORMAL_MODE_SHAPE) {
                // a separate normal for each vertex
                normalMode = NORMAL_MODE_VERTEX;
            }
        }
    }

    /**
     * Draw a sphere with radius r centered at coordinate 0, 0, 0.
     * <p/>
     * Implementation notes:
     * <p/>
     * cache all the points of the sphere in a static array top and bottom are
     * just a bunch of triangles that land in the center point
     * <p/>
     * sphere is a series of concentric circles who radii vary along the shape,
     * based on, er.. cos or something
     * <p/>
     * <PRE>
     * [toxi 031031] new sphere code. removed all multiplies with
     * radius, as scale() will take care of that anyway
     * <p/>
     * [toxi 031223] updated sphere code (removed modulos)
     * and introduced sphereAt(x,y,z,r)
     * to avoid additional translate()'s on the user/sketch side
     * <p/>
     * [davbol 080801] now using separate sphereDetailU/V
     * </PRE>
     */
    public void sphere(float r) {
        if ((sphereDetailU < 3) || (sphereDetailV < 2)) {
            sphereDetail(30);
        }

        edge(false);

        // 1st ring from south pole
        beginShape(TRIANGLE_STRIP);
        for (int i = 0; i < sphereDetailU; i++) {
            normal(0, -1, 0);
            vertex(0, -r, 0);
            normal(sphereX[i], sphereY[i], sphereZ[i]);
            vertex(r * sphereX[i], r * sphereY[i], r * sphereZ[i]);
        }
        normal(0, -r, 0);
        vertex(0, -r, 0);
        normal(sphereX[0], sphereY[0], sphereZ[0]);
        vertex(r * sphereX[0], r * sphereY[0], r * sphereZ[0]);
        endShape();

        int v1, v11, v2;

        // middle rings
        int voff = 0;
        for (int i = 2; i < sphereDetailV; i++) {
            v1 = v11 = voff;
            voff += sphereDetailU;
            v2 = voff;
            beginShape(TRIANGLE_STRIP);
            for (int j = 0; j < sphereDetailU; j++) {
                normal(sphereX[v1], sphereY[v1], sphereZ[v1]);
                vertex(r * sphereX[v1], r * sphereY[v1], r * sphereZ[v1++]);
                normal(sphereX[v2], sphereY[v2], sphereZ[v2]);
                vertex(r * sphereX[v2], r * sphereY[v2], r * sphereZ[v2++]);
            }
            // close each ring
            v1 = v11;
            v2 = voff;
            normal(sphereX[v1], sphereY[v1], sphereZ[v1]);
            vertex(r * sphereX[v1], r * sphereY[v1], r * sphereZ[v1]);
            normal(sphereX[v2], sphereY[v2], sphereZ[v2]);
            vertex(r * sphereX[v2], r * sphereY[v2], r * sphereZ[v2]);
            endShape();
        }

        // add the northern cap
        beginShape(TRIANGLE_STRIP);
        for (int i = 0; i < sphereDetailU; i++) {
            v2 = voff + i;
            normal(sphereX[v2], sphereY[v2], sphereZ[v2]);
            vertex(r * sphereX[v2], r * sphereY[v2], r * sphereZ[v2]);
            normal(0, 1, 0);
            vertex(0, r, 0);
        }
        normal(sphereX[voff], sphereY[voff], sphereZ[voff]);
        vertex(r * sphereX[voff], r * sphereY[voff], r * sphereZ[voff]);
        normal(0, 1, 0);
        vertex(0, r, 0);
        endShape();

        edge(true);
    }

    // ////////////////////////////////////////////////////////////

    // BEZIER

    public void sphereDetail(int res) {
        sphereDetail(res, res);
    }

    /**
     * Sets whether the upcoming vertex is part of an edge. Equivalent to
     * glEdgeFlag(), for people familiar with OpenGL.
     */
    public void edge(boolean edge) {
        this.edge = edge;
    }

    /**
     * Set the detail level for approximating a sphere. The ures and vres params
     * control the horizontal and vertical resolution.
     * <p/>
     * Code for sphereDetail() submitted by toxi [031031]. Code for enhanced u/v
     * version from davbol [080801].
     */
    public void sphereDetail(int ures, int vres) {
        if (ures < 3) {
            ures = 3; // force a minimum res
        }
        if (vres < 2) {
            vres = 2; // force a minimum res
        }
        if ((ures == sphereDetailU) && (vres == sphereDetailV)) {
            return;
        }

        float delta = (float) SINCOS_LENGTH / ures;
        float[] cx = new float[ures];
        float[] cz = new float[ures];
        // calc unit circle in XZ plane
        for (int i = 0; i < ures; i++) {
            cx[i] = cosLUT[(int) (i * delta) % SINCOS_LENGTH];
            cz[i] = sinLUT[(int) (i * delta) % SINCOS_LENGTH];
        }
        // computing vertexlist
        // vertexlist starts at south pole
        int vertCount = ures * (vres - 1) + 2;
        int currVert = 0;

        // re-init arrays to store vertices
        sphereX = new float[vertCount];
        sphereY = new float[vertCount];
        sphereZ = new float[vertCount];

        float angle_step = (SINCOS_LENGTH * 0.5f) / vres;
        float angle = angle_step;

        // step along Y axis
        for (int i = 1; i < vres; i++) {
            float curradius = sinLUT[(int) angle % SINCOS_LENGTH];
            float currY = cosLUT[(int) angle % SINCOS_LENGTH];
            for (int j = 0; j < ures; j++) {
                sphereX[currVert] = cx[j] * curradius;
                sphereY[currVert] = currY;
                sphereZ[currVert++] = cz[j] * curradius;
            }
            angle += angle_step;
        }
        sphereDetailU = ures;
        sphereDetailV = vres;
    }

    /**
     * Evalutes quadratic bezier at point t for points a, b, c, d. t varies
     * between 0 and 1, and a and d are the on curve points, b and c are the
     * control points. this can be done once with the x coordinates and a second
     * time with the y coordinates to get the location of a bezier curve at t.
     * <p/>
     * For instance, to convert the following example:
     * <p/>
     * <PRE>
     * stroke(255, 102, 0);
     * line(85, 20, 10, 10);
     * line(90, 90, 15, 80);
     * stroke(0, 0, 0);
     * bezier(85, 20, 10, 10, 90, 90, 15, 80);
     * <p/>
     * // draw it in gray, using 10 steps instead of the default 20
     * // this is a slower way to do it, but useful if you need
     * // to do things with the coordinates at each step
     * stroke(128);
     * beginShape(LINE_STRIP);
     * for (int i = 0; i &lt;= 10; i++) {
     * float t = i / 10.0f;
     * float x = bezierPoint(85, 10, 90, 15, t);
     * float y = bezierPoint(20, 10, 90, 80, t);
     * vertex(x, y);
     * }
     * endShape();
     * </PRE>
     */
    public float bezierPoint(float a, float b, float c, float d, float t) {
        float t1 = 1.0f - t;
        return a * t1 * t1 * t1 + 3 * b * t * t1 * t1 + 3 * c * t * t * t1 + d * t * t * t;
    }

    /**
     * Provide the tangent at the given point on the bezier curve. Fix from
     * davbol for 0136.
     */
    public float bezierTangent(float a, float b, float c, float d, float t) {
        return (3 * t * t * (-a + 3 * b - 3 * c + d) + 6 * t * (a - 2 * b + c) + 3 * (-a + b));
    }

    /**
     * Draw a cubic bezier curve. The first and last points are the on-curve
     * points. The middle two are the 'control' points, or 'handles' in an
     * application like Illustrator.
     * <p/>
     * Identical to typing:
     * <p/>
     * <PRE>
     * beginShape();
     * vertex(x1, y1);
     * bezierVertex(x2, y2, x3, y3, x4, y4);
     * endShape();
     * </PRE>
     * <p/>
     * In Postscript-speak, this would be:
     * <p/>
     * <PRE>
     * moveto(x1, y1);
     * curveto(x2, y2, x3, y3, x4, y4);
     * </PRE>
     * <p/>
     * If you were to try and continue that curve like so:
     * <p/>
     * <PRE>
     * curveto(x5, y5, x6, y6, x7, y7);
     * </PRE>
     * <p/>
     * This would be done in processing by adding these statements:
     * <p/>
     * <PRE>
     * bezierVertex(x5, y5, x6, y6, x7, y7)
     * </PRE>
     * <p/>
     * To draw a quadratic (instead of cubic) curve, use the control point twice
     * by doubling it:
     * <p/>
     * <PRE>
     * bezier(x1, y1, cx, cy, cx, cy, x2, y2);
     * </PRE>
     */
    public void bezier(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        beginShape();
        vertex(x1, y1);
        bezierVertex(x2, y2, x3, y3, x4, y4);
        endShape();
    }

    public void bezier(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        beginShape();
        vertex(x1, y1, z1);
        bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
        endShape();
    }

    // ////////////////////////////////////////////////////////////

    // CATMULL-ROM CURVE

    /**
     * Get a location along a catmull-rom curve segment.
     *
     * @param t Value between zero and one for how far along the segment
     */
    public float curvePoint(float a, float b, float c, float d, float t) {
        curveInitCheck();

        float tt = t * t;
        float ttt = t * tt;
        RMatrix3D cb = curveBasisMatrix;

        return (a * (ttt * cb.m00 + tt * cb.m10 + t * cb.m20 + cb.m30) + b * (ttt * cb.m01 + tt * cb.m11 + t * cb.m21 + cb.m31) + c * (ttt * cb.m02 + tt * cb.m12 + t * cb.m22 + cb.m32) + d
                * (ttt * cb.m03 + tt * cb.m13 + t * cb.m23 + cb.m33));
    }

    void curveInitCheck() {
        if (!curveInited) {
            curveInit();
        }
    }

    /**
     * Set the number of segments to use when drawing a Catmull-Rom curve, and
     * setting the s parameter, which defines how tightly the curve fits to each
     * vertex. Catmull-Rom curves are actually a subset of this curve type where
     * the s is set to zero.
     * <p/>
     * (This function is not optimized, since it's not expected to be called all
     * that often. there are many juicy and obvious opimizations in here, but
     * it's probably better to keep the code more readable)
     */
    void curveInit() {
        if (curveDrawMatrix == null) {
            curveBasisMatrix = new RMatrix3D();
            curveDrawMatrix = new RMatrix3D();
            curveInited = true;
        }

        float s = curveTightness;
        curveBasisMatrix.set((s - 1) / 2f, (s + 3) / 2f, (-3 - s) / 2f, (1 - s) / 2f, (1 - s), (-5 - s) / 2f, (s + 2), (s - 1) / 2f, (s - 1) / 2f, 0, (1 - s) / 2f, 0, 0, 1, 0, 0);

        splineForward(curveDetail, curveDrawMatrix);

        if (bezierBasisInverse == null) {
            bezierBasisInverse = bezierBasisMatrix.get();
            bezierBasisInverse.invert();
            curveToBezierMatrix = new RMatrix3D();
        }

        curveToBezierMatrix.set(curveBasisMatrix);
        curveToBezierMatrix.preApply(bezierBasisInverse);
        curveDrawMatrix.apply(curveBasisMatrix);
    }

    /**
     * Calculate the tangent at a t value (0..1) on a Catmull-Rom curve. Code
     * thanks to Dave Bollinger (Bug #715)
     */
    public float curveTangent(float a, float b, float c, float d, float t) {
        curveInitCheck();

        float tt3 = t * t * 3;
        float t2 = t * 2;
        RMatrix3D cb = curveBasisMatrix;

        return (a * (tt3 * cb.m00 + t2 * cb.m10 + cb.m20) + b * (tt3 * cb.m01 + t2 * cb.m11 + cb.m21) + c * (tt3 * cb.m02 + t2 * cb.m12 + cb.m22) + d * (tt3 * cb.m03 + t2 * cb.m13 + cb.m23));
    }

    public void curveDetail(int detail) {
        curveDetail = detail;
        curveInit();
    }

    public void curveTightness(float tightness) {
        curveTightness = tightness;
        curveInit();
    }

    /**
     * Draws a segment of Catmull-Rom curve.
     * <p/>
     * As of 0070, this function no longer doubles the first and last points.
     * The curves are a bit more boring, but it's more mathematically correct,
     * and properly mirrored in curvePoint().
     * <p/>
     * Identical to typing out:
     * <p/>
     * <PRE>
     * beginShape();
     * curveVertex(x1, y1);
     * curveVertex(x2, y2);
     * curveVertex(x3, y3);
     * curveVertex(x4, y4);
     * endShape();
     * </PRE>
     */
    public void curve(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        beginShape();
        curveVertex(x1, y1);
        curveVertex(x2, y2);
        curveVertex(x3, y3);
        curveVertex(x4, y4);
        endShape();
    }

    public void curveVertex(float x, float y) {
        curveVertexCheck();
        float[] vertex = curveVertices[curveVertexCount];
        vertex[X] = x;
        vertex[Y] = y;
        curveVertexCount++;

        // draw a segment if there are enough points
        if (curveVertexCount > 3) {
            curveVertexSegment(
                    curveVertices[curveVertexCount - 4][X],
                    curveVertices[curveVertexCount - 4][Y],
                    curveVertices[curveVertexCount - 3][X],
                    curveVertices[curveVertexCount - 3][Y],
                    curveVertices[curveVertexCount - 2][X],
                    curveVertices[curveVertexCount - 2][Y],
                    curveVertices[curveVertexCount - 1][X],
                    curveVertices[curveVertexCount - 1][Y]);
        }
    }

    // ////////////////////////////////////////////////////////////

    // SPLINE UTILITY FUNCTIONS (used by both Bezier and Catmull-Rom)

    protected void curveVertexCheck() {
        curveVertexCheck(shape);
    }

    // ////////////////////////////////////////////////////////////

    // SMOOTHING

    /**
     * Handle emitting a specific segment of Catmull-Rom curve. This can be
     * overridden by subclasses that need more efficient rendering options.
     */
    protected void curveVertexSegment(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        float x0 = x2;
        float y0 = y2;

        RMatrix3D draw = curveDrawMatrix;

        float xplot1 = draw.m10 * x1 + draw.m11 * x2 + draw.m12 * x3 + draw.m13 * x4;
        float xplot2 = draw.m20 * x1 + draw.m21 * x2 + draw.m22 * x3 + draw.m23 * x4;
        float xplot3 = draw.m30 * x1 + draw.m31 * x2 + draw.m32 * x3 + draw.m33 * x4;

        float yplot1 = draw.m10 * y1 + draw.m11 * y2 + draw.m12 * y3 + draw.m13 * y4;
        float yplot2 = draw.m20 * y1 + draw.m21 * y2 + draw.m22 * y3 + draw.m23 * y4;
        float yplot3 = draw.m30 * y1 + draw.m31 * y2 + draw.m32 * y3 + draw.m33 * y4;

        // vertex() will reset splineVertexCount, so save it
        int savedCount = curveVertexCount;

        vertex(x0, y0);
        for (int j = 0; j < curveDetail; j++) {
            x0 += xplot1;
            xplot1 += xplot2;
            xplot2 += xplot3;
            y0 += yplot1;
            yplot1 += yplot2;
            yplot2 += yplot3;
            vertex(x0, y0);
        }
        curveVertexCount = savedCount;
    }

    /**
     * Perform initialization specific to curveVertex(), and handle standard
     * error modes. Can be overridden by subclasses that need the flexibility.
     */
    void curveVertexCheck(int shape) {
        if (shape != POLYGON) {
            throw new RuntimeException("You must use beginShape() or " + "beginShape(POLYGON) before curveVertex()");
        }
        // to improve code init time, allocate on first use.
        if (curveVertices == null) {
            curveVertices = new float[128][3];
        }

        if (curveVertexCount == curveVertices.length) {
            // Can't use Imagine.expand() cuz it doesn't do the copy
            // properly
            float[][] temp = new float[curveVertexCount << 1][3];
            System.arraycopy(curveVertices, 0, temp, 0, curveVertexCount);
            curveVertices = temp;
        }
        curveInitCheck();
    }

    public void curve(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        beginShape();
        curveVertex(x1, y1, z1);
        curveVertex(x2, y2, z2);
        curveVertex(x3, y3, z3);
        curveVertex(x4, y4, z4);
        endShape();
    }

    public void curveVertex(float x, float y, float z) {
        curveVertexCheck();
        float[] vertex = curveVertices[curveVertexCount];
        vertex[X] = x;
        vertex[Y] = y;
        vertex[Z] = z;
        curveVertexCount++;

        // draw a segment if there are enough points
        if (curveVertexCount > 3) {
            curveVertexSegment(
                    curveVertices[curveVertexCount - 4][X],
                    curveVertices[curveVertexCount - 4][Y],
                    curveVertices[curveVertexCount - 4][Z],
                    curveVertices[curveVertexCount - 3][X],
                    curveVertices[curveVertexCount - 3][Y],
                    curveVertices[curveVertexCount - 3][Z],
                    curveVertices[curveVertexCount - 2][X],
                    curveVertices[curveVertexCount - 2][Y],
                    curveVertices[curveVertexCount - 2][Z],
                    curveVertices[curveVertexCount - 1][X],
                    curveVertices[curveVertexCount - 1][Y],
                    curveVertices[curveVertexCount - 1][Z]);
        }
    }

    /**
     * Handle emitting a specific segment of Catmull-Rom curve. This can be
     * overridden by subclasses that need more efficient rendering options.
     */
    void curveVertexSegment(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        float x0 = x2;
        float y0 = y2;
        float z0 = z2;

        RMatrix3D draw = curveDrawMatrix;

        float xplot1 = draw.m10 * x1 + draw.m11 * x2 + draw.m12 * x3 + draw.m13 * x4;
        float xplot2 = draw.m20 * x1 + draw.m21 * x2 + draw.m22 * x3 + draw.m23 * x4;
        float xplot3 = draw.m30 * x1 + draw.m31 * x2 + draw.m32 * x3 + draw.m33 * x4;

        float yplot1 = draw.m10 * y1 + draw.m11 * y2 + draw.m12 * y3 + draw.m13 * y4;
        float yplot2 = draw.m20 * y1 + draw.m21 * y2 + draw.m22 * y3 + draw.m23 * y4;
        float yplot3 = draw.m30 * y1 + draw.m31 * y2 + draw.m32 * y3 + draw.m33 * y4;

        // vertex() will reset splineVertexCount, so save it
        int savedCount = curveVertexCount;

        float zplot1 = draw.m10 * z1 + draw.m11 * z2 + draw.m12 * z3 + draw.m13 * z4;
        float zplot2 = draw.m20 * z1 + draw.m21 * z2 + draw.m22 * z3 + draw.m23 * z4;
        float zplot3 = draw.m30 * z1 + draw.m31 * z2 + draw.m32 * z3 + draw.m33 * z4;

        vertex(x0, y0, z0);
        for (int j = 0; j < curveDetail; j++) {
            x0 += xplot1;
            xplot1 += xplot2;
            xplot2 += xplot3;
            y0 += yplot1;
            yplot1 += yplot2;
            yplot2 += yplot3;
            z0 += zplot1;
            zplot1 += zplot2;
            zplot2 += zplot3;
            vertex(x0, y0, z0);
        }
        curveVertexCount = savedCount;
    }

    /**
     * If true in PImage, use bilinear interpolation for copy() operations. When
     * inherited by PGraphics, also controls shapes.
     */
    public void smooth() {
        smooth = true;
    }

    public void smooth(int level) {
        smooth = true;
    }

    /**
     * Disable smoothing. See smooth().
     */
    public void noSmooth() {
        smooth = false;
    }

    /**
     * The mode can only be set to CORNERS, CORNER, and CENTER.
     * <p/>
     * Support for CENTER was added in release 0146.
     */
    public void imageMode(int mode) {
        if ((mode == CORNER) || (mode == CORNERS) || (mode == CENTER)) {
            imageMode = mode;
        } else {
            String msg = "imageMode() only works with CORNER, CORNERS, or CENTER";
            throw new RuntimeException(msg);
        }
    }

    public void image(RainbowImage image, float x, float y) {
        // Starting in release 0144, image errors are simply ignored.
        // loadImageAsync() sets width and height to -1 when loading fails.
        if (image.width == -1 || image.height == -1) {
            return;
        }

        // If not loaded yet, don't try to draw
        if (image.width == 0 || image.height == 0) {
            return;
        }

        if (imageMode == CORNER || imageMode == CORNERS) {
            imageImpl(image, x, y, x + image.width, y + image.height, 0, 0, image.width, image.height);

        } else if (imageMode == CENTER) {
            float x1 = x - image.width / 2;
            float y1 = y - image.height / 2;
            imageImpl(image, x1, y1, x1 + image.width, y1 + image.height, 0, 0, image.width, image.height);
        }
    }

    /**
     * Expects x1, y1, x2, y2 coordinates where (x2 >= x1) and (y2 >= y1). If
     * tint() has been called, the image will be colored.
     * <p/>
     * The default implementation draws an image as a textured quad. The (u, v)
     * coordinates are in image space (they're ints, after all..)
     */
    protected void imageImpl(RainbowImage image, float x1, float y1, float x2, float y2, int u1, int v1, int u2, int v2) {
        boolean savedStroke = stroke;
        boolean savedFill = fill;
        int savedTextureMode = textureMode;

        stroke = false;
        fill = true;
        textureMode = IMAGE;

        float savedFillR = fillR;
        float savedFillG = fillG;
        float savedFillB = fillB;
        float savedFillA = fillA;

        if (tint) {
            fillR = tintR;
            fillG = tintG;
            fillB = tintB;
            fillA = tintA;

        } else {
            fillR = 1;
            fillG = 1;
            fillB = 1;
            fillA = 1;
        }

        beginShape(QUADS);
        texture(image);
        vertex(x1, y1, u1, v1);
        vertex(x1, y2, u1, v2);
        vertex(x2, y2, u2, v2);
        vertex(x2, y1, u2, v1);
        endShape();

        stroke = savedStroke;
        fill = savedFill;
        textureMode = savedTextureMode;

        fillR = savedFillR;
        fillG = savedFillG;
        fillB = savedFillB;
        fillA = savedFillA;
    }

    /**
     * Set texture image for current shape. Needs to be called between @see
     * beginShape and @see endShape
     *
     * @param image reference to a PImage object
     */
    public void texture(RainbowImage image) {
        textureImage = image;
    }

    public void vertex(float x, float y, float u, float v) {
        vertexTexture(u, v);
        vertex(x, y);
    }

    public void image(RainbowImage image, float x, float y, float c, float d) {
        image(image, x, y, c, d, 0, 0, image.width, image.height);
    }

    /**
     * Draw an image(), also specifying u/v coordinates. In this method, the u,
     * v coordinates are always based on image space location, regardless of the
     * current textureMode().
     */
    public void image(RainbowImage image, float a, float b, float c, float d, int u1, int v1, int u2, int v2) {
        if (image.width == -1 || image.height == -1) {
            return;
        }

        if (imageMode == CORNER) {
            if (c < 0) { // reset a negative width
                a += c;
                c = -c;
            }
            if (d < 0) { // reset a negative height
                b += d;
                d = -d;
            }

            imageImpl(image, a, b, a + c, b + d, u1, v1, u2, v2);

        } else if (imageMode == CORNERS) {
            if (c < a) { // reverse because x2 < x1
                float temp = a;
                a = c;
                c = temp;
            }
            if (d < b) { // reverse because y2 < y1
                float temp = b;
                b = d;
                d = temp;
            }

            imageImpl(image, a, b, c, d, u1, v1, u2, v2);

        } else if (imageMode == CENTER) {
            // c and d are width/height
            if (c < 0) {
                c = -c;
            }
            if (d < 0) {
                d = -d;
            }
            float x1 = a - c / 2;
            float y1 = b - d / 2;

            imageImpl(image, x1, y1, x1 + c, y1 + d, u1, v1, u2, v2);
        }
    }

    /**
     * Push a copy of the current transformation matrix onto the stack.
     */
    public void pushMatrix() {
        showMethodWarning("pushMatrix");
    }

    /**
     * Replace the current transformation matrix with the top of the stack.
     */
    public void popMatrix() {
        showMethodWarning("popMatrix");
    }

    /**
     * Translate in X and Y.
     */
    public void translate(float tx, float ty) {
        showMissingWarning("translate");
    }

    /**
     * Translate in X, Y, and Z.
     */
    public void translate(float tx, float ty, float tz) {
        showMissingWarning("translate");
    }

    /**
     * Two dimensional rotation.
     * <p/>
     * Same as rotateZ (this is identical to a 3D rotation along the z-axis) but
     * included for clarity. It'd be weird for people drawing 2D graphics to be
     * using rotateZ. And they might kick our a-- for the confusion.
     * <p/>
     * <A HREF="http://www.xkcd.com/c184.html">Additional background</A>.
     */
    public void rotate(float angle) {
        showMissingWarning("rotate");
    }

    /**
     * Rotate around the X axis.
     */
    public void rotateX(float angle) {
        showMethodWarning("rotateX");
    }

    /**
     * Display a warning that the specified method is simply unavailable.
     */
    public static void showMethodWarning(String method) {
        showWarning(method + "() is not available with this renderer.");
    }

    /**
     * Rotate around the Y axis.
     */
    public void rotateY(float angle) {
        showMethodWarning("rotateY");
    }

    /**
     * Rotate around the Z axis.
     * <p/>
     * The functions rotate() and rotateZ() are identical, it's just that it
     * make sense to have rotate() and then rotateX() and rotateY() when using
     * 3D; nor does it make sense to use a function called rotateZ() if you're
     * only doing things in 2D. so we just decided to have them both be the
     * same.
     */
    public void rotateZ(float angle) {
        showMethodWarning("rotateZ");
    }

    /**
     * Rotate about a vector in space. Same as the glRotatef() function.
     */
    public void rotate(float angle, float vx, float vy, float vz) {
        showMissingWarning("rotate");
    }

    /**
     * Scale in all dimensions.
     */
    public void scale(float s) {
        showMissingWarning("scale");
    }

    /**
     * Scale in X and Y. Equivalent to scale(sx, sy, 1).
     * <p/>
     * Not recommended for use in 3D, because the z-dimension is just scaled by
     * 1, since there's no way to know what else to scale it by.
     */
    public void scale(float sx, float sy) {
        showMissingWarning("scale");
    }

    /**
     * Scale in X, Y, and Z.
     */
    public void scale(float x, float y, float z) {
        showMissingWarning("scale");
    }

    /**
     * Shear along X axis
     */
    public void shearX(float angle) {
        showMissingWarning("shearX");
    }

    /**
     * Skew along Y axis
     */
    public void shearY(float angle) {
        showMissingWarning("shearY");
    }

    /**
     * Set the current transformation matrix to identity.
     */
    public void resetMatrix() {
        showMethodWarning("resetMatrix");
    }

    public void applyMatrix(RMatrix source) {
        if (source instanceof RMatrix2D) {
            applyMatrix((RMatrix2D) source);
        } else if (source instanceof RMatrix3D) {
            applyMatrix((RMatrix3D) source);
        }
    }

    public void applyMatrix(RMatrix2D source) {
        applyMatrix(source.m00, source.m01, source.m02, source.m10, source.m11, source.m12);
    }

    /**
     * Apply a 3x2 affine transformation matrix.
     */
    public void applyMatrix(float n00, float n01, float n02, float n10, float n11, float n12) {
        showMissingWarning("applyMatrix");
    }

    public void applyMatrix(RMatrix3D source) {
        applyMatrix(
                source.m00,
                source.m01,
                source.m02,
                source.m03,
                source.m10,
                source.m11,
                source.m12,
                source.m13,
                source.m20,
                source.m21,
                source.m22,
                source.m23,
                source.m30,
                source.m31,
                source.m32,
                source.m33);
    }

    /**
     * Apply a 4x4 transformation matrix.
     */
    public void applyMatrix(float n00,
                            float n01,
                            float n02,
                            float n03,
                            float n10,
                            float n11,
                            float n12,
                            float n13,
                            float n20,
                            float n21,
                            float n22,
                            float n23,
                            float n30,
                            float n31,
                            float n32,
                            float n33) {
        showMissingWarning("applyMatrix");
    }

    public RMatrix getMatrix() {
        showMissingWarning("getMatrix");
        return null;
    }

    /**
     * Set the current transformation matrix to the contents of another.
     */
    public void setMatrix(RMatrix source) {
        if (source instanceof RMatrix2D) {
            setMatrix((RMatrix2D) source);
        } else if (source instanceof RMatrix3D) {
            setMatrix((RMatrix3D) source);
        }
    }

    /**
     * Set the current transformation to the contents of the specified source.
     */
    public void setMatrix(RMatrix2D source) {
        showMissingWarning("setMatrix");
    }

    /**
     * Set the current transformation to the contents of the specified source.
     */
    public void setMatrix(RMatrix3D source) {
        showMissingWarning("setMatrix");
    }

    /**
     * Copy the current transformation matrix into the specified target. Pass in
     * null to create a new matrix.
     */
    public RMatrix2D getMatrix(RMatrix2D target) {
        showMissingWarning("getMatrix");
        return null;
    }

    /**
     * Copy the current transformation matrix into the specified target. Pass in
     * null to create a new matrix.
     */
    public RMatrix3D getMatrix(RMatrix3D target) {
        showMissingWarning("getMatrix");
        return null;
    }

    /**
     * Print the current model (or "transformation") matrix.
     */
    public void printMatrix() {
        showMethodWarning("printMatrix");
    }

    public void beginCamera() {
        showMethodWarning("beginCamera");
    }

    public void endCamera() {
        showMethodWarning("endCamera");
    }

    public void camera() {
        showMissingWarning("camera");
    }

    public void camera(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
        showMissingWarning("camera");
    }

    public void printCamera() {
        showMethodWarning("printCamera");
    }

    public void ortho() {
        showMissingWarning("ortho");
    }

    public void ortho(float left, float right, float bottom, float top) {
        showMissingWarning("ortho");
    }

    public void ortho(float left, float right, float bottom, float top, float near, float far) {
        showMissingWarning("ortho");
    }

    public void perspective() {
        showMissingWarning("perspective");
    }

    public void perspective(float fovy, float aspect, float zNear, float zFar) {
        showMissingWarning("perspective");
    }

    public void frustum(float left, float right, float bottom, float top, float near, float far) {
        showMethodWarning("frustum");
    }

    public void printProjection() {
        showMethodWarning("printCamera");
    }

    /**
     * Given an x and y coordinate, returns the x position of where that point
     * would be placed on screen, once affected by translate(), scale(), or any
     * other transformations.
     */
    public float screenX(float x, float y) {
        showMissingWarning("screenX");
        return 0;
    }

    /**
     * Given an x and y coordinate, returns the y position of where that point
     * would be placed on screen, once affected by translate(), scale(), or any
     * other transformations.
     */
    public float screenY(float x, float y) {
        showMissingWarning("screenY");
        return 0;
    }

    /**
     * Maps a three dimensional point to its placement on-screen.
     * <p/>
     * Given an (x, y, z) coordinate, returns the x position of where that point
     * would be placed on screen, once affected by translate(), scale(), or any
     * other transformations.
     */
    public float screenX(float x, float y, float z) {
        showMissingWarning("screenX");
        return 0;
    }

    /**
     * Maps a three dimensional point to its placement on-screen.
     * <p/>
     * Given an (x, y, z) coordinate, returns the y position of where that point
     * would be placed on screen, once affected by translate(), scale(), or any
     * other transformations.
     */
    public float screenY(float x, float y, float z) {
        showMissingWarning("screenY");
        return 0;
    }

    /**
     * Maps a three dimensional point to its placement on-screen.
     * <p/>
     * Given an (x, y, z) coordinate, returns its z value. This value can be
     * used to determine if an (x, y, z) coordinate is in front or in back of
     * another (x, y, z) coordinate. The units are based on how the zbuffer is
     * set up, and don't relate to anything "real". They're only useful for in
     * comparison to another value obtained from screenZ(), or directly out of
     * the zbuffer[].
     */
    public float screenZ(float x, float y, float z) {
        showMissingWarning("screenZ");
        return 0;
    }

    /**
     * Returns the model space x value for an x, y, z coordinate.
     * <p/>
     * This will give you a coordinate after it has been transformed by
     * translate(), rotate(), and camera(), but not yet transformed by the
     * projection matrix. For instance, his can be useful for figuring out how
     * points in 3D space relate to the edge coordinates of a shape.
     */
    public float modelX(float x, float y, float z) {
        showMissingWarning("modelX");
        return 0;
    }

    /**
     * Returns the model space y value for an x, y, z coordinate.
     */
    public float modelY(float x, float y, float z) {
        showMissingWarning("modelY");
        return 0;
    }

    /**
     * Returns the model space z value for an x, y, z coordinate.
     */
    public float modelZ(float x, float y, float z) {
        showMissingWarning("modelZ");
        return 0;
    }

    public void pushStyle() {
        if (styleStackDepth == styleStack.length) {
            styleStack = (RainbowStyle[]) RainbowMath.expand(styleStack);
        }
        if (styleStack[styleStackDepth] == null) {
            styleStack[styleStackDepth] = new RainbowStyle();
        }
        RainbowStyle s = styleStack[styleStackDepth++];
        getStyle(s);
    }

    public void popStyle() {
        if (styleStackDepth == 0) {
            throw new RuntimeException("Too many popStyle() without enough pushStyle()");
        }
        styleStackDepth--;
        style(styleStack[styleStackDepth]);
    }

    public void style(RainbowStyle s) {
        imageMode(s.imageMode);
        rectMode(s.rectMode);
        ellipseMode(s.ellipseMode);

        if (s.tint) {
            tint(s.tintColor);
        } else {
            noTint();
        }
        if (s.fill) {
            fill(s.fillColor);
        } else {
            noFill();
        }
        if (s.stroke) {
            stroke(s.strokeColor);
        } else {
            noStroke();
        }
        strokeWeight(s.strokeWeight);
        strokeCap(s.strokeCap);
        strokeJoin(s.strokeJoin);

        colorMode(RGB, 1);
        ambient(s.ambientR, s.ambientG, s.ambientB);
        emissive(s.emissiveR, s.emissiveG, s.emissiveB);
        specular(s.specularR, s.specularG, s.specularB);
        shininess(s.shininess);
        colorMode(s.colorMode, s.colorModeX, s.colorModeY, s.colorModeZ, s.colorModeA);

    }

    public RainbowStyle getStyle() { // ignore
        return getStyle(null);
    }

    RainbowStyle getStyle(RainbowStyle s) { // ignore
        if (s == null) {
            s = new RainbowStyle();
        }

        s.imageMode = imageMode;
        s.rectMode = rectMode;
        s.ellipseMode = ellipseMode;
        s.shapeMode = shapeMode;

        s.colorMode = colorMode;
        s.colorModeX = colorModeX;
        s.colorModeY = colorModeY;
        s.colorModeZ = colorModeZ;
        s.colorModeA = colorModeA;

        s.tint = tint;
        s.tintColor = tintColor;
        s.fill = fill;
        s.fillColor = fillColor;
        s.stroke = stroke;
        s.strokeColor = strokeColor;
        s.strokeWeight = strokeWeight;
        s.strokeCap = strokeCap;
        s.strokeJoin = strokeJoin;

        s.ambientR = ambientR;
        s.ambientG = ambientG;
        s.ambientB = ambientB;
        s.specularR = specularR;
        s.specularG = specularG;
        s.specularB = specularB;
        s.emissiveR = emissiveR;
        s.emissiveG = emissiveG;
        s.emissiveB = emissiveB;
        s.shininess = shininess;

        return s;
    }

    public void strokeWeight(float weight) {
        strokeWeight = weight;
    }

    public void strokeJoin(int join) {
        strokeJoin = join;
    }

    public void strokeCap(int cap) {
        strokeCap = cap;
    }

    public void noStroke() {
        stroke = false;
    }

    /**
     * Set the tint to either a grayscale or ARGB value. See notes attached to
     * the fill() function.
     */
    public void stroke(int rgb) {
        colorCalc(rgb);
        strokeFromCalc();
    }

    public void stroke(int rgb, float alpha) {
        colorCalc(rgb, alpha);
        strokeFromCalc();
    }

    void colorCalc(int rgb, float alpha) {
        if (((rgb & 0xff000000) == 0) && (rgb <= colorModeX)) { // see above
            colorCalc((float) rgb, alpha);

        } else {
            colorCalcARGB(rgb, alpha);
        }
    }

    protected void strokeFromCalc() {
        stroke = true;
        strokeR = calcR;
        strokeG = calcG;
        strokeB = calcB;
        strokeA = calcA;
        strokeColor = calcColor;
    }

    void colorCalc(float gray, float alpha) {
        if (gray > colorModeX) {
            gray = colorModeX;
        }
        if (alpha > colorModeA) {
            alpha = colorModeA;
        }

        if (gray < 0) {
            gray = 0;
        }
        if (alpha < 0) {
            alpha = 0;
        }

        calcR = colorModeScale ? (gray / colorModeX) : gray;
        calcG = calcR;
        calcB = calcR;
        calcA = colorModeScale ? (alpha / colorModeA) : alpha;

        calcRi = (int) (calcR * 255);
        calcGi = (int) (calcG * 255);
        calcBi = (int) (calcB * 255);
        calcAi = (int) (calcA * 255);
        calcColor = (calcAi << 24) | (calcRi << 16) | (calcGi << 8) | calcBi;
        calcAlpha = (calcAi != 255);
    }

    /**
     * Unpacks AARRGGBB color for direct use with colorCalc.
     * <p/>
     * Handled here with its own function since this is indepenent of the color
     * mode.
     * <p/>
     * Strangely the old version of this code ignored the alpha value. not sure
     * if that was a bug or what.
     * <p/>
     * Note, no need for a bounds check since it's a 32 bit number.
     */
    void colorCalcARGB(int argb, float alpha) {
        if (alpha == colorModeA) {
            calcAi = (argb >> 24) & 0xff;
            calcColor = argb;
        } else {
            calcAi = (int) (((argb >> 24) & 0xff) * (alpha / colorModeA));
            calcColor = (calcAi << 24) | (argb & 0xFFFFFF);
        }
        calcRi = (argb >> 16) & 0xff;
        calcGi = (argb >> 8) & 0xff;
        calcBi = argb & 0xff;
        calcA = (float) calcAi / 255.0f;
        calcR = (float) calcRi / 255.0f;
        calcG = (float) calcGi / 255.0f;
        calcB = (float) calcBi / 255.0f;
        calcAlpha = (calcAi != 255);
    }

    public void stroke(float gray) {
        colorCalc(gray);
        strokeFromCalc();
    }

    void colorCalc(float gray) {
        colorCalc(gray, colorModeA);
    }

    public void stroke(float gray, float alpha) {
        colorCalc(gray, alpha);
        strokeFromCalc();
    }

    public void stroke(float x, float y, float z) {
        colorCalc(x, y, z);
        strokeFromCalc();
    }

    void colorCalc(float x, float y, float z) {
        colorCalc(x, y, z, colorModeA);
    }

    void colorCalc(float x, float y, float z, float a) {
        if (x > colorModeX) {
            x = colorModeX;
        }
        if (y > colorModeY) {
            y = colorModeY;
        }
        if (z > colorModeZ) {
            z = colorModeZ;
        }
        if (a > colorModeA) {
            a = colorModeA;
        }

        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (z < 0) {
            z = 0;
        }
        if (a < 0) {
            a = 0;
        }

        switch (colorMode) {
            case RGB:
                colorCalcRGB(x, y, z, a);
                break;

            case HSB:
                colorCalcHSB(x, y, z, a);
                break;
        }
        calcRi = (int) (255 * calcR);
        calcGi = (int) (255 * calcG);
        calcBi = (int) (255 * calcB);
        calcAi = (int) (255 * calcA);
        calcColor = (calcAi << 24) | (calcRi << 16) | (calcGi << 8) | calcBi;
        calcAlpha = (calcAi != 255);
    }

    private void colorCalcRGB(float x, float y, float z, float a) {
        if (colorModeScale) {
            calcR = x / colorModeX;
            calcG = y / colorModeY;
            calcB = z / colorModeZ;
            calcA = a / colorModeA;
        } else {
            calcR = x;
            calcG = y;
            calcB = z;
            calcA = a;
        }
    }

    private void colorCalcHSB(float x, float y, float z, float a) {
        x /= colorModeX; // h
        y /= colorModeY; // s
        z /= colorModeZ; // b

        calcA = colorModeScale ? (a / colorModeA) : a;

        if (y == 0) { // saturation == 0
            calcR = calcG = calcB = z;

        } else {
            float which = (x - (int) x) * 6.0f;
            float f = which - (int) which;
            float p = z * (1.0f - y);
            float q = z * (1.0f - y * f);
            float t = z * (1.0f - (y * (1.0f - f)));

            switch ((int) which) {
                case 0:
                    calcR = z;
                    calcG = t;
                    calcB = p;
                    break;
                case 1:
                    calcR = q;
                    calcG = z;
                    calcB = p;
                    break;
                case 2:
                    calcR = p;
                    calcG = z;
                    calcB = t;
                    break;
                case 3:
                    calcR = p;
                    calcG = q;
                    calcB = z;
                    break;
                case 4:
                    calcR = t;
                    calcG = p;
                    calcB = z;
                    break;
                case 5:
                    calcR = z;
                    calcG = p;
                    calcB = q;
                    break;
            }
        }
    }

    public void stroke(float x, float y, float z, float a) {
        colorCalc(x, y, z, a);
        strokeFromCalc();
    }

    public void noTint() {
        tint = false;
    }

    /**
     * Set the tint to either a grayscale or ARGB value.
     */
    public void tint(int rgb) {
        colorCalc(rgb);
        tintFromCalc();
    }

    public void tint(int rgb, float alpha) {
        colorCalc(rgb, alpha);
        tintFromCalc();
    }

    protected void tintFromCalc() {
        tint = true;
        tintR = calcR;
        tintG = calcG;
        tintB = calcB;
        tintA = calcA;
        tintColor = calcColor;
    }

    public void tint(float gray) {
        colorCalc(gray);
        tintFromCalc();
    }

    public void tint(float gray, float alpha) {
        colorCalc(gray, alpha);
        tintFromCalc();
    }

    public void tint(float x, float y, float z) {
        colorCalc(x, y, z);
        tintFromCalc();
    }

    public void tint(float x, float y, float z, float a) {
        colorCalc(x, y, z, a);
        tintFromCalc();
    }

    public void noFill() {
        fill = false;
    }

    /**
     * Set the fill to either a grayscale value or an ARGB int.
     */
    public void fill(int rgb) {
        colorCalc(rgb);
        fillFromCalc();
    }

    public void fill(int rgb, float alpha) {
        colorCalc(rgb, alpha);
        fillFromCalc();
    }

    protected void fillFromCalc() {
        fill = true;
        fillR = calcR;
        fillG = calcG;
        fillB = calcB;
        fillA = calcA;
        fillColor = calcColor;
    }

    public void fill(float gray) {
        colorCalc(gray);
        fillFromCalc();
    }

    public void fill(float gray, float alpha) {
        colorCalc(gray, alpha);
        fillFromCalc();
    }

    public void fill(float x, float y, float z) {
        colorCalc(x, y, z);
        fillFromCalc();
    }

    public void fill(float x, float y, float z, float a) {
        colorCalc(x, y, z, a);
        fillFromCalc();
    }

    public void ambient(int rgb) {
        colorCalc(rgb);
        ambientFromCalc();
    }

    /**
     * Set the fill to either a grayscale value or an ARGB int.
     * <p/>
     * The problem with this code is that it has to detect between these two
     * situations automatically. This is done by checking to see if the high
     * bits (the alpha for 0xAA000000) is set, and if not, whether the color
     * value that follows is less than colorModeX (first param passed to
     * colorMode).
     * <p/>
     * This auto-detect would break in the following situation:
     * <p/>
     * <PRE>
     * size(256, 256);
     * for (int i = 0; i &lt; 256; i++) {
     * color c = color(0, 0, 0, i);
     * stroke(c);
     * line(i, 0, i, 256);
     * }
     * </PRE>
     * <p/>
     * ...on the first time through the loop, where (i == 0), since the color
     * itself is zero (black) then it would appear indistinguishable from code
     * that reads "fill(0)". The solution is to use the four parameter versions
     * of stroke or fill to more directly specify the desired result.
     */
    void colorCalc(int rgb) {
        if (((rgb & 0xff000000) == 0) && (rgb <= colorModeX)) {
            colorCalc((float) rgb);

        } else {
            colorCalcARGB(rgb, colorModeA);
        }
    }

    void ambientFromCalc() {
        ambientR = calcR;
        ambientG = calcG;
        ambientB = calcB;
    }

    public void ambient(float gray) {
        colorCalc(gray);
        ambientFromCalc();
    }

    public void ambient(float x, float y, float z) {
        colorCalc(x, y, z);
        ambientFromCalc();
    }

    public void specular(int rgb) {
        colorCalc(rgb);
        specularFromCalc();
    }

    void specularFromCalc() {
        specularR = calcR;
        specularG = calcG;
        specularB = calcB;
    }

    public void specular(float gray) {
        colorCalc(gray);
        specularFromCalc();
    }

    public void specular(float x, float y, float z) {
        colorCalc(x, y, z);
        specularFromCalc();
    }

    public void shininess(float shine) {
        shininess = shine;
    }

    public void emissive(int rgb) {
        colorCalc(rgb);
        emissiveFromCalc();
    }

    void emissiveFromCalc() {
        emissiveR = calcR;
        emissiveG = calcG;
        emissiveB = calcB;
    }

    public void emissive(float gray) {
        colorCalc(gray);
        emissiveFromCalc();
    }

    public void emissive(float x, float y, float z) {
        colorCalc(x, y, z);
        emissiveFromCalc();
    }

    public void lights() {
        showMethodWarning("lights");
    }

    public void noLights() {
        showMethodWarning("noLights");
    }

    public void ambientLight(float red, float green, float blue) {
        showMethodWarning("ambientLight");
    }

    public void ambientLight(float red, float green, float blue, float x, float y, float z) {
        showMethodWarning("ambientLight");
    }

    public void directionalLight(float red, float green, float blue, float nx, float ny, float nz) {
        showMethodWarning("directionalLight");
    }

    public void pointLight(float red, float green, float blue, float x, float y, float z) {
        showMethodWarning("pointLight");
    }

    public void spotLight(float red, float green, float blue, float x, float y, float z, float nx, float ny, float nz, float angle, float concentration) {
        showMethodWarning("spotLight");
    }

    public void lightFalloff(float constant, float linear, float quadratic) {
        showMethodWarning("lightFalloff");
    }

    public void lightSpecular(float x, float y, float z) {
        showMethodWarning("lightSpecular");
    }

    /**
     * Set the background to a gray or ARGB color.
     * <p/>
     * For the main drawing surface, the alpha value will be ignored. However,
     * alpha can be used on PGraphics objects from createGraphics(). This is the
     * only way to set all the pixels partially transparent, for instance.
     * <p/>
     * Note that background() should be called before any transformations occur,
     * because some implementations may require the current transformation
     * matrix to be identity before drawing.
     */
    public void background(int rgb) {
        colorCalc(rgb);
        backgroundFromCalc();
    }

    /**
     * See notes about alpha in background(x, y, z, a).
     */
    public void background(int rgb, float alpha) {
        colorCalc(rgb, alpha);
        backgroundFromCalc();
    }

    /**
     * Set the background to a grayscale value, based on the current colorMode.
     */
    public void background(float gray) {
        colorCalc(gray);
        backgroundFromCalc();
    }

    /**
     * See notes about alpha in background(x, y, z, a).
     */
    public void background(float gray, float alpha) {
        if (format == RGB) {
            background(gray); // ignore alpha for main drawing surface

        } else {
            colorCalc(gray, alpha);
            backgroundFromCalc();
        }
    }

    /**
     * Set the background to an r, g, b or h, s, b value, based on the current
     * colorMode.
     */
    public void background(float x, float y, float z) {
        colorCalc(x, y, z);
        backgroundFromCalc();
    }

    /**
     * Clear the background with a color that includes an alpha value. This can
     * only be used with objects created by createGraphics(), because the main
     * drawing surface cannot be set transparent.
     * <p/>
     * It might be tempting to use this function to partially clear the screen
     * on each frame, however that's not how this function works. When calling
     * background(), the pixels will be replaced with pixels that have that
     * level of transparency. To do a semi-transparent overlay, use fill() with
     * alpha and draw a rectangle.
     */
    public void background(float x, float y, float z, float a) {
        colorCalc(x, y, z, a);
        backgroundFromCalc();
    }

    public void clear() {
        background(0, 0, 0, 0);
    }

    void backgroundFromCalc() {
        backgroundColor = calcColor;

        backgroundImpl();
    }

    /**
     * Takes an RGB or ARGB image and sets it as the background. The width and
     * height of the image must be the same size as the sketch. Use
     * image.resize(width, height) to make short work of such a task.
     * <p/>
     * Note that even if the image is set as RGB, the high 8 bits of each pixel
     * should be set opaque (0xFF000000), because the image data will be copied
     * directly to the screen, and non-opaque background images may have strange
     * behavior. Using image.filter(OPAQUE) will handle this easily.
     * <p/>
     * When using 3D, this will also clear the zbuffer (if it exists).
     */
    public void background(RainbowImage image) {
        if ((image.width != width) || (image.height != height)) {
            throw new RuntimeException(ERROR_BACKGROUND_IMAGE_SIZE);
        }
        if ((image.format != RGB) && (image.format != ARGB)) {
            throw new RuntimeException(ERROR_BACKGROUND_IMAGE_FORMAT);
        }
        backgroundColor = 0; // just zero it out for images
        backgroundImpl(image);
    }

    /**
     * Actually set the background image. This is separated from the error
     * handling and other semantic goofiness that is shared across renderers.
     */
    void backgroundImpl(RainbowImage image) {
        // blit image to the screen
        set(0, 0, image);
    }

    /**
     * Actual implementation of clearing the background, now that the internal
     * variables for background color have been set. Called by the
     * backgroundFromCalc() method, which is what all the other background()
     * methods call once the work is done.
     */
    protected void backgroundImpl() {
        pushStyle();
        pushMatrix();
        resetMatrix();
        fill(backgroundColor);
        rect(0, 0, width, height);
        popMatrix();
        popStyle();
    }

    public void colorMode(int mode) {
        colorMode(mode, colorModeX, colorModeY, colorModeZ, colorModeA);
    }

    public void colorMode(int mode, float maxX, float maxY, float maxZ, float maxA) {
        colorMode = mode;

        colorModeX = maxX; // still needs to be set for hsb
        colorModeY = maxY;
        colorModeZ = maxZ;
        colorModeA = maxA;

        colorModeScale = ((maxA != 1) || (maxX != maxY) || (maxY != maxZ) || (maxZ != maxA));
        colorModeDefault = (colorMode == RGB) && (colorModeA == 255) && (colorModeX == 255) && (colorModeY == 255) && (colorModeZ == 255);
    }

    public void colorMode(int mode, float max) {
        colorMode(mode, max, max, max, max);
    }

    /**
     * Set the colorMode and the maximum values for (r, g, b) or (h, s, b).
     * <p/>
     * Note that this doesn't set the maximum for the alpha value, which might
     * be confusing if for instance you switched to
     * <p/>
     * <PRE>
     * colorMode(HSB, 360, 100, 100);
     * </PRE>
     * <p/>
     * because the alpha values were still between 0 and 255.
     */
    public void colorMode(int mode, float maxX, float maxY, float maxZ) {
        colorMode(mode, maxX, maxY, maxZ, colorModeA);
    }

    public final int color(int gray) { // ignore
        if (((gray & 0xff000000) == 0) && (gray <= colorModeX)) {
            if (colorModeDefault) {
                if (gray > 255) {
                    gray = 255;
                } else if (gray < 0) {
                    gray = 0;
                }
                return 0xff000000 | (gray << 16) | (gray << 8) | gray;
            } else {
                colorCalc(gray);
            }
        } else {
            colorCalcARGB(gray, colorModeA);
        }
        return calcColor;
    }

    public final int color(float gray) { // ignore
        colorCalc(gray);
        return calcColor;
    }

    /**
     * @param gray can be packed ARGB or a gray in this case
     */
    public final int color(int gray, int alpha) { // ignore
        if (colorModeDefault) {
            // bounds checking to make sure the numbers aren't to high or low
            if (gray > 255) {
                gray = 255;
            } else if (gray < 0) {
                gray = 0;
            }
            if (alpha > 255) {
                alpha = 255;
            } else if (alpha < 0) {
                alpha = 0;
            }

            return ((alpha & 0xff) << 24) | (gray << 16) | (gray << 8) | gray;
        }
        colorCalc(gray, alpha);
        return calcColor;
    }

    /**
     * @param rgb can be packed ARGB or a gray in this case
     */
    public final int color(int rgb, float alpha) { // ignore
        if (((rgb & 0xff000000) == 0) && (rgb <= colorModeX)) {
            colorCalc(rgb, alpha);
        } else {
            colorCalcARGB(rgb, alpha);
        }
        return calcColor;
    }

    public final int color(float gray, float alpha) { // ignore
        colorCalc(gray, alpha);
        return calcColor;
    }

    public final int color(int x, int y, int z) { // ignore
        if (colorModeDefault) {
            // bounds checking to make sure the numbers aren't to high or low
            if (x > 255) {
                x = 255;
            } else if (x < 0) {
                x = 0;
            }
            if (y > 255) {
                y = 255;
            } else if (y < 0) {
                y = 0;
            }
            if (z > 255) {
                z = 255;
            } else if (z < 0) {
                z = 0;
            }

            return 0xff000000 | (x << 16) | (y << 8) | z;
        }
        colorCalc(x, y, z);
        return calcColor;
    }

    public final int color(float x, float y, float z) { // ignore
        colorCalc(x, y, z);
        return calcColor;
    }

    public final int color(int x, int y, int z, int a) { // ignore
        if (colorModeDefault) {
            // bounds checking to make sure the numbers aren't to high or low
            if (a > 255) {
                a = 255;
            } else if (a < 0) {
                a = 0;
            }
            if (x > 255) {
                x = 255;
            } else if (x < 0) {
                x = 0;
            }
            if (y > 255) {
                y = 255;
            } else if (y < 0) {
                y = 0;
            }
            if (z > 255) {
                z = 255;
            } else if (z < 0) {
                z = 0;
            }

            return (a << 24) | (x << 16) | (y << 8) | z;
        }
        colorCalc(x, y, z, a);
        return calcColor;
    }

    public final int color(float x, float y, float z, float a) { // ignore
        colorCalc(x, y, z, a);
        return calcColor;
    }

    public final float alpha(int what) {
        float c = (what >> 24) & 0xff;
        if (colorModeA == 255) {
            return c;
        }
        return (c / 255.0f) * colorModeA;
    }

    public final float red(int what) {
        float c = (what >> 16) & 0xff;
        if (colorModeDefault) {
            return c;
        }
        return (c / 255.0f) * colorModeX;
    }

    public final float green(int what) {
        float c = (what >> 8) & 0xff;
        if (colorModeDefault) {
            return c;
        }
        return (c / 255.0f) * colorModeY;
    }

    public final float blue(int what) {
        float c = (what) & 0xff;
        if (colorModeDefault) {
            return c;
        }
        return (c / 255.0f) * colorModeZ;
    }

    public final float hue(int what) {
        if (what != cacheHsbKey) {
            Color.RGBToHSV((what >> 16) & 0xff, (what >> 8) & 0xff, what & 0xff, cacheHsbValue);
            cacheHsbKey = what;
        }
        return (cacheHsbValue[0] / 360f) * colorModeX;
    }

    public final float saturation(int what) {
        if (what != cacheHsbKey) {
            Color.RGBToHSV((what >> 16) & 0xff, (what >> 8) & 0xff, what & 0xff, cacheHsbValue);
            cacheHsbKey = what;
        }
        return cacheHsbValue[1] * colorModeY;
    }

    public final float brightness(int what) {
        if (what != cacheHsbKey) {
            Color.RGBToHSV((what >> 16) & 0xff, (what >> 8) & 0xff, what & 0xff, cacheHsbValue);
            cacheHsbKey = what;
        }
        return cacheHsbValue[2] * colorModeZ;
    }

    /**
     * Interpolate between two colors, using the current color mode.
     */
    public int lerpColor(int c1, int c2, float amt) {
        return lerpColor(c1, c2, amt, colorMode);
    }

    /**
     * Interpolate between two colors. Like lerp(), but for the individual color
     * components of a color supplied as an int value.
     */
    public static int lerpColor(int c1, int c2, float amt, int mode) {
        if (mode == RGB) {
            float a1 = ((c1 >> 24) & 0xff);
            float r1 = (c1 >> 16) & 0xff;
            float g1 = (c1 >> 8) & 0xff;
            float b1 = c1 & 0xff;
            float a2 = (c2 >> 24) & 0xff;
            float r2 = (c2 >> 16) & 0xff;
            float g2 = (c2 >> 8) & 0xff;
            float b2 = c2 & 0xff;

            return (((int) (a1 + (a2 - a1) * amt) << 24) | ((int) (r1 + (r2 - r1) * amt) << 16) | ((int) (g1 + (g2 - g1) * amt) << 8) | ((int) (b1 + (b2 - b1) * amt)));

        } else if (mode == HSB) {
            if (lerpColorHSB1 == null) {
                lerpColorHSB1 = new float[3];
                lerpColorHSB2 = new float[3];
                lerpColorHSB3 = new float[3];
            }

            float a1 = (c1 >> 24) & 0xff;
            float a2 = (c2 >> 24) & 0xff;
            int alfa = ((int) (a1 + (a2 - a1) * amt)) << 24;

            Color.RGBToHSV((c1 >> 16) & 0xff, (c1 >> 8) & 0xff, c1 & 0xff, lerpColorHSB1);
            Color.RGBToHSV((c2 >> 16) & 0xff, (c2 >> 8) & 0xff, c2 & 0xff, lerpColorHSB2);

            lerpColorHSB3[0] = RainbowMath.lerp(lerpColorHSB1[0], lerpColorHSB2[0], amt);
            lerpColorHSB3[1] = RainbowMath.lerp(lerpColorHSB1[1], lerpColorHSB2[1], amt);
            lerpColorHSB3[2] = RainbowMath.lerp(lerpColorHSB1[2], lerpColorHSB2[2], amt);
            return Color.HSVToColor(alfa, lerpColorHSB3);
        }
        return 0;
    }

    /**
     * Record individual lines and triangles by echoing them to another
     * renderer.
     */
    public void beginRaw(RainbowGraphics rawGraphics) { // ignore
        this.raw = rawGraphics;
        rawGraphics.beginDraw();
    }

    /**
     * Prepares the PGraphics for drawing.
     * <p/>
     * When creating your own PGraphics, you should call this before drawing
     * anything.
     */
    public void beginDraw() { // ignore
    }

    public void endRaw() { // ignore
        if (raw != null) {
            flush();
            raw.endDraw();
            raw.dispose();
            raw = null;
        }
    }

    public void flush() {
        // no-op, mostly for P3D to write sorted stuff
    }

    /**
     * This will finalize rendering so that it can be shown on-screen.
     * <p/>
     * When creating your own PGraphics, you should call this when you're
     * finished drawing.
     */
    public void endDraw() { // ignore
    }

    /**
     * Handle any takedown for this graphics context.
     * <p/>
     * This is called when a sketch is shut down and this renderer was specified
     * using the size() command, or inside endRecord() and endRaw(), in order to
     * shut things off.
     */
    public void dispose() {
    }

    /**
     * Return true if this renderer should be drawn to the screen. Defaults to
     * returning true, since nearly all renderers are on-screen beasts. But can
     * be overridden for subclasses like PDF so that a window doesn't open up. <br/>
     * <br/>
     * A better name? showFrame, displayable, isVisible, visible, shouldDisplay,
     * what to call this?
     */
    public boolean displayable() {
        return true;
    }

    /**
     * Return true if this renderer does rendering through OpenGL. Defaults to
     * false.
     */
    public boolean isGL() {
        return false;
    }

}
