package com.juankysoriano.rainbow.core.cv.blobdetector;

//==================================================
//class Metaballs2D
//==================================================
public class Metaballs2D {
    // Isovalue
    // ------------------
    protected float isovalue;

    // Grid
    // ------------------
    protected int resx, resy;
    protected float stepx, stepy;
    protected float[] gridValue;
    protected int nbGridValue;

    // EdgeVertex
    // ------------------
    protected EdgeVertex[] edgeVrt;
    protected int nbEdgeVrt;

    // Lines
    // what we pass to the renderer
    // ------------------
    protected int nbLineToDraw;

    // Constructor
    // ------------------
    public Metaballs2D() {
    }

    // init(int, int)
    // ------------------
    public void init(int resx, int resy) {
        this.resx = resx;
        this.resy = resy;

        this.stepx = 1.0f / ((float) (resx - 1));
        this.stepy = 1.0f / ((float) (resy - 1));

        // Allocate gridValue
        nbGridValue = resx * resy;
        gridValue = new float[nbGridValue];

        // Allocate EdgeVertices
        edgeVrt = new EdgeVertex[2 * nbGridValue];
        nbEdgeVrt = 2 * nbGridValue;

        // Allocate Lines
        nbLineToDraw = 0;

        // Precompute some values
        int x, y, n, index;
        n = 0;
        for (x = 0; x < resx; x++)
            for (y = 0; y < resy; y++) {
                index = 2 * n;
                // values
                edgeVrt[index] = new EdgeVertex(x * stepx, y * stepy);
                edgeVrt[index + 1] = new EdgeVertex(x * stepx, y * stepy);

                // Next!
                n++;
            }

    }

    // computeIsovalue()
    // ------------------
    public void computeIsovalue() {

        // A simple test : put a metaball on center of the screen
        /*
         * float ballx = 0.5f; float bally = 0.5f; float vx,vy; float dist;
		 * 
		 * int x,y; vx = 0.0f; for (x=0 ; x<resx; x++) { vy = 0.0f; for (y=0 ;
		 * y<resy; y++) { dist = (float)sqrt((vx-ballx)*(vx-ballx) +
		 * (vy-bally)*(vy-bally)); gridValue[x+resx*y] =
		 * 10.0f/(dist*dist+0.001f); vy+=stepy; } vx+=stepx; }
		 */

    }

    // computeMesh()
    // ------------------
    public void computeMesh() {
        // Compute IsoValue
        computeIsovalue();
        // Get Lines indices

        int x, y, squareIndex, n;
        int iEdge;
        int offx, offy, offAB;
        int toCompute;
        int offset, index;
        float t;
        float vx, vy;
        int[] edgeOffsetInfo;

        nbLineToDraw = 0;
        vx = 0.0f;
        for (x = 0; x < resx - 1; x++) {
            vy = 0.0f;
            for (y = 0; y < resy - 1; y++) {
                offset = x + resx * y;
                index = (x * resy + y) * 2;
                squareIndex = getSquareIndex(x, y);

                n = 0;
                while ((iEdge = MetaballsTable.edgeCut[squareIndex][n++]) != -1) {
                    edgeOffsetInfo = MetaballsTable.edgeOffsetInfo[iEdge];
                    offx = edgeOffsetInfo[0];
                    offy = edgeOffsetInfo[1];
                    offAB = edgeOffsetInfo[2];

                    nbLineToDraw++;
                }

                toCompute = MetaballsTable.edgeToCompute[squareIndex];
                if (toCompute > 0) {
                    if ((toCompute & 1) > 0) // Edge 0
                    {
                        t = (isovalue - gridValue[offset]) / (gridValue[offset + 1] - gridValue[offset]);
                        edgeVrt[index].x = vx * (1.0f - t) + t * (vx + stepx);
                    }
                    if ((toCompute & 2) > 0) // Edge 3
                    {
                        t = (isovalue - gridValue[offset]) / (gridValue[offset + resx] - gridValue[offset]);
                        edgeVrt[index + 1].y = vy * (1.0f - t) + t * (vy + stepy);
                    }

                } // toCompute
                vy += stepy;
            } // for y

            vx += stepx;
        } // for x

        nbLineToDraw /= 2;

    }

    // getSquareIndex(int,int)
    // ------------------
    protected int getSquareIndex(int x, int y) {
        int squareIndex = 0;
        int offy = resx * y;
        int offy1 = resx * (y + 1);
        if (gridValue[x + offy] < isovalue) {
            squareIndex |= 1;
        }
        if (gridValue[x + 1 + offy] < isovalue) {
            squareIndex |= 2;
        }
        if (gridValue[x + 1 + offy1] < isovalue) {
            squareIndex |= 4;
        }
        if (gridValue[x + offy1] < isovalue) {
            squareIndex |= 8;
        }
        return squareIndex;
    }

    // setIsoValue(float)
    // ------------------
    public void setIsovalue(float iso) {
        this.isovalue = iso;
    }

}