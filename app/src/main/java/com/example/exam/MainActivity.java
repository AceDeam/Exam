package com.example.exam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Database
    static class DB extends SQLiteOpenHelper {
        DB(Context c) { super(c, "student.db", null, 1); }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE students(id TEXT PRIMARY KEY, name TEXT, age INTEGER)");
        }
        public void onUpgrade(SQLiteDatabase db, int o, int n) {}
    }

    EditText etName, etId;
    SeekBar seekAge;
    TextView tvAge, tvResult;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        db = new DB(this).getWritableDatabase();

        etName = findViewById(R.id.etName);
        etId = findViewById(R.id.etId);
        seekAge = findViewById(R.id.seekAge);
        tvAge = findViewById(R.id.tvAge);
        tvResult = findViewById(R.id.tvResult);

        seekAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar s, int p, boolean u) { tvAge.setText("Age: " + (p + 17)); }
            public void onStartTrackingTouch(SeekBar s) {}
            public void onStopTrackingTouch(SeekBar s) {}
        });

        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            if (empty()) return;
            long r = db.insert("students", null, values());
            msg(r == -1 ? "ID already exists" : "Student added");
            show();
        });

        findViewById(R.id.btnEdit).setOnClickListener(v -> {
            if (empty()) return;
            int r = db.update("students", values(), "id=?", new String[]{etId.getText().toString()});
            msg(r > 0 ? "Student updated" : "ID not found");
            show();
        });

        findViewById(R.id.btnDelete).setOnClickListener(v -> {
            int r = db.delete("students", "id=?", new String[]{etId.getText().toString()});
            msg(r > 0 ? "Student deleted" : "ID not found");
            show();
        });

        findViewById(R.id.btnView).setOnClickListener(v -> show());
    }

    ContentValues values() {
        ContentValues cv = new ContentValues();
        cv.put("id", etId.getText().toString());
        cv.put("name", etName.getText().toString());
        cv.put("age", seekAge.getProgress() + 17);   // 17 - 50
        return cv;
    }

    boolean empty() {
        boolean e = etName.getText().toString().isEmpty() || etId.getText().toString().isEmpty();
        if (e) msg("Name and ID are required");
        return e;
    }

    void show() {
        Cursor c = db.rawQuery("SELECT * FROM students", null);
        StringBuilder sb = new StringBuilder();
        while (c.moveToNext())
            sb.append(c.getString(0)).append(" | ").append(c.getString(1))
                    .append(" | ").append(c.getInt(2)).append("\n");
        c.close();
        tvResult.setText(sb.length() == 0 ? "No data" : sb.toString());
    }

    void msg(String m) { Toast.makeText(this, m, Toast.LENGTH_SHORT).show(); }
}