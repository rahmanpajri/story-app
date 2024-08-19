package com.pajri.storyapp

import android.util.Patterns

object EmailValidator {
    fun checkEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}