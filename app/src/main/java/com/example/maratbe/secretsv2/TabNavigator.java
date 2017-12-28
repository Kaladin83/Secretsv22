package com.example.maratbe.secretsv2;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TabNavigator extends AppCompatActivity implements Constants
{
    private int numOfTabs = 3;
    private int screenHeight, screenWidth, objectHeight, buttonWidth;
    private MyTabLayout tabs;
    private ViewPager pager;
    private static int activeThemeNumber = 0;
    public static Toolbar mToolb;
    private static Theme activeTheme = new Theme();
    
    private static ArrayList<Theme> listOfThemes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_navigator);
        setDimentions();
        mToolb = (Toolbar) findViewById(R.id.my_action_bar);
        mToolb.setId(100);
        setSupportActionBar(mToolb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().setStatusBarColor(Color.GRAY);

        createTabs();
        setThemes();
        setTheme();
    }

    public void setDimentions()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        objectHeight = screenHeight/7;
        buttonWidth = screenWidth/12;
    }

    private void createTabs()
    {
        tabs = (MyTabLayout) findViewById(R.id.navigation_tab);
        pager = (ViewPager) findViewById(R.id.navigator_pager);
        setUpPager(pager);
        tabs.setupWithViewPager(pager);
        tabs.setNUmberOfTabs(numOfTabs);
        pager.setCurrentItem(1);
    }

    public void setTheme()
    {
        activeTheme = listOfThemes.get(activeThemeNumber);
        getWindow().setStatusBarColor(activeTheme.getStatusBarColor());
        mToolb.setBackgroundColor(activeTheme.getActiveBarColor());
        tabs.setBackgroundColor(activeTheme.getTabsColor());
        tabs.setSelectedTabIndicatorColor(activeTheme.getSelectedTitleColor());
    }

    public static void setActiveThemeNumber(int num)
    {
        activeThemeNumber = num;
    }

    public static int getActiveThemeNumber()
    {
        return activeThemeNumber;
    }


    public static Theme getThemeAt(int index)
    {
        return listOfThemes.get(index);
    }

    private void setThemes()
    {
        int colors[] = new int[2];
        colors[0] = ContextCompat.getColor(this, R.color.colorPrimary);
        colors[1] = ContextCompat.getColor(this, R.color.dark_gray);
        listOfThemes.add(new Theme("nightTheme1","Night Theme 1", THEME_NIGHT, R.drawable.cold1, Color.GRAY,
                ContextCompat.getColor(this,R.color.dark_gray), ContextCompat.getColor(this,R.color.light_black),"radio_night", colors, Typeface.createFromAsset(getAssets(),"Patty Sans.ttf"),
                Typeface.createFromAsset(getAssets(),"Xerox Serif Narrow Italic.ttf")));

        colors[0] = ContextCompat.getColor(this, R.color.light_green_gradient);
        colors[1] = ContextCompat.getColor(this, R.color.dark_green_gradient);
        listOfThemes.add(new Theme("coldTheme1","Cold Theme 1", THEME_WINTER, R.drawable.cold1, ContextCompat.getColor(this,R.color.blue_status_bar),
                ContextCompat.getColor(this,R.color.blue_active_bar), ContextCompat.getColor(this,R.color.light_black),"radio_winter", colors, Typeface.createFromAsset(getAssets(),"Patty Sans.ttf"),
                Typeface.createFromAsset(getAssets(),"Xerox Serif Narrow Italic.ttf")));

        colors[0] = ContextCompat.getColor(this, R.color.light_blue_gradient);
        colors[1] = ContextCompat.getColor(this, R.color.dark_blue_gradient);
        listOfThemes.add(new Theme("coldTheme2","Cold Theme 2", THEME_SPRING, R.drawable.cold2, ContextCompat.getColor(this,R.color.green_status_bar),
                ContextCompat.getColor(this,R.color.green_active_bar), ContextCompat.getColor(this,R.color.light_black),"radio_spring", colors, Typeface.createFromAsset(getAssets(),"Patty Sans.ttf")
                ,Typeface.createFromAsset(getAssets(),"Xerox Serif Narrow Italic.ttf")));

        colors[0] = ContextCompat.getColor(this, R.color.light_red_gradient);
        colors[1] = ContextCompat.getColor(this, R.color.dark_red_gradient);
        listOfThemes.add(new Theme("hotTheme1","Hot Theme 1", THEME_AUTUMN, R.drawable.hot1, ContextCompat.getColor(this,R.color.yellow_status_bar),
                ContextCompat.getColor(this,R.color.yellow_active_bar), ContextCompat.getColor(this,R.color.light_black),"radio_summer", colors, Typeface.createFromAsset(getAssets(),"Patty Sans.ttf"),
                Typeface.createFromAsset(getAssets(),"Xerox Serif Narrow Italic.ttf")));

        colors[0] = ContextCompat.getColor(this, R.color.light_yellow_gradient);
        colors[1] = ContextCompat.getColor(this, R.color.dark_yellow_gradient);
        listOfThemes.add(new Theme("hotTheme2","Hot Theme 2", THEME_SUMMER, R.drawable.hot2, ContextCompat.getColor(this,R.color.red_status_bar),
                ContextCompat.getColor(this,R.color.red_active_bar), ContextCompat.getColor(this,R.color.light_black),"radio_autumn", colors, Typeface.createFromAsset(getAssets(),"Patty Sans.ttf"),
                Typeface.createFromAsset(getAssets(),"Xerox Serif Narrow Italic.ttf")));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab_navigator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUpPager (ViewPager pager)
    {
        MyNavigationAdapter adapter = new MyNavigationAdapter(getSupportFragmentManager());

        adapter.addPage(new Secrets(), "Secrets");
        adapter.addPage(new TopTen(), "Top ten");
        adapter.addPage(new Thoughts(), "Thoughts");

        pager.setAdapter(adapter);
    }

    public class MyNavigationAdapter extends FragmentPagerAdapter
    {
        private ArrayList<Fragment> fragments = new ArrayList<>();
        private ArrayList<String> pageTitles = new ArrayList<>();

        public MyNavigationAdapter(FragmentManager manager)
        {
            super(manager);
        }

        public void addPage(Fragment page, String title)
        {
            fragments.add(page);
            pageTitles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return pageTitles.get(position);
        }

        @Override
        public Fragment getItem(int position)
        {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return numOfTabs;
        }
    }
}
