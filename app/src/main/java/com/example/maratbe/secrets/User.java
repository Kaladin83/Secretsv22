package com.example.maratbe.secrets;

import java.util.ArrayList;

public class User implements Constants{
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
    private String secrets = "";
    private String comments = "";
    private String votes = "";
    private String pinned = "";
    private String commentLikes = "";
    private String commentDislikes = "";
    private boolean loggedIn = false;
    private ArrayList<Item> usersItemsBySecretsList = new ArrayList<>();
    private ArrayList<Item> usersItemsByCommentsList = new ArrayList<>();
    private ArrayList<Item> usersItemsByVotesList = new ArrayList<>();
    private ArrayList<Item> usersItemsPinnedList = new ArrayList<>();

    public User(){

    }

    public User(String userName, String email, char idType)
    {
        this.userName = userName;
        this.email = email;
        this.idType = idType;
    }

    public User(String userName, int numOfVotes, int numOfComments, int numOfPinned, int numOfSecrets, int numOfCommentLikes, int numOfCommentDislikes, String secrets,
                String comments, String votes, String pinned, String commentLikes, String commentDislikes, int themeNumber, int commentsScore, int secretsScore)
    {
        this.userName = userName;
        this.numOfVotes = numOfVotes;
        this.numOfComments = numOfComments;
        this.numOfSecrets = numOfSecrets;
        this.numOfPinned = numOfPinned;
        this.numOfCommentLikes = numOfCommentLikes;
        this.numOfCommentDislikes = numOfCommentDislikes;
        this.secrets = secrets;
        this.comments = comments;
        this.votes = votes;
        this.pinned = pinned;
        this.commentLikes = commentLikes;
        this.commentDislikes = commentDislikes;
        this.themeNumber = themeNumber;
        this.commentsScore = commentsScore;
        this.secretsScore = secretsScore;
    }

    public void setIdType(char type)
    {
        idType = type;
    }
    public void setThemeNumber (int theme)
    {
        themeNumber = theme;
    }
    public void setSecrets(String secrets) {
        this.secrets  = secrets;
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

    public void setSecrets(int itemId) {
        if (numOfSecrets == 1)
        {
            this.secrets += itemId;
        }
        else
        {
            if ((numOfSecrets % MAX_ITEMS_SHOWS) == 1) {
                this.secrets += ":"+ itemId;
            }
            else {
                this.secrets += "," + itemId;
            }
        }
    }

    public void setPinned(int itemId) {
        if (numOfPinned == 1)
        {
            this.pinned += itemId;
        }
        else
        {
            if ((numOfPinned % MAX_ITEMS_SHOWS) == 1) {
                this.pinned += ":"+ itemId;
            }
            else {
                this.pinned += "," + itemId;
            }
        }
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setComments(int itemId) {
        if (numOfItemsWithComments == 1)
        {
            this.comments += itemId;
        }
        else
        {
            if (numOfItemsWithComments % MAX_ITEMS_SHOWS == 1) {
                this.comments += ":" + itemId;
            }
            else {
                this.comments += "," + itemId;
            }
        }
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public void setPinned(String pinned) {
        this.pinned = pinned;
    }

    public void setCommentLikes(int commentId) {
        if (numOfCommentLikes == 1)
        {
            this.commentLikes += commentId;
        }
        else
        {
            this.commentLikes += "," + commentId;
        }
    }

    public void setCommentLikes(String commentLikes) {
        this.commentLikes = commentLikes;
    }

    public void setCommentDislikes(int commentId) {
        if (numOfCommentDislikes == 1)
        {
            this.commentDislikes += commentId;
        }
        else
        {
            this.commentDislikes += "," + commentId;
        }
    }

    public void setCommentDislikes(String commentDislikes) {
        this.commentDislikes = commentDislikes;
    }

    public void setVotes(int itemId) {
        if (numOfVotes == 1)
        {
            this.votes += itemId;
        }
        else
        {
            if (numOfVotes % MAX_ITEMS_SHOWS == 1) {
                this.votes += ":" + itemId;
            }
            else {
                this.votes += "," + itemId;
            }
        }
    }

    public String getVotes() {
        return votes;
    }

    public String getCommentLikes() {
        return commentLikes;
    }

    public String getCommentDislikes() {
        return commentDislikes;
    }

    public String getVotes(int i)
    {
        String[] votesArr = votes.split(":");
        if (i < votesArr.length)
        {
            return votesArr[i];
        }
        return votesArr[votesArr.length - 1];
    }

    public String getPinned(int i)
    {
        String[] pinnedArr = pinned.split(":");
        if (i < pinnedArr.length)
        {
            return pinnedArr[i];
        }
        return pinnedArr[pinnedArr.length - 1];
    }

    public String getComments() {
        return comments;
    }

    public String getComments(int i)
    {
        String[] commentsArr = comments.split(":");
        if (i < commentsArr.length)
        {
            return commentsArr[i];
        }
        return commentsArr[commentsArr.length - 1];
    }

    public String getSecrets() {
        return secrets;
    }

    public String getPinned() {
        return pinned;
    }

    public String getUserName() {
        return userName;
    }

    public int getNumOfSecrets()
    {
        return numOfSecrets;
    }
    public String getSecrets(int i)
    {
        String[] secretsArr = secrets.split(":");
        if (i < secretsArr.length)
        {
            return secretsArr[i];
        }
        return secretsArr[secretsArr.length - 1];
    }

    public int getNumOfVotes()
    {
        return numOfVotes;
    }

    public int getNumOfPinned()
    {
        return numOfPinned;
    }

    public int getNumOfCommentLikes()
    {
        return numOfCommentLikes;
    }

    public int getNumOfCommentDislikes()
    {
        return numOfCommentDislikes;
    }

    public int getNumOfComments()
    {
        return numOfComments;
    }

    public void logIn(boolean loggedIn)
    {
        this.loggedIn = loggedIn;
    }

    public boolean isLoggedIn()
    {
        return loggedIn;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void addSecret()
    {
        numOfSecrets ++;
    }

    public void addVote()
    {
        numOfVotes ++;
    }

    public void addPinned()
    {
        numOfPinned ++;
    }

    public void setCommentsScore(int commentsScore) {
        this.commentsScore = commentsScore;
    }

    public void setSecretsScore(int secretsScore) {
        this.secretsScore = secretsScore;
    }

    public void setNumOfCommentLikes(int numOfCommentLikes)
    {
        this.numOfCommentLikes  = numOfCommentLikes;
    }

    public void setNumOfCommentDislikes(int numOfCommentDislikes)
    {
        this.numOfCommentDislikes  = numOfCommentDislikes;
    }

    public void addVote(int num)
    {
        numOfVotes += num;
    }

    public void addPinned(int num)
    {
        numOfPinned += num;
    }

    public void addComment()
    {
        numOfComments ++;
    }

    public void addComment(int num)
    {
        numOfComments += num;
    }

    public void addNumOfItemsWithComments()
    {
        numOfItemsWithComments ++;
    }

    public int getThemeNumber()
    {
        return themeNumber;
    }

    public char getIdType()
    {
        return idType;
    }
    public String getEmail()
    {
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

    public ArrayList<Item> getUsersSecrets()
    {
        return  usersItemsBySecretsList;
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


    public void setUsersItemsBySecretsList(ArrayList<Item> list)
    {
        usersItemsBySecretsList = list;
    }

    public void setUsersItemsByCommentsList(ArrayList<Item> list)
    {
        usersItemsByCommentsList = list;
    }

    public void setUsersItemsByVotesList(ArrayList<Item> list)
    {
        usersItemsByVotesList = list;
    }

    public void setUsersItemsPinnedList(ArrayList<Item> list)
    {
        usersItemsPinnedList = list;
    }
}
