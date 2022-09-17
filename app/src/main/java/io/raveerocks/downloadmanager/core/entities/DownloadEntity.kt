package io.raveerocks.downloadmanager.core.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import io.raveerocks.downloadmanager.core.helpers.TimestampConverter
import io.raveerocks.downloadmanager.core.types.DownloadStatus
import java.sql.Timestamp

@Entity(tableName = "downloads")
@TypeConverters(TimestampConverter::class)
data class DownloadEntity(
    @PrimaryKey
    val id: Long,
    @NonNull
    val title: String,
    val domain: String,
    val link: String,
    @ColumnInfo(name = "started_at")
    val startedAt: Timestamp,
    @ColumnInfo(name = "download_status")
    var downloadStatus: DownloadStatus
)