package io.raveerocks.downloadmanager.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.raveerocks.downloadmanager.core.types.DownloadStatus
import io.raveerocks.downloadmanager.core.types.MediaType
import io.raveerocks.downloadmanager.registries.RepositoryRegistry
import io.raveerocks.downloadmanager.repositories.DashboardRepository

class DashboardViewModel : ViewModel() {

    companion object {
        private val dashboardRepository: DashboardRepository by lazy { RepositoryRegistry.dashboardRepository }
    }

    val countByStatus: LiveData<Map<DownloadStatus, Int>> = dashboardRepository
        .getDownloadCountByStatus()
    val sizeByMediaType: LiveData<Map<MediaType, Float>> = dashboardRepository
        .getDownloadSizeByMediaType()
    val showDownloadStats = Transformations.map(countByStatus) { it.values.sum() != 0 }
    val showMediaStats = Transformations.map(sizeByMediaType) { it.isNotEmpty() }


}