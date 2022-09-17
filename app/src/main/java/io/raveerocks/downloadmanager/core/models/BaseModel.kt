package io.raveerocks.downloadmanager.core.models

import androidx.recyclerview.widget.DiffUtil

abstract class BaseModel(private val id: Long) {

    class ActionListener<T : BaseModel>(private val actionListener: (t: T) -> Unit) {
        fun onAction(t: T) {
            actionListener(t)
        }
    }

    class ItemDiffCallback<T : BaseModel> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }
    }
}