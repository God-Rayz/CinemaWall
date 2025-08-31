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

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            isBatteryOptimizationEnabled = VideoPreferences.getBatteryOptimization(applicationContext)
        }

        

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            val videoUri = VideoPreferences.getVideoUri(applicationContext)
            if (videoUri != null) {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(applicationContext, videoUri)
                    setSurface(holder.surface)
                    isLooping = true
                    setVolume(0f, 0f)
                    prepare()
                    start()
                }
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            if (isBatteryOptimizationEnabled) {
                if (visible) {
                    Log.d("VideoWallpaper", "Wallpaper visible, resuming video.")
                    mediaPlayer?.start()
                } else {
                    Log.d("VideoWallpaper", "Wallpaper hidden, pausing video.")
                    mediaPlayer?.pause()
                }
            }
            Log.d("VideoWallpaper", "Battery optimization state: $isBatteryOptimizationEnabled, Visible: $visible")
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            mediaPlayer?.release()
            mediaPlayer = null
        }

        override fun onDestroy() {
            super.onDestroy()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
}
