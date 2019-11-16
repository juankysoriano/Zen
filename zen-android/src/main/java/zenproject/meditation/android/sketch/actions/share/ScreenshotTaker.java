package zenproject.meditation.android.sketch.actions.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.novoda.notils.logger.simple.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.core.content.FileProvider;
import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;

class ScreenshotTaker {
    private static final String PICTURE_TITLE = ContextRetriever.INSTANCE.getResources().getString(R.string.screenshot_name);

    private final Context context;
    private final RainbowDrawer rainbowDrawer;
    private final File saveFolder;
    private final String authority;

    ScreenshotTaker(Context context, RainbowDrawer rainbowDrawer, File saveFolder, String authority) {
        this.context = context;
        this.rainbowDrawer = rainbowDrawer;
        this.saveFolder = saveFolder;
        this.authority = authority;
    }

    Uri takeScreenshot() {
        Uri uri = null;
        try {
            Bitmap bitmap = rainbowDrawer.getGraphics().getBitmap();
            saveFolder.mkdirs();
            File file = new File(saveFolder, PICTURE_TITLE);
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            uri = FileProvider.getUriForFile(context, authority, file);

            stream.flush();
            stream.close();
        } catch (IOException fileNotFound) {
            Log.e(fileNotFound.getMessage());
        }

        return uri;
    }
}
