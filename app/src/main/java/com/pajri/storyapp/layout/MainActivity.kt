package com.pajri.storyapp.layout

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import com.pajri.storyapp.R
import com.pajri.storyapp.SessionPreference
import com.pajri.storyapp.custom.MyButton
import com.pajri.storyapp.custom.EditText
import com.pajri.storyapp.custom.PasswordEditText
import com.pajri.storyapp.databinding.ActivityMainBinding
import com.pajri.storyapp.model.LoginSession
import com.pajri.storyapp.viewmodel.AuthViewModel
import com.pajri.storyapp.viewmodel.HomeViewModel
import com.pajri.storyapp.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var loginButton: MyButton
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: PasswordEditText
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val authViewModel: AuthViewModel by viewModels{
        factory
    }
    private lateinit var mSessionPreference: SessionPreference
    private lateinit var loginSession: LoginSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginButton = findViewById(R.id.login_button)
        emailEditText = findViewById(R.id.ed_login_email)
        passwordEditText = findViewById(R.id.ed_login_password)
        mSessionPreference = SessionPreference(this)
        setMyButtonEnable()
        showLoading(false)
        playAnimation()

        if (mSessionPreference.getSession().name != "") {
            val newLoginSession= mSessionPreference.getSession()
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            intent.putExtra(HomeActivity.EXTRA_RESULT, newLoginSession)
            startActivity(intent)
            finish()
        }

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        loginButton.setOnClickListener {
            showLoading(true)
            if(emailEditText.error == null && passwordEditText.error == null){
                val email = binding.edLoginEmail.text.toString().trim()
                val password = binding.edLoginPassword.text.toString().trim()
                authViewModel.loginLauncher(email, password).observe(this,{
                    if(it != null){
                        if(it.error == true){
                            showLoading(false)
                            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                        }else{
                            saveSession(it.loginResult.userId,it.loginResult.name,it.loginResult.token)
                        }
                    }else{
                        showLoading(false)
                        Toast.makeText(this, "Login gagal, Mohon periksa kembali data", Toast.LENGTH_SHORT).show()
                        finish()
                        startActivity(intent)
                    }
                })
            }
        }

        binding.toRegister.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun playAnimation() {

        val email = ObjectAnimator.ofFloat(binding.email, View.ALPHA, 1f).setDuration(500)
        val input_email = ObjectAnimator.ofFloat(binding.textInputEmail, View.ALPHA, 1f).setDuration(500)
        val login_email = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.password, View.ALPHA, 1f).setDuration(500)
        val input_pass = ObjectAnimator.ofFloat(binding.textInputPassword, View.ALPHA, 1f).setDuration(500)
        val login_pass = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val no_acc = ObjectAnimator.ofFloat(binding.noAcc, View.ALPHA, 1f).setDuration(500)
        val to_register = ObjectAnimator.ofFloat(binding.toRegister, View.ALPHA, 1f).setDuration(500)

        val together1 = AnimatorSet().apply {
            playTogether(email,input_email,login_email)
        }
        val together2 = AnimatorSet().apply {
            playTogether(password,input_pass,login_pass)
        }
        val together3 = AnimatorSet().apply {
            playTogether(no_acc,to_register)
        }

        AnimatorSet().apply {
            playSequentially(together1,together2,login,together3)
            start()
        }
    }

    private fun setMyButtonEnable() {
        val password = passwordEditText.text
        loginButton.isEnabled = emailEditText.error.isNullOrEmpty() && password != null && password.length >= 8
        loginButton.text = resources.getString(R.string.login)
    }

    private fun saveSession(userId: String, name: String, token: String) {
        mSessionPreference = SessionPreference(this)

        loginSession = LoginSession()
        loginSession.userId = userId
        loginSession.name = name
        loginSession.token = token

        mSessionPreference.setSession(loginSession)
        moveToHome()
    }

    private fun moveToHome() {
        mSessionPreference = SessionPreference(this)
        val newLoginSession = mSessionPreference.getSession()
        val intent = Intent(this@MainActivity, HomeActivity::class.java)
        Toast.makeText(this, "Welcome ${newLoginSession.name}", Toast.LENGTH_SHORT).show()
        intent.putExtra(HomeActivity.EXTRA_RESULT, newLoginSession)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingBar.visibility = View.VISIBLE
        }else{
            binding.loadingBar.visibility = View.GONE
        }
    }
}