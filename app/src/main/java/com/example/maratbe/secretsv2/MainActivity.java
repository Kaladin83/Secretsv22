package com.example.maratbe.secretsv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by MARATBE on 12/24/2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static int screenWidth, screenHeight;;
    private static DataBase dbInstance;

    private static ArrayList<Item> listOfItems = new ArrayList<>();

    private static ArrayList<Tag> listOfTags = new ArrayList<>();

    public static int getListOfItemsLength()
    {
        return  listOfItems.size();
    }

    public static Item getItemAt(int index)
    {
        return  listOfItems.get(index);
    }

    public static ArrayList<Tag> getTags()
    {
        return  listOfTags;
    }

    public static DataBase getDbInstance()
    {
        return dbInstance;
    }

    public static void addItem(Item item)
    {
        listOfItems.add(item);
    }

    public static void addTag(Tag tag)
    {
        listOfTags.add(tag);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        setDimentions();

        Button btn = (Button) findViewById(R.id.enter_button) ;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                150);
        LinearLayout lLayout = (LinearLayout) findViewById(R.id.main_layout);
        lLayout.setBackground(Util.createBorder(MainActivity.this,1, 1,true, 0));
        params.setMargins(20,(screenHeight/2)-150, 20, 0);
        btn.setLayoutParams(params);
        btn.setText("come, read some secrets");
        btn.setTextSize(22);
        btn.setBackground(Util.createBorder(MainActivity.this,1, Color.TRANSPARENT, false, 2));
        Log.d("Transperent color: ","The Color is = "+Color.RED);
        btn.setOnClickListener(this);

        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.dark_gray));

        dbInstance = new DataBase();
        //dbInstance.insertIntoTagListBunch();
        //dbInstance.insertIntoTagsBunch();
       // dbInstance.insertIntoStatisticsBunch();
        dbInstance.selectTopTenData();
    }
    public void setDimentions()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }

    public static int getScreenWidth()
    {
        return screenWidth;
    }

    public static int getScreenHeight()
    {
        return  screenHeight;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, TabNavigator.class);
        startActivity(intent);
    }
}
