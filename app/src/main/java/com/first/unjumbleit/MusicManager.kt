package com.first.unjumbleit

import android.content.Context
import android.content.Intent

object MusicManager {
    private var isMusicOn = true // Default to music on

    fun startMusic(context: Context) {
        if (isMusicOn) {
            val intent = Intent(context, MusicService::class.java)
            context.startService(intent)
        }
    }

    fun pauseMusic(context: Context) {
        if (isMusicOn) {
            val intent = Intent(context, MusicService::class.java)
            intent.action = "PAUSE"
            context.startService(intent)
        }
    }

    fun stopMusic(context: Context) {
        val intent = Intent(context, MusicService::class.java)
        context.stopService(intent)
    }

    fun toggleMusic(context: Context) {
        isMusicOn = !isMusicOn
        if (isMusicOn) {
            startMusic(context)
        } else {
            pauseMusic(context)
        }
    }

    fun isMusicOn(): Boolean {
        return isMusicOn
    }
}