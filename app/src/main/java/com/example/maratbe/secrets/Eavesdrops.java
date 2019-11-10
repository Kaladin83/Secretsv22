package com.example.maratbe.secrets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Eavesdrops extends Fragment implements Constants, View.OnClickListener{
    private int filterBtnSize, currentObjectId, chosenTime = 0, starsChosen = 0,
            filterChosen = 0, emojiObjectNumber, commentObjectNumber, lastSecretTouched = 0, lastTagTouched = 0;
    private boolean commentPanelVisible, emojiPanelVisible, isEavsdrop = false, selectionChanged = false;
    private String commentsFor, listName;
    private String currentComment, selectedTagText, currentTimeStamp, selectedSort = "date", allSelectedSort;
    private GridViewAdapter gridAdapter;
    private MakeRecyclerView makeRecyclerView;
    private View fragment;
    private RelativeLayout mainLayout;
    private CoordinatorLayout allDataLayout, tagsLayout;
    private LinearLayout progressBarLayout, secretsFilterLayout, tagsFilterLayout, searchLayout;
    private LinearLayout.LayoutParams lParams;
    private Button selectedTag;
    private LinearLayoutManager layoutManager;
    private RecyclerView secretRecyclerView, tagItemsRecyclerView;
    private GridView tagsGridView;
    private Spinner spinner;
    private TextView progressBarTxt;
    private TabNavigator tabNavigator;
    private Item.Comment newComment;
    private ArrayList<Item> list;
    private Task task;
    private Theme theme;
    private ShowStars stars;
    public enum Task {
        FETCH_COMMENTS, FETCH_ITEMS, FETCH_TAG_ITEMS, FETCH_EX_TAG_ITEMS, ADD_COMMENT
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.activity_secret2, container, false);

        setMetrics();
        createAllLayouts();

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getView() != null)
        {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener((view, keyCode, keyEvent) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (tagItemsRecyclerView.getVisibility() == View.VISIBLE)
                    {
                        if (task.equals(Task.FETCH_TAG_ITEMS))
                        {
                            tagItemsRecyclerView.setVisibility(View.GONE);
                            tagsGridView.setVisibility(View.VISIBLE);
                            return true;
                        }
                        if (task.equals(Task.FETCH_EX_TAG_ITEMS) && isEavsdrop)
                        {
                            handleFilterPanel(theme.getDrawbles()[7], R.drawable.tags_gray, R.drawable.looking_glass_gray ,
                                    theme.getSelectedTitleColor()[2], theme.getFilterButtonColor(), theme.getFilterButtonColor(),
                                    View.VISIBLE, View.GONE, View.GONE, 0, R.string.secrets_filter_layout);
                            return true;
                        }
                    }
                }
                return false;
            });
        }
    }

    public void setTabNavigatorInstance(TabNavigator tabNavigator)
    {
        this.tabNavigator = tabNavigator;
    }

    public void showExternalTagItems(String selectedTagText)
    {
        handleFilterPanel(R.drawable.list_gray, theme.getDrawbles()[8], R.drawable.looking_glass_gray,
                theme.getFilterButtonColor(), theme.getSelectedTitleColor()[2], theme.getFilterButtonColor(), View.GONE, View.VISIBLE, View.GONE, 1, R.string.tags_filter_layout);
        tagItemsRecyclerView.setVisibility(View.VISIBLE);
        tagsGridView.setVisibility(View.GONE);
        this.selectedTagText = selectedTagText;
        task = Task.FETCH_EX_TAG_ITEMS;
        new ProgressTask().execute();
    }

    private void setMetrics() {
        filterBtnSize = MainActivity.getScreenWidth() / 5;
        mainLayout = fragment.findViewById(R.id.main_eavesdrop_layout);
    }

    private void createFilterLayout() {
        secretsFilterLayout = fragment.findViewById(R.id.secrets_filter_layout);
        secretsFilterLayout.setOrientation(LinearLayout.HORIZONTAL);
        secretsFilterLayout.setBackgroundColor(theme.getGradientColors()[2]);
        secretsFilterLayout.setGravity(Gravity.CENTER);

        tagsFilterLayout = fragment.findViewById(R.id.tags_filter_layout);
        tagsFilterLayout.setVisibility(View.GONE);
        tagsFilterLayout.setOrientation(LinearLayout.HORIZONTAL);
        tagsFilterLayout.setBackgroundColor(theme.getGradientColors()[2]);
        tagsFilterLayout.setGravity(Gravity.CENTER);

        createSortLayout(secretsFilterLayout);
        createToggleFilter(secretsFilterLayout);
        createSortLayout(tagsFilterLayout);
        createToggleFilter(tagsFilterLayout);
    }

    private void createSortLayout(LinearLayout filterLayout)
    {
        lParams = new LinearLayout.LayoutParams((int)(filterBtnSize*1.2), LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout sortLayout = createLinearLayout(LinearLayout.VERTICAL, theme.getGradientColors()[2], 0, View.VISIBLE, null);

        lParams = new LinearLayout.LayoutParams((int)(filterBtnSize*1.2), LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(0,0,0,10);
        TextView txtView = new TextView(getContext());
        txtView.setText(R.string.sort_by);
        txtView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtView.setLayoutParams(lParams);

        lParams = new LinearLayout.LayoutParams((int)(filterBtnSize*1.2), 90);
        lParams.setMargins(0,0,0,10);
        spinner = new Spinner(getContext());
        spinner.setLayoutParams(lParams);
        spinner.setBackground(Util.createBorder(15, theme.getGradientColors()[0], false, null, 1));
        List<String> list = new ArrayList<>();
        list.add("Date");
        list.add("Rating");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSort = adapterView.getItemAtPosition(i).toString().toLowerCase();
                if (chosenTime > 0)
                {
                    selectionChanged = true;
                }
                if (filterChosen == 0)
                {
                    lastSecretTouched = 0;
                    task = Task.FETCH_ITEMS;
                    allSelectedSort = selectedSort;
                    new ProgressTask().execute();
                }
                else
                {

                    if (tagItemsRecyclerView.getVisibility() == View.VISIBLE)
                    {
                    //    tagSelectedSort = selectedSort;
                        task = Task.FETCH_TAG_ITEMS;
                        new ProgressTask().execute();
                    }
                }
                chosenTime++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sortLayout.addView(txtView);
        sortLayout.addView(spinner);
        filterLayout.addView(sortLayout);
    }


    private void createToggleFilter(LinearLayout filterLayout) {
        createToggleButton(R.id.read_all_btn, "All", R.drawable.list_gray, filterLayout);
        createToggleButton(R.id.read_tags_btn, "Tags", R.drawable.tags_gray, filterLayout);
        createToggleButton(R.id.read_search_btn, "Search", R.drawable.looking_glass_gray, filterLayout);
    }

    private void createAllLayouts() {
        theme = TabNavigator.getThemeAt(MainActivity.getActiveThemeNumber());
        populateListView();
        createProgressBar();
        createFilterLayout();

        showAllData();
        showTags();
        showSearch();
        RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mainLayout.addView(searchLayout, rParams);
        mainLayout.addView(progressBarLayout, rParams);

        handleFilterPanel(theme.getDrawbles()[7], R.drawable.tags_gray, R.drawable.looking_glass_gray,
                theme.getSelectedTitleColor()[2], theme.getFilterButtonColor(), theme.getFilterButtonColor(), View.VISIBLE, View.GONE, View.GONE, 0, R.string.secrets_filter_layout);
    }

    private void createProgressBar() {
        lParams = new LinearLayout.LayoutParams(MainActivity.getScreenHeight() / 9, MainActivity.getScreenHeight() / 9);
        lParams.setMargins(0,0,100,0);
        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setLayoutParams(lParams);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(
                theme.getSelectedTitleColor()[2], android.graphics.PorterDuff.Mode.MULTIPLY);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        progressBarTxt = new TextView(getContext());
        progressBarTxt.setTextColor(Color.BLACK);
        progressBarTxt.setText(R.string.loading_secrets_data);
        progressBarTxt.setTextSize(15);
        progressBarTxt.setLayoutParams(lParams);
        progressBarTxt.setTypeface(theme.getTitleTypeface());
        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout = new LinearLayout(getContext());
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(Color.WHITE);
        layout.addView(progressBar);
        layout.addView(progressBarTxt);
        layout.setPadding(40,0,40,0);

        progressBarLayout = createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, View.GONE, lParams);
        progressBarLayout.addView(layout);
    }

    private void createToggleButton(int id, String text, int image, LinearLayout filterLayout) {
        Button btn;
        lParams = new LinearLayout.LayoutParams(filterBtnSize, filterBtnSize);
        lParams.setMargins(30, 0, 0, 0);
        LinearLayout toggleBtnLayout = createLinearLayout(LinearLayout.VERTICAL, Color.TRANSPARENT, 1, View.VISIBLE, lParams);
        toggleBtnLayout.setClickable(true);
        toggleBtnLayout.setId(id);

        lParams = new LinearLayout.LayoutParams((int) (filterBtnSize * 0.5), (int) (filterBtnSize * 0.47));
        lParams.setMargins(0, 10, 0, 0);
        btn = createButton(true, image, 0, "", 17, theme.getMainTypeface());
        btn.setLayoutParams(lParams);
        toggleBtnLayout.addView(btn);

        lParams = new LinearLayout.LayoutParams((int) (filterBtnSize * 0.8), (int) (filterBtnSize * 0.33));
        btn = createButton(false, -100, 0, text, 17, theme.getMainTypeface());
        btn.setBackgroundColor(Color.TRANSPARENT);
        btn.setLayoutParams(lParams);
        toggleBtnLayout.addView(btn);

        lParams = new LinearLayout.LayoutParams((int) (filterBtnSize * 0.8), 5);
        View view = new View(getContext());
        view.setLayoutParams(lParams);
        view.setBackgroundColor(Color.GRAY);
        toggleBtnLayout.addView(view);

        filterLayout.addView(toggleBtnLayout);
    }

    private void showAllData()
    {
        allDataLayout = fragment.findViewById(R.id.secrets_coordinator_layout);
        allDataLayout.setBackgroundColor(theme.getGradientColors()[2]);
        allDataLayout.setClickable(true);

        layoutManager = new LinearLayoutManager(getContext());
        secretRecyclerView = fragment.findViewById(R.id.secrets_recycler_view);
        secretRecyclerView.setAdapter(makeRecyclerView.getRecyclerViewAdapter());
        secretRecyclerView.setLayoutManager(layoutManager);
        secretRecyclerView.setClickable(true);
        secretRecyclerView.addOnScrollListener(new MyRecyclerScroll(layoutManager,  "Secrets", 0) {
            @Override
            public void onLoadMore()
            {
                task = Task.FETCH_ITEMS;
                new ProgressTask().execute();
                lastSecretTouched = MainActivity.getSecrets().size();
                layoutManager.scrollToPositionWithOffset(lastSecretTouched-1, 0);
            }
        });
        showSecrets();
    }

    private void showTags()
    {
        MainActivity.getTags().clear();
        MainActivity.getDbInstance().selectTags(true, true);

        tagItemsRecyclerView = fragment.findViewById(R.id.tags_recycler_view);
        tagItemsRecyclerView.setVisibility(View.GONE);
        tagItemsRecyclerView.addOnScrollListener(new MyRecyclerScroll(layoutManager, "ItemsByTag", 0) {
            @Override
            public void onLoadMore() {
                task = Task.FETCH_ITEMS;
                new ProgressTask().execute();
                lastTagTouched = MainActivity.getSecrets().size();
                layoutManager.scrollToPositionWithOffset(lastTagTouched-1, 0);
            }
        });
        tagsLayout = fragment.findViewById(R.id.tags_coordinator_layout);
        tagsLayout.setBackgroundColor(theme.getGradientColors()[2]);
        gridAdapter = new GridViewAdapter(getContext());

        tagsGridView = fragment.findViewById(R.id.tags_grid_view);
        tagsGridView.setNumColumns(2);
        tagsGridView.setVerticalSpacing(30);
        tagsGridView.setGravity(Gravity.CENTER);
        tagsGridView.setAdapter(gridAdapter);
    }

    private void showSearch()
    {
        searchLayout = createLinearLayout(LinearLayout.VERTICAL, theme.getGradientColors()[2], 0,  View.GONE, null);
    }

    public void showSecrets()
    {
        makeRecyclerView.setThemeParameters(theme.getGradientColors()[0], theme.getTitleTypeface(), theme.getMainTypeface(), theme.getDrawbles(),
                MainActivity.getSecrets(), selectedSort.equals("rating"), false);
        if(selectionChanged) {
            secretRecyclerView.setAdapter(makeRecyclerView.getRecyclerViewAdapter());
            secretRecyclerView.setLayoutManager(layoutManager);
            selectionChanged = false;
        }
        else
        {
            secretRecyclerView.getAdapter().notifyDataSetChanged();
        }

        secretRecyclerView.setVisibility(View.VISIBLE);
    }

    private void populateListView() {
        makeRecyclerView = new MakeRecyclerView(getContext(), getActivity()) {
            @Override
            protected void updateFavoritesPanel(CardView ticket, Button button, int itemId) {

            }

            @Override
            public void addComment(View view) {
                LinearLayout object = (LinearLayout) (view.getParent().getParent());
                currentObjectId = object.getId();

                EditText addedComment = (EditText) ((LinearLayout) object.getChildAt(EDIT_LAYOUT)).getChildAt(SEND_EDIT);
                currentComment = addedComment.getText().toString();

                TextView txt = (TextView) ((LinearLayout) object.getChildAt(STATUS_LAYOUT)).getChildAt(COMMENTS_NUM_BTN);
                txt.setText(String.valueOf(Integer.parseInt(txt.getText().toString()) + 1));

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
                            String listName;
                            listName = tagItemsRecyclerView.getVisibility() == View.VISIBLE? "ItemByTag": "Secrets";
                            list = Util.getList(listName);
                            list.get(currentObjectId).setNumOfComments(list.get(currentObjectId).getNumOfComments()+1);
                            newComment = list.get(currentObjectId).new Comment(currentComment, starsChosen, currentTimeStamp,  MainActivity.getUser(0).getUserName());
                            task = Task.ADD_COMMENT;
                            new ProgressTask().execute();
                            stars.dismiss();
                        }
                    };
                    stars.show();
                }
            }

            @Override
            protected void showComments(int index) {
                if (tagItemsRecyclerView.getVisibility() == View.VISIBLE)
                {
                    list = MainActivity.getItemsByTag();
                    commentsFor = "tags";
                }
                else
                {
                    list = MainActivity.getSecrets();
                    commentsFor = "all";
                }
                currentObjectId = index;
                task = Task.FETCH_COMMENTS;
                new ProgressTask().execute();
            }

            @Override
            protected void tagClicked(String tagChosen)
            {
                isEavsdrop = true;
                showExternalTagItems(tagChosen);
            }

            @Override
            protected void showEmojis(View view) {
                int id = ((LinearLayout) view.getParent().getParent()).getId();
                ArrayList<Item> list;
                if (tagItemsRecyclerView.getVisibility() == View.VISIBLE)
                {
                    list = MainActivity.getItemsByTag();
                }
                else
                {
                    list = MainActivity.getSecrets();
                }
                ShowEmojies showEmojies = new ShowEmojies(getActivity(), list.get(id), theme) {
                    @Override
                    protected void removeVote() {
                        Util.removeUserVote(Util.checkVote(list, list.get(currentObjectId).getVotes(), MainActivity.getUser(0).getUserName()));
                        LinearLayout object = (LinearLayout) (view.getParent().getParent());
                        LinearLayout selectedEmojisPanel = (LinearLayout) ((LinearLayout) object.getChildAt(STATUS_LAYOUT)).getChildAt(SELECTED_EMOJI_LAYOUT);
                        TextView emojiText = (TextView)selectedEmojisPanel.getChildAt(NUM_OF_EMOJIS_TXT);
                        emojiText.setText(String.valueOf(Integer.parseInt(emojiText.getText().toString()) - 1));
                       // MainActivity.getLocalStorage().removeItem(list.get(currentObjectId).getItemId(), MainActivity.getUser(0), "votes");
                        MainActivity.getLocalStorage().updateDbVotes(list.get(currentObjectId).getItemId(), MainActivity.getUser(0));
                        tabNavigator.recreateListView(0);
                        tabNavigator.recreateListView(1);
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
            protected void handleEmojiPanel(View view) {
                LinearLayout emojiPanel;

                LinearLayout object = (LinearLayout) (view.getParent()).getParent();
                currentObjectId = object.getId();

                final ArrayList<Item> list;
                if (tagItemsRecyclerView.getVisibility() == View.VISIBLE)
                {
                    list = MainActivity.getItemsByTag();
                }
                else
                {
                    list = MainActivity.getSecrets();
                }
                AsyncTask.execute(() -> MainActivity.getDbInstance().selectCommentsData(list, currentObjectId, 0));

                emojiPanel = (LinearLayout) ((LinearLayout) object.getChildAt(ACTION_LAYOUT)).getChildAt(CHOICE_EMOJI_LAYOUT);

                if (emojiObjectNumber == currentObjectId && emojiPanelVisible) {
                    emojiPanel.setVisibility(View.GONE);
                    emojiPanelVisible = false;
                } else {
                    emojiPanel.setVisibility(View.VISIBLE);
                    emojiObjectNumber = currentObjectId;
                    emojiPanelVisible = true;
                }
            }

            @Override
            protected void handleAddCommentPanel(View view) {
                LinearLayout commentPanel;

                LinearLayout object = (LinearLayout) (view.getParent().getParent()).getParent();
                currentObjectId = object.getId();
                commentPanel = (LinearLayout) object.getChildAt(EDIT_LAYOUT);
                LinearLayout emojiPanel = (LinearLayout) view.getParent();

                if (commentObjectNumber == currentObjectId && commentPanelVisible) {
                    commentPanel.setVisibility(View.GONE);
                    Util.closeKeyboard((EditText) commentPanel.getChildAt(SEND_EDIT), getActivity());
                    emojiPanel.setVisibility(View.GONE);
                    commentPanelVisible = false;
                    emojiPanelVisible = false;
                } else {
                    commentPanel.setVisibility(View.VISIBLE);
                    (commentPanel.getChildAt(SEND_EDIT)).requestFocus();
                    Util.openKeyboard(getActivity());
                    commentObjectNumber = currentObjectId;
                    commentPanelVisible = true;
                }
            }

            @Override
            protected void updateEmojiPanel(int emoji, View view) {
                LinearLayout object = (LinearLayout) (view.getParent().getParent()).getParent();
                LinearLayout emojiPanel = (LinearLayout) ((LinearLayout) object.getChildAt(ACTION_LAYOUT)).getChildAt(CHOICE_EMOJI_LAYOUT);
                LinearLayout selectedEmojisPanel = (LinearLayout) ((LinearLayout) object.getChildAt(STATUS_LAYOUT)).getChildAt(SELECTED_EMOJI_LAYOUT);
                TextView emojiText = (TextView)selectedEmojisPanel.getChildAt(NUM_OF_EMOJIS_TXT);
                currentObjectId = object.getId();
                Util.updateVotes(emoji, MainActivity.getSecrets(), currentObjectId, selectedEmojisPanel, emojiText, getActivity(), tabNavigator, null, null);
                emojiPanel.setVisibility(View.GONE);
                emojiPanelVisible = false;
            }
        };
        makeRecyclerView.setMetrics(MainActivity.getScreenWidth(),
                MainActivity.getScreenWidth() / 16, MainActivity.getScreenWidth() / 12);
    }

    private LinearLayout createLinearLayout(int orientation, int color, int  border, int visible, LinearLayout.LayoutParams params)
    {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(orientation);
        layout.setGravity(Gravity.CENTER);
        layout.setVisibility(visible);
        layout.setBackground(Util.createBorder(20, color, false, null, border));
        if (params != null)
        {
            layout.setLayoutParams(params);
        }
        return layout;
    }

    private Button createButton(boolean isImage, int image, int id, String text, int textSize, Typeface typeface)
    {
        Button btn = new Button(getContext());
        btn.setTextColor(Color.GRAY);
        btn.setAllCaps(false);
        btn.setId(id);
        btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        if (isImage)
        {
            btn.setBackgroundResource(image);
        }
        else
        {
            btn.setText(text);
            btn.setPadding(0,0,0,0);
            btn.setBackground(Util.createBorder(1, Color.TRANSPARENT, false, null, 0));
        }

        btn.setTextSize(textSize);
        btn.setTypeface(typeface);
        btn.setOnClickListener(this);
        btn.setLayoutParams(lParams);
        lParams.gravity = Gravity.CENTER;

        return btn;
    }

    private void handleTagsPanel(boolean isTagItemsList)
    {
        if (isTagItemsList)
        {
            ArrayList<Item> list = MainActivity.getItemsByTag();
            if (list.size() > 0) {
                makeRecyclerView.setThemeParameters(theme.getGradientColors()[0], theme.getTitleTypeface(), theme.getMainTypeface(), theme.getDrawbles(),
                        MainActivity.getItemsByTag(), selectedSort.equals("rating"), false);
                makeRecyclerView.setSelectedTagColor(theme.getActiveBarColor(), selectedTagText);
                tagItemsRecyclerView.setAdapter(makeRecyclerView.getRecyclerViewAdapter());
                tagItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                tagItemsRecyclerView.setVisibility(View.VISIBLE);
                tagsGridView.setVisibility(View.GONE);
            }
        }
        else
        {
            tagItemsRecyclerView.setVisibility(View.GONE);
            tagsGridView.setVisibility(View.VISIBLE);
        }
    }

    public void handleFilterPanel(int drawable0,  int drawable1, int drawable2, int color0, int color1, int color2, int visible0,
                                  int visible1, int visible2, int filterChosen, int filterLayoutName)
    {
        LinearLayout filterLayout ;
        if (filterLayoutName == R.string.secrets_filter_layout)
        {
            filterLayout = secretsFilterLayout;
            secretsFilterLayout.setVisibility(View.VISIBLE);
            tagsFilterLayout.setVisibility(View.GONE);
        }
        else
        {
            filterLayout = tagsFilterLayout;
            secretsFilterLayout.setVisibility(View.GONE);
            tagsFilterLayout.setVisibility(View.VISIBLE);
        }
        filterLayout.setVisibility(View.VISIBLE);
        this.filterChosen = filterChosen;
        (((LinearLayout) filterLayout.getChildAt(1)).getChildAt(0)).setBackground(ContextCompat.getDrawable(getContext(), drawable0));
        ((Button)((LinearLayout) filterLayout.getChildAt(1)).getChildAt(1)).setTextColor(color0);
        (((LinearLayout) filterLayout.getChildAt(1)).getChildAt(2)).setBackgroundColor(color0);
        (((LinearLayout) filterLayout.getChildAt(2)).getChildAt(0)).setBackground(ContextCompat.getDrawable(getContext(), drawable1));
        ((Button)((LinearLayout) filterLayout.getChildAt(2)).getChildAt(1)).setTextColor(color1);
        (((LinearLayout) filterLayout.getChildAt(2)).getChildAt(2)).setBackgroundColor(color1);
        (((LinearLayout) filterLayout.getChildAt(3)).getChildAt(0)).setBackground(ContextCompat.getDrawable(getContext(), drawable2));
        ((Button)((LinearLayout) filterLayout.getChildAt(3)).getChildAt(1)).setTextColor(color2);
        (((LinearLayout) filterLayout.getChildAt(3)).getChildAt(2)).setBackgroundColor(color2);

        switchScrolls();
        if (isEavsdrop)
        {
            revertTagsResult(false);
        }

        allDataLayout.setVisibility(visible0);
        tagsLayout.setVisibility(visible1);
        searchLayout.setVisibility(visible2);
    }

    public void revertTagsResult(boolean isExternal)
    {
        makeRecyclerView.setSelectedTagColor(Color.WHITE, selectedTagText);
        tagsGridView.setVisibility(View.VISIBLE);
        tagItemsRecyclerView.setVisibility(View.GONE);
        if (allSelectedSort.equals(selectedSort))
        {
            showSecrets();
        }
        else
        {
            if (allSelectedSort.equals("date"))
            {
                spinner.setSelection(0);
            }
            else
            {
                spinner.setSelection(1);
            }
        }
        isEavsdrop = false;
        if (isExternal)
        {
            handleFilterPanel(theme.getDrawbles()[7], R.drawable.tags_gray, R.drawable.looking_glass_gray ,
                    theme.getSelectedTitleColor()[2], theme.getFilterButtonColor(), theme.getFilterButtonColor(), View.VISIBLE, View.GONE, View.GONE, 0, R.string.secrets_filter_layout);
        }
    }

    private void switchScrolls()
    {
        if (filterChosen == 0)
        {
            showSecrets();
        }
        else
        {
            gridAdapter = new GridViewAdapter(getContext());
            tagsGridView.setAdapter(gridAdapter);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (((LinearLayout)view.getParent()).getId())
        {
            case R.id.read_all_btn:
                isEavsdrop = true;
                handleFilterPanel(theme.getDrawbles()[7], R.drawable.tags_gray, R.drawable.looking_glass_gray ,
                        theme.getSelectedTitleColor()[2], theme.getFilterButtonColor(), theme.getFilterButtonColor(), View.VISIBLE, View.GONE, View.GONE, 0, R.string.secrets_filter_layout); break;
            case R.id.read_tags_btn:
                handleFilterPanel(R.drawable.list_gray, theme.getDrawbles()[8], R.drawable.looking_glass_gray,
                        theme.getFilterButtonColor(), theme.getSelectedTitleColor()[2], theme.getFilterButtonColor(), View.GONE, View.VISIBLE, View.GONE, 1, R.string.tags_filter_layout); break;
            /*case R.id.read_search_btn:
                handleFilterPanel(R.drawable.list_gray, R.drawable.tags_gray, theme.getDrawbles()[9],
                        theme.getFilterButtonColor(), theme.getFilterButtonColor(), theme.getSelectedTitleColor()[2], View.GONE, View.GONE, View.VISIBLE, 2); break;*/
        }
    }

    private class ProgressTask extends AsyncTask <Void,Void,Void>{
        @Override
        protected void onPreExecute(){
            switch(task)
            {
                case FETCH_TAG_ITEMS:
                    if (tagsGridView.getVisibility() == View.VISIBLE)
                    {
                        progressBarTxt.setText(getString(R.string.loading_tag_data, selectedTagText));
                        selectedTag.setBackground(Util.createBorder(20, theme.getSelectedTitleColor()[2], false, null, 1));
                    }
                    else
                    {
                        progressBarTxt.setText(R.string.loading_secrets_data);
                    }
                    progressBarLayout.setVisibility(View.VISIBLE);break;
                case FETCH_COMMENTS:
                    progressBarTxt.setText(R.string.loading_comments); break;
                default:
                    progressBarLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            switch (task)
            {
                case FETCH_COMMENTS:
                    MainActivity.getDbInstance().selectCommentsData(list, currentObjectId, 0); break;
                case FETCH_ITEMS:
                    MainActivity.getDbInstance().selectAllSecretsData(lastSecretTouched, selectedSort, true, true); break;
                case FETCH_TAG_ITEMS:
                case FETCH_EX_TAG_ITEMS:
                    MainActivity.getDbInstance().selectItemsByTagData(selectedTagText, selectedSort, lastTagTouched); break;
                case ADD_COMMENT:
                    MainActivity.getDbInstance().insertComment(list.get(currentObjectId).getItemId(), newComment, list.get(currentObjectId).getNumOfComments(), 0);
                    MainActivity.getDbInstance().selectCommentsData(list, currentObjectId, 0);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressBarLayout.setVisibility(View.GONE);
            switch (task) {
                case FETCH_COMMENTS:
                    listName = commentsFor.equals("tags")? "ItemsByTag": "Secrets";
                    ShowComments item = new ShowComments(getActivity(), listName, currentObjectId,
                            tabNavigator, theme.getGradientColors()[2], theme.getSelectedTitleColor()[2]);
                    item.show(); break;
                case FETCH_ITEMS:
                    showSecrets(); break;
                case FETCH_TAG_ITEMS:
                    handleTagsPanel(true);
                    if (tagsGridView.getVisibility() == View.VISIBLE)
                    {
                        selectedTag.setTextColor(Color.WHITE);
                        selectedTag.setBackground(Util.createBorder(getContext(), 20, ContextCompat.getColor(getContext(), R.color.light_gray), false, 1));
                    }break;
                case FETCH_EX_TAG_ITEMS:
                    handleTagsPanel(true);break;
                case ADD_COMMENT:
                    ShowComments showItem = new ShowComments(getActivity(), listName, currentObjectId, tabNavigator,
                            theme.getGradientColors()[2], theme.getSelectedTitleColor()[2]);
                    showItem.show();
            }
        }
    }

    public class GridViewAdapter extends BaseAdapter {
        Context mContext;

        public GridViewAdapter( Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount()
        {
            return MainActivity.getTags().size();
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
            button.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.getScreenWidth()/3, 80));
            button.setTextSize(BUTTON_TEXT_SIZE+1);
            button.setAllCaps(false);
            button.setTypeface(theme.getTitleTypeface());
            button.setBackground(Util.createBorder(20, Color.BLACK, true,  theme.getGradientColors(), 1));
            button.setText(MainActivity.getTags().get(position));
            button.setPadding(15,10,15,10);
            button.setId(position);
            button.setOnClickListener(view ->
            {
                MainActivity.getItemsByTag().clear();
                selectedTag = (Button) view;
                selectedTagText = selectedTag.getText().toString();
                task = Task.FETCH_TAG_ITEMS;
                new ProgressTask().execute();
            });
            return button;
        }
    }
}
