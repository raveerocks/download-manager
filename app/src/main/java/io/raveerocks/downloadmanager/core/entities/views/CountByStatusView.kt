package io.raveerocks.downloadmanager.core.entities.views

import androidx.room.ColumnInfo
import io.raveerocks.downloadmanager.core.types.DownloadStatus


class CountByStatusView(
    @ColumnInfo(name = "download_status")
    val downloadStatus: DownloadStatus,
    val count: Int
)