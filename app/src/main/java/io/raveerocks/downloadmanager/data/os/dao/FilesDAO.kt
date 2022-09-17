package io.raveerocks.downloadmanager.data.os.dao

interface FilesDAO {
    fun openFile(location: String): Boolean
    fun deleteFile(location: String): Boolean
}