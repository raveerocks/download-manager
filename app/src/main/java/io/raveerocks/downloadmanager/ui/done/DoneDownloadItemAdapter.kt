package io.raveerocks.downloadmanager.ui.done

import io.raveerocks.downloadmanager.core.adapters.RefreshableModelListAdapter
import io.raveerocks.downloadmanager.core.models.BaseModel.ActionListener
import io.raveerocks.downloadmanager.core.models.DoneDownload
import io.raveerocks.downloadmanager.core.views.custom.ClockedRecyclerView

class DoneDownloadItemAdapter(
    private val openActionListener: ActionListener<DoneDownload>,
    private val infoActionListener: ActionListener<DoneDownload>,
    private val deleteActionListener: ActionListener<DoneDownload>,
) : RefreshableModelListAdapter<DoneDownload, DoneDownloadItemViewHolder>() {

    override fun onCreateRefreshableViewHolder(parent: ClockedRecyclerView): DoneDownloadItemViewHolder {
        return DoneDownloadItemViewHolder.from(parent)
    }

    override fun onBindRefreshableViewHolder(
        holder: DoneDownloadItemViewHolder,
        item: DoneDownload
    ) {
        holder.bind(item, openActionListener, infoActionListener, deleteActionListener)
    }

}