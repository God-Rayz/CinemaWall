package com.videowallpaper.app.service

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.videowallpaper.app.data.PreferencesManager
import com.videowallpaper.app.data.VideoSettings

class VideoWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine {
        return VideoWallpaperEngine()
    }

    inner class VideoWallpaperEngine : Engine() {

        private var exoPlayer: ExoPlayer? = null
        private var playerView: PlayerView? = null
        private var preferencesManager: PreferencesManager? = null
        private var currentSettings: VideoSettings? = null

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            preferencesManager = PreferencesManager(this@VideoWallpaperService)
            setupPlayer()
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            loadVideoSettings()
            startVideoPlayback()
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            pauseVideoPlayback()
        }

        override fun onDestroy() {
            super.onDestroy()
            releasePlayer()
        }

        private fun setupPlayer() {
            exoPlayer = ExoPlayer.Builder(this@VideoWallpaperService).build().apply {
                repeatMode = Player.REPEAT_MODE_ALL
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_READY -> {
                                // Video is ready to play
                            }
                            Player.STATE_ENDED -> {
                                // Video ended, will loop due to REPEAT_MODE_ALL
                            }
                            Player.STATE_BUFFERING -> {
                                // Video is buffering
                            }
                            Player.STATE_IDLE -> {
                                // Player is idle
                            }
                        }
                    }

                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        // Handle playback errors
                        error.printStackTrace()
                    }
                })
            }
        }

        private fun loadVideoSettings() {
            currentSettings = preferencesManager?.getVideoSettings()
        }

        private fun startVideoPlayback() {
            val settings = currentSettings ?: return
            val videoUri = Uri.parse(settings.uri)
            
            try {
                exoPlayer?.let { player ->
                    val mediaItem = MediaItem.fromUri(videoUri)
                    player.setMediaItem(mediaItem)
                    player.prepare()
                    
                    // Apply settings
                    player.playWhenReady = true
                    player.playbackParameters = androidx.media3.common.PlaybackParameters(settings.playbackSpeed)
                    
                    // Set audio volume based on mute setting
                    if (settings.mute) {
                        player.volume = 0f
                    } else {
                        player.volume = 1f
                    }
                    
                    // Set the surface for the player
                    val surface = surfaceHolder.surface
                    player.setVideoSurfaceView(SurfaceView(this@VideoWallpaperService).apply {
                        holder.surface = surface
                    })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun pauseVideoPlayback() {
            exoPlayer?.pause()
        }

        private fun resumeVideoPlayback() {
            exoPlayer?.play()
        }

        private fun releasePlayer() {
            exoPlayer?.release()
            exoPlayer = null
        }

        override fun onVisibilityChanged(visible: Boolean) {
            if (visible) {
                resumeVideoPlayback()
            } else {
                pauseVideoPlayback()
            }
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            // Handle surface size changes if needed
        }
    }
}
