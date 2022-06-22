package com.june.pushalarmreceiver.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.june.pushalarmreceiver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    companion object {
        const val TAG = "testLog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkFCMToken()
        updateResult()
    }

    //서버로 부터 알림이 오고 알림을 눌렀을 때마다 실행됨
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent) //onCreate 했을 때 가져온 기존 인텐트를 새로 들어온 걸로 교체
        updateResult(true)
    }

    private fun checkFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if(task.isSuccessful) {
                binding.firebaseTokenTextView.text = task.result
                Log.d(TAG, "[Firebase Token] : ${task.result}")
            }
        }
    }

    private fun updateResult(isNewIntent: Boolean = false) {
        //isNewIntent : 앱이 꺼져있다가 새로 실행되었는가 앱이 기존에 켜져있었는데 알림을 눌러 갱신했느냐를 구분
        binding.resultTextView.text = intent.getStringExtra("notificationType") ?: "앱 아이콘" +
            if (isNewIntent) {
                "으로 갱신했습니다"
            }
            else {
                "으로 실행했습니다"
            }
    }
}