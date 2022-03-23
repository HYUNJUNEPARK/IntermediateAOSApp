package com.example.a3_wisesayingapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.a3_wisesayingapp.adapter.QuotesPagerAdapter
import com.example.a3_wisesayingapp.databinding.ActivityMainBinding
import com.example.a3_wisesayingapp.model.Quote
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initData()
        initViewEffect()
    }

    private fun initViewEffect() {
        binding.viewPager.setPageTransformer { page, position ->
            when {
                position.absoluteValue >= 1F -> page.alpha = 0F //투명 absoluteValue(절댓갑 1 -> 1, -1 -> 1)
                position == 0F -> page.alpha = 1F //보임
                else -> page.alpha = 1F - (2 * position.absoluteValue)
            }
        }
    }

    private fun initData() {
        val remoteConfig = Firebase.remoteConfig

        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            binding.progressBar.visibility = View.GONE

            if (task.isSuccessful) {
                val quotes = parseFirebaseJson(remoteConfig.getString("quotes"))
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")
                setViewAdapter(quotes, isNameRevealed)
            }
        }
    }

    private fun parseFirebaseJson(json: String): List<Quote> {
        val jsonArray = JSONArray(json)
        var jsonList = emptyList<JSONObject>()

        for(index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let { JSONObject ->
                jsonList += JSONObject
            }
        }

        return jsonList.map { JSONObject ->
            Quote(
                JSONObject.getString("quote"),
                JSONObject.getString("name")
            )
        }
    }

    private fun setViewAdapter(quotes: List<Quote>, isNameRevealed: Boolean) {
        val adapter = QuotesPagerAdapter(
            quotes = quotes,
            isNameRevealed = isNameRevealed
        )
        binding.viewPager.adapter = adapter
        val startPosition = adapter.itemCount/2
        binding.viewPager.setCurrentItem(startPosition, /*smoothScroll*/false) //true 라면 앱 시작 시 startPosition 까지 이동하는 게 보임
    }
}