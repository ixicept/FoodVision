package com.example.myapplication.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;
import java.util.function.Consumer;

public class CameraUtils {
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int GALLERY_REQUEST_CODE = 200;

    public static void openCamera(Activity activity, Consumer<Bitmap> onImageCaptured) {
        activity.startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_CODE);
    }

    public static void openGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    public static void handleGalleryResult(int requestCode, int resultCode, Intent data, Activity activity, Consumer<Bitmap> onImageSelected) {
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);
                onImageSelected.accept(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}