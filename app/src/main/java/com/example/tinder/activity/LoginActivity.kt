package com.example.tinder.activity

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.tinder.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater)}
    private lateinit var auth: FirebaseAuth
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText

    companion object {
        const val TAG = "testLog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText

        initLoginButton()
        initSingUpButton()
        initEmailAndPasswordEditText()

    }

    private fun initEmailAndPasswordEditText() {
        val email = emailEditText.text
        val password = passwordEditText.text
        binding.emailEditText.addTextChangedListener {
            val isEnable = email.isNotEmpty() && password.isNotEmpty()
            binding.loginButton.isEnabled = isEnable
            binding.signUpButton.isEnabled = isEnable
        }
        binding.passwordEditText.addTextChangedListener {
            val isEnable = email.isNotEmpty() && password.isNotEmpty()
            binding.loginButton.isEnabled = isEnable
            binding.signUpButton.isEnabled = isEnable
        }
    }

    private fun initLoginButton() {
        binding.loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { signInTask ->
                    if (signInTask.isSuccessful) {
                        finish()
                    }
                    else {
                        Toast.makeText(this, "로그인 실패 : ID/PW 를 확인해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun initSingUpButton() {
        binding.signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { signUpTask ->
                    Log.d(TAG, "initSingUpButton: $signUpTask")
                    if (signUpTask.isSuccessful) {
                        Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}