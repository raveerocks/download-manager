package io.raveerocks.downloadmanager.registries

import io.raveerocks.downloadmanager.receivers.*


object BroadcastReceiverRegistry {

    val downloadBroadcastReceiver: DownloadBroadcastReceiver by lazy {
        DownloadBroadcastReceiverImpl.from(RepositoryRegistry.downloadRepository)
    }

    val activeDownloadBroadcastReceiver: ActiveDownloadBroadcastReceiver by lazy {
        ActiveDownloadBroadcastReceiverImpl.from(RepositoryRegistry.activeDownloadRepository)
    }

    val doneDownloadBroadcastReceiver: DoneDownloadBroadcastReceiver by lazy {
        DoneDownloadBroadcastReceiverImpl.from(RepositoryRegistry.doneDownloadRepository)
    }

    val failedDownloadBroadcastReceiver: FailedDownloadBroadcastReceiver by lazy {
        FailedDownloadBroadcastReceiverImpl.from(RepositoryRegistry.failedDownloadRepository)
    }

}