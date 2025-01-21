package com.first.unjumbleit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Name and Version
    public static final String DATABASE_NAME = "game.db";
    public static final int DATABASE_VERSION = 1;

    // Table and Columns
    public static final String TABLE_LEVELS = "levels";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_HINT = "hint";
    public static final String COLUMN_PLANET = "planet";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_LEVEL = "level";

    // Create Table SQL Statement
    private static final String CREATE_TABLE_LEVELS =
            "CREATE TABLE " + TABLE_LEVELS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_WORD + " TEXT NOT NULL, " +
                    COLUMN_HINT + " TEXT NOT NULL, " +
                    COLUMN_PLANET + " TEXT NOT NULL, " +
                    COLUMN_STATE + " INTEGER NOT NULL, " +
                    COLUMN_LEVEL + " INTEGER NOT NULL" +
                    ");";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        Log.d("DatabaseHelper", "DatabaseHelper initialized");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "onCreate: Creating levels table");
        db.execSQL(CREATE_TABLE_LEVELS);
        Log.d("DatabaseHelper", "onCreate: Levels table created");

        Log.d("DatabaseHelper", "onCreate: Loading data from JSON");
        loadDataFromJson(db);
        Log.d("DatabaseHelper", "onCreate: Data loaded from JSON");
    }

    private void loadDataFromJson(SQLiteDatabase db) {
        try {
            Log.d("DatabaseHelper", "loadDataFromJson: Loading JSON file");
            InputStream inputStream = context.getAssets().open("levels_data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            Log.d("DatabaseHelper", "loadDataFromJson: JSON content: " + json);

            JSONObject jsonObject = new JSONObject(json);
            JSONArray levelsArray = jsonObject.getJSONArray("levels");

            Log.d("DatabaseHelper", "loadDataFromJson: Found " + levelsArray.length() + " levels in JSON");

            for (int i = 0; i < levelsArray.length(); i++) {
                JSONObject levelObject = levelsArray.getJSONObject(i);
                String word = levelObject.getString("word");
                String hint = levelObject.getString("hint");
                String planet = levelObject.getString("planet");
                int state = levelObject.getInt("state");
                int level = levelObject.getInt("level");

                Log.d("DatabaseHelper", "loadDataFromJson: Inserting level: " + level + ", word: " + word + ", planet: " + planet);

                String insertQuery = "INSERT INTO " + TABLE_LEVELS + " (" +
                        COLUMN_WORD + ", " +
                        COLUMN_HINT + ", " +
                        COLUMN_PLANET + ", " +
                        COLUMN_STATE + ", " +
                        COLUMN_LEVEL + ") VALUES ('" +
                        word + "', '" +
                        hint + "', '" +
                        planet + "', " +
                        state + ", " +
                        level + ");";
                db.execSQL(insertQuery);
            }
        } catch (IOException e) {
            Log.e("DatabaseHelper", "loadDataFromJson: Error reading JSON file", e);
        } catch (org.json.JSONException e) {
            Log.e("DatabaseHelper", "loadDataFromJson: Error parsing JSON file", e);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELS);
        onCreate(db);
    }
}