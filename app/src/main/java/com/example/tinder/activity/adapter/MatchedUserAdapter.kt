package com.example.tinder.activity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tinder.activity.CardItemAdapter
import com.example.tinder.activity.model.CardItem
import com.example.tinder.databinding.ItemCardBinding
import com.example.tinder.databinding.ItemMatchedUserBinding

class MatchedUserAdapter: ListAdapter<CardItem, MatchedUserAdapter.ViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchedUserAdapter.ViewHolder {

        val binding = ItemMatchedUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ItemMatchedUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(cardItem: CardItem) {
            binding.userNameTextView.text = cardItem.name
        }
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}