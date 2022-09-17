package io.raveerocks.downloadmanager.data.os.dao

import android.os.Bundle
import androidx.core.app.NotificationCompat
import io.raveerocks.downloadmanager.core.types.NotificationChannelType

interface NotificationDAO {
    fun createChannel(notificationChannelType: NotificationChannelType)
    fun sendNotification(
        channelId: String,
        notificationId: Long,
        icon: Int,
        color: Int,
        contentTitle: String,
        contentText: String,
        detailedText: List<String>?,
        autoCancel: Boolean,
        priority: Int,
        destinations: List<Int>?,
        args: List<Bundle?>?,
        actions: List<NotificationCompat.Action?>?
    )

    fun cancelNotification(id: Long)
}