package io.raveerocks.downloadmanager.core.helpers

import timber.log.Timber
import java.text.DecimalFormat

object MemoryUtil {

    private const val KB = 1L shl 10
    private const val MB = 1L shl 20
    private const val GB = 1L shl 30
    private const val TB = 1L shl 40

    fun Float.findSizeString(): String {
        val size: Float
        val sizeName: String
        Timber.i("Value $this and $TB")
        if (this >= TB) {
            size = this / TB
            sizeName = "TB"
        } else if (this >= GB) {
            size = this / GB
            sizeName = "GB"
        } else if (this >= MB) {
            size = this / MB
            sizeName = "MB"
        } else {
            size = this / KB
            sizeName = "KB"
        }
        return DecimalFormat("#.## $sizeName").format(size)
    }

}