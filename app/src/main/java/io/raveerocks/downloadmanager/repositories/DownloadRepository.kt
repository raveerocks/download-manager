package io.raveerocks.downloadmanager.repositories

import android.net.Uri
import io.raveerocks.downloadmanager.core.entities.DownloadEntity
import io.raveerocks.downloadmanager.core.helpers.Constants
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_ADD
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_FAILURE
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_SUCCESS
import io.raveerocks.downloadmanager.core.types.DownloadStatus
import io.raveerocks.downloadmanager.data.db.dao.DownloadDAO
import io.raveerocks.downloadmanager.data.os.dao.ClipboardDAO
import io.raveerocks.downloadmanager.data.os.dao.DownloadsDAO
import io.raveerocks.downloadmanager.data.os.dao.EventDAO
import io.raveerocks.downloadmanager.data.os.dao.PreferenceDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.*

class DownloadRepository(
    private val clipboardDAO: ClipboardDAO,
    private val downloadDAO: DownloadDAO,
    private val downloadsDAO: DownloadsDAO,
    private val eventDAO: EventDAO,
    private val preferenceDAO: PreferenceDAO,
) {

    companion object {
        private const val WWW = "www."
    }

    fun addDownload(title: String, link: String): Boolean {
        try {
            val now = Date().time
            val uri = Uri.parse(link)
            val path = uri.path
            val fileName = path?.substring(path.lastIndexOf("/") + 1) ?: "Unknown $now"
            val id = downloadsDAO.createDownload(
                uri, fileName,
                preferenceDAO.getDownloadFilePublic(),
                preferenceDAO.getDownloadOverWifiOnly(),
                preferenceDAO.getDownloadWhileRoaming(),
                preferenceDAO.getDownloadOverWhileChargingOnly(),
                preferenceDAO.getDownloadOverWhileIdleOnly()
            )
            val domain = uri.host?.removePrefix(WWW)!!
            val downloadEntity = DownloadEntity(
                id,
                title,
                domain,
                link,
                Timestamp(now),
                DownloadStatus.ACTIVE
            )
            CoroutineScope(Dispatchers.IO).launch {
                saveDownload(downloadEntity)
                publishDownloadAdded(downloadEntity.id)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun deleteDownload(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            downloadDAO.deleteById(id)
        }
    }

    fun cloneDownload(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            downloadDAO.getById(id).apply {
                addDownload(title, link)
                deleteDownload(id)
            }
        }
    }

    fun refreshDownload(id: Long) {
        downloadsDAO.getDownload(id)?.let {
            if (it.status == android.app.DownloadManager.STATUS_SUCCESSFUL) {
                CoroutineScope(Dispatchers.IO).launch {
                    updateDownloadStatus(id, DownloadStatus.DONE)
                    publishDownloadSuccessful(id)
                }
            }
            if (it.status == android.app.DownloadManager.STATUS_FAILED) {
                CoroutineScope(Dispatchers.IO).launch {
                    updateDownloadStatus(id, DownloadStatus.FAILED)
                    publishDownloadFailed(id)
                }
            }
        }
    }

    fun getCurrentClipText(): String {
        return clipboardDAO.getCurrentText()
    }

    private fun saveDownload(downloadEntity: DownloadEntity) {
        downloadDAO.upsert(downloadEntity)
    }

    private fun updateDownloadStatus(id: Long, downloadStatus: DownloadStatus) {
        downloadDAO.updateStatus(id, downloadStatus)
    }

    private fun publishDownloadAdded(id: Long) {
        eventDAO.broadcastEvent(
            ACTION_DOWNLOAD_ADD, 0,
            mapOf(Constants.EXTRA_DOWNLOAD_ID to id)
        )
    }

    private fun publishDownloadSuccessful(id: Long) {
        eventDAO.broadcastEvent(
            ACTION_DOWNLOAD_SUCCESS, 0,
            mapOf(Constants.EXTRA_DOWNLOAD_ID to id)
        )
    }

    private fun publishDownloadFailed(id: Long) {
        eventDAO.broadcastEvent(
            ACTION_DOWNLOAD_FAILURE, 0,
            mapOf(Constants.EXTRA_DOWNLOAD_ID to id)
        )
    }

    fun refreshDownloads() {
        CoroutineScope(Dispatchers.IO).launch {
            downloadDAO.getDownloadsByStatus(DownloadStatus.ACTIVE).forEach { refreshDownload(it) }
        }
    }

}