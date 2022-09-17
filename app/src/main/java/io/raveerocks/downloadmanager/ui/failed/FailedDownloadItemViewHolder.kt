package io.raveerocks.downloadmanager.ui.failed

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import io.raveerocks.downloadmanager.R
import io.raveerocks.downloadmanager.core.models.BaseModel.ActionListener
import io.raveerocks.downloadmanager.core.models.FailedDownload
import io.raveerocks.downloadmanager.core.views.custom.ClockedRecyclerView
import io.raveerocks.downloadmanager.core.views.custom.TouchableMotionLayout
import io.raveerocks.downloadmanager.databinding.LayoutFailedDownloadItemBinding


class FailedDownloadItemViewHolder private constructor(
    private val parent: ClockedRecyclerView,
    private val binding: LayoutFailedDownloadItemBinding
) : ClockedRecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ClockedRecyclerView): FailedDownloadItemViewHolder {
            return FailedDownloadItemViewHolder(
                parent, DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.layout_failed_download_item,
                    parent,
                    false
                )
            ).apply { syncWith(parent) }
        }
    }

    fun bind(
        item: FailedDownload,
        retryActionListener: ActionListener<FailedDownload>,
        deleteActionListener: ActionListener<FailedDownload>,
    ) {
        binding.apply {
            lifecycleOwner = parent.lifecycleOwner
            failedDownload = item
            failedDownloadItemContainerLayout.apply {
                transitionToStart()
                setOnTap { onTap(this) }
            }
            retryIcon.apply {
                setOnClickListener { retryActionListener.onAction(item) }
            }
            deleteIcon.apply {
                setOnClickListener { deleteActionListener.onAction(item) }
            }
            executePendingBindings()
        }
    }

    override fun onTick() {
        binding.failedDownload?.refresh()
    }

    private fun onTap(motionContainer: TouchableMotionLayout) {
        if (motionContainer.currentState != R.id.start) {
            motionContainer.transitionToStart()
        }
    }

}