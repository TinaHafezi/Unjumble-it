package com.first.unjumbleit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.first.unjumbleit.MenuActivity

class StartPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_page) // Ensure the layout matches your updated XML

        // Find the Start button
        val startButton = findViewById<Button>(R.id.startButton)

        // Set a click listener for the Start button
        startButton.setOnClickListener {
            // Navigate to the MenuActivity
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}
