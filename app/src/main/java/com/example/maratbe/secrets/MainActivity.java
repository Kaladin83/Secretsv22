package com.example.maratbe.secrets;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback, Constants
{
    private static UserLocalStorage localStorage;
    private static int screenWidth, screenHeight;
    private static DataBase dbInstance;
    private static int activeThemeNumber = 0;
    private static ArrayList<Item> topTenList = new ArrayList<>();
    private static ArrayList<Item> secretsList = new ArrayList<>();
    private static ArrayList<Item> itemByTagList = new ArrayList<>();
    private static User[] users = new User[2];
    private static int pageToRecreate = -1;

    private static ArrayList<String> listOfTags = new ArrayList<>();

    public static User getUser(int index)
    {
        return users [index];
    }

    public static void setUser(User user , int index)
    {
        users [index] = user;
    }

    public static UserLocalStorage getLocalStorage()
    {
        return localStorage;
    }

    public static void setActiveThemeNumber(int num)
    {
        activeThemeNumber = num;
    }

    public static int getActiveThemeNumber()
    {
        return activeThemeNumber;
    }

    public static ArrayList<Item> getTopTen()
    {
        return  topTenList;
    }

    public static ArrayList<Item> getSecrets() {
        return secretsList;
    }

    public static ArrayList<String> getTags()
    {
        return  listOfTags;
    }

    public static ArrayList<Item> getItemsByTag()
    {
        return itemByTagList;
    }

    public static DataBase getDbInstance()
    {
        return dbInstance;
    }

    public static void setItemByTagList(ArrayList<Item> list)
    {
        itemByTagList = list;
    }

    public static void setSecretsList(ArrayList<Item> list)
    {
        secretsList = list;
    }

    public static int getPageToRecreate() {
        return pageToRecreate;
    }

    public static void setPageToRecreate(int pageToRecreate) {
        MainActivity.pageToRecreate = pageToRecreate;
    }

    public static void setTopTenList(ArrayList<Item> list)
    {
        topTenList = list;
    }

    public static void addTag(String tag)
    {
        listOfTags.add(tag);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        setDimensions();

        Button btn = (Button) findViewById(R.id.enter_button) ;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                150);
        LinearLayout lLayout = (LinearLayout) findViewById(R.id.main_layout);
        int[] colors = {Color.BLACK, Color.RED, ContextCompat.getColor(this ,R.color.transparent_green)};
        lLayout.setBackground(Util.createBorder(1, 1, true, colors, 0));
        params.setMargins(20,(screenHeight/2)-150, 20, 0);
        btn.setLayoutParams(params);
        btn.setText(R.string.read_secrets);
        btn.setTextSize(22);
        btn.setBackground(Util.createBorder(1, Color.TRANSPARENT, false, null, 2));
        btn.setOnClickListener(this);

        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.dark_gray));

        users[0] = new User();
        dbInstance = new DataBase();


        //dbInstance.connect();
       // insert1000Items();
        //dbInstance.insertIntoTagListBunch();
        //dbInstance.insertIntoItemBunch();
     //   dbInstance.insertIntoTagsItemsBunch();
       // dbInstance.insertIntoStatisticsBunch();
        dbInstance.selectTopTenData(false);
        dbInstance.selectAllSecretsData(0, "date", false, false);
        localStorage = new UserLocalStorage(this);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},1);
            System.out.println("No permissions");
        } else {
            System.out.println("We do have permissions");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        //localStorage.clearUserData();
        selectUserData(0);
    }

    public static void selectUserData(int index) {
        if (MainActivity.getLocalStorage().isLoggedIn()){
            MainActivity.getLocalStorage().setUserData(MainActivity.getUser(0));
            dbInstance.selectUsersSecrets(users[index].getSecrets(0, MAX_ITEMS_SHOWS), index, 0, true, false);
            dbInstance.selectUsersComments(users[index].getComments(0, MAX_ITEMS_SHOWS), index, 0, false, false);
            dbInstance.selectUsersPinned(users[index].getPinned(0, MAX_ITEMS_SHOWS), index, 0, false, false);
            dbInstance.selectUsersVotes(users[index].getVotes(0, MAX_ITEMS_SHOWS), index, 0, false, true);
            activeThemeNumber = users[index].getThemeNumber();
        }
        else
        {
            dbInstance.selectUserData(users [index], index);
        }
    }

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    public void setDimensions()
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
        //intent.putExtra("Theme", selectedTheme);
        startActivity(intent);
    }
}
