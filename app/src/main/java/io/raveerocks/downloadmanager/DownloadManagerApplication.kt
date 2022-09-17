package io.raveerocks.downloadmanager

import android.app.Application
import android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE
import android.content.IntentFilter
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOADS_REFRESH
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_ADD
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_CLONE
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_DELETE
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_FAILURE
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_DOWNLOAD_SUCCESS
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_FAILED_DOWNLOAD_DELETE
import io.raveerocks.downloadmanager.core.helpers.Constants.ACTION_FAILED_DOWNLOAD_RETRY
import io.raveerocks.downloadmanager.registries.BroadcastReceiverRegistry
import io.raveerocks.downloadmanager.registries.RepositoryRegistry
import timber.log.Timber

class DownloadManagerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(object : Timber.DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                super.log(priority, "App/$tag", message, t)
            }
        })

        Timber.i("Initialising the RepositoryRegistry.")
        RepositoryRegistry.init(applicationContext)
        Timber.i("Completed initialising RepositoryRegistry.")

        Timber.i("Registering broadcast receivers.")
        registerReceiver(BroadcastReceiverRegistry.downloadBroadcastReceiver, IntentFilter().apply {
            addAction(ACTION_DOWNLOAD_COMPLETE)
            addAction(ACTION_DOWNLOAD_DELETE)
            addAction(ACTION_DOWNLOAD_CLONE)
            addAction(ACTION_DOWNLOADS_REFRESH)
        })

        registerReceiver(
            BroadcastReceiverRegistry.activeDownloadBroadcastReceiver,
            IntentFilter().apply {
                addAction(ACTION_DOWNLOAD_ADD)
                addAction(ACTION_DOWNLOAD_SUCCESS)
                addAction(ACTION_DOWNLOAD_FAILURE)
            })

        registerReceiver(
            BroadcastReceiverRegistry.doneDownloadBroadcastReceiver,
            IntentFilter().apply {
                addAction(ACTION_DOWNLOAD_SUCCESS)
            })

        registerReceiver(
            BroadcastReceiverRegistry.failedDownloadBroadcastReceiver,
            IntentFilter().apply {
                addAction(ACTION_DOWNLOAD_FAILURE)
                addAction(ACTION_FAILED_DOWNLOAD_DELETE)
                addAction(ACTION_FAILED_DOWNLOAD_RETRY)
            })
        Timber.i("Completed registering broadcast receivers.")

    }
}

