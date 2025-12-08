package com.example.myapplication.utils;

import android.app.Activity;
import android.graphics.Bitmap;

public class CameraUtils {

    public interface CameraCallback {
        void onImageCaptured(Bitmap bitmap);
    }

    public static void openCamera(Activity a, CameraCallback cb) {
        Bitmap dummy = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
        cb.onImageCaptured(dummy);
    }
}