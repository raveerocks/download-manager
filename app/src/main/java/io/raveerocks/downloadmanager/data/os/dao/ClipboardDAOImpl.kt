package io.raveerocks.downloadmanager.data.os.dao

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class ClipboardDAOImpl(private val context: Context) : ClipboardDAO {

    private val clipboardManager: android.content.ClipboardManager by lazy {
        context.getSystemService(
            AppCompatActivity.CLIPBOARD_SERVICE
        ) as android.content.ClipboardManager
    }


    override fun getCurrentText(): String {
        if (clipboardManager.hasPrimaryClip()) {
            return clipboardManager.primaryClip?.getItemAt(0)?.text.toString()
        }
        return ""
    }
}