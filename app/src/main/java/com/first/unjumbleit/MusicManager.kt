package com.first.unjumbleit

import android.content.Context
import android.media.MediaPlayer

object MusicManager {
    private var mediaPlayer: MediaPlayer? = null
    private var isMusicOn = true // Default to music on

    fun playMusic(context: Context) {
        if (isMusicOn && mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.background_music)
            mediaPlayer?.isLooping = true // Loop the music
            mediaPlayer?.start()
        }
    }

    fun pauseMusic() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    fun releaseMusic() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun toggleMusic(context: Context) {
        isMusicOn = !isMusicOn
        if (isMusicOn) {
            playMusic(context)
        } else {
            pauseMusic()
        }
    }

    fun isMusicOn(): Boolean {
        return isMusicOn
    }
}