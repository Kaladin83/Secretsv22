package com.example.maratbe.secretsv2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by MARATBE on 12/23/2017.
 */

public class TopTen extends Fragment implements Constants, View.OnClickListener{
    private int objectHeight, buttonSize, objectWidth, emojiObjectNumber = 0;
    private boolean buttonTriggered = false, emojiPanelVisible = false;
    private ListView listView;
    private RelativeLayout.LayoutParams rParams;
    private RelativeLayout panelLayout, emojiPanelLayout;
    private LinearLayout toggleLayout;
    private View fragment;
    private ArrayList<RelativeLayout> listOfObjects = new ArrayList<>();
    ArrayAdapter<RelativeLayout> adapter;
    private Button secretBtn, thoughtBtn;
    private RadioButton secretRadio, thoughtRadio;
    private ToggleButton secretToggle, thoughtToggle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.activity_top_ten, container, false);

        createTogleLayout();
        createListView(SECRET_START_VALUE);
        return fragment;
    }

    private void createTogleLayout()
    {
        toggleLayout = fragment.findViewById(R.id.toggle_layout);

        secretBtn = fragment.findViewById(R.id.secret_button);
        secretBtn.setOnClickListener(this);
        secretBtn.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.colorPrimary));
        secretBtn.setBackground(Util.createBorder(fragment.getContext(),1, Color.TRANSPARENT, false, 0));
        secretBtn.setId(ID_SECRET_BTN);
        secretBtn.setTextSize(18);
        thoughtBtn = fragment.findViewById(R.id.thought_button);
        thoughtBtn.setId(ID_THOUGHT_BTN);
        thoughtBtn.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.light_black));
        thoughtBtn.setBackground(Util.createBorder(fragment.getContext(),1, Color.TRANSPARENT, false, 0));
        thoughtBtn.setTextSize(18);
        thoughtBtn.setOnClickListener(this);
    }


    private void createListView(int type)
    {
        if (listView != null)
        {
            listView.setAdapter(null);
        }
        listOfObjects.clear();

        LinearLayout.LayoutParams params;
        RelativeLayout object;
        objectHeight =(int)( MainActivity.getScreenHeight()/4);
        objectWidth = MainActivity.getScreenWidth();
        buttonSize = (int)(MainActivity.getScreenWidth()/8*0.6);

        RelativeLayout mainLayout = fragment.findViewById(R.id.top_ten_layout);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MainActivity.getScreenHeight() - 120);
        param.addRule(RelativeLayout.BELOW, toggleLayout.getId());
        listView = new ListView(fragment.getContext());
        mainLayout.addView(listView, param);

        for (int i = type; i < 10 + type; i++) {
            object = new RelativeLayout(fragment.getContext());
            params = new LinearLayout.LayoutParams(objectWidth, objectHeight);
            params.setMargins(30,0,0,0);
            object.setLayoutParams(params);

            rParams = new RelativeLayout.LayoutParams(objectWidth  - (objectWidth /3) , objectHeight - (int)(buttonSize*1.7) );
            rParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            rParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            rParams.setMargins(0,10,0,10);
            object.addView(createButton(false, 0, i, BUTTONS[i]),rParams);

            createPanel(i);
            object.addView(panelLayout, rParams);

            rParams = new RelativeLayout.LayoutParams((objectWidth - (objectWidth /3))/2, (int)(buttonSize*1.2));
            rParams.addRule(RelativeLayout.ABOVE, ID_PANEL_LAYOUT);
            rParams.addRule(RelativeLayout.ALIGN_LEFT);

            rParams = new RelativeLayout.LayoutParams(objectWidth /3 , objectHeight);
            rParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rParams.setMargins(0,10,0,10);
            object.addView(createButton(true, R.drawable.crying_girl,i , IMAGES[i]), rParams);

            listOfObjects.add(object);
        }

        adapter = new ListViewObjectAdapter(fragment.getContext(), 0, listOfObjects);
        listView.setAdapter(adapter);
    }

    private void createPanel(int index)
    {
        panelLayout = new RelativeLayout(fragment.getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(objectWidth, (int) (buttonSize*1.2));
        panelLayout.setLayoutParams(params);
        panelLayout.setPadding(40,0,30,0);
        panelLayout.setId(ID_PANEL_LAYOUT);

        String txt = MainActivity.getItemAt(index).getRating()+"";
        setRules(-100, RelativeLayout.ALIGN_PARENT_LEFT, buttonSize, buttonSize);
        createTextVeiw(R.drawable.star, "", ID_STAR_IMG_TOP, rParams, panelLayout);
        setRules(ID_STAR_IMG_TOP, RelativeLayout.RIGHT_OF, txt.length()*TEXT_SIZE*2, buttonSize);
        createTextVeiw(-100, txt, ID_RATING_TXT_TOP, rParams, panelLayout);
     /*   setRules(ID_RATING_TXT_TOP, RelativeLayout.RIGHT_OF, buttonSize, buttonSize);
        createTextVeiw(R.drawable.transparent_comments, "", ID_COMMENTS_IMG_TOP, rParams);
        txt = MainActivity.getItemAt(index).getNumOfComments()+"";
        setRules(ID_COMMENTS_IMG_TOP, RelativeLayout.RIGHT_OF, txt.length()*TEXT_SIZE*2, buttonSize);
        createTextVeiw(-100, txt, ID_NUM_OF_COMMENTS_TXT_TOP, rParams);*/
        setRules(-100, RelativeLayout.ALIGN_PARENT_RIGHT, buttonSize, buttonSize);
        panelLayout.addView(createButton(true, R.drawable.plus, index, EXPEND_BUTTONS[index]), rParams);
        //panelLayout.addView(createButton(true, R.drawable.plus, ID_PUT_LIKE_BTN), rParams);
       // createTextVeiw(R.drawable.plus, "", ID_PUT_LIKE_BTN, rParams, panelLayout);

        createEmojiPanel(index);
        panelLayout.addView(emojiPanelLayout, rParams);

        rParams = new RelativeLayout.LayoutParams(objectWidth - (objectWidth /3), (int)(buttonSize*1.2));
        rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rParams.addRule(RelativeLayout.ALIGN_LEFT);
    }

    private void createEmojiPanel(int index)
    {
        emojiPanelLayout = new RelativeLayout(fragment.getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(objectWidth, (int) (buttonSize*1.2));
        emojiPanelLayout.setLayoutParams(params);
        emojiPanelLayout.setVisibility(View.GONE);

        setRules(-100, RelativeLayout.ALIGN_PARENT_LEFT, buttonSize, buttonSize);
        rParams.setMargins(0,0,10,0);
        emojiPanelLayout.addView(createButton(true ,R.drawable.heart, index, PUT_LIKE_BUTTONS[index]), rParams);
        setRules(PUT_LIKE_BUTTONS[index], RelativeLayout.RIGHT_OF, buttonSize, buttonSize);
        rParams.setMargins(0,0,10,0);
        emojiPanelLayout.addView(createButton(true, R.drawable.lol, index, PUT_LOL_BUTTONS[index]), rParams);
        setRules(PUT_LOL_BUTTONS[index], RelativeLayout.RIGHT_OF, buttonSize, buttonSize);
        rParams.setMargins(0,0,10,0);
        emojiPanelLayout.addView(createButton(true, R.drawable.sad, index, PUT_SAD_BUTTONS[index]), rParams);
        setRules(PUT_SAD_BUTTONS[index], RelativeLayout.RIGHT_OF, buttonSize, buttonSize);
        rParams.setMargins(0,0,0,0);
        emojiPanelLayout.addView(createButton(true, R.drawable.angry, index, PUT_ANGRY_BUTTONS[index]), rParams);

        rParams = new RelativeLayout.LayoutParams((int)((objectWidth - (objectWidth /3))/1.9), (int)(buttonSize*1.2));
        rParams.addRule(RelativeLayout.LEFT_OF, EXPEND_BUTTONS[index]);

    }

    private Button createButton(boolean isImage, int image, int index, int id)
    {
        Button btn = new Button(fragment.getContext());
        btn.setTextSize(TEXT_SIZE);
        btn.setTextColor(TEXT_COLOR);
        btn.setAllCaps(false);
        btn.setId(id);
        btn.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        if (isImage)
        {
            btn.setBackgroundResource(image);
        }
        else
        {
            btn.setText(MainActivity.getItemAt(index).getText());
            btn.setPadding(50,0,10,0);
            btn.setBackground(Util.createBorder(fragment.getContext(),1, Color.TRANSPARENT, false, 0));
        }

        btn.setTypeface(TabNavigator.getThemeAt(TabNavigator.getActiveThemeNumber()).getMainTypeface());
        btn.setGravity(Gravity.CENTER);
        btn.setOnClickListener(this);

        return btn;
    }

    public void setRules(int id_, int rule, int width, int height) {
        rParams = new RelativeLayout.LayoutParams(width, height);
        if (id_ != -100) {
            rParams.addRule(rule, id_);
        }
        else
        {
            rParams.addRule(rule);
        }
    }

    public void createTextVeiw(int icon, String txt, int id,  RelativeLayout.LayoutParams rParams, RelativeLayout panel) {
        TextView txtView = new TextView(fragment.getContext());
        txtView.setText(txt);
        txtView.setTextSize((int) (TEXT_SIZE*0.8));
        txtView.setTextColor(Color.BLACK);
        txtView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtView.setBackgroundColor(Color.TRANSPARENT);
        if (icon > -100) {
            txtView.setBackgroundResource(icon);
        }
        txtView.setId(id);
        // updateField(id, txtView);
        panel.addView(txtView, rParams);
    }

    public void handleEmojiPanel(int i)
    {
        int type = 0;
        RelativeLayout panel;
        buttonTriggered = true;

        if (emojiPanelVisible)
        {
            type = emojiObjectNumber >= 10? 10: 0;
            panel = (RelativeLayout)((RelativeLayout) (listOfObjects.get(emojiObjectNumber - type).getChildAt(1))).getChildAt(3);
            panel.setVisibility(View.GONE);
        }

        type = i >= 10? 10: 0;
        panel = (RelativeLayout)((RelativeLayout) (listOfObjects.get(i - type).getChildAt(1))).getChildAt(3);

        if (emojiObjectNumber == i && emojiPanelVisible)
        {
            panel.setVisibility(View.GONE);
            emojiPanelVisible = false;
        }
        else
        {
            panel.setVisibility(View.VISIBLE);
            emojiObjectNumber = i;
            emojiPanelVisible = true;
        }
    }

    @Override
    public void onClick(View view)
    {
        buttonTriggered = false;
        switch (view.getId())
        {
            case ID_SECRET_BTN:
            {
                secretBtn.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.colorPrimary));
                thoughtBtn.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.light_black));
                createListView(SECRET_START_VALUE);
                buttonTriggered = true; break;
            }
            case ID_THOUGHT_BTN:
            {
                thoughtBtn.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.colorPrimary));
                secretBtn.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.light_black));
                createListView(THOUGHT_START_VALUE);
                buttonTriggered = true; break;
            }
        }

        if (!buttonTriggered)
        {
            for (int i = 0; i < EXPEND_BUTTONS.length; i++)
            {
                if (view.getId() == EXPEND_BUTTONS[i])
                {
                    handleEmojiPanel(i);
                }
                if (view.getId() == BUTTONS[i] || view.getId() == IMAGES[i])
                {
                    ShowTopItem item = new ShowTopItem(getActivity(), i);
                    item.show();
                }
            }
        }
    }


    class ListViewObjectAdapter extends ArrayAdapter<RelativeLayout> {
        private Context context;
        ArrayList<RelativeLayout> listOfObjects = new ArrayList<>();

        public ListViewObjectAdapter(Context context, int resource, ArrayList<RelativeLayout> objects) {
            super(context, resource, objects);
            this.context = context;
            this.listOfObjects = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            return listOfObjects.get(position);
        }
    }
}
