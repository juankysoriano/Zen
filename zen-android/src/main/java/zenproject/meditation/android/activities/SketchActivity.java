package zenproject.meditation.android.activities;

import android.app.Activity;
import android.os.Bundle;

import zenproject.meditation.android.R;
import zenproject.meditation.android.views.ZenSketchView;

public class SketchActivity extends Activity {

    private ZenSketchView zenSketchView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sketch);
        zenSketchView = (ZenSketchView) findViewById(R.id.sketch);
    }

    @Override
    protected void onResume() {
        super.onResume();
        zenSketchView.startSketch();
    }

    @Override
    protected void onPause() {
        zenSketchView.pauseSketch();
        super.onPause();
    }

    @Override
    protected void onStop() {
        zenSketchView.stopSketch();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zenSketchView.destroySketch();
        super.onDestroy();
    }
}
