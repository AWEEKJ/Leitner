package com.uos.leitner.model;

/**
 * Created by Yunha on 2016. 11. 9..
 */

public class Sigmoid {
    private int level;
    private float value;

    public Sigmoid() {
    }

    public Sigmoid(int level, float value) {
        this.level = level;
        this.value = value;
    }

    public int getLevel() {
        return level;
    }

    public float getValue() {
        return value;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setValue(float value) {
        this.value = value;
    }
}