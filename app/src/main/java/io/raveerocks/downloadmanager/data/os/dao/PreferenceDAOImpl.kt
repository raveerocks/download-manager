package io.raveerocks.downloadmanager.data.os.dao

import android.content.Context

class PreferenceDAOImpl(private val context: Context) : PreferenceDAO {

    companion object {
        private const val PREFERENCE_DASHBOARD_HISTORY_LIMIT =
            "io.raveerocks.downloadmanager.PREFERENCE_DASHBOARD_HISTORY_LIMIT"
        private const val PREFERENCE_DASHBOARD_HISTORY_LIMIT_DEFAULT = 7
        private const val PREFERENCE_DOWNLOAD_OVER_WIFI_ONLY =
            "io.raveerocks.downloadmanager.PREFERENCE_DOWNLOAD_OVER_WIFI_ONLY"
        private const val PREFERENCE_DOWNLOAD_OVER_WIFI_ONLY_DEFAULT = true
        private const val PREFERENCE_DOWNLOAD_WHILE_ROAMING =
            "io.raveerocks.downloadmanager.PREFERENCE_DOWNLOAD_WHILE_ROAMING"
        private const val PREFERENCE_DOWNLOAD_WHILE_ROAMING_DEFAULT = true
        private const val PREFERENCE_DOWNLOAD_WHILE_CHARGING_ONLY =
            "io.raveerocks.downloadmanager.PREFERENCE_DOWNLOAD_WHILE_CHARGING_ONLY"
        private const val PREFERENCE_DOWNLOAD_WHILE_CHARGING_ONLY_DEFAULT = false
        private const val PREFERENCE_DOWNLOAD_WHILE_IDLE_ONLY =
            "io.raveerocks.downloadmanager.PREFERENCE_DOWNLOAD_WHILE_IDLE_ONLY"
        private const val PREFERENCE_DOWNLOAD_WHILE_IDLE_ONLY_DEFAULT = false
        private const val PREFERENCE_DOWNLOAD_MAKE_PUBLIC =
            "io.raveerocks.downloadmanager.PREFERENCE_DOWNLOAD_MAKE_PUBLIC"
        private const val PREFERENCE_DOWNLOAD_MAKE_PUBLIC_DEFAULT = false
    }

    private val sharedPreferences by lazy {
        context.getSharedPreferences(
            context.packageName,
            Context.MODE_PRIVATE
        )
    }

    override fun setHistoryLimit(limit: Int) {
        addSharedInt(PREFERENCE_DASHBOARD_HISTORY_LIMIT, limit)
    }

    override fun getHistoryLimit(): Int {
        return getSharedInt(
            PREFERENCE_DASHBOARD_HISTORY_LIMIT,
            PREFERENCE_DASHBOARD_HISTORY_LIMIT_DEFAULT
        )
    }

    override fun setDownloadOverWifiOnly(value: Boolean) {
        addSharedBoolean(PREFERENCE_DOWNLOAD_OVER_WIFI_ONLY, value)
    }

    override fun getDownloadOverWifiOnly(): Boolean {
        return getSharedBoolean(
            PREFERENCE_DOWNLOAD_OVER_WIFI_ONLY,
            PREFERENCE_DOWNLOAD_OVER_WIFI_ONLY_DEFAULT
        )
    }

    override fun setDownloadWhileRoaming(value: Boolean) {
        addSharedBoolean(PREFERENCE_DOWNLOAD_WHILE_ROAMING, value)
    }

    override fun getDownloadWhileRoaming(): Boolean {
        return getSharedBoolean(
            PREFERENCE_DOWNLOAD_WHILE_ROAMING,
            PREFERENCE_DOWNLOAD_WHILE_ROAMING_DEFAULT
        )
    }

    override fun setDownloadOverWhileChargingOnly(value: Boolean) {
        addSharedBoolean(PREFERENCE_DOWNLOAD_WHILE_CHARGING_ONLY, value)
    }

    override fun getDownloadOverWhileChargingOnly(): Boolean {
        return getSharedBoolean(
            PREFERENCE_DOWNLOAD_WHILE_CHARGING_ONLY,
            PREFERENCE_DOWNLOAD_WHILE_CHARGING_ONLY_DEFAULT
        )
    }

    override fun setDownloadOverWhileIdleOnly(value: Boolean) {
        addSharedBoolean(PREFERENCE_DOWNLOAD_WHILE_IDLE_ONLY, value)
    }

    override fun getDownloadOverWhileIdleOnly(): Boolean {
        return getSharedBoolean(
            PREFERENCE_DOWNLOAD_WHILE_IDLE_ONLY,
            PREFERENCE_DOWNLOAD_WHILE_IDLE_ONLY_DEFAULT
        )
    }

    override fun setDownloadFilePublic(value: Boolean) {
        addSharedBoolean(PREFERENCE_DOWNLOAD_MAKE_PUBLIC, value)
    }

    override fun getDownloadFilePublic(): Boolean {
        return getSharedBoolean(
            PREFERENCE_DOWNLOAD_MAKE_PUBLIC,
            PREFERENCE_DOWNLOAD_MAKE_PUBLIC_DEFAULT
        )
    }

    override fun resetPreferences() {
        removeSharedValue(PREFERENCE_DASHBOARD_HISTORY_LIMIT)
        removeSharedValue(PREFERENCE_DOWNLOAD_OVER_WIFI_ONLY)
        removeSharedValue(PREFERENCE_DOWNLOAD_WHILE_ROAMING)
        removeSharedValue(PREFERENCE_DOWNLOAD_WHILE_CHARGING_ONLY)
        removeSharedValue(PREFERENCE_DOWNLOAD_WHILE_IDLE_ONLY)
        removeSharedValue(PREFERENCE_DOWNLOAD_MAKE_PUBLIC)
    }

    private fun addSharedBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(key, value)
        }.apply()
    }

    private fun addSharedInt(key: String, value: Int) {
        sharedPreferences.edit().apply {
            putInt(key, value)
        }.apply()
    }

    private fun getSharedBoolean(key: String, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    private fun getSharedInt(key: String, default: Int): Int {
        return sharedPreferences.getInt(key, default)
    }

    private fun removeSharedValue(key: String) {
        sharedPreferences.edit().apply {
            remove(key)
        }.apply()
    }

}