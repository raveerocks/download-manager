package io.raveerocks.downloadmanager.receivers

import io.raveerocks.downloadmanager.repositories.FailedDownloadRepository

class FailedDownloadBroadcastReceiverImpl private constructor(private val failedDownloadRepository: FailedDownloadRepository) :
    FailedDownloadBroadcastReceiver() {

    companion object {
        fun from(failedDownloadRepository: FailedDownloadRepository): FailedDownloadBroadcastReceiver {
            return FailedDownloadBroadcastReceiverImpl(failedDownloadRepository)
        }
    }

    override fun addFailedDownload(id: Long) {
        failedDownloadRepository.addFailedDownload(id)
    }

    override fun deleteFailedDownload(id: Long) {
        failedDownloadRepository.deleteFailedDownload(id)
    }

    override fun retryFailedDownload(id: Long) {
        failedDownloadRepository.retryFailedDownload(id)
    }
}