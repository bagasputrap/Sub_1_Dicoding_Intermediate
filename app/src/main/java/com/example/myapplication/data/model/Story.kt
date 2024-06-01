package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class Story(
    // Properti untuk URL foto dalam cerita.
    @field:SerializedName("photoUrl")
    val photoUrl: String,

    // Properti untuk menunjukkan waktu pembuatan cerita.
    @field:SerializedName("createdAt")
    val createdAt: String,

    // Properti untuk menyimpan nama pengguna yang membuat cerita.
    @field:SerializedName("name")
    val name: String,

    // Properti untuk deskripsi cerita.
    @field:SerializedName("description")
    val description: String,

    // Properti untuk longitude (garis bujur) lokasi cerita.
    @field:SerializedName("lon")
    val lon: Any,

    // Properti untuk ID cerita.
    @field:SerializedName("id")
    val id: String,

    // Properti untuk latitude (garis lintang) lokasi cerita.
    @field:SerializedName("lat")
    val lat: Any
)
