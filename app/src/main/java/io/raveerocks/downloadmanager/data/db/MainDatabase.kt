package io.raveerocks.downloadmanager.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.raveerocks.downloadmanager.core.entities.ActiveDownloadEntity
import io.raveerocks.downloadmanager.core.entities.DoneDownloadEntity
import io.raveerocks.downloadmanager.core.entities.DownloadEntity
import io.raveerocks.downloadmanager.core.entities.FailedDownloadEntity
import io.raveerocks.downloadmanager.core.entities.views.ActiveDownloadView
import io.raveerocks.downloadmanager.core.entities.views.DoneDownloadView
import io.raveerocks.downloadmanager.core.entities.views.FailedDownloadView
import io.raveerocks.downloadmanager.data.db.dao.ActiveDownloadDAO
import io.raveerocks.downloadmanager.data.db.dao.DoneDownloadDAO
import io.raveerocks.downloadmanager.data.db.dao.DownloadDAO
import io.raveerocks.downloadmanager.data.db.dao.FailedDownloadDAO


@Database(
    entities = [DownloadEntity::class,
        ActiveDownloadEntity::class,
        DoneDownloadEntity::class,
        FailedDownloadEntity::class],
    views = [ActiveDownloadView::class,
        DoneDownloadView::class,
        FailedDownloadView::class], version = 1, exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {
    abstract val downloadDAO: DownloadDAO
    abstract val activeDownloadDAO: ActiveDownloadDAO
    abstract val doneDownloadDAO: DoneDownloadDAO
    abstract val failedDownloadDAO: FailedDownloadDAO
}