package com.example.elimu_smart.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {
    private SQLiteHelper dbHelper;

    public DBHelper(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void saveProfile(String career, String grades, String time) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("career", career);
        values.put("grades", grades);
        values.put("time", time);
        db.insert("profile", null, values);
        db.close();
    }

    public void savePlan(String planContent) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", planContent);
        db.insert("plan", null, values);
        db.close();
    }

    public String getLatestPlan() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT content FROM plan ORDER BY id DESC LIMIT 1", null);
        String result = "No plan found";
        if (cursor.moveToFirst()) {
            result = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return result;
    }
}
