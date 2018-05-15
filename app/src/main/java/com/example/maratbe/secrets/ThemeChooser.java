package com.example.maratbe.secrets;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class ThemeChooser extends AppCompatActivity implements Constants, View.OnClickListener {
    private int scrollHeight, objectWidth, newThemeNumber;
    private HorizontalScrollView hScroll;
    private LinearLayout scrollLayout;
    private LinearLayout.LayoutParams lParams;
    private Theme theme;
    private ShowAlertDialog alertDialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_theme_chooser);
        theme = TabNavigator.getThemeAt(MainActivity.getActiveThemeNumber());
        scrollHeight = ((int)((MainActivity.getScreenHeight() - 70) / 1.7));
        objectWidth = 550;
        setFields();
    }

    public void setFields()
    {
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_theme_layout);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(0,80,0, 50);
        mainLayout.addView(createText("Choose your Theme",TITLE_SIZE+5, theme.getActiveBarColor()));

        createScrollView();
        mainLayout.addView(hScroll);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(0,50,0,0);
        lParams.gravity = Gravity.CENTER_HORIZONTAL;
        Button chooseButton = new Button(this);
        chooseButton.setLayoutParams(lParams);
        chooseButton.setTextColor(theme.getActiveBarColor());
        chooseButton.setTypeface(theme.getMainTypeface());
        chooseButton.setTextSize(TITLE_SIZE);
        chooseButton.setAllCaps(false);
        chooseButton.setPadding(20,0,20,0);
        chooseButton.setText(R.string.choose_theme);
        chooseButton.setId((R.id.theme_choose_btn));
        chooseButton.setOnClickListener(this);
        chooseButton.setBackground(Util.createBorder(15, Color.TRANSPARENT, false, null, 1));
        mainLayout.addView(chooseButton);
    }

    public void createScrollView()
    {
        hScroll = new HorizontalScrollView(this);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, scrollHeight);
        scrollLayout = new LinearLayout(this);
        scrollLayout.setOrientation(LinearLayout.HORIZONTAL);
        scrollLayout.setLayoutParams(lParams);

        createObject(TabNavigator.getThemeAt(0), 0);
        createObject(TabNavigator.getThemeAt(1), 1);
        createObject(TabNavigator.getThemeAt(2), 2);
        createObject(TabNavigator.getThemeAt(3), 3);
        createObject(TabNavigator.getThemeAt(4), 4);

        hScroll.addView(scrollLayout);
    }

    private void createObject(Theme theme, int id)
    {
        RadioButton radio;

        lParams = new LinearLayout.LayoutParams(objectWidth, scrollHeight -10);
        LinearLayout objectLayout = new LinearLayout(this);
        objectLayout.setOrientation(LinearLayout.VERTICAL);
        objectLayout.setLayoutParams(lParams);
        objectLayout.setGravity(Gravity.CENTER);

        switch(theme.getRadioCircle())
        {
            case "radio_winter":
                radio = (RadioButton) getLayoutInflater().inflate(R.layout.radio_winter, objectLayout, false);
                break;
            case "radio_summer":
                radio = (RadioButton) getLayoutInflater().inflate(R.layout.radio_summer, objectLayout, false);
                break;
            case "radio_spring":
                radio = (RadioButton) getLayoutInflater().inflate(R.layout.radio_spring, objectLayout, false);
                break;
            case "radio_autumn":
                radio = (RadioButton) getLayoutInflater().inflate(R.layout.radio_autumn, objectLayout, false);
                break;
            default:
                radio = (RadioButton) getLayoutInflater().inflate(R.layout.radio_night, objectLayout, false);
                break;
        }
        lParams = new LinearLayout.LayoutParams(objectWidth - 40, scrollHeight/10);
        if (id == MainActivity.getActiveThemeNumber())
        {
            radio.setChecked(true);
        }
        radio.setLayoutParams(lParams);
        radio.setText(theme.getText());
        radio.setId(id);
        radio.setHighlightColor(theme.getSelectedTitleColor()[2]);
        radio.setGravity(Gravity.CENTER);
        radio.setOnClickListener(this);

        lParams =  new LinearLayout.LayoutParams(objectWidth-40, scrollHeight - (scrollHeight/10) - 60);
        Button button = new Button(this);
        button.setAllCaps(false);
        button.setBackgroundResource(theme.getIcon());
        button.setGravity(Gravity.CENTER);
        button.setId(id+5);
        button.setOnClickListener(this);
        button.setLayoutParams(lParams);

        objectLayout.addView(radio);
        objectLayout.addView(button);
        scrollLayout.addView(objectLayout);
    }

    private TextView createText(String txt, int size, int color)
    {
        TextView txtView = new TextView(this);
        txtView.setText(txt);
        txtView.setTextSize(size);
        txtView.setTextColor(color);
        txtView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtView.setLayoutParams(lParams);
        txtView.setTypeface(Typeface.createFromAsset(getAssets(),"Xerox Serif Narrow Italic.ttf"));
        txtView.setPadding(0,40,0,0);
        return  txtView;
    }

    /*public void onBackPressed() {

    }*/

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.theme_choose_btn)
        {
            alertDialog = new ShowAlertDialog(this) {
                @Override
                protected void buttonPressed(int id) {
                    if (id == 0) {
                        exit();
                    }
                    alertDialog.dismiss();
                }
              /*  @Override
                protected void okButtonPressed() {
                    alertDialog.dismiss();
                    exit();
                }

                @Override
                protected void cancelButtonPressed() {
                    alertDialog.dismiss();
                }*/
            };
            alertDialog.setTexts(getString(R.string.save_theme_title), getString(R.string.save_theme_body), new int[] {R.string.ok, R.string.cancel});
            Util.setDialogColors(this, alertDialog);
            alertDialog.show();
        }
        else
        {
            for ( int i = 0; i < 5; i++)
            {
                LinearLayout object = (LinearLayout) scrollLayout.getChildAt(i);
                RadioButton rb = (RadioButton) object.getChildAt(0);
                if (view.getId() == i || view.getId() == i+5)
                {
                    rb.setChecked(true);
                    object.setBackgroundColor(theme.getGradientColors()[0]);
                    newThemeNumber = i;
                }
                else
                {
                    object.setBackgroundColor(Color.WHITE);
                    rb.setChecked(false);
                }
            }
        }
    }

    private void exit()
    {
        MainActivity.setActiveThemeNumber(newThemeNumber);
        MainActivity.getLocalStorage().updateTheme(MainActivity.getUser(0), MainActivity.getActiveThemeNumber());
        TabNavigator.getTabNavigatorInstance().setTheme();
        Intent a = new Intent(this, TabNavigator.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
    }
}