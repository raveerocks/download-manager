package io.raveerocks.downloadmanager.data.db.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import io.raveerocks.downloadmanager.core.entities.FailedDownloadEntity
import io.raveerocks.downloadmanager.core.entities.views.FailedDownloadView
import io.raveerocks.downloadmanager.core.helpers.TimestampConverter


@Dao
@TypeConverters(TimestampConverter::class)
interface FailedDownloadDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(failedDownloadEntity: FailedDownloadEntity)

    @Query(
        "DELETE FROM failed_downloads " +
                "WHERE id=:id"
    )
    fun deleteById(id: Long)

    @Query("SELECT * FROM failed_downloads_view WHERE id=:id")
    fun getById(id: Long): FailedDownloadView

    @Query("SELECT * FROM failed_downloads_view")
    fun getFailedDownloads(): LiveData<List<FailedDownloadView>>

}