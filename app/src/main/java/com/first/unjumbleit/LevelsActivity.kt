package com.first.unjumbleit

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity

class LevelsActivity : AppCompatActivity() {

    private lateinit var levelsListView: ListView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        if (MusicManager.isMusicOn()) {
            MusicManager.startMusic(this)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_levels)

        Log.d("LevelsActivity", "onCreate: Activity created")

        // Initialize views and database helper
        levelsListView = findViewById(R.id.levelsListView)
        dbHelper = DatabaseHelper(this)

        // Get the planet name from the intent
        val planetName = intent.getStringExtra("PLANET_NAME") ?: "Multiverse" // Default to "Multiverse" if not provided
        Log.d("LevelsActivity", "Planet name received: $planetName")

        // Load levels for the selected planet
        loadLevels(planetName)

        // Set up item click listener for the ListView
        levelsListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, id ->
            // Handle item click
            onLevelClicked(id, planetName)
        }
    }

    private fun loadLevels(planetName: String) {
        Log.d("LevelsActivity", "loadLevels: Loading levels for planet: $planetName")

        // Get a readable database
        val db = dbHelper.readableDatabase

        // Query the levels table for the selected planet
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_LEVELS,
            arrayOf(
                "${DatabaseHelper.COLUMN_ID} AS _id", // Alias 'id' as '_id'
                DatabaseHelper.COLUMN_LEVEL,
                DatabaseHelper.COLUMN_PLANET,
                DatabaseHelper.COLUMN_WORD, // Include word column
                DatabaseHelper.COLUMN_HINT  // Include hint column
            ),
            "${DatabaseHelper.COLUMN_PLANET} = ?",
            arrayOf(planetName), // Filter by planet name
            null,
            null,
            DatabaseHelper.COLUMN_LEVEL
        )

        Log.d("LevelsActivity", "Query executed. Number of levels: ${cursor.count}")

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

        Log.d("LevelsActivity", "SimpleCursorAdapter created")

        // Set the adapter for the ListView
        levelsListView.adapter = adapter
        Log.d("LevelsActivity", "Adapter set for ListView")
    }

    private fun onLevelClicked(levelId: Long, planetName: String) {
        Log.d("LevelsActivity", "onLevelClicked: Level ID = $levelId, Planet = $planetName")

        // Get the word and hint for the selected level
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_LEVELS,
            arrayOf(DatabaseHelper.COLUMN_WORD, DatabaseHelper.COLUMN_HINT),
            "${DatabaseHelper.COLUMN_ID} = ?",
            arrayOf(levelId.toString()),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            val word = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WORD))
            val hint = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HINT))

            Log.d("LevelsActivity", "Word: $word, Hint: $hint")

            // Start the GameActivity and pass the level data
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra("WORD", word)
                putExtra("HINT", hint)
                putExtra("LEVEL_ID", levelId)
                putExtra("PLANET_NAME", planetName)
            }
            startActivity(intent)
        } else {
            Log.e("LevelsActivity", "No data found for level ID: $levelId")
        }

        cursor.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicManager.stopMusic(this)
        dbHelper.close() // Close the database helper to avoid memory leaks
    }

}