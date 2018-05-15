package com.example.maratbe.secrets;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class ShowStars extends Dialog implements Constants
{
    private int dialogWidth, chosenStars = 0;
    private Theme theme;
    private LinearLayout mainLayout, starLayout;
    private LinearLayout.LayoutParams lParams;

    public ShowStars(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_stars);

        dialogWidth = (int) (MainActivity.getScreenWidth() * 0.9);
        theme = TabNavigator.getThemeAt(MainActivity.getActiveThemeNumber());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dialogWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        mainLayout = findViewById(R.id.main_stars_layout);
        mainLayout.setLayoutParams(params);
        mainLayout.setBackgroundColor(Color.WHITE);

        fillUpLayout();
    }

    private void fillUpLayout()
    {
        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.gravity = Gravity.CENTER;
        TextView textView = createTextView(-100, "Please vote before sending", TEXT_SIZE+2, Color.BLACK);
        textView.setTypeface(theme.getMainTypeface());
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setPadding(60,20,40,20);
        mainLayout.addView(textView);

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth*0.9), 7);
        lParams.gravity = Gravity.CENTER;
        View view = createTextView(-100, "", 0, 0);
        view.setBackgroundColor(theme.getSelectedTitleColor()[2]);
        mainLayout.addView(view);

        new MakeStars(getContext(), dialogWidth, dialogWidth/11, 2, null, true) {
            @Override
            public void fillStars(Button chosenStar, LinearLayout starsLayout) {
                for (int i = 0; i< 5; i++)
                {
                    if (i <= chosenStar.getId())
                    {
                        starsLayout.getChildAt(i).setSelected(true);
                    }
                    else
                    {
                        starsLayout.getChildAt(i).setSelected(false);
                    }
                }
                starLayout = starsLayout;
                chosenStars = chosenStar.getId()+1;
            }
        };
        mainLayout.addView(starLayout);

        lParams = new LinearLayout.LayoutParams(dialogWidth/3, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(0,30,0,0);
        Button button = new Button(getContext());
        button.setTypeface(theme.getTitleTypeface());
        button.setAllCaps(false);
        button.setText("Send");
        button.setTextSize(TEXT_SIZE-3);
        button.setBackgroundColor(theme.getGradientColors()[1]);
        button.setPadding(30,0,30,0);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendClicked(chosenStars);
            }
        });
        mainLayout.addView(button);
    }


    public TextView createTextView(int icon, String txt, int textSize, int textColor) {
        TextView txtView = new TextView(getContext());
        txtView.setText(txt);
        txtView.setTextSize(textSize);
        txtView.setTextColor(textColor);
        txtView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtView.setBackgroundColor(Color.TRANSPARENT);
        txtView.setLayoutParams(lParams);
        if (icon > -100) {
            txtView.setBackgroundResource(icon);
        }
        return txtView;
    }

    protected abstract void sendClicked(int id);
}
