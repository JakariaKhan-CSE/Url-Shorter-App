package com.example.glyde.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.glyde.R
import com.example.glyde.data.local.HistoryEntity

class HistoryAdapter(private val onCopyClick: (String) -> Unit) :
    ListAdapter<HistoryEntity, HistoryAdapter.HistoryViewHolder>(HistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view, onCopyClick)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HistoryViewHolder(itemView: View, private val onCopyClick: (String) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val tvOriginalUrl: TextView = itemView.findViewById(R.id.tvOriginalUrl)
        private val tvShortUrl: TextView = itemView.findViewById(R.id.tvShortUrl)

        fun bind(item: HistoryEntity) {
            tvOriginalUrl.text = item.originalUrl
            tvShortUrl.text = item.shortUrl
            itemView.setOnClickListener {
                onCopyClick(item.shortUrl)
            }
        }
    }

    class HistoryDiffCallback : DiffUtil.ItemCallback<HistoryEntity>() {
        override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem == newItem
        }
    }
}
