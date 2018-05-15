package com.example.maratbe.secrets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public abstract class ShowEmojies extends Dialog implements Constants
{
    private int dialogWidth, gridHeight, statusEmojiSize;
    private Theme theme;
    private Activity activity;
    private TextView voteTitle;
    private Item passedItem;
    private ShowEmojies showEmojis;
    private LinearLayout mainLayout, statusEmojiLayout;
    private LinearLayout.LayoutParams lParams;
    private GridView usersGridView;
    private GridViewAdapter adapter;
    private ShowAlertDialog alertDialog, alertDialog2;

    public ShowEmojies(Activity activity, Item item, Theme theme) {
        super(activity);
        this.theme = theme;
        this.activity = activity;
        passedItem = item;
        showEmojis = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_show_emojies);

        dialogWidth = (int) (MainActivity.getScreenWidth() * 0.7);
        gridHeight = (int) (MainActivity.getScreenHeight() * 0.15);
        statusEmojiSize = (int) (dialogWidth * 0.16);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dialogWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        mainLayout = findViewById(R.id.show_emojies_layout);
        mainLayout.setLayoutParams(params);
        mainLayout.setBackgroundColor(theme.getGradientColors()[0]);

        createEmojiesLayout();
    }

    private void createEmojiesLayout()
    {
        lParams = new LinearLayout.LayoutParams(dialogWidth, statusEmojiSize);
        lParams.setMargins(0,10,0,10);
        statusEmojiLayout = createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, View.VISIBLE);

        lParams = new LinearLayout.LayoutParams(statusEmojiSize, statusEmojiSize);
        statusEmojiLayout.addView(createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, View.VISIBLE, createEmojiButton(R.drawable.like, R.id.emojis_likes)));
        statusEmojiLayout.addView(createView(1, statusEmojiSize, Color.BLACK));
        statusEmojiLayout.getChildAt(0).setBackgroundColor(theme.getGradientColors()[1]);

        lParams = new LinearLayout.LayoutParams(statusEmojiSize, statusEmojiSize);
        statusEmojiLayout.addView(createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, View.VISIBLE, createEmojiButton(R.drawable.dislike, R.id.emojis_dislikes)));
        statusEmojiLayout.addView(createView(1, statusEmojiSize, Color.BLACK));

        lParams = new LinearLayout.LayoutParams(statusEmojiSize, statusEmojiSize);
        statusEmojiLayout.addView(createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, View.VISIBLE, createEmojiButton(R.drawable.lol, R.id.emojis_lol)));
        statusEmojiLayout.addView(createView(1, statusEmojiSize, Color.BLACK));

        lParams = new LinearLayout.LayoutParams(statusEmojiSize, statusEmojiSize);
        statusEmojiLayout.addView(createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, View.VISIBLE, createEmojiButton(R.drawable.sad, R.id.emojis_sad)));
        statusEmojiLayout.addView(createView(1, statusEmojiSize, Color.BLACK));

        lParams = new LinearLayout.LayoutParams(statusEmojiSize, statusEmojiSize);
        statusEmojiLayout.addView(createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, View.VISIBLE, createEmojiButton(R.drawable.angry, R.id.emojis_angry)));

        mainLayout.addView(statusEmojiLayout);
        mainLayout.addView(createView((int)(dialogWidth* 0.85), 5, Color.BLACK));

        String txt = passedItem.getVotes().getLikes().size() == 1? passedItem.getVotes().getLikes().size() + " user liked it": passedItem.getVotes().getLikes().size()+" users liked it";
        lParams = new LinearLayout.LayoutParams((int)(dialogWidth* 0.9), LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.gravity = Gravity.CENTER;
        voteTitle = createTextView(-100, txt, TEXT_SIZE+3, theme.getTitleColor(), theme.getMainTypeface());
        mainLayout.addView(voteTitle);

        lParams = new LinearLayout.LayoutParams((int)(dialogWidth* 0.7), gridHeight);
        lParams = new LinearLayout.LayoutParams(dialogWidth, gridHeight);
        lParams.gravity = Gravity.CENTER;
        lParams.setMargins(0,15,0,0);
        usersGridView = new GridView(getContext());
        usersGridView.setNumColumns(1);
        usersGridView.setGravity(Gravity.CENTER);
        usersGridView.setLayoutParams(lParams);
        usersGridView.setBackground(Util.createBorder(0, theme.getGradientColors()[1], false, null, 1));
        adapter = new GridViewAdapter(getContext(), passedItem.getVotes().getLikes());
        usersGridView.setAdapter(adapter);
        mainLayout.addView(usersGridView);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusEmojiSize);
        mainLayout.addView(createButton("Remove vote", TEXT_SIZE, theme.getMainTypeface()));
    }

    private View createView(int width, int height, int color)
    {
        lParams = new LinearLayout.LayoutParams(width, height);
        lParams.gravity = Gravity.CENTER;
        View view = new View(getContext());
        view.setLayoutParams(lParams);
        view.setBackgroundColor(color);
        return view;
    }

    private LinearLayout createLinearLayout(int orientation, int color, int  border, int visible, Button button)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (dialogWidth * 0.13), (int) (dialogWidth * 0.13));
        button.setLayoutParams(params);
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(orientation);
        layout.setLayoutParams(lParams);
        layout.setGravity(Gravity.CENTER);
        layout.setVisibility(visible);
        layout.setBackground(Util.createBorder(20, color, false, null, border));
        layout.addView(button);
        return layout;
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

    private Button createButton(String text, int textSize, Typeface typeface)
    {
        Button btn = new Button(getContext());
        btn.setTextSize(textSize);
        btn.setPadding(0,0,0,0);
        btn.setTextColor(TEXT_COLOR);
        btn.setAllCaps(false);
        btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btn.setLayoutParams(lParams);
        btn.setText(text);
        btn.setBackground(Util.createBorder(1, theme.getGradientColors()[0], false, null, 0));
        btn.setTypeface(typeface);
        btn.setOnClickListener(view -> {
            alertDialog = new ShowAlertDialog(activity) {
                @Override
                protected void buttonPressed(int id) {
                    if (id == 0)
                    {
                        int emojiId = findEmojiId(Util.checkVote(null, passedItem.getVotes(), MainActivity.getUser(0).getUserName()));
                        String bodyTxt;
                        if (emojiId == 0)
                        {
                            bodyTxt = "You cannot delete your vote, as you did not voted yet for this secret";
                        }
                        else
                        {
                            removeVote();
                            bodyTxt = "Your vote vas deleted successfully!";
                            updateGridView(emojiId);
                        }

                        alertDialog2 = new ShowAlertDialog(activity) {
                            @Override
                            protected void buttonPressed(int id) {
                                alertDialog2.dismiss();
                            }
                        };
                        alertDialog2.setTexts("Delete vote", bodyTxt, new int[] {R.string.ok});
                        alertDialog2.setCanceledOnTouchOutside(false);
                        Util.setDialogColors(activity, alertDialog2);
                        alertDialog2.show();

                    }
                    alertDialog.dismiss();
                }
            };
            alertDialog.setTexts("Delete vote", "Are you sure you want to delete your vote?", new int[] {R.string.ok, R.string.cancel});
            alertDialog.setCanceledOnTouchOutside(false);
            Util.setDialogColors(activity, alertDialog);
            alertDialog.show();
            showEmojis.dismiss();
        });

        return btn;
    }

    private int findEmojiId(char c) {
        switch (c)
        {
            case 'l': return R.id.emojis_likes;
            case 'd': return R.id.emojis_dislikes;
            case 's': return R.id.emojis_sad;
            case 'a': return R.id.emojis_angry;
            case 'f': return R.id.emojis_lol;
        }
        return 0;
    }

    protected abstract void removeVote();

    public TextView createTextView(int icon, String txt, int textSize, int color, Typeface typeface) {
        TextView txtView = new TextView(getContext());
        txtView.setText(txt);
        txtView.setTextSize(textSize);
        txtView.setTextColor(color);
        txtView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtView.setBackgroundColor(Color.TRANSPARENT);
        txtView.setLayoutParams(lParams);
        if (icon > -100) {
            txtView.setBackgroundResource(icon);
        }
        txtView.setTypeface(typeface);
        return txtView;
    }

    public Button createEmojiButton(int icon, int id) {
        Button btn = new Button(getContext());
        btn.setBackgroundColor(Color.TRANSPARENT);
        btn.setLayoutParams(lParams);
        btn.setBackgroundResource(icon);
        btn.setId(id);
        btn.setOnClickListener(view -> {
            updateGridView(view.getId());
        });
        return btn;
    }

    private void updateGridView(int id) {
        String txt;
        switch (id)
        {
            case R.id.emojis_likes:
                txt = passedItem.getVotes().getLikes().size() == 1? passedItem.getVotes().getLikes().size() + " user liked it":
                        passedItem.getVotes().getLikes().size() + " users liked it";
                toggleColors(2,4,6,8,0);
                adapter = new GridViewAdapter(getContext(), passedItem.getVotes().getLikes()); break;
            case R.id.emojis_dislikes:
                txt = passedItem.getVotes().getDislikes().size() == 1? passedItem.getVotes().getDislikes().size() + " user disliked it":
                        passedItem.getVotes().getLikes().size() + " users disliked it";
                toggleColors(0,4,6,8,2);
                adapter = new GridViewAdapter(getContext(), passedItem.getVotes().getDislikes()); break;
            case R.id.emojis_lol:
                txt = passedItem.getVotes().getLol().size() == 1? "It made "+passedItem.getVotes().getLol().size() + " user laugh":
                        "It made "+passedItem.getVotes().getLol().size() + " users laugh";
                toggleColors(0,2,6,8,4);
                adapter = new GridViewAdapter(getContext(), passedItem.getVotes().getLol()); break;
            case R.id.emojis_sad:
                txt = passedItem.getVotes().getSad().size() == 1? "It made "+passedItem.getVotes().getSad().size() + " user sad":
                        "It made "+ passedItem.getVotes().getSad().size() + " users sad";
                toggleColors(0,2,4,8,6);
                adapter = new GridViewAdapter(getContext(), passedItem.getVotes().getSad()); break;
            default:
                txt = passedItem.getVotes().getAngry().size() == 1? "It made "+passedItem.getVotes().getAngry().size() + " user angry":
                        "It made "+ passedItem.getVotes().getAngry().size() + " users angry";
                toggleColors(0,2,4,6,8);
                adapter = new GridViewAdapter(getContext(), passedItem.getVotes().getAngry()); break;
        }
        voteTitle.setText(txt);
        usersGridView.setAdapter(adapter);
    }

    private void toggleColors(int i, int i1, int i2, int i3, int i4) {
        statusEmojiLayout.getChildAt(i).setBackgroundColor(theme.getGradientColors()[0]);
        statusEmojiLayout.getChildAt(i1).setBackgroundColor(theme.getGradientColors()[0]);
        statusEmojiLayout.getChildAt(i2).setBackgroundColor(theme.getGradientColors()[0]);
        statusEmojiLayout.getChildAt(i3).setBackgroundColor(theme.getGradientColors()[0]);
        statusEmojiLayout.getChildAt(i4).setBackgroundColor(theme.getGradientColors()[1]);
    }


    public class GridViewAdapter extends BaseAdapter {
        Context mContext;
        ArrayList<String> list;

        public GridViewAdapter(Context context, ArrayList<String> list) {
            this.mContext = context;
            this.list = list;
        }

        @Override
        public int getCount()
        {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            Button button;

            button = new Button(getContext());
            button.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.getScreenWidth()/3, 90));
            button.setTextSize(TEXT_SIZE - 3);
            button.setAllCaps(false);
            button.setTypeface(theme.getTitleTypeface());
            button.setBackgroundColor(Color.TRANSPARENT);
            button.setText(list.get(position));
            button.setPadding(15,10,15,10);
            button.setId(position);
            button.setOnClickListener(view ->
            {
                //MainActivity.getItemsByTag().clear();
                //selectedTag = (Button) view;
                //selectedTagText = selectedTag.getText().toString();
                //task = Eavesdrops.Task.FETCH_TAG_ITEMS;
                //new Eavesdrops.ProgressTask().execute();
            });
            return button;
        }
    }
}
