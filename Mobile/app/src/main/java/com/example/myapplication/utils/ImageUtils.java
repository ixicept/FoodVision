package com.example.myapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtils {

    public static Bitmap load(Context c, int id) {
        return BitmapFactory.decodeResource(c.getResources(), id);
    }
}