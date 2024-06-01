package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class Regist(
    // Properti untuk menunjukkan apakah ada kesalahan atau tidak saat melakukan registrasi.
    @field:SerializedName("error")
    val error: Boolean,

    // Properti untuk menyimpan pesan yang memberikan informasi tentang hasil registrasi.
    @field:SerializedName("message")
    val message: String
)
