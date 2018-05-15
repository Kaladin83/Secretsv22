package com.example.maratbe.secrets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public abstract class MakeRecyclerView implements Constants{
    private int mainWidth, statusButtonSize, actionButtonSize;
    private int stripColor, selectedTagColor;
    private RecyclerViewAdapter adapter;
    private boolean isRating = false, isNotes = false;
    private String tagToColor;
    private ArrayList<Item> list;
    private LinearLayout.LayoutParams lParams;
    private Context context;
    private Activity activity;
    private Typeface titleTf, mainTf;
    private int[] drawables = new int[11];

    public MakeRecyclerView(Context context, Activity activity)
    {
        this.context = context;
        this.activity = activity;
    }

    public void setMetrics(int mainWidth, int statusButtonSize, int actionButtonSize)
    {
        this.mainWidth = mainWidth;
        this.statusButtonSize = statusButtonSize;
        this.actionButtonSize = actionButtonSize;
    }

    public RecyclerViewAdapter getRecyclerViewAdapter()
    {
        adapter = new RecyclerViewAdapter();
        return adapter;
    }

    public void setThemeParameters(int stripColor, Typeface titleTF, Typeface mainTf, int[] drawables, ArrayList<Item> list, boolean isRating, boolean isNotes)
    {
        this.stripColor = stripColor;
        this.titleTf = titleTF;
        this.mainTf = mainTf;
        this.list = list;
        this.isRating = isRating;
        this.isNotes = isNotes;

        System.arraycopy(drawables, 0, this.drawables, 0, drawables.length);
    }

    public void setSelectedTagColor(int color, String tagToColor)
    {
        selectedTagColor = color;
        this.tagToColor = tagToColor;
    }

    private void setTagsLayout(int i, RecyclerViewAdapter.MyViewHolder holder)
    {
        String txt;

        holder.tag1Btn.setVisibility(View.GONE);
        holder.tag2Btn.setVisibility(View.GONE);
        lParams = new LinearLayout.LayoutParams((int)(mainWidth*0.97), 80);
        lParams.setMargins(0,20,0,20);
        holder.tagLayout.setBackground(Util.createBorder(10, stripColor, false, null, 1));
        holder.tagLayout.setPadding(20,0,20,0);
        holder.tagLayout.setLayoutParams(lParams);
        if (list.size() > 0)
        {
            for (int j = 0; j < 3; j++)
            {
                if (list.get(i).getArrayOfTags()[j]!= null && list.get(i).getArrayOfTags().length > 0)
                {
                    if (!list.get(i).getArrayOfTags()[j].equals(" "))
                    {
                        txt = list.get(i).getArrayOfTags()[j];
                        switch(j)
                        {
                            case 0:
                                createTagButton(holder.tag0Btn, txt); break;
                            case 1:
                                createTagButton(holder.tag1Btn, txt); break;
                            default:
                                createTagButton(holder.tag2Btn, txt); break;
                        }
                    }
                }
            }
        }
    }

    private void createTagButton(Button btn, String str)
    {
        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 65);
        lParams.setMargins(10,0,0,0);

        btn.setVisibility(View.VISIBLE);
        btn.setText(str);
        btn.setLayoutParams(lParams);
        btn.setPadding(5,0,5,0);
        btn.setTextSize(BUTTON_TEXT_SIZE-1);
        btn.setTypeface(titleTf);
        btn.setAllCaps(false);
        btn.setVisibility(View.VISIBLE);
        if (str.equals(tagToColor))
        {
            btn.setBackground(Util.createBorder(20, selectedTagColor, false, null, 1));
        }
        else
        {
            btn.setBackground(Util.createBorder(20,ContextCompat.getColor(context,R.color.white), false, null, 1));
        }
        btn.setOnClickListener(view -> tagClicked(((TextView)view).getText().toString()));
    }

    private void setStatusLayout(int i, RecyclerViewAdapter.MyViewHolder holder)
    {
        lParams = new LinearLayout.LayoutParams((int)(mainWidth*0.97), (int)(statusButtonSize*1.3));
        lParams.setMargins(0,0,0,10);
        createLinearLayout(LinearLayout.HORIZONTAL, stripColor, 1, View.VISIBLE, holder.status_layout);
        holder.status_layout.setGravity(Gravity.CENTER_VERTICAL);
        holder.status_layout.setPadding(5,0,5,0);

        lParams = new LinearLayout.LayoutParams(statusButtonSize, statusButtonSize);
        createButton(true, drawables[4],"", 0, titleTf,  holder.commentsBtn);
        holder.commentsBtn.setOnClickListener(view -> {
            String txt = ((TextView)((LinearLayout) view.getParent()).getChildAt(1)).getText().toString();
            if (Integer.parseInt(txt) > 0)
            {
                int id = ((LinearLayout)(view.getParent()).getParent()).getId();
                showComments(id);
            }
        });

        String txt = list.get(i).getNumOfComments()+"";
        lParams = new LinearLayout.LayoutParams((int)(txt.length()*2.2*BUTTON_TEXT_SIZE), statusButtonSize);
        lParams.setMargins(0,0,100,0);
        createButton(false, -100, txt, BUTTON_TEXT_SIZE, titleTf, holder.commentsNumBtn);
        holder.commentsNumBtn.setPadding(0,0,0,0);
        holder.commentsNumBtn.setOnClickListener(view -> {
            if (Integer.parseInt(((TextView) view).getText().toString()) > 0)
            {
                int id = ((LinearLayout)(view.getParent()).getParent()).getId();
                showComments(id);
            }
        });

        lParams = new LinearLayout.LayoutParams((int)(mainWidth*0.55), (int)(statusButtonSize*1.3));
        holder.selectedEmojiLayout.setGravity(Gravity.CENTER);
        holder.selectedEmojiLayout.setLayoutParams(lParams);
        holder.selectedEmojiLayout.setClickable(true);
        holder.selectedEmojiLayout.setOnClickListener(view -> {
            if (Integer.parseInt(((TextView)((LinearLayout)view).getChildAt(5)).getText().toString()) > 0)
            {
                showEmojis(view);
            }
        });
        populateEmojiSelectedPanel(holder, list.get(i));

        txt = String.valueOf(list.get(i).getVotes().getLikes().size() + list.get(i).getVotes().getDislikes().size() + list.get(i).getVotes().getLol().size()
                + list.get(i).getVotes().getSad().size() + list.get(i).getVotes().getAngry().size());
        lParams = new LinearLayout.LayoutParams(txt.length()*BUTTON_TEXT_SIZE*2, statusButtonSize);
        lParams.gravity = Gravity.CENTER_VERTICAL;
        createTextView(-100, txt, BUTTON_TEXT_SIZE, titleTf, holder.numOfEmojiesTxt);

        txt = list.get(i).getDate().substring(0,16);
        lParams = new LinearLayout.LayoutParams((int)(txt.length()*BUTTON_TEXT_SIZE*1.5), statusButtonSize);
        lParams.rightMargin = 25;
        lParams.gravity = Gravity.END;
        createTextView(-100, txt, BUTTON_TEXT_SIZE, titleTf, holder.dateAddedTxt);

        if (!isRating)
        {
            holder.starTxt.setVisibility(View.GONE);
            holder.ratingTxt.setVisibility(View.GONE);
        }
        lParams = new LinearLayout.LayoutParams(statusButtonSize, statusButtonSize);
        lParams.leftMargin = 45;
        createTextView(drawables[5], "", 0, titleTf, holder.starTxt);

        txt = list.get(i).getRating()+"";
        lParams = new LinearLayout.LayoutParams(txt.length()*BUTTON_TEXT_SIZE*2, statusButtonSize);
        createTextView(-100, txt, BUTTON_TEXT_SIZE, titleTf, holder.ratingTxt);
    }

    private void setActionLayout(int i, RecyclerViewAdapter.MyViewHolder holder)
    {
        RecyclerViewAdapter.MyViewHolder1 holder1;
        RecyclerViewAdapter.MyViewHolder2 holder2;
        lParams = new LinearLayout.LayoutParams((int)(mainWidth*0.97), (int) (actionButtonSize*1.3));
        lParams.setMargins(0,0,0,30);
        createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, View.VISIBLE, holder.actionLayout);
        holder.dateAddedTxt.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        holder.actionLayout.setGravity(Gravity.END);

        lParams = new LinearLayout.LayoutParams((int)(mainWidth /2.2), (int) (actionButtonSize*1.3));
        createEmojiPanel(holder);

        lParams = new LinearLayout.LayoutParams(actionButtonSize, actionButtonSize);
        lParams.setMargins(20,13,0,0);
        createButton(true, drawables[2], "", 0, titleTf, holder.plusBtn);
        holder.plusBtn.setOnClickListener(view -> handleEmojiPanel(view));

        lParams = new LinearLayout.LayoutParams(actionButtonSize, actionButtonSize);
        lParams.setMargins(20,13,0,0);
        createButton(true, drawables[1], "", 0, titleTf, holder.downloadBtn);
        holder.downloadBtn.setOnClickListener(view -> {
        });

        if (holder instanceof RecyclerViewAdapter.MyViewHolder1)
        {
            holder1 = (RecyclerViewAdapter.MyViewHolder1) holder;
            lParams = new LinearLayout.LayoutParams(actionButtonSize, actionButtonSize);
            lParams.setMargins(0,13,10,0);
            createButton(true , drawables[0],"",0, titleTf, holder1.shareBtn);
            holder1.shareBtn.setSelected(Util.findPinned(list.get(i).getItemId()) != -1);

            holder1.shareBtn.setOnClickListener(view -> {
                if (view.isSelected())
                {
                    view.setSelected(false);
                    MainActivity.getLocalStorage().removeItem(list.get(i).getItemId(), MainActivity.getUser(0), "pinned");
                    MainActivity.getUser(0).getUsersPinned().remove(i);
                }
                else {
                    view.setSelected(true);
                    MainActivity.getLocalStorage().updatePinned(MainActivity.getUser(0), 0, list.get(i).getItemId(), 1, 1);
                    MainActivity.getUser(0).getUsersPinned().add(list.get(i));
                }
            });
        }
        else
        {
            holder2 = (RecyclerViewAdapter.MyViewHolder2) holder;
            holder2.closeBtn.setOnClickListener(view -> {
                view.setSelected(true);
                MainActivity.getLocalStorage().removeItem(list.get(i).getItemId(), MainActivity.getUser(0), "pinned");
                MainActivity.getUser(0).getUsersPinned().remove(Util.findPinned(list.get(i).getItemId()));
                adapter.notifyDataSetChanged();});
        }

    }

    private void createEmojiPanel(RecyclerViewAdapter.MyViewHolder holder)
    {
        int width = holder instanceof RecyclerViewAdapter.MyViewHolder1? (int)(mainWidth/1.48): (int)(mainWidth/1.32);
        lParams = new LinearLayout.LayoutParams(width, (int)(actionButtonSize*1.3));
        lParams.setMargins(10,0,0,0);
        createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, View.GONE, holder.choiceEmojiLayout);

        lParams = new LinearLayout.LayoutParams(actionButtonSize, actionButtonSize);
        lParams.setMargins(0,0,70,0);
        createButton(true, drawables[3], "", 0, titleTf, holder.addCommentBtn);
        holder.addCommentBtn.setOnClickListener(view -> {
            if (MainActivity.getUser(0).isLoggedIn())
            {
                handleAddCommentPanel(view);
            }
            else
            {
                Util.showNotLoggedInDialog("Unknown user", "If you want to comment, you have to login first", activity);
            }
        });
        lParams = new LinearLayout.LayoutParams(actionButtonSize, actionButtonSize);
        lParams.setMargins(0,0,20,0);

        createButton(true ,R.drawable.like,"", 0, titleTf, holder.heartBtn);
        holder.heartBtn.setOnClickListener(view -> {
            if (MainActivity.getUser(0).isLoggedIn())
            {
                updateEmojiPanel(0, view);
            }
            else
            {
                Util.showNotLoggedInDialog("Unknown user", "If you want to vote, you have to login first", activity);
            }
        });
        createButton(true ,R.drawable.dislike,"", 0, titleTf, holder.unlikeBtn);
        holder.unlikeBtn.setOnClickListener(view -> {
            if (MainActivity.getUser(0).isLoggedIn())
            {
                updateEmojiPanel(1, view);
            }
            else
            {
                Util.showNotLoggedInDialog("Unknown user", "If you want to vote, you have to login first", activity);
            }
        });

        createButton(true, R.drawable.angry, "", 0, titleTf, holder.angryBtn);
        holder.angryBtn.setOnClickListener(view -> {
            if (MainActivity.getUser(0).isLoggedIn())
            {
                updateEmojiPanel(4, view);
            }
            else
            {
                Util.showNotLoggedInDialog("Unknown user","If you want to vote, you have to login first", activity);
            }
        });
        createButton(true, R.drawable.sad, "", 0 , titleTf, holder.sadBtn);
        holder.sadBtn.setOnClickListener(view -> {
            if (MainActivity.getUser(0).isLoggedIn())
            {
                updateEmojiPanel(3, view);
            }
            else
            {
                Util.showNotLoggedInDialog("Unknown user", "If you want to vote, you have to login first", activity);
            }
        });
        createButton(true, R.drawable.lol,"", 0, titleTf, holder.lolBtn);
        holder.lolBtn.setOnClickListener(view -> {
            if (MainActivity.getUser(0).isLoggedIn())
            {
                updateEmojiPanel(2, view);
            }
            else
            {
                Util.showNotLoggedInDialog("Unknown user", "If you want to vote, you have to login first", activity);
            }
        });
    }

    private void populateEmojiSelectedPanel(RecyclerViewAdapter.MyViewHolder holder, Item item)
    {
        char existingVote = Util.checkVote(list, item.getVotes(), MainActivity.getUser(0).getUserName());
        int size, image;
        size = existingVote == 'd'? (int)(statusButtonSize*1.25): statusButtonSize;
        image = existingVote == 'd'? R.drawable.selected_dislike: R.drawable.dislike_cut;
        lParams = new LinearLayout.LayoutParams(size, size);
        createTextView(image, "", 0, titleTf, holder.unlikeTxt);
        holder.unlikeTxt.setVisibility(View.VISIBLE);
        holder.unlikeTxt.setLayoutParams(lParams);

        size = existingVote == 'l'? (int)(statusButtonSize*1.25): statusButtonSize;
        image = existingVote == 'l'? R.drawable.selected_like: R.drawable.like_cut;
        lParams = new LinearLayout.LayoutParams(size, size);
        createTextView(image, "", 0, titleTf, holder.heartTxt);
        holder.heartTxt.setVisibility(View.VISIBLE);
        holder.heartTxt.setLayoutParams(lParams);

        size = existingVote == 'f'? (int)(statusButtonSize*1.25): statusButtonSize;
        image = existingVote == 'f'? R.drawable.selected_lol: R.drawable.lol_cut;
        lParams = new LinearLayout.LayoutParams(size, size);
        createTextView(image, "", 0, titleTf, holder.lolTxt);
        holder.lolTxt.setVisibility(View.VISIBLE);
        holder.lolTxt.setLayoutParams(lParams);

        size = existingVote == 's'? (int)(statusButtonSize*1.25): statusButtonSize;
        image = existingVote == 's'? R.drawable.selected_sad: R.drawable.sad_cut;
        lParams = new LinearLayout.LayoutParams(size, size);
        createTextView(image, "", 0, titleTf, holder.sadTxt);
        holder.sadTxt.setVisibility(View.VISIBLE);
        holder.sadTxt.setLayoutParams(lParams);

        size = existingVote == 'a'? (int)(statusButtonSize*1.25): statusButtonSize;
        image = existingVote == 'a'? R.drawable.selected_angry: R.drawable.angry;
        lParams = new LinearLayout.LayoutParams(size, size);
        createTextView(image, "", 0, titleTf, holder.angryTxt);
        holder.angryTxt.setVisibility(View.VISIBLE);
        holder.angryTxt.setLayoutParams(lParams);
    }

    private void setEditLayout(RecyclerViewAdapter.MyViewHolder holder)
    {
        lParams = new LinearLayout.LayoutParams((int)(mainWidth*0.95), LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(0, 10, 0, 30);
        lParams.gravity = Gravity.CENTER;
        createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 1, View.GONE, holder.editLayout);

        lParams = new LinearLayout.LayoutParams((int)(mainWidth*0.75), statusButtonSize*2);
        holder.sendEdit.setLayoutParams(lParams);
        holder.sendEdit.setPadding(40,0,40,0);
        holder.sendEdit.setBackgroundColor(Color.TRANSPARENT);

        lParams = new LinearLayout.LayoutParams((int)(mainWidth*0.95 - mainWidth*0.75),actionButtonSize*2);
        createButton(true, drawables[6], "", 0, titleTf, holder.sendBtn);
        holder.sendBtn.setLayoutParams(lParams);
        holder.sendBtn.setOnClickListener(view -> addComment(view));
    }

    private void createLinearLayout(int orientation, int color, int border, int visible, LinearLayout layout)
    {
        layout.setOrientation(orientation);
        layout.setLayoutParams(lParams);
        layout.setGravity(Gravity.CENTER);
        layout.setVisibility(visible);
        layout.setBackground(Util.createBorder(20, color, false, null, border));
    }

    private void createButton(boolean isImage, int image, String txt, int textSize, Typeface typeface, Button btn) {
        btn.setTextSize(textSize);
        btn.setTextColor(Color.BLACK);
        btn.setAllCaps(false);
        btn.setPadding(0,0,0,0);
        btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btn.setLayoutParams(lParams);
        if (isImage) {
            btn.setBackgroundResource(image);
        } else {
            btn.setText(txt);
            btn.setBackground(Util.createBorder(1, Color.TRANSPARENT, false, null, 0));
        }

        btn.setTypeface(typeface);
    }

    private void createTextView(int icon, String txt, int textSize, Typeface typeface, TextView txtView)
    {
        txtView.setText(txt);
        txtView.setTextSize(textSize);
        txtView.setTextColor(Color.BLACK);
        txtView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtView.setBackgroundColor(Color.TRANSPARENT);
        txtView.setLayoutParams(lParams);
        if (icon > -100) {
            txtView.setBackgroundResource(icon);
        }
        txtView.setTypeface(typeface);
    }

    protected abstract void addComment(View view);

    protected abstract void showComments(int id);

    protected abstract void tagClicked(String tagChosen);

    protected abstract void showEmojis(View view);

    protected abstract void handleEmojiPanel(View view);

    protected abstract void handleAddCommentPanel(View view);

    protected abstract void updateEmojiPanel(int i, View view);

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
        public class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout objectLayout;
            LinearLayout tagLayout;
            LinearLayout choiceEmojiLayout;
            LinearLayout selectedEmojiLayout;
            LinearLayout status_layout;
            LinearLayout actionLayout;
            LinearLayout editLayout;
            Button tag0Btn;
            Button tag1Btn;
            Button tag2Btn;
            Button commentsBtn;
            Button commentsNumBtn;
            Button addCommentBtn;
            Button plusBtn;
            Button downloadBtn;
            Button angryBtn;
            Button lolBtn;
            Button sadBtn;
            Button heartBtn;
            Button unlikeBtn;
            Button sendBtn;
            TextView messageTxt;
            TextView titleTxt;
            TextView heartTxt;
            TextView unlikeTxt;
            TextView lolTxt;
            TextView sadTxt;
            TextView angryTxt;
            TextView starTxt;
            TextView ratingTxt;
            TextView dateAddedTxt;
            TextView numOfEmojiesTxt;
            ImageView imageView;
            EditText sendEdit;

            public MyViewHolder(View v) {
                super(v);
                objectLayout = v.findViewById(R.id.object_layout);
                tagLayout = v.findViewById(R.id.tag_layout);
                choiceEmojiLayout = v.findViewById(R.id.choice_emoji_layout);
                selectedEmojiLayout = v.findViewById(R.id.selected_emoji_layout);
                status_layout = v.findViewById(R.id.status_layout);
                actionLayout = v.findViewById(R.id.action_layout);
                editLayout = v.findViewById(R.id.edit_layout);

                tag0Btn = v.findViewById(R.id.tag1_btn);
                tag1Btn = v.findViewById(R.id.tag2_btn);
                tag2Btn = v.findViewById(R.id.tag3_btn);
                commentsBtn = v.findViewById(R.id.comments_btn);
                commentsNumBtn = v.findViewById(R.id.comment_num_btn);
                plusBtn = v.findViewById(R.id.plus_btn);
                downloadBtn = v.findViewById(R.id.download_btn);
                angryBtn = v.findViewById(R.id.angry_btn);
                sadBtn = v.findViewById(R.id.sad_btn);
                lolBtn = v.findViewById(R.id.lol_btn);
                heartBtn = v.findViewById(R.id.heart_btn);
                unlikeBtn = v.findViewById(R.id.unlike_btn);
                sendBtn = v.findViewById(R.id.send_btn);
                addCommentBtn = v.findViewById(R.id.add_comment_btn);

                angryTxt = v.findViewById(R.id.angry_txt);
                lolTxt = v.findViewById(R.id.lol_txt);
                sadTxt = v.findViewById(R.id.sad_txt);
                heartTxt = v.findViewById(R.id.heart_txt);
                unlikeTxt = v.findViewById(R.id.unlike_txt);
                starTxt = v.findViewById(R.id.star_txt);
                ratingTxt = v.findViewById(R.id.rating_txt);
                messageTxt = v.findViewById(R.id.message_txt);
                titleTxt = v.findViewById(R.id.title_txt);
                dateAddedTxt = v.findViewById(R.id.date_added_txt);
                numOfEmojiesTxt = v.findViewById(R.id.num_of_emojis);
                imageView = v.findViewById(R.id.imageView);
                sendEdit = v.findViewById(R.id.send_edit);
            }
        }
        public class MyViewHolder1 extends MyViewHolder {
            Button shareBtn;
            View separator;

            public MyViewHolder1(View v) {
                super(v);
                shareBtn = v.findViewById(R.id.share_bnt);
                separator = v.findViewById(R.id.separator);
            }
        }

        public class MyViewHolder2 extends MyViewHolder {
            CardView cardView;
            Button closeBtn;

            public MyViewHolder2(View v) {
                super(v);
                cardView =  v.findViewById(R.id.cv);
                closeBtn = v.findViewById(R.id.close_btn);
            }
        }

        public RecyclerViewAdapter() {
        }

        @Override
        public int getItemViewType(int position) {
            return isNotes? 2:1;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1)
            {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_view_populator, parent, false);

                return new MyViewHolder1(v);
            }
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_view_populator, parent, false);

            return new MyViewHolder2(v);

        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            RecyclerViewAdapter.MyViewHolder1 holder1;
            RecyclerViewAdapter.MyViewHolder2 holder2;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mainWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(40,40,40,40);
            if (holder instanceof RecyclerViewAdapter.MyViewHolder2)
            {
                holder2 = (RecyclerViewAdapter.MyViewHolder2) holder;
                holder2.cardView.setLayoutParams(params);
                holder2.cardView.setBackground(ContextCompat.getDrawable(context, R.drawable.note2));
            }

            params = new FrameLayout.LayoutParams(mainWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (isNotes)
            {
                params.setMargins(0,160,0,0);
            }
            else
            {
                params.setMargins(0,20,0,0);
            }

            holder.objectLayout.setLayoutParams(params);
            holder.objectLayout.setId(position);

            setTagsLayout(position, holder);

            lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            holder.imageView.setImageResource(R.drawable.hamburger);
            holder.imageView.setLayoutParams(lParams);

            if (list.get(position).getTitle().equals(""))
            {
                holder.titleTxt.setVisibility(View.GONE);
            }
            else
            {
                holder.titleTxt.setVisibility(View.VISIBLE);
            }
            lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lParams.setMargins(50, 20, 0, 0);

            createTextView(-100, list.get(position).getTitle(), TEXT_SIZE+2, mainTf,  holder.titleTxt);
            holder.titleTxt.setPaintFlags(holder.titleTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.titleTxt.setLayoutParams(lParams);

            lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lParams.setMargins(20, 10,20,20);
            createTextView(-100, list.get(position).getText(), TEXT_SIZE, mainTf,  holder.messageTxt);
            holder.messageTxt.setLayoutParams(lParams);

            setStatusLayout(position, holder);
            setActionLayout(position, holder);
            setEditLayout(holder);

            lParams = new LinearLayout.LayoutParams((int)(mainWidth*0.92), 2);
            lParams.setMargins(0,0,0,30);
            lParams.gravity = Gravity.CENTER;
            if (holder instanceof RecyclerViewAdapter.MyViewHolder1)
            {
                holder1 = (RecyclerViewAdapter.MyViewHolder1) holder;
                holder1.separator.setLayoutParams(lParams);
                holder1.separator.setBackground(Util.createBorder(context, 20, Color.BLACK, false, 1));
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
