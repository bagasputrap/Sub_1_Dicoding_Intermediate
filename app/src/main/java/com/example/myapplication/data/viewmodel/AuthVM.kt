package com.example.myapplication.data.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.api.ApiConfig
import com.example.myapplication.data.model.Login
import com.example.myapplication.data.model.Regist
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthVM(val context: Context) : ViewModel() {

    var loading = MutableLiveData(View.GONE)
    val error = MutableLiveData("")
    val tempEmail = MutableLiveData("") // Hold email to saved with user preferences

    /* Handle API response */
    val loginResult = MutableLiveData<Login>()
    val registerResult = MutableLiveData<Regist>()

    private val TAG = AuthVM::class.simpleName

    fun login(email: String, password: String) {
        tempEmail.postValue(email)
        loading.postValue(View.VISIBLE)
        val client = ApiConfig.getApiService().doLogin(email, password)
        client.enqueue(object : Callback<Login> {
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                when (response.code()) {
                    400 -> error.postValue(context.getString(R.string.error_email_invalid))
                    401 -> error.postValue(context.getString(R.string.error_unauthorized))
                    200 -> loginResult.postValue(response.body())
                    else -> error.postValue("ERROR ${response.code()} : ${response.message()}")
                }
                loading.postValue(View.GONE)
            }

            override fun onFailure(call: Call<Login>, t: Throwable) {
                loading.postValue(View.GONE)
                Log.e(TAG, "onFailure Call: ${t.message}")
                error.postValue(t.message)
            }
        })
    }

    fun register(name: String, email: String, password: String) {
        loading.postValue(View.VISIBLE)
        val client = ApiConfig.getApiService().doRegister(name, email, password)
        client.enqueue(object : Callback<Regist> {
            override fun onResponse(call: Call<Regist>, response: Response<Regist>) {
                when (response.code()) {
                    400 -> error.postValue(context.getString(R.string.error_email_invalid))
                    201 -> registerResult.postValue(response.body())
                    else -> error.postValue("ERROR ${response.code()} : ${response.errorBody()}")
                }
                loading.postValue(View.GONE)
            }

            override fun onFailure(call: Call<Regist>, t: Throwable) {
                loading.postValue(View.GONE)
                Log.e(TAG, "onFailure Call: ${t.message}")
                error.postValue(t.message)
            }
        })
    }
}