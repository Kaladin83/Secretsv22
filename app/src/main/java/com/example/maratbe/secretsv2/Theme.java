package com.example.maratbe.secretsv2;

import android.graphics.Typeface;

/**
 * Created by MARATBE on 12/22/2017.
 */

public class Theme {
    private String name;
    private String text;
    private String radioCircle;
    private int id;
    private int icon;
    private int statusBarColor;
    private int activeBarColor;
    private int navHeaderColor;
    private int titleColor;
    private int selectedTitleColor;
    private int tabsColor;
    private int[] gradientColors = new int[2];
    private Typeface mainTypeface;
    private Typeface titleTypeface;

    public Theme() {

    }

    public Theme(String name, String text, int id, int icon, int statusBarColor, int activeBarColor, int titleColor,String radio,
                 int[] gradientColors, Typeface mainTf, Typeface titleTf) {
        setName(name);
        setText(text);
        setId(id);
        setActiveBarColor(activeBarColor);
        setStatusBarColor(statusBarColor);
        setNavHeaderColor(activeBarColor);
        setIcon(icon);
        setTitleColor(titleColor);
        setSelectedTitleColor(gradientColors[1]);
        setRadioCircle(radio);
        setGradientColors(gradientColors);
        setMainTypeFace(mainTf);
        setTitleTypeFace(titleTf);
        setTabsColor(activeBarColor);
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

    public void setNavHeaderColor(int navHeaderColor) {
        this.navHeaderColor = navHeaderColor;
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

    public void setSelectedTitleColor(int  color) {
        this.selectedTitleColor = color;
    }

    public void setTabsColor(int tabsColor) {
        this.tabsColor = tabsColor;
    }

    public void setGradientColors(int[] gradientColors) {
        for (int i = 0; i < 2; i++) {
            this.gradientColors[i] = gradientColors[i];
        }
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

    public int getNavHeaderColor() {
        return navHeaderColor;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getSelectedTitleColor() {
        return selectedTitleColor;
    }

    public int getTabsColor() {
        return tabsColor;
    }

    public int[] getGradientColors() {
        return gradientColors;
    }

    public Typeface getMainTypeface() {
        return mainTypeface;
    }

    public Typeface getTitleTypeface() {
        return titleTypeface;
    }
}
