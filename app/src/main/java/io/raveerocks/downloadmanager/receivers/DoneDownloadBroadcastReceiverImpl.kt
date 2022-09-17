package io.raveerocks.downloadmanager.receivers

import io.raveerocks.downloadmanager.repositories.DoneDownloadRepository

class DoneDownloadBroadcastReceiverImpl private constructor(private val doneDownloadRepository: DoneDownloadRepository) :
    DoneDownloadBroadcastReceiver() {
    companion object {
        fun from(doneDownloadRepository: DoneDownloadRepository): DoneDownloadBroadcastReceiver {
            return DoneDownloadBroadcastReceiverImpl(doneDownloadRepository)
        }
    }

    override fun addDoneDownload(id: Long) {
        doneDownloadRepository.addDoneDownload(id)
    }
}