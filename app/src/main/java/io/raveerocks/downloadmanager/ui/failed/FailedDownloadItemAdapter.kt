package io.raveerocks.downloadmanager.ui.failed

import io.raveerocks.downloadmanager.core.adapters.RefreshableModelListAdapter
import io.raveerocks.downloadmanager.core.models.BaseModel.ActionListener
import io.raveerocks.downloadmanager.core.models.FailedDownload
import io.raveerocks.downloadmanager.core.views.custom.ClockedRecyclerView

class FailedDownloadItemAdapter(
    private val retryActionListener: ActionListener<FailedDownload>,
    private val deleteActionListener: ActionListener<FailedDownload>,
) : RefreshableModelListAdapter<FailedDownload, FailedDownloadItemViewHolder>() {

    override fun onCreateRefreshableViewHolder(parent: ClockedRecyclerView): FailedDownloadItemViewHolder {
        return FailedDownloadItemViewHolder.from(parent)
    }

    override fun onBindRefreshableViewHolder(
        holder: FailedDownloadItemViewHolder,
        item: FailedDownload
    ) {
        holder.bind(item, retryActionListener, deleteActionListener)
    }

}