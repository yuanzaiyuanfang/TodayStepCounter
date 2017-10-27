package com.today.step.lib;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来记录当天步数列表，传感器回调30次记录一条数据
 * Created by jiahongfei on 2017/10/9.
 */

public class TodayStepDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "TodayStepDBHelper";

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "TodayStepDB.db";
    private static final String TABLE_NAME = "TodayStepData";
    private static final String TABLE_NAME_ALL = "AllStepData";
    private static final String PRIMARY_KEY = "_id";
    public static final String TODAY = "today";
    public static final String DATE = "date";
    public static final String STEP = "step";

    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TODAY + " TEXT, "
            + DATE + " long, "
            + STEP + " long);";
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SQL_QUERY_TODAY_ALL = "SELECT * FROM " + TABLE_NAME;
    private static final String SQL_QUERY_STEP = "SELECT * FROM " + TABLE_NAME + " WHERE " + TODAY + " = ? AND " + STEP + " = ?";
    private static final String SQL_QUERY_ALL = "SELECT * FROM " + TABLE_NAME_ALL;

    private static final String SQL_CREATE_TABLE_ALL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_ALL + " ("
            + PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TODAY + " TEXT, "
            + STEP + " long);";

    public TodayStepDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Logger.e(TAG, SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE_ALL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTable();
        onCreate(db);
    }

    /**
     * @param todayStepData 本次步数是否存在
     * @return
     */
    public synchronized boolean isExist(TodayStepData todayStepData) {
        Cursor cursor = getReadableDatabase().rawQuery(SQL_QUERY_STEP, new String[]{todayStepData.getToday(), todayStepData.getStep() + ""});
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        return exist;
    }

    public synchronized void createTable() {
        getWritableDatabase().execSQL(SQL_CREATE_TABLE);
    }

    /**
     * @param todayStepData 插入每次的步数
     */
    public synchronized void insert(TodayStepData todayStepData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODAY, todayStepData.getToday());
        contentValues.put(DATE, todayStepData.getDate());
        contentValues.put(STEP, todayStepData.getStep());
        getWritableDatabase().insert(TABLE_NAME, null, contentValues);
    }

    /**
     * @param allStepData 插入当日总步数
     */
    public synchronized void insertTodayAll(AllStepData allStepData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODAY, allStepData.getDay());
        contentValues.put(STEP, allStepData.getStep());
        getWritableDatabase().insert(TABLE_NAME_ALL, null, contentValues);
    }

    /**
     * @return 获取当日步数集合
     */
    public synchronized List<TodayStepData> getQueryTodayAll() {
        Cursor cursor = getReadableDatabase().rawQuery(SQL_QUERY_TODAY_ALL, new String[]{});
        List<TodayStepData> todayStepDatas = new ArrayList<>();
        while (cursor.moveToNext()) {
            String today = cursor.getString(cursor.getColumnIndex(TODAY));
            long date = cursor.getLong(cursor.getColumnIndex(DATE));
            long step = cursor.getLong(cursor.getColumnIndex(STEP));
            TodayStepData todayStepData = new TodayStepData();
            todayStepData.setToday(today);
            todayStepData.setDate(date);
            todayStepData.setStep(step);
            todayStepDatas.add(todayStepData);
        }
        cursor.close();
        return todayStepDatas;
    }

    /**
     * @return 获取历史步数集合
     */
    public synchronized List<AllStepData> getQueryAll() {
        Cursor cursor = getReadableDatabase().rawQuery(SQL_QUERY_ALL, new String[]{});
        List<AllStepData> allStepList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String today = cursor.getString(cursor.getColumnIndex(TODAY));
            long step = cursor.getLong(cursor.getColumnIndex(STEP));
            AllStepData allStepData = new AllStepData(today, step + "");
            allStepList.add(allStepData);
        }
        cursor.close();
        return allStepList;
    }

    /**
     * 日表每天删除
     */
    public synchronized void deleteTable() {
        getWritableDatabase().execSQL(SQL_DELETE_TABLE);
    }
}
