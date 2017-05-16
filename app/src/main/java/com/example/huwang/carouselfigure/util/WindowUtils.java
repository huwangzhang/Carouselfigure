package com.example.huwang.carouselfigure.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huwang on 2017/5/16.
 */

public class WindowUtils {
    public static int getScreenWidth(Activity context){
        WindowManager wm = context.getWindowManager();
        Display display = wm.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    @NonNull
    public static List<Bitmap> initBitmaps(int[] ids, Context context) {
        List<Bitmap> list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), ids[i]);
            list.add(bitmap);
        }
        return list;
    }
}
