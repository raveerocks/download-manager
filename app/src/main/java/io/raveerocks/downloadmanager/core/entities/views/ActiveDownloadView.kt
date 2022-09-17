package io.raveerocks.downloadmanager.core.entities.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.TypeConverters
import io.raveerocks.downloadmanager.core.helpers.TimestampConverter
import java.sql.Timestamp


@DatabaseView(
    "SELECT active_downloads.id,title,domain,link,started_at " +
            "FROM active_downloads INNER JOIN downloads " +
            "ON active_downloads.id = downloads.id",
    viewName = "active_downloads_view"
)
@TypeConverters(TimestampConverter::class)
class ActiveDownloadView(
    val id: Long,
    val title: String,
    val domain: String,
    val link: String,
    @ColumnInfo(name = "started_at")
    var startedAt: Timestamp,
)