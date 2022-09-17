package io.raveerocks.downloadmanager.core.models

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.raveerocks.downloadmanager.core.helpers.TimeDiffUtil.TimeAgo
import io.raveerocks.downloadmanager.core.helpers.TimeDiffUtil.getTimeAgo
import io.raveerocks.downloadmanager.core.types.FailedReason
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class FailedDownload constructor(
    val id: Long,
    val title: String,
    val domain: String,
    val link: String,
    val reason: FailedReason,
    private val downloadedAt: Date
) : BaseModel(id), Parcelable {

    @IgnoredOnParcel
    val downloadedAtElapsed: LiveData<TimeAgo>
        get() = _downloadedAtElapsed

    @IgnoredOnParcel
    private val _downloadedAtElapsed = MutableLiveData<TimeAgo>()

    init {
        refresh()
    }

    fun refresh() {
        _downloadedAtElapsed.postValue(downloadedAt.getTimeAgo())
    }
}