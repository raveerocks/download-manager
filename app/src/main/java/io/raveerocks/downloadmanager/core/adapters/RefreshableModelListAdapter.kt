package io.raveerocks.downloadmanager.core.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import io.raveerocks.downloadmanager.core.models.BaseModel
import io.raveerocks.downloadmanager.core.views.custom.ClockedRecyclerView

abstract class RefreshableModelListAdapter<T1 : BaseModel, T2 : ClockedRecyclerView.ViewHolder> :
    ListAdapter<T1, T2>(BaseModel.ItemDiffCallback<T1>()) {

    abstract fun onCreateRefreshableViewHolder(parent: ClockedRecyclerView): T2
    abstract fun onBindRefreshableViewHolder(holder: T2, item: T1)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T2 {
        return onCreateRefreshableViewHolder(parent as ClockedRecyclerView)
    }

    override fun onBindViewHolder(holder: T2, position: Int) {
        onBindRefreshableViewHolder(holder, getItem(position))
    }
}