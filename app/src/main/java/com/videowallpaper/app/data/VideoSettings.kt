package com.videowallpaper.app.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoSettings(
    val uri: String,
    val loop: Boolean = true,
    val mute: Boolean = false,
    val playbackSpeed: Float = 1.0f,
    val quality: VideoQuality = VideoQuality.MEDIUM
) : Parcelable

enum class VideoQuality(val value: String) {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high")
}
