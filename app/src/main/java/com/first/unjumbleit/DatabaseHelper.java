package com.first.unjumbleit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Name and Version
    public static final String DATABASE_NAME = "game.db";
    public static final int DATABASE_VERSION = 1;

    // Table and Columns
    public static final String TABLE_LEVELS = "levels";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_HINT = "hint";

    // Create Table SQL Statement
    private static final String CREATE_TABLE_LEVELS =
            "CREATE TABLE " + TABLE_LEVELS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_WORD + " TEXT NOT NULL, " +
                    COLUMN_HINT + " TEXT NOT NULL" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_TABLE_LEVELS);

        // Insert initial data into the levels table
        db.execSQL("INSERT INTO " + TABLE_LEVELS + " (" + COLUMN_WORD + ", " + COLUMN_HINT + ") VALUES ('APPLE', 'A fruit');");
        db.execSQL("INSERT INTO " + TABLE_LEVELS + " (" + COLUMN_WORD + ", " + COLUMN_HINT + ") VALUES ('TABLE', 'Furniture');");
        db.execSQL("INSERT INTO " + TABLE_LEVELS + " (" + COLUMN_WORD + ", " + COLUMN_HINT + ") VALUES ('HOUSE', 'A place to live');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELS);
        // Recreate table
        onCreate(db);
    }
}
