package io.raveerocks.downloadmanager.data.db.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import io.raveerocks.downloadmanager.core.entities.ActiveDownloadEntity
import io.raveerocks.downloadmanager.core.entities.views.ActiveDownloadView
import io.raveerocks.downloadmanager.core.helpers.TimestampConverter


@Dao
@TypeConverters(TimestampConverter::class)
interface ActiveDownloadDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(activeDownloadEntity: ActiveDownloadEntity)

    @Query(
        "DELETE FROM active_downloads " +
                "WHERE id=:id"
    )
    fun deleteById(id: Long)


    @Query("SELECT * FROM active_downloads_view")
    fun getActiveDownloads(): LiveData<List<ActiveDownloadView>>

}