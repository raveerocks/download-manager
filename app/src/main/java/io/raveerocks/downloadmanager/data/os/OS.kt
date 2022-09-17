package io.raveerocks.downloadmanager.data.os

import android.content.Context
import io.raveerocks.downloadmanager.data.os.dao.*

class OS private constructor(val context: Context) {
    companion object {
        fun from(context: Context): OS {
            return OS(context)
        }
    }

    val clipboardDAO: ClipboardDAO by lazy { ClipboardDAOImpl(context) }
    val downloadsDAO: DownloadsDAO by lazy { DownloadsDAOImpl(context) }
    val eventDAO: EventDAO by lazy { EventDAOImpl(context) }
    val filesDAO: FilesDAO by lazy { FilesDAOImpl(context) }
    val notificationDAO: NotificationDAO by lazy { NotificationDAOImpl(context) }
    val preferenceDAO: PreferenceDAO by lazy { PreferenceDAOImpl(context) }
    val resourceDAO: ResourceDAO by lazy { ResourceDAOImpl(context) }
}