package com.juankysoriano.rainbow.core.cv.blobdetector;

import java.util.Arrays;

//==================================================
//class BlobDetection
//==================================================
public class BlobDetection extends EdgeDetection {
    public static int MAX_NBLINE = 4000;
    public static int MAX_NB = 1000;
    // Temp
    public int blobNumber;
    public boolean[] gridVisited;
    public ThreadGroup threadGroup = new ThreadGroup("BLOB");

    public int blobWidthMin, blobHeightMin;
    private static int counter = 0;
    // Temp
    Object parent;

    // --------------------------------------------
    // Constructor
    // --------------------------------------------
    public BlobDetection(int imgWidth, int imgHeight) {
        super(imgWidth, imgHeight);

        gridVisited = new boolean[nbGridValue];
        blobNumber = 0;
        blobWidthMin = 0;
        blobHeightMin = 0;
    }

    public int getBlobNb() {
        return blobNumber;
    }

    public static void setConstants(int maxBlobNb, int maxBlobNLine) {
        MAX_NB = maxBlobNb;
        MAX_NBLINE = maxBlobNLine;
    }

    // --------------------------------------------
    // computeBlobs()
    // --------------------------------------------
    public void computeBlobs(int[] pixels, final OnBlobDetectedCallback onBlobDetectedCallback) {
        setImage(pixels);
        counter = 0;

        Thread thread = new Thread(threadGroup, new Runnable() {
            @Override
            public void run() {

                Arrays.fill(gridVisited, false);

                computeIsovalue();

                int x, y, squareIndex, n;
                int offset;

                nbLineToDraw = 0;
                blobNumber = 0;
                Blob newBlob = new Blob(BlobDetection.this);
                for (x = 0; x < resx - 1; x++) {
                    for (y = 0; y < resy - 1; y++) {
                        offset = x + resx * y;
                        if (!gridVisited[offset]) {
                            squareIndex = getSquareIndex(x, y);

                            if (squareIndex > 0 && squareIndex < 15) {
                                if (blobNumber >= 0 && blobNumber < MAX_NB) {
                                    findBlob(newBlob, x, y, onBlobDetectedCallback);
                                    blobNumber++;
                                }
                            }
                        }
                    }
                }
                nbLineToDraw /= 2;
                onBlobDetectedCallback.onBlobDetectionFinish();
            }
        }, "blob", 100000);

        thread.start();
    }

    public void findBlob(Blob newBlob, int x, int y, OnBlobDetectedCallback onBlobDetectedCallback) {

        newBlob.id = blobNumber;
        newBlob.xMin = Integer.MAX_VALUE;
        newBlob.xMax = Integer.MIN_VALUE;
        newBlob.yMin = Integer.MAX_VALUE;
        newBlob.yMax = Integer.MIN_VALUE;
        newBlob.nbLine = 0;

        computeEdgeVertex(newBlob, x, y);
        newBlob.update();
        if (onBlobDetectedCallback != null) {
            if (onBlobDetectedCallback.isToDiscardBlob(newBlob)) {
                blobNumber--;
            } else {
                onBlobDetectedCallback.onBlobDetected(newBlob);
            }
        }
    }

    // --------------------------------------------
    // computeEdgeVertex()
    // --------------------------------------------
    void computeEdgeVertex(Blob newBlob, int x, int y) {
        int offset = x + resx * y;
        if (gridVisited[offset]) {
            return;
        }

        gridVisited[offset] = true;

        if (newBlob.nbLine < MAX_NBLINE) {
            nbLineToDraw++;
            newBlob.line[newBlob.nbLine++] = ((x) * resy + (y)) * 2 ;

            calculateNextEdgeVertex(newBlob, x, y);
            try {
                computeNextEdgeVertex(newBlob, x, y);
            } catch (StackOverflowError error) {
                computeNextEdgeVertex(newBlob, x, y);
            }
        }
    }

    private void calculateNextEdgeVertex(Blob newBlob, int x, int y) {
        int index = (x * resy + y) * 2;
        int offset = x + resx * y;
        int squareIndex = getSquareIndex(x, y);
        int toCompute = MetaballsTable.edgeToCompute[squareIndex];
        if (toCompute > 0) {
            float t;
            float value;
            if ((toCompute & 1) > 0) {
                float vx = (float) x * stepx;
                t = (isovalue - gridValue[offset]) / (gridValue[offset + 1] - gridValue[offset]);
                value = vx * (1.0f - t) + t * (vx + stepx);
                edgeVrt[index].x = value;

                newBlob.xMin = Math.min(value, newBlob.xMin);
                newBlob.xMax = Math.max(value, newBlob.xMax);
            }
            if ((toCompute & 2) > 0) {
                float vy = (float) y * stepy;
                t = (isovalue - gridValue[offset]) / (gridValue[offset + resx] - gridValue[offset]);
                value = vy * (1.0f - t) + t * (vy + stepy);
                edgeVrt[index + 1].y = value;

                newBlob.yMin = Math.min(value, newBlob.yMin);
                newBlob.yMax = Math.max(value, newBlob.yMax);
            }
        }
    }

    private void computeNextEdgeVertex(Blob newBlob, int x, int y) {
        int squareIndex = getSquareIndex(x, y);
        byte neighborVoxel = MetaballsTable.neightborVoxel[squareIndex];

        if (x < resx - 2 && (neighborVoxel & (1 << 0)) > 0) {
            computeEdgeVertex(newBlob, x + 1, y);
        }
        if (x > 0 && (neighborVoxel & (1 << 1)) > 0) {
            computeEdgeVertex(newBlob, x - 1, y);
        }
        if (y < resy - 2 && (neighborVoxel & (1 << 2)) > 0) {
            computeEdgeVertex(newBlob, x, y + 1);
        }
        if (y > 0 && (neighborVoxel & (1 << 3)) > 0) {
            computeEdgeVertex(newBlob, x, y - 1);
        }
    }
};
