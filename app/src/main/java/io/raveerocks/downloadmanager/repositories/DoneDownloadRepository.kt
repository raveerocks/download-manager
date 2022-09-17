package io.raveerocks.downloadmanager.repositories


import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.raveerocks.downloadmanager.R
import io.raveerocks.downloadmanager.core.entities.DoneDownloadEntity
import io.raveerocks.downloadmanager.core.entities.views.DoneDownloadView
import io.raveerocks.downloadmanager.core.helpers.Constants
import io.raveerocks.downloadmanager.core.helpers.MemoryUtil.findSizeString
import io.raveerocks.downloadmanager.core.models.DoneDownload
import io.raveerocks.downloadmanager.core.types.MediaType
import io.raveerocks.downloadmanager.core.types.NotificationChannelType
import io.raveerocks.downloadmanager.data.db.dao.DoneDownloadDAO
import io.raveerocks.downloadmanager.data.os.dao.*
import io.raveerocks.downloadmanager.core.entities.Download
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.*

class DoneDownloadRepository(
    private val doneDownloadDAO: DoneDownloadDAO,
    private val downloadsDAO: DownloadsDAO,
    private val eventDAO: EventDAO,
    private val filesDAO: FilesDAO,
    private val notificationDAO: NotificationDAO,
    private val resourceDAO: ResourceDAO
) {

    companion object {
        private val mediaTypeMap = hashMapOf(
            "audio" to MediaType.AUDIO,
            "image" to MediaType.IMAGE,
            "video" to MediaType.VIDEO,
            "text" to MediaType.TEXT,
        )
    }

    init {
        notificationDAO.createChannel(NotificationChannelType.DOWNLOAD_DONE)
    }

    fun addDoneDownload(id: Long) {
        downloadsDAO.getDownload(id).asDoneDownloadEntity()?.let {
            CoroutineScope(Dispatchers.IO).launch {
                saveDoneDownload(it)
                notifyDownloadSuccessful(id)
            }
        }
    }

    fun deleteDoneDownload(doneDownload: DoneDownload) {
        filesDAO.deleteFile(doneDownload.location)
        CoroutineScope(Dispatchers.IO).launch {
            doneDownloadDAO.deleteById(doneDownload.id)
            publishDoneDownloadDeleted(doneDownload.id)
        }
    }

    fun openDoneDownload(doneDownload: DoneDownload): Boolean {
        return filesDAO.openFile(doneDownload.location)
    }

    fun getDoneDownloads(
        orderBy: Int,
        order: Int,
        mediaType: Set<MediaType>
    ): LiveData<List<DoneDownload>> {
        return Transformations.map(doneDownloadDAO.getDoneDownloads(orderBy, order, mediaType)) {
            it.asDoneDownload()
        }
    }

    fun refreshDownloads() {
        eventDAO.broadcastEvent(
            Constants.ACTION_DOWNLOADS_REFRESH,
            0,
            mapOf()
        )
    }

    private fun saveDoneDownload(doneDownloadEntity: DoneDownloadEntity) {
        doneDownloadDAO.upsert(doneDownloadEntity)
    }

    private fun notifyDownloadSuccessful(id: Long) {
        doneDownloadDAO.getById(id).let {
            notificationDAO.sendNotification(
                NotificationChannelType.DOWNLOAD_DONE.channelId,
                it.id,
                R.drawable.ic_baseline_library_add_check_48,
                R.color.midnight_green,
                resourceDAO.getStringResource(R.string.notification_title_file_download_done),
                it.title,
                listOf(it.title, it.domain, it.totalSizeBytes.findSizeString()),
                true,
                NotificationCompat.PRIORITY_DEFAULT,
                listOf(R.id.navigation_done, R.id.navigation_done_info),
                listOf(
                    null,
                    Bundle().apply {
                        putParcelable(
                            "doneDownload",
                            listOf(it).asDoneDownload()[0]
                        )
                    }),
                null
            )
        }
    }

    private fun publishDoneDownloadDeleted(id: Long) {
        eventDAO.broadcastEvent(
            Constants.ACTION_DOWNLOAD_DELETE,
            0,
            mapOf(Constants.EXTRA_DOWNLOAD_ID to id)
        )
    }

    private fun Download?.asDoneDownloadEntity(): DoneDownloadEntity? {
        this?.let {
            return DoneDownloadEntity(
                id,
                location!!,
                mediaTypeMap[mediaType?.split("/")?.get(0)] ?: MediaType.OTHER,
                mediaType!!,
                (totalBytesDownloadedSoFar ?: "0").toFloat(),
                Timestamp(Date().time)
            )
        }
        return null
    }

    private fun List<DoneDownloadView>.asDoneDownload(): List<DoneDownload> {
        return map {
            DoneDownload(
                it.id,
                it.title,
                it.domain,
                it.link,
                it.location,
                it.mediaType,
                it.totalSizeBytes,
                Date(it.completedAt.time)
            )
        }
    }

}