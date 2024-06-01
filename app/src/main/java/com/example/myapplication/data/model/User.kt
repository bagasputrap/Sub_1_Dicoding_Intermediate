package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class User(
    // Properti untuk menyimpan nama pengguna.
    @field:SerializedName("name")
    val name: String,

    // Properti untuk menyimpan ID pengguna.
    @field:SerializedName("userId")
    val userId: String,

    // Properti untuk menyimpan token otentikasi pengguna.
    @field:SerializedName("token")
    val token: String
)
