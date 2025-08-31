package com.simple.videowallpaper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class VideoWallpaperService : WallpaperService() {

    companion object {
        const val ACTION_UPDATE_WALLPAPER = "com.simple.videowallpaper.ACTION_UPDATE_WALLPAPER"
    }

    private var currentEngine: VideoEngine? = null

    private val wallpaperUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_UPDATE_WALLPAPER) {
                Log.d("VideoWallpaper", "Received update wallpaper broadcast. Requesting engine update.")
                currentEngine?.updateVideoSource()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter(ACTION_UPDATE_WALLPAPER)
        registerReceiver(wallpaperUpdateReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(wallpaperUpdateReceiver)
    }

    override fun onCreateEngine(): Engine {
        val engine = VideoEngine()
        currentEngine = engine
        return engine
    }

    private inner class VideoEngine : Engine() {

        private var exoPlayer: SimpleExoPlayer? = null
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
                    // More aggressive re-initialization
                    if (exoPlayer == null || !exoPlayer!!.isPlaying) {
                        currentSurfaceHolder?.let { initExoPlayer(it) }
                    } else {
                        exoPlayer?.play()
                    }
                } else {
                    Log.d("VideoWallpaper", "Wallpaper hidden, pausing video.")
                    exoPlayer?.pause()
                }
            } else {
                // If battery optimization is disabled, ensure video is playing if visible
                if (visible) {
                    if (exoPlayer == null || !exoPlayer!!.isPlaying) {
                        currentSurfaceHolder?.let { initExoPlayer(it) }
                    }
                }
            }
            Log.d("VideoWallpaper", "Battery optimization state: $isBatteryOptimizationEnabled, Visible: $visible")
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            currentSurfaceHolder = holder
            initExoPlayer(holder)
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            Log.d("VideoWallpaper", "Surface destroyed.")
            releaseExoPlayer()
            currentSurfaceHolder = null
        }

        override fun onDestroy() {
            super.onDestroy()
            releaseExoPlayer()
        }

        private fun initExoPlayer(holder: SurfaceHolder) {
            Log.d("VideoWallpaper", "Initializing ExoPlayer.")
            releaseExoPlayer() // Ensure any existing player is released
            val videoUri = VideoPreferences.getVideoUri(applicationContext)
            if (videoUri != null) {
                try {
                    val trackSelector = DefaultTrackSelector(applicationContext)
                    val loadControl = com.google.android.exoplayer2.DefaultLoadControl()
                    val renderersFactory = com.google.android.exoplayer2.DefaultRenderersFactory(applicationContext)
                    val dataSourceFactory = DefaultDataSourceFactory(applicationContext, Util.getUserAgent(applicationContext, "VideoWallpaper"))
                    val mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)

                    exoPlayer = SimpleExoPlayer.Builder(applicationContext).build().apply {
                        setMediaItem(MediaItem.fromUri(videoUri))
                        setVideoSurface(holder.surface)
                        repeatMode = Player.REPEAT_MODE_ONE // Loop the video
                        volume = 0f // Mute the video
                        addListener(object : Player.Listener {
                            override fun onPlayerError(error: com.google.android.exoplayer2.PlaybackException) {
                                Log.e("VideoWallpaper", "ExoPlayer error: ${error.message}", error)
                                releaseExoPlayer()
                            }

                            override fun onPlaybackStateChanged(playbackState: Int) {
                                if (playbackState == Player.STATE_ENDED) {
                                    Log.d("VideoWallpaper", "ExoPlayer completed, re-starting.")
                                    // No need to explicitly start, repeatMode handles looping
                                }
                            }
                        })
                        prepare()
                        playWhenReady = true
                    }
                } catch (e: Exception) {
                    Log.e("VideoWallpaper", "Error initializing ExoPlayer: ${e.message}", e)
                }
            }
        }

        private fun releaseExoPlayer() {
            exoPlayer?.release()
            exoPlayer = null
        }

        fun updateVideoSource() {
            currentSurfaceHolder?.let { initExoPlayer(it) }
        }
    }
}