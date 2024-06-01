package com.example.myapplication.data.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.myapplication.R
import com.example.myapplication.data.api.ApiConfig
import com.example.myapplication.data.model.Story
import com.example.myapplication.data.model.ListStory
import com.example.myapplication.data.model.UploadStory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryVM(val context: Context) : ViewModel() {
    var loading = MutableLiveData(View.GONE)
    var isSuccessUploadStory = MutableLiveData(false)
    val storyList = MutableLiveData<List<Story>>()
    var error = MutableLiveData("")
    private val TAG = StoryVM::class.simpleName

    fun loadStoryData(token: String) {
        loading.postValue(View.VISIBLE)
        val client = ApiConfig.getApiService().getStoryList(token, 30)
        client.enqueue(object : Callback<ListStory> {
            override fun onResponse(call: Call<ListStory>, response: Response<ListStory>) {
                if (response.isSuccessful) {
                    storyList.postValue(response.body()?.listStory)
                } else {
                    error.postValue("ERROR ${response.code()} : ${response.message()}")
                }
                loading.postValue(View.GONE)
            }

            override fun onFailure(call: Call<ListStory>, t: Throwable) {
                loading.postValue(View.GONE)
                Log.e(TAG, "onFailure Call: ${t.message}")
                error.postValue("${context.getString(R.string.error_fetch_data)} : ${t.message}")
            }
        })
    }

    fun uploadNewStory(token: String, image: File, description: String) {
        loading.postValue(View.VISIBLE)
        "${image.length() / 1024 / 1024} MB" // manual parse from bytes to Mega Bytes
        val storyDescription = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            image.name,
            requestImageFile
        )
        val client =
            ApiConfig.getApiService().doUploadImage(token, imageMultipart, storyDescription)
        client.enqueue(object : Callback<UploadStory> {
            override fun onResponse(call: Call<UploadStory>, response: Response<UploadStory>) {
                when (response.code()) {
                    401 -> error.postValue(context.getString(R.string.error_header_token))
                    413 -> error.postValue(context.getString(R.string.error_large_payload))
                    201 -> isSuccessUploadStory.postValue(true)
                    else -> error.postValue("Error ${response.code()} : ${response.message()}")
                }
                loading.postValue(View.GONE)
            }

            override fun onFailure(call: Call<UploadStory>, t: Throwable) {
                loading.postValue(View.GONE)
                Log.e(TAG, "onFailure Call: ${t.message}")
                error.postValue("${context.getString(R.string.error_send_payload)} : ${t.message}")
            }
        })
    }
}