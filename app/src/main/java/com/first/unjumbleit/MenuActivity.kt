package com.first.unjumbleit

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    private lateinit var levelsListView: ListView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        Log.d("MenuActivity", "onCreate: Activity created")

        levelsListView = findViewById(R.id.levelsListView)
        dbHelper = DatabaseHelper(this) // Instantiate DatabaseHelper

        Log.d("MenuActivity", "DatabaseHelper instantiated")

        loadLevels()
    }
    private fun loadLevels() {
        Log.d("MenuActivity", "loadLevels: Loading levels from database")

        // Get a readable database
        val db = dbHelper.readableDatabase
        Log.d("MenuActivity", "Database opened successfully")

        // Query the levels table, aliasing 'id' as '_id'
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_LEVELS,
            arrayOf(
                "${DatabaseHelper.COLUMN_ID} AS _id", // Alias 'id' as '_id'
                DatabaseHelper.COLUMN_LEVEL,
                DatabaseHelper.COLUMN_PLANET
            ),
            null, null, null, null, DatabaseHelper.COLUMN_LEVEL
        )

        Log.d("MenuActivity", "Query executed. Number of levels: ${cursor.count}")

        // Define the columns to display in the ListView
        val fromColumns = arrayOf(DatabaseHelper.COLUMN_LEVEL, DatabaseHelper.COLUMN_PLANET)
        val toViews = intArrayOf(R.id.levelNumber, R.id.planetName)

        // Create a SimpleCursorAdapter to populate the ListView
        val adapter = SimpleCursorAdapter(
            this,
            R.layout.list_item_level, // Ensure this matches the layout file name
            cursor,
            fromColumns,
            toViews,
            0
        )

        Log.d("MenuActivity", "SimpleCursorAdapter created")

        // Set the adapter for the ListView
        levelsListView.adapter = adapter
        Log.d("MenuActivity", "Adapter set for ListView")
    }

    override fun onDestroy() {
        Log.d("MenuActivity", "onDestroy: Activity destroyed")
        dbHelper.close()
        super.onDestroy()
    }
}