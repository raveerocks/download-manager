package io.raveerocks.downloadmanager.data.os.dao

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

class EventDAOImpl(private val context: Context) : EventDAO {

    override fun broadcastEvent(action: String, requestCode: Int, data: Map<String, Long>) {
        createEvent(action, requestCode, data).send()
    }

    override fun createEvent(
        action: String,
        requestCode: Int,
        data: Map<String, Long>
    ): PendingIntent {
        return PendingIntent.getBroadcast(
            context, requestCode,
            createIntentWithAction(action, data),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )
    }

    private fun createIntentWithAction(action: String, data: Map<String, Long>): Intent {
        return Intent(action)
            .apply {
                data.forEach { (keyExtra, valueExtra) ->
                    putExtra(
                        keyExtra,
                        valueExtra
                    )
                }
            }
    }
}