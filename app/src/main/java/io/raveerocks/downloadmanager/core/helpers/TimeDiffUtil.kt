package io.raveerocks.downloadmanager.core.helpers

import io.raveerocks.downloadmanager.core.types.TimeGranularity
import io.raveerocks.downloadmanager.core.types.TimeGranularity.*
import java.text.SimpleDateFormat
import java.util.*

object TimeDiffUtil {

    private const val format = "dd MMM"

    class TimeAgo(val diff: String, val unit: TimeGranularity)

    fun Date.getTimeAgo(): TimeAgo {
        val timeDiffInMillis: Long = Date().time - time

        return if (timeDiffInMillis / MONTH.toMillis() > 0) {
            TimeAgo(SimpleDateFormat(format, Locale.getDefault()).format(this), MONTH)
        } else if (timeDiffInMillis / WEEK.toMillis() > 0) {
            TimeAgo((timeDiffInMillis / WEEK.toMillis()).toString(), WEEK)
        } else if (timeDiffInMillis / DAY.toMillis() > 0) {
            TimeAgo((timeDiffInMillis / DAY.toMillis()).toString(), DAY)
        } else if (timeDiffInMillis / HOUR.toMillis() > 0) {
            TimeAgo((timeDiffInMillis / HOUR.toMillis()).toString(), HOUR)
        } else if (timeDiffInMillis / MINUTE.toMillis() > 0) {
            TimeAgo((timeDiffInMillis / MINUTE.toMillis()).toString(), MINUTE)
        } else {
            TimeAgo((timeDiffInMillis / SECOND.toMillis()).toString(), SECOND)
        }
    }
}