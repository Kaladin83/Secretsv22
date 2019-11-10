package com.example.maratbe.secrets;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TopTen extends Fragment implements Constants{
    private int commentObjectNumber = 0, emojiObjectNumber = 0, currentObjectId = 0, starsChosen, action = 0;
    private boolean emojiPanelVisible = false, commentPanelVisible = false;
    private MakeRecyclerView makeRecyclerView;
    private LinearLayout.LayoutParams lParams;
    private LinearLayout progressBarLayout;
    private View fragment;
    private String currentComment = "", currentTimeStamp;
    private EditText addedComment;
    private Theme theme;
    private ShowStars stars;
    private TabNavigator tabNavigator;
    private Item.Comment newComment;
    private RecyclerView secretRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.activity_top_ten2, container, false);

        theme = TabNavigator.getThemeAt(MainActivity.getActiveThemeNumber());
        setMainLayouts();

        if (MainActivity.getUser(0).getUserName().equals(""))
        {
            Util.showNotLoggedInDialog(getString(R.string.register_title), getString(R.string.register_body), getActivity());
        }
        return fragment;
    }

    public void setTabNavigatorInstance(TabNavigator tabNavigator)
    {
        this.tabNavigator = tabNavigator;
    }

    private void setMainLayouts()
    {
        RelativeLayout mainTopTenLayout = fragment.findViewById(R.id.main_top_ten);
        mainTopTenLayout.setGravity(Gravity.CENTER);

        lParams = new LinearLayout.LayoutParams(MainActivity.getScreenHeight()/7, MainActivity.getScreenHeight()/7);
        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setLayoutParams(lParams);
        progressBar.setBackgroundColor(Color.TRANSPARENT);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(theme.getSelectedTitleColor()[2], android.graphics.PorterDuff.Mode.MULTIPLY);

        progressBarLayout = createLinearLayout(LinearLayout.HORIZONTAL, R.color.transparent, 0, View.GONE);
        progressBarLayout.addView(progressBar);
        progressBarLayout.setLayoutParams(lParams);

        populateListView();
        secretRecyclerView = new RecyclerView(getContext());
        secretRecyclerView.setBackgroundColor(theme.getGradientColors()[1]);

        showSecrets(0);

        RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT) ;
        rParams.setMargins(0,10,0,0);
        mainTopTenLayout.addView(secretRecyclerView, rParams);
        mainTopTenLayout.addView(progressBarLayout, rParams);
    }

    private void populateListView()
    {
        makeRecyclerView = new MakeRecyclerView(getContext(), getActivity()) {
            @Override
            protected void updateFavoritesPanel(CardView ticket, Button button, int itemId) {

            }

            @Override
            public void addComment(View view)
            {
                LinearLayout object = (LinearLayout)(view.getParent().getParent());
                currentObjectId = object.getId();

                addedComment = (EditText) ((LinearLayout) object.getChildAt(EDIT_LAYOUT)).getChildAt(SEND_EDIT);
                currentComment = addedComment.getText().toString();

                TextView txt = (TextView) ((LinearLayout)object.getChildAt(STATUS_LAYOUT)).getChildAt(COMMENTS_NUM_BTN);
                txt.setText(String.valueOf(Integer.parseInt(txt.getText().toString())+1));

                if (!addedComment.getText().toString().equals("")) {
                    currentTimeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US).format(new Date());
                    (object.getChildAt(EDIT_LAYOUT)).setVisibility(View.GONE);
                    ((LinearLayout) object.getChildAt(ACTION_LAYOUT)).getChildAt(CHOICE_EMOJI_LAYOUT).setVisibility(View.GONE);
                    Util.closeKeyboard(addedComment, getActivity());
                    addedComment.setText("");

                    stars = new ShowStars(getActivity()) {
                        @Override
                        protected void sendClicked(int numOfStars) {
                            starsChosen = numOfStars;
                            commentPanelVisible = false;
                            emojiPanelVisible = false;
                            MainActivity.getTopTen().get(currentObjectId).setNumOfComments(MainActivity.getTopTen().get(currentObjectId).getNumOfComments()+1);
                            newComment = MainActivity.getTopTen().get(currentObjectId).new Comment(currentComment, starsChosen, currentTimeStamp,  MainActivity.getUser(0).getUserName());
                            action = ADD_COMMENT;
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
                new ProgressTask().execute();
            }

            @Override
            protected void tagClicked(String tagChosen) {
                ((TabNavigator) getActivity()).selectPage(0);
                tabNavigator.getEavesdropsInstance().showExternalTagItems(tagChosen);
            }

            @Override
            protected void showEmojis(View view) {
                int id = ((LinearLayout) view.getParent().getParent()).getId();
             //   MainActivity.getDbInstance().selectVotesOfItem(MainActivity.getTopTen().get(id).getItemId(), MainActivity.getTopTen(), id, true,true);
                ShowEmojies showEmojies = new ShowEmojies(getActivity(), MainActivity.getTopTen().get(id), theme) {
                    @Override
                    protected void removeVote() {
                        Util.removeUserVote(Util.checkVote(MainActivity.getTopTen() ,MainActivity.getTopTen().get(currentObjectId).getVotes(), MainActivity.getUser(0).getUserName()));
                        LinearLayout object = (LinearLayout) (view.getParent().getParent());
                        LinearLayout selectedEmojisPanel = (LinearLayout) ((LinearLayout) object.getChildAt(STATUS_LAYOUT)).getChildAt(SELECTED_EMOJI_LAYOUT);
                        TextView emojiText = (TextView)selectedEmojisPanel.getChildAt(NUM_OF_EMOJIS_TXT);
                        emojiText.setText(String.valueOf(Integer.parseInt(emojiText.getText().toString()) - 1));
                       // MainActivity.getLocalStorage().removeItem(MainActivity.getTopTen().get(currentObjectId).getItemId(), MainActivity.getUser(0), "votes");
                        MainActivity.getLocalStorage().updateDbVotes(MainActivity.getTopTen().get(currentObjectId).getItemId(), MainActivity.getUser(0));
                        tabNavigator.recreateListView(0);
                        tabNavigator.recreateListView(1);
                        AsyncTask.execute(() -> {
                            MainActivity.getDbInstance().deleteVote(MainActivity.getTopTen().get(currentObjectId).getItemId(),
                                    MainActivity.getUser(0).getUserName(), MainActivity.getTopTen().get(currentObjectId).getNumOfVotes(), true, false);
                            MainActivity.getDbInstance().updateScore(currentObjectId, Util.calculateScore(MainActivity.getTopTen().get(currentObjectId)), false, false);
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

                AsyncTask.execute(() ->  MainActivity.getDbInstance().selectCommentsData(MainActivity.getTopTen(), currentObjectId, 0));

                emojiPanel = (LinearLayout) ((LinearLayout) object.getChildAt(ACTION_LAYOUT)).getChildAt(CHOICE_EMOJI_LAYOUT);

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
                commentPanel = (LinearLayout) object.getChildAt(EDIT_LAYOUT);
                LinearLayout emojiPanel = (LinearLayout) view.getParent();

                if (commentObjectNumber == currentObjectId && commentPanelVisible)
                {
                    commentPanel.setVisibility(View.GONE);
                    Util.closeKeyboard((EditText) commentPanel.getChildAt(SEND_EDIT), getActivity());
                    emojiPanel.setVisibility(View.GONE);
                    commentPanelVisible = false;
                    emojiPanelVisible = false;
                }
                else
                {
                    commentPanel.setVisibility(View.VISIBLE);
                    (commentPanel.getChildAt(SEND_EDIT)).requestFocus();
                    Util.openKeyboard(getActivity());
                    commentObjectNumber = currentObjectId;
                    commentPanelVisible = true;
                }
            }

            @Override
            protected void updateEmojiPanel(int emoji, View view)
            {
                LinearLayout object = (LinearLayout)(view.getParent().getParent()).getParent();
                LinearLayout emojiPanel = (LinearLayout) ((LinearLayout) object.getChildAt(ACTION_LAYOUT)).getChildAt(CHOICE_EMOJI_LAYOUT);
                LinearLayout selectedEmojisPanel = (LinearLayout) ((LinearLayout) object.getChildAt(STATUS_LAYOUT)).getChildAt(SELECTED_EMOJI_LAYOUT);
                TextView emojiText = (TextView)selectedEmojisPanel.getChildAt(NUM_OF_EMOJIS_TXT);
                currentObjectId = object.getId();
                Util.updateVotes(emoji, MainActivity.getTopTen(), currentObjectId, selectedEmojisPanel, emojiText, getActivity(), tabNavigator, null, null);
                emojiPanel.setVisibility(View.GONE);
                emojiPanelVisible = false;
            }
        };
        makeRecyclerView.setMetrics(MainActivity.getScreenWidth(), MainActivity.getScreenWidth() / 16, MainActivity.getScreenWidth() / 12);
    }

    public void showSecrets(int action)
    {
        makeRecyclerView.setThemeParameters(theme.getGradientColors()[0], theme.getTitleTypeface(), theme.getMainTypeface(), theme.getDrawbles(),
                MainActivity.getTopTen(), true, false);
        if (action == 1)
        {
            secretRecyclerView.getAdapter().notifyDataSetChanged();
        }
        else
        {
            secretRecyclerView.setAdapter(makeRecyclerView.getRecyclerViewAdapter());
            secretRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        secretRecyclerView.setVisibility(View.VISIBLE);
    }

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

    private class ProgressTask extends AsyncTask <Void,Void,Void>{
        @Override
        protected void onPreExecute(){
            progressBarLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (action == ADD_COMMENT)
            {
                MainActivity.getDbInstance().insertComment(MainActivity.getTopTen().get(currentObjectId).getItemId(), newComment, MainActivity.getTopTen().get(currentObjectId).getNumOfComments(), 0);
              //  listName = "TopTen:9";
            }
          /*  else if(action == UPDATE_COMMENT)
            {
                MainActivity.getDbInstance().updateComments(MainActivity.getTopTen().get(currentObjectId).getItemId(), newComment.getCommentId(), newComment.getDateAdded(),
                        newComment.getText(), newComment.getStars(), true, false);
            }*/
         //   else
           // {
                MainActivity.getDbInstance().selectCommentsData(MainActivity.getTopTen(), currentObjectId, 0);
          //  }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressBarLayout.setVisibility(View.GONE);
            ShowComments item = new ShowComments(getActivity(), "TopTen", currentObjectId, tabNavigator,
                    theme.getGradientColors()[2], theme.getSelectedTitleColor()[2]);
            item.show();
        }
    }
}
