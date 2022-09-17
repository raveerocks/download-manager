package io.raveerocks.downloadmanager.ui.failed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.raveerocks.downloadmanager.core.models.FailedDownload
import io.raveerocks.downloadmanager.registries.RepositoryRegistry
import io.raveerocks.downloadmanager.repositories.FailedDownloadRepository

class FailedViewModel : ViewModel() {

    companion object {
        private val failedDownloadRepository: FailedDownloadRepository by lazy { RepositoryRegistry.failedDownloadRepository }
    }

    val failedDownloads: LiveData<List<FailedDownload>>
    val downloadRetriedEvent: LiveData<Boolean>
        get() = _downloadRetriedEvent

    private val _downloadRetriedEvent = MutableLiveData(false)

    init {
        failedDownloads = getDownloads()
    }

    private fun getDownloads(): LiveData<List<FailedDownload>> {
        return failedDownloadRepository.getFailedDownloads()
    }


    fun retryDownload(failedDownload: FailedDownload) {
        failedDownloadRepository.retryFailedDownload(failedDownload.id)
        _downloadRetriedEvent.value = true
    }

    fun deleteDownload(failedDownload: FailedDownload) {
        failedDownloadRepository.deleteFailedDownload(failedDownload.id)
    }

    fun downloadRetriedDone() {
        _downloadRetriedEvent.value = false
    }

    fun refreshDownloads() {
        failedDownloadRepository.refreshDownloads()
    }

}