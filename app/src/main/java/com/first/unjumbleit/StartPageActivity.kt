package com.first.unjumbleit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class StartPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_page)

        // Start playing background music
        if (MusicManager.isMusicOn()) {
            MusicManager.startMusic(this)
        }
        // Find the Start button
        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            // Navigate to the MenuActivity
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        // Find the Music Toggle button
        val musicToggleButton = findViewById<Button>(R.id.musicToggleButton)
        updateMusicButtonIcon(musicToggleButton)

        // Set a click listener for the Music Toggle button
        musicToggleButton.setOnClickListener {
            MusicManager.toggleMusic(this)
            updateMusicButtonIcon(musicToggleButton)
        }
    }

    private fun updateMusicButtonIcon(button: Button) {
        if (MusicManager.isMusicOn()) {
            button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.music_on, 0, 0) // Set "on" icon
        } else {
            button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.music_off, 0, 0) // Set "off" icon
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        MusicManager.stopMusic(this)
    }
}