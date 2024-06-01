package com.example.myapplication.data.api

import com.example.myapplication.data.model.Login
import com.example.myapplication.data.model.Regist
import com.example.myapplication.data.model.ListStory
import com.example.myapplication.data.model.UploadStory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("login") // Endpoint untuk melakukan login.
    @FormUrlEncoded // Menggunakan form-urlencoded.
    fun doLogin(
        @Field("email") email: String, // Parameter email.
        @Field("password") password: String // Parameter password.
    ): Call<Login> // Mengembalikan objek Login.

    @POST("register") // Endpoint untuk melakukan registrasi.
    @FormUrlEncoded // Menggunakan form-urlencoded.
    fun doRegister(
        @Field("name") name: String, // Parameter name.
        @Field("email") email: String, // Parameter email.
        @Field("password") password: String // Parameter password.
    ): Call<Regist> // Mengembalikan objek Regist.

    @GET("stories") // Endpoint untuk mendapatkan daftar cerita.
    fun getStoryList(
        @Header("Authorization") token:String, // Token otentikasi.
        @Query("size") size:Int // Ukuran daftar cerita yang diinginkan.
    ): Call<ListStory> // Mengembalikan objek Storylist.

    @Multipart // Menggunakan multipart form data.
    @POST("stories") // Endpoint untuk mengunggah gambar cerita.
    fun doUploadImage(
        @Header("Authorization") token:String, // Token otentikasi.
        @Part file: MultipartBody.Part, // Bagian berkas gambar.
        @Part("description") description: RequestBody // Deskripsi cerita.
    ): Call<UploadStory> // Mengembalikan objek Storyupload.
}
