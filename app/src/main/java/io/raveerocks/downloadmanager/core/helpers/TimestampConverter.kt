package io.raveerocks.downloadmanager.core.helpers

import androidx.room.TypeConverter
import java.sql.Timestamp

object TimestampConverter {
    @TypeConverter
    fun toTimestamp(timestampLong: Long?): Timestamp? {
        return if (timestampLong == null) null else Timestamp(timestampLong)
    }

    @TypeConverter
    fun fromTimestamp(timestamp: Timestamp?): Long? {
        return timestamp?.time
    }
}