package com.roisagiv.aroundme.utils;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class AssetReader {
  public static String readFile(Context context, String fileName) throws IOException {
    InputStream inputStream = context.getAssets().open(fileName);

    int size = inputStream.available();
    byte[] buffer = new byte[size];
    inputStream.read(buffer);
    inputStream.close();

    return new String(buffer, "UTF-8");
  }
}
