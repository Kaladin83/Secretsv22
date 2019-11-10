package com.example.maratbe.secrets;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class User implements Constants {
    private int commentsScore = 0;
    private int secretsScore = 0;
    private int numOfCommentLikes = 0;
    private int numOfCommentDislikes = 0;
    private int numOfSecrets = 0;
    private int numOfComments = 0;
    private int numOfVotes = 0;
    private int numOfPinned = 0;
    private int numOfItemsWithComments = 0;
    private int themeNumber = 0;
    private char idType = ' ';
    private String userName = "";
    private String email = "";
    private boolean loggedIn = false;
    private ArrayList<Item> usersItemsBySecretsList = new ArrayList<>();
    private ArrayList<Item> usersItemsByCommentsList = new ArrayList<>();
    private ArrayList<Item> usersItemsByVotesList = new ArrayList<>();
    private ArrayList<Item> usersItemsPinnedList = new ArrayList<>();
    private LinkedHashSet<String> setOfSecrets = new LinkedHashSet<>();
    private LinkedHashSet<String> setOfComments = new LinkedHashSet<>();
    private LinkedHashSet<String> setOfVotes = new LinkedHashSet<>();
    private LinkedHashSet<String> setOfPinned = new LinkedHashSet<>();
    private LinkedHashSet<String> setOfCommentsLikes = new LinkedHashSet<>();
    private LinkedHashSet<String> setOfCommentsDislikes = new LinkedHashSet<>();

    public User() {

    }

    public User(String userName, String email, char idType) {
        this.userName = userName;
        this.email = email;
        this.idType = idType;
    }

    public void setIdType(char type) {
        idType = type;
    }

    public void setThemeNumber(int theme) {
        themeNumber = theme;
    }

    public void setNumOfSecrets(int num) {
        this.numOfSecrets = num;
    }

    public void setNumOfComments(int num) {
        this.numOfComments = num;
    }

    public void setNumOfVotes(int num) {
        this.numOfVotes = num;
    }

    public void setNumOfPinned(int num) {
        this.numOfPinned = num;
    }

    public void setSecrets(LinkedHashSet<String> set) {
        setOfSecrets = (LinkedHashSet<String>)set.clone();
}

    public void setVotes(LinkedHashSet<String> set) {
        setOfVotes = (LinkedHashSet<String>)set.clone();
    }

    public void setComments(LinkedHashSet<String> set) {
        setOfComments = (LinkedHashSet<String>)set.clone();
    }

    public void setPinned(LinkedHashSet<String> set) {
        setOfPinned = (LinkedHashSet<String>)set.clone();
    }

    public void setCommentsLikes(LinkedHashSet<String> set) {
        setOfCommentsLikes = (LinkedHashSet<String>)set.clone();
    }

    public void setCommentsDislikes(LinkedHashSet<String> set) {
        setOfCommentsDislikes = (LinkedHashSet<String>)set.clone();
    }

    public void setSecrets(int itemId) {
        setOfSecrets.add(String.valueOf(itemId));
    }

    public void setComments(int itemId) {
        setOfComments.add(String.valueOf(itemId));
    }

    public void setVotes(int itemId) {
        setOfVotes.add(String.valueOf(itemId));
    }

    public void setPinned(int itemId) {
        setOfPinned.add(String.valueOf(itemId));
    }

    public void setCommentsLikes(int itemId) {
        setOfCommentsLikes.add(String.valueOf(itemId));
    }

    public void setCommentsDislikes(int itemId) {
        setOfCommentsDislikes.add(String.valueOf(itemId));
    }

    public String getVotes() {
        return Util.appendItems(setOfVotes, 0, setOfVotes.size() - 1);
    }

    public String getComments() {
        return Util.appendItems(setOfComments, 0, setOfComments.size() - 1);
    }

    public String getSecrets() {
        return Util.appendItems(setOfSecrets, 0, setOfSecrets.size() - 1);
    }

    public String getPinned() {
        return Util.appendItems(setOfPinned, 0, setOfPinned.size() - 1);
    }

    public String getCommentLikes() {
        return Util.appendItems(setOfCommentsLikes, 0, setOfCommentsLikes.size() - 1);
    }

    public String getCommentDislikes() {
        return Util.appendItems(setOfCommentsDislikes, 0, setOfCommentsDislikes.size() - 1);
    }

    public String getSecrets(int start, int end) {
        return Util.appendItems(setOfSecrets, start, end);
    }

    public String getComments(int start, int end) {
        return Util.appendItems(setOfComments, start, end);
    }

    public String getVotes(int start, int end) {
        return Util.appendItems(setOfVotes, start, end);
    }

    public String getPinned(int start, int end) {
        return Util.appendItems(setOfPinned, start, end);
    }

    public String getUserName() {
        return userName;
    }

    public int getNumOfSecrets() {
        return numOfSecrets;
    }

    public int getNumOfVotes() {
        return numOfVotes;
    }

    public int getNumOfPinned() {
        return numOfPinned;
    }

    public int getNumOfCommentLikes() {
        return numOfCommentLikes;
    }

    public int getNumOfCommentDislikes() {
        return numOfCommentDislikes;
    }

    public int getNumOfComments() {
        return numOfComments;
    }

    public void logIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void addSecret() {
        numOfSecrets++;
    }

    public void setCommentsScore(int commentsScore) {
        this.commentsScore = commentsScore;
    }

    public void setSecretsScore(int secretsScore) {
        this.secretsScore = secretsScore;
    }

    public void setNumOfCommentLikes(int numOfCommentLikes) {
        this.numOfCommentLikes = numOfCommentLikes;
    }

    public void setNumOfCommentDislikes(int numOfCommentDislikes) {
        this.numOfCommentDislikes = numOfCommentDislikes;
    }

    public void addVote(int num) {
        numOfVotes += num;
    }

    public void addPinned(int num) {
        numOfPinned += num;
    }

    public void addComment(int num) {
        numOfComments += num;
    }

    public void setNumOfItemsWithComments(int num) {
        numOfItemsWithComments = num;
    }

    public int getThemeNumber() {
        return themeNumber;
    }

    public char getIdType() {
        return idType;
    }

    public String getEmail() {
        return email;
    }

    public int getNumOfItemsWithComments() {
        return numOfItemsWithComments;
    }

    public int getCommentsScore() {
        return commentsScore;
    }

    public int getSecretsScore() {
        return secretsScore;
    }

    public ArrayList<Item> getUsersSecrets() {
        return usersItemsBySecretsList;
    }

    public ArrayList<Item> getUsersComments() {
        return usersItemsByCommentsList;
    }

    public ArrayList<Item> getUsersVotes() {
        return usersItemsByVotesList;
    }

    public ArrayList<Item> getUsersPinned() {
        return usersItemsPinnedList;
    }

    public LinkedHashSet<String> getSetOfSecrets() {
        return setOfSecrets;
    }

    public LinkedHashSet<String> getSetOfComments() {
        return setOfComments;
    }

    public LinkedHashSet<String> getSetOfCommentsLikes() {
        return setOfCommentsLikes;
    }

    public LinkedHashSet<String> getSetOfCommentsDislikes() {
        return setOfCommentsDislikes;
    }


    public LinkedHashSet<String> getSetOfVotes() {
        return setOfVotes;
    }

    public LinkedHashSet<String> getSetOfPinned() {
        return setOfPinned;
    }

    public void setUsersItemsBySecretsList(ArrayList<Item> list) {
        usersItemsBySecretsList = list;
    }

    public void setUsersItemsByCommentsList(ArrayList<Item> list) {
        usersItemsByCommentsList = list;
    }

    public void setUsersItemsByVotesList(ArrayList<Item> list) {
        usersItemsByVotesList = list;
    }

    public void setUsersPinned(ArrayList<Item> list) {
        usersItemsPinnedList = list;
    }
}
