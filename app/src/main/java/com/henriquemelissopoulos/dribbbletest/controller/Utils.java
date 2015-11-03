package com.henriquemelissopoulos.dribbbletest.controller;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by h on 31/10/15.
 */
public class Utils {

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }


    public static int pageToRequest(Collection c) {
        if (c == null || c.isEmpty()) return 0;
        return (c.size() / Config.SHOTS_PER_PAGE) + 1;
    }


    public static TextView getToolbartvTitle(Toolbar toolbar) {
        TextView tvTitle = null;
        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            tvTitle = (TextView) f.get(toolbar);
        } catch (Exception e) {
            Log.e("getToolbartvTitle", "Problem to get toolbar textview");
        }
        return tvTitle;
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
