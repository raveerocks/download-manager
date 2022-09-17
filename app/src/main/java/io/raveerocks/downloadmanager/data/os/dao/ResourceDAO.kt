package io.raveerocks.downloadmanager.data.os.dao

interface ResourceDAO {
    fun getStringResource(id: Int): String
}