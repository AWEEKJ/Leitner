package com.uos.leitner.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.uos.leitner.model.Category;

import java.util.ArrayList;
import java.util.List;

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

    // Table create statement
    // CATEGORY table create statement
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE "
            + TABLE_CATEGORY + "(" + KEY_SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_SUBJECT_NAME + " TEXT," + KEY_CURRENT_LEVEL + " INTEGER,"
            + KEY_MAX_TIME + " INTEGER" + ")";

    // SUBJECT_LOG table create statement
    private static final String CREATE_TABLE_SUBJECT_LOG = "CREATE TABLE "
            + TABLE_SUBJECT_LOG + "(" + KEY_LOG_NO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TIME_TO_TRY + " INTEGER," + KEY_TIME_TO_COMPLETE + " INTEGER,"
            + KEY_PASS_OR_FAIL + " INTEGER," + KEY_DATE + " DATETIME" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_SUBJECT_LOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECT_LOG);

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
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<Category>();
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


}
