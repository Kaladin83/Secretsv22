package com.example.maratbe.secrets;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Util extends Application implements Constants {
    private static ArrayList<Item> list;
    private static Item item;
    private static Context context;
    private static int commentId;
    private static int commentIndex;
    private static int currentObjectId;
    private static int voteIndex;
    private static char voteType = ' ';
    private static ShowAlertDialog alertDialog;

    public static Drawable createBorder(int radius, int color, boolean gradient, int[] colors, int stroke) {
        GradientDrawable gd;
        if (!gradient) {
            gd = new GradientDrawable();
            gd.setColor(color);
        } else {
            gd = new GradientDrawable(GradientDrawable.Orientation.BR_TL, colors);
        }
        gd.setCornerRadius(radius);
        gd.setStroke(stroke, Color.DKGRAY);
        return gd;
    }

    public static Drawable createBorder(Context context, int radius, int color, boolean gradient, int stroke) {
        GradientDrawable gd;
        if (!gradient) {
            gd = new GradientDrawable();
            gd.setColor(color);
        } else {
            int[] colors = {Color.BLACK, Color.RED, ContextCompat.getColor(context, R.color.transparent_green)};
            gd = new GradientDrawable(GradientDrawable.Orientation.BR_TL, colors);
        }
        gd.setCornerRadius(radius);
        gd.setStroke(stroke, color);
        return gd;
    }

    public static void closeKeyboard(EditText edit, Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    public static void openKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static int calculateScore(Item item) {
        float stars = 0;
        for (int i = 0; i < item.getComments().size(); i++)
        {
            stars += score(item.getComments().get(i).getStars());
        }
        return (int) (item.getVotes().getLikes().size() + item.getVotes().getSad().size() + item.getVotes().getLol().size() + item.getVotes().getAngry().size()  + stars);
    }

    private static float score(int stars) {
        switch (stars)
        {
            case 1: case 2:
                return 0;
            case 3:
                return 1;
            case 4:
                return 2;
            default:
                return (float) 2.5;
        }
    }

    public static void setDialogColors(Context context, ShowAlertDialog alertDialog)
    {
        switch (TabNavigator.getThemeAt(MainActivity.getActiveThemeNumber()).getRadioCircle())
        {
            case "radio_winter":
                alertDialog.setColors(ContextCompat.getColor(context, R.color.transparent_blue), ContextCompat.getColor(context, R.color.blue_active_bar),
                        ContextCompat.getColor(context, R.color.blue_active_bar));
                break;
            case "radio_summer":
                alertDialog.setColors(ContextCompat.getColor(context, R.color.transparent_yellow), ContextCompat.getColor(context, R.color.dark_yellow_gradient),
                        ContextCompat.getColor(context, R.color.dark_yellow_gradient) );
                break;
            case "radio_spring":
                alertDialog.setColors(ContextCompat.getColor(context, R.color.transparent_green), ContextCompat.getColor(context, R.color.green_active_bar),
                        ContextCompat.getColor(context, R.color.green_active_bar) );
                break;
            case "radio_autumn":
                alertDialog.setColors(ContextCompat.getColor(context, R.color.transparent_red), ContextCompat.getColor(context, R.color.dark_red_gradient),
                        ContextCompat.getColor(context, R.color.dark_red_gradient) );
                break;
            default:
                alertDialog.setColors(ContextCompat.getColor(context, R.color.transparent_purple), ContextCompat.getColor(context, R.color.colorPrimary),
                        ContextCompat.getColor(context, R.color.colorPrimary) );
                break;
        }
    }

    public static String encodeStringUrl(String url) {
        String encodedUrl = null;
        try {
            encodedUrl = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return encodedUrl;
        }
        return encodedUrl;
    }

    public static String decodeStringUrl(String encodedUrl) {
        String decodedUrl = null;
        try {
            decodedUrl = URLDecoder.decode(encodedUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return decodedUrl;
        }
        return decodedUrl;
    }

    public static void handleDeleteComment(int commentId_, int commentIndex_, Activity activity, Context context_, final Item item_, TabNavigator tabNavigator_, ShowComments showComments_) {
        item = item_;
        commentId = commentId_;
        commentIndex = commentIndex_;
        context = context_;
        final TabNavigator tabNavigator = tabNavigator_;
        final ShowComments showComments = showComments_;

        alertDialog = new ShowAlertDialog(activity) {
            @Override
            protected void buttonPressed(int id) {
                if (id == 0)
                {
                    showComments.deleteConfirmed(commentIndex);
                    AsyncTask.execute(() -> {
                        MainActivity.getDbInstance().deleteComment(item.getItemId(), commentId, item.getNumOfComments(), true, false);

                        MainActivity.getDbInstance().updateScore(item.getItemId(),
                                Util.calculateScore(item), false, false);
                        tabNavigator.recreateListView(0);
                        tabNavigator.recreateListView(1);
                    });
                }
                alertDialog.dismiss();
            }
        };
        alertDialog.setTexts(activity.getString(R.string.delete_comment_title),
                activity.getString(R.string.delete_comment_body), new int[] {R.string.ok, R.string.cancel});
        Util.setDialogColors(context, alertDialog);
        alertDialog.show();
    }

    public static void showNotLoggedInDialog(String title, String body, Activity activity_)
    {
        final Activity activity = activity_;
        alertDialog = new ShowAlertDialog(activity) {
            @Override
            protected void buttonPressed(int id) {
                if (id == 0)
                {
                    Intent intent = new Intent(activity, CreateAccount.class);
                    intent.putExtra("METHOD", "register");
                    activity.startActivity(intent);
                }
                else if(id == 1)
                {
                    Intent intent = new Intent(activity, CreateAccount.class);
                    intent.putExtra("METHOD", "signIn");
                    activity.startActivity(intent);
                }
                else
                {
                    TabNavigator.getTabNavigatorInstance().updateLogin(false);
                }
                alertDialog.dismiss();
            }
        };
        alertDialog.setTexts(title, body, new int[] {R.string.register, R.string.sign_in, R.string.not_now});
        alertDialog.setCanceledOnTouchOutside(false);
        Util.setDialogColors(activity, alertDialog);
        alertDialog.show();
    }

    public static ArrayList<Item> getList(String listName) {
        String[] values = listName.split(":");
        ArrayList<Item> returnValue;
        switch (values[0]) {
            case "Secrets":
                returnValue = MainActivity.getSecrets(); break;
            case "ItemsByTag":
                returnValue = MainActivity.getItemsByTag(); break;
            case "GetUsersSecrets":
                returnValue = MainActivity.getUser(Integer.parseInt(values[1])).getUsersSecrets(); break;
            case "GetUsersComments":
                returnValue = MainActivity.getUser(Integer.parseInt(values[1])).getUsersComments(); break;
            case "GetUsersVotes":
                returnValue = MainActivity.getUser(Integer.parseInt(values[1])).getUsersVotes(); break;
            case "GetUsersPinned":
                returnValue = MainActivity.getUser(Integer.parseInt(values[1])).getUsersPinned(); break;
            default:
                returnValue = MainActivity.getTopTen();
        }
        return returnValue;
    }

    public static void setList(ArrayList<Item> list, String listName) {
        String[] values = listName.split(":");
        switch (values[0]) {
            case "Secrets":
                MainActivity.setSecretsList(list); break;
            case "ItemsByTag":
                MainActivity.setItemByTagList(list); break;
            case "GetUsersSecrets":
                MainActivity.getUser(Integer.parseInt(values[1])).setUsersItemsBySecretsList(list); break;
            case "GetUsersComments":
                MainActivity.getUser(Integer.parseInt(values[1])).setUsersItemsByCommentsList(list); break;
            case "GetUsersVotes":
                MainActivity.getUser(Integer.parseInt(values[1])).setUsersItemsByVotesList(list); break;
            default:
                 MainActivity.setTopTenList(list);
        }
    }

    public static int getListSize(String listName, int index)
    {
        int returnValue;
        switch (listName)
        {
            case "Secrets": returnValue = MainActivity.getSecrets().size(); break;
            case "ItemsByTag": returnValue = MainActivity.getItemsByTag().size(); break;
            case "GetUsersSecrets": returnValue = MainActivity.getUser(index).getUsersSecrets().size(); break;
            case "GetUsersComments": returnValue = MainActivity.getUser(index).getUsersComments().size(); break;
            case "GetUsersVotes": returnValue = MainActivity.getUser(index).getUsersVotes().size(); break;
            case "GetUsersPinned": returnValue = MainActivity.getUser(index).getUsersPinned().size(); break;
            case "TopTen": returnValue = MainActivity.getTopTen().size(); break;
            default:
                if (listName.contains("TopTen"))
                {
                    returnValue = MainActivity.getTopTen().get(index).getComments().size(); break;
                }
                else if(listName.contains("Secrets"))
                {
                    returnValue = MainActivity.getSecrets().get(index).getComments().size(); break;
                }
                else
                {
                    String[] values = listName.split(":");
                    switch (values[1])
                    {
                        case "GetUserSecrets":  returnValue = MainActivity.getUser(Integer.parseInt(values[2])).getUsersSecrets().get(index).getComments().size(); break;
                        case "GetUsersComments": returnValue = MainActivity.getUser(Integer.parseInt(values[2])).getUsersComments().get(index).getComments().size(); break;
                        case "GetUsersVotes": returnValue = MainActivity.getUser(Integer.parseInt(values[2])).getUsersVotes().get(index).getComments().size(); break;
                        default: returnValue = MainActivity.getUser(Integer.parseInt(values[2])).getUsersPinned().get(index).getComments().size(); break;
                    }
                }
        }
        return returnValue;
    }

    public static int findPinned(int item_id) {
        for (int i = 0; i < MainActivity.getUser(0).getUsersPinned().size(); i++)
        {
            if (MainActivity.getUser(0).getUsersPinned().get(i).getItemId() == item_id)
            {
                return i;
            }
        }
        return -1;
    }

    public static char checkVote(ArrayList<Item> list_, Item.Votes votes, String userName) {
        list = list_;
        for (int i = 0; i < votes.getLikes().size(); i++)
        {
            if (votes.getLikes().get(i).equals(userName))
            {
                voteIndex = i;
                return 'l';
            }
        }
        for (int i = 0; i < votes.getDislikes().size(); i++)
        {
            if (votes.getDislikes().get(i).equals(userName))
            {
                voteIndex = i;
                return 'd';
            }
        }
        for (int i = 0; i < votes.getSad().size(); i++)
        {
            if (votes.getSad().get(i).equals(userName))
            {
                voteIndex = i;
                return 's';
            }
        }
        for (int i = 0; i < votes.getLol().size(); i++)
        {
            if (votes.getLol().get(i).equals(userName))
            {
                voteIndex = i;
                return 'f';
            }
        }
        for (int i = 0; i < votes.getAngry().size(); i++)
        {
            if (votes.getAngry().get(i).equals(userName))
            {
                voteIndex = i;
                return 'a';
            }
        }
        return ' ';
    }

    public static void updateVotes(int emoji, ArrayList<Item> list_, int currentObjectId_, LinearLayout selectedEmojisPanel,
                                  TextView emojiText, Activity activity, TabNavigator tabNavigator, UserDataDisplay userDataDisplay, Favorites favorites) {
        char existingVote;
        currentObjectId = currentObjectId_;
        switch (emoji)
        {
            case 0:
                voteType = 'l'; break;
            case 1:
                voteType = 'd'; break;
            case 2:
                voteType = 'f'; break;
            case 3:
                voteType = 's'; break;
            default:
                voteType = 'a'; break;
        }
        existingVote = Util.checkVote(list_ ,list_.get(currentObjectId).getVotes(), MainActivity.getUser(0).getUserName());

        int numOfVotes = Integer.parseInt(((TextView)selectedEmojisPanel.getChildAt(NUM_OF_EMOJIS_TXT)).getText().toString()) + 1;
        if (existingVote != voteType && existingVote != ' ')
        {
            alertDialog = new ShowAlertDialog(activity) {
                @Override
                protected void buttonPressed(int id) {
                    if (id == 0)
                    {
                        insertVote(emojiText, existingVote, voteType, numOfVotes);
                        if (tabNavigator != null) {
                            tabNavigator.recreateListView(0);
                            tabNavigator.recreateListView(1);
                        }
                        else if(userDataDisplay != null)
                        {
                            userDataDisplay.showSecrets();
                        }
                        else
                        {
                           favorites.showSecrets();
                        }
                    }
                    alertDialog.dismiss();
                }
            };
            alertDialog.setTexts("You have voted", "", new int[] {R.string.ok, R.string.cancel});
            alertDialog.setImage(new String[]{"You have already voted","Do you want to change to"}, new int[]{getImage(existingVote), getImage(voteType)});
            alertDialog.setCanceledOnTouchOutside(false);
            Util.setDialogColors(activity, alertDialog);
            alertDialog.show();
        }
        else if(existingVote == ' ')
        {
            insertVote(emojiText, existingVote, voteType, numOfVotes);
            if (tabNavigator != null) {
                tabNavigator.recreateListView(0);
                tabNavigator.recreateListView(1);
            }
            else if(userDataDisplay != null)
            {
                userDataDisplay.showSecrets();
            }
            else
            {
                favorites.showSecrets();
            }
        }
    }

    private static void insertVote(TextView textField, char existingVote, char voteType, int numOfVotes) {
        if (existingVote != ' ')
        {
            removeUserVote(existingVote);
        }
        else
        {
            MainActivity.getLocalStorage().updateVotes(MainActivity.getUser(0), 0, list.get(currentObjectId).getItemId(), 1);
            textField.setText(String.valueOf(numOfVotes));
        }
        addUserVote(voteType);

        AsyncTask.execute(() -> {
            boolean open = false;
            if (existingVote != ' ')
            {
                open = true;
                MainActivity.getDbInstance().deleteVote(list.get(currentObjectId).getItemId(),
                        MainActivity.getUser(0).getUserName(), list.get(currentObjectId).getNumOfVotes(), true, false);
            }
            MainActivity.getDbInstance().updateScore(currentObjectId, Util.calculateScore(list.get(currentObjectId)), !open, false);
            MainActivity.getDbInstance().insertVote(list.get(currentObjectId).getItemId(), voteType ,MainActivity.getUser(0).getUserName(), list.get(currentObjectId).getNumOfVotes(), false, true);
        });

    }

    public static void removeUserVote(char vote) {
        switch (vote)
        {
            case 'l': list.get(currentObjectId).getVotes().getLikes().remove(voteIndex); break;
            case 'd': list.get(currentObjectId).getVotes().getDislikes().remove(voteIndex); break;
            case 'f': list.get(currentObjectId).getVotes().getLol().remove(voteIndex); break;
            case 's': list.get(currentObjectId).getVotes().getSad().remove(voteIndex); break;
            default: list.get(currentObjectId).getVotes().getAngry().remove(voteIndex); break;
        }
    }

    private static void addUserVote(char voteType) {
        switch (voteType)
        {
            case 'l': list.get(currentObjectId).getVotes().getLikes().add(MainActivity.getUser(0).getUserName()); break;
            case 'd': list.get(currentObjectId).getVotes().getDislikes().add(MainActivity.getUser(0).getUserName()); break;
            case 'f': list.get(currentObjectId).getVotes().getLol().add(MainActivity.getUser(0).getUserName()); break;
            case 's': list.get(currentObjectId).getVotes().getSad().add(MainActivity.getUser(0).getUserName()); break;
            default: list.get(currentObjectId).getVotes().getAngry().add(MainActivity.getUser(0).getUserName()); break;
        }
    }

    private static int getImage(char vote) {
        int choice;
        switch (vote)
        {
            case 'l': choice = R.drawable.like; break;
            case 'd': choice = R.drawable.dislike; break;
            case 'f': choice = R.drawable.lol; break;
            case 's': choice = R.drawable.sad; break;
            default: choice = R.drawable.angry; break;
        }
        return choice;
    }

    public static void makeString(String item, StringBuffer str, int i) {
        if (i == 0)
        {
            str.append(item);
        }
        else
        {
            if ((i % MAX_ITEMS_SHOWS) == 0) {
                str.append(":");
                str.append(item);
            }
            else {
                str.append(",");
                str.append(item);
            }
        }
    }

    public static String appendItems(LinkedHashSet<String> setOfItems, int start, int end) {
        StringBuilder str = new StringBuilder();
        int i = 0;
        for (String item: setOfItems) {
            if (i == start)
            {
                str.append(item);
            }

            if (i > start && i < end){
                str.append(",");
                str.append(item);
            }
            if (i == end)
            {
                break;
            }
            i++;
        }
        return str.toString();
    }
}
