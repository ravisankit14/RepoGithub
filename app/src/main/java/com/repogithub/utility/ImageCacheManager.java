package com.repogithub.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.repogithub.model.GetRepo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCacheManager {

    public static Bitmap getBitmap(Context context, GetRepo getRepo) {

        String fileName = context.getCacheDir() + "/" + getRepo.getName();
        File file = new File(fileName);
        if (file.exists()) {
            try {
                return BitmapFactory.decodeStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void putBitmap(Context context, GetRepo getRepo, Bitmap bitmap) {
        String fileName = context.getCacheDir() + "/" + getRepo.getName();
        File file = new File(fileName);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}