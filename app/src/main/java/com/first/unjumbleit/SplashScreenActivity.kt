package com.first.unjumbleit

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Navigate to StartPageActivity after 3 seconds using coroutines
        lifecycleScope.launch {
            delay(3000)  // 3-second delay
            val intent = Intent(this@SplashScreenActivity, StartPageActivity::class.java)
            startActivity(intent)
            finish()  // End the SplashScreenActivity
        }
    }
}
