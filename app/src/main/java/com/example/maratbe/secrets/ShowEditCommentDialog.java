package com.example.maratbe.secrets;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class ShowEditCommentDialog extends Dialog implements Constants
{
    private int dialogWidth, chosenStars = 0;
    private Theme theme;
    private Activity activity;
    private Item.Comment comment;
    private LinearLayout mainLayout, starLayout;
    private LinearLayout.LayoutParams lParams;
    private EditText edit;
    private ShowEditCommentDialog dialog;

    public ShowEditCommentDialog(Activity activity, Item.Comment comment) {

        super(activity);
        this.activity = activity;
        this.comment = comment;
        dialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_show_edit_comment);

        dialogWidth = (int) (MainActivity.getScreenWidth() * 0.9);
        theme = TabNavigator.getThemeAt(MainActivity.getActiveThemeNumber());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dialogWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        mainLayout = findViewById(R.id.edit_comment_main_layout);
        mainLayout.setLayoutParams(params);
        //mainLayout.setBackgroundColor(theme.getGradientColors()[0]);
        if (this.getWindow() != null)
        this.getWindow().setBackgroundDrawableResource(R.drawable.edit_comment_shape);

        fillUpLayout();

        this.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });
    }

    private void fillUpLayout()
    {
        new MakeStars(getContext(),(int) (dialogWidth*0.6), dialogWidth/11, comment.getStars() - 1, null, true) {
            @Override
            public void fillStars(Button chosenStar, LinearLayout starsLayout) {
                for (int i = 0; i< 5; i++)
                {
                    if (i < chosenStar.getId()+1)
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
        lParams = new LinearLayout.LayoutParams((int) (dialogWidth*0.9), dialogWidth/11);
        lParams.setMargins(0,25,0,0);
        lParams.gravity = Gravity.CENTER;
        starLayout.setLayoutParams(lParams);
        mainLayout.addView(starLayout);

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth*0.9), 4);
        lParams.gravity = Gravity.CENTER;
        lParams.setMargins(0,25,0,10);
        View view = createTextView(-100, "", 0, 0);
        view.setBackgroundColor(theme.getSelectedTitleColor()[2]);
        mainLayout.addView(view);

        lParams = new LinearLayout.LayoutParams((int)(dialogWidth*0.96),(int) (dialogWidth/4.2));
        lParams.gravity = Gravity.CENTER;
        lParams.setMargins(20,20,20,20);
        LinearLayout editLayout = new LinearLayout(getContext());
        editLayout.setLayoutParams(lParams);
        editLayout.setBackground(Util.createBorder(15, theme.getGradientColors()[2], false, null, 1));
        editLayout.setOrientation(LinearLayout.HORIZONTAL);

        lParams = new LinearLayout.LayoutParams((int)(dialogWidth*0.7), LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.gravity = Gravity.CENTER_VERTICAL;
        edit = new EditText(getContext());
        edit.setTypeface(theme.getTitleTypeface());
       // edit.setFocusableInTouchMode(true);
        edit.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        edit.setPadding(20,15,20,15);

        edit.setLayoutParams(lParams);
        edit.setSelectAllOnFocus(true);
        edit.setBackgroundColor(Color.TRANSPARENT);
        edit.setText(comment.getText());
        Util.openKeyboard(activity);
        editLayout.addView(edit);

        lParams = new LinearLayout.LayoutParams(4, (int)(dialogWidth*0.2));
        lParams.gravity = Gravity.CENTER;
        lParams.setMargins(20,0,20,0);
        view = createTextView(-100, "", 0, 0);
        view.setBackgroundColor(theme.getSelectedTitleColor()[2]);
        editLayout.addView(view);

        lParams = new LinearLayout.LayoutParams((int)(dialogWidth*0.2), (int)(dialogWidth*0.2));
        lParams.gravity = Gravity.CENTER;
        Button button = new Button(getContext());
        button.setBackgroundResource(theme.getDrawbles()[6]);
        button.setLayoutParams(lParams);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.closeKeyboard(edit, activity);
                sendClicked(edit.getText().toString(), chosenStars);
            }
        });
        editLayout.addView(button);
        mainLayout.addView(editLayout);
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

    protected abstract void sendClicked(String text, int chosenStars);
}
