package com.example.maratbe.secrets;

import java.util.ArrayList;

public class Item
{
    private String user;
    private String text;
    private String title;
    private String date;
    private int itemId;
    private int rating;
    private int score;
    private int numOfComments;
    private int numOfVotes;
    private ArrayList<Comment> comments = new ArrayList<>();
    private String[] arrayOfTags = new String [3];
    private Votes votes;

    public Item(String user, String text, String title, int itemId, String date, int rating, int score, int numOfComments, int numOfVotes, String[] tags)
    {
        setUser(user);
        setText(text);
        setTitle(title);
        setItemId(itemId);
        setDate(date);
        setRating(rating);
        setScore(score);
        setNumOfComments(numOfComments);
        setNumOfVotes(numOfVotes);
        setArrayOfTags(tags);
    }

    public Item()
    {

    }

    public void setVotes(Votes votes)
    {
        this.votes = votes;
    }

    public Votes getVotes()
    {
        return votes;
    }

    public void setUser(String user) {

        this.user = user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setScore( int score) {
        this.score = score;
    }

    public void setNumOfComments(int numOfComments) {
        this.numOfComments = numOfComments;
    }

    public void setNumOfVotes(int numOfVotes) {
        this.numOfVotes = numOfVotes;
    }

    public void addComment(int index, Comment comment) {
        comments.add(index, comment);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void setComment(int index, Comment comment) {
        comments.set(index, comment);
    }

    public void setArrayOfTags(String[] arrayOfTags)
    {
        System.arraycopy(arrayOfTags, 0, this.arrayOfTags, 0, 3);
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public int getItemId() {
        return itemId;
    }

    public String getDate() {
        return date;
    }

    public int getRating() {
        return rating;
    }

    public int getScore() {
        return score;
    }

    public int getNumOfComments() {
        return numOfComments;
    }

    public int getNumOfVotes() {
        return numOfVotes;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public String[] getArrayOfTags() {
        return arrayOfTags;
    }

    public class Comment
    {
        private String text;
        private int commentId;
        private int stars;
        private int likes;
        private int dislikes;
        private String dateAdded;
        private String userName;

        public Comment(String text, int stars, String dateAdded, String userName)
        {
            setText(text);
            setStars(stars);
            setDateAdded(dateAdded);
            setUserName(userName);
        }

        public String getText() {
            return text;
        }

        public int getStars() {
            return stars;
        }

        public int getCommentId()
        {
            return commentId;
        }

        public int getLikes() {
            return likes;
        }

        public int getDislikes() {
            return dislikes;
        }

        public String getDateAdded() {
            return dateAdded;
        }

        public String getUserName() {
            return userName;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setStars(int stars) {
            this.stars = stars;
        }

        public void setCommentId(int commentId)
        {
            this.commentId = commentId;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public void setDislikes(int disLikes) {
            this.dislikes = disLikes;
        }

        public void setDateAdded(String dateAdded) {
            this.dateAdded = dateAdded;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }


    }

    public class Votes {
        private ArrayList<String> likes = new ArrayList<>();
        private ArrayList<String> dislikes = new ArrayList<>();
        private ArrayList<String> sad = new ArrayList<>();
        private ArrayList<String> angry = new ArrayList<>();
        private ArrayList<String> lol = new ArrayList<>();

        public ArrayList<String> getLikes() {
            return likes;
        }

        public void setLikes(ArrayList<String> likes) {
            this.likes = likes;
        }

        public ArrayList<String> getDislikes() {
            return dislikes;
        }

        public void setDislikes(ArrayList<String> dislikes) {
            this.dislikes = dislikes;
        }

        public ArrayList<String> getSad() {
            return sad;
        }

        public void setSad(ArrayList<String> sad) {
            this.sad = sad;
        }

        public ArrayList<String> getAngry() {
            return angry;
        }

        public void setAngry(ArrayList<String> angry) {
            this.angry = angry;
        }

        public ArrayList<String> getLol() {
            return lol;
        }

        public void setLol(ArrayList<String> lol) {
            this.lol = lol;
        }
    }
}
