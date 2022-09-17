package io.raveerocks.downloadmanager.core.entities.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.TypeConverters
import io.raveerocks.downloadmanager.core.helpers.TimestampConverter
import io.raveerocks.downloadmanager.core.types.FailedReason
import java.sql.Timestamp

@DatabaseView(
    "SELECT failed_downloads.id,title,domain,link,started_at,reason,failed_at " +
            "FROM failed_downloads INNER JOIN downloads " +
            "ON failed_downloads.id = downloads.id",
    viewName = "failed_downloads_view"
)
@TypeConverters(TimestampConverter::class)
class FailedDownloadView(
    val id: Long,
    val title: String,
    val domain: String,
    val link: String,
    @ColumnInfo(name = "started_at")
    var startedAt: Timestamp,
    var reason: FailedReason,
    @ColumnInfo(name = "failed_at")
    var failedAt: Timestamp,
)