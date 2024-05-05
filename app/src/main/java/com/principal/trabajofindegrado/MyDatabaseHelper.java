package com.principal.trabajofindegrado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Habits.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "Habits";
    private static final String COLUMN_ID = "habit_id";
    private static final String COLUMN_TITLE = "habit_name";
    private static final String COLUMN_DIFFICULTY = "difficulty";
    private static final String COLUMN_FREQUENCY = "frequency";
    private static final String COLUMN_START_DATE = "start_date";
    private static final String COLUMN_COMPLETION_STATUS = "completion_status";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_DIFFICULTY + " TEXT NOT NULL, " +
                COLUMN_FREQUENCY + " INTEGER NOT NULL, " +
                COLUMN_START_DATE + " DATE NOT NULL, " +
                COLUMN_COMPLETION_STATUS + " INTEGER NOT NULL);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addHabit(String name, String difficulty, int frequency, String startDate, int completionStatus){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, name);
        cv.put(COLUMN_DIFFICULTY, difficulty);
        cv.put(COLUMN_FREQUENCY, frequency);
        cv.put(COLUMN_START_DATE, startDate);
        cv.put(COLUMN_COMPLETION_STATUS, completionStatus);

        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            Toast.makeText(context, "Error al añadir el hábito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Hábito añadido correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllHabits(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public void updateHabit(String id, String name, String difficulty, int frequency, String startDate, int completionStatus){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, name);
        cv.put(COLUMN_DIFFICULTY, difficulty);
        cv.put(COLUMN_FREQUENCY, frequency);
        cv.put(COLUMN_START_DATE, startDate);
        cv.put(COLUMN_COMPLETION_STATUS, completionStatus);

        int result = db.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Failed to update habit", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Habit updated successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteHabit(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Failed to delete habit", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Habit deleted successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllHabits(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
