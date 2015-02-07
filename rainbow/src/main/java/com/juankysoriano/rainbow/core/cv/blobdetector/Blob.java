package com.juankysoriano.rainbow.core.cv.blobdetector;

//==================================================
//class Blob
//==================================================
public class Blob {
    public BlobDetection parent;

    public int id;
    public float x, y; // position of its center
    public float w, h; // width & height
    public float xMin, xMax, yMin, yMax;

    public int[] line;
    public int nbLine;

    public Blob(BlobDetection parent) {
        this.parent = parent;
        line = new int[parent.MAX_NBLINE]; // stack of index
        nbLine = 0;
    }

    public EdgeVertex getEdgeVertexA(int iEdge) {
        if (iEdge * 2 < parent.nbLineToDraw * 2) {
            return parent.getEdgeVertex(line[iEdge * 2]);
        } else {
            return null;
        }
    }

    public EdgeVertex getEdgeVertexB(int iEdge) {
        if ((iEdge * 2 + 1) < parent.nbLineToDraw * 2) {
            return parent.getEdgeVertex(line[iEdge * 2 + 1]);
        } else {
            return null;
        }
    }

    public int getEdgeNb() {
        return nbLine;
    }

    public void update() {
        w = (xMax - xMin);
        h = (yMax - yMin);
        x = 0.5f * (xMax + xMin);
        y = 0.5f * (yMax + yMin);
        nbLine /= 2;
    }
}