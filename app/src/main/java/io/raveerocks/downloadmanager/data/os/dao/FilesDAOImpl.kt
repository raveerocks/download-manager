package io.raveerocks.downloadmanager.data.os.dao

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class FilesDAOImpl(private val context: Context) : FilesDAO {

    private val fileProviderAuthority = context.packageName + ".fileprovider"

    override fun openFile(location: String): Boolean {
        return try {
            context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = FileProvider.getUriForFile(
                    context,
                    fileProviderAuthority,
                    getFile(location)
                )
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun deleteFile(location: String): Boolean {
        val file = getFile(location)
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }

    private fun getFile(location: String): File {
        return File(Uri.parse(location).path!!)
    }
}