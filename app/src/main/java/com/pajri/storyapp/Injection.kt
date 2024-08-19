package com.pajri.storyapp

import android.content.Context
import com.pajri.storyapp.api.ApiConfig
import com.pajri.storyapp.data.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstace(apiService)
    }
}