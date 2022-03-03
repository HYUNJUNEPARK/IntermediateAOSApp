package com.example.a3_wisesayingapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a3_wisesayingapp.databinding.ItemQuoteBinding
import com.example.a3_wisesayingapp.model.Quote

class QuotesPagerAdapter(
    private val quotes: List<Quote>,
    private val isNameRevealed: Boolean
): RecyclerView.Adapter<QuotesPagerAdapter.QuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding = ItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(quotes[position], isNameRevealed)
    }

    override fun getItemCount(): Int {
        return quotes.size
    }

    inner class QuoteViewHolder(private val binding: ItemQuoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(quote:Quote, isNameRevealed: Boolean) {
            binding.quoteTextView.text = quote.quote

            if (isNameRevealed) {
                binding.nameTextView.text = quote.name
                binding.nameTextView.visibility = View.VISIBLE
            }
            else {
                binding.nameTextView.visibility = View.GONE
            }
        }
    }
}