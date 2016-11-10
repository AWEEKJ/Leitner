package com.uos.leitner.model;

/**
 * Created by Yunha on 2016. 11. 4..
 */

public class Category {
    private int subject_ID;
    private String subject_Name;
    private int currentLevel;
    private int maxTime;

    // Constructor

    public Category() {
    }

    public Category(String subject_Name) {
        this.subject_Name = subject_Name;
    }
    public Category(String subject_Name, int currentLevel, int maxTime) {
        this.subject_Name = subject_Name;
        this.currentLevel = currentLevel;
        this.maxTime = maxTime;
    }

    public Category(int subject_ID, String subject_Name, int currentLevel, int maxTime) {
        this.subject_ID = subject_ID;
        this.subject_Name = subject_Name;
        this.currentLevel = currentLevel;
        this.maxTime = maxTime;
    }

    // gettter
    public int getSubject_ID() {
        return subject_ID;
    }

    public String getSubject_Name() {
        return subject_Name;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getMaxTime() {
        return maxTime;
    }

    // setter

    public void setSubject_ID(int subject_ID) {
        this.subject_ID = subject_ID;
    }

    public void setSubject_Name(String subject_Name) {
        this.subject_Name = subject_Name;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }


}