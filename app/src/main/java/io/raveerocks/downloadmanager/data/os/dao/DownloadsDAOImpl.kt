package io.raveerocks.downloadmanager.data.os.dao

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import io.raveerocks.downloadmanager.core.entities.Download

class DownloadsDAOImpl(private val context: Context) : DownloadsDAO {

    private val downloadManager: DownloadManager by lazy {
        context.getSystemService(
            AppCompatActivity.DOWNLOAD_SERVICE
        ) as DownloadManager
    }

    override fun createDownload(
        uri: Uri,
        fileName: String,
        isPublic: Boolean,
        wifiOnly: Boolean,
        whileRoaming: Boolean,
        whileChargingOnly: Boolean,
        whileIdleOnly: Boolean
    ): Long {
        return addDownload(
            uri,
            fileName,
            isPublic,
            wifiOnly,
            whileRoaming,
            whileChargingOnly,
            whileIdleOnly
        )
    }

    override fun getDownload(id: Long): Download? {
        val cursor = downloadManager.query(DownloadManager
            .Query()
            .apply {
                setFilterById(id)
            }
        )
        if (cursor.moveToFirst()) {
            return getDownload(cursor, id)
        }
        return null
    }

    override fun cancelDownload(id: Long) {
        downloadManager.remove(id)
    }

    private fun addDownload(
        uri: Uri,
        fileName: String,
        isPublic: Boolean,
        wifiOnly: Boolean,
        whileRoaming: Boolean,
        whileChargingOnly: Boolean,
        whileIdleOnly: Boolean
    ): Long {

        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)


        if (isPublic) {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        } else {
            request.setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_DOWNLOADS,
                fileName
            )
        }
        request.setAllowedOverMetered(!wifiOnly)
        request.setAllowedOverRoaming(whileRoaming)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request.setRequiresCharging(whileChargingOnly)
            request.setRequiresDeviceIdle(whileIdleOnly)
        }
        return downloadManager.enqueue(request)
    }

    private fun getDownload(
        cursor: Cursor,
        id: Long
    ): Download {
        val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
        val locationIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
        val mediaTypeIndex = cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE)
        val totalSizeBytesIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
        val totalBytesDownloadedSoFarIndex =
            cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)

        val reasonIndex = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
        return when (cursor.getInt(statusIndex)) {
            DownloadManager.STATUS_PENDING -> Download(
                id,
                cursor.getInt(statusIndex),
                cursor.getString(locationIndex),
                cursor.getString(mediaTypeIndex),
                cursor.getString(totalSizeBytesIndex),
                cursor.getString(totalBytesDownloadedSoFarIndex),
                null
            )
            DownloadManager.STATUS_RUNNING -> Download(
                id,
                cursor.getInt(statusIndex),
                cursor.getString(locationIndex),
                cursor.getString(mediaTypeIndex),
                cursor.getString(totalSizeBytesIndex),
                cursor.getString(totalBytesDownloadedSoFarIndex),
                null
            )
            DownloadManager.STATUS_PAUSED -> Download(
                id,
                cursor.getInt(statusIndex),
                null,
                null,
                null,
                null,
                cursor.getInt(reasonIndex)
            )
            DownloadManager.STATUS_SUCCESSFUL -> Download(
                id,
                cursor.getInt(statusIndex),
                cursor.getString(locationIndex),
                cursor.getString(mediaTypeIndex),
                cursor.getString(totalSizeBytesIndex),
                cursor.getString(totalBytesDownloadedSoFarIndex),
                null
            )
            DownloadManager.STATUS_FAILED -> Download(
                id,
                cursor.getInt(statusIndex),
                null,
                null,
                null,
                null,
                cursor.getInt(reasonIndex)
            )
            else -> throw RuntimeException("Invalid status")
        }
    }
}