package io.raveerocks.downloadmanager.core.entities

class Download(
    val id: Long,
    var status: Int,
    var location: String?,
    var mediaType: String?,
    var totalSizeBytes: String?,
    var totalBytesDownloadedSoFar: String?,
    var reason: Int?
)