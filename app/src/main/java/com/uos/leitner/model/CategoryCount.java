package com.uos.leitner.model;

/**
 * Created by JungJee on 2016. 12. 2..
 */

public class CategoryCount {
    private String subject_Name;
    private int subject_count;

    public CategoryCount() {
    }

    public CategoryCount(String subject_Name, int subject_count) {
        this.subject_Name = subject_Name;
        this.subject_count = subject_count;
    }

    public String getSubject_Name() {
        return subject_Name;
    }

    public void setSubject_Name(String subject_Name) {
        this.subject_Name = subject_Name;
    }

    public int getSubject_count() {
        return subject_count;
    }

    public void setSubject_count(int subject_count) {
        this.subject_count = subject_count;
    }
}
