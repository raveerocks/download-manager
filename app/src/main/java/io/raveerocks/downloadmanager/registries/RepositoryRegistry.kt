package io.raveerocks.downloadmanager.registries

import android.content.Context
import androidx.room.Room
import io.raveerocks.downloadmanager.data.db.MainDatabase
import io.raveerocks.downloadmanager.data.os.OS
import io.raveerocks.downloadmanager.repositories.*

class RepositoryRegistry {

    companion object {
        val downloadRepository: DownloadRepository
            get() = _downloadRepository
        val dashboardRepository: DashboardRepository
            get() = _dashboardRepository
        val activeDownloadRepository: ActiveDownloadRepository
            get() = _activeDownloadRepository
        val doneDownloadRepository: DoneDownloadRepository
            get() = _doneDownloadRepository
        val failedDownloadRepository: FailedDownloadRepository
            get() = _failedDownloadRepository
        val settingsRepository: SettingsRepository
            get() = _settingsRepository

        private lateinit var _downloadRepository: DownloadRepository
        private lateinit var _dashboardRepository: DashboardRepository
        private lateinit var _activeDownloadRepository: ActiveDownloadRepository
        private lateinit var _doneDownloadRepository: DoneDownloadRepository
        private lateinit var _failedDownloadRepository: FailedDownloadRepository
        private lateinit var _settingsRepository: SettingsRepository

        fun init(context: Context) {
            val os = OS.from(context)
            val mainDatabase = Room.databaseBuilder(
                context,
                MainDatabase::class.java,
                "MAIN_DATABASE"
            )
                .build()

            _downloadRepository = DownloadRepository(
                os.clipboardDAO,
                mainDatabase.downloadDAO,
                os.downloadsDAO,
                os.eventDAO,
                os.preferenceDAO
            )

            _dashboardRepository = DashboardRepository(
                mainDatabase.doneDownloadDAO,
                mainDatabase.downloadDAO,
                os.preferenceDAO
            )

            _activeDownloadRepository = ActiveDownloadRepository(
                mainDatabase.activeDownloadDAO,
                os.downloadsDAO,
                os.eventDAO
            )

            _doneDownloadRepository = DoneDownloadRepository(
                mainDatabase.doneDownloadDAO,
                os.downloadsDAO,
                os.eventDAO,
                os.filesDAO,
                os.notificationDAO,
                os.resourceDAO
            )

            _failedDownloadRepository = FailedDownloadRepository(
                os.downloadsDAO,
                os.eventDAO,
                mainDatabase.failedDownloadDAO,
                os.notificationDAO,
                os.resourceDAO
            )
            _settingsRepository = SettingsRepository(os.preferenceDAO)

        }
    }


}