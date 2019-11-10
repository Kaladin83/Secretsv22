package com.example.maratbe.secrets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowComments extends Dialog implements Constants
{
    private int nowShowing = MAX_ITEMS_SHOWS, dialogWidth, dialogHeight, buttonSize, backgroundColor, titleColor,
             commentId = 0, objectHeight = 0, currentItem;
    private String userName ="", listName = "";
    private boolean commentChoiceChanged = false;
    private Item.Comment passedComment, newComment;
    private ArrayList<Item> listOfItems;
    private Activity activity;
    private LinearLayout.LayoutParams lParams;
    private LinearLayout progressBarLayout;
    private RecyclerView recyclerView;
    private TabNavigator tabNavigator;
    private TextView progressTxt;
    private ShowEditCommentDialog editComment;
    private ShowComments dialog;
    private LinearLayoutManager layoutManager;
    private TextView numOfCommentsTxt;
    private Task task;
    public enum Task {
        FETCH_COMMENTS, UPDATE_COMMENTS, FETCH_USER_DATA
    }

    public ShowComments(Activity activity, String listName, int currentItem, TabNavigator tabNavigator, int backgroundColor, int titleColor) {
        super(activity);
        this.activity = activity;
        this.tabNavigator = tabNavigator;
        this.listName = listName;
        this.currentItem = currentItem;
        this.listOfItems = Util.getList(this.listName);
        this.backgroundColor = backgroundColor;
        this.titleColor = titleColor;
        dialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_show_item);
       // MainActivity.getLocalStorage().clearCommentsData();

        createComments();

        this.setOnKeyListener((dialogInterface, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss();
            }
            return true;
        });
    }

    private void createProgressBar() {
        lParams = new LinearLayout.LayoutParams(MainActivity.getScreenHeight() / 9, MainActivity.getScreenHeight() / 9);
        lParams.setMargins(0,0,100,0);
        lParams.gravity = Gravity.CENTER;
        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setLayoutParams(lParams);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(
                titleColor, android.graphics.PorterDuff.Mode.MULTIPLY);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        progressTxt = new TextView(getContext());
        progressTxt.setTextSize(15);
        progressTxt.setTextColor(Color.BLACK);
        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(lParams);
        layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        layout.setGravity(Gravity.CENTER);
        layout.addView(progressBar);
        layout.addView(progressTxt);
        layout.setPadding(40,0,40,0);

        lParams = new LinearLayout.LayoutParams(dialogWidth, dialogHeight);
        progressBarLayout = new LinearLayout(getContext());
        createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, View.GONE, progressBarLayout);
        progressBarLayout.setElevation(3);
        progressBarLayout.addView(layout);
    }

    private void createComments() {
        dialogWidth = (int) (MainActivity.getScreenWidth() * 0.9);
        dialogHeight = (int) (MainActivity.getScreenHeight() * 0.9);
        buttonSize = (int) (dialogWidth * 0.22);

        createProgressBar();
        createHeaderLayout();

        RelativeLayout mainLayout = findViewById(R.id.dialog_layout);
        mainLayout.setLayoutParams(new FrameLayout.LayoutParams(dialogWidth, dialogHeight));
        mainLayout.addView(progressBarLayout);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = findViewById(R.id.show_comments_recycler_view);
        recyclerView.setAdapter(new RecyclerViewAdapter (listOfItems.get(currentItem).getComments()));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setBackgroundColor(backgroundColor);
        recyclerView.addOnScrollListener(new MyRecyclerScroll(layoutManager, "comments:"+listName, currentItem) {
            @Override
            public void onLoadMore() {
                task = Task.FETCH_COMMENTS;
                nowShowing += MAX_ITEMS_SHOWS;
                new ProgressTask().execute();
                layoutManager.scrollToPositionWithOffset(nowShowing - MAX_ITEMS_SHOWS - 1, 0);
            }
        });
    }

    private void createHeaderLayout (){
        lParams = new LinearLayout.LayoutParams(dialogWidth, buttonSize);
        LinearLayout headerLayout = findViewById(R.id.show_comments_header_layout);
        headerLayout.setBackgroundColor(backgroundColor);

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth * 0.9), 3);
        lParams.gravity = Gravity.CENTER;
        View separator = new View(getContext());
        separator.setBackgroundColor(titleColor);
        separator.setLayoutParams(lParams);

        lParams = new LinearLayout.LayoutParams(dialogWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(0, 30, 0, 30);
        numOfCommentsTxt = new TextView(getContext());
        numOfCommentsTxt.setGravity(Gravity.CENTER);
        numOfCommentsTxt.setLayoutParams(lParams);
        numOfCommentsTxt.setTextSize(TEXT_SIZE+4);
        numOfCommentsTxt.setText("Displaying " + Math.min(nowShowing, listOfItems.get(currentItem).getNumOfComments()) + " comments out of " + listOfItems.get(currentItem).getNumOfComments());
        numOfCommentsTxt.setTypeface(TabNavigator.getThemeAt(MainActivity.getActiveThemeNumber()).getMainTypeface());
        numOfCommentsTxt.setTextColor(titleColor);

        headerLayout.addView(numOfCommentsTxt);
        headerLayout.addView(separator);
    }

    private void createTrailerLayout(RecyclerView.ViewHolder holder)
    {
        RecyclerViewAdapter.MyViewHolder2 viewHolder = (RecyclerViewAdapter.MyViewHolder2) holder;
        lParams = new LinearLayout.LayoutParams(dialogWidth, buttonSize);
        createLinearLayout(LinearLayout.VERTICAL, backgroundColor, 0, View.VISIBLE, viewHolder.trailerLayout);

        lParams = new LinearLayout.LayoutParams((int)(dialogWidth*0.9), 3);
        lParams.gravity = Gravity.CENTER;
        viewHolder.trailerSeparator.setBackgroundColor(titleColor);
        viewHolder.trailerSeparator.setLayoutParams(lParams);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.gravity = Gravity.CENTER_HORIZONTAL;
        viewHolder.trailerTxt.setLayoutParams(lParams);
        viewHolder.trailerTxt.setText(R.string.show_comments_trailer_txt);
        viewHolder.trailerTxt.setGravity(Gravity.CENTER);
        viewHolder.trailerTxt.setTextSize(TEXT_SIZE+4);
        viewHolder.trailerTxt.setTextColor(titleColor);
        viewHolder.trailerTxt.setTypeface(TabNavigator.getThemeAt(MainActivity.getActiveThemeNumber()).getMainTypeface());
        viewHolder.trailerTxt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

    private void createCommentLayout(int i, final Item.Comment currentComment, RecyclerView.ViewHolder holder, boolean enable)
    {
        final RecyclerViewAdapter.MyViewHolder1 viewHolder = (RecyclerViewAdapter.MyViewHolder1) holder;
        this.passedComment = currentComment;

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth * 0.87), LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(20, 50, 25, 0);
        if (i % 2 == 0) {
            lParams.gravity = Gravity.START;

        } else {
            lParams.gravity = Gravity.END;
        }
        createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, View.VISIBLE, viewHolder.wholeObjectLayout);
        viewHolder.wholeObjectLayout.setId(i);

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth * 0.79), LinearLayout.LayoutParams.WRAP_CONTENT);
        createLinearLayout(LinearLayout.VERTICAL, Color.WHITE, 1, View.VISIBLE, viewHolder.objectLayout);

        viewHolder.objectLayout.setClickable(true);
        viewHolder.objectLayout.setOnClickListener(view -> {
            LinearLayout wholeObject = (LinearLayout)view.getParent();
            objectHeight = wholeObject.getChildAt(0).getHeight();
            LinearLayout objectButtonLayout = (LinearLayout) wholeObject.getChildAt(1);
            userName = ((Button)((LinearLayout)((LinearLayout)((LinearLayout)wholeObject.getChildAt(0)).getChildAt(0)).getChildAt(0)).getChildAt(0)).getText().toString();
            if (objectButtonLayout.getVisibility() == View.GONE && MainActivity.getUser(0).getUserName().equals(userName))
            {
                objectButtonLayout.setLayoutParams(new LinearLayout.LayoutParams((int) (dialogWidth * 0.08), objectHeight));
                lParams = new LinearLayout.LayoutParams((int) (dialogWidth * 0.08), (int) (dialogWidth * 0.08));
                lParams.gravity = Gravity.BOTTOM;
                lParams.setMargins(0, objectHeight - (int)(dialogWidth * 0.16), 0, 0);
                Button deleteButton = (Button) objectButtonLayout.getChildAt(1);
                deleteButton.setLayoutParams(lParams);
                objectButtonLayout.setVisibility(View.VISIBLE);
            }
            else
            {
                objectButtonLayout.setVisibility(View.GONE);
            }
        });

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth * 0.78), LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(0,5,0,0);
        createLinearLayout(LinearLayout.HORIZONTAL, Color.WHITE, 0, View.VISIBLE, viewHolder.commentHeaderLayout);

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth * 0.78)-(44*7), MainActivity.getScreenHeight()/18);
        viewHolder.leftHeaderLayout.setLayoutParams(lParams);

        lParams = new LinearLayout.LayoutParams((TEXT_SIZE-3)*passedComment.getUserName().length()*2, MainActivity.getScreenHeight()/18);
        viewHolder.userBtn.setLayoutParams(lParams);
        viewHolder.userBtn.setPadding(0, 0, 0, 0);
        viewHolder.userBtn.setTextColor(titleColor);
        viewHolder.userBtn.setTextSize(TEXT_SIZE-3);
        viewHolder.userBtn.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        viewHolder.userBtn.setAllCaps(false);
        viewHolder.userBtn.setBackgroundColor(Color.TRANSPARENT);
        viewHolder.userBtn.setText(passedComment.getUserName());
        viewHolder.userBtn.setEnabled(enable);
        viewHolder.userBtn.setOnClickListener(view -> {
            userName = viewHolder.userBtn.getText().toString();
            if (MainActivity.getUser(0).getUserName().equals(userName))
            {
                showUserProfile(0);
            }
            else
            {
                User user = new User();
                user.setUserName(userName);
                MainActivity.setUser(user, 1);
                task = Task.FETCH_USER_DATA;
                new ProgressTask().execute();
            }
        });

        new MakeStars(getContext(),  44*6, 44, currentComment.getStars() - 1, viewHolder.starLayout, false) {
            @Override
            public void fillStars(Button chosenStar, LinearLayout starsLayout) {
                for (int i = 0; i< 5; i++)
                {
                    if (i < passedComment.getStars())
                    {
                        viewHolder.starLayout.getChildAt(i).setSelected(true);
                    }
                    else
                    {
                        viewHolder.starLayout.getChildAt(i).setSelected(false);
                    }
                }
            }
        };

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth * 0.79), LinearLayout.LayoutParams.WRAP_CONTENT);
        viewHolder.messageTxt.setPadding(15, 5, 5, 10);
        viewHolder.messageTxt.setLayoutParams(lParams);
        viewHolder.messageTxt.setSingleLine(false);
        viewHolder.messageTxt.setTextSize(TEXT_SIZE - 5);
        viewHolder.messageTxt.setText(Util.decodeStringUrl(passedComment.getText()));

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth * 0.72), 3);
        lParams.gravity = Gravity.CENTER;
        viewHolder.separator.setLayoutParams(lParams);
        viewHolder.separator.setBackground(Util.createBorder(8, Color.BLACK, false, null, 1));

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth * 0.83), LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(0,5,0,5);
        viewHolder.TSTxt.setGravity(Gravity.CENTER);
        viewHolder.TSTxt.setLayoutParams(lParams);
        viewHolder.TSTxt.setTextSize(BUTTON_TEXT_SIZE);
        viewHolder.TSTxt.setText(getContext().getString(R.string.commented_on, passedComment.getDateAdded()));

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(0,5,0,5);
        createLinearLayout(LinearLayout.HORIZONTAL, Color.WHITE, 0, View.VISIBLE, viewHolder.likesLayout);

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth * 0.08), (int) (dialogWidth * 0.08));
        lParams.setMargins(0,0,0,0);
        viewHolder.likeBtn.setLayoutParams(lParams);
        viewHolder.likeBtn.setBackground(getContext().getDrawable(R.drawable.state_list_like));
        viewHolder.likeBtn.setOnClickListener(view ->{
            LinearLayout wholeObject = (LinearLayout)view.getParent().getParent();
            userName = ((Button)((LinearLayout)((LinearLayout)wholeObject.getChildAt(0)).getChildAt(0)).getChildAt(0)).getText().toString();
            if (MainActivity.getUser(0).isLoggedIn() && !MainActivity.getUser(0).getUserName().equals(userName))
            {
                commentId = listOfItems.get(currentItem).getComments().get(((LinearLayout)view.getParent().getParent().getParent()).getId()).getCommentId();
                TextView likeTxtView = ((TextView)((LinearLayout) view.getParent()).getChildAt(1));
                TextView dislikeTxtView = ((TextView)((LinearLayout) view.getParent()).getChildAt(3));
                if (MainActivity.getLocalStorage().isCommentLike(commentId, MainActivity.getUser(0)))
                {
                    viewHolder.likeBtn.setSelected(false);
                    MainActivity.getLocalStorage().updateDbLikes(commentId, MainActivity.getUser(0));
                    MainActivity.getLocalStorage().updateUserScore("comments", MainActivity.getUser(0).getCommentsScore() - 1, MainActivity.getUser(0));
                    likeTxtView.setText(String.valueOf(Integer.parseInt(likeTxtView.getText().toString()) - 1));
                    AsyncTask.execute(() -> MainActivity.getDbInstance().insertLikeComment(commentId, Integer.parseInt(likeTxtView.getText().toString()), true, true));
                }
                else
                {
                    likeTxtView.setText(String.valueOf(Integer.parseInt(likeTxtView.getText().toString()) + 1));
                    Button dislikeBtn = ((Button)((LinearLayout) view.getParent()).getChildAt(2));
                    view.setSelected(true);
                    dislikeBtn.setSelected(false);
                    MainActivity.getLocalStorage().updateUserScore("comments", MainActivity.getUser(0).getCommentsScore() + 1, MainActivity.getUser(0));
                    if (MainActivity.getLocalStorage().updateDbDislikes(commentId, MainActivity.getUser(0)))
                    {
                        commentChoiceChanged = true;
                        MainActivity.getLocalStorage().updateUserScore("comments", MainActivity.getUser(0).getCommentsScore() - 1, MainActivity.getUser(0));
                        dislikeTxtView.setText(String.valueOf(Integer.parseInt(dislikeTxtView.getText().toString()) - 1));
                    }

                    MainActivity.getLocalStorage().updateCommentsLikes(MainActivity.getUser(0), commentId);
                    AsyncTask.execute(() -> {
                        MainActivity.getDbInstance().insertLikeComment(commentId, Integer.parseInt(likeTxtView.getText().toString()), true, true);
                        if (commentChoiceChanged)
                        {
                            MainActivity.getDbInstance().insertDislikeComment(commentId, Integer.parseInt(dislikeTxtView.getText().toString()), true, true);
                        }
                    });
                }
            }
        });
        viewHolder.dislikeBtn.setLayoutParams(lParams);
        viewHolder.dislikeBtn.setBackground(getContext().getDrawable(R.drawable.state_list_dislike));
        viewHolder.dislikeBtn.setOnClickListener(view ->{
            LinearLayout wholeObject = (LinearLayout)view.getParent().getParent();
            userName = ((Button)((LinearLayout)((LinearLayout)wholeObject.getChildAt(0)).getChildAt(0)).getChildAt(0)).getText().toString();
            if (MainActivity.getUser(0).isLoggedIn() && !MainActivity.getUser(0).getUserName().equals(userName))
            {
                commentChoiceChanged = false;
                commentId = listOfItems.get(currentItem).getComments().get(((LinearLayout)view.getParent().getParent().getParent()).getId()).getCommentId();
                TextView dislikeTxtView = ((TextView)((LinearLayout) view.getParent()).getChildAt(3));
                TextView likeTxtView = ((TextView)((LinearLayout) view.getParent()).getChildAt(1));
                if (MainActivity.getLocalStorage().isCommentDislike(commentId, MainActivity.getUser(0)))
                {
                    viewHolder.dislikeBtn.setSelected(false);
                    MainActivity.getLocalStorage().updateDbDislikes(commentId, MainActivity.getUser(0));
                    dislikeTxtView.setText(String.valueOf(Integer.parseInt(dislikeTxtView.getText().toString()) - 1));
                    MainActivity.getLocalStorage().updateUserScore("comments", MainActivity.getUser(0).getCommentsScore() - 1, MainActivity.getUser(0));
                    AsyncTask.execute(() -> {
                        MainActivity.getDbInstance().insertDislikeComment(commentId, Integer.parseInt(dislikeTxtView.getText().toString()), true, true);
                        // MainActivity.getDbInstance().updateScore(currentObjectId, Util.calculateScore(MainActivity.getTopTen().get(currentObjectId)), true, false);
                    });
                }
                else
                {
                    dislikeTxtView.setText(String.valueOf(Integer.parseInt(dislikeTxtView.getText().toString()) + 1));
                    Button likeBtn = ((Button)((LinearLayout) view.getParent()).getChildAt(0));
                    view.setSelected(true);
                    likeBtn.setSelected(false);
                    MainActivity.getLocalStorage().updateUserScore("comments", MainActivity.getUser(0).getCommentsScore() + 1, MainActivity.getUser(0));
                    if (MainActivity.getLocalStorage().updateDbLikes(commentId, MainActivity.getUser(0)))
                    {
                        commentChoiceChanged = true;
                        MainActivity.getLocalStorage().updateUserScore("comments", MainActivity.getUser(0).getCommentsScore() - 1, MainActivity.getUser(0));
                        likeTxtView.setText(String.valueOf(Integer.parseInt(likeTxtView.getText().toString()) - 1));
                    }

                    MainActivity.getLocalStorage().updateCommentsDislikes(MainActivity.getUser(0), commentId);
                    AsyncTask.execute(() -> {
                        MainActivity.getDbInstance().insertDislikeComment(commentId, Integer.parseInt(dislikeTxtView.getText().toString()), true, true);
                        if (commentChoiceChanged)
                        {
                            MainActivity.getDbInstance().insertLikeComment(commentId, Integer.parseInt(likeTxtView.getText().toString()), true, true);
                        }
                    });
                }
            }
        });

        toggleLikes(viewHolder);

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth * 0.13), (int) (dialogWidth * 0.08));
        lParams.setMargins(10,0,0,10);
        viewHolder.likeTxt.setLayoutParams(lParams);
        viewHolder.likeTxt.setText(String.valueOf(passedComment.getLikes()));
        viewHolder.likeTxt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        viewHolder.dislikeTxt.setLayoutParams(lParams);
        viewHolder.dislikeTxt.setText(String.valueOf(passedComment.getDislikes()));
        viewHolder.dislikeTxt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth * 0.08), LinearLayout.LayoutParams.WRAP_CONTENT);
        createLinearLayout(LinearLayout.VERTICAL, Color.TRANSPARENT, 0, View.GONE, viewHolder.objectButtonLayout);

        lParams = new LinearLayout.LayoutParams((int) (dialogWidth * 0.08), (int) (dialogWidth * 0.08));
        lParams.gravity = Gravity.TOP;
        viewHolder.editBtn.setBackground(getContext().getDrawable(R.drawable.edit));
        viewHolder.editBtn.setLayoutParams(lParams);
        viewHolder.editBtn.setOnClickListener(view ->  {
            commentId = listOfItems.get(currentItem).getComments().get(((LinearLayout)view.getParent().getParent()).getId()).getCommentId();
            editComment = new ShowEditCommentDialog(activity, currentComment) {
                @Override
                protected void sendClicked(String text, int chosenStars) {
                 //   ArrayList<Item> list = Util.getList(listName);
                    newComment = listOfItems.get(currentItem).new Comment(text, chosenStars, listOfItems.get(currentItem).getDate(), userName);
                    newComment.setCommentId(commentId);
                    task = Task.UPDATE_COMMENTS;
                    new ProgressTask().execute();
                    editComment.dismiss();

                }
            };
            editComment.show();
        });
        lParams = new LinearLayout.LayoutParams((int) (dialogWidth * 0.08), (int) (dialogWidth * 0.08));
        lParams.gravity = Gravity.BOTTOM;
        lParams.setMargins(0, 70, 0, 0);
        viewHolder.deleteBtn.setBackground(getContext().getDrawable(R.drawable.delete));
        viewHolder.deleteBtn.setLayoutParams(lParams);
        viewHolder.deleteBtn.setOnClickListener(view -> {
            int commentIndex = ((LinearLayout)view.getParent().getParent()).getId();
            commentId = listOfItems.get(currentItem).getComments().get(commentIndex).getCommentId();

            Util.handleDeleteComment(commentId, commentIndex, activity, getContext(), listOfItems.get(currentItem), tabNavigator, dialog);
        });
    }

       private void toggleLikes(RecyclerViewAdapter.MyViewHolder1 viewHolder) {
        if (MainActivity.getLocalStorage().isCommentLike(passedComment.getCommentId(), MainActivity.getUser(0)))
        {
            viewHolder.dislikeBtn.setSelected(true);
        }

        if (MainActivity.getLocalStorage().isCommentDislike(passedComment.getCommentId(), MainActivity.getUser(0)))
        {
            viewHolder.dislikeBtn.setSelected(true);
        }
    }

    private void createLinearLayout(int orientation, int color, int  border, int visible, LinearLayout layout)
    {
        layout.setOrientation(orientation);
        layout.setLayoutParams(lParams);
        layout.setGravity(Gravity.CENTER);
        layout.setVisibility(visible);
        layout.setBackground(Util.createBorder(20, color, false, null, border));
    }

    public void deleteConfirmed(int index)
    {
        listOfItems.get(currentItem).setNumOfComments(listOfItems.get(currentItem).getNumOfComments() -1);
        if (listOfItems.get(currentItem).getNumOfComments() < 10)
        {
            nowShowing--;
        }

        listOfItems.get(currentItem).getComments().remove(index);
        refreshRecyclerView();
        numOfCommentsTxt.setText("Displaying " + Math.min(nowShowing, listOfItems.get(currentItem).getNumOfComments()) + " comments out of " + listOfItems.get(currentItem).getNumOfComments());
    }

    public void showUserProfile(int index)
    {
        Intent intent = new Intent(getContext(), Account.class);
        intent.putExtra("USER_NAME", userName);
        intent.putExtra("USER_INDEX", index);
        getContext().startActivity(intent);
    }

    public void refreshRecyclerView()
    {
        recyclerView.getAdapter().notifyDataSetChanged();
    }
    private class ProgressTask extends AsyncTask <Void,Void,Void>{
        @Override
        protected void onPreExecute(){
            if (task == Task.FETCH_COMMENTS)
            {
                progressTxt.setText(R.string.loading_comments);
            }
            else
            {
                ((RecyclerViewAdapter)recyclerView.getAdapter()).setEnable(false);
                refreshRecyclerView();
                progressTxt.setText(R.string.loading_user_data);
            }
            progressBarLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            switch(task)
            {
                case UPDATE_COMMENTS:
                    MainActivity.getDbInstance().updateComments(listOfItems.get(currentItem).getItemId(), newComment.getCommentId(), newComment.getDateAdded(),
                            newComment.getText(), newComment.getStars(), true, false);
                    MainActivity.getDbInstance().selectCommentsData(listOfItems, currentItem, 0); break;
                case FETCH_COMMENTS:
                    MainActivity.getDbInstance().selectCommentsData(listOfItems, currentItem, nowShowing - MAX_ITEMS_SHOWS); break;
                default:
                    MainActivity.selectUserData(1);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressBarLayout.setVisibility(View.GONE);
            switch(task) {
                case UPDATE_COMMENTS:
                    ShowComments showItem = new ShowComments(activity, listName, currentItem, tabNavigator, backgroundColor, titleColor);
                    showItem.show();
                    break;
                case FETCH_COMMENTS:
                    numOfCommentsTxt.setText("Displaying " + Math.min(nowShowing, listOfItems.get(currentItem).getNumOfComments())
                            + " comments out of " + listOfItems.get(currentItem).getNumOfComments());
                    break;
                default:
                    ((RecyclerViewAdapter) recyclerView.getAdapter()).setEnable(true);
                    showUserProfile(1);
            }
            refreshRecyclerView();
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<Item.Comment>  list;
        private boolean enable = true;

        public class MyViewHolder1 extends RecyclerView.ViewHolder {
            LinearLayout objectButtonLayout;
            LinearLayout wholeObjectLayout;
            LinearLayout objectLayout;
            LinearLayout commentHeaderLayout;
            LinearLayout leftHeaderLayout;
            LinearLayout likesLayout;
            LinearLayout starLayout;
            TextView messageTxt;
            TextView TSTxt;
            TextView likeTxt;
            TextView dislikeTxt;
            View separator;
            View separator2;
            Button userBtn;
            Button likeBtn;
            Button dislikeBtn;
            Button star0Btn;
            Button star1Btn;
            Button star2Btn;
            Button star3Btn;
            Button star4Btn;
            Button editBtn;
            Button deleteBtn;

            public MyViewHolder1(View v) {
                super(v);
                objectLayout = v.findViewById(R.id.dialog_object_layout);
                wholeObjectLayout = v.findViewById(R.id.whole_object_layout);
                objectButtonLayout = v.findViewById(R.id.object_buttons_layout);
                starLayout = v.findViewById(R.id.star_layout);
                commentHeaderLayout = v.findViewById(R.id.comment_header_layout);
                leftHeaderLayout = v.findViewById(R.id.left_header_layout);
                likesLayout = v.findViewById(R.id.likes_layout);

                TSTxt = v.findViewById(R.id.ts_txt);
                likeTxt = v.findViewById(R.id.like_txt);
                dislikeTxt = v.findViewById(R.id.dislike_txt);
                messageTxt = v.findViewById(R.id.message_txt);
                separator = v.findViewById(R.id.separator);
                separator2 = v.findViewById(R.id.separator2);

                userBtn = v.findViewById(R.id.user_btn);
                likeBtn = v.findViewById(R.id.like_btn);
                dislikeBtn = v.findViewById(R.id.dislike_btn);
                star0Btn = v.findViewById(R.id.star0_btn);
                star1Btn = v.findViewById(R.id.star1_btn);
                star2Btn = v.findViewById(R.id.star2_btn);
                star3Btn = v.findViewById(R.id.star3_btn);
                star4Btn = v.findViewById(R.id.star4_btn);
                editBtn = v.findViewById(R.id.edit_btn);
                deleteBtn = v.findViewById(R.id.delete_btn);
            }
        }

        public class MyViewHolder2 extends RecyclerView.ViewHolder {
            LinearLayout trailerLayout;
            TextView trailerTxt;
            View trailerSeparator;

            public MyViewHolder2(View v) {
                super(v);
                trailerLayout = v.findViewById(R.id.show_comments_trailer_layout);
                trailerTxt = v.findViewById(R.id.trailer_txt);
                trailerSeparator = v.findViewById(R.id.trailer_separator);
            }
        }

        public RecyclerViewAdapter(ArrayList<Item.Comment> list) {
            this.list = list;
        }

        @Override
        public int getItemViewType(int position) {
            return position < listOfItems.get(currentItem).getNumOfComments()? 1: 2;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1)
            {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.dialog_show_item2, parent, false);
                return new MyViewHolder1(v);
            }
            else {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.dialog_show_item3, parent, false);
                return new MyViewHolder2(v);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MyViewHolder1)
            {
                createCommentLayout(position, list.get(position), holder, enable);
            }
            else
            {
                createTrailerLayout(holder);
            }
        }

        @Override
        public int getItemCount() {
            return list.size() ==  listOfItems.get(currentItem).getNumOfComments()? list.size()+1: list.size();
        }

        public void setEnable(boolean enable)
        {
            this.enable = enable;
        }
    }
}
