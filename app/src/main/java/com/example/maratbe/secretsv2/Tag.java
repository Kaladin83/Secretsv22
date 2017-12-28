package com.example.maratbe.secretsv2;

/**
 * Created by MARATBE on 12/28/2017.
 */

public class Tag
{
    private int tagId;
    private String tagName;

    public Tag(int tagId, String tagName)
    {
        setTagId(tagId);
        setTagName(tagName);
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
