package com.example.myapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Phone

class PhoneListAdapter(private val onItemClicked: (Phone) -> Unit) :
    ListAdapter<Phone, PhoneListAdapter.PhoneViewHolder>(PhonesDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false) // We'll need a new layout file
        return PhoneViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhoneViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
    }

    class PhoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val phoneItemView: TextView = itemView.findViewById(R.id.textViewItem) // Assume R.id.textViewItem in recyclerview_item.xml

        fun bind(phone: Phone) {
            phoneItemView.text = phone.phoneNumber
        }
    }

    companion object PhonesDiffCallback : DiffUtil.ItemCallback<Phone>() {
        override fun areItemsTheSame(oldItem: Phone, newItem: Phone): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Phone, newItem: Phone): Boolean {
            return oldItem == newItem
        }
    }
}
