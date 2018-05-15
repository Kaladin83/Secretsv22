package com.example.maratbe.secrets;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class ShowAlertDialog extends Dialog implements Constants
{
    private int dialogWidth, buttonHeight, imageSize;
    private int backgroundColor, titleColor, buttonTextColor;
    private int[] buttons, images;
    private String[] bodyStrings;
    private boolean isImage = false;
    private String title, body;
    private Theme theme;
    private LinearLayout mainLayout;
    private LinearLayout.LayoutParams lParams;

    public ShowAlertDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_custom_alert);

        dialogWidth = (int) (MainActivity.getScreenWidth() * 0.85);
        buttonHeight= (int) (dialogWidth *0.17);
        imageSize = (int) (dialogWidth *0.09);
        theme = TabNavigator.getThemeAt(MainActivity.getActiveThemeNumber());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dialogWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        mainLayout = findViewById(R.id.main_alert_layout);
        mainLayout.setLayoutParams(params);
        mainLayout.setBackgroundColor(Color.WHITE);

        fillUpLayout();
    }

    private void fillUpLayout()
    {
        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView textView = createTextView(-100, title, TEXT_SIZE+6, titleColor);
        textView.setTypeface(theme.getMainTypeface());
        textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        textView.setPadding(60,20,40,20);
        mainLayout.addView(textView);

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth*0.9), 4);
        lParams.gravity = Gravity.CENTER;

        View view = createTextView(-100, getContext().getString(R.string.empty_string), 0, 0);
        view.setBackgroundColor(buttonTextColor);
        mainLayout.addView(view);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(0,30,0,30);
        LinearLayout bodyLayout = createLinearLayout(LinearLayout.VERTICAL, R.color.transparent, 0, View.VISIBLE);
        bodyLayout.setLayoutParams(lParams);

        if (isImage)
        {
            lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lParams.setMargins(0,0,0,10);
            LinearLayout layout1 = createLinearLayout(LinearLayout.HORIZONTAL, R.color.transparent, 0, View.VISIBLE);
            LinearLayout layout2 = createLinearLayout(LinearLayout.HORIZONTAL, R.color.transparent, 0, View.VISIBLE);
            lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView textView1 = createTextView(-100, bodyStrings[0], BUTTON_TEXT_SIZE + 2, ContextCompat.getColor(getContext(), R.color.light_black));
            textView1.setTypeface(theme.getTitleTypeface());
            //textView.setSingleLine(false);
            textView1.setPadding(30,0,10,0);
            TextView textView2 = createTextView(-100, bodyStrings[1], BUTTON_TEXT_SIZE + 2, ContextCompat.getColor(getContext(), R.color.light_black));
            textView2.setTypeface(theme.getTitleTypeface());
            //textView.setSingleLine(false);
            textView2.setPadding(0,0,30,0);


            lParams = new LinearLayout.LayoutParams(imageSize, imageSize);
            ImageView image1 = new ImageView(getContext());
            image1.setBackground(ContextCompat.getDrawable(getContext(),images[0]));
            image1.setLayoutParams(lParams);

            ImageView image2 = new ImageView(getContext());
            image2.setBackground(ContextCompat.getDrawable(getContext(),images[1]));
            image2.setLayoutParams(lParams);

            layout1.addView(textView1);
            layout1.addView(image1);
            layout2.addView(textView2);
            layout2.addView(image2);
            bodyLayout.addView(layout1);
            bodyLayout.addView(layout2);
        }
        else
        {
            lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textView = createTextView(-100, body, BUTTON_TEXT_SIZE + 2, ContextCompat.getColor(getContext(), R.color.light_black));
            textView.setTypeface(theme.getTitleTypeface());
            textView.setSingleLine(false);
            textView.setPadding(30,0,30,0);
            bodyLayout.addView(textView);
        }
        mainLayout.addView(bodyLayout);

        lParams = new LinearLayout.LayoutParams(dialogWidth, buttonHeight);
        LinearLayout buttonLayout = createLinearLayout(LinearLayout.HORIZONTAL, R.color.transparent, 0, View.VISIBLE);
        lParams = new LinearLayout.LayoutParams(dialogWidth/buttons.length, buttonHeight);
        for (int i = 0; i < buttons.length; i++)
        {
            buttonLayout.addView(createButton(buttons[i], i, BUTTON_TEXT_SIZE+2, theme.getTitleTypeface()));
        }

        mainLayout.addView(buttonLayout);
    }

    public void setTexts(String title, String body, int[] buttons)
    {
        this.title = title;
        this.body = body;
        this.buttons = new int[buttons.length];
        System.arraycopy(buttons, 0, this.buttons, 0, buttons.length);
    }

    public void setImage(String[] body, int[] images)
    {
        this.isImage = true;
        this.images = new int[images.length];
        System.arraycopy(images, 0, this.images, 0, images.length);
        this.bodyStrings = body;
    }

    public void setColors(int backgroundColor, int titleColor, int buttonTextColor)
    {
        this.backgroundColor = backgroundColor;
        this.titleColor = titleColor;
        this.buttonTextColor = buttonTextColor;
    }

    private Button createButton(int text, int id, int textSize, Typeface typeface)
    {
        Button btn = new Button(getContext());
        btn.setTextSize(textSize);
        btn.setTextColor(buttonTextColor);
        btn.setAllCaps(false);
        btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btn.setLayoutParams(lParams);
        btn.setText(text);
        btn.setBackground(Util.createBorder(0, backgroundColor, false, null, 1));
        btn.setTypeface(typeface);
        btn.setId(id);
        btn.setOnClickListener(view -> buttonPressed(view.getId()));

        return btn;
    }

    protected abstract void buttonPressed(int id);

    private LinearLayout createLinearLayout(int orientation, int color, int  border, int visible)
    {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(orientation);
        layout.setLayoutParams(lParams);
        layout.setGravity(Gravity.CENTER);
        layout.setVisibility(visible);
        layout.setBackground(Util.createBorder(20, ContextCompat.getColor(getContext(), color), false, null, border));
        return layout;
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
}
