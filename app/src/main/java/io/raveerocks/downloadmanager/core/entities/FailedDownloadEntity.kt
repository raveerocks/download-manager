package io.raveerocks.downloadmanager.core.entities

import androidx.room.*
import io.raveerocks.downloadmanager.core.helpers.TimestampConverter
import io.raveerocks.downloadmanager.core.types.FailedReason
import java.sql.Timestamp


@Entity(
    tableName = "failed_downloads",
    foreignKeys = [ForeignKey(
        entity = DownloadEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("id"),
        onDelete = ForeignKey.CASCADE
    )]
)
@TypeConverters(TimestampConverter::class)
class FailedDownloadEntity(
    @PrimaryKey
    val id: Long,
    var reason: FailedReason,
    @ColumnInfo(name = "failed_at")
    var failedAt: Timestamp,
)