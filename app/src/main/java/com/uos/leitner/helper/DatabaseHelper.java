package com.uos.leitner.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.uos.leitner.model.Category;
import com.uos.leitner.model.CategoryCount;
import com.uos.leitner.model.Sigmoid;
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

    // View Table Names
    private static final String VIEW_TABLE_CATEGORY_COUNT = "category_count";

    // CATEGORY Table - column names
    private static final String KEY_SUBJECT_ID = "subject_ID";
    private static final String KEY_SUBJECT_NAME = "subject_Name";
    private static final String KEY_CURRENT_LEVEL = "current_Level";
    private static final String KEY_MAX_TIME = "max_Time";
    private static final String KEY_TIMER_MODE = "timer_mode";

    // SUBJECT_LOG Table - column names
    private static final String KEY_LOG_NO = "log_no";
    private static final String KEY_TIME_TO_TRY = "time_to_try";
    private static final String KEY_TIME_TO_COMPLETE = "time_to_complete";
    private static final String KEY_PASS_OR_FAIL = "pass_or_fail";
    private static final String KEY_DATE = "date";
    private static final String KEY_SUBJ_ID = "subject_id";

    // SIGMOID Table - column names
    private static final String KEY_SIG_LEVEL = "sig_level";
    private static final String KEY_SIG_VALUE = "sig_value";

    // CATEGORY_COUNT Table - colume name
    private static final String KEY_VIEW_SUBJECT_NAME = "subject_Name";
    private static final String KEY_VIEW_SUBJECT_COUNT = "subject_Count";


    // Table create statement
    // CATEGORY table create statement
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY
            + "("
            + KEY_SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_SUBJECT_NAME + " TEXT,"
            + KEY_CURRENT_LEVEL + " INTEGER,"
            + KEY_MAX_TIME + " INTEGER,"
            + KEY_TIMER_MODE + " INTEGER"
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

    // CATEGORY_COUNT view table create statement
    private static final String CREATE_VIEW_TABLE_CATEGORY_COUNT = "CREATE VIEW "
            + VIEW_TABLE_CATEGORY_COUNT
            + " AS "
            + "SELECT "
            + KEY_VIEW_SUBJECT_NAME
            + ", COUNT("
            + KEY_VIEW_SUBJECT_NAME
            + ") AS "
            + KEY_VIEW_SUBJECT_COUNT
            + " FROM "
            + TABLE_SUBJECT_LOG + " s,"
            + TABLE_CATEGORY + " c"
            + " WHERE s.subject_id = c.subject_id"
            + " GROUP BY "
            + KEY_SUBJECT_NAME;

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
        db.execSQL(CREATE_VIEW_TABLE_CATEGORY_COUNT);

        // sigmoid 레벨과 값들을 초기에 생성시에 넣어줘야 한다.
        for(double value : sigmoid_values) {
            db.execSQL(INSERT_SIGMOID_VALUE + value + ");");
        }

        db.execSQL("insert into category (subject_Name, current_Level, max_Time) values ('STUDY', 7, 60);");
        db.execSQL("insert into category (subject_Name, current_Level, max_Time) values ('CODING', 3, 40);");
        db.execSQL("insert into category (subject_Name, current_Level, max_Time) values ('CAPSTON', 8, 50);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (25,25,1,1);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (40,30,0,1);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (25,25,1,1);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (40,40,1,1);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (65,65,1,1);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (112,112,1,1);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (169,169,1,1);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (274,274,1,1);");

        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (74,74,1,2);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (113,113,1,2);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (182,140,0,2);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (113,113,1,2);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (182,182,1,2);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (288,240,0,2);");

        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (93,60,0,3);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (54,54,0,3);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (93,93,0,3);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (141,141,0,3);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (228,199,0,3);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (141,141,0,3);");
        db.execSQL("insert into subject_log (time_to_try, time_to_complete, pass_or_fail, subject_id) values (288,288,0,3);");
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
        values.put(KEY_TIMER_MODE, category.getMode());

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
        ct.setMode(c.getInt(c.getColumnIndex(KEY_TIMER_MODE)));

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
                ct.setMode(c.getInt(c.getColumnIndex(KEY_TIMER_MODE)));

                // adding to category list
                categories.add(ct);
            } while(c.moveToNext());
        }

        db.close();

        return categories;
    }

    public int updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT_ID, category.getSubject_ID());
        values.put(KEY_SUBJECT_NAME, category.getSubject_Name());
        values.put(KEY_CURRENT_LEVEL, category.getCurrentLevel());
        values.put(KEY_MAX_TIME, category.getMaxTime());
        values.put(KEY_TIMER_MODE, category.getMode());

        return db.update(TABLE_CATEGORY, values, KEY_SUBJECT_ID + " = ?",
                new String[] { String.valueOf(category.getSubject_ID()) });
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


        db.close();

        return sl;
    }

    /*
    * 모든 subject_log 가져오기
    * */
    public ArrayList<Subject_log> getAllSubject_log() {
        ArrayList<Subject_log> sl = new ArrayList<Subject_log>();
        String selectQuery = "SELECT * FROM " + TABLE_SUBJECT_LOG;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
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


        db.close();

        return sl;
    }

    /*
    * 특정 카테고리(maxTime)에 대해 현재 level에 맞는 도전시간 설정하기
    * */
    public long getTryTime(int currentLevel, double maxTime) {

        // 현재 레벨에 대해 maxTime을 기준으로 도전시간을 가져온다.
        long time_to_try = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_SIGMOID + " WHERE "
                + KEY_SIG_LEVEL + "=" + currentLevel;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null) {
            c.moveToFirst();
        }

        time_to_try = (long)(c.getDouble(c.getColumnIndex(KEY_SIG_VALUE)) * maxTime);
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

    /*
    * 모든 sigmoid 가져오기
    * */
    public ArrayList<Sigmoid> getAllSigmoid() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_SIGMOID;
        ArrayList<Sigmoid> sds = new ArrayList<Sigmoid>();

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                Sigmoid sm = new Sigmoid();
                sm.setLevel(c.getInt(c.getColumnIndex(KEY_SIG_LEVEL)));
                sm.setValue(c.getFloat(c.getColumnIndex(KEY_SIG_VALUE)));

                // adding to subject_log list
                sds.add(sm);
            } while(c.moveToNext());
        }

        db.close();

        return sds;


    }

    /*
    * 개별 카테고리별 총 시도횟수 가져오기
    * */
    public int getTryCount(int id) {
        int count = 0;
        String selectQuery = "SELECT COUNT(*) FROM "
                + TABLE_SUBJECT_LOG
                + " WHERE "
                + KEY_SUBJ_ID + "=" + id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            count = c.getInt(0);
            c.close();
        }

        db.close();

        return count;
    }

    /*
    * 특정 카테고리 누적시간 가져오기
    * */
    public int getSumTime(int id) {
        int sum = 0;
        String selectQuery = "SELECT SUM(time_to_try) FROM "
                + TABLE_SUBJECT_LOG
                + " WHERE "
                + KEY_SUBJ_ID + "=" + id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            sum = c.getInt(0);
            c.close();
        }

        db.close();

        return sum;
    }

    /*
    * Subject_log에 있는 모든 로그의 개수 가져오기
    * */
    public int totalLogCount() {
        int count = 0;
        String selectQuery = "SELECT COUNT(*) FROM "
                + TABLE_SUBJECT_LOG;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            count = c.getInt(0);
            c.close();
        }

        db.close();

        return count;
    }

    /*
    *  Category_count에 있는 모든 데이터 가져오기
    * */
    public ArrayList<CategoryCount> getAllCategoryCount() {


        ArrayList<CategoryCount> categoriesCount = new ArrayList<CategoryCount>();
        String selectQuery = "SELECT * FROM " + VIEW_TABLE_CATEGORY_COUNT;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);


        if(c.moveToFirst()) {
            do {
                CategoryCount cc = new CategoryCount();
                cc.setSubject_Name(c.getString(c.getColumnIndex(KEY_VIEW_SUBJECT_NAME)));
                cc.setSubject_count(c.getInt(c.getColumnIndex(KEY_VIEW_SUBJECT_COUNT)));

                // adding to category list
                categoriesCount.add(cc);
            } while(c.moveToNext());
        }


        db.close();

        return categoriesCount;
    }


    /*
    * 개별 카테고리별 성공횟수 가져오기
    * */
    public int getSuccessCount(int id) {
        int count = 0;
        String selectQuery = "SELECT COUNT(*) FROM "
                + TABLE_SUBJECT_LOG
                + " WHERE "
                + KEY_PASS_OR_FAIL + "= 1 AND "
                + KEY_SUBJ_ID + "=" + id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            count = c.getInt(0);
            c.close();
        }

        db.close();

        return count;

    }

}