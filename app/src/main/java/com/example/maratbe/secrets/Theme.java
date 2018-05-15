package com.example.maratbe.secrets;

import android.graphics.Typeface;

public class Theme {
    private String name;
    private String text;
    private String radioCircle;
    private int id;
    private int icon;
    private int statusBarColor;
    private int activeBarColor;
 //   private int navHeaderColor;
    private int titleColor;
    private int filterButtonColor;
   // private int tabsColor;
    private int[] selectedTitleColor = new int[3];
    private int[] gradientColors = new int[3];
    private int[] drawbles = new int[10];
    private Typeface mainTypeface;
    private Typeface titleTypeface;

    public Theme() {

    }

    public Theme(String name, String text, int id, int icon, int statusBarColor, int activeBarColor, int titleColor, int filterButtonColor, int[] selectedColor, String radio,
                 int[] gradientColors, int[] drawbles, Typeface mainTf, Typeface titleTf) {
        setName(name);
        setText(text);
        setId(id);
        setActiveBarColor(activeBarColor);
        setStatusBarColor(statusBarColor);
        setFilterButtonColor(filterButtonColor);
        setIcon(icon);
        setTitleColor(titleColor);
        setSelectedTitleColor(selectedColor);
        setRadioCircle(radio);
        setDrawbles(drawbles);
        setGradientColors(gradientColors);
        setMainTypeFace(mainTf);
        setTitleTypeFace(titleTf);
        //setTabsColor(activeBarColor);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRadioCircle(String radioCircle) {
        this.radioCircle = radioCircle;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setStatusBarColor(int statusBarColor) {
        this.statusBarColor = statusBarColor;
    }

    public void setActiveBarColor(int ActiveBarColor) {
        this.activeBarColor = ActiveBarColor;
    }

    public void setFilterButtonColor(int filterColor) {
        this.filterButtonColor = filterColor;
    }

    public void setMainTypeFace(Typeface tf) {
        this.mainTypeface = tf;
    }

    public void setTitleTypeFace(Typeface tf) {
        this.titleTypeface = tf;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

  /*  public void setTabsColor(int tabsColor) {
        this.tabsColor = tabsColor;
    }*/

    public void setSelectedTitleColor(int[]  color)
    {
        System.arraycopy(color, 0, selectedTitleColor, 0, color.length);
    }

    public void setGradientColors(int[] gradientColors) {
        System.arraycopy(gradientColors, 0, this.gradientColors, 0, gradientColors.length);
    }

    public void setDrawbles(int[] drawbles) {
        System.arraycopy(drawbles, 0, this.drawbles, 0, drawbles.length);
    }

    public int getId() {
        return id;
    }

    public String getName() {

        return name;
    }

    public String getText() {
        return text;
    }

    public String getRadioCircle() {
        return radioCircle;
    }

    public int getIcon() {
        return icon;
    }

    public int getStatusBarColor() {
        return statusBarColor;
    }

    public int getActiveBarColor() {
        return activeBarColor;
    }

    public int getFilterButtonColor() {
        return filterButtonColor;
    }

    public int getTitleColor() {
        return titleColor;
    }

  /*  public int getTabsColor() {
        return tabsColor;
    }*/

    public int[] getSelectedTitleColor() {
        return selectedTitleColor;
    }

    public int[] getGradientColors() {
        return gradientColors;
    }

    public int[] getDrawbles() {
        return drawbles;
    }

    public Typeface getMainTypeface() {
        return mainTypeface;
    }

    public Typeface getTitleTypeface() {
        return titleTypeface;
    }
}
