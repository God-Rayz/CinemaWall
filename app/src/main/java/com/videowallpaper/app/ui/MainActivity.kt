package com.videowallpaper.app.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.guolindev.permissionx.PermissionX
import com.videowallpaper.app.R
import com.videowallpaper.app.databinding.ActivityMainBinding
import com.videowallpaper.app.service.VideoWallpaperService
import com.videowallpaper.app.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private val videoPickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { handleVideoSelection(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupUI()
        setupEventHandlers()
        checkPermissions()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        
        // Observe video selection
        viewModel.selectedVideo.observe(this) { videoUri ->
            if (videoUri != null) {
                showVideoPreview()
                loadVideoThumbnail(videoUri)
            } else {
                hideVideoPreview()
            }
        }

        // Observe wallpaper status
        viewModel.wallpaperStatus.observe(this) { status ->
            when (status) {
                is MainViewModel.WallpaperStatus.Success -> {
                    Toast.makeText(this, R.string.wallpaper_set_successfully, Toast.LENGTH_SHORT).show()
                }
                is MainViewModel.WallpaperStatus.Error -> {
                    Toast.makeText(this, status.message, Toast.LENGTH_SHORT).show()
                }
                else -> { /* Do nothing */ }
            }
        }
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbar)
        
        // Initialize settings from preferences
        binding.switchLoopVideo.isChecked = viewModel.isLoopEnabled()
        binding.switchMuteAudio.isChecked = viewModel.isMuteEnabled()
        
        // Initialize playback speed seekbar
        val speed = viewModel.getPlaybackSpeed()
        val progress = ((speed - 0.25f) / 0.25f).toInt()
        binding.seekBarPlaybackSpeed.progress = progress
        binding.txtPlaybackSpeed.text = "${speed}x"
    }

    private fun setupEventHandlers() {
        binding.btnSelectVideo.setOnClickListener {
            requestVideoPermission()
        }

        binding.btnPreview.setOnClickListener {
            viewModel.selectedVideo.value?.let { uri ->
                previewVideo(uri)
            }
        }

        binding.btnSetWallpaper.setOnClickListener {
            viewModel.selectedVideo.value?.let { uri ->
                setVideoAsWallpaper(uri)
            }
        }

        binding.btnPlayPreview.setOnClickListener {
            viewModel.selectedVideo.value?.let { uri ->
                previewVideo(uri)
            }
        }

        // Settings changes
        binding.switchLoopVideo.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setLoopEnabled(isChecked)
        }

        binding.switchMuteAudio.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setMuteEnabled(isChecked)
        }

        binding.seekBarPlaybackSpeed.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val speed = 0.25f + (progress * 0.25f)
                    viewModel.setPlaybackSpeed(speed)
                    binding.txtPlaybackSpeed.text = "${speed}x"
                }
            }
            
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })
    }

    private fun checkPermissions() {
        PermissionX.isGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun requestVideoPermission() {
        PermissionX.init(this)
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    getString(R.string.storage_permission_message),
                    getString(R.string.grant_permission),
                    getString(R.string.cancel)
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.openAppSettings()
            }
            .request { allGranted, _, _ ->
                if (allGranted) {
                    openVideoPicker()
                }
            }
    }

    private fun openVideoPicker() {
        videoPickerLauncher.launch("video/*")
    }

    private fun handleVideoSelection(uri: Uri) {
        viewModel.selectVideo(uri)
    }

    private fun showVideoPreview() {
        binding.cardVideoPreview.visibility = View.VISIBLE
    }

    private fun hideVideoPreview() {
        binding.cardVideoPreview.visibility = View.GONE
    }

    private fun loadVideoThumbnail(uri: Uri) {
        // Load video thumbnail using Glide
        // This would be implemented with actual Glide code
        binding.imgVideoThumbnail.setImageResource(R.drawable.ic_video_placeholder)
    }

    private fun previewVideo(uri: Uri) {
        // Open video preview activity
        val intent = Intent(this, VideoPreviewActivity::class.java).apply {
            data = uri
            putExtra("loop", binding.switchLoopVideo.isChecked)
            putExtra("mute", binding.switchMuteAudio.isChecked)
            putExtra("speed", binding.sliderPlaybackSpeed.value)
        }
        startActivity(intent)
    }

    private fun setVideoAsWallpaper(uri: Uri) {
        // Save video URI and settings to preferences
        viewModel.saveVideoSettings(uri)
        
        // Show wallpaper chooser
        val intent = Intent(Intent.ACTION_SET_WALLPAPER)
        startActivity(Intent.createChooser(intent, getString(R.string.set_as_wallpaper)))
    }

    override fun onResume() {
        super.onResume()
        // Check if video wallpaper is currently active
        checkWallpaperStatus()
    }

    private fun checkWallpaperStatus() {
        // Check if our video wallpaper service is currently active
        // This would involve checking the current wallpaper service
    }
}
