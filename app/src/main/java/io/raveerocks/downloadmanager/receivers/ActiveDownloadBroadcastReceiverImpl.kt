package io.raveerocks.downloadmanager.receivers

import io.raveerocks.downloadmanager.repositories.ActiveDownloadRepository


class ActiveDownloadBroadcastReceiverImpl private constructor(private val activeDownloadRepository: ActiveDownloadRepository) :
    ActiveDownloadBroadcastReceiver() {

    companion object {
        fun from(activeDownloadRepository: ActiveDownloadRepository): ActiveDownloadBroadcastReceiver {
            return ActiveDownloadBroadcastReceiverImpl(activeDownloadRepository)
        }
    }

    override fun addActiveDownload(id: Long) {
        activeDownloadRepository.addActiveDownload(id)
    }

    override fun deleteActiveDownload(it: Long) {
        activeDownloadRepository.deleteActiveDownload(it)
    }


}