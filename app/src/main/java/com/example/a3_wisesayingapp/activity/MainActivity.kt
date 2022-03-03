package com.example.a3_wisesayingapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a3_wisesayingapp.R
import com.example.a3_wisesayingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}