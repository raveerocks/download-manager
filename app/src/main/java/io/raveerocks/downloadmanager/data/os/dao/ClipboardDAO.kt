package io.raveerocks.downloadmanager.data.os.dao

interface ClipboardDAO {
    fun getCurrentText(): String
}