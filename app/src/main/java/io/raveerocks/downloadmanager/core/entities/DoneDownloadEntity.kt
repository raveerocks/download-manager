package io.raveerocks.downloadmanager.core.entities

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import io.raveerocks.downloadmanager.core.helpers.TimestampConverter
import io.raveerocks.downloadmanager.core.types.MediaType
import java.sql.Timestamp

@Entity(
    tableName = "done_downloads",
    foreignKeys = [ForeignKey(
        entity = DownloadEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("id"),
        onDelete = CASCADE
    )]
)
@TypeConverters(TimestampConverter::class)
class DoneDownloadEntity(
    @PrimaryKey
    val id: Long,
    val location: String,
    @ColumnInfo(name = "media_type")
    val mediaType: MediaType,
    @ColumnInfo(name = "extended_media_type")
    val extendedMediaType: String,
    @ColumnInfo(name = "total_size_bytes")
    val totalSizeBytes: Float,
    @ColumnInfo(name = "completed_at")
    val completedAt: Timestamp,
)