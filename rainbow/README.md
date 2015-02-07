Rainbow
=======

A fork of [Processing][1] for Android 

![](http://s8.postimg.org/a0o3aew39/rainbow_307622_640.png)


Guide
------------

* What can I do with Rainbow? 

With Rainbow you can easily draw graphics (interactive or not) in your Android application.

* How to do it?

Easy! Just extend `Rainbow` and attach it to one of your `ViewGroup`! Then you will `@Override` the methods in the `RainbowLifeCycleCallback` to control your animations. ([Check the demo app!!][2])

Something like this:

1 ) _Creating a `Rainbow` object_:

```js
public class RainbowSketch extends Rainbow {

    private static final int FRAME_RATE = 60;
    protected RainbowSketch(ViewGroup parentView) {
        super(parentView);
    }

    public void onSketchSetup(RainbowDrawer rainbowDrawer) {
      //Called when the rainbow is being setup.
      frameRate(FRAME_RATE);
    }

    public void onDrawingStart(RainbowInputController rainbowInputController) {
      //Called when the rainbow sketch is about to start.
    }

    public void onDrawingResume() {
       //Called when the rainbow sketch is resumed
    }

    public void onDrawingStep(RainbowDrawer rainbowDrawer,
                                RainbowInputController rainbowInputController) {
       //Called when the rainbow sketch is about to perform a new step
       //Here is where the animations should be done.
       
       //This will be called a FRAME_RATE number of times per second!!! 
       //GREAT and ASYNCHRONOUS!!
    }

    public void onDrawingPause() {
       //Called when the rainbow sketch is about to pause
    }

    public void onDrawingStop(RainbowInputController rainbowInputController) {
        //Called when the rainbow sketch is about to stop
    }

    public void onSketchDestroy() {
        //Called when the rainbow sketch is about to be destroyed
    }
}
```

2 ) _What is a `RainbowDrawer`?_ 

A `RainbowDrawer` exposes to you all the methods you will need in order to do amazing drawings. Following one example.

```js
    public void onDrawingStep(RainbowDrawer rainbowDrawer,
                                  RainbowInputController rainbowInputController) {
       //Let's setup our paint-style
       rainbowDrawer.fill(Color.RED);
       rainbowDrawer.strokeWeight(2);
       rainbowDrawer.stroke(Color.BLUE);
       
       //Let's paint something!
       
       // A quarter screen rectangle
       rainbowDrawer.rect(0, 
                          0, 
                          getWidth()/2, 
                          getHeight()/2); 
       
       // A quarter screen ellipse
       rainbowDrawer.ellipse(getWidth()/2, 
                            getHeight()/2, 
                            getWidth()/2, 
                            getHeight()/2);  
    }
```

Try to compilate it a bit! 

3 ) _What is a `RainbowInputController` ?_

With `RainbowInputController` you have access to the user interactions with your `Rainbow`

```js
    public void onDrawingStep(RainbowDrawer rainbowDrawer,
                                  RainbowInputController rainbowInputController) {
       //Let's setup our paint-style
       rainbowDrawer.fill(Color.RED);
       rainbowDrawer.strokeWeight(2);
       rainbowDrawer.stroke(Color.BLUE);
       
       //Let's paint something
        rainbowDrawer.ellipse(rainbowInputController.getX(), 
                            rainbowInputController.getY(), 
                            100, 
       	                    100);
    }
```

You can also register to it a `RainbowInteractionListener` for more fine grained control over the user interactions.

```js
public interface RainbowInteractionListener {
    void onSketchTouched(final RainbowEvent event, 
                        final RainbowDrawer rainbowDrawer);

    void onSketchReleased(final RainbowEvent event, 
                        final RainbowDrawer rainbowDrawer);

    void onFingerDragged(final RainbowEvent event, 
                        final RainbowDrawer rainbowDrawer);

    void onMotionEvent(final RainbowEvent event, 
                        final RainbowDrawer rainbowDrawer);
}
```

4 . _Let's inject a `Rainbow` into your `ViewGroup`_

```js
public class SketchActivity extends Activity {

    private RainbowSketch sketch;
    private ViewGroup sketchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sketch);
        sketchView = getSketchView();
        sketch = new RainbowSketch(sketchView);
    }

    private ViewGroup getSketchView() {
        return (ViewGroup) findViewById(R.id.rainbow_layout);
    }

    @Override
    public void onPause() {
        sketch.pause();
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        sketch.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        sketch.resume();
    }

    @Override
    public void onStop() {
        sketch.stop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        sketch.destroy();
        sketch = null;
        super.onDestroy();
    }
```



License
--------

    Copyright 2014 Juanky Soriano Ltd.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

[1]:https://github.com/processing
[2]:https://github.com/juankysoriano/rainbow/tree/master/demo
