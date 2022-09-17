package io.raveerocks.downloadmanager.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.raveerocks.downloadmanager.registries.RepositoryRegistry
import io.raveerocks.downloadmanager.repositories.SettingsRepository

class SettingsViewModel : ViewModel() {

    companion object {
        private val settingsRepository: SettingsRepository by lazy { RepositoryRegistry.settingsRepository }
    }

    val wifiOnly: LiveData<Boolean>
        get() = _isWifiOnly
    val isAllowedWhileRoaming: LiveData<Boolean>
        get() = _isAllowedWhileRoaming
    val isAllowedWhileChargingOnly: LiveData<Boolean>
        get() = _isAllowedWhileChargingOnly
    val isAllowedWhileIdleOnly: LiveData<Boolean>
        get() = _isAllowedWhileIdleOnly
    val isDownloadPublic: LiveData<Boolean>
        get() = _isDownloadPublic
    val dashboardHistoryLimit: LiveData<List<Float>>
        get() = _dashboardHistoryLimit

    private val _isWifiOnly = MutableLiveData<Boolean>()
    private val _isAllowedWhileRoaming = MutableLiveData<Boolean>()
    private val _isAllowedWhileChargingOnly = MutableLiveData<Boolean>()
    private val _isAllowedWhileIdleOnly = MutableLiveData<Boolean>()
    private val _isDownloadPublic = MutableLiveData<Boolean>()
    private val _dashboardHistoryLimit = MutableLiveData<List<Float>>()


    init {
        loadPreferences()
    }

    fun setIsWifiOnly(isWifiOnly: Boolean) {
        _isWifiOnly.value = isWifiOnly
        if (!isWifiOnly) {
            _isAllowedWhileRoaming.value = settingsRepository.getDownloadWhileRoaming()
        }
    }

    fun setIsAllowedWhileRoaming(isAllowedWhileRoaming: Boolean) {
        _isAllowedWhileRoaming.value = isAllowedWhileRoaming
    }

    fun setIsAllowedWhileChargingOnly(isAllowedWhileChargingOnly: Boolean) {
        _isAllowedWhileChargingOnly.value = isAllowedWhileChargingOnly
    }

    fun setIsAllowedWhileIdleOnly(isAllowedWhileIdleOnly: Boolean) {
        _isAllowedWhileIdleOnly.value = isAllowedWhileIdleOnly
    }

    fun setIsDownloadPublic(isDownloadPublic: Boolean) {
        _isDownloadPublic.value = isDownloadPublic
    }

    fun setDashboardHistoryLimit(limit: Int) {
        _dashboardHistoryLimit.value = listOf(limit.toFloat())
    }

    fun savePreferences() {
        settingsRepository.apply {
            _isWifiOnly.value?.let { setDownloadOverWifiOnly(it) }
            _isAllowedWhileRoaming.value?.let {
                setDownloadWhileRoaming(it)
            }
            _isAllowedWhileChargingOnly.value?.let {
                setDownloadOverWhileChargingOnly(it)
            }
            _isAllowedWhileIdleOnly.value?.let {
                setDownloadOverWhileIdleOnly(it)
            }
            _isDownloadPublic.value?.let { setDownloadFilePublic(it) }
            _dashboardHistoryLimit.value?.let { setHistoryLimit(it[0].toInt()) }
        }
    }

    fun resetPreferences() {
        settingsRepository.apply {
            resetPreferences()
        }
    }


    fun loadPreferences() {
        _isWifiOnly.value = settingsRepository.getDownloadOverWifiOnly()
        _isAllowedWhileRoaming.value = settingsRepository.getDownloadWhileRoaming()
        _isAllowedWhileChargingOnly.value = settingsRepository.getDownloadOverWhileChargingOnly()
        _isAllowedWhileIdleOnly.value = settingsRepository.getDownloadOverWhileIdleOnly()
        _isDownloadPublic.value = settingsRepository.getDownloadFilePublic()
        _dashboardHistoryLimit.value = listOf(
            settingsRepository.getHistoryLimit().toFloat()
        )
    }


}