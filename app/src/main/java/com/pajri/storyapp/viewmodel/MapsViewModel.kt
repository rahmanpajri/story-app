package com.pajri.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pajri.storyapp.api.ListStory
import com.pajri.storyapp.data.StoryRepository
import com.pajri.storyapp.model.LoginSession

class MapsViewModel(private val storyRepository: StoryRepository):ViewModel() {
    fun getMapsList(loginSession: LoginSession): LiveData<List<ListStory>>{
        storyRepository.setMapsList(loginSession)
        return storyRepository.getMapsList()
    }
}