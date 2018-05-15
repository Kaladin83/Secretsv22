package com.example.maratbe.secrets;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class ShowAddTagDialog extends Dialog implements Constants
{
    private int dialogWidth, dialogHeight;
    private Activity activity;
    private Theme theme;
    private LinearLayout mainLayout;
    private LinearLayout.LayoutParams lParams;
    private EditText editText;

    public ShowAddTagDialog(Activity activity, Theme theme) {
        super(activity);
        this.activity = activity;
        this.theme = theme;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_tag);

        dialogWidth = (int) (MainActivity.getScreenWidth() * 0.7);
        dialogHeight = (int) (MainActivity.getScreenHeight() * 0.22);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dialogWidth, dialogHeight);
        mainLayout = findViewById(R.id.main_add_tag_layout);
        mainLayout.setLayoutParams(params);
        mainLayout.setBackgroundColor(theme.getGradientColors()[1]);

        createEmojiesLayout();
        Util.openKeyboard(activity);
    }

    private void createEmojiesLayout()
    {
        lParams = new LinearLayout.LayoutParams(dialogWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(0,20,0,30);
        LinearLayout titleLayout = createLinearLayout(LinearLayout.VERTICAL, Color.TRANSPARENT, 0, View.VISIBLE);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        titleLayout.addView(createTextView("Add new tag", TEXT_SIZE+8, theme.getSelectedTitleColor()[2],theme.getMainTypeface()));

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.gravity = Gravity.START;
        lParams.setMargins(20,0,20,0);
        titleLayout.addView(createTextView("* No more than 15 characters ", TEXT_SIZE+1, Color.GRAY,theme.getMainTypeface()));

        lParams = new LinearLayout.LayoutParams(dialogWidth, (int)(dialogHeight*0.4));
        LinearLayout editLayout = createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, View.VISIBLE);
        editLayout.setBackground(Util.createBorder(0, theme.getGradientColors()[0], false, null, 1));

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth* 0.48),  (int)(dialogHeight*0.4));
        lParams.setMargins(10,0,10,0);
        editText = new EditText(getContext());
        editText.setLayoutParams(lParams);
        editText.setPadding(20, 10, 20, 10);
        editText.setBackground(Util.createBorder(10, Color.TRANSPARENT, false, null, 0));
        editLayout.addView(editText);

        lParams = new LinearLayout.LayoutParams(1,  (int)(dialogHeight*0.4));
        lParams.gravity = Gravity.CENTER;
        lParams.setMargins(10,0,10,0);
        View view = new View(getContext());
        view.setLayoutParams(lParams);
        view.setBackgroundColor(Color.BLACK);
        editLayout.addView(view);

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth* 0.23),  (int)(dialogHeight*0.4));
        editLayout.addView(createButton(R.id.add_tag_cancel_btn, Color.GRAY, "Cancel", BUTTON_TEXT_SIZE+4, theme.getTitleTypeface()));

        lParams = new LinearLayout.LayoutParams(1,  (int)(dialogHeight*0.4));
        lParams.gravity = Gravity.CENTER;
        lParams.setMargins(10,0,10,0);
        view = new View(getContext());
        view.setLayoutParams(lParams);
        view.setBackgroundColor(Color.BLACK);
        editLayout.addView(view);

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth* 0.15),  (int)(dialogHeight*0.4));
        editLayout.addView(createButton(R.id.add_tag_add_btn, theme.getSelectedTitleColor()[2], "Add", BUTTON_TEXT_SIZE+4, theme.getTitleTypeface()));

        lParams = new LinearLayout.LayoutParams((int)(dialogWidth* 0.8), 4);
        lParams.gravity = Gravity.CENTER;
        lParams.setMargins(0,20,0,20);
        view = new View(getContext());
        view.setLayoutParams(lParams);
        view.setBackgroundColor(theme.getSelectedTitleColor()[2]);

        mainLayout.addView(titleLayout);
        mainLayout.addView(editLayout);
    }

    private LinearLayout createLinearLayout(int orientation, int color, int  border, int visible)
    {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(orientation);
        layout.setLayoutParams(lParams);
        layout.setGravity(Gravity.CENTER);
        layout.setVisibility(visible);
        layout.setBackground(Util.createBorder(20, color, false, null, border));
        return layout;
    }

    private Button createButton(int id, int color,String text, int textSize, Typeface typeface)
    {
        Button btn = new Button(getContext());
        btn.setTextSize(textSize);
        btn.setTextColor(color);
        btn.setAllCaps(false);
        btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btn.setLayoutParams(lParams);
        btn.setText(text);
        btn.setPadding(0,0,0,0);
        btn.setId(id);
        btn.setBackground(Util.createBorder(10, Color.TRANSPARENT, false, null, 0));
        btn.setTypeface(typeface);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.add_tag_add_btn)
                {
                    Util.closeKeyboard(editText, activity);
                    addTagClicked(editText.getText().toString());
                }
                else
                {
                    Util.closeKeyboard(editText, activity);
                    cancelClicked();
                }
            }
        });

        return btn;
    }

    protected abstract void cancelClicked();
    protected abstract void addTagClicked(String tagName);

    public TextView createTextView(String txt, int textSize, int color, Typeface typeface) {
        TextView txtView = new TextView(getContext());
        txtView.setText(txt);
        txtView.setTextSize(textSize);
        txtView.setTextColor(color);
        txtView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtView.setBackgroundColor(Color.TRANSPARENT);
        txtView.setLayoutParams(lParams);
        txtView.setTypeface(typeface);
        return txtView;
    }
}
