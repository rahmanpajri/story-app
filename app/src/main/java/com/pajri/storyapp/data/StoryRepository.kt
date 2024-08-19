package com.pajri.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.pajri.storyapp.api.*
import com.pajri.storyapp.model.LoginSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(private val apiService: ApiService) {
    val mapList = MutableLiveData<List<ListStory>>()
    val regResponse = MutableLiveData<RegisterResponse?>()
    val logResponse = MutableLiveData<LoginResponse?>()


    fun registerLauncherRepo(name:String, email: String, password:String) {
        Log.d(".RegisterActivity", "$name, $email, $password")
        val launchRegister = ApiConfig.getApiService().postRegister(name, email, password)

        launchRegister.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Log.d(".RegisterActivity","Register berhasil $responseBody")
                        regResponse.postValue(responseBody)
                    }
                }else{
                    val errMess = when (response.code()) {
                        401 -> "${response.code()} : Bad Request"
                        403 -> "${response.code()} : Forbidden"
                        404 -> "${response.code()} : Not Found"
                        else -> "${response.code()} : $response"
                    }
                    Log.e(".RegisterActivity","$errMess")
                    regResponse.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(".RegisterActivity","retrofit register failure")
            }
        })
    }

    fun loginLauncherRepo(email: String, password:String) {
        val launchLogin = ApiConfig.getApiService().login(email, password)
        launchLogin.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Log.d(".RegisterActivity","Login Berhasil $responseBody")
                        logResponse.postValue(responseBody)
                    }
                }else{
                    Log.e(".RegisterActivity","Login Gagal ${response.message()}")
                    logResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(".RegisterActivity","retrofit login failure")
            }

        })
    }


    fun getRegResponse(): LiveData<RegisterResponse?> {
        return regResponse
    }

    fun getLogResponse(): LiveData<LoginResponse?> {
        return logResponse
    }

    fun getStory(loginSession: LoginSession): LiveData<PagingData<ListStory>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService,loginSession)
            }
        ).liveData
    }

    fun setMapsList(loginSession: LoginSession){
        val storiesMap = ApiConfig.getApiService().getStoriesMaps("Bearer ${loginSession.token}", null, null)
        storiesMap.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null && !responseBody.error){
                        mapList.postValue(responseBody.listStory)
                    }
                }else{
                    Log.e(".MainActivity", "Proses gagal ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Log.e(".MapsActivity", "Retrofit Error")
            }

        })
    }

    fun getMapsList(): LiveData<List<ListStory>>{
        return mapList
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstace(apiService: ApiService): StoryRepository = instance ?: synchronized(this){
            instance ?: StoryRepository(apiService)
        }.also { instance= it }
    }
}