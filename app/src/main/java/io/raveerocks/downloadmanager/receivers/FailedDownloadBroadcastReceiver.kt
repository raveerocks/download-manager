package io.raveerocks.downloadmanager.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.raveerocks.downloadmanager.core.helpers.Constants
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_FAILURE
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_FAILED_DOWNLOAD_DELETE
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_FAILED_DOWNLOAD_RETRY

abstract class FailedDownloadBroadcastReceiver : BroadcastReceiver() {

    abstract fun addFailedDownload(id: Long)
    abstract fun deleteFailedDownload(id: Long)
    abstract fun retryFailedDownload(id: Long)

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.getLongExtra(Constants.EXTRA_DOWNLOAD_ID, -1L)?.let {
            when (intent.action) {
                ACTION_DOWNLOAD_FAILURE -> addFailedDownload(it)
                ACTION_FAILED_DOWNLOAD_DELETE -> deleteFailedDownload(it)
                ACTION_FAILED_DOWNLOAD_RETRY -> retryFailedDownload(it)
                else -> throw RuntimeException("Unknown event received")
            }
        }
    }

}

