package com.pajri.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pajri.storyapp.api.LoginResponse
import com.pajri.storyapp.api.RegisterResponse
import com.pajri.storyapp.data.StoryRepository

class AuthViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun registerLauncher(name:String, email: String, password:String):LiveData<RegisterResponse?> {
        storyRepository.registerLauncherRepo(name, email, password)
        return storyRepository.getRegResponse()
    }

    fun loginLauncher(email: String, password:String) :LiveData<LoginResponse?>{
        storyRepository.loginLauncherRepo(email,password)
        return storyRepository.getLogResponse()
    }
}