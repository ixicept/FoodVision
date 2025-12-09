package com.example.myapplication.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

public class TextApi {

    public static String extractText(Bitmap img) {
        return "Calories: 210, Sugar: 15g, Fat: 7g";
    }

    public static String extractTextFromUri(Context context, Uri uri) {
        // TODO call your real OCR API
        return "Calories: 210 Sugar: 15 Fat: 7";
    }

}