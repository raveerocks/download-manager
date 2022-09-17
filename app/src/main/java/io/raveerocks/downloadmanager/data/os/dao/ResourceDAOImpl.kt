package io.raveerocks.downloadmanager.data.os.dao

import android.content.Context

class ResourceDAOImpl(private val context: Context) : ResourceDAO {
    override fun getStringResource(id: Int): String {
        return context.getString(id)
    }
}