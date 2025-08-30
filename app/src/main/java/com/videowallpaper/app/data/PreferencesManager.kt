package com.videowallpaper.app.data

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.core.content.edit

class PreferencesManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "video_wallpaper_prefs"
        private const val KEY_VIDEO_URI = "video_uri"
        private const val KEY_LOOP_ENABLED = "loop_enabled"
        private const val KEY_MUTE_ENABLED = "mute_enabled"
        private const val KEY_PLAYBACK_SPEED = "playback_speed"
        private const val KEY_VIDEO_QUALITY = "video_quality"
        private const val KEY_FIRST_RUN = "first_run"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveVideoUri(uri: String) {
        prefs.edit {
            putString(KEY_VIDEO_URI, uri)
        }
    }

    fun getVideoUri(): String? {
        return prefs.getString(KEY_VIDEO_URI, null)
    }

    fun clearVideoUri() {
        prefs.edit {
            remove(KEY_VIDEO_URI)
        }
    }

    fun isLoopEnabled(): Boolean {
        return prefs.getBoolean(KEY_LOOP_ENABLED, true)
    }

    fun setLoopEnabled(enabled: Boolean) {
        prefs.edit {
            putBoolean(KEY_LOOP_ENABLED, enabled)
        }
    }

    fun isMuteEnabled(): Boolean {
        return prefs.getBoolean(KEY_MUTE_ENABLED, false)
    }

    fun setMuteEnabled(enabled: Boolean) {
        prefs.edit {
            putBoolean(KEY_MUTE_ENABLED, enabled)
        }
    }

    fun getPlaybackSpeed(): Float {
        return prefs.getFloat(KEY_PLAYBACK_SPEED, 1.0f)
    }

    fun setPlaybackSpeed(speed: Float) {
        prefs.edit {
            putFloat(KEY_PLAYBACK_SPEED, speed)
        }
    }

    fun getVideoQuality(): VideoQuality {
        val qualityString = prefs.getString(KEY_VIDEO_QUALITY, VideoQuality.MEDIUM.value)
        return VideoQuality.values().find { it.value == qualityString } ?: VideoQuality.MEDIUM
    }

    fun setVideoQuality(quality: VideoQuality) {
        prefs.edit {
            putString(KEY_VIDEO_QUALITY, quality.value)
        }
    }

    fun isFirstRun(): Boolean {
        return prefs.getBoolean(KEY_FIRST_RUN, true)
    }

    fun setFirstRunComplete() {
        prefs.edit {
            putBoolean(KEY_FIRST_RUN, false)
        }
    }

    fun saveVideoSettings(settings: VideoSettings) {
        prefs.edit {
            putString(KEY_VIDEO_URI, settings.uri)
            putBoolean(KEY_LOOP_ENABLED, settings.loop)
            putBoolean(KEY_MUTE_ENABLED, settings.mute)
            putFloat(KEY_PLAYBACK_SPEED, settings.playbackSpeed)
            putString(KEY_VIDEO_QUALITY, settings.quality.value)
        }
    }

    fun getVideoSettings(): VideoSettings? {
        val uri = getVideoUri() ?: return null
        return VideoSettings(
            uri = uri,
            loop = isLoopEnabled(),
            mute = isMuteEnabled(),
            playbackSpeed = getPlaybackSpeed(),
            quality = getVideoQuality()
        )
    }

    fun clearAllSettings() {
        prefs.edit {
            clear()
        }
    }
}
