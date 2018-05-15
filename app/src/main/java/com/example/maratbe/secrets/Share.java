package com.example.maratbe.secrets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Share extends Fragment implements Constants, View.OnClickListener
{
    private int buttonWidth, buttonHeight;
    private LinearLayout.LayoutParams lparams;
    private LinearLayout mainLayout, selectedTagsLayout ;
    private Theme theme;
    private GridViewAdapter gridAdapter;
    private EditText itemTitleEdit, itemBodyEdit;
    private ShowAlertDialog alertDialog;
    private ShowAddTagDialog addTagDialog;
    private TabNavigator tabNavigator;
    private GridView grid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View share = inflater.inflate(R.layout.activity_share, container, false);

        setMetrics();
        mainLayout = share.findViewById(R.id.share_main_layout);
        mainLayout.setOnTouchListener((view, motionEvent) -> {
            Util.closeKeyboard(itemTitleEdit, getActivity());
            return false;
        });
        theme = TabNavigator.getThemeAt(MainActivity.getActiveThemeNumber());

        createTitle();
        createEditLayout();
        createGridView();
        createButton();

        return share;
    }

    private void setMetrics() {
        buttonWidth = (int) (MainActivity.getScreenWidth()*0.40);
        buttonHeight = MainActivity.getScreenHeight()/ 18;
    }

    public void setTabNavigatorInstance(TabNavigator tabNavigator)
    {
        this.tabNavigator = tabNavigator;
    }

    private void createTitle() {
        lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(0,50,0,80);
        lparams.gravity = Gravity.CENTER_HORIZONTAL;
        TextView title = createTextView(R.string.share_title, 22, theme.getSelectedTitleColor()[2]);

        mainLayout.addView(title);
    }

    private void createButton() {
        lparams = new LinearLayout.LayoutParams(buttonWidth, buttonHeight);
        Button sendButton = createButton(R.id.share_save_btn, "Save", BUTTON_TEXT_SIZE+1, theme.getTitleTypeface());
        sendButton.setTextColor(theme.getSelectedTitleColor()[2]);
        sendButton.setBackground(Util.createBorder(getContext(), 10, Color.WHITE, false, 1));
        mainLayout.addView(sendButton);
    }

    private void createGridView() {
        lparams = new LinearLayout.LayoutParams((int)(MainActivity.getScreenWidth()*0.95), (int)(MainActivity.getScreenHeight()*0.07));
        lparams.setMargins(0,0,0,10);
        selectedTagsLayout = createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, 0, View.VISIBLE);
        selectedTagsLayout.setOnTouchListener((view, motionEvent) -> {
            Util.closeKeyboard(itemTitleEdit, getActivity());
            return false;
        });
        gridAdapter = new GridViewAdapter(getContext());

        lparams = new LinearLayout.LayoutParams((int)(MainActivity.getScreenWidth()*0.96), (int)(MainActivity.getScreenHeight()*0.2));
        lparams.setMargins(0,40,0,40);
        grid = new GridView(getContext());
        grid.setNumColumns(3);
        grid.setVerticalSpacing(30);
        grid.setGravity(Gravity.CENTER);
        grid.setAdapter(gridAdapter);
        grid.setLayoutParams(lparams);
        grid.setPadding(7,7,7,7);
        grid.setBackground(Util.createBorder(0, Color.WHITE, false,  null, 1));

        mainLayout.addView(grid);
        mainLayout.addView(selectedTagsLayout);
    }

    private void createEditLayout() {
        lparams = new LinearLayout.LayoutParams((int)(MainActivity.getScreenWidth()*0.9), (int)(MainActivity.getScreenHeight()*0.32));
        LinearLayout editLayout = createLinearLayout(LinearLayout.VERTICAL, Color.TRANSPARENT, 1, 15,View.VISIBLE);

        lparams = new LinearLayout.LayoutParams((int)(MainActivity.getScreenWidth()*0.87), (int)(MainActivity.getScreenHeight()*0.08));
        lparams.setMargins(0,20,0,10);
        LinearLayout headerLayout = createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, 0, View.VISIBLE);

        lparams = new LinearLayout.LayoutParams((int)(MainActivity.getScreenWidth()*0.46), (int)(MainActivity.getScreenHeight()*0.07));
        lparams.setMargins(10,0,10,0);
        itemTitleEdit = createEditView(TEXT_SIZE-3, 1, 10);
        itemTitleEdit.setGravity(Gravity.CENTER_VERTICAL);

        lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int)(MainActivity.getScreenHeight()*0.05));
        lparams.gravity = Gravity.BOTTOM;
        TextView itemTitleTxt = createTextView(R.string.title_option, BUTTON_TEXT_SIZE+1, ContextCompat.getColor(getContext(), R.color.light_gray));
        itemTitleTxt.setOnTouchListener((view, motionEvent) -> {
            Util.closeKeyboard(itemTitleEdit, getActivity());
            return false;
        });

        lparams = new LinearLayout.LayoutParams((int)(MainActivity.getScreenWidth()*0.85), 1);
        lparams.gravity = Gravity.CENTER_HORIZONTAL;
        lparams.setMargins(0,0,0,20);
        View separator = new View(getContext());
        separator.setBackgroundColor(Color.BLACK);
        separator.setLayoutParams(lparams);

        lparams = new LinearLayout.LayoutParams((int)(MainActivity.getScreenWidth()*0.85), (int)(MainActivity.getScreenHeight()*0.19));
        lparams.setMargins(10,10,10,0);
        itemBodyEdit = createEditView(TEXT_SIZE-3, 1, 10);
        itemBodyEdit.setGravity(Gravity.TOP);
        itemBodyEdit.setClickable(true);

        headerLayout.addView(itemTitleEdit);
        headerLayout.addView(itemTitleTxt);
        editLayout.addView(headerLayout);
        editLayout.addView(separator);
        editLayout.addView(itemBodyEdit);

        mainLayout.addView(editLayout);
    }

    private void saveItem() {
        if (selectedTagsLayout.getChildCount() > 0 && !itemBodyEdit.getText().toString().equals(""))
        {
            alertDialog = new ShowAlertDialog(getActivity()) {
                @Override
                protected void buttonPressed(int id) {
                    if (id == 0)
                    {
                        final String[] tags = new String [selectedTagsLayout.getChildCount()];
                        for (int i = 0; i< selectedTagsLayout.getChildCount(); i++)
                        {
                            tags[i] = ((Button)selectedTagsLayout.getChildAt(i)).getText().toString();
                        }
                        final String text = itemTitleEdit.getText().toString() + "^|" + itemBodyEdit.getText().toString();
                        AsyncTask.execute(() -> {
                            MainActivity.getDbInstance().insertItem(MainActivity.getUser(0).getUserName(), text, 0, tags, true, false);
                            MainActivity.getDbInstance().selectTags(false, false);
                            MainActivity.getDbInstance().selectAllSecretsData(0, "date", false, true);
                        });

                        selectedTagsLayout.removeAllViews();
                        itemBodyEdit.setText("");
                        itemTitleEdit.setText("");

                        tabNavigator.recreateListView(0);
                        Toast.makeText(getContext(), "Your secret was saved. \nNow everybody will see it.", Toast.LENGTH_LONG).show();
                    }
                    alertDialog.dismiss();
                }
            };

            alertDialog.setTexts(getString(R.string.insert_item_title), getString(R.string.insert_secret_body), new int[] {R.string.ok, R.string.cancel});
            Util.setDialogColors(getContext(), alertDialog);
            alertDialog.show();
        }
        else
        {
            if (selectedTagsLayout.getChildCount() == 0)
            {
                Util.closeKeyboard(itemBodyEdit, getActivity());
                Toast.makeText(getContext(), "You should select at least 1 tag", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getContext(), "The message body is mandatory", Toast.LENGTH_LONG).show();
            }
        }
    }


    private LinearLayout createLinearLayout(int orientation, int color, int  border, int radius,int visible)
    {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(orientation);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setVisibility(visible);
        layout.setBackground(Util.createBorder(radius, color, false, null, border));
        layout.setLayoutParams(lparams);
        return layout;
    }

    private TextView createTextView(int text, int size, int color)
    {
        TextView txt = new TextView(getContext());
        txt.setTextColor(color);
        txt.setText(text);
        txt.setTextSize(size);
        txt.setLayoutParams(lparams);
        return txt;
    }

    private EditText createEditView(int size, int border, int radius)
    {
        EditText edit = new EditText(getContext());
        edit.setText("");
        edit.setTextSize(size);
        edit.setTextColor(ContextCompat.getColor(getContext(),R.color.light_black));
        edit.setBackground(Util.createBorder(radius, Color.WHITE, false, null, border));
        edit.setLayoutParams(lparams);
        edit.setPadding(10,10,10,10);
        return edit;
    }

    private Button createButton(int id, String text, int textSize, Typeface typeface)
    {
        Button btn = new Button(getContext());
        btn.setTextColor(Color.GRAY);
        btn.setAllCaps(false);
        btn.setId(id);
        btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btn.setText(text);
        btn.setPadding(0,0,0,0);
        btn.setBackground(Util.createBorder(1, Color.TRANSPARENT, false, null, 0));
        btn.setTextSize(textSize);
        btn.setTypeface(typeface);
        btn.setOnClickListener(this);
        btn.setLayoutParams(lparams);

        return btn;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_save_btn:
                saveItem();
        }

    }

    private boolean repeated(String str) {
        for (int i = 0; i < selectedTagsLayout.getChildCount(); i++)
        {
            Button btn = ((Button)selectedTagsLayout.getChildAt(i));
            if (str.equals(btn.getText().toString()))
            {
                return true;
            }
        }
        return false;

    }

    public class GridViewAdapter extends BaseAdapter {
        Context mContext;

        public GridViewAdapter( Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount()
        {
            return MainActivity.getTags().size() + 1;
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
            if (position ==  MainActivity.getTags().size())
            {
                button.setLayoutParams(new LinearLayout.LayoutParams(75, 75));
                button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.plus_tag));
                button.setId(R.id.share_add_tag_btn);
            }
            else
            {
                button.setLayoutParams(new LinearLayout.LayoutParams((int)(MainActivity.getScreenWidth()/3.3), 75));
                button.setTextSize(BUTTON_TEXT_SIZE-1);
                button.setAllCaps(false);
                button.setTypeface(theme.getTitleTypeface());
                button.setBackground(Util.createBorder(10, Color.BLACK, true,  theme.getGradientColors(), 1));
                button.setText(MainActivity.getTags().get(position));
                button.setId(position);
            }
            button.setPadding(0,0,0,0);
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(selectedTagsLayout.getChildCount() < 3) {
                        if (view.getId() == R.id.share_add_tag_btn) {
                            addTagDialog = new ShowAddTagDialog(getActivity(), theme) {
                                @Override
                                protected void addTagClicked(String tagName) {
                                    Toast.makeText(getContext(), "Note: The tag may be removed or modified by the admins",
                                            Toast.LENGTH_LONG).show();
                                    if (!tagName.equals("")) {
                                        final String newTagName = tagName;
                                        createTagButton(tagName);
                                        AsyncTask.execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                MainActivity.getDbInstance().insertNewTag(newTagName);
                                            }
                                        });

                                        addTagDialog.dismiss();
                                    } else {
                                        Toast.makeText(getContext(), "The text is mandatory", Toast.LENGTH_LONG).show();
                                    }

                                }

                                @Override
                                protected void cancelClicked() {
                                    addTagDialog.dismiss();
                                }
                            };
                            addTagDialog.setCanceledOnTouchOutside(false);
                            addTagDialog.show();
                        } else if (repeated(((Button) view).getText().toString())) {
                            Toast.makeText(getContext(), "'" + ((Button) view).getText().toString() + "' already chosen", Toast.LENGTH_SHORT).show();
                        } else {
                            createTagButton(((Button) view).getText().toString());
                        }
                    }
                }

                private void createTagButton(String str) {
                    lparams = new LinearLayout.LayoutParams((int)(MainActivity.getScreenWidth()/3.3), 75);
                    lparams.setMargins(10,0,0,0);
                    Button btn = createButton(0-(selectedTagsLayout.getChildCount()+1), str, BUTTON_TEXT_SIZE-1, theme.getTitleTypeface());
                    btn.setLayoutParams(lparams);
                    btn.setBackground(Util.createBorder(10, Color.WHITE, false, null, 1));
                    btn.setElevation(2);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectedTagsLayout.removeView(view);
                        }
                    });
                    selectedTagsLayout.addView(btn);
                }
            });
            return button;
        }
    }
}
