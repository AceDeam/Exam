package com.example.exam;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etName, etId;
    SeekBar seekAge;
    TextView tvAge, tvResult;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etName);
        etId = findViewById(R.id.etId);
        seekAge = findViewById(R.id.seekAge);
        tvAge = findViewById(R.id.tvAge);
        tvResult = findViewById(R.id.tvResult);

        // Slider: 0-33 shown as age 17-50
        seekAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
                tvAge.setText("Age: " + (progress + 17));
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });

        // ADD
        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            if (isEmpty()) return;
            boolean ok = db.insert(id(), name(), age());
            toast(ok ? "Student added" : "ID already exists");
            showAll();
        });

        // EDIT
        findViewById(R.id.btnEdit).setOnClickListener(v -> {
            if (isEmpty()) return;
            boolean ok = db.update(id(), name(), age());
            toast(ok ? "Student updated" : "ID not found");
            showAll();
        });

        // DELETE
        findViewById(R.id.btnDelete).setOnClickListener(v -> {
            boolean ok = db.delete(id());
            toast(ok ? "Student deleted" : "ID not found");
            showAll();
        });

        // VIEW
        findViewById(R.id.btnView).setOnClickListener(v -> showAll());
    }

    String name() { return etName.getText().toString().trim(); }
    String id()   { return etId.getText().toString().trim(); }
    int age()     { return seekAge.getProgress() + 17; }

    boolean isEmpty() {
        if (name().isEmpty() || id().isEmpty()) {
            toast("Name and ID are required");
            return true;
        }
        return false;
    }

    void showAll() {
        Cursor c = db.getAll();
        StringBuilder sb = new StringBuilder();
        while (c.moveToNext()) {
            sb.append(c.getString(0)).append(" | ")
                    .append(c.getString(1)).append(" | ")
                    .append(c.getInt(2)).append("\n");
        }
        c.close();
        tvResult.setText(sb.length() == 0 ? "No data" : sb.toString());
    }

    void toast(String m) {
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
    }
}