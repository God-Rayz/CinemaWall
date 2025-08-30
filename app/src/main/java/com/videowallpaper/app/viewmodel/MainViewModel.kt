package com.videowallpaper.app.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.videowallpaper.app.data.PreferencesManager
import com.videowallpaper.app.data.VideoSettings

class MainViewModel : ViewModel() {

    private val preferencesManager = PreferencesManager()

    private val _selectedVideo = MutableLiveData<Uri?>()
    val selectedVideo: LiveData<Uri?> = _selectedVideo

    private val _wallpaperStatus = MutableLiveData<WallpaperStatus>()
    val wallpaperStatus: LiveData<WallpaperStatus> = _wallpaperStatus

    fun selectVideo(uri: Uri) {
        _selectedVideo.value = uri
        // Save the selected video URI
        preferencesManager.saveVideoUri(uri.toString())
    }

    fun saveVideoSettings(uri: Uri) {
        val settings = VideoSettings(
            uri = uri.toString(),
            loop = isLoopEnabled(),
            mute = isMuteEnabled(),
            playbackSpeed = getPlaybackSpeed()
        )
        preferencesManager.saveVideoSettings(settings)
    }

    fun isLoopEnabled(): Boolean {
        return preferencesManager.isLoopEnabled()
    }

    fun setLoopEnabled(enabled: Boolean) {
        preferencesManager.setLoopEnabled(enabled)
    }

    fun isMuteEnabled(): Boolean {
        return preferencesManager.isMuteEnabled()
    }

    fun setMuteEnabled(enabled: Boolean) {
        preferencesManager.setMuteEnabled(enabled)
    }

    fun getPlaybackSpeed(): Float {
        return preferencesManager.getPlaybackSpeed()
    }

    fun setPlaybackSpeed(speed: Float) {
        preferencesManager.setPlaybackSpeed(speed)
    }

    fun loadSavedVideo() {
        val savedUri = preferencesManager.getVideoUri()
        if (savedUri != null) {
            try {
                _selectedVideo.value = Uri.parse(savedUri)
            } catch (e: Exception) {
                // Handle invalid URI
                preferencesManager.clearVideoUri()
            }
        }
    }

    fun clearVideo() {
        _selectedVideo.value = null
        preferencesManager.clearVideoUri()
    }

    sealed class WallpaperStatus {
        object Success : WallpaperStatus()
        data class Error(val message: String) : WallpaperStatus()
        object Loading : WallpaperStatus()
    }
}
