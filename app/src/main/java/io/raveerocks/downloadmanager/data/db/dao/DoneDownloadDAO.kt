package io.raveerocks.downloadmanager.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.raveerocks.downloadmanager.core.entities.DoneDownloadEntity
import io.raveerocks.downloadmanager.core.entities.views.DoneDownloadView
import io.raveerocks.downloadmanager.core.entities.views.SizeByMediaTypeView
import io.raveerocks.downloadmanager.core.helpers.TimestampConverter
import io.raveerocks.downloadmanager.core.types.MediaType
import java.sql.Timestamp


@Dao
@TypeConverters(TimestampConverter::class)
interface DoneDownloadDAO {
    companion object {
        const val COLUMN_ID = 0
        const val COLUMN_TITLE = 1
        const val COLUMN_DOMAIN = 2
        const val COLUMN_LINK = 3
        const val COLUMN_STARTED = 4
        const val COLUMN_LOCATION = 5
        const val COLUMN_MEDIA_TYPE = 6
        const val COLUMN_EXTENDED_MEDIA_TYPE = 7
        const val COLUMN_SIZE = 8
        const val COLUMN_COMPLETED_AT = 9

        const val SORT_ASC = 0
        const val SORT_DESC = 1
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(doneDownloadEntity: DoneDownloadEntity)

    @Query(
        "DELETE FROM done_downloads " +
                "WHERE id=:id"
    )
    fun deleteById(id: Long)


    @Query("SELECT * FROM done_downloads_view WHERE id=:id")
    fun getById(id: Long): DoneDownloadView

    @Query(
        "SELECT * FROM done_downloads_view WHERE media_type IN (:mediaType)" +
                "ORDER BY " +
                "CASE WHEN :orderBy=$COLUMN_ID AND :order=$SORT_ASC THEN id END ASC, " +
                "CASE WHEN :orderBy=$COLUMN_ID AND :order=$SORT_DESC THEN id END DESC," +

                "CASE WHEN :orderBy=$COLUMN_TITLE AND :order=$SORT_ASC THEN title END ASC, " +
                "CASE WHEN :orderBy=$COLUMN_TITLE AND :order=$SORT_DESC THEN title END DESC," +

                "CASE WHEN :orderBy=$COLUMN_DOMAIN AND :order=$SORT_ASC THEN domain END ASC, " +
                "CASE WHEN :orderBy=$COLUMN_DOMAIN AND :order=$SORT_DESC THEN domain END DESC," +

                "CASE WHEN :orderBy=$COLUMN_LINK AND :order=$SORT_ASC THEN link END ASC, " +
                "CASE WHEN :orderBy=$COLUMN_LINK AND :order=$SORT_DESC THEN link END DESC," +

                "CASE WHEN :orderBy=$COLUMN_STARTED AND :order=$SORT_ASC THEN started_at END ASC, " +
                "CASE WHEN :orderBy=$COLUMN_STARTED AND :order=$SORT_DESC THEN started_at END DESC," +


                "CASE WHEN :orderBy=$COLUMN_LOCATION AND :order=$SORT_ASC THEN location END ASC, " +
                "CASE WHEN :orderBy=$COLUMN_LOCATION AND :order=$SORT_DESC THEN location END DESC," +

                "CASE WHEN :orderBy=$COLUMN_MEDIA_TYPE AND :order=$SORT_ASC THEN media_type END ASC, " +
                "CASE WHEN :orderBy=$COLUMN_MEDIA_TYPE AND :order=$SORT_DESC THEN media_type END DESC," +

                "CASE WHEN :orderBy=$COLUMN_EXTENDED_MEDIA_TYPE AND :order=$SORT_ASC THEN extended_media_type END ASC, " +
                "CASE WHEN :orderBy=$COLUMN_EXTENDED_MEDIA_TYPE AND :order=$SORT_DESC THEN extended_media_type END DESC," +

                "CASE WHEN :orderBy=$COLUMN_SIZE AND :order=$SORT_ASC THEN total_size_bytes END ASC, " +
                "CASE WHEN :orderBy=$COLUMN_SIZE AND :order=$SORT_DESC THEN total_size_bytes END DESC," +

                "CASE WHEN :orderBy=$COLUMN_COMPLETED_AT AND :order=$SORT_ASC THEN completed_at END ASC, " +
                "CASE WHEN :orderBy=$COLUMN_COMPLETED_AT AND :order=$SORT_DESC THEN completed_at END DESC"
    )
    fun getDoneDownloads(
        orderBy: Int,
        order: Int,
        mediaType: Set<MediaType>
    ): LiveData<List<DoneDownloadView>>


    @Query(
        "SELECT media_type,sum(total_size_bytes) AS size " +
                "FROM done_downloads_view WHERE started_at>=:time " +
                "GROUP BY media_type"
    )
    fun getSizeByMediaType(time: Timestamp): LiveData<List<SizeByMediaTypeView>>

}