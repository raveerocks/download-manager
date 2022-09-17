package io.raveerocks.downloadmanager.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.raveerocks.downloadmanager.core.helpers.Constants
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_SUCCESS

abstract class DoneDownloadBroadcastReceiver : BroadcastReceiver() {

    abstract fun addDoneDownload(id: Long)

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.getLongExtra(Constants.EXTRA_DOWNLOAD_ID, -1L)?.let {
            when (intent.action) {
                ACTION_DOWNLOAD_SUCCESS -> addDoneDownload(it)
                else -> throw RuntimeException("Unknown event received")
            }
        }
    }
}