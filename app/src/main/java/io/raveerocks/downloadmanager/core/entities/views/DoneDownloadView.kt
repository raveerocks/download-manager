package io.raveerocks.downloadmanager.core.entities.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.TypeConverters
import io.raveerocks.downloadmanager.core.helpers.TimestampConverter
import io.raveerocks.downloadmanager.core.types.MediaType
import java.sql.Timestamp

@DatabaseView(
    "SELECT done_downloads.id,title,domain,link,started_at,location,media_type,extended_media_type,total_size_bytes,completed_at " +
            "FROM done_downloads INNER JOIN downloads " +
            "ON done_downloads.id = downloads.id",
    viewName = "done_downloads_view"
)
@TypeConverters(TimestampConverter::class)
class DoneDownloadView(
    val id: Long,
    val title: String,
    val domain: String,
    val link: String,
    @ColumnInfo(name = "started_at")
    var startedAt: Timestamp,
    var location: String,
    @ColumnInfo(name = "media_type")
    var mediaType: MediaType,
    @ColumnInfo(name = "extended_media_type")
    var extendedMediaType: String,
    @ColumnInfo(name = "total_size_bytes")
    var totalSizeBytes: Float,
    @ColumnInfo(name = "completed_at")
    var completedAt: Timestamp,
)