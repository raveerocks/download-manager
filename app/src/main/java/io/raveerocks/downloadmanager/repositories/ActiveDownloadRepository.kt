package io.raveerocks.downloadmanager.repositories

import android.app.DownloadManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.raveerocks.downloadmanager.core.entities.ActiveDownloadEntity
import io.raveerocks.downloadmanager.core.entities.views.ActiveDownloadView
import io.raveerocks.downloadmanager.core.helpers.Constants
import io.raveerocks.downloadmanager.core.models.ActiveDownload
import io.raveerocks.downloadmanager.core.types.ActiveDownloadStatus
import io.raveerocks.downloadmanager.core.types.MediaType
import io.raveerocks.downloadmanager.core.types.PauseReason
import io.raveerocks.downloadmanager.data.db.dao.ActiveDownloadDAO
import io.raveerocks.downloadmanager.data.os.dao.DownloadsDAO
import io.raveerocks.downloadmanager.data.os.dao.EventDAO
import io.raveerocks.downloadmanager.core.entities.Download
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActiveDownloadRepository(
    private val activeDownloadDAO: ActiveDownloadDAO,
    private val downloadsDAO: DownloadsDAO,
    private val eventDAO: EventDAO,
) {

    companion object {
        private val activeDownloadStatusMap = hashMapOf(
            -1 to ActiveDownloadStatus.NO_PROGRESS,
            DownloadManager.STATUS_PENDING to ActiveDownloadStatus.NO_PROGRESS,
            DownloadManager.STATUS_RUNNING to ActiveDownloadStatus.IN_PROGRESS,
            DownloadManager.STATUS_PAUSED to ActiveDownloadStatus.PAUSED,
            DownloadManager.STATUS_SUCCESSFUL to ActiveDownloadStatus.DONE,
            DownloadManager.STATUS_FAILED to ActiveDownloadStatus.FAILED,
        )
        private val mediaTypeMap = hashMapOf(
            "audio" to MediaType.AUDIO,
            "image" to MediaType.IMAGE,
            "video" to MediaType.VIDEO,
            "text" to MediaType.TEXT,
        )
        private val pauseReasonMap = hashMapOf(
            DownloadManager.PAUSED_WAITING_TO_RETRY to PauseReason.WAITING_TO_RETRY,
            DownloadManager.PAUSED_WAITING_FOR_NETWORK to PauseReason.WAITING_FOR_NETWORK,
            DownloadManager.PAUSED_QUEUED_FOR_WIFI to PauseReason.QUEUED_FOR_WIFI,
            DownloadManager.PAUSED_UNKNOWN to PauseReason.UNKNOWN,
        )
    }

    fun addActiveDownload(id: Long) {
        val activeDownloadEntity = ActiveDownloadEntity(id)
        CoroutineScope(Dispatchers.IO).launch {
            saveActiveDownload(activeDownloadEntity)
        }
    }

    fun deleteActiveDownload(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            activeDownloadDAO.deleteById(id)
        }
    }

    fun cancelActiveDownload(id: Long) {
        downloadsDAO.cancelDownload(id)
        CoroutineScope(Dispatchers.IO).launch {
            activeDownloadDAO.deleteById(id)
            publishActiveDownloadCanceled(id)
        }
    }

    fun getActiveDownloads(): LiveData<List<ActiveDownload>> {
        return Transformations
            .map(activeDownloadDAO.getActiveDownloads()) {
                it.asActiveDownload()
            }
    }

    fun refreshDownloads() {
        eventDAO.broadcastEvent(
            Constants.ACTION_DOWNLOADS_REFRESH, 0,
            mapOf()
        )
    }

    private fun saveActiveDownload(activeDownloadEntity: ActiveDownloadEntity) {
        activeDownloadDAO.upsert(activeDownloadEntity)
    }

    private fun publishActiveDownloadCanceled(id: Long) {
        eventDAO.broadcastEvent(
            Constants.ACTION_DOWNLOAD_DELETE,
            0,
            mapOf(Constants.EXTRA_DOWNLOAD_ID to id)
        )
    }

    private fun List<ActiveDownloadView>.asActiveDownload(): List<ActiveDownload> {
        return map {
            ActiveDownload(
                it.id,
                it.title,
                it.domain,
                it.link,
                it.startedAt,
                this@ActiveDownloadRepository::getActiveDownloadDetails
            )
        }
    }

    private fun getActiveDownloadDetails(id: Long): ActiveDownload.ActiveDownloadDetails {
        return downloadsDAO.getDownload(id).asActiveDownloadDetails()
    }

    private fun Download?.asActiveDownloadDetails(): ActiveDownload.ActiveDownloadDetails {
        this?.let {
            return ActiveDownload.ActiveDownloadDetails(
                activeDownloadStatusMap[status]!!,
                mediaTypeMap[mediaType?.split("/")?.get(0)] ?: MediaType.OTHER,
                (totalSizeBytes ?: "0").toFloat(),
                (totalBytesDownloadedSoFar ?: "0").toFloat(),
                if (status == DownloadManager.STATUS_PAUSED) pauseReasonMap[reason] else null
            )
        }
        return ActiveDownload.ActiveDownloadDetails(
            ActiveDownloadStatus.NO_PROGRESS,
            null,
            0f,
            0f,
            null
        )
    }

}