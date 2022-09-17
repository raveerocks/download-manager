package io.raveerocks.downloadmanager.ui.active

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.raveerocks.downloadmanager.core.models.ActiveDownload
import io.raveerocks.downloadmanager.registries.RepositoryRegistry
import io.raveerocks.downloadmanager.repositories.ActiveDownloadRepository

class ActiveViewModel : ViewModel() {

    companion object {
        private val activeDownloadRepository: ActiveDownloadRepository by lazy { RepositoryRegistry.activeDownloadRepository }
    }

    val activeDownloads: LiveData<List<ActiveDownload>>
    val downloadCanceledEvent: LiveData<Boolean>
        get() = _downloadCanceledEvent

    private val _downloadCanceledEvent = MutableLiveData(false)

    init {
        activeDownloads = getDownloads()
    }

    fun cancelDownload(activeDownload: ActiveDownload) {
        activeDownloadRepository.cancelActiveDownload(activeDownload.id)
        _downloadCanceledEvent.value = true
    }

    fun downloadCanceledDone() {
        _downloadCanceledEvent.value = false
    }

    private fun getDownloads(): LiveData<List<ActiveDownload>> {
        return activeDownloadRepository.getActiveDownloads()
    }

    fun refreshDownloads() {
        activeDownloadRepository.refreshDownloads()
    }

}