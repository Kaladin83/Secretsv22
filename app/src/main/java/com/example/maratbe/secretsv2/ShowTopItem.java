package com.example.maratbe.secretsv2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MARATBE on 12/26/2017.
 */

class ShowTopItem extends Dialog implements View.OnClickListener, View.OnTouchListener, Constants
{
    private Activity activity;
    private Item passedItem;
    private int dialogHeight, dialogWidth, buttonWidth, buttonHeight, statusButtonSize, actionButtonSize, itemIndex;
    private RelativeLayout statusLayout, actionLayout, emojiLayout, emojiSelectedLayout;
    private RelativeLayout.LayoutParams rParams;
    private LinearLayout tagLayout, editLayout, screenLayout;
    private LinearLayout mainLayout;
    private EditText commentEdit;
    private GridView tagGridView;
    private ListView mainListView;
    private Button goUpBtn;
    private ArrayList<LinearLayout> listOfShowedItem = new ArrayList<>();
    ArrayAdapter<LinearLayout> adapterOfShowedItem;

    public ShowTopItem() {
        super(null);
    }

    public ShowTopItem(Activity activity, int itemIndex) {
        super(activity);
        this.activity = activity;
        this.itemIndex = itemIndex;
        passedItem = MainActivity.getItemAt(itemIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_show_item);

        dialogWidth = (int) (MainActivity.getScreenWidth() * 0.9);
        dialogHeight = (int) (MainActivity.getScreenHeight() * 0.5);

        screenLayout = findViewById(R.id.dialog_layout);
        screenLayout.setLayoutParams(new FrameLayout.LayoutParams(dialogWidth, dialogHeight));

        mainLayout = new LinearLayout(getContext());
        mainLayout.setOnTouchListener(this);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(dialogWidth, dialogHeight));
        mainLayout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.light_gray));
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        buttonWidth = mainLayout.getLayoutParams().width /5;
        buttonHeight = mainLayout.getLayoutParams().height/ 13;
        statusButtonSize = dialogWidth / 14;
        actionButtonSize = dialogWidth / 9;
        setTagsLayout();
        setScrollView();
        setStatusLayout();
        setActionLayout();
        setEditLayout();

        listOfShowedItem.add(mainLayout);
        listOfShowedItem.add(editLayout);
        //createCommentsLayout();
        createMainListView(0);
    }

    private void setTagsLayout() 
    {
        String txt;
        setRules(-100, RelativeLayout.CENTER_HORIZONTAL, dialogWidth, buttonHeight);
        setRules(-100, RelativeLayout.ALIGN_PARENT_TOP, dialogWidth, buttonHeight);
        tagLayout = new LinearLayout(getContext());
        tagLayout.setLayoutParams(rParams);
        tagLayout.setId(ID_TAG_LAYOUT);
        tagLayout.setGravity(Gravity.CENTER);
        for (int i = 0; i < 3; i++)
        {
            if (passedItem.getArrayOfTags()[i] != null)
            {
                txt = passedItem.getArrayOfTags()[i];
                Button btn = new Button(getContext());
                btn.setBackground(Util.createBorder(getContext(), 20,ContextCompat.getColor(getContext(),R.color.white), false, 1));
                btn.setPadding(0,0,0,0);
                btn.setAllCaps(false);
                btn.setText(txt);
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(txt.length()*TEXT_SIZE*2,(int)(buttonHeight*0.7));
                p.setMargins(10,10,10,10);
                btn.setLayoutParams(p);

                tagLayout.addView(btn);
            }
        }

        mainLayout.addView(tagLayout);
    }

    private void setScrollView()
    {
        TextView txtView = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dialogWidth, (int)(dialogHeight/3));
        txtView.setLayoutParams(params);
        txtView.setBackgroundColor(Color.WHITE);
        txtView.setText(passedItem.getText());
        txtView.setMovementMethod(new ScrollingMovementMethod());
        txtView.setId(ID_TEXT_SCROLL);
        txtView.setTextSize(20);
        txtView.setPadding(50,0,50,0);

        mainLayout.addView(txtView);
    }

    private void setStatusLayout() 
    {
        boolean removed = false;
        if (statusLayout != null)
        {
            mainLayout.removeView(statusLayout);
            mainLayout.removeView(actionLayout);
            mainLayout.removeView(editLayout);
            removed = true;
        }
        rParams = new RelativeLayout.LayoutParams(dialogWidth, (int)(statusButtonSize*1.3));
        rParams.setMargins(0,7,0,7);
        statusLayout = new RelativeLayout(getContext());
        statusLayout.setLayoutParams(rParams);
        statusLayout.setOnTouchListener(this);
        statusLayout.setBackground(Util.createBorder(getContext(),1, Color.TRANSPARENT, false, 1));
        statusLayout.setPadding(30,8,30,0);
        statusLayout.setId(ID_STATUS_LAYOUT);

        String txt = passedItem.getRating()+"";
        setRules(ID_RATING_TXT_TOP, RelativeLayout.LEFT_OF, statusButtonSize, statusButtonSize);
        createTextVeiw(R.drawable.star, "", ID_STAR_IMG_TOP, rParams, statusLayout);
        setRules(ID_STAR_IMG_TOP, RelativeLayout.ALIGN_PARENT_RIGHT, txt.length()*TEXT_SIZE*2, statusButtonSize);
        createTextVeiw(-100, txt, ID_RATING_TXT_TOP, rParams, statusLayout);

        createEmojiSelectPanel();
        rParams = new RelativeLayout.LayoutParams((int)(dialogWidth/1.7), (int)(statusButtonSize*1.3));
        rParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        statusLayout.addView(emojiSelectedLayout, rParams);

        //rParams = new RelativeLayout.LayoutParams(dialogWidth, (int)(statusButtonSize));
        mainLayout.addView(statusLayout);
        if (removed)
        {
            setActionLayout();
        }
    }

    private void setActionLayout()
    {
        boolean removed = false;
        if (actionLayout != null)
        {
            mainLayout.removeView(actionLayout);
            mainLayout.removeView(editLayout);
            removed = true;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dialogWidth, (int) (actionButtonSize));
        params.setMargins(0,40,0,40);
        actionLayout = new RelativeLayout(getContext());
        actionLayout.setPadding(20,0,0,20);
        actionLayout.setLayoutParams(params);
        actionLayout.setId(ID_ACTION_LAYOUT);
        actionLayout.setOnTouchListener(this);

        setRules(-100, RelativeLayout.ALIGN_PARENT_RIGHT, actionButtonSize, actionButtonSize);
        actionLayout.addView(createButton(true , R.drawable.transparent_share, ID_SHARE_BTN ), rParams);
        String txt = passedItem.getNumOfComments()+"";
        setRules(ID_COMMENTS_IMG, RelativeLayout.RIGHT_OF, (int)(txt.length()*TEXT_SIZE*2.3), actionButtonSize);
        createTextVeiw(-100, txt, ID_NUM_OF_COMMENTS_TXT, rParams, actionLayout);
        setRules(-100, RelativeLayout.ALIGN_PARENT_LEFT, actionButtonSize,  actionButtonSize);
        actionLayout.addView(createButton(true, R.drawable.transparent_comments, ID_COMMENTS_IMG), rParams);

        setRules(ID_SHARE_BTN, RelativeLayout.LEFT_OF, actionButtonSize, actionButtonSize);
        actionLayout.addView(createButton(true, R.drawable.plus, ID_PUT_LIKE_BTN), rParams);
        createEmojiPanel();
        rParams = new RelativeLayout.LayoutParams((int)(dialogWidth /2.1), actionButtonSize);
        rParams.setMargins(0,0,20,0);
        rParams.addRule(RelativeLayout.LEFT_OF, ID_PUT_LIKE_BTN);
        actionLayout.addView(emojiLayout, rParams);

        mainLayout.addView(actionLayout);
        if (removed)
        {
            setEditLayout();
        }
    }

    private void setTagGridView()
    {
        tagGridView = new GridView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(dialogWidth*0.92), (int)(statusButtonSize*3));
        params.setMargins(40,20,40,20);
        tagGridView.setLayoutParams(params);
        tagGridView.setHorizontalSpacing(3);
        tagGridView.setVerticalSpacing(3);
        tagGridView.setPadding(5,5,5,5);
        tagGridView.setBackground(Util.createBorder(getContext(),20,Color.TRANSPARENT, false, 1));
        tagGridView.setNumColumns(3);

        MainActivity.getDbInstance().selectTags();
        String[] items = new String[MainActivity.getTags().size()];
        for(int i = 0; i< items.length; i++)
        {
            items[i] = MainActivity.getTags().get(i).getTagName();
        }

        ArrayAdapter <String> itemsAdapter =
                new ArrayAdapter<String>(getContext(), R.layout.custom_grid_text_view, items);
        tagGridView.setAdapter(itemsAdapter);

        mainLayout.addView(tagGridView);
    }

    private void setEditLayout()
    {
        commentEdit = new EditText(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(dialogWidth*0.7), (int)(statusButtonSize*2));
        commentEdit.setLayoutParams(params);
        commentEdit.setPadding(40,0,40,0);
        commentEdit.setBackgroundColor(Color.TRANSPARENT);
        params = new LinearLayout.LayoutParams((int)(dialogWidth*0.9), (int)(statusButtonSize*2.2));
        params.setMargins(40, 20, 20, 20);
        editLayout = new LinearLayout(getContext());
        editLayout.setOrientation(LinearLayout.HORIZONTAL);
        editLayout.setLayoutParams(params);
        editLayout.addView(commentEdit);
        editLayout.addView(createButton(true,R.drawable.send4, ID_SEND_BTN));
        editLayout.setBackground(Util.createBorder(getContext(),20,Color.TRANSPARENT, false, 1));

        //mainLayout.addView(editLayout);
    }

    private void createMainListView(int position)
    {
        if (position > 1)
        {
            screenLayout.removeView(mainListView);
        }

        mainListView = new ListView(getContext());
        adapterOfShowedItem = new ListViewObjectAdapter(getContext(), 0, listOfShowedItem);
        mainListView.setAdapter(adapterOfShowedItem);
        mainListView.setSelection(position);

        screenLayout.addView(mainListView);
    }

    private void createTextVeiw(int icon, String txt, int id, RelativeLayout.LayoutParams rParams, RelativeLayout panel)
    {
        TextView txtView = new TextView(getContext());
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

    private Button createButton(boolean isImage, int image, int id)
    {
        Button btn = new Button(getContext());
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
            btn.setText(passedItem.getText());
            btn.setPadding(50,0,10,0);
            btn.setBackground(Util.createBorder(getContext(),1, Color.TRANSPARENT, false, 0));
        }

        btn.setTypeface(TabNavigator.getThemeAt(TabNavigator.getActiveThemeNumber()).getMainTypeface());
        btn.setGravity(Gravity.CENTER);
        btn.setOnClickListener(this);

        return btn;
    }

    private void createEmojiPanel()
    {
        emojiLayout = new RelativeLayout(getContext());
        emojiLayout.setVisibility(View.GONE);

        setRules(-100, RelativeLayout.ALIGN_PARENT_LEFT, actionButtonSize, actionButtonSize);
        rParams.setMargins(0,0,10,0);
        emojiLayout.addView(createButton(true ,R.drawable.heart, ID_EMOJI_HEART_IMG ), rParams);
        setRules(ID_EMOJI_HEART_IMG, RelativeLayout.RIGHT_OF, actionButtonSize, actionButtonSize);
        rParams.setMargins(0,0,10,0);
        emojiLayout.addView(createButton(true, R.drawable.lol, ID_EMOJI_LOL_IMG), rParams);
        setRules(ID_EMOJI_LOL_IMG, RelativeLayout.RIGHT_OF, actionButtonSize, actionButtonSize);
        rParams.setMargins(0,0,10,0);
        emojiLayout.addView(createButton(true, R.drawable.sad, ID_EMOJI_SAD_IMG), rParams);
        setRules(ID_EMOJI_SAD_IMG, RelativeLayout.RIGHT_OF, actionButtonSize, actionButtonSize);
        rParams.setMargins(0,0,0,0);
        emojiLayout.addView(createButton(true, R.drawable.angry, ID_EMOJI_ANGRY_IMG), rParams);
    }

    private void createEmojiSelectPanel()
    {
        String txt;
        emojiSelectedLayout = new RelativeLayout(getContext());

        if (passedItem.getEmojis()[0] > 0)
        {
            txt = passedItem.getEmojis()[0]+"";
            setRules(-100, RelativeLayout.ALIGN_PARENT_LEFT, statusButtonSize, statusButtonSize);
            createTextVeiw(R.drawable.heart, "", ID_EMOJI_HEART_IMG, rParams, emojiSelectedLayout);
            setRules(ID_EMOJI_HEART_IMG, RelativeLayout.RIGHT_OF, (int)(txt.length()*TEXT_SIZE*1.7), statusButtonSize);
            createTextVeiw(-100, txt, ID_EMOJI_NUM_OF_HEART_IMG, rParams, emojiSelectedLayout);
        }

        if (passedItem.getEmojis()[1] > 0)
        {
            if (passedItem.getEmojis()[0] > 0)
            {
                setRules(ID_EMOJI_NUM_OF_HEART_IMG, RelativeLayout.RIGHT_OF, statusButtonSize, statusButtonSize);
            }
            else
            {
                setRules(-100, RelativeLayout.ALIGN_PARENT_RIGHT, statusButtonSize, statusButtonSize);
            }
            txt = passedItem.getEmojis()[1]+"";
            createTextVeiw(R.drawable.lol, "", ID_EMOJI_LOL_IMG, rParams, emojiSelectedLayout);
            setRules(ID_EMOJI_LOL_IMG, RelativeLayout.RIGHT_OF, (int)(txt.length()*TEXT_SIZE*1.7), statusButtonSize);
            createTextVeiw(-100, txt, ID_EMOJI_NUM_OF_LOL_IMG, rParams, emojiSelectedLayout);
        }

        if (passedItem.getEmojis()[2] > 0)
        {
            if (passedItem.getEmojis()[1] > 0)
            {
                setRules(ID_EMOJI_NUM_OF_LOL_IMG, RelativeLayout.RIGHT_OF, statusButtonSize, statusButtonSize);
            }
            else if(passedItem.getEmojis()[0] > 0)
            {
                setRules(ID_EMOJI_NUM_OF_HEART_IMG, RelativeLayout.RIGHT_OF, statusButtonSize, statusButtonSize);
            }
            else
            {
                setRules(-100, RelativeLayout.ALIGN_PARENT_RIGHT, statusButtonSize, statusButtonSize);
            }
            txt = passedItem.getEmojis()[2]+"";
            createTextVeiw(R.drawable.sad, "", ID_EMOJI_SAD_IMG, rParams, emojiSelectedLayout);
            setRules(ID_EMOJI_SAD_IMG, RelativeLayout.RIGHT_OF, (int)(txt.length()*TEXT_SIZE*1.7), statusButtonSize);
            createTextVeiw(-100, txt, ID_EMOJI_NUM_OF_SAD_IMG, rParams, emojiSelectedLayout);
        }

        if (passedItem.getEmojis()[3] > 0)
        {
            if (passedItem.getEmojis()[2] > 0)
            {
                setRules(ID_EMOJI_NUM_OF_SAD_IMG, RelativeLayout.RIGHT_OF, statusButtonSize, statusButtonSize);
            }
            else if(passedItem.getEmojis()[1] > 0)
            {
                setRules(ID_EMOJI_NUM_OF_LOL_IMG, RelativeLayout.RIGHT_OF, statusButtonSize, statusButtonSize);
            }
            else if(passedItem.getEmojis()[0] > 0)
            {
                setRules(ID_EMOJI_NUM_OF_HEART_IMG, RelativeLayout.RIGHT_OF, statusButtonSize, statusButtonSize);
            }
            else
            {
                setRules(-100, RelativeLayout.ALIGN_PARENT_RIGHT, statusButtonSize, statusButtonSize);
            }
            txt = passedItem.getEmojis()[3]+"";
            createTextVeiw(R.drawable.angry, "", ID_EMOJI_ANGRY_IMG, rParams, emojiSelectedLayout);
            setRules(ID_EMOJI_ANGRY_IMG, RelativeLayout.RIGHT_OF, (int)(txt.length()*TEXT_SIZE*1.7), statusButtonSize);
            createTextVeiw(-100, txt, ID_EMOJI_NUM_OF_ANGRY_IMG, rParams, emojiSelectedLayout);
        }
    }

    private void setRules(int id_, int rule, int width, int hight)
    {
        rParams = new RelativeLayout.LayoutParams(width, hight);
        if (id_ != -100)
        {
            rParams.addRule(rule, id_);
        }
        else
        {
            rParams.addRule(rule);
        }
    }

    private void closeKeybord()
    {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (this.getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void createCommentsLayout()
    {
        LinearLayout.LayoutParams params;
        TextView comment, timeAdded;
        MainActivity.getDbInstance().selectCommentsData(itemIndex);
      //  Item item = MainActivity.getItemAt(itemIndex);
        ArrayList<Item.Comment> comments = MainActivity.getItemAt(itemIndex).getComments();

        for (int i = 0; i < comments.size(); i++)
        {
            LinearLayout commentLayout = new LinearLayout(getContext());
            params = new LinearLayout.LayoutParams(dialogWidth, (int) (dialogHeight*0.4));
          //  params.setMargins(30,5,10,5);
            commentLayout.setLayoutParams(params);
            commentLayout.setOrientation(LinearLayout.VERTICAL);
            commentLayout.setBackground(Util.createBorder(getContext(), 8,ContextCompat.getColor(getContext(),R.color.light_gray), false, 1));

            comment = new TextView(getContext());
            params = new LinearLayout.LayoutParams((int) (dialogWidth*0.85), (int) (dialogHeight*0.3));
            params.gravity = Gravity.CENTER_HORIZONTAL;
            comment.setLayoutParams(params);
            comment.setPadding(20,0,20,0);
            comment.setText(comments.get(i).getText());
            comment.setBackground(Util.createBorder(getContext(), 8,ContextCompat.getColor(getContext(),R.color.white), false, 1));

            View separator = new View(getContext());
            params = new LinearLayout.LayoutParams((int) (dialogWidth*0.85), 3);
            params.gravity = Gravity.CENTER;
            separator.setLayoutParams(params);
            separator.setBackground(Util.createBorder(getContext(), 8,Color.BLACK, false, 1));

            timeAdded = new TextView(getContext());
            timeAdded.setGravity(Gravity.CENTER);
            timeAdded.setBackground(Util.createBorder(getContext(), 8,ContextCompat.getColor(getContext(),R.color.white), false, 1));
            timeAdded.setText("Commented on:   " + comments.get(i).getDateAdded());

            commentLayout.addView(comment);
            commentLayout.addView(separator);
            commentLayout.addView(timeAdded);
            listOfShowedItem.add(commentLayout);
        }
        LinearLayout goUpLayout = new LinearLayout(getContext());
        goUpLayout.setLayoutParams(new LinearLayout.LayoutParams(dialogWidth, actionButtonSize));
        goUpLayout.setOrientation(LinearLayout.HORIZONTAL);
        params = new LinearLayout.LayoutParams(buttonWidth+30, actionButtonSize);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        goUpBtn = new Button(getContext());
        goUpBtn.setLayoutParams(params);
        goUpBtn.setText("Go up");
        goUpBtn.setId(ID_GO_UP_BTN);
        goUpBtn.setOnClickListener(this);
       // goUpBtn.setGravity(Gravity.CENTER_HORIZONTAL);
        goUpLayout.addView(goUpBtn);
        listOfShowedItem.add(goUpLayout);
        createMainListView(2);
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case ID_PUT_LIKE_BTN:
                if (emojiLayout.getVisibility() == View.VISIBLE) {
                    emojiLayout.setVisibility(View.GONE);
                } else {
                    emojiLayout.setVisibility(View.VISIBLE);
                }
                break;
            case ID_EMOJI_ANGRY_IMG: {
                if (MainActivity.getDbInstance().insertAngry(passedItem.getItemId()) == 0) {
                    MainActivity.getDbInstance().selectStatisticsData(itemIndex);
                    setStatusLayout();
                    break;
                }
            }
            case ID_EMOJI_LOL_IMG: {
                if (MainActivity.getDbInstance().insertLol(passedItem.getItemId()) == 0) {
                    MainActivity.getDbInstance().selectStatisticsData(itemIndex);
                    setStatusLayout();
                    break;
                }
            }
            case ID_EMOJI_HEART_IMG: {
                if (MainActivity.getDbInstance().insertLike(passedItem.getItemId()) == 0) {
                    MainActivity.getDbInstance().selectStatisticsData(itemIndex);
                    setStatusLayout();
                    break;
                }
            }
            case ID_EMOJI_SAD_IMG: {
                if (MainActivity.getDbInstance().insertSad(passedItem.getItemId()) == 0) {
                    MainActivity.getDbInstance().selectStatisticsData(itemIndex);
                    setStatusLayout();
                    break;
                }
            }
            case ID_SEND_BTN: {
                if (!commentEdit.getText().equals("")) {
                    if (MainActivity.getDbInstance().insertComment(passedItem.getItemId(), commentEdit.getText().toString(), 0) == 0) {
                        MainActivity.getDbInstance().selectStatisticsData(itemIndex);
                        setActionLayout();
                        closeKeybord();
                        commentEdit.setText("");
                        break;
                    }
                }
            }
            case ID_COMMENTS_IMG: {
                createCommentsLayout(); break;
            }
            case ID_GO_UP_BTN:
                mainListView.setSelection(0);
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        closeKeybord();
        return true;
    }

    class ListViewObjectAdapter extends ArrayAdapter<LinearLayout> {
        private Context context;
        ArrayList<LinearLayout> listOfObjects = new ArrayList<>();

        public ListViewObjectAdapter(Context context, int resource, ArrayList<LinearLayout> objects) {
            super(context, resource, objects);
            this.context = context;
            this.listOfObjects = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            return listOfObjects.get(position);
        }
    }
}
