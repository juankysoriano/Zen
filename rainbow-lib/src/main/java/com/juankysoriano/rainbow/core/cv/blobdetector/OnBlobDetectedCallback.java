package com.juankysoriano.rainbow.core.cv.blobdetector;

public interface OnBlobDetectedCallback {
    void onBlobDetected(Blob b);
    boolean isToDiscardBlob(Blob b);
    void onBlobDetectionFinish();
}
