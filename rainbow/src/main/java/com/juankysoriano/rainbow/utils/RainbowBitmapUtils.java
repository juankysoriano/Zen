package com.juankysoriano.rainbow.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;

import com.juankysoriano.rainbow.core.graphics.RainbowImage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class RainbowBitmapUtils {

    private static final String TAG = "PROCESSING";

    public static Bitmap getBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        return getMutableBitmap(drawable);
    }

    public static Bitmap getMutableBitmap(Drawable drawable) {
        if(drawable == null) {
            return null;
        }

        if (drawable.getIntrinsicHeight() > 0 && drawable.getIntrinsicWidth() > 0) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }

        return null;
    }

    public static Bitmap getBitmap(Context context, int resId, int reqWidth, int reqHeight) {
        try {
            return Picasso.with(context).load(resId).resize(reqWidth, reqHeight).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmap(Context context, String path, int reqWidth, int reqHeight) {
        try {
            if (path.startsWith("http")) {
                return Picasso.with(context).load(path).resize(reqWidth, reqHeight).get();
            } else {
                return Picasso.with(context).load("file:" + path).resize(reqWidth, reqHeight).get();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmap(Context context, File file, int reqWidth, int reqHeight) {
        try {
            return Picasso.with(context).load(file).resize(reqWidth, reqHeight).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmap(Context context, Uri uri, int reqWidth, int reqHeight) {
        try {
            return Picasso.with(context).load(uri).resize(reqWidth, reqHeight).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmap(Context context, int resId, int reqWidth, int reqHeight, int mode) {
        try {
            if (mode == RainbowImage.LOAD_CENTER_CROP) {
                return Picasso.with(context).load(resId).resize(reqWidth, reqHeight).centerCrop().get();
            } else if (mode == RainbowImage.LOAD_CENTER_INSIDE) {
                return Picasso.with(context).load(resId).resize(reqWidth, reqHeight).centerInside().get();
            } else if (mode == RainbowImage.LOAD_ORIGINAL_SIZE) {
                return Picasso.with(context).load(resId).get();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmap(Context context, String path, int reqWidth, int reqHeight, int mode) {
        try {
            if (path.startsWith("http")) {
                if (mode == RainbowImage.LOAD_CENTER_CROP) {
                    return Picasso.with(context).load(path).resize(reqWidth, reqHeight).centerCrop().get();
                } else if (mode == RainbowImage.LOAD_CENTER_INSIDE) {
                    return Picasso.with(context).load(path).resize(reqWidth, reqHeight).centerInside().get();
                } else if (mode == RainbowImage.LOAD_ORIGINAL_SIZE) {
                    return Picasso.with(context).load(path).get();
                }
            } else {
                if (mode == RainbowImage.LOAD_CENTER_CROP) {
                    return Picasso.with(context).load("file:" + path).resize(reqWidth, reqHeight).centerCrop().get();
                } else if (mode == RainbowImage.LOAD_CENTER_INSIDE) {
                    return Picasso.with(context).load("file:" + path).resize(reqWidth, reqHeight).centerInside().get();
                } else if (mode == RainbowImage.LOAD_ORIGINAL_SIZE) {
                    return Picasso.with(context).load("file:" + path).get();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmap(Context context, File file, int reqWidth, int reqHeight, int mode) {
        try {
            if (mode == RainbowImage.LOAD_CENTER_CROP) {
                return Picasso.with(context).load(file).resize(reqWidth, reqHeight).centerCrop().get();
            } else if (mode == RainbowImage.LOAD_CENTER_INSIDE) {
                return Picasso.with(context).load(file).resize(reqWidth, reqHeight).centerInside().get();
            } else if (mode == RainbowImage.LOAD_ORIGINAL_SIZE) {
                return Picasso.with(context).load(file).get();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmap(Context context, Uri uri, int reqWidth, int reqHeight, int mode) {
        try {
            if (mode == RainbowImage.LOAD_CENTER_CROP) {
                return Picasso.with(context).load(uri).resize(reqWidth, reqHeight).centerCrop().get();
            } else if (mode == RainbowImage.LOAD_CENTER_INSIDE) {
                return Picasso.with(context).load(uri).resize(reqWidth, reqHeight).centerInside().get();
            } else if (mode == RainbowImage.LOAD_ORIGINAL_SIZE) {
                return Picasso.with(context).load(uri).get();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getPhotoBitmap(Context context, String filename, int reqWidth, int reqHeight) {
        float rotation = (float) getRotation(filename);
        int width = (rotation == 0 || rotation == 180) ? reqWidth : reqHeight;
        int height = (rotation == 0 || rotation == 180) ? reqHeight : reqWidth;

        try {
            return Picasso.with(context).load("file:" + filename).resize(width, height).rotate(rotation).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getPhotoBitmap(Context context, String filename) {
        float rotation = (float) getRotation(filename);

        try {
            return Picasso.with(context).load("file:" + filename).rotate(rotation).get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static int getRotation(String filename) {
        ExifInterface exif;
        int exifOrientation = ExifInterface.ORIENTATION_NORMAL;
        int cameraId = -1;
        try {
            exif = new ExifInterface(filename);
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            cameraId = exif.getAttribute("UserComment") != null ? Integer.valueOf(exif.getAttribute("UserComment")) : -1;
        } catch (Exception e) {
            cameraId = -1;
            exifOrientation = ExifInterface.ORIENTATION_NORMAL;
        } finally {
            if (exifOrientation >= 0) {
                return exifOrientationToDegrees(exifOrientation, cameraId);
            }
        }
        return 0;
    }

    /**
     * Get rotation in degrees
     */
    private static int exifOrientationToDegrees(int exifOrientation, int cameraId) {
        int rotation = exifOrientation;
        int degrees = 0;
        int result = 0;

        if (cameraId >= 0) {
            android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
            android.hardware.Camera.getCameraInfo(cameraId, info);

            switch (rotation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degrees = cameraId == Camera.CameraInfo.CAMERA_FACING_BACK && Camera.getNumberOfCameras() > 1 ? 0 : 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degrees = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degrees = cameraId == Camera.CameraInfo.CAMERA_FACING_BACK && Camera.getNumberOfCameras() > 1 ? 180 : 1;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degrees = 90;
                    break;
            }

            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360; // compensate the mirror
            } else { // back-facing
                result = (info.orientation - degrees + 360) % 360;
            }
        } else {

            switch (rotation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    result = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    result = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    result = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    result = 0;
                    break;
            }
        }

        return result;
    }

}
