package com.example.maratbe.secrets;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by MARATBE on 4/8/2018.
 */

public class Favorites  extends AppCompatActivity implements Constants, View.OnClickListener {
    private LinearLayout progressBarLayout, headerLayout;
    private LinearLayout.LayoutParams lParams;
    private Theme theme;
    private Favorites favorites;
    private LinearLayoutManager layoutManager;
    private TextView numOfItemsTxt;
    private RecyclerView recyclerView;
    private MakeRecyclerView makeRecyclerView;
    private ArrayList<Item> list, sublist;
    private int currentObjectId, starsChosen, emojiObjectNumber, commentObjectNumber, nowShowing = 10, numberOfItems = 0, nextPrev = 0, userIndex;
    private String currentTimeStamp, listName = "GetUsersPinned";
    private String[] arrayOfBounds;
    private int[] arrayOfItemIdsLengths;
    private boolean commentPanelVisible, emojiPanelVisible;
    private ShowStars stars;
    private Item.Comment newComment;
    private Task task;

    public enum Task {
        FETCH_COMMENTS, ADD_COMMENT, FETCH_ITEMS
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_display);
        theme = TabNavigator.getThemeAt(MainActivity.getActiveThemeNumber());
        favorites = this;

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_user_display_layout);
        mainLayout.setGravity(Gravity.CENTER);

        createHeaderLayout();
        populateListView();
        createProgressBar();
        fillUpArrays();
        settingValues();

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(makeRecyclerView.getRecyclerViewAdapter());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setBackgroundColor(theme.getGradientColors()[1]);
        recyclerView.addOnScrollListener(new MyRecyclerScroll(layoutManager, listName, 0) {
            @Override
            public void onLoadMore() {
                if (nextPrev < arrayOfItemIdsLengths.length - 1) {
                    task = Task.FETCH_ITEMS;
                    nextPrev++;
                    nowShowing += arrayOfItemIdsLengths[nextPrev];
                    new ProgressTask().execute();
                    layoutManager.scrollToPositionWithOffset(nowShowing - 1, 0);
                }
            }
        });

        RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mainLayout.addView(progressBarLayout, rParams);

        showSecrets();
    }

    private void fillUpArrays()
    {
        int size;
        numberOfItems = MainActivity.getUser(userIndex).getNumOfPinned();
        size = numberOfItems % MAX_ITEMS_SHOWS == 0? numberOfItems / MAX_ITEMS_SHOWS: numberOfItems / MAX_ITEMS_SHOWS +1;
        fillUpArrays(size);
        nowShowing = arrayOfItemIdsLengths[0];
    }

    private void fillUpArrays(int size) {
        arrayOfItemIdsLengths = new int[size];
        arrayOfBounds = new String[size];
        for (int i = 0; i < arrayOfItemIdsLengths.length; i++)
        {
            if (i == arrayOfItemIdsLengths.length - 1)
            {
                arrayOfItemIdsLengths[i] = numberOfItems % MAX_ITEMS_SHOWS;
                arrayOfBounds[i] = String.valueOf(i*MAX_ITEMS_SHOWS)+","+String.valueOf(i*10+(numberOfItems % MAX_ITEMS_SHOWS));
            }
            else
            {
                arrayOfItemIdsLengths[i] = MAX_ITEMS_SHOWS;
                arrayOfBounds[i] = String.valueOf(i*MAX_ITEMS_SHOWS)+","+String.valueOf(i*MAX_ITEMS_SHOWS+MAX_ITEMS_SHOWS);
            }
        }
    }

    private void settingValues() {
        String txt ="";
        String[] bounds =  arrayOfBounds[nextPrev].split(",");
        list = MainActivity.getUser(userIndex).getUsersPinned();
        sublist = new ArrayList<>(MainActivity.getUser(userIndex).getUsersPinned().subList(Integer.parseInt(bounds[0]), Integer.parseInt(bounds[1])));
        String numOfItems = "Showing "+ Integer.parseInt(bounds[1]) + " secrets out of "+ numberOfItems + txt;
        numOfItemsTxt.setText(numOfItems);
    }

    private void createHeaderLayout() {
        headerLayout = (LinearLayout) findViewById(R.id.header_layout);
        headerLayout.setGravity(Gravity.CENTER);
        headerLayout.setOrientation(LinearLayout.VERTICAL);
        headerLayout.setId(R.id.user_display_header_layout);
        headerLayout.setBackgroundColor(theme.getGradientColors()[2]);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(0,30,0,0);
        String text = "My Favorites";
        TextView title = createTextView(text, TEXT_SIZE+8, R.id.user_display_title, theme.getSelectedTitleColor()[2]);
        lParams = new LinearLayout.LayoutParams(MainActivity.getScreenWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(0,20,0,20);
        lParams.gravity = Gravity.CENTER_HORIZONTAL;
        numOfItemsTxt = createTextView("", TEXT_SIZE, R.id.user_display_sub_title, theme.getTitleColor());
        numOfItemsTxt.setBackgroundColor(theme.getGradientColors()[0]);
        headerLayout.addView(title);
        headerLayout.addView(numOfItemsTxt);
    }

    private TextView createTextView(String text, int size, int id, int color) {
        TextView txt = new TextView(this);
        txt.setText(text);
        txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txt.setTextSize(size);
        txt.setTextColor(color);
        txt.setId(id);
        txt.setLayoutParams(lParams);
        txt.setTypeface(theme.getMainTypeface());
        return txt;
    }

    private void createProgressBar() {
        lParams = new LinearLayout.LayoutParams(MainActivity.getScreenHeight()/9, MainActivity.getScreenHeight()/9);
        lParams.setMargins(0,0,100,0);
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(lParams);
        progressBar.setBackgroundColor(Color.TRANSPARENT);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(theme.getSelectedTitleColor()[2], android.graphics.PorterDuff.Mode.MULTIPLY);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView txt = createTextView(getString(R.string.loading_secrets_data), 15, 0, Color.BLACK);
        txt.setTypeface(theme.getTitleTypeface());
        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout = new LinearLayout(this);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(Color.WHITE);
        layout.addView(progressBar);
        layout.addView(txt);
        layout.setPadding(40,0,40,0);

        progressBarLayout = new LinearLayout(this);
        progressBarLayout.setVisibility(View.GONE);
        progressBarLayout.setGravity(Gravity.CENTER);
        progressBarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        progressBarLayout.addView(layout);
    }

    private void populateListView()
    {
        makeRecyclerView = new MakeRecyclerView(this, this) {
            @Override
            public void addComment(View view)
            {
                LinearLayout object = (LinearLayout)(view.getParent().getParent());
                currentObjectId = object.getId();

                final EditText addedComment = (EditText) ((LinearLayout) object.getChildAt(EDIT_LAYOUT+1)).getChildAt(SEND_EDIT);
                final String currentComment = addedComment.getText().toString();

                TextView txt = (TextView) ((LinearLayout)object.getChildAt(STATUS_LAYOUT+1)).getChildAt(COMMENTS_NUM_BTN);
                txt.setText(String.valueOf(Integer.parseInt(txt.getText().toString())+1));

                if (!addedComment.getText().toString().equals("")) {
                    currentTimeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US).format(new Date());
                    (object.getChildAt(EDIT_LAYOUT+1)).setVisibility(View.GONE);
                    ((LinearLayout) object.getChildAt(ACTION_LAYOUT+1)).getChildAt(CHOICE_EMOJI_LAYOUT).setVisibility(View.GONE);
                    Util.closeKeyboard(addedComment, favorites);
                    addedComment.setText("");

                    stars = new ShowStars(favorites) {
                        @Override
                        protected void sendClicked(int numOfStars) {
                            starsChosen = numOfStars;
                            commentPanelVisible = false;
                            emojiPanelVisible = false;
                            list.get(currentObjectId).setNumOfComments(list.get(currentObjectId).getNumOfComments()+1);
                            newComment = list.get(currentObjectId).new Comment(currentComment, starsChosen, currentTimeStamp,  MainActivity.getUser(0).getUserName());
                            //list.get(currentObjectId).getComments().add(newComment);
                            task = Task.ADD_COMMENT;
                            new ProgressTask().execute();
                            stars.dismiss();
                        }
                    };
                    stars.show();
                }
            }

            @Override
            protected void showComments(int index)
            {
                currentObjectId = index;
                task = Task.FETCH_COMMENTS;
                new ProgressTask().execute();
            }

            @Override
            protected void tagClicked(String tagChosen) {
                TabNavigator.getTabNavigatorInstance().selectPage(0);
                //.getEavesdropsInstance().showExternalTagItems(tagChosen);
            }

            @Override
            protected void showEmojis(View view) {
                int id = ((LinearLayout) view.getParent().getParent()).getId();
                ShowEmojies showEmojies = new ShowEmojies(favorites, sublist.get(id), theme) {
                    @Override
                    protected void removeVote() {
                        Util.removeUserVote(Util.checkVote(list, list.get(currentObjectId).getVotes(), MainActivity.getUser(0).getUserName()));
                        LinearLayout object = (LinearLayout) (view.getParent().getParent());
                        LinearLayout selectedEmojisPanel = (LinearLayout) ((LinearLayout) object.getChildAt(STATUS_LAYOUT+1)).getChildAt(SELECTED_EMOJI_LAYOUT);
                        TextView emojiText = (TextView)selectedEmojisPanel.getChildAt(NUM_OF_EMOJIS_TXT);
                        emojiText.setText(String.valueOf(Integer.parseInt(emojiText.getText().toString()) - 1));
                        MainActivity.getLocalStorage().removeItem(list.get(currentObjectId).getItemId(), MainActivity.getUser(0), "votes");
                        showSecrets();
                        AsyncTask.execute(() -> {
                            MainActivity.getDbInstance().deleteVote(list.get(currentObjectId).getItemId(),
                                    MainActivity.getUser(0).getUserName(), list.get(currentObjectId).getNumOfVotes(), true, false);
                            MainActivity.getDbInstance().updateScore(currentObjectId, Util.calculateScore(list.get(currentObjectId)), false, false);
                        });
                    }
                };
                showEmojies.show();
            }

            @Override
            protected void handleEmojiPanel(View view)
            {
                LinearLayout emojiPanel;
                LinearLayout object = (LinearLayout) (view.getParent()).getParent();
                currentObjectId = object.getId();

                AsyncTask.execute(() -> MainActivity.getDbInstance().selectCommentsData(list, currentObjectId, 0));

                emojiPanel = (LinearLayout) ((LinearLayout) object.getChildAt(ACTION_LAYOUT+1)).getChildAt(CHOICE_EMOJI_LAYOUT);

                if (emojiObjectNumber == currentObjectId && emojiPanelVisible)
                {
                    emojiPanel.setVisibility(View.GONE);
                    emojiPanelVisible = false;
                }
                else
                {
                    emojiPanel.setVisibility(View.VISIBLE);
                    emojiObjectNumber = currentObjectId;
                    emojiPanelVisible = true;
                }
            }

            @Override
            protected void handleAddCommentPanel(View view)
            {
                LinearLayout commentPanel;
                LinearLayout object = (LinearLayout)(view.getParent().getParent()).getParent();
                currentObjectId = object.getId();
                commentPanel = (LinearLayout) object.getChildAt(EDIT_LAYOUT+1);
                LinearLayout emojiPanel = (LinearLayout) view.getParent();

                if (commentObjectNumber == currentObjectId && commentPanelVisible)
                {
                    commentPanel.setVisibility(View.GONE);
                    Util.closeKeyboard((EditText) commentPanel.getChildAt(SEND_EDIT), favorites);
                    emojiPanel.setVisibility(View.GONE);
                    commentPanelVisible = false;
                    emojiPanelVisible = false;
                }
                else
                {
                    commentPanel.setVisibility(View.VISIBLE);
                    (commentPanel.getChildAt(SEND_EDIT)).requestFocus();
                    Util.openKeyboard(favorites);
                    commentObjectNumber = currentObjectId;
                    commentPanelVisible = true;
                }
            }

            @Override
            protected void updateEmojiPanel(int emoji, View view)
            {
                LinearLayout object = (LinearLayout)(view.getParent().getParent()).getParent();
                LinearLayout emojiPanel = (LinearLayout) ((LinearLayout) object.getChildAt(ACTION_LAYOUT+1)).getChildAt(CHOICE_EMOJI_LAYOUT);
                LinearLayout selectedEmojisPanel = (LinearLayout) ((LinearLayout) object.getChildAt(STATUS_LAYOUT+1)).getChildAt(SELECTED_EMOJI_LAYOUT);
                TextView emojiText = (TextView)selectedEmojisPanel.getChildAt(NUM_OF_EMOJIS_TXT);
                currentObjectId = object.getId();
                Util.updateVotes(emoji, list, currentObjectId, selectedEmojisPanel, emojiText, favorites, null, null, favorites);
                emojiPanel.setVisibility(View.GONE);
                emojiPanelVisible = false;
            }
        };
        makeRecyclerView.setMetrics((int)(MainActivity.getScreenWidth()*0.9), MainActivity.getScreenWidth() / 16, MainActivity.getScreenWidth() / 12);
    }

    public void showSecrets()
    {
        settingValues();
        makeRecyclerView.setThemeParameters(theme.getGradientColors()[0], theme.getTitleTypeface(), theme.getMainTypeface(), theme.getDrawbles(),
                list, false, true);

        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {

    }

    private class ProgressTask extends AsyncTask <Void,Void,Void>{
        @Override
        protected void onPreExecute(){
            progressBarLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            switch(task)
            {
                case FETCH_COMMENTS:
                    MainActivity.getDbInstance().selectCommentsData(sublist, currentObjectId, 0); break;
                case ADD_COMMENT:
                    MainActivity.getDbInstance().insertComment(list.get(currentObjectId).getItemId(), newComment, list.get(currentObjectId).getNumOfComments(), 0);
                case FETCH_ITEMS:
                    MainActivity.getDbInstance().selectUsersPinned(MainActivity.getUser(userIndex).getSecrets(nextPrev), userIndex, nextPrev, true, true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressBarLayout.setVisibility(View.GONE);
            switch(task) {
                case FETCH_COMMENTS:
                    ShowComments item = new ShowComments(favorites, listName+":0", currentObjectId, TabNavigator.getTabNavigatorInstance(), theme.getGradientColors()[2], theme.getSelectedTitleColor()[2]);
                    item.show(); break;
                case FETCH_ITEMS:
                    showSecrets(); break;
                case ADD_COMMENT:
                    ShowComments showItem = new ShowComments(favorites, listName+":0", currentObjectId, TabNavigator.getTabNavigatorInstance(),
                            theme.getGradientColors()[2], theme.getSelectedTitleColor()[2]);
                    showItem.show();
            }
        }
    }
}
