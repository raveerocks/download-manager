package io.raveerocks.downloadmanager.repositories

import android.app.DownloadManager
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.raveerocks.downloadmanager.R
import io.raveerocks.downloadmanager.core.entities.FailedDownloadEntity
import io.raveerocks.downloadmanager.core.entities.views.FailedDownloadView
import io.raveerocks.downloadmanager.core.helpers.Constants
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOADS_REFRESH
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_CLONE
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_DELETE
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_FAILED_DOWNLOAD_DELETE
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_FAILED_DOWNLOAD_RETRY
import io.raveerocks.downloadmanager.core.models.FailedDownload
import io.raveerocks.downloadmanager.core.types.FailedReason
import io.raveerocks.downloadmanager.core.types.NotificationChannelType
import io.raveerocks.downloadmanager.data.db.dao.FailedDownloadDAO
import io.raveerocks.downloadmanager.data.os.dao.DownloadsDAO
import io.raveerocks.downloadmanager.data.os.dao.EventDAO
import io.raveerocks.downloadmanager.data.os.dao.NotificationDAO
import io.raveerocks.downloadmanager.data.os.dao.ResourceDAO
import io.raveerocks.downloadmanager.core.entities.Download
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.*

class FailedDownloadRepository(
    private val downloadsDAO: DownloadsDAO,
    private val eventDAO: EventDAO,
    private val failedDownloadDAO: FailedDownloadDAO,
    private val notificationDAO: NotificationDAO,
    private val resourceDAO: ResourceDAO
) {

    companion object {
        private val failedReasonMap = hashMapOf(
            DownloadManager.ERROR_UNKNOWN to FailedReason.UNKNOWN_ERROR,
            DownloadManager.ERROR_FILE_ERROR to FailedReason.FILE_ERROR,
            DownloadManager.ERROR_UNHANDLED_HTTP_CODE to FailedReason.HTTP_ERROR,
            DownloadManager.ERROR_HTTP_DATA_ERROR to FailedReason.HTTP_ERROR,
            DownloadManager.ERROR_TOO_MANY_REDIRECTS to FailedReason.NETWORK_ERROR,
            DownloadManager.ERROR_INSUFFICIENT_SPACE to FailedReason.MEMORY_ERROR,
            DownloadManager.ERROR_DEVICE_NOT_FOUND to FailedReason.MEMORY_ERROR,
            DownloadManager.ERROR_CANNOT_RESUME to FailedReason.CANNOT_RESUME_ERROR,
            DownloadManager.ERROR_FILE_ALREADY_EXISTS to FailedReason.FILE_ALREADY_EXISTS_ERROR,
        )
    }

    init {
        notificationDAO.createChannel(NotificationChannelType.DOWNLOAD_FAILED)
    }

    fun addFailedDownload(id: Long) {
        downloadsDAO.getDownload(id).asFailedDownloadEntity()?.let {
            CoroutineScope(Dispatchers.IO).launch {
                saveFailedDownload(it)
                notifyDownloadFailed(id)
            }
        }
    }

    fun retryFailedDownload(id: Long) {
        notificationDAO.cancelNotification(id)
        CoroutineScope(Dispatchers.IO).launch {
            failedDownloadDAO.deleteById(id)
            publishFailedDownloadRetry(id)
        }

    }

    fun deleteFailedDownload(id: Long) {
        notificationDAO.cancelNotification(id)
        CoroutineScope(Dispatchers.IO).launch {
            failedDownloadDAO.deleteById(id)
            publishFailedDownloadDeleted(id)
        }
    }

    fun getFailedDownloads(): LiveData<List<FailedDownload>> {
        return Transformations.map(failedDownloadDAO.getFailedDownloads()) {
            it.asFailedDownload()
        }
    }

    fun refreshDownloads() {
        eventDAO.broadcastEvent(
            ACTION_DOWNLOADS_REFRESH, 0,
            mapOf()
        )
    }

    private fun saveFailedDownload(failedDownloadEntity: FailedDownloadEntity) {
        failedDownloadDAO.upsert(failedDownloadEntity)
    }

    private fun notifyDownloadFailed(id: Long) {
        failedDownloadDAO.getById(id).let {
            notificationDAO.sendNotification(

                NotificationChannelType.DOWNLOAD_FAILED.channelId,
                it.id,
                R.drawable.ic_baseline_dangerous_48,
                R.color.midnight_green,
                resourceDAO.getStringResource(R.string.notification_title_file_download_failed),
                it.title,
                listOf(
                    it.title,
                    it.domain,
                    resourceDAO.getStringResource(it.reason.reason)
                ),
                true,
                NotificationCompat.PRIORITY_DEFAULT,
                listOf(R.id.navigation_failed),
                listOf(null),
                listOf(
                    NotificationCompat.Action.Builder(
                        0,
                        resourceDAO.getStringResource(R.string.notification_action_label_retry),
                        eventDAO.createEvent(
                            ACTION_FAILED_DOWNLOAD_RETRY, 0,
                            mapOf(Constants.EXTRA_DOWNLOAD_ID to it.id)
                        )
                    ).build(),

                    NotificationCompat.Action.Builder(
                        0,
                        resourceDAO.getStringResource(R.string.notification_action_label_delete),
                        eventDAO.createEvent(
                            ACTION_FAILED_DOWNLOAD_DELETE, 0,
                            mapOf(Constants.EXTRA_DOWNLOAD_ID to it.id)
                        )
                    ).build()
                )

            )
        }
    }

    private fun publishFailedDownloadRetry(id: Long) {
        eventDAO.broadcastEvent(
            ACTION_DOWNLOAD_CLONE, 0,
            mapOf(Constants.EXTRA_DOWNLOAD_ID to id)
        )
    }

    private fun publishFailedDownloadDeleted(id: Long) {
        eventDAO.broadcastEvent(
            ACTION_DOWNLOAD_DELETE, 0,
            mapOf(Constants.EXTRA_DOWNLOAD_ID to id)
        )
    }

    private fun Download?.asFailedDownloadEntity(): FailedDownloadEntity? {
        this?.let {
            return FailedDownloadEntity(
                id,
                failedReasonMap[reason] ?: FailedReason.HTTP_ERROR,
                Timestamp(Date().time)
            )
        }
        return null
    }

    private fun List<FailedDownloadView>.asFailedDownload(): List<FailedDownload> {
        return map {
            FailedDownload(
                it.id,
                it.title,
                it.domain,
                it.link,
                it.reason,
                Date(it.startedAt.time)
            )
        }
    }

}