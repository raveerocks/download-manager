package io.raveerocks.downloadmanager.data.os.dao

import android.net.Uri
import io.raveerocks.downloadmanager.core.entities.Download

interface DownloadsDAO {
    fun createDownload(
        uri: Uri,
        fileName: String,
        isPublic: Boolean,
        wifiOnly: Boolean,
        whileRoaming: Boolean,
        whileChargingOnly: Boolean,
        whileIdleOnly: Boolean
    ): Long

    fun getDownload(id: Long): Download?
    fun cancelDownload(id: Long)
}