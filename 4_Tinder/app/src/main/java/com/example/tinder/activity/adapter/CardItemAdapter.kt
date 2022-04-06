package com.example.tinder.activity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tinder.activity.model.CardItem
import com.example.tinder.databinding.ItemCardBinding

class CardItemAdapter: ListAdapter<CardItem, CardItemAdapter.ViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ItemCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(cardItem: CardItem) {
            binding.nameTextView.text = cardItem.name
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CardItem>() {
            override fun areItemsTheSame(oldItem: CardItem, newItem: CardItem): Boolean {
                return oldItem.userId == newItem.userId
            }
            override fun areContentsTheSame(oldItem: CardItem, newItem: CardItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}