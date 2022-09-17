package io.raveerocks.downloadmanager.data.os.dao

interface PreferenceDAO {
    fun setHistoryLimit(limit: Int)
    fun getHistoryLimit(): Int
    fun setDownloadOverWifiOnly(value: Boolean)
    fun getDownloadOverWifiOnly(): Boolean
    fun setDownloadWhileRoaming(value: Boolean)
    fun getDownloadWhileRoaming(): Boolean
    fun setDownloadOverWhileChargingOnly(value: Boolean)
    fun getDownloadOverWhileChargingOnly(): Boolean
    fun setDownloadOverWhileIdleOnly(value: Boolean)
    fun getDownloadOverWhileIdleOnly(): Boolean
    fun setDownloadFilePublic(value: Boolean)
    fun getDownloadFilePublic(): Boolean
    fun resetPreferences()
}