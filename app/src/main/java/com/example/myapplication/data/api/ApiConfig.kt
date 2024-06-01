package com.example.myapplication.data.api

import com.example.myapplication.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object { // Mendefinisikan companion object, yang memungkinkan untuk memiliki fungsi-fungsi dan properti-properati statis di dalam kelas Kotlin.
        fun getApiService(): ApiService { // Mendefinisikan sebuah fungsi statis bernama getApiService yang mengembalikan sebuah objek dari tipe ApiService.
            val loggingInterceptor = HttpLoggingInterceptor().apply { // Membuat interceptor untuk logging HTTP request dan response.
                level = if (BuildConfig.DEBUG) { // Memeriksa apakah aplikasi sedang dalam mode debug.
                    HttpLoggingInterceptor.Level.BODY // Jika ya, atur tingkat logging menjadi BODY.
                } else {
                    HttpLoggingInterceptor.Level.NONE // Jika tidak, atur tingkat logging menjadi NONE.
                }
            }

            val client = OkHttpClient.Builder() // Membuat instance dari OkHttpClient yang digunakan Retrofit untuk melakukan HTTP requests.
                .addInterceptor(loggingInterceptor) // Menambahkan logging interceptor ke OkHttpClient.
                .build() // Membangun OkHttpClient.

            val retrofit = Retrofit.Builder().baseUrl(BuildConfig.URL) // Membuat instance dari Retrofit dengan base URL dari konfigurasi build.
                .addConverterFactory(GsonConverterFactory.create()) // Menambahkan GsonConverterFactory untuk mengonversi JSON menjadi objek Kotlin.
                .client(client) // Menetapkan OkHttpClient yang telah dikonfigurasi ke Retrofit.
                .build() // Membangun Retrofit.

            return retrofit.create(ApiService::class.java) // Mengembalikan instance dari ApiService yang sudah dikonfigurasi dengan Retrofit.
        }
    }
}
