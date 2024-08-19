package com.pajri.storyapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.pajri.storyapp.api.ListStory
import com.pajri.storyapp.api.LoginResponse
import com.pajri.storyapp.api.LoginResult
import com.pajri.storyapp.api.RegisterResponse

object DataDummy{
    fun generateDummyStoryResponse(): List<ListStory>{
        val items: MutableList<ListStory> = arrayListOf()
        for(list in 0..5){
            val story = ListStory(
                "url $list",
                "dateFormat $list",
                "name $list",
                "description $list",
                list.toDouble(),
                list.toString(),
                list.toDouble()
            )
            items.add(story)
        }
        return items
    }

    fun generateDummyRegisterResponse():LiveData<RegisterResponse?> = liveData {
        val registerResponse = RegisterResponse(
            false,
            "Pesan Dummy"
        )
        registerResponse
    }

    fun generateDummyLoginResponse():LiveData<LoginResponse?> = liveData {
        val loginResponse = LoginResponse(
            false,
            "Pesan Dummy",
            LoginResult(
                "user-Id",
                "name",
                "token"
            )
        )
        loginResponse
    }

    fun generateDummyMapsList():LiveData<List<ListStory>> = liveData {
        val mapsList: MutableList<ListStory> = arrayListOf()
        for (i in 0..5){
            val story = ListStory(
                "url $i",
                "dateFormat $i",
                "name $i",
                "description $i",
                i.toDouble(),
                i.toString(),
                i.toDouble()
            )
            mapsList.add(story)
        }
        mapsList
    }
}