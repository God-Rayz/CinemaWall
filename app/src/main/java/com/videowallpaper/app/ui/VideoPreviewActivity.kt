package com.videowallpaper.app.ui

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.videowallpaper.app.R
import com.videowallpaper.app.databinding.ActivityVideoPreviewBinding

class VideoPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPreviewBinding
    private var exoPlayer: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupPlayer()
        loadVideo()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.preview)
    }

    private fun setupPlayer() {
        exoPlayer = ExoPlayer.Builder(this).build().apply {
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

        binding.playerView.player = exoPlayer
    }

    private fun loadVideo() {
        val videoUri = intent.data ?: return
        val loop = intent.getBooleanExtra("loop", true)
        val mute = intent.getBooleanExtra("mute", false)
        val speed = intent.getFloatExtra("speed", 1.0f)

        try {
            exoPlayer?.let { player ->
                val mediaItem = MediaItem.fromUri(videoUri)
                player.setMediaItem(mediaItem)
                player.prepare()
                
                // Apply settings
                player.playWhenReady = true
                player.playbackParameters = androidx.media3.common.PlaybackParameters(speed)
                
                // Set audio volume based on mute setting
                if (mute) {
                    player.volume = 0f
                } else {
                    player.volume = 1f
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onPause() {
        super.onPause()
        exoPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        exoPlayer?.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
        exoPlayer = null
    }
}
