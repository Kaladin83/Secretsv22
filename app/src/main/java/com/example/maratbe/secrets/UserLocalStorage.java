package com.example.maratbe.secrets;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedHashSet;

public class UserLocalStorage {
    public static final String SP_NAME = "userDetails";
    public SharedPreferences userLocalDB;

    public UserLocalStorage(Context context) {
        userLocalDB = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        storeUserBasic(user);
        setLoggedIn(true, user.getIdType());
    }

    public void storeUserBasic(User user) {
        SharedPreferences.Editor editor = userLocalDB.edit();
        editor.putString("userName" + ":" + user.getIdType(), user.getUserName());
        editor.putString("email" + ":" + user.getIdType(), user.getEmail());
        editor.apply();
    }

    public void setUserData(User user) {
        String str1;
        String str2 = ":" + user.getIdType();

        setLoggedIn(true, user.getIdType());
        str1 = "userName" + str2;
        user.setUserName(userLocalDB.getString(str1, ""));
        str1 = "theme" + str2;
        user.setThemeNumber(userLocalDB.getInt(str1, 0));
        str1 = "commentsScore" + str2;
        user.setCommentsScore(userLocalDB.getInt(str1, 0));
        str1 = "secretsScore" + str2;
        user.setSecretsScore(userLocalDB.getInt(str1, 0));

        str1 = "comments" + str2;
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.addAll(userLocalDB.getStringSet(str1, new LinkedHashSet<>()));
        user.setComments(set);
        user.setNumOfItemsWithComments(set.size());
        str1 = "numOfComments" + str2;
        user.setNumOfComments(userLocalDB.getInt(str1, 0));
        str1 = "secrets" + str2;
        set.clear();
        set.addAll(userLocalDB.getStringSet(str1, new LinkedHashSet<>()));
        user.setSecrets(set);
        user.setNumOfSecrets(set.size());
        str1 = "pinned" + str2;
        set.clear();
        set.addAll(userLocalDB.getStringSet(str1, new LinkedHashSet<>()));
        user.setPinned(set);
        user.setNumOfPinned(set.size());
        str1 = "votes" + str2;
        set.clear();
        set.addAll(userLocalDB.getStringSet(str1, new LinkedHashSet<>()));
        user.setVotes(set);
        user.setNumOfVotes(set.size());
        str1 = "commentDislikes" + str2;
        set.clear();
        set.addAll(userLocalDB.getStringSet(str1, new LinkedHashSet<>()));
        user.setCommentsDislikes(set);
        user.setNumOfCommentDislikes(set.size());
        str1 = "commentLikes" + str2;
        set.clear();
        set.addAll(userLocalDB.getStringSet(str1, new LinkedHashSet<>()));
        user.setCommentsLikes(set);
        user.setNumOfCommentLikes(set.size());
    }

    public void updateTheme(User user, int themeNum) {
        SharedPreferences.Editor editor = userLocalDB.edit();
        user.setThemeNumber(themeNum);
        editor.putInt("theme" + ":" + user.getIdType(), user.getThemeNumber());
        editor.apply();
    }

    public void updateSecrets(User user, int userIndex, int itemId) {
        user.addSecret();
        user.setSecrets(itemId);
        if (userIndex == 0) {
            SharedPreferences.Editor editor = userLocalDB.edit();
            LinkedHashSet<String> set;
            set = user.getSetOfSecrets();
            set.add(String.valueOf(itemId));
            editor.putStringSet("secrets" + ":" + user.getIdType(), set);
            editor.apply();
        }
    }

    public void updateVotes(User user, int userIndex, int itemId, int num) {
        user.addVote(num);
        user.setVotes(itemId);

        if (userIndex == 0) {
            SharedPreferences.Editor editor = userLocalDB.edit();
            LinkedHashSet<String> set;
            set = user.getSetOfVotes();
            set.add(String.valueOf(itemId));
            editor.putStringSet("votes" + ":" + user.getIdType(), set);
            editor.apply();
        }
    }

    public void updatePinned(User user, int userIndex, int itemId, int num) {
        user.addPinned(num);
        user.setPinned(itemId);

        if (userIndex == 0) {
            SharedPreferences.Editor editor = userLocalDB.edit();
            LinkedHashSet<String> set;
            set = user.getSetOfPinned();
            set.add(String.valueOf(itemId));
            editor.putStringSet("pinned" + ":" + user.getIdType(), set);
            editor.apply();
        }
    }

    public void updateComments(User user, int userIndex, int itemId, int num) {
        user.addComment(num);
        user.setComments(itemId);
        user.setNumOfItemsWithComments(user.getSetOfComments().size());
        if (userIndex == 0) {
            SharedPreferences.Editor editor = userLocalDB.edit();
            LinkedHashSet<String> set;
            set = user.getSetOfComments();
            set.add(String.valueOf(itemId));
            editor.putInt("numOfComments" + ":" + user.getIdType(), user.getNumOfComments());
            editor.putStringSet("comments" + ":" + user.getIdType(), set);
            editor.apply();
        }
    }

    public boolean isCommentLike(int commentId, User user) {
        for (String item : user.getSetOfCommentsLikes()) {
            if (item.equals(String.valueOf(commentId))) {
                return true;
            }
        }
        return false;
    }

    public boolean isCommentDislike(int commentId, User user) {
        for (String item : user.getSetOfCommentsDislikes()) {
            if (item.equals(String.valueOf(commentId))) {
                return true;
            }
        }
        return false;
    }

    public void updateCommentsLikes(User user, int commentId) {
        user.setCommentsLikes(commentId);
        SharedPreferences.Editor editor = userLocalDB.edit();
        LinkedHashSet<String> set;
        set = user.getSetOfCommentsLikes();
        set.add(String.valueOf(commentId));
        editor.putStringSet("commentLikes" + ":" + user.getIdType(), set);
        editor.apply();
    }

    public void updateCommentsDislikes(User user, int commentId) {
        user.setCommentsDislikes(commentId);
        SharedPreferences.Editor editor = userLocalDB.edit();
        LinkedHashSet<String> set;
        set = user.getSetOfCommentsDislikes();
        set.add(String.valueOf(commentId));
        editor.putStringSet("commentDislikes" + ":" + user.getIdType(), set);
        editor.apply();
    }

    public void setLoggedIn(boolean loggedIn, char type) {
        SharedPreferences.Editor editor = userLocalDB.edit();
        editor.putBoolean("loggedIn" + ":" + type, loggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        if (userLocalDB.getBoolean("loggedIn:G", false)) {
            MainActivity.getUser(0).logIn(true);
            MainActivity.getUser(0).setIdType('G');
            return true;
        }
        if (userLocalDB.getBoolean("loggedIn:F", false)) {
            MainActivity.getUser(0).logIn(true);
            MainActivity.getUser(0).setIdType('F');
            return true;
        }
        return false;
    }

    public void clearCommentsData() {
        SharedPreferences.Editor editor = userLocalDB.edit();
        editor.putString("commentLikes", "");
        editor.putString("commentDislikes", "");
        editor.putInt("numOfCommentLikes", 0);
        editor.putInt("numOfCommentDislikes", 0);
        MainActivity.getUser(0).setNumOfCommentLikes(0);
        MainActivity.getUser(0).setNumOfCommentDislikes(0);
        MainActivity.getUser(0).setCommentsLikes(new LinkedHashSet<>());
        MainActivity.getUser(0).setCommentsDislikes(new LinkedHashSet<>());
        editor.apply();
    }

    public void clearUserData() {
        SharedPreferences.Editor editor = userLocalDB.edit();
        editor.clear();
        editor.apply();
    }

    private boolean removeItem(LinkedHashSet<String> set, int itemId) {
        for (String item : set) {
            if (item.equals(String.valueOf(itemId))) {
                set.remove(item);
                return true;
            }
        }
        return false;
    }

    public void updateUserScore(String type, int score, User user) {
        SharedPreferences.Editor editor = userLocalDB.edit();
        if (type.equals("comments")) {
            user.setCommentsScore(score);
            editor.putInt("commentsScore" + ":" + user.getIdType(), score);
        } else {
            user.setSecretsScore(score);
            editor.putInt("secretsScore" + ":" + user.getIdType(), score);
        }
        editor.apply();
    }

    public boolean updateDbDislikes(int commentId, User user) {
        LinkedHashSet<String> set = user.getSetOfCommentsDislikes();
        boolean result = removeItem(set, commentId);
        if (result) {
            SharedPreferences.Editor editor = userLocalDB.edit();
            user.setCommentsDislikes(set);
            user.setNumOfCommentDislikes(user.getNumOfCommentDislikes() - 1);
            editor.putStringSet("commentDislikes" + ":" + user.getIdType(), set);
            editor.apply();
        }
        return result;
    }

    public boolean updateDbLikes(int commentId, User user) {
        LinkedHashSet<String> set = user.getSetOfCommentsLikes();
        boolean result = removeItem(set, commentId);
        if (result) {
            SharedPreferences.Editor editor = userLocalDB.edit();
            user.setCommentsLikes(set);
            user.setNumOfCommentLikes(user.getNumOfCommentLikes() - 1);
            editor.putStringSet("commentLikes" + ":" + user.getIdType(), set);
            editor.apply();
        }
        return result;
    }

    public boolean updateDbVotes(int itemId, User user) {
        LinkedHashSet<String> set = user.getSetOfVotes();
        boolean result = removeItem(set, itemId);
        if (result) {
            SharedPreferences.Editor editor = userLocalDB.edit();
            user.setVotes(set);
            user.setNumOfVotes(user.getNumOfVotes() - 1);
            editor.putStringSet("votes" + ":" + user.getIdType(), set);
            editor.apply();
        }
        return result;
    }

    public boolean updateDbPinned(int itemId, User user) {
        LinkedHashSet<String> set = user.getSetOfPinned();
        boolean result = removeItem(set, itemId);
        if (result) {
            SharedPreferences.Editor editor = userLocalDB.edit();
            user.setPinned(set);
            user.setNumOfPinned(user.getNumOfPinned() - 1);
            editor.putStringSet("pinned" + ":" + user.getIdType(), set);
            editor.apply();
        }
        return result;
    }

    public boolean updateDbComments(int itemId, User user) {
        LinkedHashSet<String> set = user.getSetOfComments();
        boolean result = removeItem(set, itemId);
        if (result) {
            SharedPreferences.Editor editor = userLocalDB.edit();
            user.setComments(set);
            user.setNumOfComments(user.getNumOfComments() - 1);
            editor.putStringSet("comments" + ":" + user.getIdType(), set);
            editor.apply();
        }

        return result;
    }
}
