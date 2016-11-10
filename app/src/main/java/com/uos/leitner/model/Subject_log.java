package com.uos.leitner.model;


/**
 * Created by Yunha on 2016. 11. 4..
 */

public class Subject_log {
    private int log_no;
    private int time_to_try;
    private int time_to_complete;
    private int pass_or_fail;
    private String date;
    private int subject_id;


    // Constructor
    public Subject_log() {
    }

    public Subject_log(int pass_or_fail, int time_to_try, int time_to_complete) {
        this.pass_or_fail = pass_or_fail;
        this.time_to_try = time_to_try;
        this.time_to_complete = time_to_complete;
    }

    public Subject_log(int time_to_try, int time_to_complete, int pass_or_fail, int subject_id) {
        this.time_to_try = time_to_try;
        this.time_to_complete = time_to_complete;
        this.pass_or_fail = pass_or_fail;
        this.subject_id = subject_id;
    }

    public Subject_log(int log_no, int time_to_try, int time_to_complete, int pass_or_fail, String date, int subject_id) {
        this.log_no = log_no;
        this.time_to_try = time_to_try;
        this.time_to_complete = time_to_complete;
        this.pass_or_fail = pass_or_fail;
        this.date = date;
        this.subject_id = subject_id;
<<<<<<< HEAD
    }
    // getter
=======
    }

    public Subject_log(int time_to_try, int time_to_complete, int pass_or_fail, String date, int subject_id) {
        this.time_to_try = time_to_try;
        this.time_to_complete = time_to_complete;
        this.pass_or_fail = pass_or_fail;
        this.date = date;
        this.subject_id = subject_id;
    }


// getter
>>>>>>> 3c8760336ea2b3f3df11b66f929930fb5bfc4d14

    public int getLog_no() {
        return log_no;
    }

    public int getTime_to_try() {
        return time_to_try;
    }

    public int getTime_to_complete() {
        return time_to_complete;
    }

    public int getPass_or_fail() {
        return pass_or_fail;
    }

    public String getDate() {
        return date;
    }

    public int getSubject_id() {
        return subject_id;
    }
    // setter

    public void setLog_no(int log_no) {
        this.log_no = log_no;
    }

    public void setTime_to_try(int time_to_try) {
        this.time_to_try = time_to_try;
    }

    public void setTime_to_complete(int time_to_complete) {
        this.time_to_complete = time_to_complete;
    }

    public void setPass_or_fail(int pass_or_fail) {
        this.pass_or_fail = pass_or_fail;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }
}
<<<<<<< HEAD
=======

>>>>>>> 3c8760336ea2b3f3df11b66f929930fb5bfc4d14
