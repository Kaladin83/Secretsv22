package com.example.maratbe.secrets;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class TabNavigator extends AppCompatActivity implements Constants, Serializable, ViewPager.OnPageChangeListener {
    private int numOfTabs = 3, tabHeight = 70, pageToUpdate;
    private boolean initialSizeObtained;
    private MyTabLayout tabs;
    private ViewPager pager;
    public Toolbar mToolb;
    private static Theme activeTheme = new Theme();
    private TopTen topTen;
    private static TabNavigator tabNavigator;
    private Eavesdrops eavesdrops;
    private Share share;
    private Menu mainMenu;
    private static ArrayList<Theme> listOfThemes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_navigator);
        //Bundle bundle = getIntent().getExtras();
       // activeThemeNumber = bundle.getInt("Theme");

        tabNavigator = this;
        setDimentions();
        mToolb = (Toolbar) findViewById(R.id.my_action_bar);
        setSupportActionBar(mToolb);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        getWindow().setStatusBarColor(Color.GRAY);
        createTabs();
    }

    public void updateLogin(boolean login) {
        MainActivity.getUser(0).logIn(login);
        MainActivity.getLocalStorage().setLoggedIn(login);
        updateMenu(login);
    }

    private void updateMenu(boolean isLoggedIn) {
        mainMenu.getItem(0).setVisible(isLoggedIn);
        mainMenu.getItem(1).setVisible(!isLoggedIn);
        mainMenu.getItem(2).setVisible(!isLoggedIn);
        mainMenu.getItem(3).setVisible(isLoggedIn);
        mainMenu.getItem(4).setVisible(isLoggedIn);
        mainMenu.getItem(5).setVisible(isLoggedIn);
    }

    @Override
    public void onBackPressed() {

        switch(pager.getCurrentItem())
        {
            case 0:
                eavesdrops.revertTagsResult(true);
                pager.setCurrentItem(1, true); break;
            case 2:
                pager.setCurrentItem(1, true); break;
            case 1:
                finish(); break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab_navigator, menu);
        this.mainMenu = menu;

        updateMenu(MainActivity.getUser(0).isLoggedIn());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id)
        {
            case R.id.settings:
                return true;
            case R.id.login:
                Intent intent = new Intent(this, CreateAccount.class);
                intent.putExtra("METHOD", "signIn");
                startActivity(intent);break;
            case R.id.register:
                intent = new Intent(this, CreateAccount.class);
                intent.putExtra("METHOD", "register");
                startActivity(intent); break;
            case R.id.account:
                intent = new Intent(this, Account.class);
                intent.putExtra("USER_NAME", MainActivity.getUser(0).getUserName());
                startActivity(intent); break;
            case R.id.theme:
                intent = new Intent(this, ThemeChooser.class);
                startActivity(intent); break;
            case R.id.favorites:
                intent = new Intent(this, Favorites.class);
                startActivity(intent); break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static TabNavigator getTabNavigatorInstance()
    {
        return tabNavigator;
    }

    public Eavesdrops getEavesdropsInstance()
    {
        return eavesdrops;
    }
    public TopTen getTopTenInstance(){ return topTen;}

    public void selectPage(int page) {

        pager.setCurrentItem(page,true);
    }

    public void recreateListView(int page)
    {
        pageToUpdate = page;
        runOnUiThread(() ->  {
            if (pageToUpdate == 0)
            {
                eavesdrops.showSecrets();
            }
            else
            {
                topTen.showSecrets(1);
            }
        });
    }

    public void setUpPager (ViewPager pager)
    {
        MyNavigationAdapter adapter = new MyNavigationAdapter(getSupportFragmentManager());

        eavesdrops = new Eavesdrops();
        topTen = new TopTen();
        share = new Share();
        topTen.setTabNavigatorInstance(this);
        eavesdrops.setTabNavigatorInstance(this);
        share.setTabNavigatorInstance(this);

        adapter.addPage(eavesdrops, "Eavesdrop");
        adapter.addPage(topTen, "Top ten");
        adapter.addPage(share, "Share");

        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);
      //  pager.setOnPageChangeListener(this);
    }

    public void setDimentions()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    private void createTabs()
    {
        tabs = (MyTabLayout) findViewById(R.id.navigation_tab);
        setThemes();
        setTheme();
        pager = (ViewPager) findViewById(R.id.navigator_pager);
        setUpPager(pager);
        tabs.setupWithViewPager(pager);
        tabs.setNUmberOfTabs(numOfTabs);
        tabs.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (initialSizeObtained)
                return;
            initialSizeObtained = true;
            tabHeight = tabs.getMeasuredHeight();
        });
        hideAnimation.setDuration(300);
        showAnimation.setDuration(300);
        pager.setOffscreenPageLimit(3);
        pager.setCurrentItem(1);
        /*  pager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (initialSizeObtained)
                    return;
                initialSizeObtained = true;
                tabHeight = pager.getMeasuredHeight();
            }
        });
        hidePagerAnimation.setDuration(500);
        showPagerAnimation.setDuration(500);*/
    }

   // public Fragment getFragmentByPosition(int position) {
       // return ContentFragment frag = (ContentFragment) adapter.getItem(pos);
   // }

    public static Theme getThemeAt(int index)
    {
        return listOfThemes.get(index);
    }

    private void setThemes()
    {
        int gradientColors[] = new int[3];
        gradientColors[0] = ContextCompat.getColor(this, R.color.light_gray);
        gradientColors[1] = ContextCompat.getColor(this, R.color.transparent_purple);
        gradientColors[2] = ContextCompat.getColor(this, R.color.transparent_blue);
        int selectedColors[] = new int[3];
        selectedColors[0] = ContextCompat.getColor(this, R.color.white);
        selectedColors[1] = ContextCompat.getColor(this, R.color.light_blue);
        selectedColors[2] = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        int[] drawbles = new int[]{R.drawable.state_list_pin_night, R.drawable.download_night, R.drawable.plus_night, R.drawable.add_comment_night,
                R.drawable.comments_night, R.drawable.star_night, R.drawable.send_night, R.drawable.list_night, R.drawable.tags_night, R.drawable.looking_glass_night};
        listOfThemes.add(new Theme("nightTheme","Night Theme", THEME_NIGHT, R.drawable.night, ContextCompat.getColor(this, R.color.dark_gray),
                ContextCompat.getColor(this, R.color.light_black), ContextCompat.getColor(this, R.color.light_black), ContextCompat.getColor(this, R.color.dark_gray),
                selectedColors, "radio_night", gradientColors, drawbles, Typeface.createFromAsset(getAssets(),"Xerox Serif Narrow Italic.ttf"), Typeface.SANS_SERIF));

        gradientColors[0] = ContextCompat.getColor(this,R.color.ultra_light_gray);
        gradientColors[1] = ContextCompat.getColor(this,R.color.white);
        gradientColors[2] = ContextCompat.getColor(this, R.color.light_blue_gradient);
        selectedColors[0] = ContextCompat.getColor(this, R.color.white);
        selectedColors[1] = ContextCompat.getColor(this, R.color.ultra_light_blue);
        selectedColors[2] = ContextCompat.getColor(this, R.color.title_blue);
        drawbles = new int[]{R.drawable.state_list_pin_winter, R.drawable.download_winter, R.drawable.plus_winter, R.drawable.add_comment_winter,
                R.drawable.comments_winter, R.drawable.star_winter, R.drawable.send_winter, R.drawable.list_winter, R.drawable.tags_winter, R.drawable.looking_glass_winter};
        listOfThemes.add(new Theme("winterTheme","Winter Theme", THEME_WINTER, R.drawable.winter, ContextCompat.getColor(this, R.color.blue_active_bar),
                ContextCompat.getColor(this, R.color.blue_active_bar), ContextCompat.getColor(this, R.color.dark_gray), ContextCompat.getColor(this, R.color.dark_gray),
                selectedColors,"radio_winter", gradientColors, drawbles, Typeface.createFromAsset(getAssets(),"Xerox Serif Narrow Italic.ttf"),Typeface.SERIF));

        gradientColors[0] = ContextCompat.getColor(this, R.color.light_green_gradient);
        gradientColors[1] = ContextCompat.getColor(this, R.color.white);
        gradientColors[2] = ContextCompat.getColor(this, R.color.transparent_green);
        selectedColors[0] = ContextCompat.getColor(this, R.color.white);
        selectedColors[1] = ContextCompat.getColor(this, R.color.light_green_gradient);
        selectedColors[2] = ContextCompat.getColor(this, R.color.title_green);
        drawbles = new int[]{R.drawable.state_list_pin_spring, R.drawable.download_spring, R.drawable.plus_spring, R.drawable.add_comment_spring,
                R.drawable.comments_spring, R.drawable.star_spring, R.drawable.send_spring, R.drawable.list_spring, R.drawable.tags_spring, R.drawable.looking_glass_spring};
        listOfThemes.add(new Theme("springTheme","Spring Theme", THEME_SPRING, R.drawable.spring, ContextCompat.getColor(this, R.color.dark_green_gradient),
                ContextCompat.getColor(this, R.color.dark_green_gradient), ContextCompat.getColor(this, R.color.light_black), ContextCompat.getColor(this, R.color.dark_gray),
                selectedColors,"radio_spring", gradientColors, drawbles, Typeface.createFromAsset(getAssets(),"Xerox Serif Narrow Italic.ttf"),Typeface.SANS_SERIF));

        gradientColors[0] = ContextCompat.getColor(this, R.color.light_red_gradient);
        gradientColors[1] = ContextCompat.getColor(this, R.color.white);
        gradientColors[2] = ContextCompat.getColor(this, R.color.transparent_red);
        selectedColors[0] = ContextCompat.getColor(this, R.color.white);
        selectedColors[1] = ContextCompat.getColor(this, R.color.light_red_gradient);
        selectedColors[2] = ContextCompat.getColor(this, R.color.dark_red_gradient);
        drawbles = new int[]{R.drawable.state_list_pin_autumn, R.drawable.download_autumn, R.drawable.plus_autumn, R.drawable.add_comment_autumn,
                R.drawable.comments_autumn, R.drawable.star_autumn, R.drawable.send_autumn, R.drawable.list_autumn, R.drawable.tags_autumn, R.drawable.looking_glass_autumn};
        listOfThemes.add(new Theme("AutumnTheme","Autumn Theme", THEME_AUTUMN, R.drawable.autumn, ContextCompat.getColor(this, R.color.red_active_bar),
                ContextCompat.getColor(this, R.color.red_active_bar), ContextCompat.getColor(this, R.color.brown), ContextCompat.getColor(this, R.color.dark_gray),
                selectedColors,"radio_autumn", gradientColors, drawbles, Typeface.createFromAsset(getAssets(),"Xerox Serif Narrow Italic.ttf"), Typeface.SANS_SERIF));

        gradientColors[0] = ContextCompat.getColor(this, R.color.light_yellow_gradient);
        gradientColors[1] = ContextCompat.getColor(this, R.color.white);
        gradientColors[2] = ContextCompat.getColor(this, R.color.transparent_yellow);
        selectedColors[0] = ContextCompat.getColor(this, R.color.white);
        selectedColors[1] = ContextCompat.getColor(this, R.color.blue_active_bar);
        selectedColors[2] = ContextCompat.getColor(this, R.color.blue_active_bar);
        drawbles = new int[]{R.drawable.state_list_pin_summer, R.drawable.download_summer2, R.drawable.plus_summer2, R.drawable.add_comment_summer2,
                R.drawable.comments_summer2, R.drawable.star_summer2, R.drawable.send_summer2, R.drawable.list_summer2, R.drawable.tags_summer2, R.drawable.looking_glass_summer2};
        listOfThemes.add(new Theme("SummerTheme","Summer Theme", THEME_SUMMER, R.drawable.summer, ContextCompat.getColor(this, R.color.yellow_active_bar),
                ContextCompat.getColor(this, R.color.yellow_active_bar), ContextCompat.getColor(this, R.color.light_black), ContextCompat.getColor(this, R.color.dark_gray),
                selectedColors,"radio_summer", gradientColors, drawbles, Typeface.createFromAsset(getAssets(),"Xerox Serif Narrow Italic.ttf"), Typeface.SANS_SERIF));
    }

    public void setTheme()
    {
        activeTheme = listOfThemes.get(MainActivity.getActiveThemeNumber());
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setTextColor(activeTheme.getSelectedTitleColor()[0]);
        getWindow().setStatusBarColor(activeTheme.getStatusBarColor());
        mToolb.setBackgroundColor(activeTheme.getActiveBarColor());
        setOverflowButtonColor();
        tabs.setBackgroundColor(activeTheme.getActiveBarColor());
        tabs.setSelectedTabIndicatorColor(activeTheme.getSelectedTitleColor()[1]);
        tabs.setTabTextColors(activeTheme.getSelectedTitleColor()[0], activeTheme.getSelectedTitleColor()[1]);
    }

    public void setOverflowButtonColor() {
        Drawable drawable = mToolb.getOverflowIcon();
        if(drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), activeTheme.getSelectedTitleColor()[0]);
            mToolb.setOverflowIcon(drawable);
        }
    }

    Animation hideAnimation = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t)
        {
           // togglePagerLayout(true);
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) tabs.getLayoutParams();
            params.topMargin = -(int) (tabHeight * interpolatedTime);
            tabs.setLayoutParams(params);

        }
    };

    Animation showAnimation = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
           // togglePagerLayout(false);
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) tabs.getLayoutParams();
            params.topMargin = (int) (tabHeight * (interpolatedTime - 1));
            tabs.setLayoutParams(params);
        }
    };

    //Click on the Olimpic image --> Toggles the top toolbar
    public void toggleTabLayout(boolean isShrink) {
        tabs.clearAnimation();  //Important
        tabs.startAnimation(isShrink? hideAnimation : showAnimation);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 1 && eavesdrops != null)
        {
           /* if (eavesdrops.getIsShrink())
            {
                eavesdrops.toggleFilterLayout(false);
            }*/
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

  /*  Animation hidePagerAnimation = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) pager.getLayoutParams();
            params.topMargin = -(int) (tabHeight * interpolatedTime);
            pager.setLayoutParams(params);
        }
    };

    Animation showPagerAnimation = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) pager.getLayoutParams();
            params.topMargin = (int) (tabHeight * (interpolatedTime - 1));
            pager.setLayoutParams(params);
        }
    };

    //Click on the Olimpic image --> Toggles the top toolbar
    public void togglePagerLayout(boolean isShrink) {
        pager.clearAnimation();  //Important
        pager.startAnimation(isShrink? hidePagerAnimation : showPagerAnimation);
    }*/

    public class MyNavigationAdapter extends FragmentStatePagerAdapter
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
