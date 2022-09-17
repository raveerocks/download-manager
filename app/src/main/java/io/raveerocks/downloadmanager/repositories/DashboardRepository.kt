package io.raveerocks.downloadmanager.repositories


import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.raveerocks.downloadmanager.core.entities.views.CountByStatusView
import io.raveerocks.downloadmanager.core.entities.views.SizeByMediaTypeView
import io.raveerocks.downloadmanager.core.types.DownloadStatus
import io.raveerocks.downloadmanager.core.types.MediaType
import io.raveerocks.downloadmanager.data.db.dao.DoneDownloadDAO
import io.raveerocks.downloadmanager.data.db.dao.DownloadDAO
import io.raveerocks.downloadmanager.data.os.dao.PreferenceDAO
import java.sql.Timestamp
import java.util.*

class DashboardRepository(
    private val doneDownloadDAO: DoneDownloadDAO,
    private val downloadDAO: DownloadDAO,
    private val preferenceDAO: PreferenceDAO,
) {

    fun getDownloadCountByStatus(): LiveData<Map<DownloadStatus, Int>> {
        return Transformations.map(downloadDAO.getDownloadsCountByStatus(Timestamp(getDay().time))) {
            it.asCountMap()
        }
    }

    fun getDownloadSizeByMediaType(): LiveData<Map<MediaType, Float>> {
        return Transformations.map(doneDownloadDAO.getSizeByMediaType(Timestamp(getDay().time))) {
            it.asSizeMap()
        }
    }

    private fun getDay(): Date {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(
            Calendar.DATE, 0 - preferenceDAO.getHistoryLimit()
        )
        return cal.time
    }

    private fun List<CountByStatusView>.asCountMap(): Map<DownloadStatus, Int> {
        val map = HashMap<DownloadStatus, Int>()
        this.forEach { map[it.downloadStatus] = it.count }
        return map
    }

    private fun List<SizeByMediaTypeView>.asSizeMap(): Map<MediaType, Float> {
        val map = HashMap<MediaType, Float>()
        this.forEach { map[it.mediaType] = it.size }
        return map
    }

}