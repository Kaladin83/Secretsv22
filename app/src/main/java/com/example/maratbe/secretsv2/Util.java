package com.example.maratbe.secretsv2;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;

/**
 * Created by MARATBE on 12/25/2017.
 */

public class Util extends Application{

    public static Drawable createBorder(Context context,int radius, int color, boolean gradient, int stroke)
    {
        GradientDrawable gd;
        if (!gradient)
        {
            gd = new GradientDrawable();
            gd.setColor(color);
        }
        else
        {
            int[] colors = {Color.BLACK, Color.RED, ContextCompat.getColor(context ,R.color.transparent_green)};
            gd = new GradientDrawable(GradientDrawable.Orientation.BR_TL, colors);
        }
        gd.setCornerRadius(radius);
        gd.setStroke(stroke, Color.DKGRAY);
        return gd;
    }
}
