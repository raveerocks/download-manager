package io.raveerocks.downloadmanager.core.types

import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import io.raveerocks.downloadmanager.R

enum class NotificationChannelType(
    val channelId: String,
    val channelName: Int,
    val channelDescription: Int,
    val importance: Int,
    val setShowBadge: Boolean,
    val enableLights: Boolean,
    val lightColor: Int,
    val enableVibration: Boolean
) {
    DOWNLOAD_DONE(
        "DOWNLOAD_COMPLETED",
        R.string.channel_name_download_completion,
        R.string.channel_description_for_download_completion,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) NotificationManager.IMPORTANCE_LOW else -1,
        true,
        true,
        Color.BLUE,
        false
    ),

    DOWNLOAD_FAILED(
        "DOWNLOAD_FAILED",
        R.string.channel_name_download_failure,
        R.string.channel_description_for_download_failure,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) NotificationManager.IMPORTANCE_LOW else -1,
        true,
        true,
        Color.RED,
        false
    )
}