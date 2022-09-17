package io.raveerocks.downloadmanager.ui.active

import io.raveerocks.downloadmanager.core.adapters.RefreshableModelListAdapter
import io.raveerocks.downloadmanager.core.models.ActiveDownload
import io.raveerocks.downloadmanager.core.models.BaseModel.ActionListener
import io.raveerocks.downloadmanager.core.views.custom.ClockedRecyclerView

class ActiveDownloadItemAdapter(private val cancelActionListener: ActionListener<ActiveDownload>) :
    RefreshableModelListAdapter<ActiveDownload, ActiveDownloadItemViewHolder>() {

    override fun onCreateRefreshableViewHolder(parent: ClockedRecyclerView): ActiveDownloadItemViewHolder {
        return ActiveDownloadItemViewHolder.from(parent)
    }

    override fun onBindRefreshableViewHolder(
        holder: ActiveDownloadItemViewHolder,
        item: ActiveDownload
    ) {
        holder.bind(item, cancelActionListener)
    }
}