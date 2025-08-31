package com.simple.videowallpaper

import android.media.MediaPlayer
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import android.util.Log

class VideoWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine {
        return VideoEngine()
    }

    private inner class VideoEngine : Engine() {

        private var mediaPlayer: MediaPlayer? = null
        private var isBatteryOptimizationEnabled: Boolean = false
        private var currentSurfaceHolder: SurfaceHolder? = null

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            isBatteryOptimizationEnabled = VideoPreferences.getBatteryOptimization(applicationContext)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            if (isBatteryOptimizationEnabled) {
                if (visible) {
                    Log.d("VideoWallpaper", "Wallpaper visible, resuming video.")
                    // Re-initialize MediaPlayer if it's not playing or in a bad state
                    if (mediaPlayer == null || !mediaPlayer!!.isPlaying) {
                        currentSurfaceHolder?.let { initMediaPlayer(it) }
                    } else {
                        mediaPlayer?.start()
                    }
                } else {
                    Log.d("VideoWallpaper", "Wallpaper hidden, pausing video.")
                    mediaPlayer?.pause()
                }
            }
            Log.d("VideoWallpaper", "Battery optimization state: $isBatteryOptimizationEnabled, Visible: $visible")
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            currentSurfaceHolder = holder
            initMediaPlayer(holder)
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            Log.d("VideoWallpaper", "Surface destroyed.")
            releaseMediaPlayer()
            currentSurfaceHolder = null
        }

        override fun onDestroy() {
            super.onDestroy()
            releaseMediaPlayer()
        }

        private fun initMediaPlayer(holder: SurfaceHolder) {
            releaseMediaPlayer() // Ensure any existing player is released
            val videoUri = VideoPreferences.getVideoUri(applicationContext)
            if (videoUri != null) {
                try {
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(applicationContext, videoUri)
                        setSurface(holder.surface)
                        isLooping = true
                        setVolume(0f, 0f) // Mute the video
                        setOnErrorListener { mp, what, extra ->
                            Log.e("VideoWallpaper", "MediaPlayer error: what=$what, extra=$extra")
                            releaseMediaPlayer()
                            true
                        }
                        setOnCompletionListener { mp ->
                            Log.d("VideoWallpaper", "MediaPlayer completed, re-starting.")
                            mp.start() // Ensure it loops even if isLooping has issues
                        }
                        prepare()
                        start()
                    }
                } catch (e: Exception) {
                    Log.e("VideoWallpaper", "Error initializing MediaPlayer: ${e.message}", e)
                }
            }
        }

        private fun releaseMediaPlayer() {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
}