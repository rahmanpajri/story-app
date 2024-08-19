package com.pajri.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.pajri.storyapp.api.ApiConfig
import com.pajri.storyapp.api.ListStory
import com.pajri.storyapp.api.StoriesResponse
import com.pajri.storyapp.data.StoryRepository
import com.pajri.storyapp.model.LoginSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun getStories(loginSession: LoginSession): LiveData<PagingData<ListStory>> {
        return storyRepository.getStory(loginSession)
    }
}