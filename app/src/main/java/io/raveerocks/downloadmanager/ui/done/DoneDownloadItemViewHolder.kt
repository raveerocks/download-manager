package io.raveerocks.downloadmanager.ui.done

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import io.raveerocks.downloadmanager.R
import io.raveerocks.downloadmanager.core.models.BaseModel.ActionListener
import io.raveerocks.downloadmanager.core.models.DoneDownload
import io.raveerocks.downloadmanager.core.views.custom.ClockedRecyclerView
import io.raveerocks.downloadmanager.core.views.custom.TouchableMotionLayout
import io.raveerocks.downloadmanager.databinding.LayoutDoneDownloadItemBinding


class DoneDownloadItemViewHolder private constructor(
    private val parent: ClockedRecyclerView,
    private val binding: LayoutDoneDownloadItemBinding
) : ClockedRecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ClockedRecyclerView): DoneDownloadItemViewHolder {
            return DoneDownloadItemViewHolder(
                parent, DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.layout_done_download_item,
                    parent,
                    false
                )
            ).apply { syncWith(parent) }
        }
    }

    fun bind(
        item: DoneDownload,
        openActionListener: ActionListener<DoneDownload>,
        infoActionListener: ActionListener<DoneDownload>,
        deleteActionListener: ActionListener<DoneDownload>,
    ) {
        binding.apply {
            lifecycleOwner = parent.lifecycleOwner
            doneDownload = item
            doneDownloadItemContainerLayout.apply {
                transitionToStart()
                setOnTap { onTap(this, openActionListener, item) }
            }
            infoIcon.apply {
                setOnClickListener { infoActionListener.onAction(item) }
            }
            deleteIcon.apply {
                setOnClickListener { deleteActionListener.onAction(item) }
            }
            executePendingBindings()
        }
    }

    override fun onTick() {
        binding.doneDownload?.refresh()
    }

    private fun onTap(
        motionContainer: TouchableMotionLayout,
        openActionListener: ActionListener<DoneDownload>,
        item: DoneDownload
    ) {
        if (motionContainer.currentState == R.id.start) {
            openActionListener.onAction(item)
        } else {
            motionContainer.transitionToStart()
        }
    }

}