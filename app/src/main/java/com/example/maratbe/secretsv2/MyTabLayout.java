package com.example.maratbe.secretsv2;

import android.content.Context;
import android.icu.text.DateFormat;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

import java.lang.reflect.Field;

/**
 * Created by MARATBE on 12/24/2017.
 */

public class MyTabLayout extends TabLayout {

    private static final int WIDTH_INDEX = 0;
    private int numOfTabs= 3;
    private static final String SCROLLABLE_TAB_MIN_WIDTH = "mScrollableTabMinWidth";

    public MyTabLayout(Context context) {
        super(context);
        initTabMinWidth();
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTabMinWidth();
    }

    public MyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTabMinWidth();
    }

    public void setNUmberOfTabs(int numOftabs)
    {
        this.numOfTabs = numOftabs;
    }

    private void initTabMinWidth() {
        int width = MainActivity.getScreenWidth();
        int tabMinWidth = width / numOfTabs;

        Field field;
        Field field2;
        try {
            field = TabLayout.class.getDeclaredField(SCROLLABLE_TAB_MIN_WIDTH);
            field.setAccessible(true);
            field.set(this, tabMinWidth);
       //     field2 = TabLayout.class.getDeclaredField();
        //    field2.setAccessible(true);
         //   field2.set(this, width);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}