package com.istrong.tangramandroiddemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;

import com.istrong.tangramandroiddemo.CustomApplication;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

    /**
     * 获取assets目录下文件的字节数组
     *
     * @param context  上下文
     * @param fileName assets目录下的文件
     * @return assets目录下文件的字节数组
     */
    public static byte[] getAssertsFile(Context context, String fileName) {
        InputStream inputStream = null;
        AssetManager assetManager = context.getAssets();
        try {
            inputStream = assetManager.open(fileName);
            if (inputStream == null) {
                return null;
            }

            BufferedInputStream bis = null;
            int length;
            try {
                bis = new BufferedInputStream(inputStream);
                length = bis.available();
                byte[] data = new byte[length];
                bis.read(data);

                return data;
            } catch (IOException e) {

            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (Exception e) {

                    }
                }
            }

            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void launchActivity(Class<? extends Activity> clz) {
        Intent intent = new Intent(CustomApplication.applicationContext, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CustomApplication.applicationContext.startActivity(intent);
    }

}
