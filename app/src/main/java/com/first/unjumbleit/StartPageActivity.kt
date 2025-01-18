package com.first.unjumbleit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.first.unjumbleit.R

class StartPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_page) // Ensure this layout exists

        val level1Button = findViewById<Button>(R.id.level1Button)
        val level2Button = findViewById<Button>(R.id.level2Button)

        level1Button.setOnClickListener {
            startGame(1)  // Start level 1
        }

        level2Button.setOnClickListener {
            startGame(2)  // Start level 2
        }
    }

    private fun startGame(level: Int) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("LEVEL", level)  // Pass the selected level
        startActivity(intent)
    }
}
