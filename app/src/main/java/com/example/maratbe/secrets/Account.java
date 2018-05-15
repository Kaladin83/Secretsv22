package com.example.maratbe.secrets;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Account extends AppCompatActivity implements Constants, View.OnClickListener {
   // private Theme theme;
    private LinearLayout.LayoutParams lParams;
    private LinearLayout mainLayout;
    private String userName = "";
    private int userIndex = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_account);
        userName = getIntent().getStringExtra("USER_NAME");
        userIndex = getIntent().getIntExtra("USER_INDEX", 0);
        //theme = TabNavigator.getThemeAt(MainActivity.getActiveThemeNumber());
        mainLayout = (LinearLayout) findViewById(R.id.main_account_layout);
        mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        setFields();
    }

    private void setFields() {
        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(20,70,20,40);
        TextView title = createTextView(R.string.account_title, TEXT_SIZE + 8, ContextCompat.getColor(this, R.color.colorPrimary));
        title.setText(getString(R.string.account_title, userName));

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(20,40,20,40);
        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.profile_pic);

        lParams = new LinearLayout.LayoutParams((int)(MainActivity.getScreenWidth()*0.87), LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout tableLayout = createLinearLayout(LinearLayout.VERTICAL, Color.TRANSPARENT, 1, 10, View.VISIBLE);
        LinearLayout columnLayout = createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, 10, View.VISIBLE);
        LinearLayout valuesLayout = createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, 10, View.VISIBLE);

        lParams = new LinearLayout.LayoutParams((int)((MainActivity.getScreenWidth()*0.87)/3) - 2, MainActivity.getScreenHeight()/24);
        TextView secrets = createTextView(R.string.account_secrets, 16, Color.BLACK);
        TextView comments = createTextView(R.string.account_comments, 16, Color.BLACK);
        TextView votes = createTextView(R.string.account_votes, 16, Color.BLACK);
        Button numOfSecrets = createButton(MainActivity.getUser(userIndex).getNumOfSecrets() + "", R.id.num_of_secrets_btn, 16, ContextCompat.getColor(this, R.color.colorPrimary));
        Button numOfComments = createButton(MainActivity.getUser(userIndex).getNumOfComments() + "", R.id.num_of_comments_btn, 16, ContextCompat.getColor(this, R.color.colorPrimary));
        Button numOfVotes = createButton(MainActivity.getUser(userIndex).getNumOfVotes() + "", R.id.num_of_votes_btn, 16, ContextCompat.getColor(this, R.color.colorPrimary));

        columnLayout.addView(secrets);
        columnLayout.addView(createSeparator(1, MainActivity.getScreenHeight()/16));
        columnLayout.addView(comments);
        columnLayout.addView(createSeparator(1, MainActivity.getScreenHeight()/16));
        columnLayout.addView(votes);
        valuesLayout.addView(numOfSecrets);
        valuesLayout.addView(createSeparator(1, MainActivity.getScreenHeight()/16));
        valuesLayout.addView(numOfComments);
        valuesLayout.addView(createSeparator(1, MainActivity.getScreenHeight()/16));
        valuesLayout.addView(numOfVotes);

        tableLayout.addView(columnLayout);
        tableLayout.addView(createSeparator((int)(MainActivity.getScreenWidth()*0.87), 1));
        tableLayout.addView(valuesLayout);

        mainLayout.addView(title);
        mainLayout.addView(image);
        mainLayout.addView(tableLayout);
    }

    private View createSeparator(int width, int height) {
        lParams = new LinearLayout.LayoutParams(width, height);
        View verticalSeparator = new View(this);
        verticalSeparator.setLayoutParams(lParams);
        verticalSeparator.setBackgroundColor(Color.BLACK);
        return verticalSeparator;
    }

    private LinearLayout createLinearLayout(int orientation, int color, int  border, int radius,int visible)
    {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(orientation);
        layout.setGravity(Gravity.CENTER);
        layout.setVisibility(visible);
        layout.setBackground(Util.createBorder(radius, color, false, null, border));
        layout.setLayoutParams(lParams);
        return layout;
    }

    private TextView createTextView(int text, int size, int color)
    {
        TextView txt = new TextView(this);
        txt.setTextColor(color);
        txt.setText(text);
        txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txt.setTextSize(size);
        txt.setLayoutParams(lParams);
        txt.setPadding(0,0,0,0);
        return txt;
    }

    private Button createButton(String text, int id, int size, int color) {
        Button button = new Button(this);
        button.setTextSize(size);
        button.setPadding(0,0,0,0);
        button.setTextColor(color);
        button.setAllCaps(false);
        button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        button.setLayoutParams(lParams);
        button.setText(text);
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setId(id);
        button.setOnClickListener(this);
        return button;
    }

    @Override
    public void onClick(View view) {
        Intent intent =  new Intent(getBaseContext(), UserDataDisplay.class);
        intent.putExtra("USER_INDEX", userIndex);
        switch (view.getId())
        {
            case R.id.num_of_secrets_btn:
                intent.putExtra("DISPLAY_DATA", "secrets"); break;
            case R.id.num_of_comments_btn:
                intent.putExtra("DISPLAY_DATA", "comments"); break;
            case R.id.num_of_votes_btn:
                intent.putExtra("DISPLAY_DATA", "votes");break;
        }
        startActivity(intent);
    }
}
