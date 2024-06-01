package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ListStory(
    // Properti untuk daftar cerita.
    @field:SerializedName("listStory")
    val listStory: List<Story>,

    // Properti untuk menunjukkan apakah ada kesalahan atau tidak saat mendapatkan daftar cerita.
    @field:SerializedName("error")
    val error: Boolean,

    // Properti untuk menyimpan pesan yang memberikan informasi tentang hasil operasi mendapatkan daftar cerita.
    @field:SerializedName("message")
    val message: String
)
