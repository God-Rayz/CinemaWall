package com.simple.videowallpaper

import android.Manifest
import android.app.AlertDialog
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var videoPreview: ImageView

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            openVideoPicker()
        } else {
            Toast.makeText(this, "Permission denied to read storage", Toast.LENGTH_SHORT).show()
        }
    }

    private val selectVideoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val videoUri = result.data?.data
            if (videoUri != null) {
                VideoPreferences.setVideoUri(this, videoUri)
                Toast.makeText(this, "Video selected. Setting wallpaper...", Toast.LENGTH_LONG).show()
                // Send broadcast to VideoWallpaperService to update its video
                val updateIntent = Intent(VideoWallpaperService.ACTION_UPDATE_WALLPAPER)
                sendBroadcast(updateIntent)
                setWallpaper()
                loadVideoThumbnail()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoPreview = findViewById(R.id.video_preview)

        val selectVideoButton = findViewById<Button>(R.id.select_video_button)
        selectVideoButton.setOnClickListener {
            checkAndRequestPermissions()
        }

        val refreshWallpaperButton = findViewById<Button>(R.id.refresh_wallpaper_button)
        refreshWallpaperButton.setOnClickListener {
            val updateIntent = Intent(VideoWallpaperService.ACTION_UPDATE_WALLPAPER)
            sendBroadcast(updateIntent)
            Toast.makeText(this, "Wallpaper refresh requested.", Toast.LENGTH_SHORT).show()
        }

        val batteryOptimizationSwitch = findViewById<Switch>(R.id.battery_optimization_switch)
        batteryOptimizationSwitch.isChecked = VideoPreferences.getBatteryOptimization(this)
        batteryOptimizationSwitch.setOnCheckedChangeListener { _, isChecked ->
            VideoPreferences.setBatteryOptimization(this, isChecked)
        }

        val batteryOptimizationInfoIcon = findViewById<ImageView>(R.id.battery_optimization_info_icon)
        batteryOptimizationInfoIcon.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Battery Optimization")
                .setMessage("When enabled, the video wallpaper will pause when another app is in the foreground, saving battery life.")
                .setPositiveButton("Got it", null)
                .show()
        }

        findViewById<LinearLayout>(R.id.video_preview_container).setOnClickListener { 
            checkAndRequestPermissions()
        }

        loadVideoThumbnail()
    }

    private fun loadVideoThumbnail() {
        val videoUri = VideoPreferences.getVideoUri(this)
        if (videoUri != null) {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(this, videoUri)
                val bitmap = retriever.getFrameAtTime(0)
                if (bitmap != null) {
                    videoPreview.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error loading video thumbnail", Toast.LENGTH_SHORT).show()
            } finally {
                retriever.release()
            }
        }
    }

    private fun checkAndRequestPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_VIDEO else Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                openVideoPicker()
            }
            else -> {
                requestPermissionLauncher.launch(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_VIDEO else Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }

    private fun openVideoPicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"
        selectVideoLauncher.launch(intent)
    }

    private fun setWallpaper() {
        val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
            ComponentName(this, VideoWallpaperService::class.java))
        startActivity(intent)
        Toast.makeText(this, "Please select 'Video Wallpaper' and then 'Set wallpaper'.", Toast.LENGTH_LONG).show()
    }
}
