package io.raveerocks.downloadmanager.core.entities.views


import androidx.room.ColumnInfo
import io.raveerocks.downloadmanager.core.types.MediaType

class SizeByMediaTypeView(
    @ColumnInfo(name = "media_type")
    val mediaType: MediaType,
    val size: Float
)