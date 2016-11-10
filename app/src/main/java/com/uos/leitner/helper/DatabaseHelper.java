package com.uos.leitner.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.uos.leitner.model.Category;
import com.uos.leitner.model.Subject_log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Yunha on 2016. 11. 4..
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version - our database schema version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Leitner";

    // Table Names
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_SUBJECT_LOG = "subject_log";
    private static final String TABLE_SIGMOID = "sigmoid";

    // CATEGORY Table - column names
    private static final String KEY_SUBJECT_ID = "subject_ID";
    private static final String KEY_SUBJECT_NAME = "subject_Name";
    private static final String KEY_CURRENT_LEVEL = "current_Level";
    private static final String KEY_MAX_TIME = "max_Time";


    // SUBJECT_LOG Table - column names
    private static final String KEY_LOG_NO = "log_no";
    private static final String KEY_TIME_TO_TRY = "time_to_try";
    private static final String KEY_TIME_TO_COMPLETE = "time_to_complete";
    private static final String KEY_PASS_OR_FAIL = "pass_or_fail";
    private static final String KEY_DATE = "date";
    private static final String KEY_SUBJ_ID = "subject_id";
<<<<<<< HEAD
=======

    // SIGMOID Table - column names
    private static final String KEY_SIG_LEVEL = "sig_level";
    private static final String KEY_SIG_VALUE = "sig_value";
>>>>>>> 3c8760336ea2b3f3df11b66f929930fb5bfc4d14

    // Table create statement
    // CATEGORY table create statement
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY
            + "("
            + KEY_SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_SUBJECT_NAME + " TEXT,"
            + KEY_CURRENT_LEVEL + " INTEGER,"
            + KEY_MAX_TIME + " INTEGER"
            + ")";

    // SUBJECT_LOG table create statement
    private static final String CREATE_TABLE_SUBJECT_LOG = "CREATE TABLE " + TABLE_SUBJECT_LOG
            + "("
            + KEY_LOG_NO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TIME_TO_TRY + " INTEGER,"
            + KEY_TIME_TO_COMPLETE + " INTEGER,"
            + KEY_PASS_OR_FAIL + " INTEGER,"
            + KEY_DATE + " DATETIME, "
            + KEY_SUBJ_ID + " INTEGER"
            + ")";

    // SIGMOID tabale create statement
    private static final String CREATE_TABLE_SIGMOID = "CREATE TABLE " + TABLE_SIGMOID
            + "("
            + KEY_SIG_LEVEL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_SIG_VALUE + " REAL"
            + ")";

    // insert default sigmoid table values
    private static final String INSERT_SIGMOID_VALUE = "INSERT INTO " + TABLE_SIGMOID
            + "(" + KEY_SIG_VALUE + ")"
            + "VALUES "
            + "(";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        double[] sigmoid_values = {0.007, 0.011, 0.018, 0.031, 0.047,
                0.076, 0.120, 0.182, 0.269, 0.378,
                0.500, 0.622, 0.731, 0.818, 0.881,
                0.924, 0.952, 0.971, 0.982, 0.999};

        // creating required tables
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_SUBJECT_LOG);
        db.execSQL(CREATE_TABLE_SIGMOID);

        // sigmoid 레벨과 값들을 초기에 생성시에 넣어줘야 한다.
        for(double value : sigmoid_values) {
            db.execSQL(INSERT_SIGMOID_VALUE + value + ");");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECT_LOG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGMOID);

        // create new tables
        onCreate(db);
    }

    /*
    * Creating a category
    */
    public long createCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT_NAME, category.getSubject_Name());
        values.put(KEY_CURRENT_LEVEL, category.getCurrentLevel());
        values.put(KEY_MAX_TIME, category.getMaxTime());

        // insert row
        long category_id = db.insert(TABLE_CATEGORY, null, values);

        db.close();

        return category_id;

    }

    /*
    * Get a single category
    * */
    public Category getCategory(long category_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY + " WHERE "
                + KEY_SUBJECT_ID + "=" + category_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null) {
            c.moveToFirst();
        }

        Category ct = new Category();
        ct.setSubject_ID(c.getInt(c.getColumnIndex(KEY_SUBJECT_ID)));
        ct.setSubject_Name(c.getString(c.getColumnIndex(KEY_SUBJECT_NAME)));
        ct.setCurrentLevel(c.getInt(c.getColumnIndex(KEY_CURRENT_LEVEL)));
        ct.setMaxTime(c.getInt(c.getColumnIndex(KEY_MAX_TIME)));

        db.close();

        return ct;
    }

    /*
    * Getting all categories
    * */
    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<Category>();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                Category ct = new Category();
                ct.setSubject_ID(c.getInt(c.getColumnIndex(KEY_SUBJECT_ID)));
                ct.setSubject_Name(c.getString(c.getColumnIndex(KEY_SUBJECT_NAME)));
                ct.setCurrentLevel(c.getInt(c.getColumnIndex(KEY_CURRENT_LEVEL)));
                ct.setMaxTime(c.getInt(c.getColumnIndex(KEY_MAX_TIME)));

                // adding to category list
                categories.add(ct);
            } while(c.moveToNext());
        }

        db.close();

        return categories;
    }

    /*
    * Updating a category
    * */
    public int updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT_NAME, category.getSubject_Name());
        values.put(KEY_CURRENT_LEVEL, category.getCurrentLevel());
        values.put(KEY_MAX_TIME, category.getMaxTime());

        db.close();
        // updating row
        return db.update(TABLE_CATEGORY, values, KEY_SUBJECT_ID + " = ?",
                new String[] {String.valueOf(category.getSubject_ID())});
    }

    /*
    * Deleting a category
    * */
    public void deleteCategory(long category_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, KEY_SUBJECT_ID + " = ?",
                new String[] {String.valueOf(category_id)});

        db.close();
    }

    /*
    * Create a Subject_log
    * */
    public long createSubjectLog(Subject_log slog) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TIME_TO_TRY, slog.getTime_to_try());
        values.put(KEY_TIME_TO_COMPLETE, slog.getTime_to_complete());
        values.put(KEY_PASS_OR_FAIL, slog.getPass_or_fail());
        values.put(KEY_DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        values.put(KEY_SUBJ_ID, slog.getSubject_id());

<<<<<<< HEAD
    /*
    * 특정 카테고리에 속하는 subject_log 읽기
    * */
    public List<Subject_log> getSomeSubject_log(long subject_id) {
        List<Subject_log> sl = new ArrayList<Subject_log>();
        String selectQuery = "SELECT * FROM " + TABLE_SUBJECT_LOG + " WHERE SUBJECT_ID = " + subject_id;
=======
        long log_no = db.insert(TABLE_SUBJECT_LOG, null, values);

        db.close();

        return log_no;
    }

    /*
    * 특정 카테고리에 속하는 subject_log 읽기
    * */
    public  ArrayList<Subject_log> getSomeSubject_log(long subject_id) {
        ArrayList<Subject_log> sl = new ArrayList<Subject_log>();
        String selectQuery = "SELECT * FROM " + TABLE_SUBJECT_LOG + " WHERE " + KEY_SUBJ_ID + "=" + subject_id;
>>>>>>> 3c8760336ea2b3f3df11b66f929930fb5bfc4d14

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if(c.moveToFirst()) {
            do {
                Subject_log s = new Subject_log();
                s.setLog_no(c.getInt(c.getColumnIndex(KEY_LOG_NO)));
                s.setTime_to_try(c.getInt(c.getColumnIndex(KEY_TIME_TO_TRY)));
                s.setTime_to_complete(c.getInt(c.getColumnIndex(KEY_TIME_TO_COMPLETE)));
                s.setPass_or_fail(c.getInt(c.getColumnIndex(KEY_PASS_OR_FAIL)));
                s.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
                s.setSubject_id(c.getInt(c.getColumnIndex(KEY_SUBJ_ID)));

                // adding to subject_log list
                sl.add(s);
            } while(c.moveToNext());
        }

<<<<<<< HEAD
        return sl;
    }




=======
        db.close();

        return sl;
    }

    /*
    * 특정 카테고리(maxTime)에 대해 현재 level에 맞는 도전시간 설정하기
    * */
    public double getTryTime(int currentLevel, int maxTime) {

        // 현재 레벨에 대해 maxTime을 기준으로 도전시간을 가져온다.
        double time_to_try = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_SIGMOID + " WHERE "
                + KEY_SIG_LEVEL + "=" + currentLevel;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null) {
            c.moveToFirst();
        }

        time_to_try = (c.getDouble(c.getColumnIndex(KEY_SIG_VALUE))) * (double)maxTime;
        Log.e(LOG, " : "+time_to_try);

        db.close();

        return time_to_try;
    }

    /*
    * Updating a category 수정했습니다
    * */
    public void updateCategory(int id, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT_NAME, newName);

        db.update(TABLE_CATEGORY, values, KEY_SUBJECT_ID + "='"+id+"'", null);

        db.close();
    }
>>>>>>> 3c8760336ea2b3f3df11b66f929930fb5bfc4d14
}