package com.example.a3_wisesayingapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a3_wisesayingapp.R
import com.example.a3_wisesayingapp.adapter.QuotesPagerAdapter
import com.example.a3_wisesayingapp.databinding.ActivityMainBinding
import com.example.a3_wisesayingapp.module.Quote

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val sampleData: List<Quote> = arrayListOf(
        Quote("sample saying 1","sample speaker 1"),
        Quote("sample saying 2","sample speaker 2"),
        Quote("sample saying 3","sample speaker 3")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.viewPager.adapter = QuotesPagerAdapter(sampleData)
    }
}