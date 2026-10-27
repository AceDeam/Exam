package com.example.exam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "student.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE students (" +
                "student_id TEXT PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "age INTEGER NOT NULL CHECK(age BETWEEN 17 AND 50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS students");
        onCreate(db);
    }

    // ADD
    public boolean insert(String id, String name, int age) {
        ContentValues cv = new ContentValues();
        cv.put("student_id", id);
        cv.put("name", name);
        cv.put("age", age);
        return getWritableDatabase().insert("students", null, cv) != -1;
    }

    // EDIT
    public boolean update(String id, String name, int age) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("age", age);
        return getWritableDatabase().update("students", cv, "student_id=?", new String[]{id}) > 0;
    }

    // DELETE
    public boolean delete(String id) {
        return getWritableDatabase().delete("students", "student_id=?", new String[]{id}) > 0;
    }

    // VIEW
    public Cursor getAll() {
        return getReadableDatabase().rawQuery("SELECT * FROM students", null);
    }
}