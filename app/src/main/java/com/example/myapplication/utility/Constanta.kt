package com.example.myapplication.utility

object Constanta {

    enum class UserPreferences {
        UserUID, UserName, UserEmail, UserToken
    }

    enum class StoryDetail {
        UserName, ImageURL, ContentDescription, UploadTime
    }

    const val preferenceName = "Settings"
    const val preferenceDefaultValue = "Not Set"

    val emailPattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

}