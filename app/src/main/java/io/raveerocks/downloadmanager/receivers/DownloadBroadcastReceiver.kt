package io.raveerocks.downloadmanager.receivers

import android.app.DownloadManager
import android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOADS_REFRESH
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_CLONE
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_DELETE

abstract class DownloadBroadcastReceiver : BroadcastReceiver() {

    abstract fun refreshDownload(id: Long)
    abstract fun cloneDownload(id: Long)
    abstract fun deleteDownload(id: Long)
    abstract fun refreshDownloads()

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)?.let {
            when (intent.action) {
                ACTION_DOWNLOAD_COMPLETE -> {
                    refreshDownload(it)
                }
                ACTION_DOWNLOAD_DELETE -> {
                    deleteDownload(it)
                }
                ACTION_DOWNLOAD_CLONE -> {
                    cloneDownload(it)
                }
                ACTION_DOWNLOADS_REFRESH -> {
                    refreshDownloads()
                }
                else -> throw RuntimeException("Unknown event received")
            }
        }
    }
}