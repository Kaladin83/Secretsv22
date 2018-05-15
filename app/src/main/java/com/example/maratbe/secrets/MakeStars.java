package com.example.maratbe.secrets;

import android.content.Context;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;

public abstract class MakeStars
{
    private LinearLayout starsLayout;
    private LinearLayout.LayoutParams lParams;
    private Context context;
    private boolean layoutPassed = false;
    private int starSize;
    private int stars = 2;
    boolean clickable;

    public MakeStars(Context context, int layoutWidth, int starSize, int stars, LinearLayout layout, boolean clickable)
    {
        this.clickable = clickable;
        this.context = context;
        this.starSize = starSize;
        this.stars = stars;
        if (layout != null)
        {
            layoutPassed = true;
            starsLayout = layout;
        }
        else
        {
            starsLayout = new LinearLayout(context);
        }

        makeStars(layoutWidth);
    }

    private void makeStars(int layoutWidth)
    {
        lParams = new LinearLayout.LayoutParams(layoutWidth, starSize+14);
        //RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(layoutWidth, starSize+14);
        lParams.setMargins(0,7,0,7);

        starsLayout.setGravity(Gravity.CENTER);
        starsLayout.setLayoutParams(lParams);
        starsLayout.setOrientation(LinearLayout.HORIZONTAL);
        lParams = new LinearLayout.LayoutParams(starSize, starSize);
        for (int i = 0; i< 5; i++)
        {
            if (layoutPassed)
            {
                createButton(i, (Button) starsLayout.getChildAt(i));
            }
            else
            {
                starsLayout.addView(createButton(i));
            }

        }
        fillStars((Button) starsLayout.getChildAt(stars), starsLayout);
    }

    private Button createButton(int id)
    {
        lParams.setMargins(0,0,7,0);
        Button btn = new Button(context);
        btn.setLayoutParams(lParams);
        btn.setId(id);
        btn.setBackground(context.getDrawable(R.drawable.state_list_stars));
        if (clickable)
        {
            btn.setOnClickListener(view -> fillStars((Button) view, starsLayout));
        }

        return btn;
    }

    private void createButton(int id, Button btn)
    {
        lParams.setMargins(0,0,7,0);
        btn.setLayoutParams(lParams);
        btn.setId(id);
        btn.setPadding(0,0,0,0);
        btn.setBackground(context.getDrawable(R.drawable.state_list_stars));
        if (clickable)
        {
            btn.setOnClickListener(view -> fillStars((Button) view, starsLayout));
        }
    }

    public abstract void fillStars(Button chosenStar, LinearLayout starsLayout);
}
