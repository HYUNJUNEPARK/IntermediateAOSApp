package com.example.tinder.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.tinder.R
import com.example.tinder.databinding.ActivityLoginBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var facebookCallbackManger: CallbackManager

    companion object {
        const val TAG = "testLog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth
        facebookCallbackManger = CallbackManager.Factory.create()

        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText

        initButtonState()
        initLoginButton()
        initSingUpButton()
        initFacebookLoginButton()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookCallbackManger.onActivityResult(requestCode, requestCode, data)
    }

    private fun initButtonState() {
        val emailTextView = emailEditText.text
        val passwordTextView = passwordEditText.text

        binding.emailEditText.addTextChangedListener {
            val isEmpty:Boolean = emailTextView.isNotEmpty() && passwordTextView.isNotEmpty()
            binding.loginButton.isEnabled = isEmpty
            binding.signUpButton.isEnabled = isEmpty
        }

        binding.passwordEditText.addTextChangedListener {
            val isEmpty:Boolean = emailTextView.isNotEmpty() && passwordTextView.isNotEmpty()
            binding.loginButton.isEnabled = isEmpty
            binding.signUpButton.isEnabled = isEmpty
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
                        Toast.makeText(this, getString(R.string.toast_login_fail), Toast.LENGTH_SHORT).show()
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
                    if (signUpTask.isSuccessful) {
                        Toast.makeText(this, getString(R.string.toast_signup_finish), Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, getString(R.string.toast_signup_fail), Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun initFacebookLoginButton() {
        binding.facebookLoginButton.setPermissions("email", "public_profile") //이메일, 프로필 정보를 가져옴(더 필요한 데이터가 있다면 페이스북 문서 참고)
        binding.facebookLoginButton.registerCallback(facebookCallbackManger, object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {//로그인 액세스 토큰을 받아 credential 을 만들어 파이어베이스에 넘김
                val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this@LoginActivity) { signInTask ->
                        if (signInTask.isSuccessful) {
                            finish()
                        }
                        else {
                            Toast.makeText(this@LoginActivity, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            override fun onCancel() { }
            override fun onError(error: FacebookException?) {
                Log.e(TAG, "onError: $error")
                Toast.makeText(this@LoginActivity, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show()
            }
        })
    }
}