package io.raveerocks.downloadmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val eventShowFAB: LiveData<Boolean>
        get() = _eventShowFAB
    val eventHideFAB: LiveData<Boolean>
        get() = _eventHideFAB

    private val _eventShowFAB = MutableLiveData<Boolean>()
    private val _eventHideFAB = MutableLiveData<Boolean>()


    fun showFAB() {
        _eventShowFAB.value = true
    }

    fun hideFAB() {
        _eventHideFAB.value = true
    }

    fun showFABDone() {
        _eventShowFAB.value = false
    }

    fun hideFABDone() {
        _eventHideFAB.value = false
    }
}