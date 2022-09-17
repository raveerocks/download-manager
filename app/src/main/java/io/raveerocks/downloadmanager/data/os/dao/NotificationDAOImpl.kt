package io.raveerocks.downloadmanager.data.os.dao

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import io.raveerocks.downloadmanager.R
import io.raveerocks.downloadmanager.core.types.NotificationChannelType

class NotificationDAOImpl(private val context: Context) : NotificationDAO {

    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(
            AppCompatActivity.NOTIFICATION_SERVICE
        ) as NotificationManager
    }

    override fun createChannel(notificationChannelType: NotificationChannelType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                notificationChannelType.channelId,
                context.getString(notificationChannelType.channelName),
                notificationChannelType.importance
            )
                .apply {
                    setShowBadge(notificationChannelType.setShowBadge)
                    enableLights(notificationChannelType.enableLights)
                    lightColor = notificationChannelType.lightColor
                    enableVibration(notificationChannelType.enableVibration)
                    description =
                        context.getString(notificationChannelType.channelDescription)
                }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun sendNotification(
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
    ) {
        notificationManager.notify(
            notificationId.toInt(),
            createNotification(
                channelId,
                icon,
                color,
                contentTitle,
                contentText,
                detailedText,
                autoCancel,
                priority,
                destinations,
                args,
                actions
            )
        )
    }

    override fun cancelNotification(id: Long) {
        notificationManager.cancel(id.toInt())
    }

    private fun createNotification(
        channelId: String,
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
    ): Notification {
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setColor(ContextCompat.getColor(context, color))
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setStyle(
                NotificationCompat
                    .BigTextStyle()
                    .setBigContentTitle(contentTitle)
                    .bigText(detailedText?.reduce { a, b -> a + "\n" + b })
            )
            .setAutoCancel(autoCancel)
            .setPriority(priority)
            .setContentIntent(createContentIntent(destinations, args))
            .apply { actions?.forEach(this::addAction) }
            .build()
    }

    private fun createContentIntent(destinations: List<Int>?, args: List<Bundle?>?): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.navigation)
            .addDestinations(destinations, args)
            .createPendingIntent()
    }

    private fun NavDeepLinkBuilder.addDestinations(
        destinations: List<Int>?,
        args: List<Bundle?>?
    ): NavDeepLinkBuilder {
        destinations?.let {
            it.indices.forEach { index ->
                addDestination(destinations[index], args?.let { args[index] })
            }
        }
        return this
    }

}