package io.raveerocks.downloadmanager

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import io.raveerocks.downloadmanager.core.helpers.MemoryUtil.findSizeString
import io.raveerocks.downloadmanager.core.helpers.TimeDiffUtil
import io.raveerocks.downloadmanager.core.models.ActiveDownload
import io.raveerocks.downloadmanager.core.types.*
import io.raveerocks.downloadmanager.core.views.custom.*
import java.text.SimpleDateFormat
import java.util.*


private val pauseTypeMap = hashMapOf(
    PauseReason.WAITING_TO_RETRY to R.drawable.ic_baseline_refresh_48,
    PauseReason.WAITING_FOR_NETWORK to R.drawable.ic_baseline_signal_cellular_connected_no_internet_4_bar_48,
    PauseReason.QUEUED_FOR_WIFI to R.drawable.ic_baseline_signal_wifi_statusbar_connected_no_internet_4_48,
    PauseReason.UNKNOWN to R.drawable.ic_baseline_question_mark_48
)
private val mimeTypeMap = hashMapOf(
    MediaType.AUDIO to R.drawable.ic_baseline_library_music_48,
    MediaType.IMAGE to R.drawable.ic_baseline_photo_library_48,
    MediaType.VIDEO to R.drawable.ic_baseline_video_library_48,
    MediaType.TEXT to R.drawable.ic_baseline_text_library_48,
    MediaType.OTHER to R.drawable.ic_baseline_question_mark_48,
)
private val failureTypeMap = hashMapOf(
    FailedReason.FILE_ERROR to R.drawable.ic_baseline_insert_drive_file_48,
    FailedReason.NETWORK_ERROR to R.drawable.ic_baseline_signal_cellular_connected_no_internet_4_bar_48,
    FailedReason.MEMORY_ERROR to R.drawable.ic_baseline_disc_full_48,
    FailedReason.CANNOT_RESUME_ERROR to R.drawable.ic_baseline_incomplete_circle_48,
    FailedReason.FILE_ALREADY_EXISTS_ERROR to R.drawable.ic_baseline_file_copy_48,
    FailedReason.HTTP_ERROR to R.drawable.ic_baseline_cancel_schedule_send_48,
    FailedReason.UNKNOWN_ERROR to R.drawable.ic_baseline_question_mark_48
)

private const val dateDisplayFormat = "dd MMM yyyy, hh:mm:ss aa"


@BindingAdapter("isVisible")
fun setIsVisible(view: View, isVisible: Boolean?) {
    isVisible?.let {
        if (it) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }
}

@BindingAdapter("status")
fun setStatus(downloadStatusView: DownloadStatusPieChartView, data: Map<DownloadStatus, Int>?) {
    data?.let {
        downloadStatusView.submit(it)
    }
}

@BindingAdapter("status")
fun setStatus(downloadStatusView: DownloadStatusBarChartView, data: Map<DownloadStatus, Int>?) {
    data?.let {
        downloadStatusView.submit(it)
    }
}

@BindingAdapter("distribution")
fun setDistribution(
    mediaDistributionView: MediaDistributionPieChartView,
    data: Map<MediaType, Float>?
) {
    data?.let {
        mediaDistributionView.submit(it)
    }
}

@BindingAdapter("distribution")
fun setDistribution(
    mediaDistributionView: MediaDistributionBarChartView,
    data: Map<MediaType, Float>?
) {
    data?.let {
        mediaDistributionView.submit(it)
    }
}

@BindingAdapter("progress")
fun setProgress(
    progressPieChartView: ProgressPieChartView,
    progress: ActiveDownload.ActiveDownloadDetails?
) {
    progress?.let {
        if (it.status == ActiveDownloadStatus.IN_PROGRESS) {
            progressPieChartView.submit(it)
            progressPieChartView.visibility = View.VISIBLE
        } else {
            progressPieChartView.visibility = View.INVISIBLE
        }
    }
}

@BindingAdapter("progress")
fun setProgress(
    imageView: ImageView,
    activeDownloadDetails: ActiveDownload.ActiveDownloadDetails?
) {
    activeDownloadDetails?.let {
        when (it.status) {
            ActiveDownloadStatus.IN_PROGRESS -> {
                imageView.visibility = View.INVISIBLE
            }
            ActiveDownloadStatus.NO_PROGRESS -> {
                imageView.setImageResource(R.drawable.ic_baseline_not_started_48)
                imageView.visibility = View.VISIBLE
            }
            ActiveDownloadStatus.PAUSED -> {
                pauseTypeMap[it.pauseReason ?: PauseReason.UNKNOWN]?.let { pauseReasonImage ->
                    imageView.setImageResource(pauseReasonImage)
                }
                imageView.visibility = View.VISIBLE
            }
            ActiveDownloadStatus.DONE -> {}
            ActiveDownloadStatus.FAILED -> {
                imageView.setImageResource(R.drawable.ic_baseline_running_with_errors_48)
                imageView.visibility = View.VISIBLE
            }
        }
    }

}

@BindingAdapter("downloadingFrom")
fun setDownloadingFrom(textView: TextView, domain: String?) {
    domain?.let {
        textView.text = String.format(
            textView.context.getString(R.string.label_downloading_from_format), it
        )
    }
}


@BindingAdapter("mimeType")
fun setMimeType(imageView: ImageView, mediaType: MediaType?) {
    mimeTypeMap[mediaType]?.let {
        imageView.setImageResource(it)
    }
}

@BindingAdapter("downloadedFrom")
fun setDownloadedFrom(textView: TextView, domain: String?) {
    domain?.let {
        textView.text = String.format(
            textView.context.getString(R.string.label_downloaded_from_format), it
        ).trimIfLargerThan(30)
    }
}

@BindingAdapter("fileSize")
fun setFileSize(textView: TextView, size: Float?) {
    size?.let {
        textView.text = size.findSizeString()
    }
}

@BindingAdapter("timeAgo")
fun setTimeAgo(textView: TextView, timeAgo: TimeDiffUtil.TimeAgo?) {
    timeAgo?.let {
        if (timeAgo.unit == TimeGranularity.SECOND) {
            textView.text = textView.context.getString(R.string.description_now)
        } else {
            textView.text = String.format(
                textView.context.getString(R.string.label_time_ago_format),
                timeAgo.diff,
                textView.context.getString(timeAgo.unit.descriptor)
            )
        }
    }
}

@BindingAdapter("downloadedAt")
fun setDownloadedAt(textView: TextView, date: Date?) {
    date?.let {
        textView.text =
            SimpleDateFormat(dateDisplayFormat, Locale.getDefault()).format(date)
    }
}

@BindingAdapter("failureReason")
fun setErrorType(imageView: ImageView, failureReason: FailedReason?) {
    failureReason?.let {
        failureTypeMap[failureReason]?.let {
            imageView.setImageResource(it)
        }
    }
}

@BindingAdapter("failureDetailedReason")
fun setDownloadedFrom(textView: TextView, failedReason: FailedReason?) {
    failedReason?.let {
        textView.text = String.format(
            textView.context.getString(failedReason.reason), it
        ).trimIfLargerThan(30)
    }
}

private fun String.trimIfLargerThan(i: Int): String {
    if (i >= length) {
        return this
    }
    return "${substring(0, i)}..."
}