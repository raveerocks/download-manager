package io.raveerocks.downloadmanager.receivers

import io.raveerocks.downloadmanager.repositories.DownloadRepository

class DownloadBroadcastReceiverImpl private constructor(private val downloadRepository: DownloadRepository) :
    DownloadBroadcastReceiver() {

    companion object {
        fun from(downloadRepository: DownloadRepository): DownloadBroadcastReceiver {
            return DownloadBroadcastReceiverImpl(downloadRepository)
        }
    }

    override fun refreshDownload(id: Long) {
        downloadRepository.refreshDownload(id)
    }

    override fun cloneDownload(id: Long) {
        downloadRepository.cloneDownload(id)
    }

    override fun deleteDownload(id: Long) {
        downloadRepository.deleteDownload(id)
    }

    override fun refreshDownloads() {
        downloadRepository.refreshDownloads()
    }
}