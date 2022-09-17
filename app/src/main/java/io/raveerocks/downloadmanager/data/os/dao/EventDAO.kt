package io.raveerocks.downloadmanager.data.os.dao

import android.app.PendingIntent


interface EventDAO {
    fun broadcastEvent(action: String, requestCode: Int, data: Map<String, Long>)
    fun createEvent(action: String, requestCode: Int, data: Map<String, Long>): PendingIntent

}