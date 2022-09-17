package io.raveerocks.downloadmanager.ui.add


import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.raveerocks.downloadmanager.registries.RepositoryRegistry
import io.raveerocks.downloadmanager.repositories.DownloadRepository

class AddDownloadViewModel : ViewModel() {

    companion object {
        private val downloadRepository: DownloadRepository by lazy { RepositoryRegistry.downloadRepository }
    }

    val title = MutableLiveData("")
    val link = MutableLiveData("")

    val downloadAddedEvent: LiveData<Boolean>
        get() = _downloadAddedEvent
    val downloadFailedEvent: LiveData<Boolean>
        get() = _downloadFailedEvent

    private val _downloadAddedEvent = MutableLiveData<Boolean>()
    private val _downloadFailedEvent = MutableLiveData<Boolean>()

    init {
        _downloadAddedEvent.value = false
        _downloadFailedEvent.value = false
        val currentClipText = downloadRepository.getCurrentClipText()
        if (Patterns.WEB_URL.matcher(currentClipText).matches()) {
            link.value = currentClipText
        }
    }

    fun addDownload() {
        if (downloadRepository.addDownload(title.value!!, link.value!!)) {
            _downloadAddedEvent.value = true
        } else {
            _downloadFailedEvent.value = true
        }
    }

    fun downloadAddedDone() {
        _downloadAddedEvent.value = false
    }

    fun downloadFailedDone() {
        _downloadFailedEvent.value = false
    }
}