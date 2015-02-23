package com.juankysoriano.rainbow.core.drawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.net.Uri;

import com.juankysoriano.rainbow.core.extra.RainbowStyle;
import com.juankysoriano.rainbow.core.graphics.RainbowGraphics;
import com.juankysoriano.rainbow.core.graphics.RainbowGraphics2D;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;
import com.juankysoriano.rainbow.core.matrix.RMatrix;
import com.juankysoriano.rainbow.core.matrix.RMatrix2D;
import com.juankysoriano.rainbow.core.matrix.RMatrix3D;
import com.juankysoriano.rainbow.utils.RainbowBitmapUtils;
import com.juankysoriano.rainbow.utils.RainbowIO;

import java.io.File;

public class RainbowDrawer {

    private RainbowGraphics graphics;
    private int[] pixels; // this value will be null until loadPixels() has been called.
    private int width;
    private int height;
    private boolean drawing;

    public RainbowDrawer() {
    }

    /**
     * Interpolate between two colors. Like lerp(), but for the individual color
     * components of a color supplied as an int value.
     */
    public static int lerpColor(final int c1, final int c2, final float amt, final int mode) {
        return RainbowGraphics.lerpColor(c1, c2, amt, mode);
    }

    /**
     * Returns color resulting of blending two input colors given the blend mode.
     *
     * @param c1
     * @param c2
     * @param mode
     * @return
     */
    public static int blendColor(final int c1, final int c2, final int mode) {
        return RainbowImage.blendColor(c1, c2, mode);
    }

    public static RainbowGraphics createStandaloneGraphics(final int width, final int height) {
        RainbowGraphics pg = new RainbowGraphics2D();
        pg.setParent(null);
        pg.setPrimary(false);
        pg.setSize(width, height);

        return pg;
    }

    public int[] getPixels() {
        return pixels;
    }

    public RainbowGraphics getGraphics() {
        return graphics;
    }

    public void setGraphics(RainbowGraphics graphics) {
        this.graphics = graphics;
        this.width = graphics == null ? 0 : graphics.width;
        this.height = graphics == null ? 0 : graphics.height;
    }

    public int getWidth() {
        return graphics.width;
    }

    public int getHeight() {
        return graphics.height;
    }

    public boolean hasGraphics() {
        return graphics != null;
    }

    public RainbowGraphics createGraphics(final int iwidth, final int iheight) {
        RainbowGraphics pg = new RainbowGraphics2D();
        pg.setParent(graphics.parent);
        pg.setPrimary(false);
        pg.setSize(iwidth, iheight);

        return pg;
    }

    public RainbowImage createImage(final int wide, final int high, final int format) {
        final RainbowImage image = new RainbowImage(wide, high, format);
        image.parent = graphics.parent;
        return image;
    }

    /**
     * Intercepts any relative paths to make them absolute (relative to the
     * sketch folder) before passing to save() in PImage. (Changed in 0100)
     */
    public void save(final String filename) {
        graphics.save(getContext(), RainbowIO.savePath(getContext(), filename));
    }

    public Context getContext() {
        return graphics.parent.getContext();
    }

    public void loadImage(String path, int mode, RainbowImage.LoadPictureListener listener) {
        Bitmap bitmap = RainbowBitmapUtils.getBitmap(getContext(), path, width, height, mode);
        loadImage(bitmap, listener);
    }

    public void loadImage(Bitmap bitmap, RainbowImage.LoadPictureListener listener) {
        if (bitmap == null) {
            listener.onLoadFail();
        }
        final RainbowImage image = new RainbowImage(bitmap);
        image.parent = graphics.parent;
        listener.onLoadSucceed(image);
    }

    public void loadImage(String path, int width, int height, RainbowImage.LoadPictureListener listener) {
        Bitmap bitmap = RainbowBitmapUtils.getBitmap(getContext(), path, width, height);
        loadImage(bitmap, listener);
    }

    public void loadImage(String path, int width, int height, int mode, RainbowImage.LoadPictureListener listener) {
        Bitmap bitmap = RainbowBitmapUtils.getBitmap(getContext(), path, width, height, mode);
        loadImage(bitmap, listener);
    }

    public void loadImage(int resID, int mode, RainbowImage.LoadPictureListener listener) {
        final Bitmap bitmap = RainbowBitmapUtils.getBitmap(getContext(), resID, width, height, mode);
        loadImage(bitmap, listener);
    }

    public void loadImage(int resID, int width, int height, RainbowImage.LoadPictureListener listener) {
        final Bitmap bitmap = RainbowBitmapUtils.getBitmap(getContext(), resID, width, height);
        loadImage(bitmap, listener);
    }

    public void loadImage(int resID, int width, int height, int mode, RainbowImage.LoadPictureListener listener) {
        final Bitmap bitmap = RainbowBitmapUtils.getBitmap(getContext(), resID, width, height, mode);
        loadImage(bitmap, listener);
    }

    public void loadImage(File file, int mode, RainbowImage.LoadPictureListener listener) {
        final Bitmap bitmap = RainbowBitmapUtils.getBitmap(getContext(), file, width, height, mode);
        loadImage(bitmap, listener);
    }

    public void loadImage(File file, int width, int height, RainbowImage.LoadPictureListener listener) {
        final Bitmap bitmap = RainbowBitmapUtils.getBitmap(getContext(), file, width, height);
        loadImage(bitmap, listener);
    }

    public void loadImage(Uri uri, int mode, RainbowImage.LoadPictureListener listener) {
        final Bitmap bitmap = RainbowBitmapUtils.getBitmap(getContext(), uri, width, height, mode);
        loadImage(bitmap, listener);
    }

    public void loadImage(Uri uri, int width, int height, RainbowImage.LoadPictureListener listener) {
        final Bitmap bitmap = RainbowBitmapUtils.getBitmap(getContext(), uri, width, height);
        loadImage(bitmap, listener);
    }

    public final int color(int gray) {
        if (graphics == null) {
            if (gray > 255) {
                gray = 255;
            } else if (gray < 0) {
                gray = 0;
            }
            return 0xff000000 | (gray << 16) | (gray << 8) | gray;
        }
        return graphics.color(gray);
    }

    public final int color(final float fgray) {
        if (graphics == null) {
            int gray = (int) fgray;
            if (gray > 255) {
                gray = 255;
            } else if (gray < 0) {
                gray = 0;
            }
            return 0xff000000 | (gray << 16) | (gray << 8) | gray;
        }
        return graphics.color(fgray);
    }

    public final int color(final int gray, int alpha) {
        if (graphics == null) {
            if (alpha > 255) {
                alpha = 255;
            } else if (alpha < 0) {
                alpha = 0;
            }
            if (gray > 255) {
                // then assume this is actually a #FF8800
                return (alpha << 24) | (gray & 0xFFFFFF);
            } else {
                // if (gray > 255) gray = 255; else if (gray < 0) gray = 0;
                return (alpha << 24) | (gray << 16) | (gray << 8) | gray;
            }
        }
        return graphics.color(gray, alpha);
    }

    public final int color(final float fgray, final float falpha) {
        if (graphics == null) {
            int gray = (int) fgray;
            int alpha = (int) falpha;
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
            return 0xff000000 | (gray << 16) | (gray << 8) | gray;
        }
        return graphics.color(fgray, falpha);
    }

    public final int color(int x, int y, int z) {
        if (graphics == null) {
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
        return graphics.color(x, y, z);
    }

    public final int color(float x, float y, float z) {
        if (graphics == null) {
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

            return 0xff000000 | ((int) x << 16) | ((int) y << 8) | (int) z;
        }
        return graphics.color(x, y, z);
    }

    public final int color(int x, int y, int z, int a) {
        if (graphics == null) {
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
        return graphics.color(x, y, z, a);
    }

    public final int color(float x, float y, float z, float a) {
        if (graphics == null) {
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

            return ((int) a << 24) | ((int) x << 16) | ((int) y << 8) | (int) z;
        }
        return graphics.color(x, y, z, a);
    }

    /**
     * Override the g.pixels[] function to set the pixels[] array that's part of
     * the Imagine object. Allows the use of pixels[] in the code, rather than
     * g.pixels[].
     */
    public void loadPixels() {
        graphics.loadPixels();
        pixels = graphics.pixels;
    }

    public void updatePixels() {
        graphics.updatePixels();
    }

    public void updatePixels(final int x1, final int y1, final int x2, final int y2) {
        graphics.updatePixels(x1, y1, x2, y2);
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
    public void setCache(final RainbowImage image, final Object storage) {
        graphics.setCache(image, storage);
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
    public Object getCache(final RainbowImage image) {
        return graphics.getCache(image);
    }

    /**
     * Remove information associated with this renderer from the cache, if any.
     *
     * @param image The PGraphics renderer whose cache data should be removed
     */
    public void removeCache(final RainbowImage image) {
        graphics.removeCache(image);
    }

    protected void flush() {
        graphics.flush();
    }

    /**
     * Prepares rainbow sketch for draw.
     */
    public void beginDraw() {
        drawing = true;
        if (graphics != null) {
            graphics.beginDraw();
        }
    }

    public boolean isDrawing() {
        return drawing;
    }

    /**
     * Ends synchronous draw. Makes drawing effective
     */
    public void endDraw() {
        if (graphics != null) {
            graphics.endDraw();
        }
        drawing = false;
    }

    /**
     * Start a new shape of type POLYGON
     */
    public void beginShape() {
        graphics.beginShape();
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
    public void beginShape(final int kind) {
        graphics.beginShape(kind);
    }

    /**
     * Sets whether the upcoming vertex is part of an edge. Equivalent to
     * glEdgeFlag(), for people familiar with OpenGL.
     */
    public void edge(final boolean edge) {
        graphics.edge(edge);
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
    public void normal(final float nx, final float ny, final float nz) {
        graphics.normal(nx, ny, nz);
    }

    /**
     * Set texture mode to either to use coordinates based on the IMAGE (more
     * intuitive for new users) or NORMALIZED (better for advanced chaps)
     */
    public void textureMode(final int mode) {
        graphics.textureMode(mode);
    }

    public void textureWrap(final int wrap) {
        graphics.textureWrap(wrap);
    }

    /**
     * Set texture image for current shape. Needs to be called between @see
     * beginShape and @see endShape
     *
     * @param image reference to a PImage object
     */
    public void texture(final RainbowImage image) {
        graphics.texture(image);
    }

    /**
     * Removes texture image for current shape. Needs to be called between @see
     * beginShape and @see endShape
     */
    public void noTexture() {
        graphics.noTexture();
    }

    public void vertex(final float x, final float y) {
        graphics.vertex(x, y);
    }

    public void vertex(final float x, final float y, final float z) {
        graphics.vertex(x, y, z);
    }

    /**
     * Used by renderer subclasses or PShape to efficiently pass in already
     * formatted vertex information.
     *
     * @param v vertex parameters, as a float array of length
     *          VERTEX_FIELD_COUNT
     */
    public void vertex(final float[] v) {
        graphics.vertex(v);
    }

    public void vertex(final float x, final float y, final float u, final float v) {
        graphics.vertex(x, y, u, v);
    }

    public void vertex(final float x, final float y, final float z, final float u, final float v) {
        graphics.vertex(x, y, z, u, v);
    }

    /**
     * This feature is in testing, do not use or rely upon its implementation
     */
    public void breakShape() {
        graphics.breakShape();
    }

    public void beginContour() {
        graphics.beginContour();
    }

    public void endContour() {
        graphics.endContour();
    }

    public void endShape() {
        graphics.endShape();
    }

    public void endShape(final int mode) {
        graphics.endShape(mode);
    }

    public void clip(final float a, final float b, final float c, final float d) {
        graphics.clip(a, b, c, d);
    }

    public void noClip() {
        graphics.noClip();
    }

    public void blendMode(final int mode) {
        graphics.blendMode(mode);
    }

    public void bezierVertex(final float x2, final float y2, final float x3, final float y3, final float x4, final float y4) {
        graphics.bezierVertex(x2, y2, x3, y3, x4, y4);
    }

    public void bezierVertex(final float x2, final float y2, final float z2, final float x3, final float y3, final float z3, final float x4, final float y4, final float z4) {
        graphics.bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    public void quadraticVertex(final float cx, final float cy, final float x3, final float y3) {
        graphics.quadraticVertex(cx, cy, x3, y3);
    }

    public void quadraticVertex(final float cx, final float cy, final float cz, final float x3, final float y3, final float z3) {
        graphics.quadraticVertex(cx, cy, cz, x3, y3, z3);
    }

    public void curveVertex(final float x, final float y) {
        graphics.curveVertex(x, y);
    }

    public void curveVertex(final float x, final float y, final float z) {
        graphics.curveVertex(x, y, z);
    }

    public void point(final float x, final float y) {
        graphics.point(x, y);
    }

    public void point(final float x, final float y, final float z) {
        graphics.point(x, y, z);
    }

    public void line(final float x1, final float y1, final float x2, final float y2) {
        graphics.line(x1, y1, x2, y2);
    }

    public void line(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {
        graphics.line(x1, y1, z1, x2, y2, z2);
    }

    /**
     * Explores a imaginary line in order to seek for line points. When a line point is found a listener is notified.
     *
     * @param x1       start x
     * @param y1       start y
     * @param x2       end x
     * @param y2       end y
     * @param listener PointDetectedListener which will apply a operation over the identified point
     */
    public void exploreLine(final float x1, final float y1, final float x2, final float y2, final PointDetectedListener listener) {
        LineExplorer lineExplorer = new LineExplorer();
        lineExplorer.exploreLine(x1, y1, x2, y2, this, listener);
    }

    public void triangle(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3) {
        graphics.triangle(x1, y1, x2, y2, x3, y3);
    }

    public void quad(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3, final float x4, final float y4) {
        graphics.quad(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public void rectMode(final int mode) {
        graphics.rectMode(mode);
    }

    public void rect(final float a, final float b, final float c, final float d) {
        graphics.rect(a, b, c, d);
    }

    public void rect(final float a, final float b, final float c, final float d, final float r) {
        graphics.rect(a, b, c, d, r);
    }

    public void rect(final float a, final float b, final float c, final float d, final float tl, final float tr, final float br, final float bl) {
        graphics.rect(a, b, c, d, tl, tr, br, bl);
    }

    public void ellipseMode(final int mode) {
        graphics.ellipseMode(mode);
    }

    public void ellipse(final float a, final float b, final float c, final float d) {
        graphics.ellipse(a, b, c, d);
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
    public void arc(final float a, final float b, final float c, final float d, final float start, final float stop) {
        graphics.arc(a, b, c, d, start, stop);
    }

    public void arc(final float a, final float b, final float c, final float d, final float start, final float stop, final int mode) {
        graphics.arc(a, b, c, d, start, stop, mode);
    }

    public void box(final float size) {
        graphics.box(size);
    }

    public void box(final float w, final float h, final float d) {
        graphics.box(w, h, d);
    }

    public void sphereDetail(final int res) {
        graphics.sphereDetail(res);
    }

    /**
     * Set the detail level for approximating a sphere. The ures and vres params
     * control the horizontal and vertical resolution.
     * <p/>
     * Code for sphereDetail() submitted by toxi [031031]. Code for enhanced u/v
     * version from davbol [080801].
     */
    public void sphereDetail(final int ures, final int vres) {
        graphics.sphereDetail(ures, vres);
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
    public void sphere(final float r) {
        graphics.sphere(r);
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
    public float bezierPoint(final float a, final float b, final float c, final float d, final float t) {
        return graphics.bezierPoint(a, b, c, d, t);
    }

    /**
     * Provide the tangent at the given point on the bezier curve. Fix from
     * davbol for 0136.
     */
    public float bezierTangent(final float a, final float b, final float c, final float d, final float t) {
        return graphics.bezierTangent(a, b, c, d, t);
    }

    public void bezierDetail(final int detail) {
        graphics.bezierDetail(detail);
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
    public void bezier(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3, final float x4, final float y4) {
        graphics.bezier(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public void bezier(final float x1,
                       final float y1,
                       final float z1,
                       final float x2,
                       final float y2,
                       final float z2,
                       final float x3,
                       final float y3,
                       final float z3,
                       final float x4,
                       final float y4,
                       final float z4) {
        graphics.bezier(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    /**
     * Get a location along a catmull-rom curve segment.
     *
     * @param t Value between zero and one for how far along the segment
     */
    public float curvePoint(final float a, final float b, final float c, final float d, final float t) {
        return graphics.curvePoint(a, b, c, d, t);
    }

    /**
     * Calculate the tangent at a t value (0..1) on a Catmull-Rom curve. Code
     * thanks to Dave Bollinger (Bug #715)
     */
    public float curveTangent(final float a, final float b, final float c, final float d, final float t) {
        return graphics.curveTangent(a, b, c, d, t);
    }

    public void curveDetail(final int detail) {
        graphics.curveDetail(detail);
    }

    public void curveTightness(final float tightness) {
        graphics.curveTightness(tightness);
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
    public void curve(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3, final float x4, final float y4) {
        graphics.curve(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public void curve(final float x1,
                      final float y1,
                      final float z1,
                      final float x2,
                      final float y2,
                      final float z2,
                      final float x3,
                      final float y3,
                      final float z3,
                      final float x4,
                      final float y4,
                      final float z4) {
        graphics.curve(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    /**
     * If true in PImage, use bilinear interpolation for copy() operations. When
     * inherited by PGraphics, also controls shapes.
     */
    public void smooth() {
        graphics.smooth();
    }

    public void smooth(final int level) {
        graphics.smooth(level);
    }

    /**
     * Disable smoothing. See smooth().
     */
    public void noSmooth() {
        graphics.noSmooth();
    }

    /**
     * The mode can only be set to CORNERS, CORNER, and CENTER.
     * <p/>
     * Support for CENTER was added in release 0146.
     */
    public void imageMode(final int mode) {
        graphics.imageMode(mode);
    }

    public void image(final RainbowImage image, final float x, final float y) {
        graphics.image(image, x, y);
    }

    /**
     * Draw an image(), also specifying u/v coordinates. In this method, the u,
     * v coordinates are always based on image space location, regardless of the
     * current textureMode().
     */
    public void image(final RainbowImage image, final float a, final float b, final float c, final float d, final int u1, final int v1, final int u2, final int v2) {
        graphics.image(image, a, b, c, d, u1, v1, u2, v2);
    }

    /**
     * Push a copy of the current transformation matrix onto the stack.
     */
    public void pushMatrix() {
        graphics.pushMatrix();
    }

    /**
     * Replace the current transformation matrix with the top of the stack.
     */
    public void popMatrix() {
        graphics.popMatrix();
    }

    /**
     * Translate in X and Y.
     */
    public void translate(final float tx, final float ty) {
        graphics.translate(tx, ty);
    }

    /**
     * Translate in X, Y, and Z.
     */
    public void translate(final float tx, final float ty, final float tz) {
        graphics.translate(tx, ty, tz);
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
    public void rotate(final float angle) {
        graphics.rotate(angle);
    }

    /**
     * Rotate around the X axis.
     */
    public void rotateX(final float angle) {
        graphics.rotateX(angle);
    }

    /**
     * Rotate around the Y axis.
     */
    public void rotateY(final float angle) {
        graphics.rotateY(angle);
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
    public void rotateZ(final float angle) {
        graphics.rotateZ(angle);
    }

    /**
     * Rotate about a vector in space. Same as the glRotatef() function.
     */
    public void rotate(final float angle, final float vx, final float vy, final float vz) {
        graphics.rotate(angle, vx, vy, vz);
    }

    /**
     * Scale in all dimensions.
     */
    public void scale(final float s) {
        graphics.scale(s);
    }

    /**
     * Scale in X and Y. Equivalent to scale(sx, sy, 1).
     * <p/>
     * Not recommended for use in 3D, because the z-dimension is just scaled by
     * 1, since there's no way to know what else to scale it by.
     */
    public void scale(final float sx, final float sy) {
        graphics.scale(sx, sy);
    }

    /**
     * Scale in X, Y, and Z.
     */
    public void scale(final float x, final float y, final float z) {
        graphics.scale(x, y, z);
    }

    /**
     * Shear along X axis
     */
    public void shearX(final float angle) {
        graphics.shearX(angle);
    }

    /**
     * Skew along Y axis
     */
    public void shearY(final float angle) {
        graphics.shearY(angle);
    }

    /**
     * Set the current transformation matrix to identity.
     */
    public void resetMatrix() {
        graphics.resetMatrix();
    }

    public void applyMatrix(final RMatrix source) {
        graphics.applyMatrix(source);
    }

    public void applyMatrix(final RMatrix2D source) {
        graphics.applyMatrix(source);
    }

    /**
     * Apply a 3x2 affine transformation matrix.
     */
    public void applyMatrix(final float n00, final float n01, final float n02, final float n10, final float n11, final float n12) {
        graphics.applyMatrix(n00, n01, n02, n10, n11, n12);
    }

    public void applyMatrix(final RMatrix3D source) {
        graphics.applyMatrix(source);
    }

    /**
     * Apply a 4x4 transformation matrix.
     */
    public void applyMatrix(final float n00,
                            final float n01,
                            final float n02,
                            final float n03,
                            final float n10,
                            final float n11,
                            final float n12,
                            final float n13,
                            final float n20,
                            final float n21,
                            final float n22,
                            final float n23,
                            final float n30,
                            final float n31,
                            final float n32,
                            final float n33) {
        graphics.applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
    }

    public RMatrix getMatrix() {
        return graphics.getMatrix();
    }

    /**
     * Set the current transformation to the contents of the specified source.
     */
    public void setMatrix(final RMatrix3D source) {
        graphics.setMatrix(source);
    }

    /**
     * Set the current transformation matrix to the contents of another.
     */
    public void setMatrix(final RMatrix source) {
        graphics.setMatrix(source);
    }

    /**
     * Set the current transformation to the contents of the specified source.
     */
    public void setMatrix(final RMatrix2D source) {
        graphics.setMatrix(source);
    }

    /**
     * Copy the current transformation matrix into the specified target. Pass in
     * null to create a new matrix.
     */
    public RMatrix2D getMatrix(final RMatrix2D target) {
        return graphics.getMatrix(target);
    }

    /**
     * Copy the current transformation matrix into the specified target. Pass in
     * null to create a new matrix.
     */
    public RMatrix3D getMatrix(final RMatrix3D target) {
        return graphics.getMatrix(target);
    }

    /**
     * Print the current model (or "transformation") matrix.
     */
    public void printMatrix() {
        graphics.printMatrix();
    }

    public void beginCamera() {
        graphics.beginCamera();
    }

    public void endCamera() {
        graphics.endCamera();
    }

    public void camera() {
        graphics.camera();
    }

    public void camera(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ) {
        graphics.camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public void printCamera() {
        graphics.printCamera();
    }

    public void ortho() {
        graphics.ortho();
    }

    public void ortho(final float left, final float right, final float bottom, final float top) {
        graphics.ortho(left, right, bottom, top);
    }

    public void ortho(final float left, final float right, final float bottom, final float top, final float near, final float far) {
        graphics.ortho(left, right, bottom, top, near, far);
    }

    public void perspective() {
        graphics.perspective();
    }

    public void perspective(final float fovy, final float aspect, final float zNear, final float zFar) {
        graphics.perspective(fovy, aspect, zNear, zFar);
    }

    public void frustum(final float left, final float right, final float bottom, final float top, final float near, final float far) {
        graphics.frustum(left, right, bottom, top, near, far);
    }

    public void printProjection() {
        graphics.printProjection();
    }

    /**
     * Given an x and y coordinate, returns the x position of where that point
     * would be placed on screen, once affected by translate(), scale(), or any
     * other transformations.
     */
    public float screenX(final float x, final float y) {
        return graphics.screenX(x, y);
    }

    /**
     * Given an x and y coordinate, returns the y position of where that point
     * would be placed on screen, once affected by translate(), scale(), or any
     * other transformations.
     */
    public float screenY(final float x, final float y) {
        return graphics.screenY(x, y);
    }

    /**
     * Maps a three dimensional point to its placement on-screen.
     * <p/>
     * Given an (x, y, z) coordinate, returns the x position of where that point
     * would be placed on screen, once affected by translate(), scale(), or any
     * other transformations.
     */
    public float screenX(final float x, final float y, final float z) {
        return graphics.screenX(x, y, z);
    }

    /**
     * Maps a three dimensional point to its placement on-screen.
     * <p/>
     * Given an (x, y, z) coordinate, returns the y position of where that point
     * would be placed on screen, once affected by translate(), scale(), or any
     * other transformations.
     */
    public float screenY(final float x, final float y, final float z) {
        return graphics.screenY(x, y, z);
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
    public float screenZ(final float x, final float y, final float z) {
        return graphics.screenZ(x, y, z);
    }

    /**
     * Returns the model space x value for an x, y, z coordinate.
     * <p/>
     * This will give you a coordinate after it has been transformed by
     * translate(), rotate(), and camera(), but not yet transformed by the
     * projection matrix. For instance, his can be useful for figuring out how
     * points in 3D space relate to the edge coordinates of a shape.
     */
    public float modelX(final float x, final float y, final float z) {
        return graphics.modelX(x, y, z);
    }

    /**
     * Returns the model space y value for an x, y, z coordinate.
     */
    public float modelY(final float x, final float y, final float z) {
        return graphics.modelY(x, y, z);
    }

    /**
     * Returns the model space z value for an x, y, z coordinate.
     */
    public float modelZ(final float x, final float y, final float z) {
        return graphics.modelZ(x, y, z);
    }

    public void pushStyle() {
        graphics.pushStyle();
    }

    public void popStyle() {
        graphics.popStyle();
    }

    public void style(final RainbowStyle s) {
        graphics.style(s);
    }

    public void strokeWeight(final float weight) {
        graphics.strokeWeight(weight);
    }

    public void strokeJoin(final int join) {
        graphics.strokeJoin(join);
    }

    public void strokeCap(final int cap) {
        graphics.strokeCap(cap);
    }

    public void noStroke() {
        graphics.noStroke();
    }

    public void fillShader(Shader shader) {
        if (graphics instanceof RainbowGraphics2D) {
            ((RainbowGraphics2D) graphics).getFillPaint().setShader(shader);
        }
    }

    public void strokeShader(Shader shader) {
        if (graphics instanceof RainbowGraphics2D) {
            ((RainbowGraphics2D) graphics).getStrokePaint().setShader(shader);
        }
    }

    /**
     * Set the tint to either a grayscale or ARGB value. See notes attached to
     * the fill() function.
     */
    public void stroke(final int rgb) {
        graphics.stroke(rgb);
    }

    public void stroke(final int rgb, final float alpha) {
        graphics.stroke(rgb, alpha);
    }

    public void stroke(final float gray) {
        graphics.stroke(gray);
    }

    public void stroke(final float gray, final float alpha) {
        graphics.stroke(gray, alpha);
    }

    public void stroke(final float x, final float y, final float z) {
        graphics.stroke(x, y, z);
    }

    public void stroke(final float x, final float y, final float z, final float a) {
        graphics.stroke(x, y, z, a);
    }

    public void noTint() {
        graphics.noTint();
    }

    /**
     * Set the tint to either a grayscale or ARGB value.
     */
    public void tint(final int rgb) {
        graphics.tint(rgb);
    }

    public void tint(final int rgb, final float alpha) {
        graphics.tint(rgb, alpha);
    }

    public void tint(final float gray) {
        graphics.tint(gray);
    }

    public void tint(final float gray, final float alpha) {
        graphics.tint(gray, alpha);
    }

    public void tint(final float x, final float y, final float z) {
        graphics.tint(x, y, z);
    }

    public void tint(final float x, final float y, final float z, final float a) {
        graphics.tint(x, y, z, a);
    }

    public void noFill() {
        graphics.noFill();
    }

    /**
     * Set the fill to either a grayscale value or an ARGB int.
     */
    public void fill(final int rgb) {
        graphics.fill(rgb);
    }

    public void fill(final int rgb, final float alpha) {
        graphics.fill(rgb, alpha);
    }

    public void fill(final float gray) {
        graphics.fill(gray);
    }

    public void fill(final float gray, final float alpha) {
        graphics.fill(gray, alpha);
    }

    public void fill(final float x, final float y, final float z) {
        graphics.fill(x, y, z);
    }

    public void fill(final float x, final float y, final float z, final float a) {
        graphics.fill(x, y, z, a);
    }

    public void ambient(final int rgb) {
        graphics.ambient(rgb);
    }

    public void ambient(final float gray) {
        graphics.ambient(gray);
    }

    public void ambient(final float x, final float y, final float z) {
        graphics.ambient(x, y, z);
    }

    public void specular(final int rgb) {
        graphics.specular(rgb);
    }

    public void specular(final float gray) {
        graphics.specular(gray);
    }

    public void specular(final float x, final float y, final float z) {
        graphics.specular(x, y, z);
    }

    public void shininess(final float shine) {
        graphics.shininess(shine);
    }

    public void emissive(final int rgb) {
        graphics.emissive(rgb);
    }

    public void emissive(final float gray) {
        graphics.emissive(gray);
    }

    public void emissive(final float x, final float y, final float z) {
        graphics.emissive(x, y, z);
    }

    public void lights() {
        graphics.lights();
    }

    public void noLights() {
        graphics.noLights();
    }

    public void ambientLight(final float red, final float green, final float blue) {
        graphics.ambientLight(red, green, blue);
    }

    public void ambientLight(final float red, final float green, final float blue, final float x, final float y, final float z) {
        graphics.ambientLight(red, green, blue, x, y, z);
    }

    public void directionalLight(final float red, final float green, final float blue, final float nx, final float ny, final float nz) {
        graphics.directionalLight(red, green, blue, nx, ny, nz);
    }

    public void pointLight(final float red, final float green, final float blue, final float x, final float y, final float z) {
        graphics.pointLight(red, green, blue, x, y, z);
    }

    public void spotLight(final float red,
                          final float green,
                          final float blue,
                          final float x,
                          final float y,
                          final float z,
                          final float nx,
                          final float ny,
                          final float nz,
                          final float angle,
                          final float concentration) {
        graphics.spotLight(red, green, blue, x, y, z, nx, ny, nz, angle, concentration);
    }

    public void lightFalloff(final float constant, final float linear, final float quadratic) {
        graphics.lightFalloff(constant, linear, quadratic);
    }

    public void lightSpecular(final float x, final float y, final float z) {
        graphics.lightSpecular(x, y, z);
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
    public void background(final int rgb) {
        graphics.background(rgb);
    }

    /**
     * See notes about alpha in background(x, y, z, a).
     */
    public void background(final int rgb, final float alpha) {
        graphics.background(rgb, alpha);
    }

    /**
     * Set the background to a grayscale value, based on the current colorMode.
     */
    public void background(final float gray) {
        graphics.background(gray);
    }

    /**
     * See notes about alpha in background(x, y, z, a).
     */
    public void background(final float gray, final float alpha) {
        graphics.background(gray, alpha);
    }

    /**
     * Set the background to an r, g, b or h, s, b value, based on the current
     * colorMode.
     */
    public void background(final float x, final float y, final float z) {
        graphics.background(x, y, z);
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
    public void background(final float x, final float y, final float z, final float a) {
        graphics.background(x, y, z, a);
    }

    public void clear() {
        graphics.clear();
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
    public void background(final RainbowImage image) {
        image(image, 0, 0, width, height);
    }

    public void image(final RainbowImage image, final float x, final float y, final float c, final float d) {
        graphics.image(image, x, y, c, d);
    }

    public void colorMode(final int mode) {
        graphics.colorMode(mode);
    }

    public void colorMode(final int mode, final float max) {
        graphics.colorMode(mode, max);
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
    public void colorMode(final int mode, final float maxX, final float maxY, final float maxZ) {
        graphics.colorMode(mode, maxX, maxY, maxZ);
    }

    public void colorMode(final int mode, final float maxX, final float maxY, final float maxZ, final float maxA) {
        graphics.colorMode(mode, maxX, maxY, maxZ, maxA);
    }

    public final float alpha(final int what) {
        return graphics.alpha(what);
    }

    public final float red(final int what) {
        return graphics.red(what);
    }

    public final float green(final int what) {
        return graphics.green(what);
    }

    public final float blue(final int what) {
        return graphics.blue(what);
    }

    public final float hue(final int what) {
        return graphics.hue(what);
    }

    public final float saturation(final int what) {
        return graphics.saturation(what);
    }

    public final float brightness(final int what) {
        return graphics.brightness(what);
    }

    /**
     * Grab a subsection of a PImage, and copy it into a fresh PImage. As of
     * release 0149, no longer honors imageMode() for the coordinates.
     */

    /**
     * Interpolate between two colors, using the current color mode.
     */
    public int lerpColor(final int c1, final int c2, final float amt) {
        return graphics.lerpColor(c1, c2, amt);
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
        return graphics.displayable();
    }

    /**
     * Return true if this renderer does rendering through OpenGL. Defaults to
     * false.
     */
    public boolean isGL() {
        return graphics.isGL();
    }

    /**
     * Returns the native Bitmap object for this PImage.
     */
    public Object getNative() {
        return graphics.getNative();
    }

    /**
     * Returns an ARGB "color" type (a packed 32 bit int with the color. If the
     * coordinate is outside the image, zero is returned (black, but completely
     * transparent).
     * <p/>
     * If the image is in RGB format (i.e. on a PVideo object), the value will
     * get its high bits set, just to avoid cases where they haven't been set
     * already.
     * <p/>
     * If the image is in ALPHA format, this returns a white with its alpha
     * value set.
     * <p/>
     * This function is included primarily for beginners. It is quite slow
     * because it has to check to see if the x, y that was provided is inside
     * the bounds, and then has to check to see what image type it is. If you
     * want things to be more efficient, access the pixels[] array directly.
     */
    public int get(final int x, final int y) {
        return graphics.get(x, y);
    }

    /**
     * @param w width of pixel rectangle to get
     * @param h height of pixel rectangle to get
     */
    public RainbowImage get(final int x, final int y, final int w, final int h) {
        return graphics.get(x, y, w, h);
    }

    /**
     * Returns a copy of this PImage. Equivalent to get(0, 0, width, height).
     */
    public RainbowImage get() {
        return graphics.get();
    }

    /**
     * Set a single pixel to the specified color.
     */
    public void set(final int x, final int y, final int c) {
        graphics.set(x, y, c);
    }

    /**
     * Efficient method of drawing an image's pixels directly to this surface.
     * No variations are employed, meaning that any scale, tint, or imageMode
     * settings will be ignored.
     */
    public void set(final int x, final int y, final RainbowImage img) {
        graphics.set(x, y, img);
    }

    /**
     * Set alpha channel for an image. Black colors in the source image will
     * make the destination image completely transparent, and white will make
     * things fully opaque. Gray values will be in-between steps.
     * <p/>
     * Strictly speaking the "blue" value from the source image is used as the
     * alpha color. For a fully grayscale image, this is correct, but for a
     * color image it's not 100% accurate. For a more accurate conversion, first
     * use filter(GRAY) which will make the image into a "correct" grayscake by
     * performing a proper luminance-based conversion.
     */
    public void mask(final int alpha[]) {
        graphics.mask(alpha);
    }

    /**
     * Set alpha channel for an image using another image as the source.
     */
    public void mask(final RainbowImage alpha) {
        graphics.mask(alpha);
    }

    /**
     * Method to apply a variety of basic filters to this image.
     * <p/>
     * <UL>
     * <LI>filter(BLUR) provides a basic blur.
     * <LI>filter(GRAY) converts the image to grayscale based on luminance.
     * <LI>filter(INVERT) will invert the color components in the image.
     * <LI>filter(OPAQUE) set all the high bits in the image to opaque
     * <LI>filter(THRESHOLD) converts the image to black and white.
     * <LI>filter(DILATE) grow white/light areas
     * <LI>filter(ERODE) shrink white/light areas
     * </UL>
     * Luminance conversion code contributed by <A
     * HREF="http://www.toxi.co.uk">toxi</A>
     * <p/>
     * Gaussian blur code contributed by <A
     * HREF="http://incubator.quasimondo.com">Mario Klingemann</A>
     */
    public void filter(final int kind) {
        graphics.filter(kind);
    }

    /**
     * Method to apply a variety of basic filters to this image. These filters
     * all take a parameter.
     * <p/>
     * <UL>
     * <LI>filter(BLUR, int radius) performs a gaussian blur of the specified
     * radius.
     * <LI>filter(POSTERIZE, int levels) will posterize the image to between 2
     * and 255 levels.
     * <LI>filter(THRESHOLD, float center) allows you to set the center point
     * for the threshold. It takes a value from 0 to 1.0.
     * </UL>
     * Gaussian blur code contributed by <A
     * HREF="http://incubator.quasimondo.com">Mario Klingemann</A> and later
     * updated by toxi for better speed.
     */
    public void filter(final int kind, final float param) {
        graphics.filter(kind, param);
    }

    /**
     * Copy things from one area of this image to another area in the same
     * image.
     */
    public void copy(final int sx, final int sy, final int sw, final int sh, final int dx, final int dy, final int dw, final int dh) {
        graphics.copy(sx, sy, sw, sh, dx, dy, dw, dh);
    }

    /**
     * Copies area of one image into another PImage object.
     */
    public void copy(final RainbowImage src, final int sx, final int sy, final int sw, final int sh, final int dx, final int dy, final int dw, final int dh) {
        graphics.copy(src, sx, sy, sw, sh, dx, dy, dw, dh);
    }

    /**
     * Blends one area of this image to another area.
     *
     * @see com.juankysoriano.rainbow.core.graphics.RainbowImage#blendColor(int, int, int)
     */
    public void blend(final int sx, final int sy, final int sw, final int sh, final int dx, final int dy, final int dw, final int dh, final int mode) {
        graphics.blend(sx, sy, sw, sh, dx, dy, dw, dh, mode);
    }

    /**
     * Copies area of one image into another PImage object.
     *
     * @see com.juankysoriano.rainbow.core.graphics.RainbowImage#blendColor(int, int, int)
     */
    public void blend(final RainbowImage src, final int sx, final int sy, final int sw, final int sh, final int dx, final int dy, final int dw, final int dh, final int mode) {
        graphics.blend(src, sx, sy, sw, sh, dx, dy, dw, dh, mode);
    }

    public interface PointDetectedListener {
        void onPointDetected(float x, float y, RainbowDrawer rainbowDrawer);
    }

}
