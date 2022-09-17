package io.raveerocks.downloadmanager.core.models


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.raveerocks.downloadmanager.core.types.ActiveDownloadStatus
import io.raveerocks.downloadmanager.core.types.MediaType
import io.raveerocks.downloadmanager.core.types.PauseReason
import java.sql.Timestamp


class ActiveDownload constructor(
    val id: Long,
    val title: String,
    val domain: String,
    val link: String,
    var startedAt: Timestamp,
    val getActiveDownloadDetails: (id: Long) -> ActiveDownloadDetails
) : BaseModel(id) {

    class ActiveDownloadDetails(
        val status: ActiveDownloadStatus,
        val mediaType: MediaType?,
        val totalSizeBytes: Float,
        val totalBytesDownloadedSoFar: Float,
        val pauseReason: PauseReason?
    )

    val details: LiveData<ActiveDownloadDetails>
        get() = _details
    private val _details = MutableLiveData<ActiveDownloadDetails>()

    init {
        refresh()
    }

    fun refresh() {
        _details.postValue(getActiveDownloadDetails(id))
    }

}