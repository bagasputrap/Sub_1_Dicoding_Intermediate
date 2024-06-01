package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class UploadStory(
    // Properti untuk menunjukkan apakah terjadi kesalahan atau tidak saat mengunggah cerita.
    @field:SerializedName("error")
    val error: Boolean,

    // Properti untuk menyimpan pesan yang memberikan informasi tentang hasil operasi mengunggah cerita.
    @field:SerializedName("message")
    val message: String
)
