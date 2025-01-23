package com.first.unjumbleit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val splashVideoView = findViewById<VideoView>(R.id.splashVideoView)
        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.portal}")
        splashVideoView.setVideoURI(videoUri)
        splashVideoView.start()

        splashVideoView.setOnCompletionListener {
            startActivity(Intent(this, StartPageActivity::class.java))
            finish()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, StartPageActivity::class.java))
            finish()
        }, 3000) // 5 seconds fallback timeout
    }
}