package com.juankysoriano.rainbow.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by juankysoriano on 08/04/2014.
 */
public abstract class RainbowIO {
    /**
     * Simplified method to open a Java InputStream.
     * <p/>
     * This method is useful if you want to use the facilities provided by
     * Imagine to easily open things from the data folder or from a URL
     */
    public static InputStream createInput(Context context, final String filename) {
        final InputStream input = createInputRaw(context, filename);
        if ((input != null) && filename.toLowerCase().endsWith(".gz")) {
            try {
                return new GZIPInputStream(input);
            } catch (final IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return input;
    }

    /**
     * Call createInput() without automatic gzip decompression.
     */
    public static InputStream createInputRaw(Context context, final String filename) {
        InputStream stream = null;

        if (filename == null) {
            return null;
        }

        if (filename.length() == 0) {
            return null;
        }

        if (filename.indexOf(":") != -1) { // at least smells like URL
            try {
                HttpGet httpRequest = null;
                httpRequest = new HttpGet(URI.create(filename));
                final HttpClient httpclient = new DefaultHttpClient();
                final HttpResponse response = httpclient.execute(httpRequest);
                final HttpEntity entity = response.getEntity();
                return entity.getContent();

            } catch (final MalformedURLException mfue) {
            } catch (final FileNotFoundException fnfe) {
            } catch (final IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        // Try the assets folder
        final AssetManager assets = context.getAssets();
        try {
            stream = assets.open(filename);
            if (stream != null) {
                return stream;
            }
        } catch (final IOException e) {
        }

        // Maybe this is an absolute path, didja ever think of that?
        final File absFile = new File(filename);
        if (absFile.exists()) {
            try {
                stream = new FileInputStream(absFile);
                if (stream != null) {
                    return stream;
                }
            } catch (final FileNotFoundException fnfe) {
                // fnfe.printStackTrace();
            }
        }

        // Maybe this is a file that was written by the sketch later.
        final File sketchFile = new File(sketchPath(context, filename));
        if (sketchFile.exists()) {
            try {
                stream = new FileInputStream(sketchFile);
                if (stream != null) {
                    return stream;
                }
            } catch (final FileNotFoundException fnfe) {
            }
        }

        // Attempt to load the file more directly. Doesn't like paths.
        try {
            stream = context.openFileInput(filename);
            if (stream != null) {
                return stream;
            }
        } catch (final FileNotFoundException e) {

        }

        return null;
    }

    public static InputStream createInput(final File file) {
        if (file == null) {
            throw new IllegalArgumentException("File passed to createInput() was null");
        }
        try {
            final InputStream input = new FileInputStream(file);
            if (file.getName().toLowerCase().endsWith(".gz")) {
                return new GZIPInputStream(input);
            }
            return input;

        } catch (final IOException e) {
            System.err.println("Could not createInput() for " + file);
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] loadBytes(final InputStream input) {
        try {
            final BufferedInputStream bis = new BufferedInputStream(input);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();

            int c = bis.read();
            while (c != -1) {
                out.write(c);
                c = bis.read();
            }
            return out.toByteArray();

        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] loadBytes(final File file) {
        final InputStream is = createInput(file);
        return loadBytes(is);
    }

    public static String[] loadStrings(final File file) {
        final InputStream is = createInput(file);
        if (is != null) {
            return loadStrings(is);
        }
        return null;
    }

    public static String[] loadStrings(final BufferedReader reader) {
        try {
            String lines[] = new String[100];
            int lineCount = 0;
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (lineCount == lines.length) {
                    final String temp[] = new String[lineCount << 1];
                    System.arraycopy(lines, 0, temp, 0, lineCount);
                    lines = temp;
                }
                lines[lineCount++] = line;
            }
            reader.close();

            if (lineCount == lines.length) {
                return lines;
            }

            // resize array to appropriate amount for these lines
            final String output[] = new String[lineCount];
            System.arraycopy(lines, 0, output, 0, lineCount);
            return output;

        } catch (final IOException e) {
            e.printStackTrace();
            // throw new RuntimeException("Error inside loadStrings()");
        }
        return null;
    }

    public static String[] loadStrings(final InputStream input) {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));

            String lines[] = new String[100];
            int lineCount = 0;
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (lineCount == lines.length) {
                    final String temp[] = new String[lineCount << 1];
                    System.arraycopy(lines, 0, temp, 0, lineCount);
                    lines = temp;
                }
                lines[lineCount++] = line;
            }
            reader.close();

            if (lineCount == lines.length) {
                return lines;
            }

            // resize array to appropriate amount for these lines
            final String output[] = new String[lineCount];
            System.arraycopy(lines, 0, output, 0, lineCount);
            return output;

        } catch (final IOException e) {
            e.printStackTrace();
            // throw new RuntimeException("Error inside loadStrings()");
        }
        return null;
    }

    public static OutputStream createOutput(final File file) {
        try {
            final FileOutputStream fos = new FileOutputStream(file);
            if (file.getName().toLowerCase().endsWith(".gz")) {
                return new GZIPOutputStream(fos);
            }
            return fos;

        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean saveStream(final File targetFile, final InputStream sourceStream) {
        File tempFile = null;
        try {
            final File parentDir = targetFile.getParentFile();
            createPath(targetFile);
            tempFile = File.createTempFile(targetFile.getName(), null, parentDir);

            final BufferedInputStream bis = new BufferedInputStream(sourceStream, 16384);
            final FileOutputStream fos = new FileOutputStream(tempFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            final byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            bos.flush();
            bos.close();
            bos = null;

            if (targetFile.exists() && !targetFile.delete()) {
                System.err.println("Could not replace " + targetFile.getAbsolutePath() + ".");
            }

            if (!tempFile.renameTo(targetFile)) {
                System.err.println("Could not rename temporary file " + tempFile.getAbsolutePath());
                return false;
            }
            return true;

        } catch (final IOException e) {
            if (tempFile != null) {
                tempFile.delete();
            }
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Saves bytes to a specific File location specified by the user.
     */
    public static void saveBytes(final File file, final byte buffer[]) {
        try {
            final String filename = file.getAbsolutePath();
            createPath(filename);
            OutputStream output = new FileOutputStream(file);
            if (file.getName().toLowerCase().endsWith(".gz")) {
                output = new GZIPOutputStream(output);
            }
            saveBytes(output, buffer);
            output.close();

        } catch (final IOException e) {
            System.err.println("error saving bytes to " + file);
            e.printStackTrace();
        }
    }

    /**
     * Spews a buffer of bytes to an OutputStream.
     */
    public static void saveBytes(final OutputStream output, final byte buffer[]) {
        try {
            output.write(buffer);
            output.flush();

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveStrings(final File file, final String strings[]) {
        try {
            final String location = file.getAbsolutePath();
            createPath(location);
            OutputStream output = new FileOutputStream(location);
            if (file.getName().toLowerCase().endsWith(".gz")) {
                output = new GZIPOutputStream(output);
            }
            saveStrings(output, strings);
            output.close();

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveStrings(final OutputStream output, final String strings[]) {
        try {
            final OutputStreamWriter osw = new OutputStreamWriter(output, "UTF-8");
            final PrintWriter writer = new PrintWriter(osw);
            for (int i = 0; i < strings.length; i++) {
                writer.println(strings[i]);
            }
            writer.flush();
        } catch (final UnsupportedEncodingException e) {
        } // will not happen
    }

    /**
     * Takes a path and creates any in-between folders if they don't already
     * exist. Useful when trying to save to a subfolder that may not actually
     * exist.
     */
    public static void createPath(final String path) {
        createPath(new File(path));
    }

    public static void createPath(final File file) {
        try {
            final String parent = file.getParent();
            if (parent != null) {
                final File unit = new File(parent);
                if (!unit.exists()) {
                    unit.mkdirs();
                }
            }
        } catch (final SecurityException se) {
            System.err.println("You don't have permissions to create " + file.getAbsolutePath());
        }
    }

    /**
     * Save the contents of a stream to a file in the sketch folder. This is
     * basically saveBytes(blah, loadBytes()), but done more efficiently (and
     * with less confusing syntax).
     */
    public static boolean saveStream(Context context, final String targetFilename, final String sourceLocation) {
        return saveStream(context, saveFile(context, targetFilename), sourceLocation);
    }

    /**
     * Identical to the other saveStream(), but writes to a File object, for
     * greater control over the file location. Note that unlike other api
     * methods, this will not automatically compress or uncompress gzip files.
     */
    public static boolean saveStream(Context context, final File targetFile, final String sourceLocation) {
        return saveStream(targetFile, createInputRaw(context, sourceLocation));
    }

    public static boolean saveStream(Context context, final String targetFilename, final InputStream sourceStream) {
        return saveStream(saveFile(context, targetFilename), sourceStream);
    }

    /**
     * Saves bytes to a file to inside the sketch folder. The filename can be a
     * relative path, i.e. "poo/bytefun.txt" would save to a file named
     * "bytefun.txt" to a subfolder called 'poo' inside the sketch folder. If
     * the in-between subfolders don't exist, they'll be created.
     */
    public static void saveBytes(Context context, final String filename, final byte buffer[]) {
        saveBytes(saveFile(context, filename), buffer);
    }

    public static void saveStrings(Context context, final String filename, final String strings[]) {
        saveStrings(saveFile(context, filename), strings);
    }

    /**
     * Prepend the sketch folder path to the filename (or path) that is passed
     * in. External libraries should use this function to save to the sketch
     * folder.
     * <p/>
     * Note that when running as an applet inside a web browser, the sketchPath
     * will be set to null, because security restrictions prevent applets from
     * accessing that information.
     * <p/>
     * This will also cause an error if the sketch is not inited properly,
     * meaning that init() was never called on the Imagine when hosted my some
     * other main() or by other code. For proper use of init(), see the examples
     * in the main description text for Imagine.
     */
    public static String sketchPath(Context context, final String where) {
        return context.getFileStreamPath(where).getAbsolutePath();
    }

    public static File sketchFile(Context context, final String where) {
        return new File(sketchPath(context, where));
    }

    /**
     * Returns a path inside the applet folder to save to. Like sketchPath(),
     * but creates any in-between folders so that things save properly.
     * <p/>
     * All saveXxxx() functions use the path to the sketch folder, rather than
     * its data folder. Once exported, the data folder will be found inside the
     * jar file of the exported application or applet. In this case, it's not
     * possible to save data into the jar file, because it will often be running
     * from a server, or marked in-use if running from a local file system. With
     * this in mind, saving to the data path doesn't make sense anyway. If you
     * know you're running locally, and want to save to the data folder, use
     * <TT>saveXxxx("data/blah.dat")</TT>.
     */
    public static String savePath(Context context, final String where) {
        if (where == null) {
            return null;
        }
        final String filename = sketchPath(context, where);
        createPath(filename);
        return filename;
    }

    /**
     * Identical to savePath(), but returns a File object.
     */
    public static File saveFile(Context context, final String where) {
        return new File(savePath(context, where));
    }

    public byte[] loadBytes(Context context, final String filename) {
        final InputStream is = createInput(context, filename);
        if (is != null) {
            return loadBytes(is);
        }

        System.err.println("The file \"" + filename + "\" " + "is missing or inaccessible, make sure " + "the URL is valid or that the file has been " + "added to your sketch and is readable.");
        return null;
    }

    /**
     * Load data from a file and shove it into a String array.
     * <p/>
     * Exceptions are handled internally, when an error, occurs, an exception is
     * printed to the console and 'null' is returned, but the program continues
     * running. This is a tradeoff between 1) showing the user that there was a
     * problem but 2) not requiring that all i/o code is contained in try/catch
     * blocks, for the sake of new users (or people who are just trying to get
     * things done in a "scripting" fashion. If you want to handle exceptions,
     * use Java methods for I/O.
     */
    public static String[] loadStrings(Context context, final String filename) {
        final InputStream is = createInput(context, filename);
        if (is != null) {
            return loadStrings(is);
        }

        System.err.println("The file \"" + filename + "\" " + "is missing or inaccessible, make sure " + "the URL is valid or that the file has been " + "added to your sketch and is readable.");
        return null;
    }

    /**
     * Similar to createInput() (formerly openStream), this creates a Java
     * OutputStream for a given filename or path. The file will be created in
     * the sketch folder, or in the same folder as an exported application.
     * <p/>
     * If the path does not exist, intermediate folders will be created. If an
     * exception occurs, it will be printed to the console, and null will be
     * returned.
     * <p/>
     * Future releases may also add support for handling HTTP POST via this
     * method (for better symmetry with createInput), however that's maybe a
     * little too clever (and then we'd have to add the same features to the
     * other file functions like createWriter). Who you callin' bloated?
     */
    public static OutputStream createOutput(Context context, final String filename) {
        try {
            // in spite of appearing to be the 'correct' option, this doesn't
            // allow
            // for paths, so no subfolders, none of that savePath() goodness.
            // Context context = getApplicationContext();
            // // MODE_PRIVATE is default, should we use that instead?
            // return context.openFileOutput(filename, MODE_WORLD_READABLE);

            File file = new File(filename);
            if (!file.isAbsolute()) {
                file = new File(sketchPath(context, filename));
            }
            final FileOutputStream fos = new FileOutputStream(file);
            if (file.getName().toLowerCase().endsWith(".gz")) {
                return new GZIPOutputStream(fos);
            }
            return fos;

        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
