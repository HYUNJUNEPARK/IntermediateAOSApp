package com.example.a3_wisesayingapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a3_wisesayingapp.adapter.QuotesPagerAdapter
import com.example.a3_wisesayingapp.databinding.ActivityMainBinding
import com.example.a3_wisesayingapp.model.Quote
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

//    private val sampleData: List<Quote> = arrayListOf(
//        Quote("sample saying 1","sample speaker 1"),
//        Quote("sample saying 2","sample speaker 2"),
//        Quote("sample saying 3","sample speaker 3")
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        initView()
        initData()
    }

//    private fun initView() {
//
//    }

    private fun initData() {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")

                displayQuotesPager(quotes, isNameRevealed)
            }
        }
    }

    private fun parseQuotesJson(json: String): List<Quote> {
        val jsonArray = JSONArray(json)
        var jsonList = emptyList<JSONObject>()
        for(index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let { JSONObject ->
                jsonList = jsonList + JSONObject
            }
        }
        return jsonList.map { JSONObject ->
            Quote(
                JSONObject.getString("quote"),
                JSONObject.getString("name")
            )
        }
    }

    private fun displayQuotesPager(quotes: List<Quote>, isNameRevealed: Boolean) {
        binding.viewPager.adapter = QuotesPagerAdapter(
            quotes = quotes,
            isNameRevealed = isNameRevealed
        )
    }
}