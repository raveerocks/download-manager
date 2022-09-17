package io.raveerocks.downloadmanager.core.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import io.raveerocks.downloadmanager.core.helpers.TimestampConverter


@Entity(
    tableName = "active_downloads",
    foreignKeys = [ForeignKey(
        entity = DownloadEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("id"),
        onDelete = ForeignKey.CASCADE
    )]
)
@TypeConverters(TimestampConverter::class)
class ActiveDownloadEntity(
    @PrimaryKey
    val id: Long
)