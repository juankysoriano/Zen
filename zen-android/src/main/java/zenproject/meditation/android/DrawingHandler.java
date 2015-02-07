package zenproject.meditation.android;

public class DrawingHandler {
    /** ZenMeditation sketch;

    public DrawingHandler(ZenMeditation sketch) {
        this.sketch = sketch;
    }

    //Draws the inkContainer
    void drawInkContainer() {
        float inkFactor = (sketch.inkAccount / sketch.maxAccount);
        float inkTotalHeight = (4 * sketch.height) / 5 - sketch.infoHeight - sketch.infoInitY - 20 * sketch.width / 480;
        sketch.fill(150, 150, 150, 100);
        sketch.stroke(20, 20, 20);
        sketch.rect(sketch.width - sketch.height / 16, sketch.infoInitY + sketch.infoHeight + 5 * sketch.height / 480, sketch.height / 16 - 5 * sketch.height / 480, inkTotalHeight, 6 * sketch.height / 480);
        sketch.noStroke();
        sketch.fill(10, 10, 10, 150);
        float inkLevelB = inkTotalHeight * (inkFactor);
        float inkLevelA = sketch.infoInitY + sketch.infoHeight + 5 * sketch.height / 480 + (inkTotalHeight - inkLevelB);
        inkLevelB = inkLevelB <= 0 ? 0 : inkLevelB;
        sketch.rect(sketch.width - sketch.height / 16, inkLevelA, sketch.height / 16 - 5 * sketch.width / 800, inkLevelB, 6 * sketch.height / 480);
    }

    //Draws the cleaning effect
    void drawCleaning() {
        if (sketch.cleaningLevel < 36) {
            sketch.isPainting = false;
            sketch.isRestoring = false;
            sketch.isInformating = false;
            sketch.infoShowed = false;
            sketch.tint(255, sketch.cleaningLevel * 3);
            drawInkContainer();
            sketch.cleaningLevel++;
        } else {
            sketch.infoShowed = false;
            sketch.cleaningLevel = 20;
            sketch.isCleaning = false;
        }

        sketch.image(sketch.canvas, 0, 0, sketch.width, sketch.canvasAHeight);
        drawInkContainer();
        sketch.image(sketch.info, sketch.infoInitX, sketch.infoInitY, sketch.infoHeight, sketch.infoHeight);
        sketch.image(sketch.canvasdown, 0, sketch.canvasAHeight, sketch.width, sketch.canvasBHeight);
        sketch.image(sketch.ink, sketch.inkInitX, sketch.inkInitY, sketch.buttonHeight, sketch.buttonHeight);
        sketch.image(sketch.share, sketch.shareInitX, sketch.shareInitY, sketch.buttonHeight, sketch.buttonHeight);
        sketch.image(sketch.water, sketch.waterInitX, sketch.waterInitY, sketch.buttonHeight, sketch.buttonHeight);
        sketch.image(sketch.reset, sketch.resetInitX, sketch.resetInitY, sketch.buttonHeight, sketch.buttonHeight);
        sketch.noTint();
    }

    //The drawing information effect.
    void drawInformation() {
        if (sketch.cleaningLevel < 26) {
            sketch.isPainting = false;
            sketch.isRestoring = false;
            sketch.isCleaning = false;
            sketch.infoShowed = false;
            sketch.cleaningLevel++;
            sketch.tint(255, sketch.cleaningLevel * 2);
            sketch.image(sketch.information, 0, 0, sketch.width, sketch.height);
            sketch.noTint();
        } else {
            sketch.cleaningLevel = 20;
            sketch.isInformating = false;
            sketch.infoShowed = true;
        }
    }

    //The drawing restoring effect (coming back from information)
    void drawRestoring() {
        if (sketch.cleaningLevel < 36) {
            sketch.isPainting = false;
            sketch.isInformating = false;
            sketch.isCleaning = false;
            sketch.infoShowed = false;
            sketch.cleaningLevel++;
            sketch.tint(255, sketch.cleaningLevel * 3);
        } else {
            sketch.cleaningLevel = 20;
            sketch.infoShowed = false;
            sketch.isRestoring = false;
        }
        sketch.image(sketch.backupCanvas, 0, 0, sketch.width, sketch.height);
        sketch.noTint();
    }

    //The drawing ink effect
    void drawPainting() {
        //If the mouse is pressed and the info is not showed
        if (sketch.mousePressed && !sketch.infoShowed) {
            sketch.isPainting = true;
            if (sketch.inkAccount > 0) //if there is ink in our container paint the ink trace.
            {
                paintInk();
            } else //finnish all current drawings.
            {
                sketch.remainPainting = false;
            }
        } else if (!sketch.infoShowed) //is the mouse is not pressed, and the info is not showed
        {
            sketch.isPainting = false;
            //if there is still some ink into the container, and the sketch.radius of the ink > 1
            //we keep painting the remain ink.
            if (sketch.radius > 1 && sketch.inkAccount > 0) {
                sketch.remainPainting = true;
                paintInk();
            } else //restore the paint variables.
            {
                sketch.remainPainting = false;
                sketch.radius = 1;
                sketch.x = -1;
                sketch.y = -1;
                sketch.xOld = -1;
                sketch.yOld = -1;
            }
        }
    }

    //Paint the ink
    private void paintInk() {
        if (sketch.mousePressed && (sketch.x < 0 || sketch.remainPainting)) {
            sketch.remainPainting = false;
            sketch.x = sketch.pmouseX;
            sketch.y = sketch.pmouseY;
            sketch.xVel *= 0.33f;
            sketch.yVel *= 0.33f;
        }

        sketch.xOld = sketch.x;
        sketch.yOld = sketch.y;

        //calculate distance and springing
        sketch.xD = (sketch.mouseX - sketch.x) * sketch.spring;
        sketch.yD = (sketch.mouseY - sketch.y) * sketch.spring;

        //calculate velocity and damping
        sketch.xVel = (sketch.xVel + sketch.xD) * sketch.damp;
        sketch.yVel = (sketch.yVel + sketch.yD) * sketch.damp;

        //calculate position;
        sketch.x += sketch.xVel;
        sketch.y += sketch.yVel;

        sketch.noStroke();
        inkLine((int) sketch.x, (int) sketch.y, (int) sketch.xOld, (int) sketch.yOld);
    }

    private void inkLine(int x, int y, int px, int py) {
        float distance = PApplet.abs(PApplet.dist(x, y, px, py));
        float maxDistance = PApplet.dist(0, 0, sketch.width, sketch.height);
        //When some ink is remaining
        if (!sketch.mousePressed) {
            sketch.radius--;
        }
        //Painting fast mean decreasing radius size a max of 1
        else if (distance > maxDistance * 0.05) {
            sketch.radius = sketch.radius - PApplet.min(1, sketch.radius / 6);
        } else {

            sketch.radius = sketch.radius + PApplet.min(1, sketch.radius / 6);
        }

        //This is the typical method for drawing a line composed by dots, bot instead of
        //calling drawDot(x,y) or drawPoint(x,y) we call drawInk(x,y).
        if (x == px && y == py) {
            drawInk(x, y);
        }

        float dx = x - px;
        float dy = y - py;
        if (dx == 0) {
            if (PApplet.min(y, py) == py) {
                for (float i = PApplet.min(y, py); i < PApplet.max(y, py); i += 1) {
                    drawInk(x, i);
                }
            } else {
                for (float i = PApplet.max(y, py); i >= PApplet.min(y, py); i -= 1) {
                    drawInk(x, i);
                }
            }
            return;
        }
        float k = dy / dx;
        float m = y - x * k;
        if (PApplet.min(x, px) == px) {
            for (float i = PApplet.min(x, px); i < PApplet.max(x, px); i += 1 / PApplet.max(1, PApplet.abs(k))) {
                drawInk(i, k * i + m);
            }
        } else {
            for (float i = PApplet.max(x, px); i >= PApplet.min(x, px); i -= 1 / PApplet.max(1, PApplet.abs(k))) {
                drawInk(i, k * i + m);
            }
        }
    }

    private void drawInk(float x, float y) {
        //Out of bounds test.
        if (y + sketch.radius / 2 > sketch.canvasAHeight) {
            y = sketch.canvasAHeight - sketch.radius / 2;
        }

        if (x + sketch.radius / 2 >= sketch.infoInitX) {
            x = sketch.infoInitX - sketch.radius / 2;
        }

        //Max radius depends on inkAccount
        sketch.radius = sketch.radius > sketch.inkAccount * 0.0016f ? sketch.inkAccount * 0.0016f : sketch.radius;
        sketch.inkAccount -= sketch.inkAccount < sketch.maxAccount / 2 && sketch.radius < 40 ? 40 : sketch.radius;
        ;
        sketch.tint(255, 55);
        sketch.imageMode(PApplet.CENTER);
        sketch.image(sketch.texture, x, y, sketch.radius < 2 ? 2 : sketch.radius, sketch.radius < 2 ? 2 : sketch.radius);
        sketch.imageMode(PApplet.CORNER);
        sketch.noTint();

        sketch.paintCounter++;
        if (sketch.paintCounter % 15 == 0 && sketch.radius > 0) {
            sketch.brancDrawers.add(new BranchDrawer((int) x, (int) y, true, sketch.radius, sketch));
        }
    }

    void drawBranches() {
        for (int i = 0; i < sketch.brancDrawers.size() && !sketch.infoShowed; i++) {
            BranchDrawer branchDrawer = (BranchDrawer) sketch.brancDrawers.get(i);
            if (branchDrawer != null) {
                branchDrawer.draw();
                branchDrawer.calculate();
                if (sketch.random(100) > 98 && branchDrawer.ACTIVE) {
                    sketch.brancDrawers.add(new BranchDrawer(branchDrawer));
                }
                if (!branchDrawer.ALIVE) {
                    sketch.brancDrawers.remove(i);
                }
            }
        }
    } **/
}
