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
): RecyclerView.Adapter<QuotesPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actualPositionIndex = position % quotes.size //quotes 포지션을 넘어가면 부터 시작해 뷰페이저를 반복할 수 있음
        holder.bindData(quotes[actualPositionIndex], isNameRevealed)
    }

    override fun getItemCount(): Int {
        //return quotes.size 뷰페이저의 끝이 있음
        return Int.MAX_VALUE //계속하다보면 페이지의 끝은 오지만 현실적으로 가능성이 매우 낮음
    }

    inner class ViewHolder(private val binding: ItemQuoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(quote:Quote, isNameRevealed: Boolean) {
            binding.quoteTextView.text = "\"${quote.quote}\""

            if (isNameRevealed) {
                binding.nameTextView.text = "- ${quote.name} -"
                binding.nameTextView.visibility = View.VISIBLE
            }
            else {
                binding.nameTextView.visibility = View.GONE
            }
        }
    }
}