package com.first.unjumbleit

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase // Add this import
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var db: SQLiteDatabase? = null
    private var cursor: Cursor? = null
    private var currentLevel: Int = 0
    private var currentWord: String = ""
    private var currentHint: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        Log.d("GameActivity", "onCreate: Activity created")

        // Get the level ID from the intent
        currentLevel = intent.getIntExtra("LEVEL_ID", 1) // Default to level 1 if not provided
        Log.d("GameActivity", "Level ID received: $currentLevel")

        // Initialize DatabaseHelper and get a readable database
        dbHelper = DatabaseHelper(this)
        db = dbHelper.readableDatabase

        // Initialize UI elements
        val levelTextView = findViewById<TextView>(R.id.levelText)
        val hintTextView = findViewById<TextView>(R.id.hintText)
        val guessInput = findViewById<EditText>(R.id.guess)
        val submitButton = findViewById<Button>(R.id.submitButton)

        // Load the level data from the database
        loadLevel(currentLevel)

        // Display the level and hint
        levelTextView.text = "Level $currentLevel"
        hintTextView.text = "Hint: $currentHint"

        // Handle the submit button logic
        submitButton.setOnClickListener {
            val guess = guessInput.text.toString()
            if (guess.equals(currentWord, ignoreCase = true)) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
                loadNextLevel()
            } else {
                Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadLevel(level: Int) {
        Log.d("GameActivity", "loadLevel: Loading level $level")

        // Query the levels table for the specific level
        cursor = db?.query(
            DatabaseHelper.TABLE_LEVELS,
            null,  // All columns
            "${DatabaseHelper.COLUMN_LEVEL} = ?",
            arrayOf(level.toString()),
            null,
            null,
            null
        )

        if (cursor?.moveToFirst() == true) {
            // Retrieve word and hint from the cursor
            currentWord = cursor?.getString(cursor?.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WORD) ?: -1) ?: ""
            currentHint = cursor?.getString(cursor?.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HINT) ?: -1) ?: ""

            Log.d("GameActivity", "Level $level loaded: Word = $currentWord, Hint = $currentHint")
        } else {
            Log.e("GameActivity", "Level data not found for level $level")
            Toast.makeText(this, "Level data not found!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadNextLevel() {
        // Move to the next level, update the currentLevel, and load the data for the next level
        currentLevel++
        if (currentLevel > 3) { // Adjust this condition based on your total number of levels
            // Handle the end of the game, e.g., show a "game completed" message
            Toast.makeText(this, "Congratulations! You've completed all levels!", Toast.LENGTH_LONG).show()
            finish()  // Or restart the game
        } else {
            loadLevel(currentLevel)
            val hintTextView = findViewById<TextView>(R.id.hintText)
            hintTextView.text = "Hint: $currentHint"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cursor?.close()
        db?.close()
    }
}