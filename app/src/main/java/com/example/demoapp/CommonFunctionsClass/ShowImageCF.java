package com.example.demoapp.CommonFunctionsClass;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ShowImageCF {

    //Function to convert Bitmap Image to byte[]
    public byte[] convertToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }


    //Function to convert byte[] into Bitmap Image
    public static Bitmap convertToBitmapImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
