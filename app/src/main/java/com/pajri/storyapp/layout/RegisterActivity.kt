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
import androidx.lifecycle.ViewModelProvider
import com.pajri.storyapp.EmailValidator
import com.pajri.storyapp.R
import com.pajri.storyapp.custom.EditText
import com.pajri.storyapp.custom.MyButton
import com.pajri.storyapp.custom.PasswordEditText
import com.pajri.storyapp.databinding.ActivityRegisterBinding
import com.pajri.storyapp.viewmodel.AuthViewModel
import com.pajri.storyapp.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerButton: MyButton
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: PasswordEditText
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val authViewModel: AuthViewModel by viewModels{
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(false)

        nameEditText = findViewById(R.id.ed_register_name)
        emailEditText = findViewById(R.id.ed_register_email)
        passwordEditText = findViewById(R.id.ed_register_password)
        registerButton = findViewById(R.id.register_button)

        setListener()
        playAnimation()

        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
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

        binding.registerButton.setOnClickListener {
            if(validateName()){
                registerButton.isEnabled
                showLoading(true)
                val name = binding.edRegisterName.text.toString().trim()
                val email = binding.edRegisterEmail.text.toString().trim()
                val password = binding.edRegisterPassword.text.toString().trim()

                authViewModel.registerLauncher(name, email, password).observe(this,{
                    if(it != null){
                        Toast.makeText(this, "Register berhasil dilakukan", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                        showLoading(false)
                        startActivity(intent)
                    }else{
                        showLoading(false)
                        Toast.makeText(this, "Mohon periksa kembali data yang dimasukkan", Toast.LENGTH_SHORT).show()
                        finish()
                        startActivity(intent)
                    }
                })
            }
        }
    }

    private fun playAnimation() {

        val name = ObjectAnimator.ofFloat(binding.name, View.ALPHA, 1f).setDuration(500)
        val input_name = ObjectAnimator.ofFloat(binding.textInputName, View.ALPHA, 1f).setDuration(500)
        val login_name = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.email, View.ALPHA, 1f).setDuration(500)
        val input_email = ObjectAnimator.ofFloat(binding.textInputEmail, View.ALPHA, 1f).setDuration(500)
        val login_email = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.password, View.ALPHA, 1f).setDuration(500)
        val input_pass = ObjectAnimator.ofFloat(binding.textInputPassword, View.ALPHA, 1f).setDuration(500)
        val login_pass = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)

        val together1 = AnimatorSet().apply {
            playTogether(name,input_name,login_name)
        }
        val together2 = AnimatorSet().apply {
            playTogether(email,input_email,login_email)
        }
        val together3 = AnimatorSet().apply {
            playTogether(password,input_pass,login_pass)
        }

        AnimatorSet().apply {
            playSequentially(together1,together2,together3,register)
            start()
        }
    }

    private fun setListener() {
        with(binding){
            edRegisterName.addTextChangedListener(validateInput(edRegisterName))
        }
    }

    private fun setMyButtonEnable() {
        val password = passwordEditText.text
        registerButton.isEnabled = nameEditText.error.isNullOrEmpty() && emailEditText.error.isNullOrEmpty() && password != null && password.length >= 8
        registerButton.text = resources.getString(R.string.register)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingBar.visibility = View.VISIBLE
        }else{
            binding.loadingBar.visibility = View.GONE
        }
    }

    inner class validateInput(private val view: View): TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            when(view.id){
                R.id.ed_register_name -> {
                    validateName()
                }
            }
        }
        override fun afterTextChanged(p0: Editable?) {
        }
    }

    private fun validateName(): Boolean {
        if(binding.edRegisterName.text.toString().trim().isEmpty()){
            binding.edRegisterName.error = "Nama tidak boleh kosong"
            binding.edRegisterName.requestFocus()
            return false
        }else{
            return true
        }
    }

}