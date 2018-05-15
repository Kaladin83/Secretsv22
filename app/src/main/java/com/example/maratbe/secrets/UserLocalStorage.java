package com.example.maratbe.secrets;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStorage {
    public static final String SP_NAME = "userDetails";
    public SharedPreferences userLocalDB;

    public UserLocalStorage(Context context){
        userLocalDB = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user)
    {
        SharedPreferences.Editor editor = userLocalDB.edit();
        editor.putString("userName", user.getUserName());
        editor.apply();
        setLoggedIn(true);
    }

    public void updateTheme(User user, int themeNum)
    {
        SharedPreferences.Editor editor = userLocalDB.edit();
        user.setThemeNumber(themeNum);
        editor.putInt("theme", user.getThemeNumber());
        editor.apply();
    }

    public void updateSecrets(User user, int itemId)
    {
        SharedPreferences.Editor editor = userLocalDB.edit();
        user.addSecret();
        user.setSecrets(itemId);
        editor.putInt("numOfSecrets", user.getNumOfSecrets());
        editor.putString("secrets", user.getSecrets());
        editor.apply();
    }

    public void updateSecrets(User user, int userIndex, int itemId, int i)
    {
        if (i == 0)
        {
            user.setSecrets("");
            user.setNumOfSecrets(0);
        }
        user.addSecret();
        user.setSecrets(itemId);
        if (userIndex == 0)
        {
            SharedPreferences.Editor editor = userLocalDB.edit();
            editor.putInt("numOfSecrets", user.getNumOfSecrets());
            editor.putString("secrets", user.getSecrets());
            editor.apply();
        }
    }

    public void updateVotes(User user, int itemId)
    {
        SharedPreferences.Editor editor = userLocalDB.edit();
        user.addVote();
        user.setVotes(itemId);
        editor.putInt("numOfVotes", user.getNumOfVotes());
        editor.putString("votes", user.getVotes());
        editor.apply();

    }

    public void updateVotes(User user, int userIndex, int itemId, int num, int i)
    {
        if (i == 0)
        {
            user.setVotes("");
            user.setNumOfVotes(0);
        }
        user.addVote(num);
        user.setVotes(itemId);

        if (userIndex == 0)
        {
            SharedPreferences.Editor editor = userLocalDB.edit();
            editor.putInt("numOfVotes", user.getNumOfVotes());
            editor.putString("votes", user.getVotes());
            editor.putInt("numOfItemsWithVotes", user.getNumOfVotes());
            editor.apply();
        }
    }

    public void updatePinned(User user, int userIndex, int itemId, int num, int i)
    {
        if (i == 0)
        {
            user.setPinned("");
            user.setNumOfPinned(0);
        }
        user.addPinned(num);
        user.setPinned(itemId);

        if (userIndex == 0)
        {
            SharedPreferences.Editor editor = userLocalDB.edit();
            editor.putInt("numOfPinned", user.getNumOfPinned());
            editor.putString("pinned", user.getPinned());
            editor.apply();
        }
    }

    public void updateComments(User user, int itemId)
    {
        SharedPreferences.Editor editor = userLocalDB.edit();
        user.addComment();
        if (!user.getComments().contains(","+itemId+",") && !user.getComments().contains(":"+itemId+",") &&
                !user.getComments().contains(","+itemId+":"))
        {
            user.addNumOfItemsWithComments();
            user.setComments(itemId);
        }

        editor.putString("comments", user.getComments());
        editor.putInt("numOfComments", user.getNumOfComments());
        editor.putInt("numOfItemsWithComments", user.getNumOfItemsWithComments());
        editor.apply();
    }

    public void updateComments(User user, int userIndex, int itemId, int num, int i)
    {
        if (i == 0)
        {
            user.setComments("");
            user.setNumOfComments(0);
        }

        user.addComment(num);
        user.addNumOfItemsWithComments();
        user.setComments(itemId);
        if (userIndex == 0)
        {
            SharedPreferences.Editor editor = userLocalDB.edit();
            editor.putString("comments", user.getComments());
            editor.putInt("numOfComments", user.getNumOfComments());
            editor.putInt("numOfItemsWithComments", user.getNumOfItemsWithComments());
            editor.apply();
        }
    }

    public boolean isCommentLike(int commentId, User user)
    {
        String comments = user.getCommentLikes();
        if (comments.contains(","+commentId+",") || (comments.contains(commentId+"") && user.getNumOfCommentLikes() == 1) || comments.contains(","+commentId))
        {
            return true;
        }
        return false;
    }

    public boolean isCommentDislike(int commentId, User user)
    {
        String comments = user.getCommentDislikes();
       /* if (comments.contains(","+commentId+",") ||  (comments.contains(commentId+"") && user.getNumOfCommentDislikes() == 1) || comments.contains(","+commentId))
        {
            return true;
        }*/
        return comments.contains(","+commentId+",") ||  (comments.contains(commentId+"") && user.getNumOfCommentDislikes() == 1) || comments.contains(","+commentId);
       // return false;
    }

    public void updateCommentsLikes(User user, int commentId)
    {
        user.setNumOfCommentLikes(user.getNumOfCommentLikes()+1);
        user.setCommentLikes(commentId);

        SharedPreferences.Editor editor = userLocalDB.edit();
        editor.putString("commentLikes", user.getCommentLikes());
        editor.putInt("numOfCommentLikes", user.getNumOfCommentLikes());
        editor.apply();
    }

    public void updateCommentsDislikes(User user, int commentId)
    {
        user.setNumOfCommentDislikes(user.getNumOfCommentDislikes()+1);
        user.setCommentDislikes(commentId);

        SharedPreferences.Editor editor = userLocalDB.edit();
        editor.putString("commentDislikes", user.getCommentDislikes());
        editor.putInt("numOfCommentDislikes", user.getNumOfCommentDislikes());
        editor.apply();
    }

    public User getLoggedInUser()
    {
        return new User(userLocalDB.getString("userName", ""),
                userLocalDB.getInt("numOfVotes", 0),userLocalDB.getInt("numOfComments", 0), userLocalDB.getInt("numOfPinned", 0), userLocalDB.getInt("numOfSecrets", 0),
                userLocalDB.getInt("numOfCommentLikes", 0), userLocalDB.getInt("numOfCommentDislikes", 0),
                userLocalDB.getString("secrets",""), userLocalDB.getString("comments",""),userLocalDB.getString("votes",""), userLocalDB.getString("pinned",""),
                userLocalDB.getString("commentLikes",""), userLocalDB.getString("commentDislikes",""), userLocalDB.getInt("theme",0),
                userLocalDB.getInt("commentsScore", 0), userLocalDB.getInt("secretsScore", 0));
    }

    public void setLoggedIn(boolean loggedIn)
    {
        SharedPreferences.Editor editor = userLocalDB.edit();
        editor.putBoolean("loggedIn", loggedIn);
        editor.apply();
    }
    public boolean isLoggedIn()
    {
        return userLocalDB.getBoolean("loggedIn", false);
    }

    public void clearCommentsData()
    {
        SharedPreferences.Editor editor = userLocalDB.edit();
        editor.putString("commentLikes","");
        editor.putString("commentDislikes","");
        editor.putInt("numOfCommentLikes", 0);
        editor.putInt("numOfCommentDislikes", 0);
        MainActivity.getUser(0).setNumOfCommentLikes(0);
        MainActivity.getUser(0).setNumOfCommentDislikes(0);
        MainActivity.getUser(0).setCommentLikes("");
        MainActivity.getUser(0).setCommentDislikes("");
        editor.apply();
    }

    public void clearUserData()
    {
        SharedPreferences.Editor editor = userLocalDB.edit();
        editor.clear();
        editor.apply();
    }

    public boolean removeDislike(int commentId, User user) {
        String comments = user.getCommentDislikes();

        if (comments.contains(","+commentId+","))
        {
            updateDbDislikes(comments.replace(","+commentId+",",","), user);
            return true;
        }

        if (comments.contains(commentId+"") && user.getNumOfCommentDislikes() == 1)
        {
            updateDbDislikes("", user);
            return true;
        }

        if (comments.contains(","+commentId))
        {
            updateDbDislikes(comments.replace(","+commentId,""), user);
            return true;
        }
        return false;
    }

    public boolean removeLike(int commentId, User user) {
        String comments = user.getCommentLikes();

        if (comments.contains(","+commentId+","))
        {
            updateDbLikes(comments.replace(","+commentId+",",","), user);
            return true;
        }

        if (comments.contains(commentId+"") && user.getNumOfCommentLikes() == 1)
        {
            updateDbLikes("", user);
            return true;
        }

        if (comments.contains(","+commentId))
        {
            updateDbLikes(comments.replace(","+commentId,""), user);
            return true;
        }
        return false;
    }

    public boolean removeItem(int itemId, User user, String action) {
        String text = action.equals("votes")? user.getVotes(): user.getPinned();
        int numOfItems = action.equals("votes")? user.getNumOfVotes(): user.getNumOfPinned();
        String changeTo = "$$";

        if (text.contains(","+itemId+","))
        {
            changeTo = reorganizeString(","+itemId, text, numOfItems);
        }

        if (text.contains(itemId+"") && user.getNumOfCommentLikes() == 1)
        {
            changeTo = "";
        }

        if (text.contains(","+itemId))
        {
            changeTo = reorganizeString(","+itemId, text, numOfItems);
        }

        if (text.contains(":"+itemId))
        {
            changeTo = reorganizeString(":"+itemId, text, numOfItems);
        }
        if (!changeTo.equals("$$"))
        {
            if (action.equals("votes"))
            {
                updateDbVotes(changeTo, user);
            }
            else
            {
                updateDbPinned(changeTo, user);
            }
            return true;
        }
        return false;
    }

    private String reorganizeString(String str, String text, int number) {
        StringBuffer newStr = new StringBuffer();
        if (number < 11)
        {
            return text.replace(str, "");
        }
        else
        {
            String txt = text.replace(":","");
            String[] items = txt.split(",");
            for (int i = 0; i < items.length; i++)
            {
                Util.makeString(items[i], newStr, i);
            }
            return newStr.toString();
        }
    }

    public void updateUserScore(String type, int score, User user)
    {
        SharedPreferences.Editor editor = userLocalDB.edit();
        if (type.equals("comments"))
        {
            user.setCommentsScore(score);
            editor.putInt("commentsScore", score);
        }
        else
        {
            user.setSecretsScore(score);
            editor.putInt("secretsScore", score);
        }
        editor.apply();
    }

    private void updateDbDislikes(String comments, User user) {
        SharedPreferences.Editor editor = userLocalDB.edit();
        editor.putString("commentDislikes", comments);
        editor.putInt("numOfCommentDislikes", user.getNumOfCommentDislikes() - 1);
        editor.apply();
        user.setCommentDislikes(comments);
        user.setNumOfCommentDislikes(user.getNumOfCommentDislikes() - 1);
    }

    private void updateDbLikes(String comments, User user) {
        SharedPreferences.Editor editor = userLocalDB.edit();
        editor.putString("commentLikes", comments);
        editor.putInt("numOfCommentLikes", user.getNumOfCommentLikes() - 1);
        editor.apply();
        user.setCommentLikes(comments);
        user.setNumOfCommentLikes(user.getNumOfCommentLikes() - 1);
    }

    private void updateDbVotes(String votes, User user) {
        SharedPreferences.Editor editor = userLocalDB.edit();
        editor.putString("votes", votes);
        editor.putInt("numOfVotes", user.getNumOfVotes() - 1);
        editor.apply();
        user.setVotes(votes);
        user.setNumOfVotes(user.getNumOfVotes() - 1);
    }

    private void updateDbPinned(String pinned, User user) {
        SharedPreferences.Editor editor = userLocalDB.edit();
        editor.putString("pinned", pinned);
        editor.putInt("numOfPinned", user.getNumOfPinned() - 1);
        editor.apply();
        user.setPinned(pinned);
        user.setNumOfPinned(user.getNumOfPinned() - 1);
    }
}
