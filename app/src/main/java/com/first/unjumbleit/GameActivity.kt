package com.first.unjumbleit

import android.app.AlertDialog
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private var currentLevelId: Long = -1
    private var currentWord: String = ""
    private var currentHint: String = ""
    private var currentPlanetName: String = ""
    private lateinit var scrambledLetters: List<Char>
    private lateinit var guessTextView: TextView
    private lateinit var letterButtonsContainer: GridLayout
    private lateinit var star1: ImageView
    private lateinit var star2: ImageView
    private lateinit var star3: ImageView
    private var attempts: Int = 0
    private var stars: Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        if (MusicManager.isMusicOn()) {
            MusicManager.startMusic(this)
        }

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

        // Set the background based on the planet name
        setPlanetBackground(currentPlanetName)

        // Initialize UI elements
        val levelTextView = findViewById<TextView>(R.id.levelText)
        val hintTextView = findViewById<TextView>(R.id.hintText)
        guessTextView = findViewById(R.id.guessText)
        letterButtonsContainer = findViewById(R.id.letterButtonsContainer)
        val submitButton = findViewById<Button>(R.id.submitButton)

        // Initialize star views
        star1 = findViewById(R.id.star1)
        star2 = findViewById(R.id.star2)
        star3 = findViewById(R.id.star3)

        // Display the level and hint
        levelTextView.text = "Level $currentLevelId "
        hintTextView.text = "Hint: $currentHint"

        // Scramble the letters of the current word
        scrambledLetters = scrambleLetters(currentWord)
        Log.d("GameActivity", "Scrambled letters: ${scrambledLetters.joinToString("")}")

        // Set up letter buttons dynamically
        setupLetterButtons()

        // Handle the submit button logic
        submitButton.setOnClickListener {
            val guess = guessTextView.text.toString()
            if (guess.equals(currentWord, ignoreCase = true)) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
                updateStars()
                loadNextLevel()
            } else {
                attempts++
                if (stars > 0) {
                    stars--
                    updateStars()
                }
                if (stars == 0) {
                    showYouLostDialog()
                } else {
                    Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show()
                }
            }
            // Clear the guess and re-enable all buttons after submission
            clearGuessAndResetButtons()
        }
    }

    private fun showYouLostDialog() {
        // Inflate the custom layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.custom_you_lost_dialog, null)

        // Build the dialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView) // Set the custom layout
            .setCancelable(false) // Prevent dismissing the dialog by tapping outside
            .create()

        // Set up button click listeners
        dialogView.findViewById<Button>(R.id.btnTryAgain).setOnClickListener {
            // Restart the current level
            restartLevel()
            dialog.dismiss() // Dismiss the dialog
        }

        dialogView.findViewById<Button>(R.id.btnExit).setOnClickListener {
            // Exit the game
            finish()
            dialog.dismiss() // Dismiss the dialog
        }

        // Optional: Add animation
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        // Show the dialog
        dialog.show()
    }

    private fun restartLevel() {
        // Reset stars and attempts
        stars = 3
        attempts = 0
        updateStars()

        // Reset the guess text view and letter buttons
        guessTextView.text = ""
        setupLetterButtons()
    }

    private fun updateStars() {
        star1.setImageResource(if (stars >= 1) R.drawable.pickle else R.drawable.pickle_gray)
        star2.setImageResource(if (stars >= 2) R.drawable.pickle else R.drawable.pickle_gray)
        star3.setImageResource(if (stars >= 3) R.drawable.pickle else R.drawable.pickle_gray)
    }

    private fun setPlanetBackground(planetName: String) {
        val rootLayout = findViewById<ViewGroup>(R.id.rootLayout)
        val backgroundResId = when (planetName.lowercase()) {
            "multiverse" -> R.drawable.bg_multiverse
            "parasites" -> R.drawable.bg_parasites
            "monsters" -> R.drawable.bg_monsters
            "science" -> R.drawable.bg_science
            "supernatural" -> R.drawable.bg_supernatural
            "venus" -> R.drawable.bg_venus
            else -> R.drawable.starry_background
        }
        rootLayout.setBackgroundResource(backgroundResId)
    }

    private fun scrambleLetters(word: String): List<Char> {
        val letters = word.toCharArray().toList()
        return letters.shuffled(Random(System.currentTimeMillis()))
    }

    private fun setupLetterButtons() {
        letterButtonsContainer.removeAllViews()
        letterButtonsContainer.columnCount = 4 // Ensure column count matches your XML

        // Define button width/height in dp (adjust as needed)
        val buttonSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            60f,
            resources.displayMetrics
        ).toInt()

        for ((index, letter) in scrambledLetters.withIndex()) {
            val button = Button(this).apply {
                text = letter.toString()
                layoutParams = GridLayout.LayoutParams().apply {
                    width = buttonSize // Fixed width
                    height = buttonSize // Fixed height

                    // Calculate row and column positions
                    val row = index / letterButtonsContainer.columnCount
                    val col = index % letterButtonsContainer.columnCount

                    // Set column/row specs
                    columnSpec = GridLayout.spec(col, 1f) // Column index and weight
                    rowSpec = GridLayout.spec(row, 1f) // Row index and weight

                    setMargins(8, 8, 8, 8)
                }
                setOnClickListener {
                    guessTextView.append(letter.toString())
                    it.isEnabled = false
                }
            }
            letterButtonsContainer.addView(button)
        }
    }
    private fun clearGuessAndResetButtons() {
        // Clear the guess text view
        guessTextView.text = ""

        // Re-enable all letter buttons
        for (i in 0 until letterButtonsContainer.childCount) {
            val button = letterButtonsContainer.getChildAt(i) as Button
            button.isEnabled = true
        }
    }

    private fun loadNextLevel() {
        val nextLevelId = currentLevelId + 1
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
            currentLevelId = nextLevelId
            currentWord = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WORD))
            currentHint = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HINT))

            Log.d("GameActivity", "Next level loaded: Level ID = $currentLevelId, Word = $currentWord, Hint = $currentHint")

            val levelTextView = findViewById<TextView>(R.id.levelText)
            val hintTextView = findViewById<TextView>(R.id.hintText)
            levelTextView.text = "Level $currentLevelId - $currentPlanetName"
            hintTextView.text = "Hint: $currentHint"

            scrambledLetters = scrambleLetters(currentWord)
            Log.d("GameActivity", "Scrambled letters: ${scrambledLetters.joinToString("")}")

            setupLetterButtons()
        } else {
            Toast.makeText(this, "Congratulations! You've completed all levels!", Toast.LENGTH_LONG).show()
            finish()
        }

        cursor?.close()
        db.close()
    }

}