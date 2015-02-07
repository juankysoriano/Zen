/* -*- mode: java; c-basic-offset: 2; indent-tabs-mode: nil -*- */

/*
 Part of the Processing project - http://processing.org

 Copyright (c) 2005-10 Ben Fry and Casey Reas

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

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.juankysoriano.rainbow.core.matrix.RMatrix;
import com.juankysoriano.rainbow.core.matrix.RMatrix2D;
import com.juankysoriano.rainbow.core.matrix.RMatrix3D;
import com.juankysoriano.rainbow.utils.RainbowBitmapUtils;
import com.juankysoriano.rainbow.utils.RainbowMath;

/**
 * Subclass for PGraphics that implements the graphics API using the Android 2D
 * graphics model. Similar tradeoffs to ANDROID2D mode with the original
 * (desktop) version of Processing.
 */
public class RainbowGraphics2D extends RainbowGraphics {

    /**
     * The temporary path object that does most of the drawing work. If there
     * are any points in the path (meaning that moveto has been called), then
     * vertexCount will be 1 (or more). In the POLYGON case, vertexCount is only
     * set to 1 after the first point is drawn (to indicate a moveto) and not
     * incremented after, since the variable isn't used for POLYGON paths.
     */
    private final Path path;
    private final float[] transform;
    /**
     * Temporary rectangle object.
     */
    private final RectF rect;
    private boolean breakShape;
    private float[] screenPoint;
    /**
     * coordinates for internal curve calculation
     */
    private float[] curveCoordX;
    private float[] curveCoordY;
    private float[] curveDrawX;
    private float[] curveDrawY;
    private Rect imageImplSrcRect;
    private RectF imageImplDstRect;
    private Paint tintPaint;
    private Paint strokePaint;
    private Paint fillPaint;
    private Paint clearingFillPaint;
    private boolean isFillClear = false;

    private Bitmap normalBitmap;
    private Bitmap backgroundBitmap;
    private Canvas canvas;
    private Shader backgroundBitmapShader;

    public RainbowGraphics2D() {
        transform = new float[9];
        path = new Path();
        rect = new RectF();
        initPaints();
    }

    /**
     * Called in response to a resize event, handles setting the new width and
     * height internally, as well as re-allocating the pixel buffer for the new
     * size.
     * <p/>
     * Note that this will nuke any cameraMode() settings.
     */
    @Override
    public void setSize(int iwidth, int iheight) { // ignore
        width = iwidth;
        height = iheight;

        allocate();
        reapplySettings();
    }

    @Override
    protected void allocate() {
        initBitmaps();
        initPaints();
        if(primarySurface) {
            initShaders();
        }
    }

    private void initBitmaps() {
        normalBitmap = Bitmap.createBitmap(width, height, Config.ARGB_4444);

        if(primarySurface) {
            backgroundBitmap = RainbowBitmapUtils.getBitmap(parent.getDrawingView().getBackground());
            if (backgroundBitmap == null) {
                backgroundBitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
            }
        }

        canvas = new Canvas(normalBitmap);

        if(primarySurface) {
            Drawable parentBackground = parent.getDrawingView().getBackground();
            if (parentBackground != null) {
                parentBackground.setBounds(0, 0, width, height);
                parentBackground.draw(canvas);
            }
        }

        super.setBitmap(normalBitmap);
    }

    private void initPaints() {
        fillPaint = new Paint();
        fillPaint.setStyle(Style.FILL);
        strokePaint = new Paint();
        strokePaint.setStyle(Style.STROKE);
        tintPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        clearingFillPaint = new Paint();
        clearingFillPaint.setStyle(Style.FILL);
    }

    private void initShaders() {
        backgroundBitmapShader = new BitmapShader(backgroundBitmap,
                BitmapShader.TileMode.REPEAT,
                BitmapShader.TileMode.REPEAT);
        clearingFillPaint.setShader(backgroundBitmapShader);
    }

    @Override
    public void dispose() {
        normalBitmap = null;
        backgroundBitmap = null;
        canvas = null;
        backgroundBitmapShader = null;
    }

    @Override
    public Bitmap getBitmap() {
        return super.getBitmap();
    }

    @Override
    public synchronized void beginDraw() {
        checkSettings();
        resetMatrix();
        vertexCount = 0;
    }

    @Override
    public void resetMatrix() {
        getCanvas().setMatrix(new android.graphics.Matrix());
    }

    Canvas getCanvas() {
        return canvas;
    }

    @Override
    public synchronized void endDraw() {
        if (primarySurface) {
            Canvas screen = parent.getDrawingView().lockCanvas();
            if(screen != null) {
                android.graphics.Matrix matrix = new android.graphics.Matrix();
                screen.drawBitmap(normalBitmap, matrix, null);
                parent.getDrawingView().unlockCanvasAndPost(screen);
            }
        } else {
            loadPixels();
        }

        setModified();
        super.updatePixels();
    }

    @Override
    public void loadPixels() {
        if ((pixels == null) || (pixels.length != width * height)) {
            pixels = new int[width * height];
        }
        normalBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
    }

    @Override
    public void beginShape(int kind) {
        shape = kind;
        vertexCount = 0;
        curveVertexCount = 0;
    }

    @Override
    public void texture(RainbowImage image) {
        showMethodWarning("texture");
    }

    @Override
    public void vertex(float x, float y) {
        if (shape == POLYGON) {
            vertexPolygon(x, y);
        } else {
            curveVertexCount = 0;

            if (vertexCount == vertices.length) {
                float temp[][] = new float[vertexCount << 1][VERTEX_FIELD_COUNT];
                System.arraycopy(vertices, 0, temp, 0, vertexCount);
                vertices = temp;
            }

            vertices[vertexCount][X] = x;
            vertices[vertexCount][Y] = y;
            vertexCount++;

            switch (shape) {
                case POINTS:
                    break;
                case LINES:
                    vertexLines(x, y);
                    break;
                case TRIANGLES:
                    vertexTriangles(x, y);
                    break;
                case TRIANGLE_STRIP:
                    vertexTriangleStrip(x, y);
                    break;
                case TRIANGLE_FAN:
                    vertexTriangleFan(x, y);
                    break;
                case QUAD:
                case QUADS:
                    vertexQuads(x, y);
                    break;
                case QUAD_STRIP:
                    vertexQuadStrip(x, y);
                    break;
            }
        }
    }

    private void vertexQuadStrip(float x, float y) {
        if ((vertexCount >= 4) && ((vertexCount % 2) == 0)) {
            quad(
                    vertices[vertexCount - 4][X],
                    vertices[vertexCount - 4][Y],
                    vertices[vertexCount - 2][X],
                    vertices[vertexCount - 2][Y],
                    x,
                    y,
                    vertices[vertexCount - 3][X],
                    vertices[vertexCount - 3][Y]);
        }
    }

    private void vertexQuads(float x, float y) {
        if ((vertexCount % 4) == 0) {
            quad(
                    vertices[vertexCount - 4][X],
                    vertices[vertexCount - 4][Y],
                    vertices[vertexCount - 3][X],
                    vertices[vertexCount - 3][Y],
                    vertices[vertexCount - 2][X],
                    vertices[vertexCount - 2][Y],
                    x,
                    y);
            vertexCount = 0;
        }
    }

    private void vertexTriangleFan(float x, float y) {
        if (vertexCount >= 3) {
            triangle(vertices[0][X], vertices[0][Y], vertices[vertexCount - 2][X], vertices[vertexCount - 2][Y], x, y);
        }
    }

    private void vertexTriangleStrip(float x, float y) {
        if (vertexCount >= 3) {
            triangle(vertices[vertexCount - 2][X], vertices[vertexCount - 2][Y], x,
                    y,
                    vertices[vertexCount - 3][X],
                    vertices[vertexCount - 3][Y]);
        }
    }

    private void vertexTriangles(float x, float y) {
        if ((vertexCount % 3) == 0) {
            triangle(vertices[vertexCount - 3][X], vertices[vertexCount - 3][Y], vertices[vertexCount - 2][X], vertices[vertexCount - 2][Y], x, y);
            vertexCount = 0;
        }
    }

    private void vertexLines(float x, float y) {
        if ((vertexCount % 2) == 0) {
            line(vertices[vertexCount - 2][X], vertices[vertexCount - 2][Y], x, y);
            vertexCount = 0;
        }
    }

    private void vertexPolygon(float x, float y) {
        if (vertexCount == 0) {
            path.reset();
            path.moveTo(x, y);
            vertexCount = 1;
        } else if (breakShape) {
            path.moveTo(x, y);
            breakShape = false;
        } else {
            path.lineTo(x, y);
        }
    }

    @Override
    public void vertex(float x, float y, float z) {
        showDepthWarningXYZ("vertex");
    }

    @Override
    public void vertex(float x, float y, float u, float v) {
        showVariationWarning("vertex(x, y, u, v)");
    }

    @Override
    public void vertex(float x, float y, float z, float u, float v) {
        showDepthWarningXYZ("vertex");
    }

    @Override
    public void breakShape() {
        breakShape = true;
    }

    @Override
    public void endShape(int mode) {
        if (shape == POINTS && stroke && vertexCount > 0) {
            endPointsShape();
        } else if (shape == POLYGON) {
            endPolygonShape(mode);
        }
        shape = 0;
    }

    private void endPointsShape() {
        android.graphics.Matrix m = getCanvas().getMatrix();
        if (strokeWeight == 1 && m.isIdentity()) {
            if (screenPoint == null) {
                screenPoint = new float[2];
            }
            for (int i = 0; i < vertexCount; i++) {
                screenPoint[0] = vertices[i][X];
                screenPoint[1] = vertices[i][Y];
                m.mapPoints(screenPoint);
                set(RainbowMath.round(screenPoint[0]), RainbowMath.round(screenPoint[1]), strokeColor);
                float x = vertices[i][X];
                float y = vertices[i][Y];
                set(RainbowMath.round(screenX(x, y)), RainbowMath.round(screenY(x, y)), strokeColor);
            }
        } else {
            float sw = strokeWeight / 2;
            // temporarily use the stroke Paint as a fill
            getStrokePaint().setStyle(Style.FILL);
            for (int i = 0; i < vertexCount; i++) {
                float x = vertices[i][X];
                float y = vertices[i][Y];
                rect.set(x - sw, y - sw, x + sw, y + sw);
                getCanvas().drawOval(rect, getStrokePaint());
            }
            getStrokePaint().setStyle(Style.STROKE);
        }
    }

    private void endPolygonShape(int mode) {
        if (!path.isEmpty()) {
            if (mode == CLOSE) {
                path.close();
            }
            drawPath();
        }
    }

    @Override
    public void set(int x, int y, int argb) {
        if ((x < 0) || (y < 0) || (x >= width) || (y >= height)) {
            return;
        }
        normalBitmap.setPixel(x, y, argb);
    }

    @Override
    public float screenX(float x, float y) {
        if (screenPoint == null) {
            screenPoint = new float[2];
        }
        screenPoint[0] = x;
        screenPoint[1] = y;
        getCanvas().getMatrix().mapPoints(screenPoint);
        return screenPoint[0];
    }

    @Override
    public float screenY(float x, float y) {
        if (screenPoint == null) {
            screenPoint = new float[2];
        }
        screenPoint[0] = x;
        screenPoint[1] = y;
        getCanvas().getMatrix().mapPoints(screenPoint);
        return screenPoint[1];
    }

    public Paint getStrokePaint() {
        return strokePaint;
    }

    void drawPath() {
        if (fill) {
            getCanvas().drawPath(path, getFillPaint());
        }
        if (stroke) {
            getCanvas().drawPath(path, getStrokePaint());
        }
    }

    public Paint getFillPaint() {
        return isFillClear ? clearingFillPaint : fillPaint;
    }

    @Override
    public void bezierVertex(float x1, float y1, float x2, float y2, float x3, float y3) {
        bezierVertexCheck();
        path.cubicTo(x1, y1, x2, y2, x3, y3);
    }

    @Override
    public void bezierVertex(float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        showDepthWarningXYZ("bezierVertex");
    }

    @Override
    public void quadraticVertex(float ctrlX, float ctrlY, float endX, float endY) {
        bezierVertexCheck();
        path.quadTo(ctrlX, ctrlY, endX, endY);
    }

    @Override
    public void quadraticVertex(float x2, float y2, float z2, float x4, float y4, float z4) {
        showDepthWarningXYZ("quadVertex");
    }

    @Override
    protected void curveVertexCheck() {
        super.curveVertexCheck();

        if (curveCoordX == null) {
            curveCoordX = new float[4];
            curveCoordY = new float[4];
            curveDrawX = new float[4];
            curveDrawY = new float[4];
        }
    }

    @Override
    protected void curveVertexSegment(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        curveCoordX[0] = x1;
        curveCoordY[0] = y1;

        curveCoordX[1] = x2;
        curveCoordY[1] = y2;

        curveCoordX[2] = x3;
        curveCoordY[2] = y3;

        curveCoordX[3] = x4;
        curveCoordY[3] = y4;

        curveToBezierMatrix.mult(curveCoordX, curveDrawX);
        curveToBezierMatrix.mult(curveCoordY, curveDrawY);

        if (vertexCount == 0) {
            path.moveTo(curveDrawX[0], curveDrawY[0]);
            vertexCount = 1;
        }

        path.cubicTo(curveDrawX[1], curveDrawY[1], curveDrawX[2], curveDrawY[2], curveDrawX[3], curveDrawY[3]);
    }

    @Override
    public void curveVertex(float x, float y, float z) {
        showDepthWarningXYZ("curveVertex");
    }

    @Override
    public void point(float x, float y) {
        if (stroke) {
            getCanvas().drawPoint(x, y, getStrokePaint());
        }
    }

    @Override
    public void line(float x1, float y1, float x2, float y2) {
        if (stroke) {
            getCanvas().drawLine(x1, y1, x2, y2, getStrokePaint());
        }
    }

    @Override
    public void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        path.reset();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.close();
        drawPath();
    }

    @Override
    public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        path.reset();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        path.close();
        drawPath();
    }

    @Override
    protected void rectImpl(float x1, float y1, float x2, float y2) {
        if (fill) {
            getCanvas().drawRect(x1, y1, x2, y2, getFillPaint());
        }
        if (stroke) {
            getCanvas().drawRect(x1, y1, x2, y2, getStrokePaint());
        }
    }

    @Override
    protected void arcImpl(float x, float y, float w, float h, float start, float stop, int mode) {

        if (stop - start >= RainbowMath.TWO_PI) {
            ellipseImpl(x, y, w, h);

        } else {

            start = start * RainbowMath.RAD_TO_DEG;
            stop = stop * RainbowMath.RAD_TO_DEG;

            while (start < 0) {
                start += 360;
                stop += 360;
            }
            if (start > stop) {
                float temp = start;
                start = stop;
                stop = temp;
            }

            float sweep = stop - start;
            rect.set(x, y, x + w, y + h);

            if (mode == 0) {
                if (fill) {
                    getCanvas().drawArc(rect, start, sweep, true, getFillPaint());
                }
                if (stroke) {
                    getCanvas().drawArc(rect, start, sweep, false, getStrokePaint());
                }
            } else if (mode == OPEN) {
                if (fill) {
                    showMissingWarning("arc");
                }
                if (stroke) {
                    getCanvas().drawArc(rect, start, sweep, false, getStrokePaint());
                }
            } else if (mode == CHORD) {
                showMissingWarning("arc");

            } else if (mode == PIE) {
                if (fill) {
                    getCanvas().drawArc(rect, start, sweep, true, getFillPaint());
                }
                if (stroke) {
                    getCanvas().drawArc(rect, start, sweep, true, getStrokePaint());
                }

            }
        }
    }

    @Override
    protected void ellipseImpl(float x, float y, float w, float h) {
        rect.set(x, y, x + w, y + h);
        if (fill) {
            getCanvas().drawOval(rect, getFillPaint());
        }
        if (stroke) {
            getCanvas().drawOval(rect, getStrokePaint());
        }
    }

    @Override
    public void box(float w, float h, float d) {
        showMethodWarning("box");
    }

    @Override
    public void sphere(float r) {
        showMethodWarning("sphere");
    }

    /**
     * Ignored (not needed)
     */
    @Override
    public void bezierDetail(int detail) {
    }

    /**
     * Ignored (not needed)
     */
    @Override
    public void curveDetail(int detail) {
    }

    @Override
    public void smooth() {
        smooth = true;
        strokePaint.setAntiAlias(true);
        fillPaint.setAntiAlias(true);
        clearingFillPaint.setAntiAlias(true);
    }

    @Override
    public void noSmooth() {
        smooth = false;
        strokePaint.setAntiAlias(false);
        fillPaint.setAntiAlias(false);
        clearingFillPaint.setAntiAlias(false);
    }

    /**
     * Handle renderer-specific image drawing.
     */
    @Override
    protected void imageImpl(RainbowImage src, float x1, float y1, float x2, float y2, int u1, int v1, int u2, int v2) {

        if (src.getBitmap() == null && src.format == ALPHA) {
            // create an alpha normalBitmap for this feller
            src.setBitmap(Bitmap.createBitmap(src.width, src.height, Config.ARGB_4444));
            int[] px = new int[src.pixels.length];
            for (int i = 0; i < px.length; i++) {
                px[i] = src.pixels[i] << 24 | 0xFFFFFF;
            }
            src.getBitmap().setPixels(px, 0, src.width, 0, 0, src.width, src.height);
            src.modified = false;
        }

        if (src.getBitmap() == null || src.width != src.getBitmap().getWidth() || src.height != src.getBitmap().getHeight()) {
            src.setBitmap(Bitmap.createBitmap(src.width, src.height, Config.ARGB_4444));
            src.modified = true;
        }
        if (src.modified) {
            if (!src.getBitmap().isMutable()) {
                src.setBitmap(Bitmap.createBitmap(src.width, src.height, Config.ARGB_4444));
            }
            src.getBitmap().setPixels(src.pixels, 0, src.width, 0, 0, src.width, src.height);
            src.modified = false;
        }

        if (imageImplSrcRect == null) {
            imageImplSrcRect = new Rect(u1, v1, u2, v2);
            imageImplDstRect = new RectF(x1, y1, x2, y2);
        } else {
            imageImplSrcRect.set(u1, v1, u2, v2);
            imageImplDstRect.set(x1, y1, x2, y2);
        }

        getCanvas().drawBitmap(src.getBitmap(), imageImplSrcRect, imageImplDstRect, tint ? tintPaint : null);
    }

    @Override
    public void pushMatrix() {
        getCanvas().save(Canvas.MATRIX_SAVE_FLAG);
    }

    @Override
    public void popMatrix() {
        getCanvas().restore();
    }

    @Override
    public void translate(float tx, float ty) {
        getCanvas().translate(tx, ty);
    }

    @Override
    public void rotate(float angle) {
        getCanvas().rotate(angle * RainbowMath.RAD_TO_DEG);
    }

    @Override
    public void rotateX(float angle) {
        showDepthWarning("rotateX");
    }

    @Override
    public void rotateY(float angle) {
        showDepthWarning("rotateY");
    }

    @Override
    public void rotateZ(float angle) {
        showDepthWarning("rotateZ");
    }

    @Override
    public void rotate(float angle, float vx, float vy, float vz) {
        showVariationWarning("rotate");
    }

    @Override
    public void scale(float s) {
        getCanvas().scale(s, s);
    }

    @Override
    public void scale(float sx, float sy) {
        getCanvas().scale(sx, sy);
    }

    @Override
    public void scale(float sx, float sy, float sz) {
        showDepthWarningXYZ("scale");
    }

    @Override
    public void shearX(float angle) {
        getCanvas().skew((float) Math.tan(angle), 0);
    }

    @Override
    public void shearY(float angle) {
        getCanvas().skew(0, (float) Math.tan(angle));
    }

    @Override
    public void applyMatrix(float n00, float n01, float n02, float n10, float n11, float n12) {
        android.graphics.Matrix m = new android.graphics.Matrix();
        m.setValues(new float[]{n00, n01, n02, n10, n11, n12, 0, 0, 1});
        getCanvas().concat(m);
    }

    @Override
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
        showVariationWarning("applyMatrix");
    }

    @Override
    public RMatrix getMatrix() {
        return getMatrix((RMatrix2D) null);
    }

    @Override
    public RMatrix2D getMatrix(RMatrix2D target) {
        if (target == null) {
            target = new RMatrix2D();
        }

        android.graphics.Matrix m = new android.graphics.Matrix();
        getCanvas().getMatrix(m);
        m.getValues(transform);
        target.set(transform[0], transform[1], transform[2], transform[3], transform[4], transform[5]);
        return target;
    }

    @Override
    public void setMatrix(RMatrix2D source) {
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.setValues(new float[]{source.m00, source.m01, source.m02, source.m10, source.m11, source.m12, 0, 0, 1});
        getCanvas().setMatrix(matrix);
    }

    @Override
    public void setMatrix(RMatrix3D source) {
        showVariationWarning("setMatrix");
    }

    @Override
    public RMatrix3D getMatrix(RMatrix3D target) {
        showVariationWarning("getMatrix");
        return target;
    }

    @Override
    public float screenX(float x, float y, float z) {
        showDepthWarningXYZ("screenX");
        return 0;
    }

    @Override
    public float screenY(float x, float y, float z) {
        showDepthWarningXYZ("screenY");
        return 0;
    }

    @Override
    public float screenZ(float x, float y, float z) {
        showDepthWarningXYZ("screenZ");
        return 0;
    }

    @Override
    public void strokeCap(int cap) {
        super.strokeCap(cap);

        if (strokeCap == ROUND) {
            getStrokePaint().setStrokeCap(Paint.Cap.ROUND);
        } else if (strokeCap == PROJECT) {
            getStrokePaint().setStrokeCap(Paint.Cap.SQUARE);
        } else {
            getStrokePaint().setStrokeCap(Paint.Cap.BUTT);
        }
    }

    @Override
    public void strokeJoin(int join) {
        super.strokeJoin(join);

        if (strokeJoin == MITER) {
            getStrokePaint().setStrokeJoin(Paint.Join.MITER);
        } else if (strokeJoin == ROUND) {
            getStrokePaint().setStrokeJoin(Paint.Join.ROUND);
        } else {
            getStrokePaint().setStrokeJoin(Paint.Join.BEVEL);
        }
    }

    @Override
    public void strokeWeight(float weight) {
        super.strokeWeight(weight);
        getStrokePaint().setStrokeWidth(weight);
    }

    @Override
    protected void strokeFromCalc() {
        super.strokeFromCalc();
        getStrokePaint().setColor(strokeColor);
        getStrokePaint().setShader(null);
    }

    @Override
    protected void tintFromCalc() {
        super.tintFromCalc();
        tintPaint.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.MULTIPLY));
    }

    public void fill(int rgb) {
        fill(rgb, 255);
    }

    public void fill(int rgb, float alpha) {
        if (rgb == CLEAR) {
            isFillClear = true;
            clearingFillPaint.setAlpha((int) alpha);
        } else {
            isFillClear = false;
            super.fill(rgb, alpha);
        }
    }

    @Override
    protected void fillFromCalc() {
        super.fillFromCalc();
        getFillPaint().setColor(fillColor);
        getFillPaint().setShader(null);
    }

    @Override
    public void backgroundImpl() {
        getCanvas().drawColor(backgroundColor);
    }

    @Override
    public void beginRaw(RainbowGraphics recorderRaw) {
        showMethodWarning("beginRaw");
    }

    @Override
    public void endRaw() {
        showMethodWarning("endRaw");
    }

    /**
     * Update the pixels[] buffer to the PGraphics image.
     * <p/>
     * Unlike in PImage, where updatePixels() only requests that the update
     * happens, in PGraphicsJava2D, this will happen immediately.
     */
    @Override
    public void updatePixels(int x, int y, int c, int d) {
        if ((x != 0) || (y != 0) || (c != width) || (d != height)) {
            // Show a warning message, but continue anyway.
            showVariationWarning("updatePixels(x, y, w, h)");
        }
        updatePixels();
    }

    /**
     * Update the pixels[] buffer to the PGraphics image.
     * <p/>
     * Unlike in PImage, where updatePixels() only requests that the update
     * happens, in PGraphicsJava2D, this will happen immediately.
     */
    @Override
    public void updatePixels() {
        normalBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    @Override
    public RainbowGraphics2D resize(int wide, int high) {
        showMethodWarning("resize");
        return this;
    }

    @Override
    public int get(int x, int y) {
        if ((x < 0) || (y < 0) || (x >= width) || (y >= height)) {
            return 0;
        }
        return normalBitmap.getPixel(x, y);
    }

    @Override
    public RainbowImage get() {
        return get(0, 0, width, height);
    }

    @Override
    public void set(int x, int y, RainbowImage src) {
        if (src.format == ALPHA) {
            throw new RuntimeException("set() not available for ALPHA images");
        }

        if (src.getBitmap() == null) {
            getCanvas().drawBitmap(src.pixels, 0, src.width, x, y, src.width, src.height, false, null);
        } else {
            if (src.width != src.getBitmap().getWidth() || src.height != src.getBitmap().getHeight()) {
                src.setBitmap(Bitmap.createBitmap(src.width, src.height, Config.ARGB_8888));
                src.modified = true;
            }
            if (src.modified) {
                if (!src.getBitmap().isMutable()) {
                    src.setBitmap(Bitmap.createBitmap(src.width, src.height, Config.ARGB_8888));
                }
                src.getBitmap().setPixels(src.pixels, 0, src.width, 0, 0, src.width, src.height);
                src.modified = false;
            }
            getCanvas().save(Canvas.MATRIX_SAVE_FLAG);
            getCanvas().setMatrix(null); // set to identity
            getCanvas().drawBitmap(src.getBitmap(), x, y, null);
            getCanvas().restore();
        }
    }

    @Override
    public void mask(int alpha[]) {
        showMethodWarning("mask");
    }

    @Override
    public void mask(RainbowImage alpha) {
        showMethodWarning("mask");
    }

    @Override
    public void copy(int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh) {
        rect.set(sx, sy, sx + sw, sy + sh);
        Rect src = new Rect(dx, dy, dx + dw, dy + dh);
        getCanvas().drawBitmap(normalBitmap, src, rect, null);
    }
}