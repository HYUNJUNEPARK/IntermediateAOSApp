package com.example.a3_wisesayingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a3_wisesayingapp.databinding.ItemQuoteBinding
import com.example.a3_wisesayingapp.module.Quote

class QuotesPagerAdapter(private val quotes: List<Quote>): RecyclerView.Adapter<QuotesPagerAdapter.QuoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding = ItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(quotes[position])
    }

    override fun getItemCount(): Int {
        return quotes.size
    }

    inner class QuoteViewHolder(private val binding: ItemQuoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(quote:Quote) {
            binding.quoteTextView.text = quote.quote
            binding.nameTextView.text = quote.name
        }
    }
}