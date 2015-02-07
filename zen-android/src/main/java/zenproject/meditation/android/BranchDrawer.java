package zenproject.meditation.android;

public class BranchDrawer {
    /** public int x;
    public int y;
    public float angle;
    public float rad;
    public float step;
    public float shrink;
    public PVector pos;
    public PVector pPos;
    public boolean ALIVE;
    public boolean ACTIVE;
    public float upRadius;
    public ZenMeditation sketch;

    BranchDrawer(int x, int y, boolean active, float radius, ZenMeditation sketch) {
        this.sketch = sketch;
        upRadius = radius;
        ACTIVE = active;
        angle = sketch.random(-PApplet.PI, PApplet.PI);
        rad = sketch.random(3, 5);
        step = sketch.random(-.1f, .1f);
        while (step < .02f && step > -.02f) {
            step = sketch.random(-.1f, .1f);
        }
        shrink = PApplet.map(rad, 5, 8, .98f, .995f);//random(.98,1);
        pos = new PVector(x, y);
        pPos = new PVector(0, 0);
        pPos.set(pos);
        ALIVE = true;
    }

    BranchDrawer(BranchDrawer parentDrawer) {
        this.sketch = parentDrawer.sketch;
        upRadius = parentDrawer.upRadius;
        ACTIVE = parentDrawer.ACTIVE;
        angle = parentDrawer.angle;
        rad = parentDrawer.rad;
        step = -parentDrawer.step;
        shrink = PApplet.map(rad, 5, 8, .98f, .999f);
        pos = new PVector(parentDrawer.pos.x, parentDrawer.pos.y);
        pPos = new PVector(parentDrawer.pos.x, parentDrawer.pos.y);
        ALIVE = true;
    }

    public void draw() {
        if (ACTIVE) {
            sketch.stroke(0, 0, 0, 128);
            if (pos.y + 4 < sketch.inkInitY && pPos.y + 4 < sketch.inkInitY) {
                sketch.line(pos.x, pos.y, pPos.x, pPos.y);
            } else {
                rad = 0;
            }
        }
    }

    public void calculate() {
        if (ACTIVE) {
            pPos.set(pos);
            pos.x += rad * PApplet.cos(angle) / 2;
            pos.y += rad * PApplet.sin(angle) / 2;
            angle += step;
            rad *= shrink;
            if (rad < .05f || angle > 8 * PApplet.TWO_PI || angle < -8 * PApplet.TWO_PI) {
                ALIVE = false;
                sketch.noStroke();
                int randomColorA = (int) sketch.random(100);
                int randomColorB = (int) sketch.random(20);
                float randomRadiusA = 4 + sketch.random(4);
                float randomRadiusC = 4 + sketch.random(4);
                float flowerX = pos.x;
                float flowerY = pos.y;

                if (sketch.random(10) > upRadius) {
                    sketch.fill(235 + randomColorB, 212 + randomColorA, 212 + randomColorB, 100);
                    sketch.ellipse(flowerX, flowerY - randomRadiusC, randomRadiusC, randomRadiusC);
                    sketch.ellipse(flowerX - randomRadiusC, flowerY - randomRadiusC / 2, randomRadiusC, randomRadiusC);
                    sketch.ellipse(flowerX + randomRadiusC, flowerY - randomRadiusC / 2, randomRadiusC, randomRadiusC);
                    sketch.ellipse(flowerX + randomRadiusC / 2, flowerY + randomRadiusC / 2, randomRadiusC, randomRadiusC);
                    sketch.ellipse(flowerX - randomRadiusC / 2, flowerY + randomRadiusC / 2, randomRadiusC, randomRadiusC);

                    sketch.fill(255 - randomColorB, 110 - randomColorA, 110 - randomColorB, 64);
                    sketch.ellipse(flowerX, flowerY, randomRadiusC, randomRadiusC);
                } else {
                    sketch.fill(randomColorA, 150 - randomColorA, 30, 30);
                    sketch.ellipse(pos.x, pos.y, randomRadiusA, randomRadiusA);
                }
            }
        }
    } **/
}