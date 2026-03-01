package com.example.firestore.utils;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
public class ImageUtils {
    public static byte[] uriToBlob(ContentResolver resolver, Uri uri) {
        try {
            InputStream inputStream = resolver.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Bitmap blobToBitmap(byte[] blob) {
        return BitmapFactory.decodeByteArray(blob, 0, blob.length);
    }
}
