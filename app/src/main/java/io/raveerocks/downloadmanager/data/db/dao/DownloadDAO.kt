package io.raveerocks.downloadmanager.data.db.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import io.raveerocks.downloadmanager.core.entities.DownloadEntity
import io.raveerocks.downloadmanager.core.entities.views.CountByStatusView
import io.raveerocks.downloadmanager.core.helpers.TimestampConverter
import io.raveerocks.downloadmanager.core.types.DownloadStatus
import java.sql.Timestamp


@Dao
@TypeConverters(TimestampConverter::class)
interface DownloadDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(downloadEntity: DownloadEntity)

    @Query(
        "SELECT * FROM downloads " +
                "WHERE id=:id"
    )
    fun getById(id: Long): DownloadEntity

    @Query(
        "DELETE FROM downloads " +
                "WHERE id=:id"
    )
    fun deleteById(id: Long)

    @Query(
        "UPDATE downloads SET download_status=:downloadStatus " +
                "WHERE id=:id"
    )
    fun updateStatus(id: Long, downloadStatus: DownloadStatus)

    @Query(
        "SELECT download_status,count(id) AS count " +
                "FROM downloads " +
                "WHERE started_at>=:from " +
                "GROUP BY download_status"
    )
    fun getDownloadsCountByStatus(from: Timestamp): LiveData<List<CountByStatusView>>

    @Query(
        "SELECT id FROM downloads " +
                "WHERE download_status=:downloadStatus"
    )
    fun getDownloadsByStatus(downloadStatus: DownloadStatus): List<Long>
}