package com.simple.videowallpaper

import android.content.Context
import android.net.Uri

object VideoPreferences {

    private const val PREFERENCES_NAME = "video_wallpaper_prefs"
    private const val KEY_VIDEO_URI = "video_uri"
    private const val KEY_BATTERY_OPTIMIZATION = "battery_optimization"

    fun setVideoUri(context: Context, uri: Uri) {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_VIDEO_URI, uri.toString()).apply()
    }

    fun getVideoUri(context: Context): Uri? {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val uriString = prefs.getString(KEY_VIDEO_URI, null)
        return if (uriString != null) Uri.parse(uriString) else null
    }

    fun setBatteryOptimization(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_BATTERY_OPTIMIZATION, enabled).apply()
    }

    fun getBatteryOptimization(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BATTERY_OPTIMIZATION, false) // Default to false (optimization off)
    }
}
