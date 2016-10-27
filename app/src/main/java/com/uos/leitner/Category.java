package com.uos.leitner;

/**
 * Created by changhyeon on 2016. 10. 27..
 */

class Category {
    private String Name;
    private String Time;

    public Category(String Name, String Time) {
        this.Name = Name;
        this.Time = Time;
    }

    public String getName() {
        return Name;
    }

    public String getTime() {
        return Time;
    }
}
