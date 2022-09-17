package io.raveerocks.downloadmanager.repositories

import io.raveerocks.downloadmanager.data.os.dao.PreferenceDAO


class SettingsRepository(private val preferenceDAO: PreferenceDAO) {

    fun getDownloadOverWifiOnly(): Boolean {
        return preferenceDAO.getDownloadOverWifiOnly()
    }

    fun getDownloadWhileRoaming(): Boolean {
        return preferenceDAO.getDownloadWhileRoaming()
    }

    fun getDownloadOverWhileChargingOnly(): Boolean {
        return preferenceDAO.getDownloadOverWhileChargingOnly()
    }

    fun getDownloadOverWhileIdleOnly(): Boolean {
        return preferenceDAO.getDownloadOverWhileIdleOnly()
    }

    fun getDownloadFilePublic(): Boolean {
        return preferenceDAO.getDownloadFilePublic()
    }

    fun getHistoryLimit(): Int {
        return preferenceDAO.getHistoryLimit()
    }

    fun setDownloadOverWifiOnly(value: Boolean) {
        preferenceDAO.setDownloadOverWifiOnly(value)
    }

    fun setDownloadWhileRoaming(value: Boolean) {
        preferenceDAO.setDownloadWhileRoaming(value)
    }

    fun setDownloadOverWhileChargingOnly(value: Boolean) {
        preferenceDAO.setDownloadOverWhileChargingOnly(value)
    }

    fun setDownloadOverWhileIdleOnly(value: Boolean) {
        preferenceDAO.setDownloadOverWhileIdleOnly(value)
    }

    fun setDownloadFilePublic(value: Boolean) {
        preferenceDAO.setDownloadFilePublic(value)
    }

    fun setHistoryLimit(limit: Int) {
        preferenceDAO.setHistoryLimit(limit)
    }

    fun resetPreferences() {
        preferenceDAO.resetPreferences()
    }
}