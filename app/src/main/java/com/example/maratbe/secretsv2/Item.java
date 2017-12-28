package com.example.maratbe.secretsv2;

import android.graphics.drawable.Icon;
import android.media.Image;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by MARATBE on 12/15/2017.
 */

public class Item
{
    private String user;
    private String text;
    private char type;
    private int itemId;
    private int date;
    private int rating;
    private int numOfComments;
    private ArrayList<Comment> comments = new ArrayList<>();
    private int[] emojis = new int[4];
    private String[] arrayOfTags = new String[3];

    public Item(String user, String text, char type, int itemId, int date, int rating, int numOfComments, String[] tags, int[] emojis)
    {
        setUser(user);
        setText(text);
        setType(type);
        setItemId(itemId);
        setDate(date);
        setRating(rating);
        setNumOfComments(numOfComments);
        setArrayOfTags(tags);
        setEmojis(emojis);
    }

    public Item()
    {

    }

    public void setUser(String user) {

        this.user = user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(char type) {
        this.type = type;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setNumOfComments(int numOfComments) {
        this.numOfComments = numOfComments;
    }

    public void setComments(Comment comment) {
        comments.add(comment);
    }

    public void setArrayOfTags(String[] arrayOfTags)
    {
        for (int i = 0; i < 3; i++)
        {
            this.arrayOfTags[i] = arrayOfTags[i];
        }
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public char getType() {
        return type;
    }

    public int getItemId() {
        return itemId;
    }

    public int getDate() {
        return date;
    }

    public int getRating() {
        return rating;
    }

    public int getNumOfComments() {
        return numOfComments;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public int[] getEmojis() {
        return emojis;
    }

    public String[] getArrayOfTags() {
        return arrayOfTags;
    }

    public void setEmojis(int[] emojis)
    {
        for (int i = 0; i < 4; i++)
        {
            this.emojis[i] = emojis[i];
        }
    }

    public class Comment
    {
        private String text;
        private String dateAdded;

        public Comment(String text, String dateAdded)
        {
            setText(text);
            setDateAdded(dateAdded);
        }

        public String getText() {
            return text;
        }

        public String getDateAdded() {
            return dateAdded;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setDateAdded(String dateAdded) {
            this.dateAdded = dateAdded;
        }
    }
}
