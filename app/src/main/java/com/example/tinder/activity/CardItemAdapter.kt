package com.example.tinder.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tinder.R
import com.example.tinder.activity.model.CardItem
import com.example.tinder.databinding.ItemCardBinding

class CardItemAdapter: ListAdapter<CardItem, CardItemAdapter.ViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardItemAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        //val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //return ViewHolder(binding)

        return ViewHolder(inflater.inflate(R.layout.item_card, parent, false))
    }

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        fun bind(cardItem: CardItem) {
            //TODO Binding
            view.findViewById<TextView>(R.id.nameTextView).text = cardItem.name
        }
    }

    override fun onBindViewHolder(holder: CardItemAdapter.ViewHolder, position: Int) {
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