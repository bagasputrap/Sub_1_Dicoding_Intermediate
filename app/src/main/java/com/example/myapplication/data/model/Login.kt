package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class Login(
    @field:SerializedName("loginResult")
    val loginResult: User, // Properti loginResult adalah hasil login dari jenis User.

    @field:SerializedName("error")
    val error: Boolean, // Properti error menunjukkan apakah ada kesalahan atau tidak.

    @field:SerializedName("message")
    val message: String // Properti message adalah pesan yang menjelaskan hasil operasi login.
)
