package com.first.unjumbleit

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private var currentLevelId: Long = -1
    private var currentWord: String = ""
    private var currentHint: String = ""
    private var currentPlanetName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        Log.d("GameActivity", "onCreate: Activity created")

        // Get the word, hint, level ID, and planet name from the intent
        currentWord = intent.getStringExtra("WORD") ?: ""
        currentHint = intent.getStringExtra("HINT") ?: ""
        currentLevelId = intent.getLongExtra("LEVEL_ID", -1)
        currentPlanetName = intent.getStringExtra("PLANET_NAME") ?: ""

        Log.d("GameActivity", "Level ID = $currentLevelId, Planet = $currentPlanetName")
        Log.d("GameActivity", "Word: $currentWord, Hint: $currentHint")

        // Initialize UI elements
        val levelTextView = findViewById<TextView>(R.id.levelText)
        val hintTextView = findViewById<TextView>(R.id.hintText)
        val guessInput = findViewById<EditText>(R.id.guess)
        val submitButton = findViewById<Button>(R.id.submitButton)

        // Display the level and hint
        levelTextView.text = "Level $currentLevelId - $currentPlanetName"
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

    private fun loadNextLevel() {
        // Increment the level ID and check if it's within the valid range
        val nextLevelId = currentLevelId + 1

        // Query the database to check if the next level exists
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val cursor: Cursor? = db.query(
            DatabaseHelper.TABLE_LEVELS,
            arrayOf(DatabaseHelper.COLUMN_WORD, DatabaseHelper.COLUMN_HINT),
            "${DatabaseHelper.COLUMN_ID} = ? AND ${DatabaseHelper.COLUMN_PLANET} = ?",
            arrayOf(nextLevelId.toString(), currentPlanetName),
            null,
            null,
            null
        )

        if (cursor?.moveToFirst() == true) {
            // Load the next level
            currentLevelId = nextLevelId
            currentWord = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WORD))
            currentHint = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HINT))

            Log.d("GameActivity", "Next level loaded: Level ID = $currentLevelId, Word = $currentWord, Hint = $currentHint")

            // Update the UI
            val levelTextView = findViewById<TextView>(R.id.levelText)
            val hintTextView = findViewById<TextView>(R.id.hintText)
            levelTextView.text = "Level $currentLevelId - $currentPlanetName"
            hintTextView.text = "Hint: $currentHint"

            // Clear the guess input
            val guessInput = findViewById<EditText>(R.id.guess)
            guessInput.text.clear()
        } else {
            // No more levels, show completion message
            Toast.makeText(this, "Congratulations! You've completed all levels!", Toast.LENGTH_LONG).show()
            finish()
        }

        cursor?.close()
        db.close()
    }
}