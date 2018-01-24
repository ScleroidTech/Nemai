package com.scleroid.nemai.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author Ganesh Kaple
 * @since 23-01-2018
 */

public class GridUtils {
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 230);
        return noOfColumns;
    }

}
