package io.raveerocks.downloadmanager.ui.active

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import io.raveerocks.downloadmanager.R
import io.raveerocks.downloadmanager.core.models.ActiveDownload
import io.raveerocks.downloadmanager.core.models.BaseModel.ActionListener
import io.raveerocks.downloadmanager.core.views.custom.ClockedRecyclerView
import io.raveerocks.downloadmanager.core.views.custom.TouchableMotionLayout
import io.raveerocks.downloadmanager.databinding.LayoutActiveDownloadItemBinding


class ActiveDownloadItemViewHolder private constructor(
    private val parent: ClockedRecyclerView,
    private val binding: LayoutActiveDownloadItemBinding
) : ClockedRecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ClockedRecyclerView): ActiveDownloadItemViewHolder {
            return ActiveDownloadItemViewHolder(
                parent, DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.layout_active_download_item,
                    parent,
                    false
                )
            ).apply { syncWith(parent) }
        }
    }

    fun bind(item: ActiveDownload, cancelActionListener: ActionListener<ActiveDownload>) {
        binding.apply {
            activeDownload = item
            lifecycleOwner = parent.lifecycleOwner
            activeDownloadItemContainerLayout.apply {
                transitionToStart()
                setOnTap { onTap(this) }
            }
            cancelIcon.setOnClickListener { cancelActionListener.onAction(item) }
            executePendingBindings()
        }
    }

    override fun onTick() {
        binding.activeDownload?.refresh()
    }

    private fun onTap(motionContainer: TouchableMotionLayout) {
        if (motionContainer.currentState != R.id.start) {
            motionContainer.transitionToStart()
        }
    }


}