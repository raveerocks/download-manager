package io.raveerocks.downloadmanager.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.raveerocks.downloadmanager.core.helpers.Constants
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_ADD
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_FAILURE
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_SUCCESS

abstract class ActiveDownloadBroadcastReceiver : BroadcastReceiver() {

    abstract fun addActiveDownload(id: Long)
    abstract fun deleteActiveDownload(it: Long)

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.getLongExtra(Constants.EXTRA_DOWNLOAD_ID, -1L)?.let {
            when (intent.action) {
                ACTION_DOWNLOAD_ADD -> addActiveDownload(it)
                ACTION_DOWNLOAD_SUCCESS -> deleteActiveDownload(it)
                ACTION_DOWNLOAD_FAILURE -> deleteActiveDownload(it)
                else -> throw RuntimeException("Unknown event received")
            }
        }
    }
}