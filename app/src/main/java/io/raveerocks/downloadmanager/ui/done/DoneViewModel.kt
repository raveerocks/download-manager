package io.raveerocks.downloadmanager.ui.done

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.raveerocks.downloadmanager.core.models.DoneDownload
import io.raveerocks.downloadmanager.core.types.MediaType
import io.raveerocks.downloadmanager.core.types.SortFieldType
import io.raveerocks.downloadmanager.registries.RepositoryRegistry
import io.raveerocks.downloadmanager.repositories.DoneDownloadRepository
import java.util.*

class DoneViewModel : ViewModel() {

    companion object {
        private val doneDownloadRepository: DoneDownloadRepository by lazy { RepositoryRegistry.doneDownloadRepository }
        private val allMedia = setOf(
            MediaType.AUDIO, MediaType.IMAGE, MediaType.VIDEO,
            MediaType.TEXT, MediaType.OTHER
        )
    }

    val doneDownloads: LiveData<List<DoneDownload>>
    val downloadOpenFailedEvent: LiveData<Boolean>
        get() = _downloadOpenFailedEvent

    private val selectedMediaType = MutableLiveData<List<MediaType>>(Collections.emptyList())
    private val selectedSort = MutableLiveData(SortFieldType.NONE)
    private val _downloadOpenFailedEvent = MutableLiveData(false)

    init {
        doneDownloads = getDownloads()
    }

    fun openDownload(doneDownload: DoneDownload) {
        _downloadOpenFailedEvent.value = !doneDownloadRepository.openDoneDownload(doneDownload)
    }

    fun deleteDownload(doneDownload: DoneDownload) {
        doneDownloadRepository.deleteDoneDownload(doneDownload)
    }

    private fun getDownloads(): LiveData<List<DoneDownload>> {
        return Transformations.switchMap(selectedMediaType) { list ->
            return@switchMap Transformations.switchMap(selectedSort) {
                doneDownloadRepository.getDoneDownloads(
                    it.column,
                    it.sortOrder,
                    HashSet(list.ifEmpty { allMedia })
                )
            }
        }
    }

    fun sortDownloads(sortBy: SortFieldType) {
        if (sortBy.isASC() && selectedSort.value == sortBy) {
            selectedSort.value = sortBy.inverse
        } else {
            selectedSort.value = sortBy
        }
    }

    fun filterDownloads(isChecked: Boolean, mediaType: MediaType) {
        val newList = ArrayList(selectedMediaType.value!!)
        if (!isChecked) {
            newList.remove(mediaType)
        } else {
            newList.add(mediaType)
        }
        selectedMediaType.value = newList
    }

    fun onDownloadOpenFailedDone() {
        _downloadOpenFailedEvent.value = false
    }

    fun refreshDownloads() {
        doneDownloadRepository.refreshDownloads()
    }

}